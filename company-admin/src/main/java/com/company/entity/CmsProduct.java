package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_product")
public class CmsProduct {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    private String productName;

    private String productCode;

    private String coverImage;

    private String description;

    private String detail;

    /** 状态: 1-上架, 0-下架 */
    private Integer status;

    /** 排序 */
    private Integer orderNum;

    /** 浏览量 */
    private Integer viewCount;

    /** 图片列表(JSON数组) */
    private String images;

    /** 是否推荐: 1-是, 0-否 */
    private Integer isRecommend;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}