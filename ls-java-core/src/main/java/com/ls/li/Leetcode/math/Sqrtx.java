/**
 * 
 */
package com.ls.li.Leetcode.math;

/**
 * @author lishuai
 * @data 2017-1-10 上午11:13:08
 */

public class Sqrtx {

	/**
	 * @author lishuai
	 * @data 2017-1-10 上午11:13:08
Implement int sqrt(int x).

Compute and return the square root of x.
	 */

	public static void main(String[] args) {
		System.out.println(mySqrt(-4));

	}
	//1
	 public static int mySqrt(int x) {        
	    if (x <= 0) {
	    	return 0;
	    }
	    int res = 0;
	    long temp = 1;
	    for (int i = 1; i < Integer.MAX_VALUE; i++) {
	    	
	    }
	    
	    return res;
	 }
	//1内建函数
    public static int mySqrt0(int x) {        
    	return (int)Math.sqrt(x);
    }
}
