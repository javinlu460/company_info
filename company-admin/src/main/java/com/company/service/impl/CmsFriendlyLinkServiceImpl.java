package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsFriendlyLink;
import com.company.mapper.CmsFriendlyLinkMapper;
import com.company.service.CmsFriendlyLinkService;
import org.springframework.stereotype.Service;

@Service
public class CmsFriendlyLinkServiceImpl extends ServiceImpl<CmsFriendlyLinkMapper, CmsFriendlyLink> implements CmsFriendlyLinkService {
}