package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.PageQuery;
import com.company.dto.SysRoleDTO;
import com.company.entity.SysRole;
import com.company.entity.SysRoleMenu;
import com.company.mapper.SysRoleMenuMapper;
import com.company.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    public SysRoleController(SysRoleService sysRoleService,
                             SysRoleMenuMapper sysRoleMenuMapper) {
        this.sysRoleService = sysRoleService;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }

    @ApiOperation("角色分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public R<?> list(PageQuery pageQuery) {
        Page<SysRole> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(SysRole::getRoleName, pageQuery.getKeyword())
                    .or().like(SysRole::getRoleKey, pageQuery.getKeyword());
        }
        wrapper.orderByDesc(SysRole::getCreateTime);
        return R.ok(sysRoleService.page(page, wrapper));
    }

    @ApiOperation("所有角色(下拉选择用)")
    @GetMapping("/all")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public R<?> all() {
        List<SysRole> roles = sysRoleService.list(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 1));
        return R.ok(roles);
    }

    @ApiOperation("角色详情(含menuIds)")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    public R<?> getInfo(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        if (role == null) {
            return R.fail("角色不存在");
        }
        // 获取关联菜单ID
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("role", role);
        data.put("menuIds", menuIds);
        return R.ok(data);
    }

    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    public R<?> add(@Validated @RequestBody SysRoleDTO dto) {
        // 检查角色名称唯一性
        long count = sysRoleService.count(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, dto.getRoleName()));
        if (count > 0) {
            return R.fail("角色名称已存在");
        }

        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleKey(dto.getRoleKey());
        role.setSort(dto.getSort());
        role.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        role.setRemark(dto.getRemark());
        sysRoleService.save(role);

        // 保存角色菜单关联
        saveRoleMenus(role.getId(), dto.getMenuIds());
        return R.ok();
    }

    @ApiOperation("编辑角色")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public R<?> edit(@Validated @RequestBody SysRoleDTO dto) {
        if (dto.getId() == null) {
            return R.fail("角色ID不能为空");
        }
        SysRole role = sysRoleService.getById(dto.getId());
        if (role == null) {
            return R.fail("角色不存在");
        }

        role.setRoleName(dto.getRoleName());
        role.setRoleKey(dto.getRoleKey());
        if (dto.getSort() != null) {
            role.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            role.setStatus(dto.getStatus());
        }
        role.setRemark(dto.getRemark());
        sysRoleService.updateById(role);

        // 更新角色菜单关联
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, dto.getId()));
        saveRoleMenus(dto.getId(), dto.getMenuIds());
        return R.ok();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    public R<?> remove(@PathVariable Long id) {
        sysRoleService.removeById(id);
        // 删除角色菜单关联
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        return R.ok();
    }

    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                sysRoleMenuMapper.insert(roleMenu);
            }
        }
    }
}
