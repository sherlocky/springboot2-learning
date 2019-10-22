package com.sherlocky.springboot2.shirojwt.shiro.matcher;


import com.sherlocky.springboot2.shirojwt.domain.bo.JwtAccount;
import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;


/**
 * JWT token 匹配器
 */
@Component
public class JwtMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String jwt = (String) authenticationInfo.getCredentials();
        JwtAccount jwtAccount = null;
        try {
            jwtAccount = JwtUtils.parseJwt(jwt);
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            // 令牌错误
            throw new AuthenticationException(JwtConstants.TOKEN_ERROR);
        } catch (ExpiredJwtException e) {
            // 令牌过期
            throw new AuthenticationException(JwtConstants.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new AuthenticationException(JwtConstants.TOKEN_ERROR);
        }
        if (null == jwtAccount) {
            throw new AuthenticationException(JwtConstants.TOKEN_ERROR);
        }
        return jwtAccount != null;
    }
}
