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
        String[] configKeys = {"company_name", "company_logo", "company_icp", "company_description",
                "company_phone", "company_email", "company_address"};
        for (String key : configKeys) {
            SysConfig config = sysConfigService.getOne(
                    new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
            if (config != null) {
                data.put(key, config.getConfigValue());
            } else {
                data.put(key, "");
            }
        }
        return R.ok(data);
    }
}
