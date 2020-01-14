# Spring Boot 优雅停止服务的几种方法

## 准备工作
注入一个 [Bean 对象](src/java/com/sherlocky/springboot2/shutdown/bean/ShutDownBean.java)，配置上[``preDestroy``方法](src/java/com/sherlocky/springboot2/shutdown/config/ShutDownConfiguration.java)。这样在服务停止的时候会调用 Bean 的该方法。可以在这个方法里打印日志，发送通知等。

## 1.Spring Boot actuator
引入相关依赖，打开 shutdown 节点，并暴露 shutdown web 调用。

执行以下命令，即可关闭服务：
```bash
curl -X POST http://localhost:8080/actuator/shutdown
```
关闭服务前会调用 Bean 对象的``preDestroy``方法。

## 2.关闭主程序启动时的 context
获取程序启动时候的 context，然后关闭主程序启动时的 context，[代码戳我](src/java/com/sherlocky/springboot2/shutdown/Springboot2ShutDownApplication.java)。  
这样关闭服务前也会调用 Bean 对象的``preDestroy``方法。

## 3.将进程好写入pid文件，kill 停止
可以在 Spring Boot 启动时将进程号写入一个``app.pid``文件，生成的路径可以自定义。
```java
SpringApplication application = new SpringApplication(Springboot2ShutDownApplication.class);
application.addListeners(new ApplicationPidFileWriter("/xxx/app.pid"));
application.run();
```

通过命令直接停止服务:
```bash
cat /xxx/app.id | xargs kill
```
kill 时 Bean 对象的``preDestroy``方法也会被调用的。

## 4.调用``SpringApplication.exit()``方法
[调用``SpringApplication.exit()``方法](src/java/com/sherlocky/springboot2/shutdown/Springboot2ShutDownApplication.java#exitApplication)也可以退出程序，同时将生成一个退出码，这个退出码可以传递给所有的 context。  
这个就是一个 JVM 的钩子，调用这个方法会把所有``preDestroy``的方法执行、停止，并传递具体退出码给所有 context。  
通过调用``System.exit(exitCode)``可以将这个错误码也传给 JVM。  
程序执行完后最后会输出：``Process finished with exit code 0``，给 JVM 一个 SIGNAL。

## 5.自定义实现 Controller 关闭服务
还可以自定义实现一个 [Controller](src/java/com/sherlocky/springboot2/shutdown/controller/ShutDownController.java)，在 Controller 中关闭 context。  
然后调用 Controller 方法退出程序:
```bash
curl -X POST http://localhost:8080/api/shutDownContext
```

**这种方法不会调用 Bean 的``preDestroy``方法...**