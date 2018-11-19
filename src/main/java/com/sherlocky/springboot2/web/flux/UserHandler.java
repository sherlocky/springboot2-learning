package com.sherlocky.springboot2.web.flux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 处理器类 Handler
 * @author: zhangcx
 * @date: 2018/11/19 13:34
 */
@Component
public class UserHandler {
    // 用 Mono 作为返回对象，是因为返回包含了一个 ServerResponse 对象，而不是多个元素
    public Mono<ServerResponse> helloUser(ServerRequest request) {
        // ServerResponse 是对响应的封装，可以设置响应状态，响应头，响应正文。比如 ok 代表的是 200 响应码、MediaType 枚举是代表这文本内容类型、返回的是 String 的对象。
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("Hello, User!"));
    }
}
