package com.sherlocky.springboot2.shiro.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sherlocky.springboot2.shiro.pojo.UserPO;
import com.sherlocky.springboot2.shiro.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * 用户 业务类
 * <p>暂时不读写数据库，模拟用户CURD操作</p>
 */
@Service
public class UserServiceImpl implements UserService {
    private UserPO admin;
    @PostConstruct
    void init() {
        String password = "admin", salt = "system";
        admin = UserPO.builder()
                .uid(1L)
                .username("admin")
                .name("管理员")
                .salt(salt)
                // MD5(admin+盐) 加密后的字符串
                .password(UserPO.passwordEncrypt(password, salt))
                //.password("033766688d73f3611a3f3caff4fc45cf")
                .roles(Sets.newHashSet("system"))
                // 6位 盐
                .build();
    }

    @Override
    public UserPO getUserByUsername(String username) {
        if (Objects.equals(admin.getUsername(), username)) {
            return admin;
        }
        return null;
    }

    @Override
    public UserPO getUserByUid(String uid) {
        if (Objects.equals(admin.getUid(), uid)) {
            return admin;
        }
        return null;
    }

    @Override
    public List<UserPO> listUsers() {
        return Lists.newArrayList(admin);
    }

    @Override
    public boolean updateUser(UserPO user) {
        // 。。。
        return true;
    }

    @Override
    public boolean createUser(UserPO user) {
        // 。。。
        return true;
    }
}
