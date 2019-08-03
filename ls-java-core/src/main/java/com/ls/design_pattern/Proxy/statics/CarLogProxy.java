package com.ls.design_pattern.Proxy.statics;


import com.ls.design_pattern.Proxy.Moveable;

/**
 * 聚合方式实现日志代理
 * Created by cipher on 2017/9/6.
 */
public class CarLogProxy implements Moveable {

    private Moveable moveable;

    public CarLogProxy(Moveable moveable) {
        this.moveable = moveable;
    }

    public void move() {
        System.out.println("日志开始......");
        moveable.move();
        System.out.println("日志结束......");
    }

}
