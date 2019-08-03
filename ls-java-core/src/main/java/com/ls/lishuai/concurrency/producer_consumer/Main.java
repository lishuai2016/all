package com.ls.lishuai.concurrency.producer_consumer;

public class Main {

	/**
	 * ������������ģʽ
	 */
	public static void main(String[] args) {
		EventStorage store = new EventStorage();
		
		Producer p = new Producer(store);
		Thread t1 = new Thread(p);

		Consumer c = new Consumer(store);
		Thread t2 = new Thread(c);
		
		t1.start();
		t2.start();
	}

}
