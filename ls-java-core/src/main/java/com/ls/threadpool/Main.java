package com.ls.threadpool;



import java.util.concurrent.*;

/**
 * @program: ads-data-dxp
 * @author: lishuai
 * @create: 2019-01-10 16:06
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolComponent threadPoolComponent = new ThreadPoolComponent();
        ThreadPoolExecutor threadPoolExecutor = threadPoolComponent.threadPoolExecutor;
//        int TOTAL_TASK = 10;
////        System.out.println("111111111");
////        for (int i = 0;i < 301;i++) {
////            threadPoolExecutor.execute(new Task(i));
////        }
////        System.out.println("222222222222");
//        CompletionService<String> cService = new ExecutorCompletionService(threadPoolExecutor);
//         System.out.println("111111111");
//        for (int i = 0;i < TOTAL_TASK;i++) {
//            cService.submit(new TaskCall("task"+i));
//        }
//        System.out.println("222222222222");
//        for (int i = 0; i < TOTAL_TASK; i++) {
//            Future<String> future = cService.take();
//            System.out.println("获取结果:" + future.get());
//        }
        Task task = new Task(1);
        TaskCall taskCall = new TaskCall("ls");

        Future<String> future = threadPoolExecutor.submit(taskCall);

        Object o = future.get();
        System.out.println(o);


        threadPoolComponent.destroy();
    }
}
