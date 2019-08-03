/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-11-29 下午1:39:14
 */

public class MoveZeroes {

	/**
	 * @author lishuai
	 * @data 2016-11-29 下午1:39:14
	 * @param args
题：283
Given an array nums, write a function to move all 0's to the end of it 
while maintaining the relative order of the non-zero elements.

For example, given nums = [0, 1, 0, 3, 12], after calling your function, nums should be [1, 3, 12, 0, 0].

Note:
You must do this in-place without making a copy of the array.
Minimize the total number of operations.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[]  nums = {0, 1, 0, 3, 12};
		moveZeroes(nums);
	}
	//1 思路 把不为零的元素保存在一个list中，然后回写原数组
    public static void moveZeroes(int[] nums) {
       List<Integer> list=new ArrayList<Integer>();
       for(int i=0;i<nums.length;i++){
    	   if(nums[i]!=0){
    		   list.add(nums[i]);
    	   }
       }
       for(int j=0;j<nums.length;j++){
    	   if(j<list.size()){
    		   nums[j]=list.get(j);
    	   }else{
    		   nums[j]=0;
    	   }
       }
 
    }
    
    //2 思路 把不为零的元素前移并且统计有多少个不为零的元素，其余的赋值为0
    public static void moveZeroes1(int[] nums) {
          int j=0;
		  for(int i=0;i<nums.length;i++){
			  if(nums[i]!=0){
				  nums[j]=nums[i];
				  j++;
			  }
		  }
		  for(int k=j;k<nums.length;k++){
			  nums[k]=0;
		  }

    }
}
