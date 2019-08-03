package com.ls.mini.spring.ioc.beans;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 13:30
 *
 * bean的定义对象
 */
public class BeanDefinition {

    private Object bean; //封装的bean对象

    private Class beanClass;//类对象

    private String beanClassName;//对应类的名称，全限定名称

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {//通过全限定名来获得类对象
        this.beanClassName = beanClassName;

        try {
            beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private PropertyValues propertyValues = new PropertyValues();//封装构建bean时所需的属性，需要进行初始化

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
