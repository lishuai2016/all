package com.ls.mini.spring.ioc;

import com.ls.mini.spring.ioc.beans.BeanPostProcessor;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 21:51
 */
public class BeanInitializeLogger implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        System.out.println("Initialize bean " + beanName + " start!");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        System.out.println("Initialize bean " + beanName + " end!");
        return bean;
    }
}
