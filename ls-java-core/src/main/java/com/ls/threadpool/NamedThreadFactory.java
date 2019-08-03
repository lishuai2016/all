package com.ls.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 14:34
 * 线程工厂的内部类
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_COUNT = new AtomicInteger();
    final AtomicInteger threadCount;
    private final ThreadGroup group;
    private final String namePrefix;
    private final boolean isDaemon;

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this.threadCount = new AtomicInteger(1);
        SecurityManager s = System.getSecurityManager();
        this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = prefix + "-" + POOL_COUNT.getAndIncrement() + "-T";
        this.isDaemon = daemon;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, this.namePrefix + this.threadCount.getAndIncrement(), 0L);
        t.setDaemon(this.isDaemon);
        if (t.getPriority() != 5) {
            t.setPriority(5);
        }

        return t;
    }
}
