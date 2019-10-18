package com.sherlocky.springboot2.shirojwt.shiro.provider.impl;


import com.sherlocky.springboot2.shirojwt.domain.vo.UserAccount;
import com.sherlocky.springboot2.shirojwt.service.AccountService;
import com.sherlocky.springboot2.shirojwt.shiro.provider.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountProviderImpl implements AccountProvider {
    @Autowired
    private AccountService accountService;

    @Override
    public UserAccount loadAccount(String account) {
        return accountService.loadAccount(account);
    }
}
