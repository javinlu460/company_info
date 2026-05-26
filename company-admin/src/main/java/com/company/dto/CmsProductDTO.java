package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmsProductDTO {

    private Long id;

    private Long categoryId;

    @NotBlank(message = "产品名称不能为空")
    private String title;

    private String description;

    private String content;

    private String coverImage;

    private String images;

    private Integer sort;

    private Integer status;

    private Integer isRecommend;
}
