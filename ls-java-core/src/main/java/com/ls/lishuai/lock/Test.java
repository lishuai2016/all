package com.ls.lishuai.lock;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/30 14:37
死锁导致的阻塞
BLOCKED---waiting for monitor entry

"Thread-1" prio=6 tid=0x000000000c019000 nid=0x2f38 waiting for monitor entry [0x000000000d14f000]
java.lang.Thread.State: BLOCKED (on object monitor)
at ls.lock.LeftRightDeadLock.rightLeft(LeftRightDeadLock.java:32)
- waiting to lock <0x00000000d7d53bb8> (a java.lang.Object)
- locked <0x00000000d7d53bc8> (a java.lang.Object)
at ls.lock.Test$2.run(Test.java:26)

"Thread-0" prio=6 tid=0x000000000c018000 nid=0x3de0 waiting for monitor entry [0x000000000cddf000]
java.lang.Thread.State: BLOCKED (on object monitor)
at ls.lock.LeftRightDeadLock.leftRight(LeftRightDeadLock.java:20)
- waiting to lock <0x00000000d7d53bc8> (a java.lang.Object)
- locked <0x00000000d7d53bb8> (a java.lang.Object)
at ls.lock.Test$1.run(Test.java:16)

Found one Java-level deadlock:
=============================
"Thread-1":
waiting to lock monitor 0x000000000aa87f58 (object 0x00000000d7d53bb8, a java.lang.Object),
which is held by "Thread-0"
"Thread-0":
waiting to lock monitor 0x000000000c01ad08 (object 0x00000000d7d53bc8, a java.lang.Object),
which is held by "Thread-1"

 */
public class Test {
    public static void main(String[] args) throws Exception{
        System.out.println("主线程开始"+Thread.currentThread().getName());
        final LeftRightDeadLock lock = new LeftRightDeadLock();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    lock.leftRight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    lock.rightLeft();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("主线程结束"+Thread.currentThread().getName());
    }
}
