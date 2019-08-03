package com.ls.threadpool;


import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * @author: lishuai
 * @create: 2019-03-17 16:00
 *
1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行
3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务
4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理
5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程
6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
当开启的任务数 > maxSize + queueSize时会执行拒绝策略


问题：
1、coreSize = 3   maxSize = 10  queueSize = 20  keepAliveTime = 60000
依据上面的设置，当重复执行一组任务（30个），只有第一组不会出现线程池满的问题，其余的第二第三组都会出现线程池满的问题？？？
按道理不应该出现的


2、异常的输出会和正常的日志输出交叉



 */
public class ThreadPoolTest {
    private static final Integer coreSize = 3;
    private static final Integer maxSize = 10;
    private static final Integer queueSize = 20;
    private static final Integer keepAliveTime = 60000;

    public static ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.newCachedThreadPool(coreSize, maxSize, keepAliveTime, ThreadPoolUtils.buildQueue(queueSize), new NamedThreadFactory("data-syn"), new MyRejectedExecutionHandler());

    @PreDestroy
    public void destroy() {
        threadPoolExecutor.shutdownNow();
    }



    public static void main(String[] args) {
        System.out.println("");
        try {
            deal(1,0,3);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("111111111111111111111111");
        } finally {
            System.out.println("22222222222222222222");
        }
//        deal(2,100,30);
//        deal(3,200,30);


    }

    public static void deal(int testnum,int start,int size) {
        System.out.println(String.format("开始往线程池中添加任务，当前线程池的状态是：pool:%s, active:%s, queue:%s, taskcnt: %s",threadPoolExecutor.getPoolSize(), threadPoolExecutor.getActiveCount(), threadPoolExecutor.getQueue().size(), threadPoolExecutor.getTaskCount()));
        BlockingQueue<Future<String>> queue = new LinkedBlockingQueue<>();//存放异步执行获取的结果
        for (int i= start;i < start+size;i++) {
            try {
                queue.add(threadPoolExecutor.submit(new TaskCallTest(i)));
            } catch (MyException e) {
                System.out.println("线程执行异常"+i);
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("添加线程异常"+i);
                e.printStackTrace();
            }
        }
        int queuesize = queue.size();
        System.out.println(String.format("************************结果队列%s的大小%s",testnum,queuesize));
        for (int i = 0;i < queuesize; i++) {
            String result = null;
            try {
                result = queue.take().get();
            }catch (MyException e) {
                System.out.println("线程执行异常"+i);
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("ExecutionException");
                e.printStackTrace();
            }
            System.out.println("线程执行的结果："+result);
        }

        System.out.println(String.format("本批次任务执行结束：pool:%s, active:%s, queue:%s, taskcnt: %s",threadPoolExecutor.getPoolSize(), threadPoolExecutor.getActiveCount(), threadPoolExecutor.getQueue().size(), threadPoolExecutor.getTaskCount()));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

}
