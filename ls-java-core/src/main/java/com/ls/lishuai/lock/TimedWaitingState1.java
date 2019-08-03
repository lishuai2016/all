package com.ls.lishuai.lock;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 11:55
TIMED_WAITING--- in Object.wait()（object.wait(1 * 60 * 1000)造成）

"t2" prio=6 tid=0x000000000c0e5000 nid=0x3788 in Object.wait() [0x000000000cf9f000]
java.lang.Thread.State: TIMED_WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c978> (a java.lang.Object)
at ls.lock.TimedWaitingState1$1.run(TimedWaitingState1.java:26)
- locked <0x00000000d7d4c978> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c0b9000 nid=0x31a8 in Object.wait() [0x000000000cddf000]
java.lang.Thread.State: TIMED_WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c978> (a java.lang.Object)
at ls.lock.TimedWaitingState1$1.run(TimedWaitingState1.java:26)
- locked <0x00000000d7d4c978> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)
 */
public class TimedWaitingState1 {
    private static Object object = new Object();
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                        try {
                            // 进入等待的同时,会进入释放监视器
                            object.wait(1 * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
