package com.ls.mini.spring.ioc.beans;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 14:30
 *
 * 封装构建bean时的一些属性信息
 */
public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {  //通过构造函数传入属性值，并且定义为final，构建之后属性不可变
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
