package com.ls.design_pattern.Proxy.dynamic.jdk;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 18:33
 */
public class RealSubject implements Subject {
    @Override
    public void rent() {
        System.out.println("I want to rent my house");
    }

    @Override
    public void hello(String str) {
        System.out.println("hello: " + str);
    }


    @Override
    public String re(String str) {
        return "1111111111111111111111111"+str;
    }
}
