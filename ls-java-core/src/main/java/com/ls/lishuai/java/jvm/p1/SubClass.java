package com.ls.lishuai.java.jvm.p1;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/23 10:37
 */
public class SubClass extends SuperClass {
    public static int value1 = 123;

    static {
        System.out.println("SubClass init!");
    }
}
