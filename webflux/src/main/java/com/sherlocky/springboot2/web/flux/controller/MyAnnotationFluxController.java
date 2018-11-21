package com.sherlocky.springboot2.web.flux.controller;

import com.sherlocky.springboot2.domain.User;
import com.sherlocky.springboot2.web.flux.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 购买自<a href="https://gitbook.cn/gitchat/column/5acda6f6d7966c5ae1086f2b#catalog">【李强强 ·Spring Boot WebFlux 上手教程】</a>
 * <a href="https://github.com/JeffLi1993/springboot-learning-example">github地址</a>
 * <a href="https://gitee.com/jeff1993/springboot-learning-example">gitee地址</a>
 *
 * <h1>响应式 API</h1>
 * Reactor 框架是 Spring Boot Webflux 响应库依赖，通过 Reactive Streams 并与其他响应库交互。提供了 两种响应式 API : Mono 和 Flux。一般是将 Publisher 作为输入，在框架内部转换成 Reactor 类型并处理逻辑，然后返回 Flux 或 Mono 作为输出。
 *
 * <h1>编程模型</h1>
 * Spring 5 web 模块包含了 Spring WebFlux 的 HTTP 抽象。类似 Servlet API , WebFlux 提供了 WebHandler API 去定义非阻塞 API 抽象接口。可以选择以下两种编程模型实现：
 * <p>- 1.注解控制层。和 MVC 保持一致，WebFlux 也支持响应性 @RequestBody 注解。</p>
 * <p>- 2.功能性端点。基于 lambda 轻量级编程模型，用来路由和处理请求的小工具。和上面最大的区别就是，这种模型，全程控制了请求 - 响应的生命流程</p>
 *
 * <h1>内嵌容器</h1>
 * WebFlux 默认是通过 Netty 启动，并且自动设置了默认端口为 8080。另外还提供了对 Jetty、Undertow 等容器的支持。开发者自行在添加对应的容器 Starter 组件依赖，即可配置并使用对应内嵌容器实例。
 * <i>(但是要注意，必须是 Servlet 3.1+ 容器，如 Tomcat、Jetty；或者非 Servlet 容器，如 Netty 和 Undertow。)</i>
 *
 * <p>Starter 组件</p>
 * 跟 Spring Boot 大框架一样，Spring Boot Webflux 提供了很多 “开箱即用” 的 Starter 组件。Starter 组件是可被加载在应用中的 Maven 依赖项。只需要在 Maven 配置中添加对应的依赖配置，即可使用对应的 Starter 组件。例如，添加 spring-boot-starter-webflux 依赖，就可用于构建响应式 API 服务，其包含了 Web Flux 和 Tomcat 内嵌容器等。
 *
 * <h1>Spring Boot 2.0 WebFlux 组件</h1>
 * Spring Boot WebFlux 官方提供了很多 Starter 组件，每个模块会有多种技术实现选型支持，来实现各种复杂的业务需求：
 *
 * <p>- Web：Spring WebFlux</p>
 * <p>- 模板引擎：Thymeleaf</p>
 * <p>- 存储：Redis、MongoDB、Cassandra。不支持 MySQL</p>
 * <p>- 内嵌容器：Tomcat、Jetty、Undertow</p>
 *
 *
 * @author zhangcx
 * @date 2018/11/19 11:13
 */
@RestController
@RequestMapping("/flux/annotation/users")
public class MyAnnotationFluxController {
    @Autowired
    private UserHandler userHandler;

    /** 使用 WebFlux 实现 Restful 接口 */
    @GetMapping()
    public Flux<User> listUsers() {
        return userHandler.listUsers();
    }

    @GetMapping("/{userId}")
    public Mono<User> getUser(@PathVariable Long userId) {
        return userHandler.findUserById(userId);
    }

    @PostMapping
    public Mono<Long> createUser(@RequestBody User user) {
        return userHandler.createUser(user);
    }

    @PutMapping("/{userId}")
    public Mono<Long> updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userHandler.updateUser(userId, user);
    }

    @DeleteMapping(value = "/{userId}")
    public Mono<Long> deleteUser(@PathVariable Long userId) {
        return userHandler.deleteUser(userId);
    }
}