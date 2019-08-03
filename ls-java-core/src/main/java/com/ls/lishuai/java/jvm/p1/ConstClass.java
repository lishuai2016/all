package com.ls.lishuai.java.jvm.p1;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/23 10:41
 */
public class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }

    public static final String HELLOWORLD = "hello world";
}
