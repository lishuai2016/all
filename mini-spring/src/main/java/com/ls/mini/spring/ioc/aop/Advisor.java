package com.ls.mini.spring.ioc.aop;

import org.aopalliance.aop.Advice;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 18:10
 */
public interface Advisor {
    Advice getAdvice();
}
