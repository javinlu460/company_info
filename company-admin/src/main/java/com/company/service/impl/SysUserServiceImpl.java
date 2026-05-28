package com.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.SysMenu;
import com.company.entity.SysRole;
import com.company.entity.SysRoleMenu;
import com.company.entity.SysUser;
import com.company.entity.SysUserRole;
import com.company.mapper.SysMenuMapper;
import com.company.mapper.SysRoleMapper;
import com.company.mapper.SysRoleMenuMapper;
import com.company.mapper.SysUserMapper;
import com.company.mapper.SysUserRoleMapper;
import com.company.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;

    public SysUserServiceImpl(SysUserRoleMapper sysUserRoleMapper,
                              SysRoleMapper sysRoleMapper,
                              SysRoleMenuMapper sysRoleMenuMapper,
                              SysMenuMapper sysMenuMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        // 获取用户角色列表
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        // 判断是否为超级管理员角色
        List<SysRole> roles = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>().in(SysRole::getId, roleIds));
        boolean isAdmin = roles.stream().anyMatch(r -> "admin".equals(r.getRoleKey()));
        if (isAdmin) {
            // 超级管理员拥有所有权限
            List<String> allPerms = new ArrayList<>();
            allPerms.add("*:*:*");
            return allPerms;
        }

        // 获取角色对应的菜单ID列表
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds));
        if (roleMenus.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        // 获取菜单权限标识列表
        List<SysMenu> menus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().in(SysMenu::getId, menuIds)
                        .eq(SysMenu::getStatus, 1)
                        .isNotNull(SysMenu::getPerms));

        return menus.stream().map(SysMenu::getPerms).distinct().collect(Collectors.toList());
    }
}