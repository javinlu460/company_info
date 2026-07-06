package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.CmsFaqDTO;
import com.company.dto.PageQuery;
import com.company.entity.CmsFaq;
import com.company.service.CmsFaqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "常见问题管理")
@RestController
@RequestMapping("/admin/faq")
public class CmsFaqController {

    private final CmsFaqService cmsFaqService;

    public CmsFaqController(CmsFaqService cmsFaqService) {
        this.cmsFaqService = cmsFaqService;
    }

    @ApiOperation("FAQ分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('content:faq:list')")
    public R<?> list(PageQuery pageQuery) {
        Page<CmsFaq> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<CmsFaq> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(CmsFaq::getQuestion, pageQuery.getKeyword());
        }
        wrapper.orderByAsc(CmsFaq::getOrderNum);
        return R.ok(cmsFaqService.page(page, wrapper));
    }

    @ApiOperation("FAQ详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('content:faq:list')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(cmsFaqService.getById(id));
    }

    @ApiOperation("新增FAQ")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('content:faq:add')")
    public R<?> add(@Validated @RequestBody CmsFaqDTO dto) {
        CmsFaq faq = new CmsFaq();
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setCategory(StringUtils.isNotBlank(dto.getCategory()) ? dto.getCategory() : "通用");
        faq.setOrderNum(dto.getSort());
        faq.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        cmsFaqService.save(faq);
        return R.ok();
    }

    @ApiOperation("编辑FAQ")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('content:faq:edit')")
    public R<?> edit(@Validated @RequestBody CmsFaqDTO dto) {
        if (dto.getId() == null) {
            return R.fail("FAQ ID不能为空");
        }
        CmsFaq faq = cmsFaqService.getById(dto.getId());
        if (faq == null) {
            return R.fail("FAQ不存在");
        }
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setCategory(StringUtils.isNotBlank(dto.getCategory()) ? dto.getCategory() : "通用");
        faq.setOrderNum(dto.getSort());
        if (dto.getStatus() != null) {
            faq.setStatus(dto.getStatus());
        }
        cmsFaqService.updateById(faq);
        return R.ok();
    }

    @ApiOperation("删除FAQ")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('content:faq:delete')")
    public R<?> remove(@PathVariable Long id) {
        cmsFaqService.removeById(id);
        return R.ok();
    }
}
