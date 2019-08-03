package com.ls.mini.spring.ioc.aop;

import java.lang.reflect.Method;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 18:13
 *
 * 方法匹配接口
 */
public interface MethodMatcher {
    boolean matches(Method method, Class targetClass);
}
