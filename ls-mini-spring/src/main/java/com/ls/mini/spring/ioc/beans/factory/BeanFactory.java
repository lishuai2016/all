package com.ls.mini.spring.ioc.beans.factory;

import com.ls.mini.spring.ioc.beans.BeanDefinition;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 14:07
 *
 * 抽象的工厂接口，定义了通过工厂获得bean和注册bean的方法
 */
public interface BeanFactory {
     Object getBean(String name) throws Exception;//获得bean对象
}
