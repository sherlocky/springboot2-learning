package com.sherlocky.springboot2.web.mvc;

import com.sherlocky.springboot2.dao.UserRepository;
import com.sherlocky.springboot2.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SpringMVC 方式实现 Restful Controller 风格（等价于 {@link MyMvcController}）
 * <p>直接使用 @RestController 注解，通过它可以将返回的对象直接转化为JSON，
 * 这样就不需要在每个方法上使用 @ResponseBody 注解了。</p>
 * <p>使用 @RestController 注解后不能使用返回字符串的方式直接渲染视图(例如：JSP)了，
 * 可以采用 ModelAndView 形式返回。</p>
 *
 * @author: zhangcx
 * @date: 2018/01/25 15:03
 */
@RestController
@RequestMapping("/mvc/rest/users")
public class MyMvcRestController {
    private static final String USER_LIST_PATH_NAME = "userList";
    @Autowired
    private UserRepository userRepository;

    /** Springmvc 中使用 Thymeleaf 模板语言返回页面 */
    @RequestMapping("/hello")
    public ModelAndView hello(final Model model) {
        model.addAttribute("name", "我是DJ");
        model.addAttribute("id", "123456");
        return new ModelAndView("hello");
    }

    // Model 对象来进行数据绑定到视图
    @RequestMapping("/hello/list")
    public ModelAndView listPage(final Model model) {
        model.addAttribute("users", userRepository.findAll());
        // return 字符串，该字符串对应的目录在 resources/templates 下的模板名字
        // 一般会集中用常量管理模板视图的路径
        return new ModelAndView(USER_LIST_PATH_NAME);
    }

    /** 使用 SpringMVC 实现 Restful 接口 */
    /* 还可以指定 consumes 和 produces 两个属性 。
        consumes : 代表的是限制该方法接收什么类型的请求体（ body),
        produces : 代表的是限定返回的媒体类型，仅当 request 请求头中的（Accept）类型中包含该指定类型才返回
     */
    /*
      使用@ControllerAdvice 和 ＠ExceptionHandler 注解可以处理异常，
      @ControllerAdvice ：是用来定义控制器通知的，
      @ExceptionHandler ： 则是指定异常发生的处理方法
     */
    @GetMapping("")
    public List<User> listUsers() {
        return Stream.of(new User(1L), new User(2L), new User(3L)).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        return new User(userId);
    }
}