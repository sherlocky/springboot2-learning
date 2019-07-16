package com.sherlocky.springboot2.dubbo.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: zhangcx
 * @date: 2019/7/16 9:47
 */
@Service(version = "${base.service.version}")
public class DefaultBaseService implements BaseService {
    /**
     * The default value of ${dubbo.application.name}
     * is ${spring.application.name}
     */
    @Value("${dubbo.application.name}")
    private String applicationName;
    @Value("${base.service.name}")
    private String serviceName;

    @Override
    public String sayHello(String word) {
        System.out.println("Hello: " + word);
        return String.format("[%s]::[%s]::Hello, %s", applicationName, serviceName, word);
    }
}
