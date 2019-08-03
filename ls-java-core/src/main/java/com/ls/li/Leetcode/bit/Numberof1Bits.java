/**
 * 
 */
package com.ls.li.Leetcode.bit;

/**
 * @author lishuai
 * @data 2017-1-5 下午2:50:10
 */

public class Numberof1Bits {

	/**
	 * @author lishuai
	 * @data 2017-1-5 下午2:50:10
Write a function that takes an unsigned integer and returns the number of ’1' bits it has 
(also known as the Hamming weight).

For example, the 32-bit integer ’11' has binary representation 00000000000000000000000000001011, 
so the function should return 3.
	 */

	public static void main(String[] args) {
		System.out.println(hammingWeight(3));

	}
	
	//4
	public static int hammingWeight4(int n) {
		int ones = 0;
	    	while(n!=0) {
	    		ones = ones + (n & 1);
	    		n = n>>>1;
	    	}
	    	return ones;
	}
	//3 九章 
	 public int hammingWeight3(int n) {
	        if (n == 0) {
	            return 0;
	        }
	        
	        int count = 1;
	        while ((n & (n - 1)) != 0) {
	            n &= n-1;
	            count++;
	        }
	        return count;
	    }
    // 1有几个1时间复杂度就是多少
    public static int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
        	n = n & (n - 1);
        	count++;
        }        
        return count;
    }
    //2 内建函数
    public static int hammingWeight2(int n) {       
        return Integer.bitCount(n);
    }
}
