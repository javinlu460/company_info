package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmsFaqDTO {

    private Long id;

    @NotBlank(message = "问题不能为空")
    private String question;

    @NotBlank(message = "答案不能为空")
    private String answer;

    private String category;

    private Integer sort;

    private Integer status;
}
