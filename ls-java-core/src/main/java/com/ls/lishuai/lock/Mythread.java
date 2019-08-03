package com.ls.lishuai.lock;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/6 15:54
 */
public class Mythread implements Runnable{
    VolatileFeaturesExample e;
    CountDownLatch countDownLatch;
    public Mythread(VolatileFeaturesExample e,CountDownLatch countDownLatch){
        this.e = e;
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            e.getAndIncrement();
        }
        countDownLatch.countDown();
    }
}
