/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-6 下午5:35:38
 */

public class SearchinRotatedSortedArrayII {

	/**
	 * @author lishuai
	 * @data 2016-12-6 下午5:35:38
Suppose a sorted array is rotated at some pivot unknown to you beforehand.

(i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).

You are given a target value to search. If found in the array return its index, otherwise return -1.

You may assume no duplicate exists in the array.



Follow up for "Search in Rotated Sorted Array":
What if duplicates are allowed?

Would this affect the run-time complexity? How and why?

Write a function to determine if a given target is in the array.
	 */
	public static void main(String[] args) {
		int[] a = {4, 5 ,6 ,7,0, 1 ,2 };
		search(a,6);
	}
	//2 二分查找
	public static boolean search(int[] nums, int target) {
		if (nums == null || nums.length == 0) return false;
	    int low = 0;
	    int high = nums.length - 1;
	    //递增
	    int position = 0;	   
	    if (nums[low] >= nums[high]) {
	    	while (low <= high) {
	 	    	int mid = low + (high - low) / 2;
	 	    	if (nums[mid] > nums[low]) {
	 	    		low = mid + 1;
	 	    	} else {
	 	    		high = mid - 1;
	 	    	}
	 	    }		
	    }
	    
    	return false;
    }
	
	//1 直接遍历时间复杂度N
    public static boolean search1(int[] nums, int target) {
	    for (int i = 0;i < nums.length;i++) if (nums[i] == target) return true;
    	return false;
    }
}
