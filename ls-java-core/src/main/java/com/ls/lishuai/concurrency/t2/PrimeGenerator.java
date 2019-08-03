package com.ls.lishuai.concurrency.t2;

public class PrimeGenerator extends Thread {

	public void run() {
		long number = 1l;
		while (true) {
			if (isPrime(number)) {
				System.out.printf("number %d is prime\n",number);
			}
			if (isInterrupted()) {//�ж��߳��Ƿ���ֹ
				System.out.printf("Interrupted");
				return;//����ֹʱ����
			}
			number++;
		}
	}
	
	private boolean isPrime(long number) {
		if (number <= 2) {
			return true;
		}
		for (long i = 2; i < number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}
}
