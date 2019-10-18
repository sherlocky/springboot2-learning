package com.sherlocky.springboot2.shirojwt.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 【错误码枚举】
 * 约定 code 为 0 表示操作成功,
 * 1 或 2 等正数表示软件错误,
 * -1, -2 等负数表示系统错误.
 *
 * @author: zhangcx
 * @date: 2019/10/18 8:30
 */
public enum StatusCodeEnum {
    SUCCESS(0, "success"),
    INVALID_REQUEST(1, "无效请求"),
    SYSTEM_UNKNOWN_ERROR(-1, "系统繁忙，请稍后再试..."),

    //认证授权相关
    DYNAMIC_KEY_ISSUED_SUCCESS(1000, "动态秘钥签发成功"),
    DYNAMIC_KEY_ISSUED_FAIL(1001, "动态秘钥签发失败"),
    LOGIN_FAIL(1002, "用户密码认证失败"),

    JWT_ISSUED_SUCCESS(1003, "用户密码认证成功,JWT 签发成功,返回jwt"),
    JWT_ISSUED_FAIL(1004, "JWT 签发失败"),
    // jwt_real_token过期,jwt_refresh_token还未过期,服务器返回新的jwt,客户端需携带新返回的jwt对这次请求重新发起
    JWT_NEW(1005, "新的密钥"),
    // jwt_real_token过期,jwt_refresh_token过期(通知客户端重新登录)
    JWT_EXPIRED(1006, "密钥过期"),
    // jwt_token认证失败无效(包括用户认证失败或者jwt令牌错误无效或者用户未登录)
    JWT_ERROR(1007, "密钥认证失败"),
    // jwt token无权限访问此资源
    JWT_NO_PERMISSION(1008, "无权限"),

    // 用户相关
    ACCOUNT_REGISTER_SUCCESS(2000, "注册用户成功"),
    ACCOUNT_REGISTER_FAIL(2001, "用户不存在"),
    ACCOUNT_INFO_LACK(2002, "注册账户信息不完善"),
    ACCOUNT_EXISTED(2003, "注册用户已存在"),
    ACCOUNT_NO_EXISTS(2004, "用户不存在");

    private int code;
    private String msg;

    StatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "StatusCodeEnum{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public boolean isSuccessful() {
        return this.code == StatusCodeEnum.SUCCESS.getCode();
    }

    public boolean isFailed() {
        return !isSuccessful();
    }

    public static void main(String[] args) {
        System.out.println("| 代码  | 描述   |");
        System.out.println("| ---- | ---- |");
        Arrays.stream(StatusCodeEnum.values()).forEach((ce) -> {
            System.out.println("| " + StringUtils.rightPad(String.valueOf(ce.getCode()), 4) + " | " + ce.getMsg() + " |");
        });
    }
}
