package com.sherlocky.springboot2.shirojwt.shiro.realm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import com.sherlocky.springboot2.shirojwt.shiro.constant.ShiroConstants;
import com.sherlocky.springboot2.shirojwt.shiro.token.JwtAuthenticationToken;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtUtils;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Component;

/**
 * JWT 认证领域
 */
@Component
@Slf4j
public class JwtRealm extends AuthorizingRealm {
    @Override
    public Class<?> getAuthenticationTokenClass() {
        // 此realm只支持jwtToken
        return JwtAuthenticationToken.class;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof JwtAuthenticationToken)) {
            return null;
        }
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authenticationToken;
        String jwt = (String) jwtToken.getCredentials();
        String payload = null;
        try {
            // 预先解析Payload
            // 没有做任何的签名校验
            payload = JwtUtils.parseJwtPayload(jwt);
        } catch (MalformedJwtException e) {
            //令牌格式错误
            throw new AuthenticationException(JwtConstants.TOKEN_MALFORMED);
        } catch (Exception e) {
            //令牌无效
            throw new AuthenticationException(JwtConstants.TOKEN_ERROR);
        }
        if (!StringUtils.hasText(payload)) {
            //令牌无效
            throw new AuthenticationException(JwtConstants.TOKEN_ERROR);
        }
        return new SimpleAuthenticationInfo(payload, jwt, this.getName());
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String payload = (String) principalCollection.getPrimaryPrincipal();
        if (!JSON.isValidObject(payload)) {
            log.error("$$$ 该payload不是合法的json字符串");
            return null;
        }
        JSONObject payloadObject = JSON.parseObject(payload);
        String rolesStr = payloadObject.getString(JwtConstants.CLAIMS_ROLES);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (StringUtils.hasText(rolesStr)) {
            info.setRoles(StringUtils.splitToSet(payloadObject.getString(""), ShiroConstants.ROLES_SEPARATOR));
        }
        // 本例未引入权限，故此处权限Set会永远为空
        String permissionsStr = payloadObject.getString(JwtConstants.CLAIMS_PERMISSIONS);
        if (StringUtils.hasText(permissionsStr)) {
            info.setStringPermissions(StringUtils.splitToSet(payloadObject.getString(""), ShiroConstants.ROLES_SEPARATOR));
        }
        return info;
    }
}
