package com.ls.lishuai.concurrency.threadpoolexecutor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	private Date initdate;
	private String name;
	public Task(String name) {
		initdate = new Date();
		this.name = name;
	}
	public void run() {
		try {
			System.out.printf("%s: task %s created on : %s\n",Thread.currentThread().getName(),name,initdate);
			System.out.printf("%s: task %s started on : %s\n",Thread.currentThread().getName(),name,new Date());
			Long duration = (long)(Math.random()*10);
			System.out.printf("%s task %s : doing a task durion %d seconds \n",Thread.currentThread().getName(),name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: task %s finisded on : %s\n",Thread.currentThread().getName(),name,new Date());
	}

}
