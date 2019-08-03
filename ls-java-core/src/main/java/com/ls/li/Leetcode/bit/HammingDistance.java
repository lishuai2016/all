/**
 * 
 */
package com.ls.li.Leetcode.bit;

/**
 * @author lishuai
 * @data 2017-1-4 下午3:22:26
 */

public class HammingDistance {

	/**
	 * @author lishuai
	 * @data 2017-1-4 下午3:22:26
The Hamming distance between two integers is the number of positions at which the corresponding bits are different.

Given two integers x and y, calculate the Hamming distance.

Note:
0 ≤ x, y < 231.

Example:

Input: x = 1, y = 4

Output: 2

Explanation:
1   (0 0 0 1)
4   (0 1 0 0)
       ↑   ↑

The above arrows point to positions where the corresponding bits are different.
	 */

	public static void main(String[] args) {
		System.out.println(hammingDistance(1,4));

	}
	//2使用内建函数   12ms
	public static int hammingDistance1(int x, int y) {
		return Integer.bitCount(x ^ y);
	}
	
	
	//1思路：先异或操作，然后统计结果中1的个数   14ms
    public static int hammingDistance(int x, int y) {
    	int res = 0;
    	int temp = x ^ y;
    	while (temp != 0) {
    		temp &= (temp - 1);
    		res++;
    	}
        return res;
    }
}
