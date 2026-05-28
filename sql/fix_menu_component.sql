-- 修复菜单表中 component 路径与前端实际文件路径不匹配的问题
-- 请在MySQL中执行此脚本

USE company_info;

-- 网站配置：system/config/index → config/index
UPDATE sys_menu SET component = 'config/index' WHERE id = 17;

-- 产品分类：content/product-category/index → content/productCategory/index
UPDATE sys_menu SET component = 'content/productCategory/index' WHERE id = 26;

-- 新闻分类：content/news-category/index → content/newsCategory/index
UPDATE sys_menu SET component = 'content/newsCategory/index' WHERE id = 36;
