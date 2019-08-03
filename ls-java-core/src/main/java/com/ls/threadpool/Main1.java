package com.ls.threadpool;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-03-17 20:51
 */
public class Main1 {
    public static void main(String[] args) {
        System.out.println("111");
        AtomicInteger failedThread = new AtomicInteger();

        for (int i = 0;i < 10;i++) {
            failedThread.incrementAndGet();
        }
        System.out.println("failedThread"+failedThread.get());

//        m1();
//
//        try {
//            m1();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("catch");
//        } finally {
//            System.out.println("finally");
//        }
    }


    public static void m1() {
        boolean flag = true;
        if (flag) {
            throw new MyException1("11111");
        } else {
            throw new MyException("2222");

        }
    }
}
