package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmsBannerDTO {

    private Long id;

    private String title;

    @NotBlank(message = "图片地址不能为空")
    private String imageUrl;

    private String linkUrl;

    private Integer sort;

    private Integer status;
}
