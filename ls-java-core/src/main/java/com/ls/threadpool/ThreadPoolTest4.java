package com.ls.threadpool;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 16:48
 * https://uule.iteye.com/blog/1539591
使用方法一，自己创建一个集合来保存Future存根并循环调用其返回结果的时候，主线程并不能保证首先获得的是最先完成任务的线程返回值。
它只是按加入线程池的顺序返回。因为take方法是阻塞方法，后面的任务完成了，前面的任务却没有完成，主程序就那样等待在那儿，
只到前面的完成了，它才知道原来后面的也完成了。

使用方法二，使用CompletionService来维护处理线程不的返回结果时，主线程总是能够拿到最先完成的任务的返回值，而不管它们加入线程池的顺序。
 */
public class ThreadPoolTest4 {
    // 具有返回值的测试线程
    class MyThread implements Callable<String> {
        private String name;
        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public String call() {
            System.out.println(Thread.currentThread().getName()+"开始"+name);
            int sleepTime = new Random().nextInt(1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 返回给调用者的值
            String str = Thread.currentThread().getName()+":"+ name + " sleep time:" + sleepTime;
            System.out.println(Thread.currentThread().getName()+"结束"+name);

            return str;
        }
    }

    private final int POOL_SIZE = 5;
    private final int TOTAL_TASK = 20;

    // 方法一，自己写集合来实现获取线程池中任务的返回结果
    public void testByQueue() throws Exception {
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        BlockingQueue<Future<String>> queue = new LinkedBlockingQueue<Future<String>>();

        // 向里面扔任务
        for (int i = 0; i < TOTAL_TASK; i++) {
            Future<String> future = pool.submit(new MyThread("task" + i));
            queue.add(future);
        }
        System.out.println("队列的大小"+queue.size());
        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
            System.out.println("第一种方式获取结果:" + queue.take().get());
            System.out.println("队列的大小"+queue.size());
        }

        // 关闭线程池
        pool.shutdown();
    }

    // 方法二，通过CompletionService来实现获取线程池中任务的返回结果
    public void testByCompetion() throws Exception {
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        CompletionService<String> cService = new ExecutorCompletionService(pool);

        // 向里面扔任务
        for (int i = 0; i < TOTAL_TASK; i++) {
            cService.submit(new MyThread("task" + i));
        }

        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
            Future<String> future = cService.take();
            System.out.println("第一种方式获取结果:" + future.get());
        }

        // 关闭线程池
        pool.shutdown();
    }

    public static void main(String[] args) throws Exception {
        ThreadPoolTest4 t = new ThreadPoolTest4();
        t.testByQueue();
        //t.testByCompetion();
    }
}
