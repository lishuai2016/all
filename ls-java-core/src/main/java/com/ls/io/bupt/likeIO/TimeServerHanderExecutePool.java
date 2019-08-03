package com.ls.io.bupt.likeIO;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lishuai on 2017/6/17.
 */
public class TimeServerHanderExecutePool {
    private ExecutorService executor;
    public TimeServerHanderExecutePool(int maxPoolSize, int queueSize) {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize)
                );
    }
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
