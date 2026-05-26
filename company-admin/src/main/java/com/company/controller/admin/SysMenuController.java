package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.dto.SysMenuDTO;
import com.company.entity.SysMenu;
import com.company.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @ApiOperation("菜单树")
    @GetMapping("/tree")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public R<?> tree() {
        return R.ok(sysMenuService.getMenuTree());
    }

    @ApiOperation("菜单详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(sysMenuService.getById(id));
    }

    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    public R<?> add(@Validated @RequestBody SysMenuDTO dto) {
        SysMenu menu = new SysMenu();
        menu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setPerms(dto.getPerms());
        menu.setIcon(dto.getIcon());
        menu.setOrderNum(dto.getSort() != null ? dto.getSort() : 0);
        menu.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        menu.setVisible(dto.getVisible() != null ? dto.getVisible() : 1);
        sysMenuService.save(menu);
        return R.ok();
    }

    @ApiOperation("编辑菜单")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    public R<?> edit(@Validated @RequestBody SysMenuDTO dto) {
        if (dto.getId() == null) {
            return R.fail("菜单ID不能为空");
        }
        SysMenu menu = sysMenuService.getById(dto.getId());
        if (menu == null) {
            return R.fail("菜单不存在");
        }
        menu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setPerms(dto.getPerms());
        menu.setIcon(dto.getIcon());
        menu.setOrderNum(dto.getSort() != null ? dto.getSort() : 0);
        if (dto.getStatus() != null) {
            menu.setStatus(dto.getStatus());
        }
        if (dto.getVisible() != null) {
            menu.setVisible(dto.getVisible());
        }
        sysMenuService.updateById(menu);
        return R.ok();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    public R<?> remove(@PathVariable Long id) {
        // 检查是否有子菜单
        long count = sysMenuService.count(
                new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            return R.fail("存在子菜单，不允许删除");
        }
        sysMenuService.removeById(id);
        return R.ok();
    }
}
