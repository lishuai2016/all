/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lishuai
 * @data 2017-1-4 上午9:23:42
 */

public class SingleNumberIII {

	/**
	 * @author lishuai
	 * @data 2017-1-4 上午9:23:42
Given an array of numbers nums, in which exactly two elements appear only once 
and all the other elements appear exactly twice. Find the two elements that appear only once.

For example:

Given nums = [1, 2, 1, 3, 2, 5], return [3, 5].

Note:
The order of the result is not important. So in the above example, [5, 3] is also correct.
Your algorithm should run in linear runtime complexity. Could you implement it using only constant space complexity?


	 */

	public static void main(String[] args) {
//		int[] a = {1, 2, 1, 5, 2, 10};
//		singleNumber2(a);
		System.out.println(8&~1);
	}
	
	//3九章  和2类似
	public static int[] singleNumber3(int[] A) {
		int xor = 0;
        for (int i = 0; i < A.length; i++) {
            xor ^= A[i];
        }
        
        int lastBit = xor - (xor & (xor - 1));
        int group0 = 0, group1 = 0;
        for (int i = 0; i < A.length; i++) {
            if ((lastBit & A[i]) == 0) {
                group0 ^= A[i];
            } else {
                group1 ^= A[i];
            }
        }
        return new int[]{group0,group1};
    }
	/**
Once again, we need to use XOR to solve this problem. But this time, we need to do it in two passes:

In the first pass, we XOR all elements in the array, and get the XOR of the two numbers we need to find. 
Note that since the two numbers are distinct, 
so there must be a set bit (that is, the bit with value '1') in the XOR result. Find
out an arbitrary set bit (for example, the rightmost set bit).

In the second pass, we divide all numbers into two groups, one with the aforementioned bit set, 
another with the aforementinoed bit unset. 
Two different numbers we need to find must fall into thte two distrinct groups. 
XOR numbers in each group, we can find a number in either group.

Complexity:

Time: O (n)

Space: O (1)

A Corner Case:

When diff == numeric_limits<int>::min(), -diff is also numeric_limits<int>::min().
Therefore, the value of diff after executing diff &= -diff is still numeric_limits<int>::min(). 
The answer is still correct.


0101
1010
	 */
	
	
	
	//2 找出异或操作结果中的一位非零为，再次遍历异或操作一遍数组，将其分为两部分  2ms
	public static int[] singleNumber2(int[] nums) {
        // Pass 1 : 
        // Get the XOR of the two numbers we need to find
        int diff = 0;
        for (int num : nums) {
            diff ^= num;
        }
        // Get its last set bit
        diff &= -diff;
        
        // Pass 2 :
        int[] rets = {0, 0}; // this array stores the two numbers we will return
        for (int num : nums)
        {
            if ((num & diff) == 0) // the bit is not set
            {
                rets[0] ^= num;
            }
            else // the bit is set
            {
                rets[1] ^= num;
            }
        }
        return rets;
    }
	
	
	//1 借助map来统计  19ms
    public static int[] singleNumber1(int[] nums) {
        List<Integer> list = new ArrayList<>();
        Map<Integer,Integer> map = new HashMap<>();
        for (int i : nums) {
        	map.put(i, map.get(i) != null ? map.get(i) + 1 : 1);
        }
        for (Integer i : map.keySet()) {
        	if (map.get(i) == 1) {
        		list.add(i);
        	}
        }
        return new int[]{list.get(0),list.get(1)};
    }
}
