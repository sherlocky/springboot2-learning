package com.sherlocky.springboot2.shirojwt.service;

import com.sherlocky.springboot2.shirojwt.domain.bo.UserAccount;
import com.sherlocky.springboot2.shirojwt.domain.po.AuthUserDO;

public interface AccountService {

    UserAccount loadAccount(String account);

    boolean isAccountExistByUsername(String uid);

    boolean registerAccount(AuthUserDO authUser);

    String loadAccountRole(String account);
}
