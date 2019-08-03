package com.ls.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-05-04 11:49
 */
public class PrintababLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Wrapper wrapper = new Wrapper();
        Thread odd = new Thread(new PrintOdd(wrapper,lock,condition));
        Thread even = new Thread(new PrintEven(wrapper,lock,condition));
        odd.start();
        even.start();
    }


    private static class Wrapper {
        private Integer num = 1;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }
    //打印奇数
    private static class PrintOdd implements Runnable{
        Wrapper wrapper;
        ReentrantLock lock;
        Condition condition;

        public PrintOdd(Wrapper wrapper,ReentrantLock lock,Condition condition) {
            this.wrapper = wrapper;
            this.lock = lock;
            this.condition = condition;
        }
        @Override
        public void run() {
                while (wrapper.getNum() <= 100) {
                    try {
                        lock.lock();
                        if (wrapper.getNum() % 2 == 0) {//偶数的时候释放锁等待
                           condition.await();
                        } else {
                            System.out.println(String.format("当前线程：%s, PrintOdd 打印奇数：%s",Thread.currentThread().getName(),wrapper.getNum()));
                            wrapper.setNum(wrapper.getNum()+1);
                            condition.signal();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }

        }
    }


    //打印偶数
    private static class PrintEven implements Runnable{
        Wrapper wrapper;
        ReentrantLock lock;
        Condition condition;

        public PrintEven(Wrapper wrapper,ReentrantLock lock,Condition condition) {
            this.wrapper = wrapper;
            this.lock = lock;
            this.condition = condition;
        }
        @Override
        public void run() {

                while (wrapper.getNum() <= 100) {
                    try {
                        lock.lock();
                        if (wrapper.getNum() % 2 == 1) {//奇数的时候释放锁等待
                           condition.await();
                        } else {
                            System.out.println(String.format("当前线程：%s, PrintEven 打印偶数：%s",Thread.currentThread().getName(),wrapper.getNum()));
                            wrapper.setNum(wrapper.getNum()+1);
                           condition.signal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }

        }
    }
}
