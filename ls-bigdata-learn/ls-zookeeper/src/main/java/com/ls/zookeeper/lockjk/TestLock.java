package com.ls.zookeeper.lockjk;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class TestLock {
	private static final Logger LOG = LoggerFactory.getLogger(TestLock.class);
	 //确保所有线程运行结束；
	private static final String CONNECTION_STRING = "192.168.80.201:2181";
	private static final int THREAD_NUM = 10; 
	public static   CountDownLatch threadSemaphore = new CountDownLatch(THREAD_NUM);
	private static final String GROUP_PATH = "/disLocks";
	private static final String SUB_PATH = "/disLocks/sub";
	private static final int SESSION_TIMEOUT = 10000;
	
	public static void main(String[] args) {
	        for(int i=0; i < THREAD_NUM; i++){
	            final int threadId = i;
	            new Thread(){
	                @Override
	                public void run() {
	                    try{
	                    	 new LockService().doService(new DoTemplate() {
								
								@Override
								public void dodo() {
									// TODO Auto-generated method stub
									LOG.info("我要修改一个文件。。。。"+threadId);
								}
							});
	                    } catch (Exception e){
	                        LOG.error("【第"+threadId+"个线程】 抛出的异常：");
	                        e.printStackTrace();
	                    }
	                }
	            }.start();
	        }
	        try {
//	        	Thread.sleep(60000);
	        	threadSemaphore.await();
	            LOG.info("所有线程运行结束!");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
