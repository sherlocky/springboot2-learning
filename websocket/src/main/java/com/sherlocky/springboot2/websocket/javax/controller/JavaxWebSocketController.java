package com.sherlocky.springboot2.websocket.javax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/javax/websocket/web")
public class JavaxWebSocketController {

    @RequestMapping()
    public String init() {
        return "success";
    }

}