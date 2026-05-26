package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.entity.CmsProduct;
import com.company.entity.CmsProductCategory;
import com.company.service.CmsProductCategoryService;
import com.company.service.CmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "前台产品")
@RestController
@RequestMapping("/web/product")
public class WebProductController {

    private final CmsProductService cmsProductService;
    private final CmsProductCategoryService cmsProductCategoryService;

    public WebProductController(CmsProductService cmsProductService,
                                CmsProductCategoryService cmsProductCategoryService) {
        this.cmsProductService = cmsProductService;
        this.cmsProductCategoryService = cmsProductCategoryService;
    }

    @ApiOperation("产品列表(status=1)")
    @GetMapping("/list")
    public R<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                     @RequestParam(defaultValue = "10") Integer pageSize,
                     @RequestParam(required = false) Long categoryId) {
        Page<CmsProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CmsProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CmsProduct::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(CmsProduct::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(CmsProduct::getCreateTime);
        return R.ok(cmsProductService.page(page, wrapper));
    }

    @ApiOperation("产品详情(浏览量+1)")
    @GetMapping("/{id}")
    public R<?> detail(@PathVariable Long id) {
        CmsProduct product = cmsProductService.getById(id);
        if (product == null || product.getStatus() != 1) {
            return R.fail("产品不存在");
        }
        // 浏览量+1
        CmsProduct update = new CmsProduct();
        update.setId(id);
        update.setViewCount(product.getViewCount() != null ? product.getViewCount() + 1 : 1);
        cmsProductService.updateById(update);
        // 返回更新后的浏览量
        product.setViewCount(update.getViewCount());
        return R.ok(product);
    }

    @ApiOperation("产品分类列表")
    @GetMapping("/categories")
    public R<?> categories() {
        List<CmsProductCategory> list = cmsProductCategoryService.list(
                new LambdaQueryWrapper<CmsProductCategory>()
                        .eq(CmsProductCategory::getStatus, 1)
                        .orderByAsc(CmsProductCategory::getOrderNum));
        return R.ok(list);
    }
}
