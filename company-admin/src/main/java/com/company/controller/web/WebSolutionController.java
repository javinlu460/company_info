package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.CmsSolution;
import com.company.service.CmsSolutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "前台解决方案")
@RestController
@RequestMapping("/web/solution")
public class WebSolutionController {

    private final CmsSolutionService cmsSolutionService;

    public WebSolutionController(CmsSolutionService cmsSolutionService) {
        this.cmsSolutionService = cmsSolutionService;
    }

    @ApiOperation("方案列表(status=1)")
    @GetMapping("/list")
    public R<?> list() {
        List<CmsSolution> list = cmsSolutionService.list(
                new LambdaQueryWrapper<CmsSolution>()
                        .eq(CmsSolution::getStatus, 1)
                        .orderByAsc(CmsSolution::getOrderNum));
        return R.ok(list);
    }
}
