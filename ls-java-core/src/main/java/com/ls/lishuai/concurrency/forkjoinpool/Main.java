package com.ls.lishuai.concurrency.forkjoinpool;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProductlistGenerator g = new ProductlistGenerator();
		List<Product> list = g.genetor(10000);
		Task task = new Task(list,0,list.size(),0.20);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		do {
			System.out.printf("main: thread count : %d\n",pool.getActiveThreadCount());
			System.out.printf("main: thread steal : %d\n",pool.getStealCount());
			System.out.printf("main: thread parallelism : %d\n",pool.getParallelism());
			
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());
		
		pool.shutdown();
		if (task.isCompletedNormally()) {
			System.out.println("main: thread has completed!");
		}
		for (int i = 0;i <list.size();i++) {
			Product p =list.get(i);
			if (p.getPrice() != 12) {
				System.out.printf("product %s : %f\n",p.getName(),p.getPrice());
			}
		}
		System.out.println("main: end!");
	}

}
