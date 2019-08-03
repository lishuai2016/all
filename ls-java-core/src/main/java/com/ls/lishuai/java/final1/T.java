package com.ls.lishuai.java.final1;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/26 16:00
 */
public class T {
    public static void main(String[] args)  {
        String a = "hello2";
        final String b = "hello";
        String d = "hello";
        String c = b + 2;
        String e = d + 2;
        System.out.println((a == c));
        System.out.println((a == e));
    }
}
