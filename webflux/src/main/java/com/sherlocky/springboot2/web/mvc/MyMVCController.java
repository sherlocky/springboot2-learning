package com.sherlocky.springboot2.web.mvc;

import com.sherlocky.springboot2.dao.UserRepository;
import com.sherlocky.springboot2.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private static final String USER_LIST_PATH_NAME = "userList";
    @Autowired
    private UserRepository userRepository;

    /** Springmvc 中使用 Thymeleaf 模板语言返回页面 */
    @RequestMapping("/hello")
    public String hello(final Model model) {
        model.addAttribute("name", "我是DJ");
        model.addAttribute("id", "123456");
        return "hello";
    }

    // Model 对象来进行数据绑定到视图
    @RequestMapping("/hello/list")
    public String listPage(final Model model) {
        model.addAttribute("users", userRepository.findAll());
        // return 字符串，该字符串对应的目录在 resources/templates 下的模板名字
        // 一般会集中用常量管理模板视图的路径
        return USER_LIST_PATH_NAME;
    }



    /** 使用 SpringMVC 实现 Restful 接口 */
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