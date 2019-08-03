package com.ls.lishuai.concurrency.t1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	/**
	 * �߳���Ϣ�Ļ�ȡ������
	 */
	public static void main(String[] args) {
		Thread[] threads = new Thread[10];
		Thread.State status[] = new Thread.State[10];
		for (int i = 0;i < 10; i++ ) {
			threads[i] = new Thread(new Calculator(i));
			if (i % 2 == 0) {
				threads[i].setPriority(Thread.MAX_PRIORITY);
			} else {
				threads[i].setPriority(Thread.MIN_PRIORITY);
			}
			threads[i].setName("thread " + i );
		}
		FileWriter file = null;
		PrintWriter pw = null;
		try {
			file = new FileWriter(".\\log.txt");
			pw = new PrintWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//��ʼ��״̬
		for (int i = 0; i < 10; i++) {
			pw.println("Main : Status of Thread "+ i + " : " + threads[i].getState());
			status[i] = threads[i].getState();
		}
		//�����߳�
		for (int i = 0; i < 10; i++) {
			threads[i].start();
		}
		boolean finish = false;
		while (!finish) {
			for (int i = 0; i < 10; i++) {
				if (threads[i].getState() != status[i]) {
					writeThreadInfo(pw,threads[i],status[i]);
					status[i] = threads[i].getState();
				}
			}
			finish=true;
			for (int i = 0; i < 10; i++) {
				finish=finish&(threads[i].getState() == Thread.State.TERMINATED);
			}
		}
		pw.close();//���ӹرջ�����ļ��е�����Ϊ��
	}
	
	private static void writeThreadInfo (PrintWriter pw,Thread thread,Thread.State status) {
		pw.printf("Mian : id %d - %s \n",thread.getId(),thread.getName());pw.println();
		pw.printf("Mian : priority: %d \n",thread.getPriority());pw.println();
		pw.printf("Mian : old status %s \n",status);pw.println();
		pw.printf("Mian : new status %s \n",thread.getState());pw.println();
		pw.printf("Mian : ********************************\n");pw.println();
	}

}
