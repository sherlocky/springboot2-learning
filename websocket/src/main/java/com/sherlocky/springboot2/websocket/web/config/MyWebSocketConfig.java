package com.sherlocky.springboot2.websocket.web.config;

import com.sherlocky.springboot2.websocket.web.handler.MyWebSocketHandler;
import com.sherlocky.springboot2.websocket.web.interceptor.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @description webSocket配置类，实现接口来配置Websocket请求的路径和拦截器及其他信息
 */
@Configuration
@EnableWebSocket
public class MyWebSocketConfig implements WebSocketConfigurer {
    // websocket请求路径
    private static String WEBSOCKET_URL = "/websocket/echo";
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;
    @Autowired
    private MyHandshakeInterceptor myHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 绑定前端连接端点url
        registry.addHandler(myWebSocketHandler, WEBSOCKET_URL)
                .setAllowedOrigins("*") // 必须添加
                .addInterceptors(myHandshakeInterceptor);
        // .withSockJS: 开启SockJS支持 (测试开启后，无法连接websocket，原因暂不明)
    }
}