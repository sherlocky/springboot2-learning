package com.sherlocky.springboot2.shirojwt.shiro.realm;


import com.sherlocky.springboot2.shirojwt.shiro.matcher.JwtMatcher;
import com.sherlocky.springboot2.shirojwt.shiro.matcher.PasswordMatcher;
import com.sherlocky.springboot2.shirojwt.shiro.provider.AccountProvider;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 多 Realms 管理器
 */
@Component
public class RealmsManager {
    @Autowired
    private PasswordMatcher passwordMatcher;
    @Autowired
    private JwtMatcher jwtMatcher;
    @Autowired
    private AccountProvider accountProvider;

    public List<Realm> initListRealms() {
        List<Realm> realmList = new LinkedList<>();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setAccountProvider(accountProvider);
        passwordRealm.setCredentialsMatcher(passwordMatcher);
        // 设置了 AuthenticationTokenClass，token匹配（相同或为其子类）时 Realm 的 supports 方法会返回 true
        // 子类中已覆盖了 getAuthenticationTokenClass() 方法
        //passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(jwtMatcher);
        //jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }
}
