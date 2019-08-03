package com.ls.lishuai.concurrency.readwritelock;

public class Writer implements Runnable {

	private PricesInfo p;
	public Writer(PricesInfo p) {
		this.p = p;
	}
	public void run() {
		for (int i = 0; i< 1; i++) {
			System.out.printf("writer: attempt to modify the prices\n");
			p.setprices(Math.random()*10, Math.random()*8);
			System.out.printf("writer: the prices has been modified\n");
//			try {
//				Thread.sleep(2);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}

	}

}
