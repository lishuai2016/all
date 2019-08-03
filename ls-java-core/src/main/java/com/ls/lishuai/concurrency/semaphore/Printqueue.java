package com.ls.lishuai.concurrency.semaphore;

import java.util.concurrent.Semaphore;

public class Printqueue {

	private final Semaphore s;
	public Printqueue() {
		s = new Semaphore(1);
	}
	public void printjob() {
		try {
			s.acquire();
			Long duration = (long)(Math.random()*10000);
			System.out.printf(Thread.currentThread().getName() +": printqueue: printing a job during   " + (duration/1000) + "   seconds");
			System.out.println();
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			s.release();
		}
	}
}
