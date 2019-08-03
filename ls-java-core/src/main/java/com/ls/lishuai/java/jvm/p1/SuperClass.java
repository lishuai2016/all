package com.ls.lishuai.java.jvm.p1;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/23 10:27
 */
public class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }

    {
        System.out.println(" init!");
    }
    public static int value = 123;
}
