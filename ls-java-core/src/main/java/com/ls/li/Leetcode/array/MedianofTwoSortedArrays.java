/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-2 上午8:58:59
 */

public class MedianofTwoSortedArrays {

	/**
	 * @author lishuai
	 * @data 2016-12-2 上午8:58:59
There are two sorted arrays nums1 and nums2 of size m and n respectively.

Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).

Example 1:
nums1 = [1, 3]
nums2 = [2]

The median is 2.0
Example 2:
nums1 = [1, 2]
nums2 = [3, 4]

The median is (2 + 3)/2 = 2.5
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a = {1, 3};
		int[] b = {2};
		
		System.out.println(findMedianSortedArrays(a,b));
	}

	//1 思路首先把两个数组合并为一个，然后去中间数
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
    	int n = nums1.length + nums2.length;
    	int[] temp = new int[n];
     	int m1 = 0;
    	int m2 = 0; 
    	int i;
    	for ( i = 0;m1 < nums1.length && m2 < nums2.length && i < n;i++) {
    		if (nums1[m1] < nums2[m2]) {
    			temp[i] = nums1[m1];
    			m1++;
    		} else {
    			temp[i] = nums2[m2];
    			m2++;
    		}
    	}
    	while (m1 < nums1.length && i < n) {
    		temp[i] = nums1[m1];
    		m1++;
    		i++;
    	}
    	while (m2 < nums2.length && i < n) {
    		temp[i] = nums2[m2];
    		m2++;
    		i++;
    	}
    	return (n % 2 == 0) ? (double)(temp[n / 2] + temp[n / 2 - 1]) / 2 : temp[n / 2];
    }
}
