package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageDTO {

    private String name;

    private String phone;

    private String email;

    @NotBlank(message = "留言内容不能为空")
    private String content;

    private String address;
}
