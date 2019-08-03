package com.ls.lishuai.java.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/7 10:59
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface FruitIdParameter {
    int value() default -1;
}
