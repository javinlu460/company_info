package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsProduct;
import com.company.mapper.CmsProductMapper;
import com.company.service.CmsProductService;
import org.springframework.stereotype.Service;

@Service
public class CmsProductServiceImpl extends ServiceImpl<CmsProductMapper, CmsProduct> implements CmsProductService {
}