package com.sherlocky.springboot2.web.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 路由器类 Router
 * @author: zhangcx
 * @date: 2018/11/19 13:36
 */
@Configuration
public class UserRouter {
    // RouterFunctions 对请求路由处理类，即将请求路由到处理器。这里将一个 GET 请求 /xxx 路由到处理器 userHandler 的 helloUser 方法上。跟 Spring MVC 模式下的 HandleMapping 的作用类似。
    // RouterFunctions.route(RequestPredicate, HandlerFunction) 方法，对应的入参是请求参数和处理函数，如果请求匹配，就调用对应的处理器函数。
    @Bean
    public RouterFunction<ServerResponse> routeUser(UserHandler userHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/flux/clazz/users/hello")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        userHandler::helloUser);
    }
}
