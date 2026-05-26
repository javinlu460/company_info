package com.company.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.entity.CmsProductCategory;

import java.util.List;

public interface CmsProductCategoryService extends IService<CmsProductCategory> {

    /**
     * 获取分类树
     */
    List<CmsProductCategory> getCategoryTree();
}