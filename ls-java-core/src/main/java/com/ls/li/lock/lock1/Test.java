/**
 * 
 */
package com.ls.li.lock.lock1;

/**
 * @author lishuai
 * @data 2017-3-2 下午4:14:39
 */

public class Test {
	public static final Object obj = new Object();
    
    public static void main(String[] args) {
          
           new Thread( new Produce()).start();
           new Thread( new Consumer()).start();
          
    }
}
