package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_case")
public class CmsCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String coverImage;

    private String clientName;

    private String industry;

    private String content;

    /** 排序 */
    @TableField("sort")
    private Integer orderNum;

    /** 状态: 1-上架, 0-下架 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
