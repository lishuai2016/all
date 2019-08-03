package com.ls.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 14:36
 */
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRejectedExecutionHandler.class);
    private int i = 1;

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (this.i++ % 7 == 0) {
            this.i = 1;
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Task:{} has been reject because of threadPool exhausted! pool:{}, active:{}, queue:{}, taskcnt: {}", new Object[]{r, executor.getPoolSize(), executor.getActiveCount(), executor.getQueue().size(), executor.getTaskCount()});
            }
        }

        throw new RejectedExecutionException("Callback handler thread pool has bean exhausted 提交的任务太多，线程池被耗尽");
    }
}
