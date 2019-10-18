package com.sherlocky.springboot2.shirojwt.service;

import com.sherlocky.springboot2.shirojwt.domain.vo.UserAccount;
import com.sherlocky.springboot2.shirojwt.domain.vo.AuthUser;

public interface AccountService {

    UserAccount loadAccount(String account);

    boolean isAccountExistByUid(String uid);

    boolean registerAccount(AuthUser authUser);

    String loadAccountRole(String account);
}
