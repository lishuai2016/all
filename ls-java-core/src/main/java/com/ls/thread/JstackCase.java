package com.ls.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-04 14:08
 */
public class JstackCase {

    public static Executor executor = Executors.newFixedThreadPool(5);
    public static Object lock = new Object();


    public static void main(String[] args) {
        System.out.println("hello");
        Task task1 = new Task();
        Task task2 = new Task();
        executor.execute(task1);
        executor.execute(task2);

    }

    static class Task implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                calculate();
            }
        }

        public void calculate() {
            int i= 0;
            while (true) {
                i++;
            }
        }
    }

}
