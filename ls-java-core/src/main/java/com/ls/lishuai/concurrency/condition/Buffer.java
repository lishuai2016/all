package com.ls.lishuai.concurrency.condition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {

	private LinkedList<String> buffer;
	private int maxsize;
	private ReentrantLock lock;
	private Condition lines;
	private Condition space;
	private boolean pendinglines;
	public void setPendinglines(boolean pendinglines) {
		this.pendinglines = pendinglines;
	}

	public Buffer(int maxsize) {
		this.maxsize=maxsize;
		buffer=new LinkedList<String>();
		lock=new ReentrantLock();
		lines=lock.newCondition();
		space=lock.newCondition();
		pendinglines=true;
	}
	
	public void insert(String line) {
		lock.lock();
		try {
			while (buffer.size() == maxsize) {
				space.await();
			}
			buffer.offer(line);
			System.out.printf("%s: inserted line: %d\n", Thread.currentThread().getName(),buffer.size());
			lines.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
	}
	public String get() {
		String line=null;
		lock.lock();
		
		try {
			while ((buffer.size() == 0) && (hasPendingLines())) {
				lines.await();
			}
			if (hasPendingLines()) {
				line = buffer.poll();
				System.out.printf("%s: line readed: %d\n",Thread.currentThread().getName(),buffer.size());
				space.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return line;
	}
	public boolean hasPendingLines() {
		return this.pendinglines || buffer.size()>0;
	}
	
}

