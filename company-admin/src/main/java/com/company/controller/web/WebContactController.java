package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.dto.MessageDTO;
import com.company.entity.CmsMessage;
import com.company.entity.SysConfig;
import com.company.service.CmsMessageService;
import com.company.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "前台联系")
@RestController
@RequestMapping("/web/contact")
public class WebContactController {

    private final SysConfigService sysConfigService;
    private final CmsMessageService cmsMessageService;

    public WebContactController(SysConfigService sysConfigService,
                                CmsMessageService cmsMessageService) {
        this.sysConfigService = sysConfigService;
        this.cmsMessageService = cmsMessageService;
    }

    @ApiOperation("获取联系信息")
    @GetMapping("/info")
    public R<?> info() {
        Map<String, String> data = new HashMap<>();
        // 读取所有配置
        java.util.List<SysConfig> allConfigs = sysConfigService.list();
        Map<String, String> configMap = new HashMap<>();
        for (SysConfig c : allConfigs) {
            if (c.getConfigKey() != null) {
                configMap.put(c.getConfigKey(), c.getConfigValue() != null ? c.getConfigValue() : "");
            }
        }
        data.put("name", configMap.getOrDefault("company_name", ""));
        data.put("phone", configMap.getOrDefault("company_phone", ""));
        data.put("email", configMap.getOrDefault("company_email", ""));
        data.put("address", configMap.getOrDefault("company_address", ""));
        data.put("fax", configMap.getOrDefault("company_fax", ""));
        return R.ok(data);
    }

    @ApiOperation("提交留言")
    @PostMapping("/message")
    public R<?> submitMessage(@Validated @RequestBody MessageDTO dto) {
        CmsMessage message = new CmsMessage();
        message.setName(dto.getName());
        message.setPhone(dto.getPhone());
        message.setEmail(dto.getEmail());
        message.setContent(dto.getContent());
        message.setAddress(dto.getAddress());
        message.setIsRead(0);
        cmsMessageService.save(message);
        return R.ok("留言提交成功");
    }
}
