package com.ls.lishuai.concurrency.threadpoolexecutor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		for (int i=0;i < 10; i++) {
			Task t = new Task("task" + i);
			server.executorTask(t);
		}
		server.endServer();
	}

}
