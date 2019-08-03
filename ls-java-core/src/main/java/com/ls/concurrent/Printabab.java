package com.ls.concurrent;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-05-04 11:21
 * java两个线程交替打印奇偶数
 *https://www.jianshu.com/p/41ab99e6ac2c
 */
public class Printabab {

    public static void main(String[] args) {
        Wrapper wrapper = new Wrapper();
        Thread odd = new Thread(new PrintOdd(wrapper));
        Thread even = new Thread(new PrintEven(wrapper));
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

        public PrintOdd(Wrapper wrapper) {
            this.wrapper = wrapper;
        }
        @Override
        public void run() {
            synchronized (wrapper){
                while (wrapper.getNum() <= 100) {
                    if (wrapper.getNum() % 2 == 0) {//偶数的时候释放锁等待
                        try {
                            wrapper.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(String.format("当前线程：%s, PrintOdd 打印奇数：%s",Thread.currentThread().getName(),wrapper.getNum()));
                        wrapper.setNum(wrapper.getNum()+1);
                        wrapper.notify();
                    }
                }
            }
        }
    }


    //打印偶数
    private static class PrintEven implements Runnable{
        Wrapper wrapper;

        public PrintEven(Wrapper wrapper) {
            this.wrapper = wrapper;
        }
        @Override
        public void run() {
            synchronized (wrapper){
                while (wrapper.getNum() <= 100) {
                    if (wrapper.getNum() % 2 == 1) {//奇数的时候释放锁等待
                        try {
                            wrapper.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(String.format("当前线程：%s, PrintEven 打印偶数：%s",Thread.currentThread().getName(),wrapper.getNum()));
                        wrapper.setNum(wrapper.getNum()+1);
                        wrapper.notify();
                    }
                }
            }
        }
    }
}
