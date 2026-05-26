package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.entity.CmsNews;
import com.company.entity.CmsNewsCategory;
import com.company.service.CmsNewsCategoryService;
import com.company.service.CmsNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "前台新闻")
@RestController
@RequestMapping("/web/news")
public class WebNewsController {

    private final CmsNewsService cmsNewsService;
    private final CmsNewsCategoryService cmsNewsCategoryService;

    public WebNewsController(CmsNewsService cmsNewsService,
                             CmsNewsCategoryService cmsNewsCategoryService) {
        this.cmsNewsService = cmsNewsService;
        this.cmsNewsCategoryService = cmsNewsCategoryService;
    }

    @ApiOperation("新闻列表(status=1)")
    @GetMapping("/list")
    public R<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                     @RequestParam(defaultValue = "10") Integer pageSize,
                     @RequestParam(required = false) Long categoryId) {
        Page<CmsNews> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CmsNews> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CmsNews::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(CmsNews::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(CmsNews::getCreateTime);
        return R.ok(cmsNewsService.page(page, wrapper));
    }

    @ApiOperation("新闻详情(浏览量+1)")
    @GetMapping("/{id}")
    public R<?> detail(@PathVariable Long id) {
        CmsNews news = cmsNewsService.getById(id);
        if (news == null || news.getStatus() != 1) {
            return R.fail("新闻不存在");
        }
        // 浏览量+1
        CmsNews update = new CmsNews();
        update.setId(id);
        update.setViewCount(news.getViewCount() != null ? news.getViewCount() + 1 : 1);
        cmsNewsService.updateById(update);
        // 返回更新后的浏览量
        news.setViewCount(update.getViewCount());
        return R.ok(news);
    }

    @ApiOperation("新闻分类列表")
    @GetMapping("/categories")
    public R<?> categories() {
        List<CmsNewsCategory> list = cmsNewsCategoryService.list(
                new LambdaQueryWrapper<CmsNewsCategory>()
                        .eq(CmsNewsCategory::getStatus, 1)
                        .orderByAsc(CmsNewsCategory::getOrderNum));
        return R.ok(list);
    }
}
