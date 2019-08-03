package com.ls.lishuai.java.jvm.stack;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/24 10:54
 */

import java.util.ArrayList;
import java.util.List;

/**
 * -XX:+PrintFlagsFinal
 * maxHeapSize 289406976 byte
 * maxPermSize 85983232 byte
 * threadStackSize 1024 byte
 *
 * JVM中可以生成的最大数量由JVM的堆内存大小、Thread的Stack内存大小、系统最大可创建的线程数量
 * （Java线程的实现是基于底层系统的线程机制来实现的，Windows下_beginthreadex，Linux下pthread_create）三个方面影响。
 * 具体数量可以根据Java进程可以访问的最大内存（32位系统上一般2G）、堆内存、Thread的Stack内存来估算。
 *
 * (MaxProcessMemory - JVMMemory – ReservedOsMemory) / (ThreadStackSize) = Number of threads
 * MaxProcessMemory : 进程的最大寻址空间
 * JVMMemory : JVM内存
 * ReservedOsMemory : 保留的操作系统内存，如Native heap，JNI之类，一般100多M
 * ThreadStackSize : 线程栈的大小，jvm启动时由Xss指定
 *
 * http://www.rigongyizu.com/jvm-max-threads/
 */
public class TestMaxThread {
    public static final int BATCH_SIZE = 2000;
    public static void main(String[] args){
        List<Thread> threads = new ArrayList<Thread>();
        try{
            for(int i=0;i<=100*1000;i+= BATCH_SIZE){
                long start = System.currentTimeMillis();
                createThread(threads,BATCH_SIZE);
                long end = System.currentTimeMillis();
                Thread.sleep(1000);
                long delay = end - start;
                System.out.printf("%,d threads: Time to create %,d threads was %.3f seconds %n", threads.size(), BATCH_SIZE, delay / 1e3);
            }
        }catch(Throwable e){
            System.out.println("After creating "+ threads.size()+" threads");
            e.printStackTrace();
        }
    }
    private static void createThread(List<Thread> threads,int num){
        for(int i=0;i<num;i++){
            Thread t = new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        while (!Thread.interrupted()){
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                    }
                }
            });
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            threads.add(t);
            t.start();
        }
    }
}
