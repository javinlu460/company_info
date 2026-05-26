package com.company.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String menuName;

    private String path;

    private String component;

    private String icon;

    /** 菜单类型: 1-目录, 2-菜单, 3-按钮 */
    private Integer menuType;

    private String perms;

    /** 排序 */
    private Integer orderNum;

    /** 状态: 1-正常, 0-禁用 */
    private Integer status;

    /** 是否可见: 1-显示, 0-隐藏 */
    private Integer visible;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    /** 子菜单列表(非数据库字段) */
    @TableField(exist = false)
    private List<SysMenu> children;
}