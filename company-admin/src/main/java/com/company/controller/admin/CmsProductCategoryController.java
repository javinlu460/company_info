package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.CmsProductCategory;
import com.company.service.CmsProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "产品分类管理")
@RestController
@RequestMapping("/admin/product-category")
public class CmsProductCategoryController {

    private final CmsProductCategoryService cmsProductCategoryService;

    public CmsProductCategoryController(CmsProductCategoryService cmsProductCategoryService) {
        this.cmsProductCategoryService = cmsProductCategoryService;
    }

    @ApiOperation("分类树")
    @GetMapping("/tree")
    @PreAuthorize("@ss.hasPermi('cms:productCategory:list')")
    public R<?> tree() {
        return R.ok(cmsProductCategoryService.getCategoryTree());
    }

    @ApiOperation("分类列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('cms:productCategory:list')")
    public R<?> list() {
        List<CmsProductCategory> list = cmsProductCategoryService.list(
                new LambdaQueryWrapper<CmsProductCategory>().orderByAsc(CmsProductCategory::getOrderNum));
        return R.ok(list);
    }

    @ApiOperation("新增分类")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('cms:productCategory:add')")
    public R<?> add(@RequestBody CmsProductCategory category) {
        cmsProductCategoryService.save(category);
        return R.ok();
    }

    @ApiOperation("编辑分类")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('cms:productCategory:edit')")
    public R<?> edit(@RequestBody CmsProductCategory category) {
        if (category.getId() == null) {
            return R.fail("分类ID不能为空");
        }
        cmsProductCategoryService.updateById(category);
        return R.ok();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:productCategory:remove')")
    public R<?> remove(@PathVariable Long id) {
        // 检查是否有子分类
        long count = cmsProductCategoryService.count(
                new LambdaQueryWrapper<CmsProductCategory>().eq(CmsProductCategory::getParentId, id));
        if (count > 0) {
            return R.fail("存在子分类，不允许删除");
        }
        cmsProductCategoryService.removeById(id);
        return R.ok();
    }
}
