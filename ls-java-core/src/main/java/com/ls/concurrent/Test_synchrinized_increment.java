package com.ls.concurrent;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08
 */
public class Test_synchrinized_increment {
    public  int data=0;

    public synchronized void increment() {
         data++;
    }
    //多个线程调用increment
}
