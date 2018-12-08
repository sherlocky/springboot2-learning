## starter demo
一个 starter demo 模拟实现自动配置，官方称为：**``configuration metadata file``**。

官方文档摘要（[戳我看全文](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor)）：
>You can easily generate your own configuration metadata file from items annotated with ``@ConfigurationProperties`` by using the ``spring-boot-configuration-processor`` jar. The jar includes a Java annotation processor which is invoked as your project is compiled. To use the processor, include a dependency on ``spring-boot-configuration-processor``.

基本流程：
- 添加依赖（可参考： build.gradle）
- 属性配置（可参考： DemoServiceProperties）
- 判断依据类（可参考： DemoService）
- 自动配置类（可参考： DemoServiceAutoConfiguration）
- 注册配置（可参考：spring.factories）: 想要自动配置生效，必须注册自动配置类