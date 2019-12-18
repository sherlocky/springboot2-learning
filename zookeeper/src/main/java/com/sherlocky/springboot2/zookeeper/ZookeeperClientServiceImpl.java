package com.sherlocky.springboot2.zookeeper;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: zhangcx
 * @date: 2019/12/18 14:27
 * @since:
 */
@Service
@Slf4j
public class ZookeeperClientServiceImpl implements ZookeeperClientService {
    @Autowired
    CuratorFramework curator;

    /**
     * 获取子列表
     * @param path
     * @return
     */
    @Override
    public List<String> getChildren(String path) {
        Assert.hasText(path, "path不能为空！");
        List<String> list = null;
        try {
            list = curator.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("$$$ 获取子节点失败", e);
        }
        return list;
    }

    /**
     * 创建节点
     * @param path
     */
    @Override
    public boolean create(String path) {
        Assert.hasText(path, "path不能为空！");
        boolean success = false;
        try {
            curator.create()
                    // 递归创建父节点
                    .creatingParentContainersIfNeeded()
                    // 默认就是持久节点
                    //.withMode(CreateMode.PERSISTENT)
                    // 标识所创建节点的权限，默认匿名权限
                    //.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(path);
            success = true;
        } catch (Exception e) {
            log.error("$$$ 创建节点失败", e);
        }
        return success;
    }

    /**
     * 创建节点
     * @param path
     * @param data
     */
    @Override
    public boolean create(String path, String data) {
        Assert.hasText(path, "path不能为空！");
        if (data == null) {
            return create(path);
        }
        boolean success = false;
        try {
            curator.create()
                    .creatingParentContainersIfNeeded()
                    .forPath(path, data.getBytes(StandardCharsets.UTF_8));
            success = true;
        } catch (Exception e) {
            log.error("$$$ 创建节点失败", e);
        }
        return success;
    }

    /**
     * 获取节点数据
     * @param path
     */
    @Override
    public String getData(String path) {
        Assert.hasText(path, "path不能为空！");
        Stat stat = new Stat();
        String result = null;
        try {
            // 读取数据并获得Stat信息
            byte[] data = curator.getData().storingStatIn(stat).forPath(path);
            result = new String(data, StandardCharsets.UTF_8);
            log.info(JSON.toJSONString(stat));
        } catch (Exception e) {
            log.error("$$$ 获取节点数据失败", e);
        }
        return result;
    }

    /**
     * 更新节点数据
     * @param path
     */
    @Override
    public boolean setData(String path, String data) {
        Assert.hasText(path, "path不能为空！");
        Assert.notNull(data, "节点数据不能为null！");
        boolean success = false;
        try {
            // 支持 compressed() 压缩，decompressed() 解压缩
            // 节点可以有版本号，更新数据时可以校验
            curator.setData().forPath(path, data.getBytes(StandardCharsets.UTF_8));
            success = true;
        } catch (Exception e) {
            log.error("$$$ 获取节点数据失败", e);
        }
        return success;
    }

    /**
     * 删除节点
     * @param path
     * @return
     */
    @Override
    public boolean delete(String path) {
        Assert.hasText(path, "path不能为空！");
        boolean success = false;
        try {
            curator.delete().forPath(path);
            success = true;
        } catch (Exception e) {
            log.error("$$$ 删除节点失败", e);
        }
        return success;
    }

    /**
     * 删除节点（递归删除子节点）
     * @param path
     * @return
     */
    @Override
    public boolean deleteChildrenIfNeeded(String path) {
        Assert.hasText(path, "path不能为空！");
        boolean success = false;
        try {
            curator.delete().deletingChildrenIfNeeded().forPath(path);
            success = true;
        } catch (Exception e) {
            log.error("$$$ 删除节点失败", e);
        }
        return success;
    }
}
