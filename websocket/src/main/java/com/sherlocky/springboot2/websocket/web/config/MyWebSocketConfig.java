package com.sherlocky.springboot2.websocket.web.config;

import com.sherlocky.springboot2.websocket.web.handler.MyWebSocketHandler;
import com.sherlocky.springboot2.websocket.web.interceptor.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @description webSocket配置类，绑定前端连接端点url及其他信息
 */
@Configuration
@EnableWebSocket
public class MyWebSocketConfig implements WebSocketConfigurer {
    private static String WEBSOCKET_URL = "/websocket";
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;
    @Autowired
    private MyHandshakeInterceptor myHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 绑定前端连接端点url
        registry.addHandler(myWebSocketHandler, WEBSOCKET_URL).addInterceptors(myHandshakeInterceptor).withSockJS();
        // withSockJS: 开启SockJS支持
    }

/*    @Bean
    ServletWebServerFactory servletWebServerFactory(){
        return new TomcatServletWebServerFactory();
    }*/
}