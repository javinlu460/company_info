# 企业官网系统生产环境部署文档

> 适用版本：company-admin + company-web + company-cms
> 更新时间：2026-08-26

## 一、环境要求

| 组件 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 1.8+ | 运行 Spring Boot 后端 |
| MySQL | 8.0 | 数据存储 |
| Nginx | 1.20+ | 反向代理 + 静态资源服务 |
| Node.js | 16+ | 前端构建 |
| Maven | 3.6+ | 后端构建 |

**最低服务器配置（小型官网）**：
- 2 vCPU / 2 GB 内存 / 40 GB 磁盘 / 3 Mbps 带宽

**推荐配置**：
- 2 vCPU / 4 GB 内存 / 60 GB 磁盘 / 3~5 Mbps 带宽

---

## 二、服务器目录规划

```
/www/company-web/dist       # 前台官网静态文件
/www/company-cms/dist       # 后台管理系统静态文件
/www/server/company-admin   # 后端 JAR 及启动脚本
/data/company_info/uploads  # 文件上传目录
/www/server/panel/vhost/nginx/company.conf  # Nginx 站点配置（宝塔）
```

---

## 三、安全组配置

在阿里云控制台，为 ECS 实例添加安全组规则：

| 端口 | 用途 | 授权对象 |
|------|------|---------|
| 80 | HTTP 访问 | 0.0.0.0/0 |
| 443 | HTTPS（可选） | 0.0.0.0/0 |
| 8080 | 后端 API（建议只开放内网或本地） | 127.0.0.1/32 |
| 22 | SSH 远程连接 | 你的 IP |

**建议**：8080 端口不要对外开放，Nginx 通过 `127.0.0.1:8080` 反向代理访问后端。

---

## 四、MySQL 安装与初始化

### 4.1 安装 MySQL（Ubuntu 22.04）

```bash
sudo apt update
sudo apt install -y mysql-server
sudo systemctl enable mysql
sudo systemctl start mysql
```

### 4.2 创建数据库和用户

```bash
sudo mysql -u root
```

```sql
CREATE DATABASE company_info CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'company_info'@'localhost' IDENTIFIED BY '你的强密码';
GRANT ALL PRIVILEGES ON company_info.* TO 'company_info'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 4.3 导入表结构和初始数据

将本地 `sql/init.sql` 上传到服务器，然后执行：

```bash
mysql -u company_info -p company_info < /path/to/init.sql
```

---

## 五、后端打包与部署

### 5.1 本地打包

在项目根目录执行：

```bash
cd company-admin
mvn clean package -DskipTests
```

打包产物：

```
company-admin/target/company-admin-1.0.0.jar
```

### 5.2 上传到服务器

```bash
scp company-admin/target/company-admin-1.0.0.jar root@你的服务器IP:/www/server/company-admin/
```

### 5.3 生产环境配置

后端使用 `application-prod.yml`，关键配置如下：

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/company_info?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=true
    username: company_info
    password: 你的数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver

file:
  upload-path: /data/company_info/uploads/
```

**配置方式二选一**：

#### 方式 A：直接修改配置文件（不推荐，密码会打包进 JAR）

修改 `company-admin/src/main/resources/application-prod.yml` 后重新打包。

#### 方式 B：使用环境变量（推荐）

启动时传入环境变量：

```bash
export PROD_DB_HOST="localhost"
export PROD_DB_PORT="3306"
export PROD_DB_NAME="company_info"
export PROD_DB_USERNAME="company_info"
export PROD_DB_PASSWORD="你的数据库密码"
export PROD_UPLOAD_PATH="/data/company_info/uploads/"
export JWT_SECRET="自定义的JWT密钥，建议32位以上随机字符串"
```

### 5.4 创建上传目录

```bash
mkdir -p /data/company_info/uploads
chmod -R 755 /data/company_info/uploads
```

### 5.5 启动后端

#### 前台启动（调试时用）

```bash
cd /www/server/company-admin
java -jar -Xms256m -Xmx512m company-admin-1.0.0.jar --spring.profiles.active=prod
```

#### 后台启动（生产推荐）

```bash
cd /www/server/company-admin
nohup java -jar -Xms256m -Xmx512m company-admin-1.0.0.jar --spring.profiles.active=prod > nohup.out 2>&1 &
```

### 5.6 停止后端

```bash
ps -ef | grep company-admin-1.0.0.jar
kill -9 <PID>
```

---

## 六、前端打包与部署

### 6.1 前台官网 company-web

```bash
cd company-web
npm install
npm run build
```

打包产物在 `company-web/dist`，上传到服务器：

```bash
scp -r company-web/dist root@你的服务器IP:/www/company-web/
```

### 6.2 后台管理系统 company-cms

```bash
cd company-cms
npm install
npm run build
```

> 注意：`company-cms/vite.config.js` 中已配置 `base: '/admin/'`，所以构建出的资源路径会带 `/admin/` 前缀。

打包产物在 `company-cms/dist`，上传到服务器：

```bash
scp -r company-cms/dist root@你的服务器IP:/www/company-cms/
```

### 6.3 目录权限

```bash
chmod -R 755 /www/company-web/dist
chmod -R 755 /www/company-cms/dist
```

---

## 七、Nginx 配置

### 7.1 配置站点

将 `deploy/company.conf` 上传到 Nginx 站点配置目录：

- **宝塔面板**：`/www/server/panel/vhost/nginx/company.conf`
- **原生 Nginx**：`/etc/nginx/conf.d/company.conf`

配置文件内容：

```nginx
server {
    listen 80 default_server;
    server_name _;

    # 前台官网
    location / {
        root /www/company-web/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后台管理系统
    location /admin {
        alias /www/company-cms/dist;
        index index.html;
        try_files $uri $uri/ /admin/index.html;
    }

    # 后台静态资源
    location /admin/assets/ {
        alias /www/company-cms/dist/assets/;
    }

    # 后端 API 代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 上传文件访问
    location /uploads/ {
        alias /data/company_info/uploads/;
    }
}
```

### 7.2 测试并重载 Nginx

```bash
nginx -t
nginx -s reload
```

宝塔面板用户也可以在「网站」→「设置」→「配置文件」中粘贴后保存。

---

## 八、验证部署

| 地址 | 预期结果 |
|------|---------|
| `http://你的服务器IP/` | 显示前台官网首页 |
| `http://你的服务器IP/admin/` | 显示后台登录页 |
| `http://你的服务器IP/api/` | 后端接口上下文，单独访问会 404 或返回 JSON 错误 |

**后台默认账号**：`admin` / `admin123`

---

## 九、常见问题

### 9.1 后台 `/admin/` 空白，Console 报 assets 404

- 确认 `company-cms/vite.config.js` 有 `base: '/admin/'`
- 确认上传的是重新构建后的 `company-cms/dist`
- 确认 Nginx 配置里有 `/admin/assets/` 映射

### 9.2 登录接口报 500 "系统内部错误"

检查后端日志 `nohup.out`，常见原因：

- 数据库连接失败（用户名/密码错误、MySQL 未启动）
- 数据库未导入 `init.sql`

### 9.3 访问 IP 超时

- 检查安全组是否放行 80 端口
- 检查服务器防火墙：`ufw status` 或 `systemctl status firewalld`

### 9.4 上传图片后前台不显示

- 确认 `/data/company_info/uploads/` 目录存在且有写入权限
- 确认 Nginx 配置里有 `/uploads/` 映射
- 确认后端 `file.upload-path` 配置为 `/data/company_info/uploads/`

### 9.5 内存不足导致 Java 或 MySQL 被杀

2 GB 内存服务器建议：

- MySQL 限制内存：`/etc/mysql/mysql.conf.d/mysqld.cnf` 添加
  ```ini
  innodb_buffer_pool_size = 256M
  key_buffer_size = 16M
  ```
- Java 限制堆内存：`-Xms256m -Xmx512m`

---

## 十、后续维护

### 更新后端

```bash
cd /www/server/company-admin
ps -ef | grep company-admin-1.0.0.jar
kill -9 <PID>
# 上传新 JAR 后
nohup java -jar -Xms256m -Xmx512m company-admin-1.0.0.jar --spring.profiles.active=prod > nohup.out 2>&1 &
```

### 更新前端

```bash
# 本机重新打包后
scp -r company-web/dist root@你的服务器IP:/www/company-web/
scp -r company-cms/dist root@你的服务器IP:/www/company-cms/
# 服务器上重载 Nginx
nginx -s reload
```

---

## 十一、推荐进阶

- 使用 HTTPS：申请免费 SSL 证书，Nginx 配置 443 端口
- 数据库分离：使用阿里云 RDS，释放 ECS 内存压力
- 进程守护：使用 `systemd` 或 `supervisor` 管理后端进程
- 定期备份：MySQL 数据 + 上传文件目录 `/data/company_info/uploads/`
