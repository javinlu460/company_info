-- 留言表新增公司名和需求类型字段
ALTER TABLE cms_message ADD COLUMN company_name VARCHAR(100) DEFAULT NULL COMMENT '公司名称' AFTER address;
ALTER TABLE cms_message ADD COLUMN requirement_type VARCHAR(50) DEFAULT NULL COMMENT '需求类型（来图加工/打样/批量生产/精密再制造/外贸服务/其他）' AFTER company_name;
