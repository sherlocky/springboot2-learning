package com.sherlocky.springboot2.shirojwt.shiro.token;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWT token 类 继承自 AuthenticationToken
 */
@Data
public class JwtAuthenticationToken implements AuthenticationToken {
    private static final long serialVersionUID = 1855670237444723736L;
    /**
     * 用户账户标识
     */
    private String account;
    /**
     * 用户的IP
     */
    private String ipHost;
    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * json web token值
     */
    private String jwt;

    public JwtAuthenticationToken(String ipHost, String deviceInfo, String jwt, String account) {
        this.ipHost = ipHost;
        this.deviceInfo = deviceInfo;
        this.jwt = jwt;
        this.account = account;
    }

    @Override
    public Object getPrincipal() {
        return this.account;
    }

    @Override
    public Object getCredentials() {
        return this.jwt;
    }
}
