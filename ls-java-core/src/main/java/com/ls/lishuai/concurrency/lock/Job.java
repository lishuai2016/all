package com.ls.lishuai.concurrency.lock;

public class Job implements Runnable {

	private PrintQueue queue;
	public Job(PrintQueue queue) {
		this.queue = queue;
	}
	public void run() {
		System.out.printf("%s: going to print a document\n",Thread.currentThread().getName());
		queue.printjob();
		System.out.printf("%s: the document has been printed\n",Thread.currentThread().getName());
	}

}
