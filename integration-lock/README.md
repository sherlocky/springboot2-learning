# Spring Integration 实现分布式锁
## 分布式锁
分布式锁是一种悲观锁，需要满足一下特性
- 1.互斥性：每个线程的创建的锁是互斥的，即A线程创建来锁和B线程创建的锁不能同时锁住。
- 2.排他性：每个线程的创建的锁是排他的，即当前线程创建的锁只有当前线程能够访问（加锁，解锁）别的线程不允许访问这两者都继承了

## Spring Integration 提供了全局锁
目前为这几种存储提供了实现：Gemfire、JDBC、Redis、Zookeeper。
它们使用相同的API抽象，不论使用哪种存储，开发者的编码体验都是一样的，如果需要更换实现，只需要修改依赖和配置就可以了，无需修改代码。

### RedisLockRegistry
需要依赖 Redis，Redis 集群在分布式中常用于需要高并发，分区容错，高可用的场景。

Redis 在分布式锁的使用可参考[官方文档](https://redis.io/topics/distlock)，Spring 对redis这种特性进行封装，使用redis的策略在大部分高并发的场景中都可以满足。

Redis 实现获取到的锁为``org.springframework.integration.redis.util.RedisLockRegistry.RedisLock``。

Gradle 依赖：
```gradle
implementation('org.springframework.boot:spring-boot-starter-integration')
implementation('org.springframework.boot:spring-boot-starter-data-redis')
implementation('org.springframework.integration:spring-integration-redis')
```

### ZookeeperLockRegistry
需要依赖 Zookpeer，Zookpeer 集群满足来分布式场景下的一致性，分区容错性。
所以``ZookeeperLockRegistry``在不是特别大的并发下能够完美的保证线程同步。

获取到的锁为``org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry.ZkLock``。

Gradle 依赖：
```gradle
implementation('org.springframework.integration:spring-integration-zookeeper')
// Netflix公司开源的一套Zookeeper客户端框架
implementation 'org.apache.curator:curator-recipes:2.13.0'
```

## 锁的使用
两种分布式锁实现都继承了``LockRegistry``。故可通过``LockRegistry``来统一声明变量，获取锁。

## 阿里开发规约对锁使用的要求
>【强制】在使用阻塞等待获取锁的方式中，必须在 try 代码块之外，并且在加锁方法与 try 代
码块之间没有任何可能抛出异常的方法调用，避免加锁成功后，在 finally 中无法解锁。

- 说明一：如果在 lock 方法与 try 代码块之间的方法调用抛出异常，那么无法解锁，造成其它线程无法成功
获取锁。
- 说明二：如果 lock 方法在 try 代码块之内，可能由于其它方法抛出异常，导致在 finally 代码块中，
unlock 对未加锁的对象解锁，它会调用 AQS 的 tryRelease 方法（取决于具体实现类），抛出
IllegalMonitorStateException 异常。
- 说明三：在 Lock 对象的 lock 方法实现中可能抛出 unchecked 异常，产生的后果与说明二相同。

>【强制】在使用尝试机制来获取锁的方式中，进入业务代码块之前，必须先判断当前线程是否持有锁。锁的释放规则与锁的阻塞等待方式相同。
- 说明：Lock 对象的 unlock 方法在执行时，它会调用 AQS 的 tryRelease 方法（取决于具体实现类），如果当前线程不持有锁，则抛出 IllegalMonitorStateException 异常。
