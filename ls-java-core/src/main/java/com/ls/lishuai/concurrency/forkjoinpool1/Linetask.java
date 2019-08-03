package com.ls.lishuai.concurrency.forkjoinpool1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class Linetask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;

	private String[] line;
	private int start,end;
	private String word;
	public Linetask(String[] line,int start,int end,String word) {
		this.line = line;
		this.start = start;
		this.end = end;
		this.word = word;
	}
	@Override
	protected Integer compute() {
		Integer res = null;
		if (end - start<100) {
			res = count(line,start,end,word);
		} else {
			int m = (start+ end)/2;
			Linetask t1 = new Linetask(line,start,m,word);
			Linetask t2 = new Linetask(line,m,end,word);
			invokeAll(t1,t2);
			
			try {
				res=t1.get()+t2.get();
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	private Integer count(String[] line,int start,int end,String word) {
		int count=0;
		for (int i = start;i < end;i++) {
			if (line[i].endsWith(word)) {
				count++;
			}
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return count;
	}

}
