package com.ls.lishuai.java.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 17:14
 */
public class MyThread extends Thread {
    CountDownLatch latch = null;
    MyThread(String name,CountDownLatch latch) {
        super(name);
        this.latch = latch;
    }
    @Override
    public void run() {
        System.out.println("当前线程的名称："+Thread.currentThread().getName() + "开始执行");
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
        System.out.println("当前线程的名称："+Thread.currentThread().getName() + "结束执行");
    }
}
