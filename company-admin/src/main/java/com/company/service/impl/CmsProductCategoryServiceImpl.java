package com.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsProductCategory;
import com.company.mapper.CmsProductCategoryMapper;
import com.company.service.CmsProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CmsProductCategoryServiceImpl extends ServiceImpl<CmsProductCategoryMapper, CmsProductCategory> implements CmsProductCategoryService {

    @Override
    public List<CmsProductCategory> getCategoryTree() {
        List<CmsProductCategory> allCategories = baseMapper.selectList(
                new LambdaQueryWrapper<CmsProductCategory>().orderByAsc(CmsProductCategory::getOrderNum));

        Map<Long, List<CmsProductCategory>> categoryMap = allCategories.stream()
                .collect(Collectors.groupingBy(CmsProductCategory::getParentId));

        allCategories.forEach(cat -> cat.setChildren(categoryMap.getOrDefault(cat.getId(), new ArrayList<>())));

        return categoryMap.getOrDefault(0L, new ArrayList<>());
    }
}