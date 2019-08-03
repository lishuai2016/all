package com.ls.lishuai.concurrency.readwritelock;

public class Main {

	/**
	 * ������
	 */
	public static void main(String[] args) {
		PricesInfo p = new PricesInfo();
		
		
		Reader[] rs = new Reader[5];
		Thread[] ts = new Thread[5];
		for (int i=0;i < 5;i++) {
			rs[i] = new Reader(p);;
			ts[i] = new Thread(rs[i]);
		}
		
		Writer w = new Writer(p);
		Thread tw = new Thread(w);
		for (int i=0;i < 5;i++) {
			ts[i].start();
		}
		
		tw.start();
	}

}
