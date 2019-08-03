package com.ls.lishuai.java.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:06
 *
 * CyclicBarrier：一个同步辅助类，用于协调多个子线程，
 * 让多个子线程在这个屏障前等待，直到所有子线程都到达了这个屏障时，再一起继续执行后面的动作。


子线程执行了await()方法，必须等待其它所有子线程执行await()方法之后，才能一起继续后续的(await后main的)工作，
就像上面的例子，所有自驾车必须都到达公司大门口之后，才能一起继续各自自驾车前往目的地。
但，主线程await()之后的工作与子线程await()之后的工作是不受影响的，只要所有的子线程执行了await()方法，
主线程此时就可以后续的工作了，不必管子线程await()方法后续工作的情况。



CyclicBarrier与CountDownLatch的区别：

1）、构造两者对象传入的参数不一样：构造CyclicBarrier比构造CountDownLatch的参数大了1，
原因是构造CyclicBarrier的数量表示的是调用await()的次数，构造CountDownLatch的数量表示的是调用countDown()的次数；

2）、子线程调用了barrier.await()之后，必须等待所有子线程都完成barrier.await()调用后才能一起继续后续自己的工作，

而子线程调用latch.countDown()之后，会继续子线程自己的工作，不用等待其它子线程latch.countDown()调用情况。

公司发送通知，每一位员工在周六早上8点【自驾车】到公司大门口集合
员工：1，正在前往公司大门口集合...
员工：1，已到达。
员工：2，正在前往公司大门口集合...
员工：3，正在前往公司大门口集合...
员工：4，正在前往公司大门口集合...
员工：2，已到达。
员工：5，正在前往公司大门口集合...
员工：3，已到达。
员工：4，已到达。
员工：5，已到达。
员工：5，【自驾车】前往目的地
员工：1，【自驾车】前往目的地
员工：2，【自驾车】前往目的地
所有员工已经到达公司大门口，公司领导一并【自驾车】同员工前往活动目的地。
员工：4，【自驾车】前往目的地
员工：3，【自驾车】前往目的地

 */
public class CompanyCyclicBarrier {
    public static void main(String[] args) throws InterruptedException {
        int count = 5;//员工数量
        CyclicBarrier barrier = new CyclicBarrier(count + 1); //创建计数器（要多创建一个包含主线程的一次调用）
        ExecutorService threadPool = Executors.newFixedThreadPool(count);
        System.out.println("公司发送通知，每一位员工在周六早上8点【自驾车】到公司大门口集合");
        for (int i = 0; i < count; i++) {
            threadPool.execute(new Employee1(barrier, i + 1));//将子线程添加进线程池执行
            Thread.sleep(10);
        }
        try {
            barrier.await(); //阻塞当前线程，直到所有子线程都执行了 barrier.await()
            Thread.sleep(10);
            System.out.println("所有员工已经到达公司大门口，公司领导一并【自驾车】同员工前往活动目的地。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();//最后关闭线程池，但执行以前提交的任务，不接受新任务
        }
    }
}

//工作线程
class Employee1 implements Runnable{
    private CyclicBarrier barrier;
    private int employeeIndex;
    public Employee1(CyclicBarrier barrier,int employeeIndex){
        this.barrier = barrier;
        this.employeeIndex = employeeIndex;
    }
    @Override
    public void run() {
        try {
            System.out.println("员工："+employeeIndex+"，正在前往公司大门口集合...");
            Thread.sleep(10*employeeIndex);
            System.out.println("员工："+employeeIndex+"，已到达。");
            barrier.await();
            System.out.println("员工："+employeeIndex+"，【自驾车】前往目的地");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
