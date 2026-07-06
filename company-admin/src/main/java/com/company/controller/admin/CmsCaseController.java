package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.CmsCaseDTO;
import com.company.dto.PageQuery;
import com.company.entity.CmsCase;
import com.company.service.CmsCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "客户案例管理")
@RestController
@RequestMapping("/admin/case")
public class CmsCaseController {

    private final CmsCaseService cmsCaseService;

    public CmsCaseController(CmsCaseService cmsCaseService) {
        this.cmsCaseService = cmsCaseService;
    }

    @ApiOperation("案例分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('content:case:list')")
    public R<?> list(PageQuery pageQuery) {
        Page<CmsCase> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<CmsCase> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(CmsCase::getTitle, pageQuery.getKeyword());
        }
        wrapper.orderByAsc(CmsCase::getOrderNum);
        return R.ok(cmsCaseService.page(page, wrapper));
    }

    @ApiOperation("案例详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('content:case:list')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(cmsCaseService.getById(id));
    }

    @ApiOperation("新增案例")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('content:case:add')")
    public R<?> add(@Validated @RequestBody CmsCaseDTO dto) {
        CmsCase caseItem = new CmsCase();
        caseItem.setTitle(dto.getTitle());
        caseItem.setDescription(dto.getDescription());
        caseItem.setCoverImage(dto.getCoverImage());
        caseItem.setClientName(dto.getClientName());
        caseItem.setIndustry(dto.getIndustry());
        caseItem.setContent(dto.getContent());
        caseItem.setOrderNum(dto.getSort());
        caseItem.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        cmsCaseService.save(caseItem);
        return R.ok();
    }

    @ApiOperation("编辑案例")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('content:case:edit')")
    public R<?> edit(@Validated @RequestBody CmsCaseDTO dto) {
        if (dto.getId() == null) {
            return R.fail("案例ID不能为空");
        }
        CmsCase caseItem = cmsCaseService.getById(dto.getId());
        if (caseItem == null) {
            return R.fail("案例不存在");
        }
        caseItem.setTitle(dto.getTitle());
        caseItem.setDescription(dto.getDescription());
        caseItem.setCoverImage(dto.getCoverImage());
        caseItem.setClientName(dto.getClientName());
        caseItem.setIndustry(dto.getIndustry());
        caseItem.setContent(dto.getContent());
        caseItem.setOrderNum(dto.getSort());
        if (dto.getStatus() != null) {
            caseItem.setStatus(dto.getStatus());
        }
        cmsCaseService.updateById(caseItem);
        return R.ok();
    }

    @ApiOperation("删除案例")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('content:case:delete')")
    public R<?> remove(@PathVariable Long id) {
        cmsCaseService.removeById(id);
        return R.ok();
    }
}
