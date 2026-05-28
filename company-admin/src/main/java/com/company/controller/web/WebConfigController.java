package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.SysConfig;
import com.company.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "前台网站配置")
@RestController
@RequestMapping("/web/config")
public class WebConfigController {

    private final SysConfigService sysConfigService;

    public WebConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @ApiOperation("获取网站公开配置")
    @GetMapping("/site")
    public R<?> site() {
        Map<String, String> data = new HashMap<>();
        Map<String, String> configMap = getConfigMap();
        data.put("siteName", configMap.getOrDefault("company_name", ""));
        data.put("logo", configMap.getOrDefault("company_logo", ""));
        data.put("phone", configMap.getOrDefault("company_phone", ""));
        data.put("email", configMap.getOrDefault("company_email", ""));
        data.put("address", configMap.getOrDefault("company_address", ""));
        data.put("fax", configMap.getOrDefault("company_fax", ""));
        data.put("icp", configMap.getOrDefault("icp_number", ""));
        data.put("siteDesc", configMap.getOrDefault("company_description", ""));
        // 富文本内容
        data.put("aboutContent", configMap.getOrDefault("company_description", ""));
        data.put("cultureContent", configMap.getOrDefault("company_culture", ""));
        data.put("honorContent", configMap.getOrDefault("company_honor", ""));
        data.put("visionContent", configMap.getOrDefault("company_vision", ""));
        return R.ok(data);
    }

    private Map<String, String> getConfigMap() {
        Map<String, String> map = new HashMap<>();
        java.util.List<SysConfig> all = sysConfigService.list();
        for (SysConfig c : all) {
            if (c.getConfigKey() != null) {
                map.put(c.getConfigKey(), c.getConfigValue() != null ? c.getConfigValue() : "");
            }
        }
        return map;
    }
}
