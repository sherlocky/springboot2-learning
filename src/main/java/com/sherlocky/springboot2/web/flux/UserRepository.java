package com.sherlocky.springboot2.web.flux;

import com.sherlocky.springboot2.web.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 数据访问层 UserRepository
 *
 * @Repository 用于标注数据访问组件，即 DAO 组件。实现代码中使用名为 repository 的 Map 对象作为内存数据存储，并对对象具体实现了具体业务逻辑。UserRepository 负责将 Book 持久层（数据操作）相关的封装组织，完成新增、查询、删除等操作。
 *
 * 这里不会涉及到数据存储这块，具体数据存储会在后续介绍。
 * @author: zhangcx
 * @date: 2018/11/19 17:28
 */
@Repository
public class UserRepository {
    private ConcurrentMap<Long, User> repository = new ConcurrentHashMap<>();

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Long save(User user) {
        Long id = idGenerator.incrementAndGet();
        user.setId(id);
        repository.put(id, user);
        return id;
    }

    public Collection<User> findAll() {
        return repository.values();
    }


    public User findUserById(Long id) {
        return repository.get(id);
    }

    public Long updateUser(User user) {
        repository.put(user.getId(), user);
        return user.getId();
    }

    public Long deleteUser(Long id) {
        repository.remove(id);
        return id;
    }
}
