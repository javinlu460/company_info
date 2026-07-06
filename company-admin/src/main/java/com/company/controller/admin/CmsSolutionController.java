package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.CmsSolutionDTO;
import com.company.dto.PageQuery;
import com.company.entity.CmsSolution;
import com.company.service.CmsSolutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "解决方案管理")
@RestController
@RequestMapping("/admin/solution")
public class CmsSolutionController {

    private final CmsSolutionService cmsSolutionService;

    public CmsSolutionController(CmsSolutionService cmsSolutionService) {
        this.cmsSolutionService = cmsSolutionService;
    }

    @ApiOperation("方案分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('content:solution:list')")
    public R<?> list(PageQuery pageQuery) {
        Page<CmsSolution> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<CmsSolution> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(CmsSolution::getTitle, pageQuery.getKeyword());
        }
        wrapper.orderByAsc(CmsSolution::getOrderNum);
        return R.ok(cmsSolutionService.page(page, wrapper));
    }

    @ApiOperation("方案详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('content:solution:list')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(cmsSolutionService.getById(id));
    }

    @ApiOperation("新增方案")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('content:solution:add')")
    public R<?> add(@Validated @RequestBody CmsSolutionDTO dto) {
        CmsSolution solution = new CmsSolution();
        solution.setTitle(dto.getTitle());
        solution.setSummary(dto.getSummary());
        solution.setContent(dto.getContent());
        solution.setCoverImage(dto.getCoverImage());
        solution.setTags(dto.getTags());
        solution.setOrderNum(dto.getSort());
        solution.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        cmsSolutionService.save(solution);
        return R.ok();
    }

    @ApiOperation("编辑方案")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('content:solution:edit')")
    public R<?> edit(@Validated @RequestBody CmsSolutionDTO dto) {
        if (dto.getId() == null) {
            return R.fail("方案ID不能为空");
        }
        CmsSolution solution = cmsSolutionService.getById(dto.getId());
        if (solution == null) {
            return R.fail("方案不存在");
        }
        solution.setTitle(dto.getTitle());
        solution.setSummary(dto.getSummary());
        solution.setContent(dto.getContent());
        solution.setCoverImage(dto.getCoverImage());
        solution.setTags(dto.getTags());
        solution.setOrderNum(dto.getSort());
        if (dto.getStatus() != null) {
            solution.setStatus(dto.getStatus());
        }
        cmsSolutionService.updateById(solution);
        return R.ok();
    }

    @ApiOperation("删除方案")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('content:solution:delete')")
    public R<?> remove(@PathVariable Long id) {
        cmsSolutionService.removeById(id);
        return R.ok();
    }
}
