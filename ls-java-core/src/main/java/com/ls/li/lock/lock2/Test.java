/**
 * 
 */
package com.ls.li.lock.lock2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lishuai
 * @data 2017-3-2 下午4:22:23
 */

public class Test {
	 public static void main(String[] args) {
         Lock lock = new ReentrantLock();
         
         Consumer consumer = new Consumer(lock);
         Producer producer = new Producer(lock);
         
          new Thread(consumer).start();
          new Thread( producer).start();
         
   }
}
