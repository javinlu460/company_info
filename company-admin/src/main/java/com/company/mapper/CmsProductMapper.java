package com.company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.entity.CmsProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CmsProductMapper extends BaseMapper<CmsProduct> {
}