package com.ls.lishuai.concurrency.forkjoinpool1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class DocumentTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;

	private String[][] document;
	private int start,end;
	private String word;
	public DocumentTask(String[][] document,int start,int end,String word) {
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}
	@Override
	protected Integer compute() {
		Integer result = null;
		if (end - start< 10) {
			 result = processlines(document,start,end,word);
		} else {
			int m = (start+end)/2;
			DocumentTask t1 = new DocumentTask(document,start,m,word);
			DocumentTask t2 = new DocumentTask(document,m,end,word);
			invokeAll(t1,t2);
			try {
				result = t1.get()+t2.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	private Integer processlines(String[][] document,int start,int end,String word) {
		List<Linetask> tasks = new ArrayList<Linetask>();
		for (int i=start;i<end;i++) {
			Linetask t = new Linetask(document[i],0,document[i].length,word);
			tasks.add(t);
		}
		invokeAll(tasks);
		Integer res = 0;
		for (int i = 0;i<tasks.size();i++) {
			Linetask t = tasks.get(i);
			try {
				res+=t.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
}
