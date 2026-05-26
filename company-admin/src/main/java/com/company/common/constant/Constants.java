package com.company.common.constant;

public class Constants {

    /** 成功状态码 */
    public static final int SUCCESS = 200;

    /** 未认证 */
    public static final int UNAUTHORIZED = 401;

    /** 无权限 */
    public static final int FORBIDDEN = 403;

    /** 系统错误 */
    public static final int ERROR = 500;

    /** JWT Token前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** JWT Header Key */
    public static final String TOKEN_HEADER = "Authorization";

    /** 逻辑删除 - 已删除 */
    public static final int DELETED = 1;

    /** 逻辑删除 - 未删除 */
    public static final int NOT_DELETED = 0;

    /** 超级管理员角色ID */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;
}