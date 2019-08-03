package com.ls.mini.spring.ioc.aop;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 18:12
 *
 * 切入点的两个匹配连接点的方式：
 * 1、按照类信息；
 * 2、按照方法信息；
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
