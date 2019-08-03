/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 上午9:09:47
 */

public class LongestIncreasingSubsequence {

	/**
	 * @author lishuai
	 * @data 2017-3-30 上午9:09:47
最长上升子序列的定义：

最长上升子序列问题是在一个无序的给定序列中找到一个尽可能长的由低到高排列的子序列，这种子序列不一定是连续的或者唯一的。

给出 [5,4,1,2,3]，LIS 是 [1,2,3]，返回 3
给出 [4,2,4,5,3,7]，LIS 是 [2,4,5,7]，返回 4
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {5,4,1,2,3};
		longestIncreasingSubsequence1(a);
		
	}
//	public static int t(int[] a) {
//		int max = 1;
//		
//		return max;
//	}
	
	 public static int longestIncreasingSubsequence1(int[] nums) {
	        int []f = new int[nums.length];
	        int max = 0;
	        for (int i = 0; i < nums.length; i++) {
	            f[i] = 1;
	            for (int j = 0; j < i; j++) {
	                if (nums[j] < nums[i]) {
	                    f[i] = f[i] > f[j] + 1 ? f[i] : f[j] + 1;
	                }
	            }
	            if (f[i] > max) {
	                max = f[i];
	            }
	        }
	        for (int i : f) {
	        	System.out.print(i + "  ");
	        }
	        return max;
	 }
	 
	 
	 public int longestIncreasingSubsequence(int[] nums) {
	        int[] minLast = new int[nums.length + 1];
	        minLast[0] = Integer.MIN_VALUE;
	        for (int i = 1; i <= nums.length; i++) {
	            minLast[i] = Integer.MAX_VALUE;
	        }
	        
	        for (int i = 0; i < nums.length; i++) {
	            // find the first number in minLast > nums[i]
	            int index = binarySearch(minLast, nums[i]);
	            minLast[index] = nums[i];
	        }
	        
	        for (int i = nums.length; i >= 1; i--) {
	            if (minLast[i] != Integer.MAX_VALUE) {
	                return i;
	            }
	        }
	        
	        return 0;
	    }
	    
	    // find the first number > num
	    private int binarySearch(int[] minLast, int num) {
	        int start = 0, end = minLast.length - 1;
	        while (start + 1 < end) {
	            int mid = (end - start) / 2 + start;
	            if (minLast[mid] < num) {
	                start = mid;
	            } else {
	                end = mid;
	            }
	        }
	        
	        if (minLast[start] > num) {
	            return start;
	        }
	        return end;
	    }
}
