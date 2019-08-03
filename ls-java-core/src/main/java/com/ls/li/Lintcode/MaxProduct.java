/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 上午10:11:01
 */

public class MaxProduct {

	/**
	 * @author lishuai
	 * @data 2017-3-30 上午10:11:01
找出一个序列中乘积最大的连续子序列（至少包含一个数）
比如, 序列 [2,3,-2,4] 中乘积最大的子序列为 [2,3] ，其乘积为6。
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
//	public int maxProduct(int[] nums) {
//		if (nums == null || nums.length == 0) {
//			return 0;
//		}
//		int max = nums[0];
//        int localMax = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//        	
//        }
//		return 0;
//    }
	
	 public int maxProduct(int[] nums) {
	        int[] max = new int[nums.length];
	        int[] min = new int[nums.length];
	        
	        min[0] = max[0] = nums[0];
	        int result = nums[0];
	        for (int i = 1; i < nums.length; i++) {
	            min[i] = max[i] = nums[i];
	            if (nums[i] > 0) {
	                max[i] = Math.max(max[i], max[i - 1] * nums[i]);
	                min[i] = Math.min(min[i], min[i - 1] * nums[i]);
	            } else if (nums[i] < 0) {
	                max[i] = Math.max(max[i], min[i - 1] * nums[i]);
	                min[i] = Math.min(min[i], max[i - 1] * nums[i]);
	            }
	            
	            result = Math.max(result, max[i]);
	        }
	        
	        return result;
	    }
}
