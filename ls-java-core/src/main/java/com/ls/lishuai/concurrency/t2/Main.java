package com.ls.lishuai.concurrency.t2;

import java.util.concurrent.TimeUnit;

public class Main {

	/**
	 * �̵߳��жϣ����⻹����ͨ��InterruptedException�쳣���жϳ���
	 */
	public static void main(String[] args) {
		Thread task = new PrimeGenerator();
		task.start();
		//5���Ӻ���ֹ�߳�
		try {
			//Thread.sleep(5000);//����
			TimeUnit.SECONDS.sleep(1);//�룬������������ʱ��ķ�ʽ
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		task.interrupt();//��ֹ�߳�
	}

}
