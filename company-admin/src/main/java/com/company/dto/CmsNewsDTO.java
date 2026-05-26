package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmsNewsDTO {

    private Long id;

    private Long categoryId;

    @NotBlank(message = "新闻标题不能为空")
    private String title;

    private String summary;

    private String content;

    private String coverImage;

    private String author;

    private Integer sort;

    private Integer status;

    private Integer isTop;
}
