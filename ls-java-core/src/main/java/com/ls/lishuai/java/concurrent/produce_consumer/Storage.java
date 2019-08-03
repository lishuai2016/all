package com.ls.lishuai.java.concurrent.produce_consumer;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/20 13:56
 *
 *
 * wait() / nofity()方法是基类Object的两个方法：

wait()方法：当缓冲区已满/空时，生产者/消费者线程停止自己的执行，放弃锁，使自己处于等等状态，让其他线程执行。
notify()方法：当生产者/消费者向缓冲区放入/取出一个产品时，向其他等待的线程发出可执行的通知，同时放弃锁，使自己处于等待状态。
 */
import java.util.LinkedList;

public class Storage {
    // 仓库最大存储量
    private final int MAX_SIZE = 100;

    // 仓库存储的载体
    private LinkedList<Object> list = new LinkedList<Object>();

    // 生产产品
    public void produce(String producer) {
        synchronized (list) {
            // 如果仓库已满
            while (list.size() == MAX_SIZE) {
                System.out.println("仓库已满，【"+producer+"】： 暂时不能执行生产任务!");
                try {
                    // 由于条件不满足，生产阻塞
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 生产产品
            list.add(new Object());
            System.out.println("【"+producer+"】：生产了一个产品\t【现仓储量为】:" + list.size());
            list.notifyAll();
        }
    }

    // 消费产品
    public void consume(String consumer) {
        synchronized (list) {
            //如果仓库存储量不足
            while (list.size()==0) {
                System.out.println("仓库已空，【"+consumer+"】： 暂时不能执行消费任务!");
                try {
                    // 由于条件不满足，消费阻塞
                    list.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.remove();
            System.out.println("【"+consumer+"】：消费了一个产品\t【现仓储量为】:" + list.size());
            list.notifyAll();
        }
    }

    public LinkedList<Object> getList() {
        return list;
    }

    public void setList(LinkedList<Object> list) {
        this.list = list;
    }

    public int getMAX_SIZE() {
        return MAX_SIZE;
    }
}
