/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-31 上午9:55:23
 */

public class MaxNumber {

	/**
	 * @author lishuai
	 * @data 2017-3-31 上午9:55:23
给出两个长度分别是m和n的数组来表示两个大整数，数组的每个元素都是数字0-9。
从这两个数组当中选出k个数字来创建一个最大数，其中k满足k <= m + n。
选出来的数字在创建的最大数里面的位置必须和在原数组内的相对位置一致。
返回k个数的数组。你应该尽可能的去优化算法的时间复杂度和空间复杂度。

您在真实的面试中是否遇到过这个题？ Yes
样例
给出 nums1 = [3, 4, 6, 5], nums2 = [9, 1, 2, 5, 8, 3], k = 5
返回 [9, 8, 6, 5, 3]

给出 nums1 = [6, 7], nums2 = [6, 0, 4], k = 5
返回 [6, 7, 6, 0, 4]

给出 nums1 = [3, 9], nums2 = [8, 9], k = 3
返回 [9, 8, 9]
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
//	public int[] maxNumber(int[] nums1, int[] nums2, int k) {
//        // Write your code here
//		return null;
//    }
	
	public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        // Write your code here
        if (k == 0)
            return new int[0];

        int m = nums1.length, n = nums2.length;
        if (m + n < k) return null;
        if (m + n == k) {
            int[] results = merge(nums1, nums2, k);
            return results;
        } else {
            int max = m >= k ? k : m;
            int min = n >= k ? 0 : k - n;

            int[] results = new int[k];
            for(int i=0; i < k; ++i)
                results[i] = -0x7ffffff;
            for(int i = min; i <= max; ++i) {
                int[] temp = merge(getMax(nums1, i), getMax(nums2, k - i), k);
                results = isGreater(results, 0, temp, 0) ? results : temp;
            }
            return results;
        }
    }

    private int[] merge(int[] nums1, int[] nums2, int k) {
        int[] results = new int[k];
        if (k == 0) return results;
        int i = 0, j = 0;
        for(int l = 0; l < k; ++l) {
            results[l] = isGreater(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
        }
        return results;
    }

    private boolean isGreater(int[] nums1, int i, int[] nums2, int j) {
        for(; i < nums1.length && j < nums2.length; ++i, ++j) {
            if (nums1[i] > nums2[j])
                return true;
            if (nums1[i] < nums2[j])
                return false;
        }
        return i != nums1.length;
    }

    private int[] getMax(int[] nums, int k) {
        if (k == 0)
            return new int[0];
        int[] results = new int[k];
        int i = 0;
        for(int j = 0; j < nums.length; ++j) {
            while(nums.length - j + i > k && i > 0 && results[i-1] < nums[j])
                i--;
            if (i < k)
                results[i++] = nums[j];
        }
        return results;
    }
}
