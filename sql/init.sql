-- ============================================================
-- 企业信息管理系统 - 数据库初始化脚本
-- Database: company_info
-- Charset: UTF8MB4
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS company_info DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE company_info;

-- ============================================================
-- 系统管理表
-- ============================================================

-- sys_user 用户表
CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  password VARCHAR(200) NOT NULL COMMENT '密码(BCrypt)',
  nickname VARCHAR(50) COMMENT '昵称',
  avatar VARCHAR(500) COMMENT '头像',
  email VARCHAR(100) COMMENT '邮箱',
  phone VARCHAR(20) COMMENT '手机号',
  status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- sys_role 角色表
CREATE TABLE IF NOT EXISTS sys_role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
  role_key VARCHAR(50) NOT NULL UNIQUE COMMENT '角色标识',
  sort INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1 COMMENT '状态',
  remark VARCHAR(200) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- sys_menu 菜单/权限表
CREATE TABLE IF NOT EXISTS sys_menu (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
  menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
  menu_type TINYINT COMMENT '类型 1目录 2菜单 3按钮',
  path VARCHAR(200) COMMENT '路由路径',
  component VARCHAR(200) COMMENT '组件路径',
  perms VARCHAR(200) COMMENT '权限标识',
  icon VARCHAR(100) COMMENT '图标',
  sort INT DEFAULT 0 COMMENT '排序',
  visible TINYINT DEFAULT 1 COMMENT '是否可见',
  status TINYINT DEFAULT 1 COMMENT '状态',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单/权限表';

-- sys_user_role 用户角色关联
CREATE TABLE IF NOT EXISTS sys_user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- sys_role_menu 角色菜单关联
CREATE TABLE IF NOT EXISTS sys_role_menu (
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- sys_config 系统配置
CREATE TABLE IF NOT EXISTS sys_config (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
  config_value TEXT COMMENT '配置值',
  config_type VARCHAR(20) DEFAULT 'text' COMMENT '值类型(text/image/richtext)',
  remark VARCHAR(200) COMMENT '说明',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================================
-- 内容管理表
-- ============================================================

-- cms_product_category 产品分类
CREATE TABLE IF NOT EXISTS cms_product_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
  name VARCHAR(100) NOT NULL COMMENT '分类名称',
  sort INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类表';

-- cms_product 产品
CREATE TABLE IF NOT EXISTS cms_product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT COMMENT '分类ID',
  title VARCHAR(200) NOT NULL COMMENT '产品名称',
  description VARCHAR(500) COMMENT '简介',
  content TEXT COMMENT '详情(富文本)',
  cover_image VARCHAR(500) COMMENT '封面图',
  images TEXT COMMENT '产品图片(JSON数组)',
  sort INT DEFAULT 0,
  status TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐',
  view_count INT DEFAULT 0 COMMENT '浏览量',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- cms_news_category 新闻分类
CREATE TABLE IF NOT EXISTS cms_news_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL COMMENT '分类名称',
  sort INT DEFAULT 0,
  status TINYINT DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻分类表';

-- cms_news 新闻
CREATE TABLE IF NOT EXISTS cms_news (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT COMMENT '分类ID',
  title VARCHAR(200) NOT NULL COMMENT '标题',
  summary VARCHAR(500) COMMENT '摘要',
  content TEXT COMMENT '内容(富文本)',
  cover_image VARCHAR(500) COMMENT '封面图',
  author VARCHAR(50) COMMENT '作者',
  sort INT DEFAULT 0,
  status TINYINT DEFAULT 1 COMMENT '状态 0草稿 1发布',
  is_top TINYINT DEFAULT 0 COMMENT '是否置顶',
  view_count INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- cms_banner 轮播图
CREATE TABLE IF NOT EXISTS cms_banner (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) COMMENT '标题',
  image_url VARCHAR(500) NOT NULL COMMENT '图片地址',
  link_url VARCHAR(500) COMMENT '跳转链接',
  sort INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- cms_message 留言
CREATE TABLE IF NOT EXISTS cms_message (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) COMMENT '姓名',
  phone VARCHAR(20) COMMENT '电话',
  email VARCHAR(100) COMMENT '邮箱',
  content TEXT NOT NULL COMMENT '留言内容',
  address VARCHAR(200) COMMENT '地址',
  is_read TINYINT DEFAULT 0 COMMENT '是否已读 0未读 1已读',
  reply_content TEXT COMMENT '回复内容',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言表';

-- cms_friendly_link 友情链接
CREATE TABLE IF NOT EXISTS cms_friendly_link (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL COMMENT '链接名称',
  url VARCHAR(500) NOT NULL COMMENT '链接地址',
  logo VARCHAR(500) COMMENT 'logo图片',
  sort INT DEFAULT 0,
  status TINYINT DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友情链接表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 1. 初始化用户（admin / admin123）
INSERT INTO sys_user (id, username, password, nickname, status) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 1);

-- 2. 初始化角色
INSERT INTO sys_role (id, role_name, role_key, sort, status, remark) VALUES
(1, '超级管理员', 'admin', 1, 1, '拥有系统全部权限'),
(2, '普通编辑', 'editor', 2, 1, '内容编辑权限');

-- 3. 初始化菜单树
-- ---- 一级目录：系统管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(1, 0, '系统管理', 1, '/system', NULL, NULL, 'setting', 1, 1, 1);

-- ---- 系统管理 -> 用户管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(2, 1, '用户管理', 2, 'user', 'system/user/index', NULL, 'user', 1, 1, 1),
(3, 2, '用户查询', 3, NULL, NULL, 'system:user:list', NULL, 1, 1, 1),
(4, 2, '用户新增', 3, NULL, NULL, 'system:user:add', NULL, 2, 1, 1),
(5, 2, '用户修改', 3, NULL, NULL, 'system:user:edit', NULL, 3, 1, 1),
(6, 2, '用户删除', 3, NULL, NULL, 'system:user:delete', NULL, 4, 1, 1);

-- ---- 系统管理 -> 角色管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(7, 1, '角色管理', 2, 'role', 'system/role/index', NULL, 'peoples', 2, 1, 1),
(8, 7, '角色查询', 3, NULL, NULL, 'system:role:list', NULL, 1, 1, 1),
(9, 7, '角色新增', 3, NULL, NULL, 'system:role:add', NULL, 2, 1, 1),
(10, 7, '角色修改', 3, NULL, NULL, 'system:role:edit', NULL, 3, 1, 1),
(11, 7, '角色删除', 3, NULL, NULL, 'system:role:delete', NULL, 4, 1, 1);

-- ---- 系统管理 -> 菜单管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(12, 1, '菜单管理', 2, 'menu', 'system/menu/index', NULL, 'tree-table', 3, 1, 1),
(13, 12, '菜单查询', 3, NULL, NULL, 'system:menu:list', NULL, 1, 1, 1),
(14, 12, '菜单新增', 3, NULL, NULL, 'system:menu:add', NULL, 2, 1, 1),
(15, 12, '菜单修改', 3, NULL, NULL, 'system:menu:edit', NULL, 3, 1, 1),
(16, 12, '菜单删除', 3, NULL, NULL, 'system:menu:delete', NULL, 4, 1, 1);

-- ---- 一级菜单：网站配置 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(17, 0, '网站配置', 2, '/config', 'system/config/index', NULL, 'edit', 2, 1, 1),
(18, 17, '配置查询', 3, NULL, NULL, 'system:config:list', NULL, 1, 1, 1),
(19, 17, '配置修改', 3, NULL, NULL, 'system:config:edit', NULL, 2, 1, 1);

-- ---- 一级目录：内容管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(20, 0, '内容管理', 1, '/content', NULL, NULL, 'documentation', 3, 1, 1);

-- ---- 内容管理 -> 轮播图管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(21, 20, '轮播图管理', 2, 'banner', 'content/banner/index', NULL, 'swticher', 1, 1, 1),
(22, 21, '轮播图查询', 3, NULL, NULL, 'content:banner:list', NULL, 1, 1, 1),
(23, 21, '轮播图新增', 3, NULL, NULL, 'content:banner:add', NULL, 2, 1, 1),
(24, 21, '轮播图修改', 3, NULL, NULL, 'content:banner:edit', NULL, 3, 1, 1),
(25, 21, '轮播图删除', 3, NULL, NULL, 'content:banner:delete', NULL, 4, 1, 1);

-- ---- 内容管理 -> 产品分类 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(26, 20, '产品分类', 2, 'product-category', 'content/product-category/index', NULL, 'tree', 2, 1, 1),
(27, 26, '分类查询', 3, NULL, NULL, 'content:productCategory:list', NULL, 1, 1, 1),
(28, 26, '分类新增', 3, NULL, NULL, 'content:productCategory:add', NULL, 2, 1, 1),
(29, 26, '分类修改', 3, NULL, NULL, 'content:productCategory:edit', NULL, 3, 1, 1),
(30, 26, '分类删除', 3, NULL, NULL, 'content:productCategory:delete', NULL, 4, 1, 1);

-- ---- 内容管理 -> 产品管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(31, 20, '产品管理', 2, 'product', 'content/product/index', NULL, 'shopping', 3, 1, 1),
(32, 31, '产品查询', 3, NULL, NULL, 'content:product:list', NULL, 1, 1, 1),
(33, 31, '产品新增', 3, NULL, NULL, 'content:product:add', NULL, 2, 1, 1),
(34, 31, '产品修改', 3, NULL, NULL, 'content:product:edit', NULL, 3, 1, 1),
(35, 31, '产品删除', 3, NULL, NULL, 'content:product:delete', NULL, 4, 1, 1);

-- ---- 内容管理 -> 新闻分类 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(36, 20, '新闻分类', 2, 'news-category', 'content/news-category/index', NULL, 'tree', 4, 1, 1),
(37, 36, '分类查询', 3, NULL, NULL, 'content:newsCategory:list', NULL, 1, 1, 1),
(38, 36, '分类新增', 3, NULL, NULL, 'content:newsCategory:add', NULL, 2, 1, 1),
(39, 36, '分类修改', 3, NULL, NULL, 'content:newsCategory:edit', NULL, 3, 1, 1),
(40, 36, '分类删除', 3, NULL, NULL, 'content:newsCategory:delete', NULL, 4, 1, 1);

-- ---- 内容管理 -> 新闻管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(41, 20, '新闻管理', 2, 'news', 'content/news/index', NULL, 'news', 5, 1, 1),
(42, 41, '新闻查询', 3, NULL, NULL, 'content:news:list', NULL, 1, 1, 1),
(43, 41, '新闻新增', 3, NULL, NULL, 'content:news:add', NULL, 2, 1, 1),
(44, 41, '新闻修改', 3, NULL, NULL, 'content:news:edit', NULL, 3, 1, 1),
(45, 41, '新闻删除', 3, NULL, NULL, 'content:news:delete', NULL, 4, 1, 1);

-- ---- 一级菜单：留言管理 ----
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(46, 0, '留言管理', 2, '/message', 'content/message/index', NULL, 'message', 4, 1, 1),
(47, 46, '留言查询', 3, NULL, NULL, 'content:message:list', NULL, 1, 1, 1),
(48, 46, '留言回复', 3, NULL, NULL, 'content:message:reply', NULL, 2, 1, 1),
(49, 46, '留言删除', 3, NULL, NULL, 'content:message:delete', NULL, 3, 1, 1);

-- 4. 关联admin用户与超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 5. 关联超级管理员角色与所有菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 19),
(1, 20),
(1, 21),
(1, 22),
(1, 23),
(1, 24),
(1, 25),
(1, 26),
(1, 27),
(1, 28),
(1, 29),
(1, 30),
(1, 31),
(1, 32),
(1, 33),
(1, 34),
(1, 35),
(1, 36),
(1, 37),
(1, 38),
(1, 39),
(1, 40),
(1, 41),
(1, 42),
(1, 43),
(1, 44),
(1, 45),
(1, 46),
(1, 47),
(1, 48),
(1, 49);

-- 6. 初始化系统配置
INSERT INTO sys_config (config_key, config_value, config_type, remark) VALUES
('company_name', '', 'text', '企业名称'),
('company_logo', '/uploads/logo.png', 'image', '企业Logo'),
('company_phone', '', 'text', '联系电话'),
('company_email', '', 'text', '邮箱'),
('company_address', '', 'text', '地址'),
('company_description', '', 'richtext', '公司简介'),
('company_culture', '', 'richtext', '企业文化'),
('company_honor', '', 'richtext', '资质荣誉'),
('company_vision', '', 'richtext', '公司愿景'),
('icp_number', '', 'text', '备案号'),
('company_fax', '', 'text', '传真');