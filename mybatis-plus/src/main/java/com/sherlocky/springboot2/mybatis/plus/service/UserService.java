package com.sherlocky.springboot2.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sherlocky.springboot2.mybatis.plus.entity.User;
import com.sherlocky.springboot2.mybatis.plus.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author: zhangcx
 * @date: 2019/6/4 13:40
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
