package com.ls.lishuai.concurrency.producer_consumer;

public class Consumer implements Runnable{
	private EventStorage store;
	public Consumer(EventStorage store) {
		this.store = store;
	}
	public void run() {
		for (int i = 0; i< 100; i++) {
			store.get();
		}

	}
}
