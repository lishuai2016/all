package com.ls.lishuai.concurrency.readwritelock;

public class Reader implements Runnable {

	private PricesInfo p;
	public Reader(PricesInfo p) {
		this.p = p;
	}
	public void run() {
		for (int i = 0; i< 10; i++) {
			System.out.printf("%s: price1: % f\n",Thread.currentThread().getName(),p.getprices1());
			System.out.printf("%s: price2: % f\n",Thread.currentThread().getName(),p.getprices2());
		}

	}

}
