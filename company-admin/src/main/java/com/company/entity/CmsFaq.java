package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_faq")
public class CmsFaq {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String question;

    private String answer;

    private String category;

    /** 排序 */
    @TableField("sort")
    private Integer orderNum;

    /** 状态: 1-显示, 0-隐藏 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
