package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmsSolutionDTO {

    private Long id;

    @NotBlank(message = "方案标题不能为空")
    private String title;

    private String summary;

    private String content;

    private String coverImage;

    private String tags;

    private Integer sort;

    private Integer status;
}
