package com.ls.lishuai.java.annotation;

import java.lang.annotation.*;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/6 20:38
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}
