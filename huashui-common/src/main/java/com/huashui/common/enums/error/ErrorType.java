package com.huashui.common.enums.error;

public enum ErrorType {
    // 通用错误 1000xx
    PARAM_ERROR(100001, "参数校验失败"),
    UNAUTHORIZED(100002, "未登录或登录已过期"),
    FORBIDDEN(100003, "无权限访问"),
    CODE_ERROR(100005, "验证码错误"),
    CODE_NOT_EXISTS(100006, "验证码过期"),

    // 用户模块 2000xx
    USER_NOT_FOUND(200001, "用户不存在"),
    USER_ALREADY_EXISTS(200002, "用户已存在"),
    PASSWORD_ERROR(200003, "密码错误"),
    ACCOUNT_NOT_NULL(200005, "账号不能为空"),

    // 宿舍模块 3000xx
    ROOM_FULL(300001, "宿舍床位已满"),
    
    // 报修模块 4000xx
    REPAIR_ORDER_NOT_FOUND(400001, "报修单不存在");

    private final int code;
    private final String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}