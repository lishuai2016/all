package com.ls.lishuai.concurrency.threadgroup;

import java.util.concurrent.TimeUnit;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadGroup tg = new ThreadGroup("searcher");
		Result r = new Result();
		SearchTask s = new SearchTask(r);
		for (int i=0; i <5; i++ ) {
			Thread t = new Thread(tg,s);
			t.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("number of threads: %d\n",tg.activeCount());
		System.out.printf("information about the thread group\n");
		tg.list();
		Thread[] threads = new Thread[tg.activeCount()];
		tg.enumerate(threads);
		for (int i=0;i<tg.activeCount();i++) {
			System.out.printf("thread %s : %s\n",threads[i].getName(),threads[i].getState());
		}
		waitfinish(tg);
		tg.interrupt();
		
	}

	private static void waitfinish(ThreadGroup tg) {
		while (tg.activeCount() > 9) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
