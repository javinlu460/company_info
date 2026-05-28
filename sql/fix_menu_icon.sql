-- 修复菜单图标名称，使用 Element Plus 有效的图标组件名（PascalCase）
USE company_info;

UPDATE sys_menu SET icon = 'Setting' WHERE id = 1;
UPDATE sys_menu SET icon = 'User' WHERE id = 2;
UPDATE sys_menu SET icon = 'UserFilled' WHERE id = 7;
UPDATE sys_menu SET icon = 'Grid' WHERE id = 12;
UPDATE sys_menu SET icon = 'Edit' WHERE id = 17;
UPDATE sys_menu SET icon = 'Document' WHERE id = 20;
UPDATE sys_menu SET icon = 'Picture' WHERE id = 21;
UPDATE sys_menu SET icon = 'Collection' WHERE id = 26;
UPDATE sys_menu SET icon = 'Goods' WHERE id = 31;
UPDATE sys_menu SET icon = 'Folder' WHERE id = 36;
UPDATE sys_menu SET icon = 'Notebook' WHERE id = 41;
UPDATE sys_menu SET icon = 'Message' WHERE id = 46;
