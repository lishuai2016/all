package com.ls.threadpool;

import java.util.concurrent.Callable;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-03-17 20:50
 */
public class TaskCallTest implements Callable<String> {

    int num;

    public TaskCallTest(int n) {
        this.num = n;
    }

    @Override
    public String call() throws Exception {

        if (num % 2 == 0) {
            throw new MyException("自动抛异常");
        }
        Thread.sleep(5000);

        return num+"---"+Thread.currentThread().getName();
    }
}

