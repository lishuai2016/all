package com.ls.lishuai.java.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/7 9:54
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface FruitAction {
    String value() default "I am peeling";
}
