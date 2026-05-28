package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_friendly_link")
public class CmsFriendlyLink {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String linkName;

    @TableField("url")
    private String linkUrl;

    private String logo;

    /** 状态: 1-正常, 0-禁用 */
    private Integer status;

    /** 排序 */
    @TableField("sort")
    private Integer orderNum;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}