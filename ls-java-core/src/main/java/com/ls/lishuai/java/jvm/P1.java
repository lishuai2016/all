package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:35
 *
 *
 *

类继承的初始化：通过引用 static 字段，触发某个类的初始化，则声明该字段的类，以及该类的父类被初始化。


People静态代码块
P1静态代码块
People普通代码块
People构造函数
P1普通代码块
P1构造函数

1、子类在执行的时候先加载父类的静态代码块，接着加载自己的静态代码块，
2、然后再执行父类的普通代码块和构造函数
3、然后执行自己的普通代码块和构造函数


 *
 */
public class P1 extends People {

    P1(){
        System.out.println("P1构造函数");
    }
    static {
        System.out.println("P1静态代码块");
    }
    {
        System.out.println("P1普通代码块");
    }

}
