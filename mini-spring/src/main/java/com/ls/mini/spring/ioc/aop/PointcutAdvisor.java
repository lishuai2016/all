package com.ls.mini.spring.ioc.aop;


/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 18:11
 */
public interface PointcutAdvisor extends Advisor{

    Pointcut getPointcut();
}
