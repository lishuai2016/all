package com.ls.lishuai.java;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/5 17:03
 */
public class TestThreadLocal {


    static  ThreadLocal<User> threadLocal = new ThreadLocal<User>();
    public static void main(String[] args) {

        User u = new User();
        u.setId(1);
        threadLocal.set(u);

        User u1 = threadLocal.get();
        u.setName("1111111111");
        System.out.println(u1);
        User u2 = threadLocal.get();
        System.out.println(u2);

    }
}
