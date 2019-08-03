package com.ls.mini.spring.ioc.aop;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 17:40
 *
 * 被代理的对象
 * 1、jdk的代理需要被代理类实现某个接口
 * 2、cglib可以基于继承进行动态代理
 *
 */
public class TargetSource {
    private Class<?> targetClass;//被代理类本身的类型

    private Class<?>[] interfaces;//jdk代理只用这个不用targetClass

    private Object target;

    public TargetSource(Object target, Class<?> targetClass,Class<?>... interfaces) {
        this.target = target;
        this.targetClass = targetClass;
        this.interfaces = interfaces;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }
}
