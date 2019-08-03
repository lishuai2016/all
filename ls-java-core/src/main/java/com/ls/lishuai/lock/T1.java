package com.ls.lishuai.lock;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/6 15:52
 */
public class T1 {
    public static void main(String[] args) {
        CountDownLatch count = new CountDownLatch(10);
        VolatileFeaturesExample e = new VolatileFeaturesExample();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Mythread(e,count));
            thread.start();
        }
        try {
            count.await();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("result: " + e.get());
    }
}
