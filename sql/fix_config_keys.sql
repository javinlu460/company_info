-- 迁移配置数据：将camelCase key的值迁移到underscore key中，然后删除重复记录
USE company_info;

-- 将驼峰命名记录的值迁移到下划线命名记录
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'companyName') t) WHERE config_key = 'company_name' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'companyLogo') t) WHERE config_key = 'company_logo' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'phone') t) WHERE config_key = 'company_phone' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'email') t) WHERE config_key = 'company_email' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'address') t) WHERE config_key = 'company_address' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'companyIntro') t) WHERE config_key = 'company_description' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'companyCulture') t) WHERE config_key = 'company_culture' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'companyHonor') t) WHERE config_key = 'company_honor' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'companyVision') t) WHERE config_key = 'company_vision' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'fax') t) WHERE config_key = 'company_fax' AND (config_value IS NULL OR config_value = '');
UPDATE sys_config SET config_value = (SELECT t.v FROM (SELECT config_value AS v FROM sys_config WHERE config_key = 'icp') t) WHERE config_key = 'icp_number' AND (config_value IS NULL OR config_value = '');

-- 删除重复的驼峰命名记录
DELETE FROM sys_config WHERE config_key IN ('companyName', 'companyLogo', 'phone', 'email', 'address', 'fax', 'icp', 'companyIntro', 'companyCulture', 'companyHonor', 'companyVision');
