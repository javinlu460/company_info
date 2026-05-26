package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.CmsProductDTO;
import com.company.dto.PageQuery;
import com.company.entity.CmsProduct;
import com.company.service.CmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "产品管理")
@RestController
@RequestMapping("/admin/product")
public class CmsProductController {

    private final CmsProductService cmsProductService;

    public CmsProductController(CmsProductService cmsProductService) {
        this.cmsProductService = cmsProductService;
    }

    @ApiOperation("产品分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('cms:product:list')")
    public R<?> list(PageQuery pageQuery,
                     @RequestParam(required = false) Long categoryId) {
        Page<CmsProduct> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<CmsProduct> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(CmsProduct::getProductName, pageQuery.getKeyword());
        }
        if (categoryId != null) {
            wrapper.eq(CmsProduct::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(CmsProduct::getCreateTime);
        return R.ok(cmsProductService.page(page, wrapper));
    }

    @ApiOperation("产品详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:product:query')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(cmsProductService.getById(id));
    }

    @ApiOperation("新增产品")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('cms:product:add')")
    public R<?> add(@Validated @RequestBody CmsProductDTO dto) {
        CmsProduct product = new CmsProduct();
        BeanUtils.copyProperties(dto, product);
        // DTO的title映射到entity的productName
        product.setProductName(dto.getTitle());
        // DTO的content映射到entity的detail
        product.setDetail(dto.getContent());
        // DTO的sort映射到entity的orderNum
        product.setOrderNum(dto.getSort());
        cmsProductService.save(product);
        return R.ok();
    }

    @ApiOperation("编辑产品")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('cms:product:edit')")
    public R<?> edit(@Validated @RequestBody CmsProductDTO dto) {
        if (dto.getId() == null) {
            return R.fail("产品ID不能为空");
        }
        CmsProduct product = cmsProductService.getById(dto.getId());
        if (product == null) {
            return R.fail("产品不存在");
        }
        BeanUtils.copyProperties(dto, product);
        product.setProductName(dto.getTitle());
        product.setDetail(dto.getContent());
        product.setOrderNum(dto.getSort());
        cmsProductService.updateById(product);
        return R.ok();
    }

    @ApiOperation("删除产品")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:product:remove')")
    public R<?> remove(@PathVariable Long id) {
        cmsProductService.removeById(id);
        return R.ok();
    }
}
