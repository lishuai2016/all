package com.ls.lishuai.concurrency.condition;

public class Producer implements Runnable {
	private FileMock mock;
	private Buffer buffer;
	
	public Producer(FileMock mock,Buffer buffer) {
		this.mock = mock;
		this.buffer = buffer;
	}
	public void run() {
		buffer.setPendinglines(true);
		while (mock.hasmorelines()) {
			String line = mock.getline();
			buffer.insert(line);
		}
		buffer.setPendinglines(false);
	}

}
