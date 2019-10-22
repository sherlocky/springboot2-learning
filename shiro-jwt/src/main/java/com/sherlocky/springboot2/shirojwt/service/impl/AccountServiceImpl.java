package com.sherlocky.springboot2.shirojwt.service.impl;

import com.sherlocky.springboot2.shirojwt.domain.po.AuthUserDO;
import com.sherlocky.springboot2.shirojwt.domain.bo.UserAccount;
import com.sherlocky.springboot2.shirojwt.service.AccountService;
import com.sherlocky.springboot2.shirojwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * 账户安全相关业务类 TODO
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserService userService;

    @Override
    public UserAccount loadAccount(String account) throws DataAccessException {
        AuthUserDO au = userService.getUserByAccount(account);
        return au != null ? new UserAccount(au.getUsername(), au.getPassword(), au.getSalt()) : null;
    }

    @Override
    public boolean isAccountExistByUsername(String username) {
        return userService.getUserByAccount(username) != null;
    }

    @Override
    public boolean registerAccount(AuthUserDO account) throws DataAccessException {
        // 给新用户授权xx角色  TODO
        return userService.authorityUserRole(account.getUid(),0);
    }

    @Override
    public String loadAccountRole(String account) throws DataAccessException {
        return userService.loadAccountRole(account);
    }
}
