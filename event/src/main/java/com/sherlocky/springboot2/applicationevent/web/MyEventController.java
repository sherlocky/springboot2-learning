package com.sherlocky.springboot2.applicationevent.web;

import com.sherlocky.springboot2.applicationevent.service.MyEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangcx
 * @date: 2019/5/30 15:04
 */
@RestController
@RequestMapping("/event")
public class MyEventController {
    @Autowired
    private MyEventService service;

    @GetMapping("")
    public String publishEvent(@RequestParam(name = "message", required = false, defaultValue = "") String msg) {
        service.publishEvent(msg);
        return "发布事件" + msg + "成功~";
    }
}
