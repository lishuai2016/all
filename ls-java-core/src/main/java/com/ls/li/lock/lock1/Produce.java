/**
 * 
 */
package com.ls.li.lock.lock1;

/**
 * @author lishuai
 * @data 2017-3-2 下午4:14:08
 */

public class Produce implements Runnable {
	 
    @Override
    public void run() {
           // TODO Auto-generated method stub
           int count = 10;
           while(count > 0) {
                synchronized (Test. obj) {
                    
                     System.out.print("count = " + count);
                    System. out.println( "A");
                    count --;
                    Test. obj.notify();
                    
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