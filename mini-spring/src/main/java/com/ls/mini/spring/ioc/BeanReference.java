package com.ls.mini.spring.ioc;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 16:03
 */
public class BeanReference {

    private String name;

    private Object bean;

    public BeanReference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}

