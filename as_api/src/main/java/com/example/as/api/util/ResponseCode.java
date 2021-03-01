package com.example.as.api.util;

public enum ResponseCode {
    RC_SUCCESS(0, "SUCCESS."),
    RC_ACCOUNT_INVALID(5001, "账号不存在"),
    RC_PWD_INVALID(5002, "密码错误"),
    RC_NEED_LOGIN(5003, "请先登录"),
    RC_USER_FORBID(6001, "用户信息非法"),
    RC_CONFIG_INVALID(8001, "请输入正确的配置"),
    RC_ERROR(1001, "存在错误");
    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
