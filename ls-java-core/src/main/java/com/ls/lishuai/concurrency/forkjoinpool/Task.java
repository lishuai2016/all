package com.ls.lishuai.concurrency.forkjoinpool;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction{

	private static final long serialVersionUID = 1L;

	private List<Product> products;
	private int first;
	private int last;
	private double increment;
	public Task(List<Product> products,int first,int last,double increment) {
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}
	@Override
	protected void compute() {
		if (last-first < 10) {
			updateprices();
			//System.out.printf("update first:%d  last:%d\n",first,last );
		} else {
			int m = (last+first) /2;
			System.out.printf("task: pending tasks : %s  m: %d \n", getQueuedTaskCount(),m);//
			Task t1 = new Task(products,first,m+1,increment);
			Task t2 = new Task(products,m+1,last,increment);
			invokeAll(t1,t2);//
		}
		
	}
	
	private void updateprices() {
		for (int i = first;i < last;i++) {
			Product p = products.get(i);
			p.setPrice(p.getPrice()*(1+increment));
			
		}
	}

}
