package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.PageQuery;
import com.company.entity.CmsMessage;
import com.company.service.CmsMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "留言管理")
@RestController
@RequestMapping("/admin/message")
public class CmsMessageController {

    private final CmsMessageService cmsMessageService;

    public CmsMessageController(CmsMessageService cmsMessageService) {
        this.cmsMessageService = cmsMessageService;
    }

    @ApiOperation("留言分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('cms:message:list')")
    public R<?> list(PageQuery pageQuery,
                     @RequestParam(required = false) Integer isRead) {
        Page<CmsMessage> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<CmsMessage> wrapper = new LambdaQueryWrapper<>();
        if (isRead != null) {
            wrapper.eq(CmsMessage::getIsRead, isRead);
        }
        wrapper.orderByDesc(CmsMessage::getCreateTime);
        return R.ok(cmsMessageService.page(page, wrapper));
    }

    @ApiOperation("留言详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:message:query')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(cmsMessageService.getById(id));
    }

    @ApiOperation("标记已读")
    @PutMapping("/{id}/read")
    @PreAuthorize("@ss.hasPermi('cms:message:edit')")
    public R<?> markRead(@PathVariable Long id) {
        CmsMessage message = new CmsMessage();
        message.setId(id);
        message.setIsRead(1);
        cmsMessageService.updateById(message);
        return R.ok();
    }

    @ApiOperation("删除留言")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('cms:message:remove')")
    public R<?> remove(@PathVariable Long id) {
        cmsMessageService.removeById(id);
        return R.ok();
    }
}
