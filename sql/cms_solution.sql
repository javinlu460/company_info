-- 解决方案表
CREATE TABLE IF NOT EXISTS cms_solution (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL COMMENT '方案标题',
  summary VARCHAR(500) COMMENT '摘要描述',
  content TEXT COMMENT '详情(富文本)',
  cover_image VARCHAR(500) COMMENT '封面图',
  tags VARCHAR(500) COMMENT '标签(JSON数组)',
  sort INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='解决方案表';

-- 初始化数据
INSERT INTO cms_solution (title, summary, tags, sort, status) VALUES
('OEM 定制 · 按图加工', '客户提供图纸或样品，我们按规格进行精密加工与组装，确保尺寸、材质、工艺完全符合要求。', '["来图加工","批量稳定","保密协议"]', 1, 1),
('快速打样 · 新品开发验证', '针对新品开发阶段提供小批量打样服务，快速验证结构设计与工艺可行性，缩短研发周期。', '["7-15天交样","工艺建议","小批量"]', 2, 1),
('小批量 · 维修/替换用件', '为设备维护、备件更换提供灵活的小批量生产，无需开模大批囤货，降低库存与资金压力。', '["按需生产","交期灵活","可追溯"]', 3, 1),
('逆向工程 · 来样复制', '仅有实物样品即可完成测绘、建模与生产复制，帮助客户延续供应或优化老旧零部件。', '["三维测绘","结构优化","老旧件替代"]', 4, 1);

-- 菜单权限
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(50, 20, '解决方案管理', 2, 'solution', 'content/solution/index', NULL, 'Operation', 6, 1, 1),
(51, 50, '方案查询', 3, NULL, NULL, 'content:solution:list', NULL, 1, 1, 1),
(52, 50, '方案新增', 3, NULL, NULL, 'content:solution:add', NULL, 2, 1, 1),
(53, 50, '方案修改', 3, NULL, NULL, 'content:solution:edit', NULL, 3, 1, 1),
(54, 50, '方案删除', 3, NULL, NULL, 'content:solution:delete', NULL, 4, 1, 1);

-- 角色权限关联
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1,50),(1,51),(1,52),(1,53),(1,54);
