package com.ls.mini.spring.ioc.beans;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 15:03
 *
 * 通过参数传入配置文件的地址，然后加载配置文件中定义的bean到容器中
 * 从配置中读取信息生成BeanDefinition对象
 */
public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws Exception;
}
