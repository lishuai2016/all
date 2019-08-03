package com.ls.design_pattern.Proxy.statics;


import com.ls.design_pattern.Proxy.Car;
import com.ls.design_pattern.Proxy.Moveable;

/**
 * 静态代理测试类
 * Created by cipher on 2017/9/6.
 */
public class Client {

    public static void main(String[] args) {
        // 继承方式实现代理
        Moveable carByExtend = new CarByExtend();
        carByExtend.move();
        System.out.println("-----------------");
        // 聚合方式实现时间代理
        Moveable carTimeProxy = new CarTimeProxy(new Car());
        carTimeProxy.move();
        System.out.println("-----------------");
        // 聚合方式实现日志代理
        Moveable carLogProxy = new CarLogProxy(new Car());
        carLogProxy.move();
        System.out.println("-----------------");
        // 聚合方式实现时间、日志代理
        Moveable carTimeLogProxy = new CarTimeProxy(new CarLogProxy(new Car()));
        carTimeLogProxy.move();
    }

}
