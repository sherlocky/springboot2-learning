package com.sherlocky.springboot2.shirojwt.shiro.provider;


import com.sherlocky.springboot2.shirojwt.domain.vo.UserAccount;

/**
 * 数据库用户密码账户提供
 */
public interface AccountProvider {
    /**
     * 数据库用户密码账户提供
     *
     * @param account
     * @return
     */
    UserAccount loadAccount(String account);
}
