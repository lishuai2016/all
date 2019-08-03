/**
 * 
 */
package com.ls.li.Leetcode.math;

import java.util.Arrays;

/**
 * @author lishuai
 * @data 2017-1-9 下午5:54:24
 */

public class MinimumMovestoEqualArrayElements {

	/**
	 * @author lishuai
	 * @data 2017-1-9 下午5:54:24
Given a non-empty integer array of size n, 
find the minimum number of moves required to make all array elements equal,
 where a move is incrementing n - 1 elements by 1.

Example:

Input:
[1,2,3]

Output:
3

Explanation:
Only three moves are needed (remember each move increments two elements):

[1,2,3]  =>  [2,3,3]  =>  [3,4,3]  =>  [4,4,4]
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
    public static int minMoves(int[] nums) {
    	if (nums == null || nums.length < 2) {
    		return -1;
    	}
    	int steps = 0;
    	Arrays.sort(nums);
    	boolean flag = true;
    	while (flag) {
    		
    	}
    	
    	
    	
        return steps;
    }
}
