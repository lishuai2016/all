package com.ls.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-05-04 12:00
 *
 * https://www.cnblogs.com/MarsCheng/p/10355945.html
 */
public class NotifyTest2 implements Runnable {
    private Object prev;
    private Object self;
    AtomicInteger i;

    private NotifyTest2(AtomicInteger num,Object prev, Object self) {
        this.i = num;
        this.prev = prev;
        this.self = self;
    }


    @Override
    public void run() {
        while (true) {
            synchronized (prev) {
                synchronized (self) {
                    if (i.get() <= 102) {
                        System.out.println(Thread.currentThread().getName() + ":" + i.get());
                        i.getAndIncrement();
                        self.notify();
                    }
                }
                try {
                    prev.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args)  {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        AtomicInteger num = new AtomicInteger(1);
        NotifyTest2 testA = new NotifyTest2(num,c,a);
        NotifyTest2 testB = new NotifyTest2(num,a,b);
        NotifyTest2 testC = new NotifyTest2(num,b,c);

        new Thread(testA).start();
        new Thread(testB).start();
        new Thread(testC).start();
    }
}
