package com.ls.lishuai.lock;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 11:04
 *
TIMED_WAITING (parking)---waiting on condition（通过条件变量condition.await(5, TimeUnit.MINUTES)造成）

"t2" prio=6 tid=0x000000000c321000 nid=0x3bb4 waiting on condition [0x000000000d28f000]
java.lang.Thread.State: TIMED_WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ed18> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2176)
at ls.lock.TimedWaitingState$1.run(TimedWaitingState.java:31)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c320800 nid=0x3d64 waiting on condition [0x000000000d05e000]
java.lang.Thread.State: TIMED_WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ed18> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2176)
at ls.lock.TimedWaitingState$1.run(TimedWaitingState.java:31)
at java.lang.Thread.run(Thread.java:744)
 */


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimedWaitingState {
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
                    condition.await(5, TimeUnit.MINUTES);
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

