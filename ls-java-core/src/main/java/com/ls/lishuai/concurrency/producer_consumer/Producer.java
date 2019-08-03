package com.ls.lishuai.concurrency.producer_consumer;

public class Producer implements Runnable {

	private EventStorage store;
	public Producer(EventStorage store) {
		this.store = store;
	}
	public void run() {
		for (int i = 0; i< 100; i++) {
			store.set();
		}

	}

}
