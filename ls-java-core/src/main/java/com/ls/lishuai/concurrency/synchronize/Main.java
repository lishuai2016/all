package com.ls.lishuai.concurrency.synchronize;

public class Main {

	/**
	 * synchronized 
	 */
	public static void main(String[] args) {
		Account a = new Account();
		a.setBalance(1000);
		Company c = new Company(a);
		Thread companyThread = new Thread(c);
		
		Bank b = new Bank(a);
		Thread bankThread = new Thread(b);
		System.out.printf("account : initial balance: %f\n",a.getBalance());
		companyThread.start();
		bankThread.start();
		
		try {
			companyThread.join();
			bankThread.join();
			System.out.printf("account : final balance: %f\n",a.getBalance());
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}

}
