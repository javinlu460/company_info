package com.company.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ss")
public class PermissionService {

    /**
     * 判断当前用户是否拥有指定权限
     */
    public boolean hasPermi(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser)) {
            return false;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }

        // 如果拥有超级管理员权限 *:*:* ，则允许所有操作
        if (permissions.contains("*:*:*")) {
            return true;
        }

        return permissions.contains(permission);
    }
}
