package com.ls.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-07 23:03
 */
public class TestLock_synchronized {
    private static Lock lock = new ReentrantLock();

    private static int num1 = 0;
    private static int num2 = 0;
    private  static final int count = 300000;  //并发量
    public static void main(String[] args) {
        lockDemo();
        SyncDemo();
    }
    /**
     * 本机测试下20万自增基本能确定性能,但是不是特别明显,50万差距还是挺大的
     * 30万以下数据synchronized优于Lock
     * 30万以上数据Lock优于synchronized
     */
    public static void lockDemo(){
        long start = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            final int num = i;
            new Runnable() {
                @Override
                public void run() {
                    lock(num);
                }
            }.run();
        }
        long end = System.currentTimeMillis();
        System.out.println("累加："+num1);
        System.out.println("ReentrantLock锁："+ (end-start));
    }
    public static void SyncDemo(){
        long start = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            final int num = i;
            new Runnable() {
                @Override
                public void run() {
                    sync(num);
                }
            }.run();
        }
        long end = System.currentTimeMillis();
        System.out.println("累加："+num2);
        System.out.println("synchronized锁："+ (end-start));
    }
    public static void lock(int i){
        lock.lock();
        num1 ++;
        lock.unlock();
    }
    public static synchronized void sync(int i){
        num2 ++;
    }
}
