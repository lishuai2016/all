package com.ls.lishuai.java;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/4 10:03
 */
public class WatchThread {


    public static void main(String[] args) throws InterruptedException {
        new WatchThread().testThread();
    }

    private void testThread() throws InterruptedException {
        int numThread =10;
        CountDownLatch signal = new CountDownLatch(numThread);

        Executor es = Executors.newFixedThreadPool(numThread);
        for (int i=0;i <= numThread;i++) {
            Runnable r  = new TestThread(signal);
            es.execute(r);
        }
        signal.await();
        System.out.println("11111111111111");

    }


    private class TestThread implements Runnable{

        private CountDownLatch signal;
        public TestThread(CountDownLatch signal) {
            this.signal = signal;
        }
        public void run() {
            System.out.println("开启了线程数" + signal.getCount());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            signal.countDown();
            System.out.println(Thread.currentThread().getName() + "结束，还有多少线程" + signal.getCount());
        }
    }

}



