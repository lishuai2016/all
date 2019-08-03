/**
 * 
 */
package com.ls.li.lock.lock2;

import java.util.concurrent.locks.Lock;

/**
 * @author lishuai
 * @data 2017-3-2 下午4:21:23
 */

public class Consumer implements Runnable {
	 
    private Lock lock;
    public Consumer(Lock lock) {
           this. lock = lock;
    }
    @Override
    public void run() {
           // TODO Auto-generated method stub
           int count = 10;
           while( count > 0 ) {
                try {
                     lock.lock();
                    count --;
                    System. out.print( "B");
               } finally {
                     lock.unlock(); //主动释放锁
                     try {
                          Thread. sleep(91L);
                    } catch (InterruptedException e) {
                           // TODO Auto-generated catch block
                          e.printStackTrace();
                    }
               }
          }

    }

}
