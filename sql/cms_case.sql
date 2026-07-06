-- 客户案例表
CREATE TABLE IF NOT EXISTS cms_case (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL COMMENT '案例标题',
  description VARCHAR(500) COMMENT '案例描述',
  cover_image VARCHAR(500) COMMENT '封面图',
  client_name VARCHAR(100) COMMENT '客户名称',
  industry VARCHAR(100) COMMENT '所属行业',
  content TEXT COMMENT '详情(富文本)',
  sort INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户案例表';

-- 初始化数据
INSERT INTO cms_case (title, description, cover_image, client_name, industry, sort, status) VALUES
('某矿业集团设备升级项目', '为客户提供整套矿山设备零部件加工方案', '', '某矿业集团', '矿山行业', 1, 1),
('城市基建工程配套', '基础设施建设配套设备精密加工', '', '某建设集团', '建筑工程', 2, 1),
('化工厂设备定制', '耐腐蚀高密封性专业设备加工', '', '某化工企业', '石油化工', 3, 1),
('环保水处理设备供应', '高效节能环保处理设备制造', '', '某环保科技', '环保行业', 4, 1);

-- 菜单权限
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(55, 20, '客户案例管理', 2, 'case', 'content/case/index', NULL, 'Trophy', 7, 1, 1),
(56, 55, '案例查询', 3, NULL, NULL, 'content:case:list', NULL, 1, 1, 1),
(57, 55, '案例新增', 3, NULL, NULL, 'content:case:add', NULL, 2, 1, 1),
(58, 55, '案例修改', 3, NULL, NULL, 'content:case:edit', NULL, 3, 1, 1),
(59, 55, '案例删除', 3, NULL, NULL, 'content:case:delete', NULL, 4, 1, 1);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1,55),(1,56),(1,57),(1,58),(1,59);
