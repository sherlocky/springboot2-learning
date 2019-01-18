package com.sherlocky.springboot2.web.reactive.websocket.config;

import com.sherlocky.springboot2.web.reactive.websocket.handler.ReactiveWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocketConfiguration 配置类
 *
 */
@Configuration
public class ReactiveWebSocketConfiguration {
    private static String WEBSOCKET_URL = "/reactive/websocket";

    /**
     * WebSocketHandlerAdapter 负责将 EchoHandler 处理类适配到 WebFlux 容器中
     */

    @Autowired // 此处直接注解在方法上，自动装配 ReactiveWebsocketHandler 到方法
    @Bean
    public HandlerMapping webSocketMapping(final ReactiveWebsocketHandler echoHandler) {
        final Map<String, WebSocketHandler> map = new HashMap<>();
        map.put(WEBSOCKET_URL, echoHandler);

        // SimpleUrlHandlerMapping 指定了 WebSocket 的路由配置
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        // 使用 map 指定 WebSocket 协议的路由，路由为 ws://localhost:8080/echo
        mapping.setUrlMap(map);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}