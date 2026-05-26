package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsNewsCategory;
import com.company.mapper.CmsNewsCategoryMapper;
import com.company.service.CmsNewsCategoryService;
import org.springframework.stereotype.Service;

@Service
public class CmsNewsCategoryServiceImpl extends ServiceImpl<CmsNewsCategoryMapper, CmsNewsCategory> implements CmsNewsCategoryService {
}