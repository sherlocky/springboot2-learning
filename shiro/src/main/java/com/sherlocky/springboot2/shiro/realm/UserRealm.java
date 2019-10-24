package com.sherlocky.springboot2.shiro.realm;

import com.sherlocky.springboot2.shiro.pojo.UserPO;
import com.sherlocky.springboot2.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 加这一步的目的是在Post请求的时候会先进认证，然后再到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new UnknownAccountException();
        }
        UserPO user = userService.getUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UnknownAccountException();
        }
        if (!user.getValid()) {
            throw new LockedAccountException();
        }
        /**
         * 启用了加盐的密码加密校验，也可以不使用（不传递salt参数即可）
         * @see UserPO#passwordEncrypt(String, String)}
         * @see com.sherlocky.springboot2.shiro.config.ShiroConfigure#hashedCredentialsMatcher()
         */
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        return info;
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthenticationException("$$$ PrincipalCollection 参数不能为空。");
        }
        UserPO user = (UserPO) getAvailablePrincipal(principalCollection);
        if (ObjectUtils.isEmpty(user)) {
            throw new AuthenticationException("$$$ 用户不存在");
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 设置用户角色
        if (!ObjectUtils.isEmpty(user.getRoles())) {
            info.setRoles(user.getRoles());
        }
        return info;
    }
}