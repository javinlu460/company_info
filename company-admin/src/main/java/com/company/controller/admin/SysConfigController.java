package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.SysConfig;
import com.company.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "系统配置管理")
@RestController
@RequestMapping("/admin/config")
public class SysConfigController {

    private final SysConfigService sysConfigService;

    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @ApiOperation("所有配置列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public R<?> list() {
        List<SysConfig> configs = sysConfigService.list(
                new LambdaQueryWrapper<SysConfig>().orderByDesc(SysConfig::getCreateTime));
        return R.ok(configs);
    }

    @ApiOperation("按key获取配置")
    @GetMapping("/{key}")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    public R<?> getByKey(@PathVariable String key) {
        SysConfig config = sysConfigService.getOne(
                new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
        if (config == null) {
            return R.fail("配置项不存在");
        }
        return R.ok(config);
    }

    @ApiOperation("更新配置")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    public R<?> update(@RequestBody SysConfig config) {
        if (config.getId() == null && config.getConfigKey() == null) {
            return R.fail("配置ID或配置Key不能为空");
        }
        if (config.getId() != null) {
            sysConfigService.updateById(config);
        } else {
            SysConfig existing = sysConfigService.getOne(
                    new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
            if (existing != null) {
                existing.setConfigValue(config.getConfigValue());
                sysConfigService.updateById(existing);
            } else {
                return R.fail("配置项不存在");
            }
        }
        return R.ok();
    }

    @ApiOperation("批量更新配置")
    @PutMapping("/batch")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    public R<?> batchUpdate(@RequestBody List<SysConfig> configs) {
        for (SysConfig config : configs) {
            if (config.getId() != null) {
                sysConfigService.updateById(config);
            } else if (config.getConfigKey() != null) {
                SysConfig existing = sysConfigService.getOne(
                        new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
                if (existing != null) {
                    existing.setConfigValue(config.getConfigValue());
                    sysConfigService.updateById(existing);
                } else {
                    // 配置不存在则新增
                    sysConfigService.save(config);
                }
            }
        }
        return R.ok();
    }
}
