package com.ls.lishuai.concurrency.threadpoolexecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

	private ThreadPoolExecutor executor;
	public Server() {
//		executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);//�̶���С���̳߳�
	}
	public void executorTask(Task task) {
		System.out.println("server: a new task has arrived");
		executor.execute(task);
		System.out.printf("server: pool size : %d\n",executor.getPoolSize());
		System.out.printf("server: active count : %d\n",executor.getActiveCount());
		System.out.printf("server: completed tasks : %d\n",executor.getCompletedTaskCount());
		System.out.printf("server: task count : %d\n",executor.getTaskCount());
	}
	
	public void endServer() {
		executor.shutdown();
	}
}
