package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_news")
public class CmsNews {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    private String title;

    private String coverImage;

    private String summary;

    private String content;

    /** 状态: 1-发布, 0-草稿 */
    private Integer status;

    /** 作者 */
    private String author;

    /** 浏览量 */
    private Integer viewCount;

    /** 是否置顶: 1-是, 0-否 */
    private Integer isTop;

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