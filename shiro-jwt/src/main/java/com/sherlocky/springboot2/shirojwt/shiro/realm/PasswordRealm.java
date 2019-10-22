package com.sherlocky.springboot2.shirojwt.shiro.realm;


import com.sherlocky.common.util.Md5Utils;
import com.sherlocky.springboot2.shirojwt.domain.bo.UserAccount;
import com.sherlocky.springboot2.shirojwt.shiro.provider.AccountProvider;
import com.sherlocky.springboot2.shirojwt.shiro.token.PasswordAuthenticationToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

/**
 * 账户 密码认证领域
 */
@Component
public class PasswordRealm extends AuthorizingRealm {
    private AccountProvider accountProvider;

    /**
     * 此Realm只支持PasswordToken
     *
     * @return java.lang.Class<?>
     */
    @Override
    public Class<?> getAuthenticationTokenClass() {
        return PasswordAuthenticationToken.class;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordAuthenticationToken)) {
            return null;
        }

        if (null == authenticationToken.getPrincipal() || null == authenticationToken.getCredentials()) {
            throw new UnknownAccountException();
        }
        String account = (String) authenticationToken.getPrincipal();
        UserAccount userAccount = accountProvider.loadAccount(account);
        if (userAccount != null) {
            // 用盐对密码进行MD5加密
            ((PasswordAuthenticationToken) authenticationToken).setPassword(Md5Utils.md5(((PasswordAuthenticationToken) authenticationToken).getPassword() + userAccount.getSalt()));
            return new SimpleAuthenticationInfo(account, userAccount.getPassword(), getName());
        } else {
            return new SimpleAuthenticationInfo(account, "", getName());
        }

    }

    /**
     * <p>授权</p>
     * 这里只需要认证登录，成功之后派发 json web token 授权在那里进行
     *
     * @param principalCollection 1
     * @return org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    public void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }
}
