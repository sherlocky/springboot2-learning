package com.sherlocky.springboot2.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Dubbo 服务端Provider 示例
 * <p>可参考：<a href="http://dubbo.apache.org/zh-cn/docs/user/recommend.html">Dubbo文档-推荐用法</a></p>
 *
 * @author: zhangcx
 * @date: 2019/7/15 14:58
 */
@SpringBootApplication
public class DubboProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerApplication.class, args);
    }
}
