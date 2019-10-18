package com.sherlocky.springboot2.shirojwt.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>自定义Authenticator认证器</p>
 *
 * 当配置了多个Realm时，我们通常使用的认证器是shiro自带的{@link ModularRealmAuthenticator},
 * 其中决定使用的Realm的是doAuthenticate()方法，只有一个的时候，就直接使用当前的Realm，
 * 如果有多个Realm就会根据Realm的supports返回值决定是否使用对应的Realm。
 */
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        List<Realm> realms = this.getRealms()
                .stream()
                .filter(realm -> {
                    return realm.supports(authenticationToken);
                })
                .collect(toList());
        return realms.size() == 1 ? this.doSingleRealmAuthentication(realms.iterator().next(), authenticationToken) : this.doMultiRealmAuthentication(realms, authenticationToken);
    }
}
