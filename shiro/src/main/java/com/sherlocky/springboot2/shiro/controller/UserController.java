package com.sherlocky.springboot2.shiro.controller;

import com.sherlocky.springboot2.shiro.pojo.ResponseBean;
import com.sherlocky.springboot2.shiro.pojo.UserDTO;
import com.sherlocky.springboot2.shiro.pojo.UserPO;
import com.sherlocky.springboot2.shiro.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public ResponseBean login(@RequestBody UserDTO userDTO, HttpSession session) {
        // 也可以在Filter中校验账号 密码
        if (StringUtils.isEmpty(userDTO.getUsername())) {
            return ResponseBean.error("请输入用户名");
        }

        if (StringUtils.isEmpty(userDTO.getPassword())) {
            return ResponseBean.error("请输入密码");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(), userDTO.getPassword(), userDTO.getRememberMe());
        Subject subject = SecurityUtils.getSubject();
        try {
            // 登录时会经过 {@link UserRealm} 认证
            subject.login(token);
            session.setAttribute("user", subject.getPrincipal());
            return ResponseBean.success();
        } catch (AuthenticationException ex) {
            return ResponseBean.error("$$$ 账号或密码错误!");
        }
    }

    /**
     * 获取用户列表
     * @return
     */
    @GetMapping("/user")
    public List<UserPO> getUser() {
        return userService.listUsers();
    }


    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping("/user")
    public ResponseBean updateUser(UserPO user) {
        // subject获取当前登录的对象
        UserPO loginUser = (UserPO) SecurityUtils.getSubject().getPrincipal();
        // 数据库修改用户信息
        userService.updateUser(user);
        // 将修改的信息赋值给登录对象的User，前端就会跟着更新了
        loginUser.setName(user.getName());
        return ResponseBean.success();
    }
}