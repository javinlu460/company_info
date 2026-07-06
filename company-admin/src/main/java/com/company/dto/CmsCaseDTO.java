package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmsCaseDTO {

    private Long id;

    @NotBlank(message = "案例标题不能为空")
    private String title;

    private String description;

    private String coverImage;

    private String clientName;

    private String industry;

    private String content;

    private Integer sort;

    private Integer status;
}
