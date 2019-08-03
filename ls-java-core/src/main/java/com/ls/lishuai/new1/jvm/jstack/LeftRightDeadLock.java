package com.ls.lishuai.new1.jvm.jstack;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/26 11:38
 */
public class LeftRightDeadLock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() throws Exception {
        synchronized (left) {
            System.out.println(Thread.currentThread().getName()+"获得left锁");
            Thread.sleep(1000);
            synchronized (right)
            {
                System.out.println("leftRight end!");
            }
        }
    }

    public void rightLeft() throws Exception {
        synchronized (right) {    System.out.println(Thread.currentThread().getName()+"获得right锁");
            Thread.sleep(1000);
            synchronized (left)
            {
                System.out.println("rightLeft end!");
            }
        }
    }


}
