package com.ls.mini.spring.ioc.beans.factory;

import com.ls.mini.spring.ioc.beans.BeanDefinition;
import com.ls.mini.spring.ioc.beans.BeanPostProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 14:09
 *
 * 抽象的工厂类
 * 实现了获得bean和注册bean的逻辑，通过一个map来维护bean信息
 *
 * 使用模板方法，定义一个抽象的创建bean的抽象方法
 */
public abstract class AbstractBeanFactory implements BeanFactory{

    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();//IOC容器

    private final List<String> beanDefinitionNames = new ArrayList<String>();//即可工厂中的所有已经注册bean的名称

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();//对bean初始化的拦截操作

    /**
     * 这里可以实现懒加载
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(String name) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is defined");
        }
        Object bean = beanDefinition.getBean();//在这里没有实力话，再进行实例的创建
        if (bean == null) {
            bean = doCreateBean(beanDefinition);//创建一个bean实例，并设置bean的属性字段
            bean = initializeBean(bean,name);//对bean进行初始化处理【这里可能有问题，需要用代理生成后的对象bean，替换，否则容易造成切面advice不生效】
            beanDefinition.setBean(bean);//在这里有可能对象为代理的对象，需要更新原来变量的内容
        }
        return bean;
    }

    //  这里需要返回代理类包装后的对象，在容器的map中替换掉包装前的bean对象，否则，若这里返回的对象不是代理生成的对象，那这里肯定有问题
    protected Object initializeBean(Object bean, String name) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
        }

        // TODO:call initialize method
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
        }
        return bean;
    }

    //注册bean的接口保留在这里
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }

    /**
     * 初始化bean实例的接口，通过主动调用来完成对象bean的实例化
     * @throws Exception
     */
    public void preInstantiateSingletons() throws Exception {
        for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
            String beanName = (String) it.next();
            getBean(beanName);
        }
    }

    protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
        Object bean = createBeanInstance(beanDefinition);
        beanDefinition.setBean(bean);
        //设置对象到beanDefinition中去   懒加载，在get的时候才进行设值，之前是在注册的时候实例化设置的
        //在这里通过实例化一个没有初始化属性的bean到beanDefinition，解决了循环引用问题？？？
        applyPropertyValues(bean,beanDefinition);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
        return beanDefinition.getBeanClass().newInstance();//根据类对象构建一个无参数的实例
    }

    //让子类去实现，对bean设置属性（放在这里不可以吗？？？？）
    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

    }

    //添加BeanPostProcessor实例，对bean进行初始化处理
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    //获取指定类型所有bean实例
    public List getBeansForType(Class type) throws Exception {
        List beans = new ArrayList<Object>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }
        return beans;
    }
}
