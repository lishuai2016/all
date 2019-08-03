package com.ls.mini.spring.ioc.context;

import com.ls.mini.spring.ioc.beans.BeanPostProcessor;
import com.ls.mini.spring.ioc.beans.factory.AbstractBeanFactory;

import java.util.List;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 17:02
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected AbstractBeanFactory beanFactory;//委托

    //构造函数中传入工厂实例对象
    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    //子类在初始化的时候调用这个方法，这里定义了实例化的模板方法
    public void refresh() throws Exception {
        loadBeanDefinitions(beanFactory);//注册bean信息
        registerBeanPostProcessors(beanFactory);//注册bean留下的扩展BeanPostProcessor的实现，对实例初始化进行额外的操作
        onRefresh();//实例化bean对象
    }

    //子类实现如何把配置的bean信息解析到beanFactory中
    protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

    //添加BeanPostProcessor处理，在实例化bean后
    protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
        List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
        }
    }

    //初始化单例bean对象
    protected void onRefresh() throws Exception{
        beanFactory.preInstantiateSingletons();
    }

    //获取bean实例委托给当前的beanFactory
    @Override
    public Object getBean(String name) throws Exception {
        return beanFactory.getBean(name);
    }
}
