package com.ls.mini.spring.ioc.beans;

import com.ls.mini.spring.ioc.beans.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 15:05
 *
 * 抽象类在构造函数定义个ResourceLoader来
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private Map<String,BeanDefinition> registry;//初始化一个BeanDefinition注册的容器

    private ResourceLoader resourceLoader;//定位配置文件的位置，便于解析

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
