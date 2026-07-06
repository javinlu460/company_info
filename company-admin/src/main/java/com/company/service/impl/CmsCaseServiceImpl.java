package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsCase;
import com.company.mapper.CmsCaseMapper;
import com.company.service.CmsCaseService;
import org.springframework.stereotype.Service;

@Service
public class CmsCaseServiceImpl extends ServiceImpl<CmsCaseMapper, CmsCase> implements CmsCaseService {
}
