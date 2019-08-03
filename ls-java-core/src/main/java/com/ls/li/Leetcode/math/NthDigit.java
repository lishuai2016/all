/**
 * 
 */
package com.ls.li.Leetcode.math;

/**
 * @author lishuai
 * @data 2017-1-9 下午5:50:58
 */

public class NthDigit {

	/**
	 * @author lishuai
	 * @data 2017-1-9 下午5:50:58
Find the nth digit of the infinite integer sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...

Note:
n is positive and will fit within the range of a 32-bit signed integer (n < 231).

Example 1:

Input:
3

Output:
3
Example 2:

Input:
11

Output:
0

Explanation:
The 11th digit of the sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... is a 0, 
which is part of the number 10.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
    public static int findNthDigit(int n) {
    	if (n <= 0) {
    		return -1;
    	}
        return 0;
    }
}
