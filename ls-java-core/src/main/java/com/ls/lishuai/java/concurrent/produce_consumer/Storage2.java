package com.ls.lishuai.java.concurrent.produce_consumer;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/20 14:15
 *
 * BlockingQueue
 * 它是一个已经在内部实现了同步的队列，实现方式采用的是我们第2种await() / signal()方法。
 * 它可以在生成对象时指定容量大小。它用于阻塞操作的是put()和take()方法：

put()方法：类似于我们上面的生产者线程，容量达到最大时，自动阻塞。

take()方法：类似于我们上面的消费者线程，容量为0时，自动阻塞。
 */
import java.util.concurrent.LinkedBlockingQueue;

public class Storage2 {
    // 仓库最大存储量
    private final int MAX_SIZE = 100;

    // 仓库存储的载体
    private LinkedBlockingQueue<Object> list = new LinkedBlockingQueue<Object>(100);

    // 生产产品
    public void produce(String producer) {
        // 如果仓库已满
        if (list.size() == MAX_SIZE) {
            System.out.println("仓库已满，【" + producer + "】： 暂时不能执行生产任务!");
        }

        // 生产产品
        try {
            list.put(new Object());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("【" + producer + "】：生产了一个产品\t【现仓储量为】:" + list.size());
    }

    // 消费产品
    public void consume(String consumer) {
        // 如果仓库存储量不足
        if (list.size() == 0) {
            System.out.println("仓库已空，【" + consumer + "】： 暂时不能执行消费任务!");
        }

        try {
            list.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("【" + consumer + "】：消费了一个产品\t【现仓储量为】:" + list.size());

    }

    public LinkedBlockingQueue<Object> getList() {
        return list;
    }

    public void setList(LinkedBlockingQueue<Object> list) {
        this.list = list;
    }
    public int getMAX_SIZE() {
        return MAX_SIZE;
    }
}
