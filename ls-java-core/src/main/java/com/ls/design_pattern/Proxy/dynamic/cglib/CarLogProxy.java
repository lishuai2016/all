package com.ls.design_pattern.Proxy.dynamic.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 实现动态代理
 * Created by cipher on 2017/9/6.
 */
public class CarLogProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        // 设置父类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    /**
     * 拦截所有目标的方法调用
     *
     * @param o           目标类的实例
     * @param method      目标方法的反射对象
     * @param objects     方法的参数
     * @param methodProxy 代理类的对象
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("日志开始......");
        // 代理类调用父类的方法
        methodProxy.invokeSuper(o, objects);
        System.out.println("日志结束......");
        return null;
    }

}
