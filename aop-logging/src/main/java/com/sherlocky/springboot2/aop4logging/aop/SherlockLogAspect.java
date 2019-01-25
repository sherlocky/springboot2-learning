package com.sherlocky.springboot2.aop4logging.aop;

import cn.hutool.core.date.DateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 自定义日志切面
 * @author: zhangcx
 * @date: 2019/1/25 10:24
 */
@Aspect
@Component
/* @Order(1)标记切面类的处理优先级,i值越小,优先级别越高.PS:可以注解到类,也能注解到方法上 */
public class SherlockLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SherlockLogAspect.class);

    // 定义切点 Pointcut
    @Pointcut("@annotation(com.sherlocky.springboot2.aop4logging.aop.annotation.SherlockLogAnnotation)")//自定义接口实现
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void doBefore(JoinPoint joinPoint){
        // 接收到请求，记录请求内容
        logger.info("[SherlockLogAspect.doBefore()]");
        //有格式的打印日志，收集日志信息到ES
        logger.info(DateUtil.now() + "|" +
                joinPoint.getSignature().getDeclaringTypeName() + "|" + Arrays.toString(joinPoint.getArgs()));
        // 记录下请求内容
        logger.info("[CLASS_METHOD] : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature
                ().getName());
        logger.info("[ARGS] : " + Arrays.toString(joinPoint.getArgs()));
        //类方法
        logger.info("[class_method]={}", joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName());
        //参数
        logger.info("[args]={}", joinPoint.getArgs());
    }

    @AfterReturning(returning = "ret", pointcut = "logPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("[RETURN] : " + ret);
    }
}
