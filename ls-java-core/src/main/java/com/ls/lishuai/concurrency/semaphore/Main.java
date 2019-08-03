package com.ls.lishuai.concurrency.semaphore;

public class Main {

	/**
	 * �ź�����ʹ�ã�������һ��Դ��
	 */
	public static void main(String[] args) {
//		Printqueue1 p = new Printqueue1();
//		Thread[] ts = new Thread[10];
//		for (int i=0;i < 10; i++) {
//			ts[i] = new Thread(new Job(p),"thread" + i);
//		}
//		for (int i=0;i < 10; i++) {
//			ts[i].start();
//		}
		
		for (int i = 0;i <100; i++) {
			System.out.println(Math.random());//С��1��double��
		}
	}

}
