package com.ls.lishuai.java.concurrent;

import java.util.concurrent.Semaphore;

/**
 *
 * 作用：可用于限流（实例：下载服务）
 *
 *
 * @Author: lishuai
 * @CreateDate: 2018/7/19 10:14
 *Semaphore翻译成字面意思为 信号量，Semaphore可以控同时访问的线程个数，
 * 通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
 *
下面对上面说的三个辅助类进行一个总结：

　　1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：

　　　　CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；

　　　　而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；

　　　　另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。

　　2）Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。


工人0占用一个机器在生产...
工人3占用一个机器在生产...
工人0释放出机器
工人4占用一个机器在生产...
工人3释放出机器
工人2占用一个机器在生产...
工人4释放出机器
工人1占用一个机器在生产...
工人2释放出机器
工人1释放出机器
 */
public class TestSemaphore {
    public static void main(String[] args) {
        int N = 5;            //工人数
        Semaphore semaphore = new Semaphore(2); //机器数目
        for(int i=0;i<N;i++)
            new Worker(i,semaphore).start();
    }
    static class Worker extends Thread{
        private int num;
        private Semaphore semaphore;
        public Worker(int num,Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人"+this.num+"占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人"+this.num+"释放出机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
