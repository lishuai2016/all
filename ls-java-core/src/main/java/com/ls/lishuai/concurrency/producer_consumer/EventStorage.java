package com.ls.lishuai.concurrency.producer_consumer;

import java.util.Date;
import java.util.LinkedList;


public class EventStorage {

	private int maxSize;
	private LinkedList<Date> storage;
	public EventStorage() {
		this.maxSize = 10;
		this.storage = new LinkedList<Date>();
	}
	//
	public synchronized void set() {
		while (storage.size() == maxSize) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		storage.add(new Date());
		System.out.printf("set: %d   \n",storage.size());
		notifyAll();
	}
	//
	public synchronized void get() {
		while (storage.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("get: %d: %s  \n",storage.size(),storage.poll());
		notifyAll();
	}
}
