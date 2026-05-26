package com.company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.entity.CmsMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CmsMessageMapper extends BaseMapper<CmsMessage> {
}