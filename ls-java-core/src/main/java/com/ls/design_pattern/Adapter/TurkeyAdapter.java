package com.ls.design_pattern.Adapter;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 21:06
 */
public class TurkeyAdapter implements Duck {
    //持有被适配的对象，通过构造函数传入
    public Turkey turkey;
    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }
    @Override
    public void quack() {
        turkey.gobble();
    }

    @Override
    public void fly() {
        for (int i = 0; i < 5; i++) {
            turkey.fly();
        }
    }
    //测试
    public static void main(String[] args) {
        MallardDuck duck = new MallardDuck();
        WildTurkey turkey = new WildTurkey();
        TurkeyAdapter turkeyAdapter = new TurkeyAdapter(turkey);
        System.out.println("turkey say...");
        turkey.gobble();
        turkey.fly();

        System.out.println("duck say...");
        testDuck(duck);


        System.out.println("turkeyAdapter say...");
        testDuck(turkeyAdapter);

    }
    static void testDuck(Duck duck) {
        duck.quack();
        duck.fly();
    }


}
