/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-11-30 上午10:34:26
 */

public class ContainsDuplicate {

	/**
	 * @author lishuai
	 * @data 2016-11-30 上午10:34:26
Given an array of integers, find if the array contains any duplicates. 
Your function should return true if any value appears at least twice in the array, 
and it should return false if every element is distinct.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(2>>1);
		
		
		
	}
	//1思路   用map统计，没有重复时，加入map已经存在则退出，返回结果（用list也行） 时间复杂度为n,空间复杂度n
    public static  boolean containsDuplicate(int[] nums) {
        Map<Integer, Integer> m1=new HashMap<Integer, Integer>();
		 for(int i=0;i<nums.length;i++){
			 if( m1.containsKey(nums[i])){
				 return true;
			 }else{
				 m1.put(nums[i], 1);
			 }				
		 }
   	return false;
   	
   	
   	

	}

    

    /**
 This solution is very interesting. I want to share my understanding about it.

For this problem, one of the simplest solutions is to create a big lookup table k. 
The value of each element in the table is to determine whether the index value has appeared before. 
The solution with Bit Manipulation is similar to that. 
Instead of using 0 or 1 in each element to show whether the number exit, 
it uses 8 bits of each element to indicate the existence of 8 numbers.

For a number A, assume k1 = A/8, k2 = A%8+1. 
It uses k1 as the index for the lookup table to get a element.
 And then the k2-th bit of the element is checked to find out whether A appeared before. 
 If the bit is 1, return true. Or if the bit is 0, set the bit to 1 (add A to the lookup table).

For example, now we get a number 0x800 (k1 = 0x100, k2 = 1). 
Then we get the 0x100-th byte in the table ( table[0x100] = 0b10000111). 
The 1st bit is 1. So we know 0x800 appeared before.
Moreover, 0b10000111 means 0x800, 0x801, 0x802 and 0x807 has appeared before.

I hope my explaination is helpful.
     */
    //2  时间复杂度为n(暂时还没理解)
    public static  boolean containsDuplicate1(int[] nums) {
    	 byte[] mark = new byte[150000];
    	    for (int i : nums) {
    	        int j = i/8;
    	        int k = i%8;
    	        int check = 1<<k;
    	        if ((mark[j] & check) != 0) {
    	            return true;
    	        }
    	        mark[j]|=check;
    	    }
    	    return false;
    }
    //3  思路 ：先排序后比较相邻的元素   时间复杂度n(logn)
    public static  boolean containsDuplicate2(int[] nums) {
    	if(nums.length==0 || nums.length==1){
            return false;
        }
        Arrays.sort(nums);
        
        for(int i=0;i<nums.length-1;i++){
            if(nums[i]==nums[i+1]){
                return true;
            }
        }
        return false;
    }
    //4 使用set 不可重复特性:如果元素已经存在，再添加时返回false Time complexity: O(N), memory: O(N)
    public static  boolean containsDuplicate3(int[] nums) {
    	if(nums.length==0) return false;
    	Set<Integer> set1 = new HashSet <>();   	
    	for(int i = 0; i< nums.length;i++)
    	if(!set1.add(nums[i])) return true;
    	return false;
    	
    }
    
    //5 Time complexity: O(N^2), memory: O(1)
    public static  boolean containsDuplicate4(int[] nums) {
    	 for(int i = 0; i < nums.length; i++) {
             for(int j = i + 1; j < nums.length; j++) {
                 if(nums[i] == nums[j]) {
                     return true;
                 }
             }
         }
         return false;
    }
}
