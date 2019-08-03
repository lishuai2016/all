package com.ls.lishuai.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {

	//private final Lock queueLock = new ReentrantLock();//�ǹ�ƽģʽ
	private final Lock queueLock = new ReentrantLock(true);//��ƽģʽ���޸����Ĺ�ƽ�ԣ�
	
	public void printjob() {
		queueLock.lock();
		try {
			Long duration = (long)(Math.random()*10000);
			System.out.printf(Thread.currentThread().getName() +": printqueue: printing a job during   " + (duration/1000) + "   seconds");
			System.out.println();
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
		
		queueLock.lock();
		try {
			Long duration = (long)(Math.random()*10000);
			System.out.printf(Thread.currentThread().getName() +": printqueue: printing a job during   " + (duration/1000) + "   seconds");
			System.out.println();
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
	}
}
