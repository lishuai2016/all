package com.ls.lishuai.java.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/1 11:05
线程Thread-0正在写入数据...
线程Thread-2正在写入数据...
线程Thread-1正在写入数据...
线程Thread-3正在写入数据...
线程Thread-0写入数据完毕，等待其他线程写入完毕
线程Thread-2写入数据完毕，等待其他线程写入完毕
线程Thread-1写入数据完毕，等待其他线程写入完毕
java.util.concurrent.TimeoutException
at java.util.concurrent.CyclicBarrier.dowait(CyclicBarrier.java:250)
at java.util.concurrent.CyclicBarrier.await(CyclicBarrier.java:427)
at ls.java.concurrent.TestCyclicBarrier3$Writer.run(TestCyclicBarrier3.java:41)
java.util.concurrent.BrokenBarrierException
at java.util.concurrent.CyclicBarrier.dowait(CyclicBarrier.java:243)
at java.util.concurrent.CyclicBarrier.await(CyclicBarrier.java:427)
at ls.java.concurrent.TestCyclicBarrier3$Writer.run(TestCyclicBarrier3.java:41)
java.util.concurrent.BrokenBarrierException
at java.util.concurrent.CyclicBarrier.dowait(CyclicBarrier.java:243)
at java.util.concurrent.CyclicBarrier.await(CyclicBarrier.java:427)
at ls.java.concurrent.TestCyclicBarrier3$Writer.run(TestCyclicBarrier3.java:41)
Thread-0所有线程写入完毕，继续处理其他任务...
Thread-1所有线程写入完毕，继续处理其他任务...
Thread-2所有线程写入完毕，继续处理其他任务...
java.util.concurrent.BrokenBarrierException
at java.util.concurrent.CyclicBarrier.dowait(CyclicBarrier.java:200)
at java.util.concurrent.CyclicBarrier.await(CyclicBarrier.java:427)
at ls.java.concurrent.TestCyclicBarrier3$Writer.run(TestCyclicBarrier3.java:41)
线程Thread-3写入数据完毕，等待其他线程写入完毕
Thread-3所有线程写入完毕，继续处理其他任务...
 */
public class TestCyclicBarrier3 {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++) {
            if(i<N-1) {
                new Writer(barrier).start();
            } else { //然最后一个线程延迟启动
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Writer(barrier).start();
            }
        }
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                try {
                    cyclicBarrier.await(2000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }
}

