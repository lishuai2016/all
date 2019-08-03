/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-11-29 下午1:57:55
 */

public class MissingNumber {

	/**
	 * @author lishuai
	 * @data 2016-11-29 下午1:57:55
268. Missing Number
Given an array containing n distinct numbers taken from 0, 1, 2, ..., n, 
find the one that is missing from the array.

For example,
Given nums = [0, 1, 3] return 2.

Note:
Your algorithm should run in linear runtime complexity.
 Could you implement it using only constant extra space complexity?
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a={3, 1, 0};
		System.out.println(missingNumber(a));
		
		System.out.println();
		

	}
	
	
	//1 思路，用一个map保存出现过的元素 用map保存元素
    public static int missingNumber(int[] nums) {
       Map<Integer, Integer> map=new HashMap<Integer, Integer>();
       for(int i=0;i<nums.length;i++){
    	   if(!map.containsKey(nums[i])){
    		   map.put(nums[i], 1);
    	   }
       }
	    for(int j=0;j<nums.length;j++){
	    	if(!map.containsKey(j)){
	    		return j;
	    	}
	    }	
    	return nums.length;
    }
    //2 思路和1一样 用list保存元素
    public static int missingNumber1(int[] nums) {
        List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<nums.length;i++){
     	   if(!list.contains(nums[i])){
     		   list.add(nums[i]);
     	   }
        }
 	    for(int j=0;j<nums.length;j++){
 	    	if(!list.contains(j)){
 	    		return j;
 	    	}
 	    }	
     	return nums.length;
     }
    
    //3 异或运算 （按位操作）   a^a^b=b       0^x=x 零异或任何数为那个数本身  1ms
    public static int missingNumber2(int[] nums) {
    	int xor = 0, i = 0;
    	for (i = 0; i < nums.length; i++) {
    		xor = xor ^ i ^ nums[i];
    	}

    	return xor ^ i;
    }
    //4  since the n numbers are from [0, n], we can just add all the numbers from [0, n] together and minus the sum of the n-1 numbers in array.
    //数组较大时有可能溢出   1ms
    public static int missingNumber3(int[] nums) {    	
    	int sum=nums.length;
    	for(int i=0;i<nums.length;i++ ){
    		sum=sum+i-nums[i];
    	}
    	return sum;
    }
    //5 BitSet的使用      4ms
    /**
    BitSet 类创建一个特殊类型的数组保存位值。该BitSet中数组的大小可以根据需要增加。这使得它类似于比特的向量。
    BitSet(int size) 所有位初始化为零。
    void set(int index)设置由index指定的位。
    int nextClearBit(int startIndex)返回下个清零位的索引，（即，下一个零位），从由startIndex指定的索引开始
     */
    public static int missingNumber4(int[] nums) {
    	 BitSet bitSet = new BitSet(nums.length);
         for (int i = 0; i < nums.length; i++) {
            bitSet.set(nums[i]);
         }
        
         return bitSet.nextClearBit(0);
    }
}
