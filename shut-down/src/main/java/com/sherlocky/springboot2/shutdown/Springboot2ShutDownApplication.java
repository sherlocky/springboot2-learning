package com.sherlocky.springboot2.shutdown;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * springboot 优雅停机示例
 */
@SpringBootApplication
public class Springboot2ShutDownApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Springboot2ShutDownApplication.class, args);
        /**
         * 可通过关闭主程序启动时的context，来停止服务
         * 程序启动十秒后进行关闭。
         */
        /**
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ctx.close();
        */

        /**
         * 还可通过调用 SpringApplication.exit() 方法也可以退出程序
         */
        //exitApplication(ctx);
    }

    public static void exitApplication(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
        System.exit(exitCode);
    }
}
