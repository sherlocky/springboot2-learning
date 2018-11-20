package com.sherlocky.springboot2.web.flux.controller;

import com.sherlocky.springboot2.domain.User;
import com.sherlocky.springboot2.web.flux.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebFlux 中使用 Thymeleaf 模板语言返回页面
 * @author: zhangcx
 * @date: 2018/11/20 13:49
 */
// 注意不要使用@RestController注解,使用@RestController不能返回页面返回的是字符串(路径的字符串)。
@Controller
@RequestMapping("/flux")
public class MyWebFluxThymeleafController {
    private static final String USER_LIST_PATH_NAME = "userList";

    /** WebFlux 中使用 Thymeleaf 模板语言返回页面 */
    @Autowired
    private UserHandler userHandler;

    /**
     * Thymeleaf 是现代的模板语言引擎，可以独立运行也可以服务于 Web，主要目标是为开发提供天然的模板，并且能在 HTML 里面准确的显示。是新一代 Java 模板引擎，在 Spring 4 后推荐使用。
     *
     * 常用语法糖如下：
     * ${...}：变量表达式；
     * th:text：处理 Tymeleaf 表达式；
     * th:each：遍历表达式，可遍历的对象有，实现 java.util.Iterable、java.util.Map（遍历时取 java.util.Map.Entry）、array 等
     */
    // 返回值 Mono<String> 或者 String 都行，但是 Mono<String> 代表着 这个返回 View 也是回调的
    @GetMapping("/hello")
    public Mono<String> hello(final Model model) {
        model.addAttribute("name", "我是DJ");
        model.addAttribute("id", "123456");

        String path = "hello";
        return Mono.create(monoSink -> monoSink.success(path));
    }

    // Model 对象来进行数据绑定到视图
    @GetMapping("/hello/list")
    public String listPage(final Model model) {
        final Flux<User> userFluxList = userHandler.listUsers();
        // TODO 会报表达式错误（Exception evaluating SpringEL expression:）
        /**
         * 具体错误如下：
         * org.springframework.expression.spel.SpelEvaluationException: EL1008E: Property or field 'id' cannot be found on object of
         * type 'reactor.core.publisher.FluxIterable' - maybe not public or not valid?
         */
        // tomcat9 和 undertow2 均报同样的错，在netty4下可以正常返回页面（springboot 2.1.0 webflux默认容器就是netty4）
        // 在build.gradle文件中，可以屏蔽掉tomcat以启用netty
        model.addAttribute("users", userFluxList);
        // return 字符串，该字符串对应的目录在 resources/templates 下的模板名字
        // 一般会集中用常量管理模板视图的路径
        return USER_LIST_PATH_NAME;
    }
}
