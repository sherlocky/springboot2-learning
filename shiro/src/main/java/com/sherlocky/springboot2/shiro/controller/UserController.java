package com.sherlocky.springboot2.shiro.controller;

import com.sherlocky.springboot2.shiro.pojo.ResponseBean;
import com.sherlocky.springboot2.shiro.pojo.UserDTO;
import com.sherlocky.springboot2.shiro.pojo.UserPO;
import com.sherlocky.springboot2.shiro.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 暂未考虑重复登录
     *
     * @param userDTO
     * @param session
     * @return
     */
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
            UserPO user = (UserPO) subject.getPrincipal();
            // 不能直接将 UserPO 返回，转换为 UserDTO 再返回（使用对象转换器）
            UserDTO returnUser = userDTO.convert4(user);
            session.setAttribute("user", returnUser);
            return ResponseBean.success(returnUser);
        } catch (AuthenticationException ex) {
            return ResponseBean.error("$$$ 账号或密码错误!");
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/user/login/info")
    public ResponseBean loginInfo() {
        Subject subject = SecurityUtils.getSubject();
        UserPO user = (UserPO) subject.getPrincipal();
        // 不能直接将 UserPO 返回，转换为 UserDTO 再返回（使用对象转换器）
        return ResponseBean.success(new UserDTO().convert4(user));
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("/user")
    public List<UserPO> getUser() {
        return userService.listUsers();
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping("/user")
    @RequiresRoles("system")
    public ResponseBean createUser(@RequestBody UserDTO user) {
        // 数据库修改用户信息
        userService.createUser(user.convert2User());
        return ResponseBean.success("创建用户成功~");
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PutMapping("/user")
    @RequiresRoles("system")
    public ResponseBean updateUser(@RequestBody UserDTO user, HttpSession session) {
        // subject获取当前登录的对象
        UserPO loginUser = (UserPO) SecurityUtils.getSubject().getPrincipal();
        // 如果修改的是当前登录用户
        if (Objects.equals(loginUser.getUsername(), user.getUsername())) {
            // 将修改的信息赋值给登录对象的User，前端就会跟着更新了
            loginUser.setName(user.getName());
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            if (userDTO != null) {
                userDTO.setName(user.getName());
            }
        }
        // 数据库修改用户信息
        userService.updateUser(user.convert2User());
        return ResponseBean.success("更新用户成功~");
    }
}