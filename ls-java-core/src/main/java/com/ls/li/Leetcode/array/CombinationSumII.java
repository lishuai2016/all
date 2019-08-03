/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-2 下午5:08:14
 */

public class CombinationSumII {

	/**
	 * @author lishuai
	 * @data 2016-12-2 下午5:08:14
Given a collection of candidate numbers (C) and a target number (T), 
find all unique combinations in C where the candidate numbers sums to T.

Each number in C may only be used once in the combination.

Note:
All numbers (including target) will be positive integers.
The solution set must not contain duplicate combinations.
For example, given candidate set [10, 1, 2, 7, 6, 1, 5] and target 8, 
A solution set is: 
[
  [1, 7],
  [1, 2, 5],
  [2, 6],
  [1, 1, 6]
]
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a= {10, 1, 2, 7, 6, 1, 5};
		System.out.println(combinationSum2(a,8));
	}
	//1 回溯法Time Limit Exceeded（当数组较大时，因为压站出站耗时）
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
    	Arrays.sort(candidates);
    	int len = 0;
    	for (int i = 0;i < candidates.length;i++) {
    		if (candidates[i] <= target) {       		
        		len++;
        	}
    	}
    	List<List<Integer>> res = new ArrayList<List<Integer>>();
    	back(res, new ArrayList<Integer>(), candidates, target, len, 0);
    	return res;
    }

    public static void back(List<List<Integer>> res,List<Integer> list,int[] nums,int target,int len,int start) {
    	if (target == 0) {
    		List<Integer> t = new ArrayList<Integer>(list);
    		if (!res.contains(t)) res.add(t);
    	}
    	for (int i = start;i < len;i++) {
    		list.add(nums[i]);
    		back(res, list, nums, target - nums[i], len, i + 1);
    		list.remove(list.size() - 1);
    	}
    }
}
