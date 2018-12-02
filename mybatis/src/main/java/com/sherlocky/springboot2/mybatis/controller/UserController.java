package com.sherlocky.springboot2.mybatis.controller;

import com.sherlocky.springboot2.mybatis.model.UserModel;
import com.sherlocky.springboot2.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2018/12/2 14:35
 */
@RestController
@RequestMapping(value = "/mybatis/users")
public class UserController {
    // 默认页起始(从1开始)
    private static final String PAGE_NO = "1";
    // 默认页大小
    private static final String PAGE_SIZE = "5";
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserModel> list(@RequestParam(defaultValue = PAGE_NO) int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize) {
        return userService.list(pageNo, pageSize);
    }

    @GetMapping("/{userId}")
    public UserModel get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @PostMapping
    public Long create(@RequestBody UserModel user) {
        return userService.create(user);
    }

    @PutMapping()
    public Long update(@RequestBody UserModel user) {
        return userService.update(user);
    }

    @DeleteMapping(value = "/{userId}")
    public Long remove(@PathVariable Long userId) {
        return userService.remove(userId);
    }

}
