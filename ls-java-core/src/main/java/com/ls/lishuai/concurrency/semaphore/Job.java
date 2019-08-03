package com.ls.lishuai.concurrency.semaphore;

public class Job implements Runnable {

	private Printqueue1 queue;
	public Job(Printqueue1 queue) {
		this.queue = queue;
	}
	public void run() {
		System.out.printf("%s: going to print a document\n",Thread.currentThread().getName());
		queue.printjob();
		System.out.printf("%s: the document has been printed\n",Thread.currentThread().getName());
	}

}
