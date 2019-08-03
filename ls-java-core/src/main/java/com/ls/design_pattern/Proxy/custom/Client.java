package com.ls.design_pattern.Proxy.custom;


import com.ls.design_pattern.Proxy.Car;
import com.ls.design_pattern.Proxy.Moveable;

/**
 * 自定义模拟 JDK 动态代理测试类
 * Created by cipher on 2017/9/6.
 *
 * 有问题？？？
 */
public class Client {

    public static void main(String[] args) throws Exception {
        InvocationHandler handler = new TimeHandler(new Car());
        Moveable moveable = (Moveable) Proxy.newProxyInstance(Moveable.class, handler);
        moveable.move();
    }

}
