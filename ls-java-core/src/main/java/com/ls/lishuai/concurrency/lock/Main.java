package com.ls.lishuai.concurrency.lock;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrintQueue queue = new PrintQueue();
		
		Thread[] threads = new Thread[10];
		for (int i = 0; i< 10; i++) {
			threads[i] = new Thread(new Job(queue),"thread " + i);
		}
		for (int i = 0; i< 10; i++) {
			threads[i].start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

}
