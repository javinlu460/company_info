package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsFaq;
import com.company.mapper.CmsFaqMapper;
import com.company.service.CmsFaqService;
import org.springframework.stereotype.Service;

@Service
public class CmsFaqServiceImpl extends ServiceImpl<CmsFaqMapper, CmsFaq> implements CmsFaqService {
}
