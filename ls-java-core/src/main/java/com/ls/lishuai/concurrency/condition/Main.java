package com.ls.lishuai.concurrency.condition;

public class Main {

	/**
	 * ������ʹ�ö�����
	 */
	public static void main(String[] args) {
		FileMock mock = new FileMock(100,10);
		Buffer buffer = new Buffer(20);
		Producer p = new Producer(mock,buffer);
		Thread tp = new Thread(p);
		
		Consumer[] c = new Consumer[3];
		Thread[] ts = new Thread[3];
		for (int i = 0;i <3;i++) {
			c[i] = new Consumer(buffer);
			ts[i] = new Thread(c[i],"consumer " +i);
		}
		tp.start();
		for (int i = 0;i <3;i++) {
			ts[i].start();
		}
	}

}
