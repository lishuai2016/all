package com.ls.lishuai.new1.jvm.jstack;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/26 14:10
 *
 * BLOCKED---waiting for monitor entry（热点同步代码块造成的阻塞）
 */
public class BlockedStateBySynchronized {
    private static Object object = new Object();

    public static void main(String[] args) {
        System.out.println("主线程开始：" + Thread.currentThread().getName());
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                    long begin = System.currentTimeMillis();
                    long end = System.currentTimeMillis();
                    // 让线程运行1分钟,会一直持有object的监视器
                    while ((end - begin) <= 1 * 60 * 1000) {
                        end = System.currentTimeMillis();
                    }
                }
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
        System.out.println("主线程结束："+Thread.currentThread().getName());
    }


}
