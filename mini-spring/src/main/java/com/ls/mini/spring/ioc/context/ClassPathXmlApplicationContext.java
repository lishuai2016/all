package com.ls.mini.spring.ioc.context;

import com.ls.mini.spring.ioc.beans.BeanDefinition;
import com.ls.mini.spring.ioc.beans.factory.AbstractBeanFactory;
import com.ls.mini.spring.ioc.beans.factory.AutowireCapableBeanFactory;
import com.ls.mini.spring.ioc.beans.io.ResourceLoader;
import com.ls.mini.spring.ioc.beans.xml.XmlBeanDefinitionReader;

import java.util.Map;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 17:04
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    private String configLocation;

    public ClassPathXmlApplicationContext(String configLocation) throws Exception {
        this(configLocation, new AutowireCapableBeanFactory());//使用自动装配工厂实例化ApplicationContext中的属性
    }

    public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
        super(beanFactory);
        this.configLocation = configLocation;
        refresh();//调用父类的模板方法，初始化容器中的bean 单例对象
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
        xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : xmlBeanDefinitionReader.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }
}
