package com.ls.design_pattern.Proxy;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 18:07
 */
public class SubjectProxy implements Subject {

    //代理模式的作用是：为其他对象提供一种代理以控制对这个对象的访问。
    Subject subjectimpl = new RealSubject();
    @Override
    public void doSomething() {
        System.out.println("before"); //调用目标对象之前可以做相关操作
        subjectimpl.doSomething();
        System.out.println("after");//调用目标对象之后可以做相关操作
    }
}
