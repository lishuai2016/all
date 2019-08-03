package com.ls.lishuai.concurrency;

public class Calculator implements Runnable {

	private int number;
	public Calculator(int number) {
		this.number = number;
	}
	public void run() {
		for (int i = 1; i <= 10; i++) {
			System.out.printf("%s : %d * %d = %d\n", Thread.currentThread().getName(), number,i, i * number);
		}

	}

}
