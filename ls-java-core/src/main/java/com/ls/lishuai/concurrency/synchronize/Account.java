package com.ls.lishuai.concurrency.synchronize;

public class Account {
	private final Object o;
	
	private double balance;
	public Account() {
		o = new Object();
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	//o����ʹ��this���棨ʹ�÷���������ʵ��ͬ����
	public  void addamount(double amount) {
		synchronized(o) {
			double temp = balance;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			temp+=amount;
			balance = temp;
		}
	}
	//
	public  void subtractamount(double amount) {
		synchronized(o) {
			double temp = balance;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			temp-=amount;
			balance = temp;
		}
		
	}
	
	//synchronized
//	public  synchronized void addamount(double amount) {
//		double temp = balance;
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		temp+=amount;
//		balance = temp;
//	}
	//
//	public synchronized void subtractamount(double amount) {
//		double temp = balance;
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		temp-=amount;
//		balance = temp;
//	}
}
