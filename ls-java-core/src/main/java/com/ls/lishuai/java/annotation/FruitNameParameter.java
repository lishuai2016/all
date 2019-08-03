package com.ls.lishuai.java.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/7 11:52
 */
@Target(PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitNameParameter {
    String value() default "";
}
