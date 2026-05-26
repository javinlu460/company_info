package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsNews;
import com.company.mapper.CmsNewsMapper;
import com.company.service.CmsNewsService;
import org.springframework.stereotype.Service;

@Service
public class CmsNewsServiceImpl extends ServiceImpl<CmsNewsMapper, CmsNews> implements CmsNewsService {
}