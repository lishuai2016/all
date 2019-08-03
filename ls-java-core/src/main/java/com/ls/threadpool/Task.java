package com.ls.threadpool;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 14:52
 */
public class Task implements Runnable {
    private Integer num;

    public Task(Integer num) {
        this.num = num;
    }

    @Override
    public void run() {
        try {
            System.out.println("当前任务开始："+ num);
            Thread.sleep(1000);//五分钟
            System.out.println("当前任务结束："+ num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
