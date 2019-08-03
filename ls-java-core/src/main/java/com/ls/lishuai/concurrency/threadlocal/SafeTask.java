package com.ls.lishuai.concurrency.threadlocal;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SafeTask implements Runnable {

	//�ܱ�֤��ͬ�߳�֮�以������
	private static ThreadLocal<Date> starttime = new ThreadLocal<Date>() {
		protected Date initialValue() {
			return new Date();
		}
	};
	public void run() {
		
		System.out.printf("starting thread: %s : %s\n",Thread.currentThread().getId(),starttime.get());
		
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random() * 10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("starting thread: %s : %s\n",Thread.currentThread().getId(),starttime.get());

	}

}
