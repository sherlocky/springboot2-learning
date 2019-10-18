package com.sherlocky.springboot2.shirojwt.shiro.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 账户密码 Token 类 继承自 AuthenticationToken
 */
@Data
@AllArgsConstructor
public class PasswordAuthenticationToken implements AuthenticationToken {
    private static final long serialVersionUID = -983787780800181328L;
    /** 登录主体：可以是用户名/邮箱/手机号等 */
    private String account;
    private String password;
    private String timestamp;
    private String host;
    private String dynamicKey;

    @Override
    public Object getPrincipal() {
        return this.account;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }
}
