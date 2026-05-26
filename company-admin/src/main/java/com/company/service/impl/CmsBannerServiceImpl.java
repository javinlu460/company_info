package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsBanner;
import com.company.mapper.CmsBannerMapper;
import com.company.service.CmsBannerService;
import org.springframework.stereotype.Service;

@Service
public class CmsBannerServiceImpl extends ServiceImpl<CmsBannerMapper, CmsBanner> implements CmsBannerService {
}