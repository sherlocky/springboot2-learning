package com.sherlocky.springboot2.websocket.web.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @description 前端页面与后台通信握手拦截器, 可用于完善定向发送信息功能。
 */
@Component
public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private static Log log = LogFactory.getLog(HttpSessionHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,Map<String, Object> attributes) throws Exception {
        // 此处可将用户信息放入WebSocketSession的属性当中，以便定向发送消息。
        attributes.put("WEBSOCKET_USERID", 1L);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("### 通过 MyHandshakeInterceptor 的 afterHandshake 拦截器！");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}