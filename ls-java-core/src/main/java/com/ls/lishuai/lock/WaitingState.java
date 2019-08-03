package com.ls.lishuai.lock;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 11:03
 *
WAITING---（in Object.wait()）（调用 object.wait()造成的）

"t2" prio=6 tid=0x000000000c379800 nid=0x3e28 in Object.wait() [0x000000000d06f000]
java.lang.Thread.State: WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Object.wait(Object.java:503)
at ls.lock.WaitingState$1.run(WaitingState.java:29)
- locked <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c375000 nid=0x3f54 in Object.wait() [0x000000000d18f000]
java.lang.Thread.State: WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Object.wait(Object.java:503)
at ls.lock.WaitingState$1.run(WaitingState.java:29)
- locked <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)
 */



public class WaitingState {
    private static Object object = new Object();
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                    try {
                        object.wait();
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

