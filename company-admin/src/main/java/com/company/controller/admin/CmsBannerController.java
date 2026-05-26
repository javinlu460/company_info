package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.dto.CmsBannerDTO;
import com.company.entity.CmsBanner;
import com.company.service.CmsBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "轮播图管理")
@RestController
@RequestMapping("/admin/banner")
public class CmsBannerController {

    private final CmsBannerService cmsBannerService;

    public CmsBannerController(CmsBannerService cmsBannerService) {
        this.cmsBannerService = cmsBannerService;
    }

    @ApiOperation("轮播图列表(按sort排序)")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('cms:banner:list')")
    public R<?> list() {
        List<CmsBanner> list = cmsBannerService.list(
                new LambdaQueryWrapper<CmsBanner>().orderByAsc(CmsBanner::getOrderNum));
        return R.ok(list);
    }

    @ApiOperation("新增轮播图")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('cms:banner:add')")
    public R<?> add(@Validated @RequestBody CmsBannerDTO dto) {
        CmsBanner banner = new CmsBanner();
        banner.setTitle(dto.getTitle());
        banner.setImageUrl(dto.getImageUrl());
        banner.setLinkUrl(dto.getLinkUrl());
        banner.setOrderNum(dto.getSort());
        banner.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        cmsBannerService.save(banner);
        return R.ok();
    }

    @ApiOperation("编辑轮播图")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('cms:banner:edit')")
    public R<?> edit(@Validated @RequestBody CmsBannerDTO dto) {
        if (dto.getId() == null) {
            return R.fail("轮播图ID不能为空");
        }
        CmsBanner banner = cmsBannerService.getById(dto.getId());
        if (banner == null) {
            return R.fail("轮播图不存在");
        }
        banner.setTitle(dto.getTitle());
        banner.setImageUrl(dto.getImageUrl());
        banner.setLinkUrl(dto.getLinkUrl());
        banner.setOrderNum(dto.getSort());
        if (dto.getStatus() != null) {
            banner.setStatus(dto.getStatus());
        }
        cmsBannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation("删除轮播图")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:banner:remove')")
    public R<?> remove(@PathVariable Long id) {
        cmsBannerService.removeById(id);
        return R.ok();
    }
}
