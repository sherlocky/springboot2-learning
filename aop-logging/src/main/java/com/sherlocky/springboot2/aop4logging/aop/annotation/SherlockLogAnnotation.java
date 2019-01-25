package com.sherlocky.springboot2.aop4logging.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义日志注解
 * @author: zhangcx
 * @date: 2019/1/25 10:20
 */
/* @Target: 标注这个类它可以标注的位置,常用的元素类型(ElementType) */
/* @Retention: 标注这个注解的注解保留时期 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SherlockLogAnnotation {

    public String value() default "";
}
