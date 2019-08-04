package com.roncoo.eshop.inventory.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

import com.roncoo.eshop.inventory.request.Request;

/**
 * 执行请求的工作线程
 * @author Administrator
 *
 */
public class RequestProcessorThread implements Callable<Boolean> {
	
	/**
	 * 自己监控的内存队列
	 */
	private ArrayBlockingQueue<Request> queue;

	public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
		this.queue = queue;
	}
	
	@Override
	public Boolean call() throws Exception {
		try {
			while(true) {
				// ArrayBlockingQueue
				// Blocking就是说明，如果队列满了，或者是空的，那么都会在执行操作的时候，阻塞住
				Request request = queue.take();
				System.out.println("===========日志===========: 工作线程处理请求，商品id=" + request.getProductId()); 
				// 执行这个request操作
				request.process();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
