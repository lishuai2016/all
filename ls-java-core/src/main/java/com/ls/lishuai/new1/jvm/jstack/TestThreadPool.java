package com.ls.lishuai.new1.jvm.jstack;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/26 13:07
 */
public class TestThreadPool {
        public static void main(String[] args) {
        long start = System.currentTimeMillis();


        // 创建一个同时允许两个线程并发执行的线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // 创建一个初始值为5的倒数计数器
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for(int i = 0; i < 5; i++) {
            Thread thread = new TestThread("TestThreadPool"+i,countDownLatch);
            executor.execute(thread);
        }

        try {
            // 阻塞当前线程，直到倒数计数器倒数到0
            countDownLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("子线程执行时长：" + (end - start));
    }
}
