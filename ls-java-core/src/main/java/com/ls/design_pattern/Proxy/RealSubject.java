package com.ls.design_pattern.Proxy;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 18:07
 */
public class RealSubject implements Subject {
    @Override
    public void doSomething() {
        System.out.println( "call doSomething()" );
    }
}
