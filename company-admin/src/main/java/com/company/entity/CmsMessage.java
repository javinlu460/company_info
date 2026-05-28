package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_message")
public class CmsMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String phone;

    private String email;

    private String content;

    private String address;

    /** 是否已读: 1-已读, 0-未读 */
    private Integer isRead;

    @TableField("reply_content")
    private String reply;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}