package com.company.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);
}