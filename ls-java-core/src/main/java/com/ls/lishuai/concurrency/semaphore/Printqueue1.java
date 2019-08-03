package com.ls.lishuai.concurrency.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * �ź������ƶั����Դ���3
 * @author lishuai
 *
 */
public class Printqueue1 {
	private final Semaphore s;
	private boolean[] freeprinters;
	private Lock lock;
	public Printqueue1() {
		this.s = new Semaphore(3);//
		freeprinters = new boolean[3];
		for (int i=0;i < 3;i++) {
			freeprinters[i] = true;
		}
		lock = new ReentrantLock();
	}
	public void printjob() {
		try {
			s.acquire();
			int assignedprinter = getprinter();
			Long duration = (long)(Math.random()*10);
			System.out.printf(" %s printqueue: printing a job %d during  %d    seconds\n",Thread.currentThread().getName(),assignedprinter,duration);
			TimeUnit.SECONDS.sleep(duration);
			freeprinters[assignedprinter]=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			s.release();
		}
	}
	private int getprinter() {
		int ret = -1;
		try {
			lock.lock();
			for (int i = 0;i < freeprinters.length;i++) {
				if (freeprinters[i]) {
					ret = i;
					freeprinters[i] =false;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return ret;
	}
}
