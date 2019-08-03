package com.ls.lishuai.concurrency.condition;

import java.util.Random;

public class Consumer implements Runnable {

	private Buffer buffer;
	
	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}
	public void run() {
		while( buffer.hasPendingLines()) {
			String line = buffer.get();
			processline(line);
		}
	}
	private void processline(String line) {
		try {
			Random random = new Random();
			Thread.sleep(random.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
