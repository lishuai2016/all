package com.ls.lishuai.jvm.demo;

public class TestDeadThread implements Runnable {
	int a, b;

	public TestDeadThread(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public void run() {
		synchronized (Integer.valueOf(a)) {
			synchronized (Integer.valueOf(b)) {
				System.out.println(a + b);
			}
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new TestDeadThread(1, 2)).start();
			new Thread(new TestDeadThread(2, 1)).start();
		}
	}
}
