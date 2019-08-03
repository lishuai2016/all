/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-2 下午3:31:32
 */

public class RemoveDuplicatesfromSortedArray {

	/**
	 * @author lishuai
	 * @data 2016-12-2 下午3:31:32
Given a sorted array, 
remove the duplicates in place such that each element appear only once and return the new length.

Do not allocate extra space for another array, you must do this in place with constant memory.

For example,
Given input array nums = [1,1,2],

Your function should return length = 2, with the first two elements of nums being 1 and 2 respectively. 
It doesn't matter what you leave beyond the new length.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a = {1,1,2,3,4,5,6,6,6,7};
		System.out.println(removeDuplicates2(a));
	}
	//1 思路：定义两个变量，一个代表上一个元素的值，一个表示无重复元素数组中已经存在多少元素的下标，通过当前值是否和上一个值相等来判断是否放入数组，时间复杂度N
	public static int removeDuplicates1(int[] nums) {
		if (nums == null || nums.length == 0) return 0;
		int cur = nums[0];
		int k = 1;
        for (int i = 1;i < nums.length;i++) {
    	   if (cur != nums[i]) {
    		   nums[k] = nums[i];
    		   cur = nums[i];
    		   k++;
    	   }
        }
		return k;
    }
	
	//2 和1的思路一样，但是减少一个变量，优化代码。时间复杂度N
	public static int removeDuplicates2(int[] nums) {
		if (nums == null || nums.length == 0) return 0;		
		int k = 1;
        for (int i = 1;i < nums.length;i++) {
    	   if (nums[i - 1] != nums[i]) {
    		   nums[k++] = nums[i];
    	   }
        }
		return k;
    }
	
	
	
}
