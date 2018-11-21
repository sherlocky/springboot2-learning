package com.sherlocky.springboot2.websocket.web.controller;

import com.sherlocky.springboot2.websocket.web.handler.MyWebSocketHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/websocket/api")
public class MyWebSocketController {
    private static Log log = LogFactory.getLog(MyWebSocketController.class);

    @Bean//这个注解会从Spring容器拿出Bean
    public MyWebSocketHandler webSocketHandler() {
        return new MyWebSocketHandler();
    }

    @RequestMapping("/login/{userId}")
    @ResponseBody
    public Map<String, Object> login(@PathVariable Long userId, HttpServletRequest request) throws Exception {
        log.info("############# " + userId + " 登录了 WebSocket。");
        HttpSession session = request.getSession(true);
        session.setAttribute("SESSION_WEBSOCKET_USERID", userId);

        //stream法初始化map
        Map map = Stream.of(1, 2, 3, 4).collect(Collectors.toMap(o -> o, integer -> integer, (o, o2) -> o2, HashMap::new));
        System.out.println(map);

        // 双括号法初始化Map
        return new HashMap<String, Object>() {
            {
                put("userId", userId);
                put("message", "登录了");
            }
        };
    }

    @RequestMapping("/send/{userId}")
    @ResponseBody
    public String send(@PathVariable Long userId) {
        log.info("############# 给 " + userId + " 发送消息。");
        webSocketHandler().sendMessageToUser(userId, new TextMessage("你好，测试！！！！"));
        return "给用户发送消息成功！";
    }
}