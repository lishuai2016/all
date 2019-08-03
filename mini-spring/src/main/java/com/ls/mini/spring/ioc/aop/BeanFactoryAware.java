package com.ls.mini.spring.ioc.aop;

import com.ls.mini.spring.ioc.beans.factory.BeanFactory;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 22:19
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws Exception;
}
