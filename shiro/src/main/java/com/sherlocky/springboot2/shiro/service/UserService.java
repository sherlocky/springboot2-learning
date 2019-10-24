package com.sherlocky.springboot2.shiro.service;

import com.sherlocky.springboot2.shiro.pojo.UserPO;

import java.util.List;

/**
 * 用户 业务处理接口
 */
public interface UserService {

    UserPO getUserByUsername(String username);

    UserPO getUserByUid(String uid);

    List<UserPO> listUsers();

    boolean updateUser(UserPO user);
}
