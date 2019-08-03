/**
 * 
 */
package com.ls.li.lock.lock1;

/**
 * @author lishuai
 * @data 2017-3-2 下午4:13:35
 */

public class Consumer implements Runnable {
	 
    @Override
    public synchronized void run() {
           // TODO Auto-generated method stub
           int count = 10;
           while(count > 0) {
                synchronized (Test. obj) {
                    
                    System. out.println( "B");
                    count --;
                    Test. obj.notify(); // 主动释放对象锁
                    
                     try {
                          Test. obj.wait();
                          
                    } catch (InterruptedException e) {
                           // TODO Auto-generated catch block
                          e.printStackTrace();
                    }
               }
               
          }
    }
}