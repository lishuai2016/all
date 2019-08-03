package com.ls.lishuai.concurrency.synchronize;

public class Bank implements Runnable {

	private Account account;
	public Bank(Account account) {
		this.account = account;
	}
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("��");
			account.subtractamount(1000);
		}
	}

}
