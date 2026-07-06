package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.CmsFaq;
import com.company.service.CmsFaqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "前台常见问题")
@RestController
@RequestMapping("/web/faq")
public class WebFaqController {

    private final CmsFaqService cmsFaqService;

    public WebFaqController(CmsFaqService cmsFaqService) {
        this.cmsFaqService = cmsFaqService;
    }

    @ApiOperation("FAQ列表(status=1)")
    @GetMapping("/list")
    public R<?> list() {
        List<CmsFaq> list = cmsFaqService.list(
                new LambdaQueryWrapper<CmsFaq>()
                        .eq(CmsFaq::getStatus, 1)
                        .orderByAsc(CmsFaq::getOrderNum));
        return R.ok(list);
    }

    @ApiOperation("FAQ分类列表")
    @GetMapping("/categories")
    public R<?> categories() {
        List<CmsFaq> list = cmsFaqService.list(
                new LambdaQueryWrapper<CmsFaq>()
                        .eq(CmsFaq::getStatus, 1)
                        .select(CmsFaq::getCategory));
        List<String> categories = list.stream()
                .map(CmsFaq::getCategory)
                .filter(category -> category != null && !category.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());
        return R.ok(categories);
    }
}
