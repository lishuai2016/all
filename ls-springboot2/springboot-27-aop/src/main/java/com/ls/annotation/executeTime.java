package com.ls.annotation;

import java.lang.annotation.*;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-18
 * 用于记录方法的执行时间，添加在方法上即可,注意用来加在service层方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface executeTime {
    String value() default "";
}
