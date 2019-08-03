/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-5 上午9:41:00
 */

public class SingleNumber {

	/**
	 * @author lishuai
	 * @data 2017-1-5 上午9:41:01
Given an array of integers, every element appears twice except for one. Find that single one.

Note:
Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
	 */

	public static void main(String[] args) {
		

	}
	//1 使用位的异或操作 1ms
    public static int singleNumber(int[] nums) {
        if (nums == null || nums.length == 0) return -1;
        int res = 0;
    	for (int i : nums) {
    		res ^= i;
    	}
    	return res;
    }
}
