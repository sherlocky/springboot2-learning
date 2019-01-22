package com.sherlocky.springboot2.redis.service;

import com.sherlocky.springboot2.redis.dao.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangcx
 * @date: 2019/1/18 15:53
 */
@Service
public class UserService {
    // 模拟数据库
    private Map<Long, User> users = new ConcurrentHashMap<>();

    // @Cacheable: 表示先从缓存中通过定义的键查询，如果可以查询到数据，则返回；否则执行该方法，返回数据，并将返回结果保存到缓存中
    @Cacheable(value = "redisCache", key = "'redis_user_'+#id")
    public User getUserById(Long id) {
        System.out.println("$$$ 未从缓存中读取到，读取数据库。。");
        // 此处模拟从数据库读取用户
        return users.get(id);
    }

    // @CachePut: 表示将方法返回结果放到缓存中
    @CachePut(value = "redisCache", key = "'redis_user_'+#result.id")
    public User createUser(User user) {
        // 此处模拟数据库 保存新用户
        users.put(user.getId(), user);
        return user;
    }

    @CachePut(value = "redisCache", key = "'redis_user_'+#id", condition = "#result != 'null'")
    public User updateUserName(Long id, String userName) {
        // 此处调用 getUserById 方法，该方法缓存注解失效（@Cacheable 也是基于Spring AOP代理的，内部方法直接调用，是不走代理的，故会失效）
        // 所以这里还会走到方法内部，将查询到最新数据
        User u = this.getUserById(id);
        if (u == null) {
            return null;
        }
        u.setName(userName);
        // 此处模拟数据库 更新用户
        users.put(id, u);
        return u;
    }

     // @CacheEvict: 通过定义的键移除缓存，它有一个Boolean类型的配置项 beforeInvocation，表示在方法之前或者方法之后移除缓存，默认值为 false（方法之后移除缓存）
    @CacheEvict(value = "redisCache", key = "'redis_user_'+#id", beforeInvocation = false)
    public User deleteUser(Long id) {
        // 此处模拟数据库 删除用户
        return users.remove(id);
    }
}
