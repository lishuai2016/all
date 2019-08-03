package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:30
 *
People静态代码块
People普通代码块
People构造函数
 *
 * 先执行静态代码块、一般代码块、构造函数
 * 要是构造多个类实例，静态代码块只会执行一次，而一般代码块和构造函数会执行多次
 *
 */
public class People {
    People(){
        System.out.println("People构造函数");
    }
    static {
        System.out.println("People静态代码块");
    }
    {
        System.out.println("People普通代码块");
    }

}
