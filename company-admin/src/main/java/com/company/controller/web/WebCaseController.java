package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.CmsCase;
import com.company.service.CmsCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "前台客户案例")
@RestController
@RequestMapping("/web/case")
public class WebCaseController {

    private final CmsCaseService cmsCaseService;

    public WebCaseController(CmsCaseService cmsCaseService) {
        this.cmsCaseService = cmsCaseService;
    }

    @ApiOperation("案例列表(status=1)")
    @GetMapping("/list")
    public R<?> list() {
        List<CmsCase> list = cmsCaseService.list(
                new LambdaQueryWrapper<CmsCase>()
                        .eq(CmsCase::getStatus, 1)
                        .orderByAsc(CmsCase::getOrderNum));
        return R.ok(list);
    }

    @ApiOperation("案例详情")
    @GetMapping("/{id}")
    public R<?> detail(@PathVariable Long id) {
        CmsCase cmsCase = cmsCaseService.getById(id);
        if (cmsCase == null || cmsCase.getStatus() == 0) {
            return R.fail("案例不存在或已下架");
        }
        return R.ok(cmsCase);
    }
}
