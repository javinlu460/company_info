# 企业官网系统

## 1. 项目简介

企业官网系统是一个前后端分离的综合性企业信息管理平台，包含前台官网展示和后台管理系统两大模块。

- **前台官网**：面向访客，展示企业介绍、产品信息、新闻动态、联系方式等，支持留言功能
- **后台管理**：面向管理员，提供内容管理、用户权限管理、系统配置等完整功能

系统采用 RBAC 权限模型，支持菜单级权限控制，前后端分离架构便于独立开发和部署。

---

## 2. 技术架构

### 2.1 整体架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前台官网      │    │   后台管理      │    │   后端API       │
│  company-web    │    │  company-cms    │    │ company-admin   │
│   Vue 3 + Vite  │    │ Vue 3 + Element │    │ SpringBoot +    │
│                 │    │   Plus + Pinia  │    │ MyBatis-Plus    │
└────────┬────────┘    └────────┬────────┘    └────────┬────────┘
         │                      │                      │
         └──────────────────────┼──────────────────────┘
                                │
                         ┌──────┴──────┐
                         │   MySQL 8.0 │
                         └─────────────┘
```

### 2.2 技术栈详情

| 层级 | 技术 | 版本 | 说明 |
|:---|:---|:---|:---|
| **后端** | Spring Boot | 2.7.18 | 核心框架 |
| | MyBatis-Plus | 3.5.5 | ORM 框架 |
| | Spring Security | - | 安全认证 |
| | JWT | 0.11.5 | Token 认证 |
| | MySQL | 8.0 | 关系型数据库 |
| | Knife4j | 4.3.0 | API 文档 |
| | Hutool | 5.8.22 | 工具库 |
| | Lombok | - | 代码简化 |
| **前台** | Vue | 3.4.21 | 前端框架 |
| | Vue Router | 4.3.0 | 路由管理 |
| | Vite | 5.2.0 | 构建工具 |
| | Axios | 1.6.8 | HTTP 请求 |
| **后台** | Vue | 3.3.0 | 前端框架 |
| | Vue Router | 4.2.0 | 路由管理 |
| | Pinia | 2.1.0 | 状态管理 |
| | Element Plus | 2.4.0 | UI 组件库 |
| | WangEditor | 5.1.0 | 富文本编辑器 |
| | Vite | 5.0.0 | 构建工具 |
| | Axios | 1.6.0 | HTTP 请求 |
| | Sass | 1.69.0 | CSS 预处理器 |

---

## 3. 环境要求

### 3.1 开发环境

| 环境 | 版本要求 | 说明 |
|:---|:---|:---|
| JDK | 1.8+ | 后端运行环境 |
| Maven | 3.6+ | 后端构建工具 |
| Node.js | 16+ | 前端运行环境 |
| MySQL | 8.0 | 数据库 |
| IDE | IDEA / VSCode | 推荐 IDEA 开发后端，VSCode 开发前端 |

### 3.2 验证环境

```bash
# 验证 JDK
java -version

# 验证 Maven
mvn -v

# 验证 Node.js
node -v
npm -v

# 验证 MySQL
mysql --version
```

---

## 4. 快速开始（开发环境）

### 4.1 克隆项目

```bash
git clone <项目仓库地址>
cd company_info
```

### 4.2 数据库初始化

1. 创建数据库（使用 MySQL 命令行或图形化工具）：

```bash
mysql -u root -p
```

```sql
CREATE DATABASE company_info CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 导入初始化脚本：

```bash
mysql -u root -p company_info < sql/init.sql
```

> `init.sql` 包含 13 张表的创建语句及初始数据（包括默认管理员账号、菜单数据、系统配置等）。

### 4.3 后端启动

1. 修改数据库配置：

编辑 `company-admin/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/company_info?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root        # 修改为你的数据库账号
    password: root        # 修改为你的数据库密码
```

2. 启动方式一：IDEA 运行

- 使用 IDEA 打开 `company-admin` 项目
- 运行 `CompanyApplication.java` 主类

3. 启动方式二：Maven 命令

```bash
cd company-admin
mvn spring-boot:run
```

后端服务启动后，API 地址为：`http://localhost:8080/api`

API 文档地址：`http://localhost:8080/api/doc.html`

### 4.4 前台启动

```bash
cd company-web
npm install
npm run dev
```

前台官网访问地址：`http://localhost:3000`

### 4.5 后台管理启动

```bash
cd company-cms
npm install
npm run dev
```

后台管理访问地址：`http://localhost:3001`

### 4.6 访问地址汇总

| 服务 | 地址 | 说明 |
|:---|:---|:---|
| 后端 API | http://localhost:8080/api | RESTful API |
| API 文档 | http://localhost:8080/api/doc.html | Knife4j 接口文档 |
| 前台官网 | http://localhost:3000 | 企业官网展示 |
| 后台管理 | http://localhost:3001 | 管理系统后台 |

---

## 5. 默认账号

| 账号类型 | 用户名 | 密码 | 说明 |
|:---|:---|:---|:---|
| 管理员 | admin | admin123 | 超级管理员，拥有所有权限 |

> 首次登录后请及时修改默认密码。

---

## 6. 生产部署

### 6.1 后端部署

1. 打包：

```bash
cd company-admin
mvn clean package -DskipTests
```

打包后的 jar 文件位于 `target/company-admin-1.0.0.jar`。

2. 运行：

```bash
java -jar company-admin-1.0.0.jar
```

3. 配置文件外置（推荐）：

将 `application.yml` 放到 jar 包同级目录的 `config` 文件夹下，Spring Boot 会自动加载：

```
company-admin-1.0.0.jar
config/
  └── application.yml
```

> 外置配置优先级高于 jar 包内配置，便于生产环境灵活调整。

4. 后台运行（Linux）：

```bash
nohup java -jar company-admin-1.0.0.jar > app.log 2>&1 &
```

### 6.2 前端部署

#### 前台官网打包

```bash
cd company-web
npm install
npm run build
```

打包后的静态文件位于 `company-web/dist` 目录。

#### 后台管理打包

```bash
cd company-cms
npm install
npm run build
```

打包后的静态文件位于 `company-cms/dist` 目录。

### 6.3 Nginx 配置示例

#### 方案一：前台和后台使用不同端口

```nginx
# 前台官网 - 80 端口
server {
    listen 80;
    server_name www.yourcompany.com;

    location / {
        root /usr/share/nginx/html/company-web;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 文件上传静态资源映射
    location /uploads/ {
        alias /usr/share/nginx/html/uploads/;
    }
}

# 后台管理 - 8081 端口
server {
    listen 8081;
    server_name admin.yourcompany.com;

    location / {
        root /usr/share/nginx/html/company-cms;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

#### 方案二：后台通过 /admin 路径部署

```nginx
server {
    listen 80;
    server_name www.yourcompany.com;

    # 前台官网
    location / {
        root /usr/share/nginx/html/company-web;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后台管理
    location /admin {
        alias /usr/share/nginx/html/company-cms;
        index index.html;
        try_files $uri $uri/ /admin/index.html;
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 文件上传静态资源映射
    location /uploads/ {
        alias /usr/share/nginx/html/uploads/;
    }
}
```

### 6.4 完整 Nginx 配置示例

```nginx
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

    access_log /var/log/nginx/access.log main;

    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    gzip on;

    # 前台官网
    server {
        listen 80;
        server_name www.yourcompany.com yourcompany.com;

        # 前台静态资源
        location / {
            root /usr/share/nginx/html/company-web;
            index index.html;
            try_files $uri $uri/ /index.html;
        }

        # API 代理到后端
        location /api/ {
            proxy_pass http://127.0.0.1:8080/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
        }

        # 上传文件静态服务
        location /uploads/ {
            alias /usr/share/nginx/html/uploads/;
            expires 30d;
        }
    }

    # 后台管理（独立端口）
    server {
        listen 8081;
        server_name admin.yourcompany.com;

        location / {
            root /usr/share/nginx/html/company-cms;
            index index.html;
            try_files $uri $uri/ /index.html;
        }

        location /api/ {
            proxy_pass http://127.0.0.1:8080/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
}
```

---

## 7. 项目配置说明

### 7.1 application.yml 关键配置项

```yaml
server:
  port: 8080              # 后端服务端口
  servlet:
    context-path: /api    # API 上下文路径，所有接口前缀

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/company_info?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root        # 数据库用户名
    password: root        # 数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver
  servlet:
    multipart:
      max-file-size: 10MB     # 单个文件最大上传大小
      max-request-size: 20MB  # 单次请求最大上传大小

mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true   # 下划线转驼峰
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # SQL 日志输出
  global-config:
    db-config:
      logic-delete-field: deleted        # 逻辑删除字段
      logic-delete-value: 1              # 逻辑删除值
      logic-not-delete-value: 0          # 逻辑未删除值
      id-type: auto                      # 主键自增

jwt:
  secret: companyInfoSecretKey2024VeryLongSecretKeyForJWT  # JWT 密钥（生产环境请修改）
  expiration: 86400000   # Token 过期时间（毫秒），默认 24 小时

# 文件上传路径
file:
  upload-path: ./uploads/   # 文件上传存储路径，支持相对或绝对路径
```

### 7.2 配置项说明表

| 配置项 | 默认值 | 说明 |
|:---|:---|:---|
| `server.port` | 8080 | 后端服务端口 |
| `server.servlet.context-path` | /api | API 统一前缀 |
| `spring.datasource.url` | - | 数据库连接地址 |
| `spring.datasource.username` | root | 数据库用户名 |
| `spring.datasource.password` | root | 数据库密码 |
| `spring.servlet.multipart.max-file-size` | 10MB | 单文件大小限制 |
| `jwt.secret` | companyInfoSecretKey... | JWT 签名密钥，生产环境务必修改 |
| `jwt.expiration` | 86400000 | Token 有效期（24小时） |
| `file.upload-path` | ./uploads/ | 文件上传目录 |

---

## 8. 目录结构说明

```
company_info/
├── company-admin/                 # SpringBoot 后端项目
│   ├── src/main/java/com/company/
│   │   ├── common/                # 公共组件（常量、异常、结果封装、工具类）
│   │   ├── config/                # 配置类（CORS、Knife4j、MyBatis-Plus、Security、WebMvc）
│   │   ├── controller/            # 控制器
│   │   │   ├── admin/             # 后台管理接口
│   │   │   └── web/               # 前台官网接口
│   │   ├── dto/                   # 数据传输对象
│   │   ├── entity/                # 实体类
│   │   ├── mapper/                # MyBatis Mapper 接口
│   │   ├── security/              # Spring Security 相关（JWT 过滤器、认证入口、权限服务）
│   │   ├── service/               # 业务逻辑层
│   │   └── CompanyApplication.java # 启动类
│   ├── src/main/resources/
│   │   ├── mapper/                # MyBatis XML 映射文件
│   │   └── application.yml        # 配置文件
│   └── pom.xml                    # Maven 配置
│
├── company-web/                   # 前台官网 Vue 项目
│   ├── src/
│   │   ├── api/                   # API 接口封装
│   │   ├── assets/css/            # 全局样式
│   │   ├── components/            # 公共组件
│   │   ├── router/                # 路由配置
│   │   ├── views/                 # 页面视图
│   │   ├── App.vue                # 根组件
│   │   └── main.js                # 入口文件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── company-cms/                   # 后台管理 Vue 项目
│   ├── src/
│   │   ├── api/                   # API 接口封装
│   │   ├── components/            # 公共组件（图片上传、富文本编辑器）
│   │   ├── layout/                # 布局组件（侧边栏、导航栏、主内容区）
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # Pinia 状态管理
│   │   ├── views/                 # 页面视图
│   │   ├── App.vue                # 根组件
│   │   ├── main.js                # 入口文件
│   │   └── permission.js          # 权限控制
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── sql/
│   └── init.sql                   # 数据库初始化脚本
│
└── README.md                      # 项目说明文档
```

---

## 9. 功能模块说明

### 9.1 前台功能列表

| 模块 | 功能描述 |
|:---|:---|
| **首页** | Banner 轮播图展示、企业简介、产品推荐、新闻动态 |
| **关于我们** | 企业介绍、企业文化、发展历程 |
| **产品中心** | 产品分类列表、产品详情展示 |
| **新闻资讯** | 新闻分类列表、新闻详情查看 |
| **业务领域** | 业务范围介绍 |
| **联系我们** | 联系方式展示、在线留言 |

### 9.2 后台功能列表

| 模块 | 功能描述 |
|:---|:---|
| **仪表盘** | 系统概览、数据统计 |
| **内容管理** | |
| ├─ 轮播图管理 | Banner 增删改查、排序、启用/禁用 |
| ├─ 新闻管理 | 新闻发布、编辑、分类管理 |
| ├─ 产品管理 | 产品发布、编辑、分类管理 |
| ├─ 友情链接 | 友链增删改查 |
| └─ 留言管理 | 前台留言查看、回复、删除 |
| **系统管理** | |
| ├─ 用户管理 | 管理员账号增删改查、状态管理 |
| ├─ 角色管理 | 角色定义、权限分配 |
| ├─ 菜单管理 | 系统菜单配置、权限标识 |
| └─ 系统配置 | 网站名称、Logo 等基础配置 |

---

## 10. 常见问题（FAQ）

### 10.1 跨域问题

**问题描述**：前端请求后端接口时报 `CORS` 跨域错误。

**解决方案**：

后端已配置 `CorsConfig.java` 允许跨域，开发环境下前端 Vite 代理也已配置。

如需调整，修改 `company-admin/src/main/java/com/company/config/CorsConfig.java`：

```java
registry.addMapping("/**")
    .allowedOrigins("http://localhost:3000", "http://localhost:3001")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowCredentials(true);
```

生产环境建议通过 Nginx 反向代理解决跨域，或配置具体的域名白名单。

### 10.2 文件上传路径配置

**问题描述**：文件上传后无法访问或路径不正确。

**解决方案**：

1. 修改 `application.yml` 中的上传路径：

```yaml
file:
  upload-path: /usr/share/nginx/html/uploads/   # Linux 绝对路径
```

2. 确保上传目录存在且有写入权限：

```bash
mkdir -p /usr/share/nginx/html/uploads/
chmod 755 /usr/share/nginx/html/uploads/
```

3. Nginx 中配置静态资源映射（参见第 6 章 Nginx 配置）。

### 10.3 数据库连接失败

**问题描述**：启动后端时报数据库连接错误。

**排查步骤**：

1. 确认 MySQL 服务已启动：

```bash
# Windows
net start mysql

# Linux
systemctl status mysql
```

2. 检查数据库是否存在：

```sql
SHOW DATABASES;
```

3. 检查用户名密码是否正确：

```bash
mysql -u root -p -e "SHOW DATABASES;"
```

4. 检查 `application.yml` 中的数据库配置是否正确。

5. 确认 MySQL 驱动版本兼容（本项目使用 `com.mysql.cj.jdbc.Driver`，要求 MySQL 5.7+）。

### 10.4 前端代理配置

**问题描述**：前端开发时 API 请求 404。

**解决方案**：

前台项目 `company-web/vite.config.js`：

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

后台项目 `company-cms/vite.config.js`：

```javascript
server: {
  port: 3001,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

确保后端服务已启动在 `http://localhost:8080`。

### 10.5 Node.js 版本兼容性

**问题描述**：`npm install` 时报错或运行异常。

**解决方案**：

本项目要求 Node.js 16+。建议使用 `nvm` 管理 Node 版本：

```bash
# 安装 nvm（Windows 使用 nvm-windows）
# 安装并切换 Node 版本
nvm install 18
nvm use 18

# 验证版本
node -v  # 应输出 v18.x.x
```

如遇 `node-sass` 或原生模块编译错误，请确保安装了 Python 和 Visual Studio Build Tools（Windows）或 `build-essential`（Linux）。

---

> **提示**：如有其他问题，请检查后端日志和浏览器控制台输出，或参考 [Knife4j 接口文档](http://localhost:8080/api/doc.html) 进行接口调试。
