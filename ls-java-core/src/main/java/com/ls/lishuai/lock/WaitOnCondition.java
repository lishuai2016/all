package com.ls.lishuai.lock;//package ls.lock;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/7/31 11:59
// *
// *
//TIMED_WAITING---waiting on condition
//"t2" prio=6 tid=0x000000000c632000 nid=0x3f14 waiting on condition [0x000000000d20f000]
//java.lang.Thread.State: TIMED_WAITING (sleeping)
//at java.lang.Thread.sleep(Native Method)
//at ls.lock.WaitOnCondition$1.run(WaitOnCondition.java:17)
//at java.lang.Thread.run(Thread.java:744)
//
//"t1" prio=6 tid=0x000000000c551000 nid=0xae8 waiting on condition [0x000000000d07e000]
//java.lang.Thread.State: TIMED_WAITING (sleeping)
//at java.lang.Thread.sleep(Native Method)
//at ls.lock.WaitOnCondition$1.run(WaitOnCondition.java:17)
//at java.lang.Thread.run(Thread.java:744)
// */
//public class WaitOnCondition {
//    private static Object object = new Object();
//
//    public static void main(String[] args) {
//        Runnable task = new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
//                try {
//                    Thread.sleep(1*60*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        new Thread(task, "t1").start();
//        new Thread(task, "t2").start();
//    }
//}
