package com.sherlocky.springboot2.shirojwt.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 密码匹配
 */
@Component
public class PasswordMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        return Objects.equals(authenticationToken.getPrincipal().toString(),
                        authenticationInfo.getPrincipals().getPrimaryPrincipal().toString())
                &&
                Objects.equals(authenticationToken.getCredentials().toString(),
                        authenticationInfo.getCredentials().toString());
    }
}
