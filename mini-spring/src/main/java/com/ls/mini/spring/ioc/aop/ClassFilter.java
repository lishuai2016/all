package com.ls.mini.spring.ioc.aop;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 18:12
 *
 * 类匹配接口
 */
public interface ClassFilter {

    boolean matches(Class targetClass);
}
