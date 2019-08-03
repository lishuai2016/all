package com.ls.lishuai.java.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:02
 * CountDownLatch：一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完后再执行。
 *
每一个员工到达之后，执行countDown()方法，直到所有员工到达之后，计数器为0，主线程才会继续执行。
但子线程执行了countDown()方法，之后会继续自己的工作，比如上面的【吃饭、喝水、拍照】，
是不受主线程是否阻塞、其它线程是否已经执行countDown()方法的影响的。
 */
public class Company {
    public static void main(String[] args) throws InterruptedException {
        int count = 3;//员工数量
        CountDownLatch latch = new CountDownLatch(count);//创建计数器 构造参数传入的数量值代表的是latch.countDown()调用的次数
        ExecutorService threadPool =  Executors.newFixedThreadPool(count);//创建线程池，可以通过以下方式创建
        System.out.println("公司发送通知，每一位员工在周六早上8点到公司大门口集合");
        for(int i =0;i<count ;i++){
            Thread.sleep(10);
            threadPool.execute(new Employee(latch,i+1));//将子线程添加进线程池执行
        }
        try {
            latch.await();//阻塞当前线程，直到所有员工到达公司大门口之后才执行
            System.out.println("所有员工已经到达公司大门口，大巴车发动，前往活动目的地。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            //最后关闭线程池，但执行以前提交的任务，不接受新任务
            threadPool.shutdown();
        }
    }
}

//工作线程
class Employee implements Runnable{
    private CountDownLatch latch;
    private int employeeIndex;
    public Employee(CountDownLatch latch,int employeeIndex){
        this.latch = latch;
        this.employeeIndex = employeeIndex;
    }
    @Override
    public void run() {
        try {
            System.out.println("员工："+employeeIndex+"，正在前往公司大门口集合...");
            Thread.sleep(10);
            System.out.println("员工："+employeeIndex+"，已到达。");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            latch.countDown();//当前计算工作已结束，计数器减一
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("员工："+employeeIndex+"，吃饭、喝水、拍照。");//执行coutDown()之后，继续执行自己的工作，不受主线程的影响
        }
    }
}