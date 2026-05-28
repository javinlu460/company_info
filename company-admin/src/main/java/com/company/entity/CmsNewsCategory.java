package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("cms_news_category")
public class CmsNewsCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父分类ID(非数据库字段) */
    @TableField(exist = false)
    private Long parentId;

    @TableField("name")
    private String categoryName;

    /** 排序 */
    @TableField("sort")
    private Integer orderNum;

    /** 状态: 1-正常, 0-禁用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    /** 子分类列表(非数据库字段) */
    @TableField(exist = false)
    private List<CmsNewsCategory> children;
}