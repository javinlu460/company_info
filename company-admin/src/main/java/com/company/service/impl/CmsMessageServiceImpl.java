package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsMessage;
import com.company.mapper.CmsMessageMapper;
import com.company.service.CmsMessageService;
import org.springframework.stereotype.Service;

@Service
public class CmsMessageServiceImpl extends ServiceImpl<CmsMessageMapper, CmsMessage> implements CmsMessageService {
}