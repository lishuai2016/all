package com.ls.design_pattern.Strategy.duck;

/**
 * 红头鸭，具体的鸭子
 * Created by cipher on 2017/9/12.
 */
public class RedheadDuck extends Duck {

    public RedheadDuck() {
        super.setFlyingStrategy(new FlyWithWing());
    }

    public void display() {
        System.out.println("我的头是红色的");
    }

}
