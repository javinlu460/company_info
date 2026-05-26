package com.company.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysMenuDTO {

    private Long id;

    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    private Integer menuType;

    private String path;

    private String component;

    private String perms;

    private String icon;

    private Integer sort;

    private Integer visible;

    private Integer status;
}
