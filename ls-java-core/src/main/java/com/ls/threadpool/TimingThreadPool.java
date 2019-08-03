package com.ls.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 18:53
 *
 * http://www.cnblogs.com/nyatom/p/10119306.html
Java自定义线程池-记录每个线程执行耗时
　　ThreadPoolExecutor是可扩展的，其提供了几个可在子类化中改写的方法，如下：

protected void beforeExecute(Thread t, Runnable r) { }
protected void afterExecute(Runnable r, Throwable t) { }
protected void terminated() { }
　　现基于此，完成一个统计每个线程执行耗时，并计算平均耗时的 自定义线程池样例。
通过 beforeExecute、afterExecute、terminated 方法来添加日志记录和统计信息收集。
为了测量任务的运行时间，beforeExecute必须记录开始时间并把它保存到一个ThreadLocal变量中，
然后由afterExecute来读取。同时，使用两个 AtomicLong变量，
分别用以记录已处理的任务数和总的处理时间，并通过terminated来输出包含平均任务时间的日志消息。
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        log.info(String.format("线程执行前Thread %s: start %s",t,r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.info(String.format("Thread %s: end %s, time=%dns",t,r,taskTime));

        } finally {
            super.afterExecute(r,t);
        }
    }

    @Override
    protected void terminated() {
        try {
            log.info(String.format("Terminated: avg time=%dns",totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }
}

