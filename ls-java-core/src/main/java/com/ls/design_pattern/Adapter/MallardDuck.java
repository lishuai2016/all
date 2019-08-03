package com.ls.design_pattern.Adapter;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 21:05
 */
public class MallardDuck implements Duck {

    @Override
    public void quack() {
        System.out.println("quack");
    }

    @Override
    public void fly() {
        System.out.println("i am flying ....");
    }

}
