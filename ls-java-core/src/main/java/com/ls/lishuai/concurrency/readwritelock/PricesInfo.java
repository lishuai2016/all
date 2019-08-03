package com.ls.lishuai.concurrency.readwritelock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PricesInfo {

	private double prices1;
	private double prices2;
	private ReadWriteLock lock;
	
	public PricesInfo() {
		prices1 = 1.0;
		prices2 = 2.0;
		lock = new ReentrantReadWriteLock();
	}
	public double getprices1() {
		lock.readLock().lock();
		double value = prices1;
		lock.readLock().unlock();
		return value;
	}
	public double getprices2() {
		lock.readLock().lock();
		double value = prices2;
		lock.readLock().unlock();
		return value;
	}
	public void setprices(double prices1,double prices2) {
		lock.writeLock().lock();
		this.prices1 = prices1;
		this.prices2 = prices2;
		lock.writeLock().unlock();
	}
}
