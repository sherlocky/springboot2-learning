package com.sherlocky.springboot2.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.sherlocky.springboot2.mybatis.dao.UserDAO;
import com.sherlocky.springboot2.mybatis.model.UserModel;
import com.sherlocky.springboot2.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2018/12/2 14:32
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    //private UserMapper userDAO;
    private UserDAO userDAO;

    @Override
    public Long create(UserModel user) {
        if (user == null) {
            return null;
        }
        return userDAO.create(user);
    }

    @Override
    public UserModel get(Long id) {
        if (id == null) {
            return null;
        }
        return userDAO.get(id);
    }

    /**
     * 查询全部列表,并做分页
     *
     * @param pageNum 开始页数
     * @param pageSize 每页显示的数据条数
     * @return
     */
    @Override
    public List<UserModel> list(int pageNum, int pageSize) {
        // 将参数传给这个方法就可以实现物理分页了，非常简单（页数从 1 开始）。
        PageHelper.startPage(pageNum, pageSize);
        return userDAO.list();
    }

    @Override
    public Long update(UserModel user) {
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            return null;
        }
        return userDAO.update(user);
    }

    @Override
    public Long remove(Long id) {
        if (id == null) {
            return null;
        }
        return userDAO.remove(id);
    }
}