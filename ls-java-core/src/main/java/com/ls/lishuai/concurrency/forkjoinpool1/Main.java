package com.ls.lishuai.concurrency.forkjoinpool1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Document d = new Document();
		String[][] document = d.generateDocument(100, 1000, "the");
		DocumentTask task = new DocumentTask(document,0,100,"the");
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		do {
			System.out.println("****************************************************");
			System.out.printf("main: thread active count : %d\n",pool.getActiveThreadCount());
			System.out.printf("main: thread steal : %d\n",pool.getStealCount());
			System.out.printf("main: task count : %d\n",pool.getQueuedTaskCount());
			System.out.printf("main: thread parallelism : %d\n",pool.getParallelism());
			System.out.println("****************************************************");
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());

		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.printf("%d\n", task.get());
		} catch (InterruptedException e) {				
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
