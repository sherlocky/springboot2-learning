package com.sherlocky.springboot2.mybatis.service;

import com.sherlocky.springboot2.mybatis.model.UserModel;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2018/12/2 14:31
 */
public interface UserService {
    Long create(UserModel user);

    UserModel get(Long id);

    List<UserModel> list(int pageNum, int pageSize);

    Long update(UserModel user);

    Long remove(Long id);
}
