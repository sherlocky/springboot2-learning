package com.sherlocky.springboot2.zookeeper;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2ZookeeperTest {
    @Autowired
    ZookeeperClientService client;

    @Test
    public void testListChildren() {
        /*List<String> list = client.getChildren("/SpringIntegration-LockRegistry");
        print(list);*/
        printAllChildren("/", "");
    }

    private void printAllChildren(String path, String prefix) {
        print(prefix + ":" + path);
        List<String> list = client.getChildren(path);
        if (!CollectionUtils.isEmpty(list)) {
            for (String s : list) {
                printAllChildren(path + (path.endsWith("/") ? "" : "/") + s, prefix + "  ");
            }
        }
    }

    @Test
    public void testGetData() {
        print(client.getData("/zookeeper/config"));
        print(client.getData("/zookeeper/quota"));
        print(client.getData("/dubbo/config/dubbo/dubbo.properties"));

        print(client.getData("/dubbo/config/consumers"));
        print(client.getData("/dubbo/config/routers"));
        print(client.getData("/dubbo/config/configurators"));
        print(client.getData("/dubbo/config/providers"));
    }

    @Test
    public void testSetData() {
        // dubbo 在配置中心中，配置注册中心和元数据中心地址
        print(client.create("/dubbo/config/dubbo/dubbo.properties",
                "dubbo.registry.address=zookeeper://127.0.0.1:2181" +
                        "\n" +
                        "dubbo.metadata-report.address=zookeeper://127.0.0.1:2181"));
    }

    @Test
    public void testDelete() {
        print(client.deleteChildrenIfNeeded("/dubbo/metadata"));
        print(client.deleteChildrenIfNeeded("/dubbo/com.sherlocky.springboot2.dubbo.service.BaseService"));
    }

    private void print(String str) {
        System.out.println(str);
    }

    private void print(Object obj) {
        System.out.println(JSON.toJSONString(obj));
    }
}
