package com.sherlocky.springboot2.dubbo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ConfigCenterConfig;
import org.apache.dubbo.config.MetadataReportConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * Dubbo 服务端Provider 示例
 * <p>可参考：<a href="http://dubbo.apache.org/zh-cn/docs/user/recommend.html">Dubbo文档-推荐用法</a></p>
 *
 * @author: zhangcx
 * @date: 2019/7/15 14:58
 */
@Slf4j
@SpringBootApplication
public class DubboProducerApplication implements CommandLineRunner {
    @Resource
    RegistryConfig registryConfig;
    @Resource
    MetadataReportConfig metadataReportConfig;
    @Resource
    ConfigCenterConfig configCenterConfig;

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("#####################################");
        log.info("############# 注册中心");
        log.info(JSON.toJSONString(registryConfig));
        log.info("############# 元数据中心");
        log.info(JSON.toJSONString(metadataReportConfig));
        log.info("############# 配置中心");
        log.info(JSON.toJSONString(configCenterConfig));
        log.info("#####################################");
    }
}
