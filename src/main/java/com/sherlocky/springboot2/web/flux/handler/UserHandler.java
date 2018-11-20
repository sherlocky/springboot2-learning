package com.sherlocky.springboot2.web.flux.handler;

import com.sherlocky.springboot2.dao.UserRepository;
import com.sherlocky.springboot2.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 处理器类 Handler
 * @author: zhangcx
 * @date: 2018/11/19 13:34
 */
// @Component 泛指组件，当组件不好归类的时候，使用该注解进行标注
@Component
public class UserHandler {
    // 使用 final 和 @Autowired 标注在构造器注入 UserRepository Bean：
    private final UserRepository userRepository;
    @Autowired
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 从返回值可以看出，Mono 和 Flux (是非阻塞写法，相当于回调方式)适用于两个场景，即：
     *
     * <p>- Mono：实现发布者，并返回 0 或 1 个元素，即单对象。</p>
     * <p>- Flux：实现发布者，并返回 N 个元素，即 List 列表对象。</p>
     *
     * Mono 常用的方法有：
     *
     * - Mono.create()：使用 MonoSink 来创建 Mono。
     * - Mono.justOrEmpty()：从一个 Optional 对象或 null 对象中创建 Mono。
     * - Mono.error()：创建一个只包含错误消息的 Mono。
     * - Mono.never()：创建一个不包含任何消息通知的 Mono。
     * - Mono.delay()：在指定的延迟时间之后，创建一个 Mono，产生数字 0 作为唯一值。
     *
     * Flux 其实是 Mono 的一个补充。
     */

    public Mono<Long> createUser(User user) {
        return Mono.create(userMonoSink -> userMonoSink.success(userRepository.save(user)));
    }

    // 批量查找
    public Flux<User> listByIds(final Flux<Long> userIds) {
        return userIds.flatMap(id -> Mono.justOrEmpty(userRepository.findUserById(id)));
    }

    // 通过id查找
    public Mono<User> findUserById(Long id) {
        return Mono.justOrEmpty(userRepository.findUserById(id)).switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }

    public Flux<User> listUsers() {
        return Flux.fromIterable(userRepository.findAll());
    }

    public Mono<Long> updateUser(Long userId, User user) {
        // Mono.just(user);
        return Mono.create(userMonoSink -> userMonoSink.success(userRepository.updateUser(userId, user)));
    }

    public Mono<Long> deleteUser(Long id) {
        // Mono.justOrEmpty(V);
        return Mono.create(userMonoSink -> userMonoSink.success(userRepository.deleteUser(id)));
    }
}
