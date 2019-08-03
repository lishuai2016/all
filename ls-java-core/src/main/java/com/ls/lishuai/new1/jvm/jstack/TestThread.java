package com.ls.lishuai.new1.jvm.jstack;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/26 13:07
 */
public class TestThread extends Thread {
    CountDownLatch countDownLatch;
    TestThread(String name, CountDownLatch countDownLatch) {
        super(name);
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        System.out.println(this.getName() + "子线程开始");
        try {
            // 子线程休眠五秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }

        System.out.println(this.getName() + "子线程结束");

    }
}
