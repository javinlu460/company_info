package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.CmsNewsDTO;
import com.company.dto.PageQuery;
import com.company.entity.CmsNews;
import com.company.service.CmsNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "新闻管理")
@RestController
@RequestMapping("/admin/news")
public class CmsNewsController {

    private final CmsNewsService cmsNewsService;

    public CmsNewsController(CmsNewsService cmsNewsService) {
        this.cmsNewsService = cmsNewsService;
    }

    @ApiOperation("新闻分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('cms:news:list')")
    public R<?> list(PageQuery pageQuery,
                     @RequestParam(required = false) Long categoryId) {
        Page<CmsNews> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<CmsNews> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(CmsNews::getTitle, pageQuery.getKeyword());
        }
        if (categoryId != null) {
            wrapper.eq(CmsNews::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(CmsNews::getCreateTime);
        return R.ok(cmsNewsService.page(page, wrapper));
    }

    @ApiOperation("新闻详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:news:query')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(cmsNewsService.getById(id));
    }

    @ApiOperation("新增新闻")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('cms:news:add')")
    public R<?> add(@Validated @RequestBody CmsNewsDTO dto) {
        CmsNews news = new CmsNews();
        news.setCategoryId(dto.getCategoryId());
        news.setTitle(dto.getTitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setCoverImage(dto.getCoverImage());
        news.setAuthor(dto.getAuthor());
        news.setOrderNum(dto.getSort());
        news.setStatus(dto.getStatus());
        news.setIsTop(dto.getIsTop() != null ? dto.getIsTop() : 0);
        cmsNewsService.save(news);
        return R.ok();
    }

    @ApiOperation("编辑新闻")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('cms:news:edit')")
    public R<?> edit(@Validated @RequestBody CmsNewsDTO dto) {
        if (dto.getId() == null) {
            return R.fail("新闻ID不能为空");
        }
        CmsNews news = cmsNewsService.getById(dto.getId());
        if (news == null) {
            return R.fail("新闻不存在");
        }
        news.setCategoryId(dto.getCategoryId());
        news.setTitle(dto.getTitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setCoverImage(dto.getCoverImage());
        news.setAuthor(dto.getAuthor());
        news.setOrderNum(dto.getSort());
        if (dto.getStatus() != null) {
            news.setStatus(dto.getStatus());
        }
        if (dto.getIsTop() != null) {
            news.setIsTop(dto.getIsTop());
        }
        cmsNewsService.updateById(news);
        return R.ok();
    }

    @ApiOperation("删除新闻")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:news:remove')")
    public R<?> remove(@PathVariable Long id) {
        cmsNewsService.removeById(id);
        return R.ok();
    }
}
