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
        String[] configKeys = {"company_name", "company_phone", "company_email", "company_address", "company_description"};
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
