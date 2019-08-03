package com.ls.lishuai.new1.jvm.jstack;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/26 11:37
 */
public class TestDeadLock {
    public static void main(String[] args) throws Exception{
        System.out.println("主线程开始"+Thread.currentThread().getName());
        final LeftRightDeadLock lock = new LeftRightDeadLock();
        Thread t1 = new Thread("TestDeadLock_1"){
            @Override
            public void run() {
                try {
                    lock.leftRight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread("TestDeadLock_2"){
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

