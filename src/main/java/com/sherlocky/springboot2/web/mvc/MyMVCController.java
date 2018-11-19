package com.sherlocky.springboot2.web.mvc;

import com.sherlocky.springboot2.web.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SpringMVC 方式实现 Controller
 * @author: zhangcx
 * @date: 2018/11/19 11:13
 */
@Controller
@RequestMapping("/mvc/users")
public class MyMVCController {

    @RequestMapping("")
    @ResponseBody
    public List<User> listUsers() {
        return Stream.of(new User(1L), new User(2L), new User(3L)).collect(Collectors.toList());
    }

    @RequestMapping("/{userId}")
    @ResponseBody
    public User getUser(@PathVariable Long userId) {
        return new User(userId);
    }
}