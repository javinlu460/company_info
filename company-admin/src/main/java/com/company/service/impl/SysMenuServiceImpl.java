package com.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.SysMenu;
import com.company.mapper.SysMenuMapper;
import com.company.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> allMenus = baseMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1)
                        .orderByAsc(SysMenu::getOrderNum));

        // 构建菜单树
        Map<Long, List<SysMenu>> menuMap = allMenus.stream()
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        allMenus.forEach(menu -> menu.setChildren(menuMap.getOrDefault(menu.getId(), new ArrayList<>())));

        // 返回顶级菜单
        return menuMap.getOrDefault(0L, new ArrayList<>());
    }
}