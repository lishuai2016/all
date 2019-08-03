package com.ls.lishuai.mythread.pool;

import java.util.concurrent.*;

/**
 * callable 跟runnable的区别：
 * runnable的run方法不会有任何返回结果，所以主线程无法获得任务线程的返回值
 * 
 * callable的call方法可以返回结果，但是主线程在获取时是被阻塞，需要等待任务线程返回才能拿到结果
 * @author
 *
 */
public class ThreadPoolWithcallable {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool(4); 
		
		for(int i = 0; i < 10; i++){
			Future<String> submit = pool.submit(new Callable<String>(){
				@Override
				public String call() throws Exception {
					//System.out.println("a");
					Thread.sleep(5000);
					return "b--"+Thread.currentThread().getName();
				}			   
			   });
			//从Future中get结果，这个方法是会被阻塞的，一直要等到线程任务返回结果
			System.out.println(submit.get());
		} 
			pool.shutdown();

	}

}
