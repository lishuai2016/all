package com.ls.lishuai.concurrency.synchronize;

public class Company implements Runnable {

	private Account account;
	public Company(Account account) {
		this.account = account;
	}
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("��");
			account.addamount(1000);
		}
	}

}
