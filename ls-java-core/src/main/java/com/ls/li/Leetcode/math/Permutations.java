/**
 * 
 */
package com.ls.li.Leetcode.math;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2017-1-10 上午11:31:19
 */

public class Permutations {

	/**
	 * @author lishuai
	 * @data 2017-1-10 上午11:31:19
Given a collection of numbers, return all possible permutations. 
For example, [1,2,3] have the following permutations: 
[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], and [3,2,1].
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public List<List<Integer>> permute(int[] nums) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		
		
		
		return res;
	}
	//2九章
	public List<List<Integer>> permute2(int[] nums) {
        ArrayList<List<Integer>> rst = new ArrayList<List<Integer>>();
        if (nums == null) {
            return rst; 
        }
        
        if (nums.length == 0) {
           rst.add(new ArrayList<Integer>());
           return rst;
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        helper(rst, list, nums);
        return rst;
   }
   
   public void helper(ArrayList<List<Integer>> rst, ArrayList<Integer> list, int[] nums){
       if(list.size() == nums.length) {
           rst.add(new ArrayList<Integer>(list));
           return;
       }
       
       for(int i = 0; i < nums.length; i++){
           if(list.contains(nums[i])){
               continue;
           }
           list.add(nums[i]);
           helper(rst, list, nums);
           list.remove(list.size() - 1);
       }
       
   }


//Non-Recursion
   /**
    * @param nums: A list of integers.
    * @return: A list of permutations.
    */
   public List<List<Integer>> permute1(int[] nums) {
       ArrayList<List<Integer>> permutations
            = new ArrayList<List<Integer>>();
       if (nums == null) {
           
           return permutations;
       }

       if (nums.length == 0) {
           permutations.add(new ArrayList<Integer>());
           return permutations;
       }
       
       int n = nums.length;
       ArrayList<Integer> stack = new ArrayList<Integer>();
       
       stack.add(-1);
       while (stack.size() != 0) {
           Integer last = stack.get(stack.size() - 1);
           stack.remove(stack.size() - 1);
           
           // increase the last number
           int next = -1;
           for (int i = last + 1; i < n; i++) {
               if (!stack.contains(i)) {
                   next = i;
                   break;
               }
           }
           if (next == -1) {
               continue;
           }
           
           // generate the next permutation
           stack.add(next);
           for (int i = 0; i < n; i++) {
               if (!stack.contains(i)) {
                   stack.add(i);
               }
           }
           
           // copy to permutations set
           ArrayList<Integer> permutation = new ArrayList<Integer>();
           for (int i = 0; i < n; i++) {
               permutation.add(nums[stack.get(i)]);
           }
           permutations.add(permutation);
       }
       
       return permutations;
   }
   
}
