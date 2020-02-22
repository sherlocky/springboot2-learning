package com.sherlocky.springboot2.zookeeper;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2019/12/18 14:26
 * @since:
 */
public interface ZookeeperClientService {
    /**
     * 获取子列表
     * @param path
     * @return
     */
    List<String> getChildren(String path);
    /**
     * 创建节点
     * @param path
     */
    boolean create(String path);
    /**
     * 创建节点
     * @param path
     * @param data
     */
    boolean create(String path, String data);
    /**
     * 获取节点数据
     * @param path
     */
    String getData(String path);
    /**
     * 更新节点数据
     * @param path
     */
    boolean setData(String path, String data);
    /**
     * 删除节点
     * @param path
     * @return
     */
    boolean delete(String path);
    /**
     * 删除节点（递归删除子节点）
     * @param path
     * @return
     */
    boolean deleteChildrenIfNeeded(String path);
}
