package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.common.result.R;
import com.company.dto.PageQuery;
import com.company.dto.SysUserDTO;
import com.company.entity.SysUser;
import com.company.entity.SysUserRole;
import com.company.mapper.SysUserRoleMapper;
import com.company.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public SysUserController(SysUserService sysUserService,
                             SysUserRoleMapper sysUserRoleMapper,
                             PasswordEncoder passwordEncoder) {
        this.sysUserService = sysUserService;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation("用户分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    public R<?> list(PageQuery pageQuery) {
        Page<SysUser> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            wrapper.like(SysUser::getUsername, pageQuery.getKeyword())
                    .or().like(SysUser::getNickname, pageQuery.getKeyword())
                    .or().like(SysUser::getPhone, pageQuery.getKeyword());
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        Page<SysUser> result = sysUserService.page(page, wrapper);
        // 清除密码
        result.getRecords().forEach(u -> u.setPassword(null));
        return R.ok(result);
    }

    @ApiOperation("用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public R<?> getInfo(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return R.fail("用户不存在");
        }
        user.setPassword(null);
        // 获取关联角色ID
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        List<Long> roleIds = new java.util.ArrayList<>();
        userRoles.forEach(ur -> roleIds.add(ur.getRoleId()));
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("user", user);
        data.put("roleIds", roleIds);
        return R.ok(data);
    }

    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    public R<?> add(@Validated @RequestBody SysUserDTO dto) {
        // 检查用户名唯一性
        long count = sysUserService.count(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        if (count > 0) {
            return R.fail("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        sysUserService.save(user);

        // 保存用户角色关联
        saveUserRoles(user.getId(), dto.getRoleIds());
        return R.ok();
    }

    @ApiOperation("编辑用户")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public R<?> edit(@Validated @RequestBody SysUserDTO dto) {
        if (dto.getId() == null) {
            return R.fail("用户ID不能为空");
        }
        SysUser user = sysUserService.getById(dto.getId());
        if (user == null) {
            return R.fail("用户不存在");
        }

        user.setUsername(dto.getUsername());
        if (StringUtils.isNotBlank(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }
        sysUserService.updateById(user);

        // 更新用户角色关联
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, dto.getId()));
        saveUserRoles(dto.getId(), dto.getRoleIds());
        return R.ok();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    public R<?> remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        // 删除用户角色关联
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        return R.ok();
    }

    @ApiOperation("修改用户状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public R<?> changeStatus(@PathVariable Long id, @RequestBody SysUserDTO dto) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setStatus(dto.getStatus());
        sysUserService.updateById(user);
        return R.ok();
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }
}
