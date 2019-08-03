package com.ls.mini.spring.ioc;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-23 11:54
 */
public enum AopProxyEnum {
    JDK("jdk"),CGLIB("cglib");

    private String value;

    AopProxyEnum(String value) {
        this.value = value;
    }
}
