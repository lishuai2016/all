package com.ls.lishuai.concurrency.threadlocal;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UnsafeTask implements Runnable {

	private Date starttime;
	public void run() {
		starttime = new Date();
		System.out.printf("starting thread: %s : %s\n",Thread.currentThread().getId(),starttime);
		
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random() * 10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("starting thread: %s : %s\n",Thread.currentThread().getId(),starttime);
	}

}
