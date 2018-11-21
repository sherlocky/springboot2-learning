package com.sherlocky.springboot2.websocket.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;

/**
 * @description webSocket处理类
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    // TODO 暂存用户信息
    private static final ArrayList<WebSocketSession> users;

    static {
        users = new ArrayList<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.info("系统WebSocket连接已建立！");
        //此处可添加客户端接收用户
        logger.info(webSocketSession.getAttributes().get("WEBSOCKET_USERID").toString());
        users.add(webSocketSession);
    }

    /**
     * 给指定用户发送信息
     *
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        //获取指定用户ID
        Long userId = (Long) webSocketSession.getAttributes().get("WEBSOCKET_USERID");
        String message;
        logger.info("处理推送的消息");
        //判断客户端是否消息发送，不需要客户端与客户端的单向通信，此处可省略。
        if (!webSocketMessage.getPayload().equals("undefined")) {
            message = "client 发送的消息为：" + webSocketMessage.getPayload();
        } else {
            message = "推送测试信息 ---" + System.currentTimeMillis();
        }
        sendMessageToUser(userId, new TextMessage(message));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        logger.error("系统WebSocket传输错误，连接关闭！用户ID：" + webSocketSession.getAttributes().get("WEBSOCKET_USERID"), throwable);
        //移除异常用户信息
        users.remove(webSocketSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToUser(Long userId, TextMessage message) {
        logger.info("发送消息至用户！");
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("WEBSOCKET_USERID").equals(userId)) {
                sendSocketSessionMsg(user, message);
            }
        }
    }

    /**
     * 发送消息
     *
     * @param user    接收用户
     * @param message 消息
     */
    private boolean sendSocketSessionMsg(WebSocketSession user, TextMessage message) {
        String msg = message.getPayload();
        boolean sendSuccess = true;
        try {
            if (user.isOpen()) {
                synchronized (user) {
                    user.sendMessage(message);
                }
            } else {
                logger.error("WebSocket连接未打开，系统消息推送失败：" + msg);
                sendSuccess = false;
            }
        } catch (Exception e) {
            logger.error("系统消息推送失败：" + msg, e);
            sendSuccess = false;
        }
        return sendSuccess;
    }
}
