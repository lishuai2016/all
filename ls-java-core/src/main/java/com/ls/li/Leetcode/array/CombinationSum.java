/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-2 下午5:06:15
 */

public class CombinationSum {

	/**
	 * @author lishuai
	 * @data 2016-12-2 下午5:06:15
Given a set of candidate numbers (C) and a target number (T), 
find all unique combinations in C where the candidate numbers sums to T.

The same repeated number may be chosen from C unlimited number of times.

Note:
All numbers (including target) will be positive integers.
The solution set must not contain duplicate combinations.
For example, given candidate set [2, 3, 6, 7] and target 7, 
A solution set is: 
[
  [7],
  [2, 2, 3]
]
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
    	Arrays.sort(candidates);
    	List<List<Integer>> res = new ArrayList<List<Integer>>();
    	Set<Integer> set = new HashSet<Integer>();
    	int len = 0;
        for (int i = 0;i < candidates.length;i++) {
        	if (candidates[i] <= target) {
        		set.add(candidates[i]);
        		len++;
        	}
        }
        for (int i = 0;i < candidates.length;i++) {
        	List<Integer> list = new ArrayList<Integer>();        	
        	int num1 = candidates[i];
        	if (target % num1 == 0) {
        		res.add(Arrays.asList(num1));
        	}
        	list.add(num1);
        	int temp = target - num1;
        	if (temp == 0) {
        		res.add(Arrays.asList(num1));
        		continue;
        	} 
        	while (temp != 0) {
        		
        	}
        	
        }
        
        
    	return null;
    }
}
