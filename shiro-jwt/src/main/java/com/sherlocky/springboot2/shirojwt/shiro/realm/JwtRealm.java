package com.sherlocky.springboot2.shirojwt.shiro.realm;

import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import com.sherlocky.springboot2.shirojwt.shiro.constant.ShiroConstants;
import com.sherlocky.springboot2.shirojwt.shiro.token.JwtAuthenticationToken;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtUtils;
import io.jsonwebtoken.MalformedJwtException;
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

import java.util.Map;
import java.util.Set;

/**
 * JWT 认证领域
 */
@Component
public class JwtRealm extends AuthorizingRealm {
    private static final String JWT_PREFIX = "jwt:";
    private static final int JWT_PREFIX_LEN = JWT_PREFIX.length();
    private static final char LEFT = '{';
    private static final char RIGHT = '}';

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
        if (null == payload) {
            //令牌无效
            throw new AuthenticationException(JwtConstants.TOKEN_ERROR);
        }
        return new SimpleAuthenticationInfo("jwt:" + payload, jwt, this.getName());
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String payload = (String) principalCollection.getPrimaryPrincipal();
        // TODO 此处JSON 判断获取不合理，改一下
        // likely to be json, parse it:
        if (payload.startsWith(JWT_PREFIX) && payload.charAt(JWT_PREFIX_LEN) == LEFT
                && payload.charAt(payload.length() - 1) == RIGHT) {

            Map<String, Object> payloadMap = JwtUtils.readValue(payload.substring(JWT_PREFIX_LEN));
            Set<String> roles = StringUtils.splitToSet((String) payloadMap.get("roles"), ShiroConstants.ROLES_SEPARATOR);
            Set<String> permissions = StringUtils.splitToSet((String) payloadMap.get("perms"), ShiroConstants.ROLES_SEPARATOR);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if (null != roles && !roles.isEmpty()) {
                info.setRoles(roles);
            }
            if (null != permissions && !permissions.isEmpty()) {
                info.setStringPermissions(permissions);
            }
            return info;
        }
        return null;
    }
}
