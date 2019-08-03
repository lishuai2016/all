/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.Arrays;

/**
 * @author lishuai
 * @data 2016-12-2 上午11:26:59
 */

public class Sum3Closest {

	/**
	 * @author lishuai
	 * @data 2016-12-2 上午11:26:59
Given an array S of n integers, find three integers in S such that the sum is closest to a given number, target.
Return the sum of the three integers. You may assume that each input would have exactly one solution.

    For example, given array S = {-1 2 1 -4}, and target = 1.

    The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

    public static int threeSumClosest(int[] nums, int target) {
    	Arrays.sort(nums);
    	int res = 0;
        for (int i = 0;i < nums.length-2;i++) {
        	if (i == 0 || (i > 0 && nums[i] != nums[i+1])) {
        		int left = i + 1;
        		int right = nums.length - 1;
        	    int x = nums[i];
        		while (left < right) {
        			
        		}
        	}
        }
    	
    	
    	
    	return res;
    }
}
