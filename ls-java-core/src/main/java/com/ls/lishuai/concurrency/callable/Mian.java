package com.ls.lishuai.concurrency.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Mian {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		Random random = new Random();
		for (int i=0;i < 10;i++) {
			Integer number = random.nextInt(10);
			FactorialCalculator f = new FactorialCalculator(number);
			Future<Integer> res = executor.submit(f);
			list.add(res);
		}
		do {
			System.out.printf("main: number of completed tasks: %d\n",executor.getCompletedTaskCount());
			for (int i=0;i < 10;i++) {
				Future<Integer> res = list.get(i);
				System.out.printf("main: task %d : %s\n",i,res.isDone());
			}
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		} while(executor.getCompletedTaskCount() < list.size());
		System.out.println("main: list");
		for (int i=0;i < list.size();i++) {
			Future<Integer> res = list.get(i);
			Integer number = null;
			try {
				number = res.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.printf("main: task %d %d\n",i,number);
		}
		
executor.shutdown();
	}

}
