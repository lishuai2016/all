package com.ls.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 18:54
 * 测试自定义线程池
 */


public class TestCustomThreadPool {


    public static void main(String[] args) {

        try {
            TimingThreadPool threadPool = new TimingThreadPool(10,10,0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());

            List<TestCallable> tasks = new ArrayList<>();

            for (int i = 0 ; i < 100 ; i++) {
                tasks.add(new TestCallable());
            }

            List<Future<Long>> futures = threadPool.invokeAll(tasks);
            for (Future<Long> future : futures) {
                System.out.print(" - "+future.get());
            }
            threadPool.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static class TestCallable implements Callable<Long> {

        @Override
        public Long call() throws Exception {
            long total = 0 ;
            for (int i = 0 ; i < 100 ; i++) {
                long now = getRandom();
                total += now;
            }
            Thread.sleep(total);
            return total;
        }

        public long getRandom () {
            return Math.round(Math.random() * 10);
        }
    }

}
