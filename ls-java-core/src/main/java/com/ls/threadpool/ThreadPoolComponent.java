package com.ls.threadpool;


import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadPoolExecutor;

;

/**
 * @author: lishuai
 * @create: 2019-01-10 14:15
1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行
3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务
4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理
5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程
6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
 当开启的任务数 > maxSize + queueSize时会执行拒绝策略
 */
@Component
public class ThreadPoolComponent {
    private static final Integer coreSize = 30;
    private static final Integer maxSize = 200;
    private static final Integer queueSize = 100;
    private static final Integer keepAliveTime = 60000;

    public ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.newCachedThreadPool(coreSize, maxSize, keepAliveTime, ThreadPoolUtils.buildQueue(queueSize), new NamedThreadFactory("data-syn"), new MyRejectedExecutionHandler());

    @PreDestroy
    public void destroy() {
        threadPoolExecutor.shutdownNow();
    }

}
