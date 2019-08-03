package com.ls.threadpool;

import java.util.concurrent.*;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/11 9:40
 */
public class ThreadPoolUtils {
    public static int THREAD_PRIORITY_NORMAL = 0;
    public static int THREAD_PRIORITY_HIGH = 10;
    public static int THREAD_PRIORITY_LOW = -10;

    public ThreadPoolUtils() {
    }

    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue());
    }

    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize, BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, queue);
    }

    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, queue, threadFactory);
    }

    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, queue, threadFactory, handler);
    }

    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60000L, TimeUnit.MILLISECONDS, new SynchronousQueue());
    }

    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60000L, TimeUnit.MILLISECONDS, queue);
    }

    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60000L, TimeUnit.MILLISECONDS, queue, threadFactory);
    }

    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60000L, TimeUnit.MILLISECONDS, queue, threadFactory, handler);
    }

    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize, int maximumPoolSize, int keepAliveTime, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, (long)keepAliveTime, TimeUnit.MILLISECONDS, queue, threadFactory, handler);
    }

    public static BlockingQueue<Runnable> buildQueue(int size) {
        return buildQueue(size, false);
    }

    public static BlockingQueue<Runnable> buildQueue(int size, boolean isPriority) {
        Object queue;
        if (size == 0) {
            queue = new SynchronousQueue();
        } else if (isPriority) {
            queue = size < 0 ? new PriorityBlockingQueue() : new PriorityBlockingQueue(size);
        } else {
            queue = size < 0 ? new LinkedBlockingQueue() : new LinkedBlockingQueue(size);
        }

        return (BlockingQueue)queue;
    }
}
