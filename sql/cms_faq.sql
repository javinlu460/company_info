-- 常见问题表
CREATE TABLE IF NOT EXISTS cms_faq (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  question VARCHAR(500) NOT NULL COMMENT '问题',
  answer TEXT NOT NULL COMMENT '答案',
  category VARCHAR(50) DEFAULT '通用' COMMENT '分类',
  sort INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1 COMMENT '状态 0隐藏 1显示',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常见问题表';

-- 初始化数据
INSERT INTO cms_faq (question, answer, category, sort, status) VALUES
('最小起订量（MOQ）是多少？', '无固定MOQ，1件起可打样或加工；批量订单价格更优。', '加工能力', 1, 1),
('你们能加工哪些材质？', '碳钢、不锈钢、铝合金、铜合金、铸铁、工程塑料等均可加工，支持客户指定牌号。', '加工能力', 2, 1),
('公差最小能做到多少？', '常规件 ±0.05mm，精密件可达 ±0.01mm，CNC 五轴联动满足复杂曲面要求。', '加工能力', 3, 1),
('能否按我提供的图纸定制？', '完全可以。支持 DWG/STEP/IGS/PDF 等多种图纸格式，来图即可报价。', '定制流程', 4, 1),
('打样周期多长？', '简单件 5-7 个工作日，复杂件 10-15 个工作日（含首件检测报告）。', '定制流程', 5, 1),
('如何获取报价？', '发送图纸至邮箱或通过网站提交，工程师 24h 内回复详细报价单。', '定制流程', 6, 1),
('批量交期一般多久？', '视零件复杂度与数量，通常 15-30 个工作日；可协商加急。', '交期与质量', 7, 1),
('质量如何保证？', '全流程ISO9001管控，关键尺寸 100% 检测，出货附带质检报告。', '交期与质量', 8, 1),
('售后和质保政策？', '非人为因素导致的质量问题，免费返工或补件；质保期 12 个月。', '交期与质量', 9, 1);

-- 菜单权限
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort, visible, status) VALUES
(60, 20, '常见问题管理', 2, 'faq', 'content/faq/index', NULL, 'QuestionFilled', 8, 1, 1),
(61, 60, 'FAQ查询', 3, NULL, NULL, 'content:faq:list', NULL, 1, 1, 1),
(62, 60, 'FAQ新增', 3, NULL, NULL, 'content:faq:add', NULL, 2, 1, 1),
(63, 60, 'FAQ修改', 3, NULL, NULL, 'content:faq:edit', NULL, 3, 1, 1),
(64, 60, 'FAQ删除', 3, NULL, NULL, 'content:faq:delete', NULL, 4, 1, 1);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1,60),(1,61),(1,62),(1,63),(1,64);
