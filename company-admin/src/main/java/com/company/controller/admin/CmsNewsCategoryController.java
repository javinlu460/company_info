package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.CmsNewsCategory;
import com.company.service.CmsNewsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "新闻分类管理")
@RestController
@RequestMapping("/admin/news-category")
public class CmsNewsCategoryController {

    private final CmsNewsCategoryService cmsNewsCategoryService;

    public CmsNewsCategoryController(CmsNewsCategoryService cmsNewsCategoryService) {
        this.cmsNewsCategoryService = cmsNewsCategoryService;
    }

    @ApiOperation("分类列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('cms:newsCategory:list')")
    public R<?> list() {
        List<CmsNewsCategory> list = cmsNewsCategoryService.list(
                new LambdaQueryWrapper<CmsNewsCategory>().orderByAsc(CmsNewsCategory::getOrderNum));
        return R.ok(list);
    }

    @ApiOperation("新增分类")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('cms:newsCategory:add')")
    public R<?> add(@RequestBody CmsNewsCategory category) {
        cmsNewsCategoryService.save(category);
        return R.ok();
    }

    @ApiOperation("编辑分类")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('cms:newsCategory:edit')")
    public R<?> edit(@RequestBody CmsNewsCategory category) {
        if (category.getId() == null) {
            return R.fail("分类ID不能为空");
        }
        cmsNewsCategoryService.updateById(category);
        return R.ok();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:newsCategory:remove')")
    public R<?> remove(@PathVariable Long id) {
        // 检查是否有子分类
        long count = cmsNewsCategoryService.count(
                new LambdaQueryWrapper<CmsNewsCategory>().eq(CmsNewsCategory::getParentId, id));
        if (count > 0) {
            return R.fail("存在子分类，不允许删除");
        }
        cmsNewsCategoryService.removeById(id);
        return R.ok();
    }
}
