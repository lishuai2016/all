package com.ls.design_pattern.Strategy.duck;

/**
 * 飞行接口实现类
 * Created by cipher on 2017/9/12.
 */
public class FlyNoWay implements FlyingStrategy {

    public void performFly() {
        System.out.println("我不会飞！");
    }

}
