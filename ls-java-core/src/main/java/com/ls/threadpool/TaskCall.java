package com.ls.threadpool;

import java.util.concurrent.Callable;

/**
 * @author: lishuai
 * @create: 2019-01-10 16:36
 */
public class TaskCall implements Callable<String> {

    private String taskName;

    public TaskCall(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String call() throws Exception {
        try {
            System.out.println(Thread.currentThread()+"当前任务开始："+ taskName);
            Thread.sleep(1000);//五分钟
            System.out.println(Thread.currentThread()+"当前任务结束："+ taskName);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return taskName+"6666";
    }
}
