package com.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.entity.CmsSolution;
import com.company.mapper.CmsSolutionMapper;
import com.company.service.CmsSolutionService;
import org.springframework.stereotype.Service;

@Service
public class CmsSolutionServiceImpl extends ServiceImpl<CmsSolutionMapper, CmsSolution> implements CmsSolutionService {
}
