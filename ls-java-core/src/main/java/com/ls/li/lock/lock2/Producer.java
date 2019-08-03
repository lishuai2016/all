/**
 * 
 */
package com.ls.li.lock.lock2;

import java.util.concurrent.locks.Lock;

/**
 * @author lishuai
 * @data 2017-3-2 下午4:21:49
 */

public class Producer implements Runnable{
	 
    private Lock lock;
    public Producer(Lock lock) {
           this. lock = lock;
    }
    @Override
    public void run() {
           // TODO Auto-generated method stub
           int count = 10;
           while (count > 0) {
                try {
                     lock.lock();
                    count --;
                    System. out.print( "A");
               } finally {
                     lock.unlock();
                     try {
                          Thread. sleep(90L);
                    } catch (InterruptedException e) {
                           // TODO Auto-generated catch block
                          e.printStackTrace();
                    }
               }
          }
    }
}
