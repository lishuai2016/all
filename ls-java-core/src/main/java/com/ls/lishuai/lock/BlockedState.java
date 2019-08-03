package com.ls.lishuai.lock;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 10:54
 *
 *
模拟热锁导致的阻塞
BLOCKED---waiting for monitor entry

"t2" prio=6 tid=0x000000000c389800 nid=0x3ee4 waiting for monitor entry [0x000000000cc6f000]
java.lang.Thread.State: BLOCKED (on object monitor)
at ls.lock.BlockedState$1.run(BlockedState.java:19)
- waiting to lock <0x00000000d7d4c460> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c388800 nid=0x3d08 runnable [0x000000000cf0e000]
java.lang.Thread.State: RUNNABLE
at ls.lock.BlockedState$1.run(BlockedState.java:24)
- locked <0x00000000d7d4c460> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)
 */



public class BlockedState {
    private static Object object = new Object();

    public static void main(String[] args) {
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
    }
}

