package com.ls.zookeeper.lock;

import java.net.InetAddress;

public class Main {

	/**
	 * @param args
	 * 控制不同进程使用某公共资源
	 * 
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		System.out.println(Thread.currentThread().getName());
		
		InetAddress address = InetAddress.getLocalHost();
		System.out.println(address);
//		
//		 for(int i=0; i < THREAD_NUM; i++){  
//	            final int threadId = i+1;  
//	            new Thread(){  
//	                @Override  
//	                public void run() {  
//	                    try{  
//	                        DistributedLock dc = new DistributedLock(threadId);  
//	                        dc.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);  
//	                        //GROUP_PATH不存在的话，由一个线程创建即可；  
//	                        synchronized (threadSemaphore){  
//	                            dc.createPath(GROUP_PATH, "该节点由线程" + threadId + "创建", true);  
//	                        }  
//	                        dc.getLock();  
//	                    } catch (Exception e){  
//	                        LOG.error("【第"+threadId+"个线程】 抛出的异常：");  
//	                        e.printStackTrace();  
//	                    }  
//	                }  
//	            }.start();  
//		 }
		 
		Lock lock = LockFactory.getLock("/root/test", address.toString());
		
		
		while(true)
		{
			if (lock == null) {
				//to do
				
			}
			else {
				Thread.sleep(5*1000);
				System.out.println(lock.isLock());
			}
		}
		
		
	}

}
