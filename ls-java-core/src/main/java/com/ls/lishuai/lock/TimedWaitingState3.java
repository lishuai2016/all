package com.ls.lishuai.lock;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 11:04
 *
WAITING (parking)---waiting on condition（通过条件变量condition.await()造成）

"t2" prio=6 tid=0x000000000c3ae800 nid=0x3f1c waiting on condition [0x000000000d03f000]
java.lang.Thread.State: WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ef90> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
at ls.lock.TimedWaitingState3$1.run(TimedWaitingState3.java:51)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c3ad800 nid=0x36a4 waiting on condition [0x000000000cdbe000]
java.lang.Thread.State: WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ef90> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
at ls.lock.TimedWaitingState3$1.run(TimedWaitingState3.java:51)
at java.lang.Thread.run(Thread.java:744)

 */


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimedWaitingState3 {
    // java的显示锁,类似java对象内置的监视器
    private static Lock lock = new ReentrantLock();
    // 锁关联的条件队列(类似于object.wait)
    private static Condition condition = lock.newCondition();
    public static void main(String[] args) {
        Runnable task = new Runnable() {

            @Override
            public void run() {
                // 加锁,进入临界区
                lock.lock();
                System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 解锁,退出临界区
                lock.unlock();
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}

