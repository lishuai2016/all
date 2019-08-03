package com.ls.lishuai.java.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 17:18
 */
public class TestThread {
    public static void main(String[] arg) throws Exception{
        final CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new MyThread("t1", latch);
        Thread t2 = new MyThread("t2", latch);
        System.out.println("主线程开始");
        t1.start();
        t2.start();
        latch.await();//等在这里，所有子线程都执行完再继续往下走
        System.out.println("主线程结束");

    }
}
