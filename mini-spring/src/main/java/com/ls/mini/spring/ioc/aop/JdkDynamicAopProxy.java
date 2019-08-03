package com.ls.mini.spring.ioc.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 17:45
 *
 * 基于jdk的动态代理通过
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getInterfaces(), this);
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        //所有的对象都做增强处理，没有了切入点的匹配处理
//        MethodInterceptor methodInterceptor = advised.getMethodInterceptor();//方法拦截器
//        return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method,
//                args));//用方法拦截器包装被调用的方法，进而可以插入拦截器中的逻辑
        MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
        if (advised.getMethodMatcher() != null
                && advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(),
                    method, args));
        } else {
            return method.invoke(advised.getTargetSource().getTarget(), args);
        }
    }

}

