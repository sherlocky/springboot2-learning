package com.sherlocky.springboot2.validation.constant;

/**
 * 状态枚举类
 */
public enum ResponseCode {

    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    ILLEGAL_ARGUMENT(2, "参数错误"),
    EMPTY_RESULT(3, "结果为空"),
    NEED_LOGIN(10, "需要登录");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}