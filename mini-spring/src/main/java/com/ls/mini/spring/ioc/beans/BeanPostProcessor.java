package com.ls.mini.spring.ioc.beans;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 21:09
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception;

    Object postProcessAfterInitialization(Object bean, String beanName) throws Exception;
}
