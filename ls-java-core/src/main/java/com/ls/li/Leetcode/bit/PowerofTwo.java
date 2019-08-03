/**
 * 
 */
package com.ls.li.Leetcode.bit;

/**
 * @author lishuai
 * @data 2017-1-5 下午3:06:32
 */

public class PowerofTwo {

	/**
	 * @author lishuai
	 * @data 2017-1-5 下午3:06:32
Given an integer, write a function to determine if it is a power of two.
2的多少次幂
1
10
100
1000
10000
	 */

	public static void main(String[] args) {
		System.out.println(Integer.bitCount(-1));

	}
	// 2是对 1的简化，只判断一次
	public static boolean isPowerOfTwo(int n) {
	    	if (n <= 0) return false;      
	    	return  (n & (n - 1))== 0 ? true : false;	    	
	}
	
	// 1根据上面的特点 只能最高位是1才成立，因此统计1的个数，不为1的都是false
    public static boolean isPowerOfTwo1(int n) {
    	if (n <= 0) return false;      
    	return  Integer.bitCount(n) != 1 ? false : true;
    }
    //0
    public boolean isPowerOfTwo0(int n) {
        Boolean tag = false;
        if(n == 0) {
 			return false;
 		}
 		if(n == 1) {
 			tag = true;
 		} else {
 			while((n != 1) && ( n % 2 == 0)) {
 				n = n/2;				
 			}
 			if(n == 1) {
 				tag=true;
 			}
 		}
 		return tag;
     }
}
