package com.company.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.common.utils.JwtUtils;
import com.company.dto.LoginDTO;
import com.company.entity.SysRole;
import com.company.entity.SysUser;
import com.company.entity.SysUserRole;
import com.company.mapper.SysRoleMapper;
import com.company.mapper.SysUserRoleMapper;
import com.company.security.LoginUser;
import com.company.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SysUserService sysUserService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthController(SysUserService sysUserService,
                          SysUserRoleMapper sysUserRoleMapper,
                          SysRoleMapper sysRoleMapper,
                          JwtUtils jwtUtils,
                          PasswordEncoder passwordEncoder) {
        this.sysUserService = sysUserService;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public R<?> login(@Validated @RequestBody LoginDTO loginDTO) {
        SysUser sysUser = sysUserService.getOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginDTO.getUsername()));
        if (sysUser == null) {
            return R.fail("用户不存在");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), sysUser.getPassword())) {
            return R.fail("密码错误");
        }
        if (sysUser.getStatus() != 1) {
            return R.fail("用户已被禁用");
        }

        String token = jwtUtils.generateToken(sysUser.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", sysUser.getUsername());
        return R.ok(result);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public R<?> logout() {
        SecurityContextHolder.clearContext();
        return R.ok("登出成功");
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/userInfo")
    public R<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser)) {
            return R.unauthorized("未登录");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        // 清除密码不返回
        sysUser.setPassword(null);

        // 获取角色列表
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, sysUser.getId()));
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<SysRole> roles = roleIds.isEmpty() ? new ArrayList<>() :
                sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().in(SysRole::getId, roleIds));

        // 获取权限列表
        List<String> permissions = loginUser.getPermissions();

        Map<String, Object> result = new HashMap<>();
        result.put("user", sysUser);
        result.put("roles", roles);
        result.put("permissions", permissions);
        return R.ok(result);
    }

}