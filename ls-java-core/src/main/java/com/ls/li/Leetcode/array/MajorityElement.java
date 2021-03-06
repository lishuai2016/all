/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lishuai
 * @data 2016-11-30 下午5:41:40
 */

public class MajorityElement {

	/**
	 * @author lishuai
	 * @data 2016-11-30 下午5:41:40
Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊ n/2 ⌋ times.

You may assume that the array is non-empty and the majority element always exist in the array.
	 */

	public static void main(String[] args) {
		int[] a = {1,1,1,1,2,3};
		majorityElement3(a);

	}
	//1 思路：先对数组排序，返回排序好的数字中间位置对应的值即可
	public int majorityElement(int[] nums) {
		Arrays.sort(nums);
		return nums[nums.length/2];
	}
	
	//九章
	public int majorityNumber11(ArrayList<Integer> nums) {
        int count = 0, candidate = -1;
        for (int i = 0; i < nums.size(); i++) {
            if (count == 0) {
                candidate = nums.get(i);
                count = 1;
            } else if (candidate == nums.get(i)) {
                count++;
            } else {
                count--;
            }
        }
        return candidate;
    }
	//2思路 摩尔投票法算法    选取大多数
	public int majorityElement1(int[] nums) {
		int count=0;
		int majority=nums[0];		
		for(int i=1;i<nums.length;i++){
			if(nums[i]==majority){
				count++;
			}else if(count==0){
				majority=nums[i];
			}else{
				count--;
			}
		}		
		return majority;
	}	
	//3 使用map统计出现的次数
	public int majorityElement2(int[] nums) {
   		if(nums.length==1){
   			return nums[0];
		}else{
			 int back=0;
			 Map<Integer, Integer> m1=new HashMap<Integer, Integer>();
			 for(int i=0;i<nums.length;i++){
				 if(m1.containsKey(nums[i])){
					 Integer v=m1.get(nums[i]);
					 m1.put(nums[i], v+1);
					 
				 }else{
					 m1.put(nums[i], 1);
				 }	
				 
				 if(m1.get(nums[i])>nums.length/2){
					 back=nums[i];
					 break;
				 }
				
			 }
			
			return back;
		}
	}
	
	//4 Bit manipulation （原理？？？）把数组所有元素的二进制表示统计累计32位的每一位；然后遍历位数组，要是对应的为1的各个大于数组的一半这说明是要求数字的一部分，然后拼接
	public static int majorityElement3(int[] nums) {
	    int[] bit = new int[32];
	    for (int num: nums)
	        for (int i=0; i<32; i++) 
	            if ((num>>(31-i) & 1) == 1)
	                bit[i]++;
	    int ret=0;
	    for (int i=0; i<32; i++) {
	        bit[i]=bit[i]>nums.length/2?1:0;
	        ret += bit[i]*(1<<(31-i));
	    }
	    return ret;
	}
	//5 统计32中的每一位，在数组中是1对应的个数大还是0对应的个数大，然后拼接
	public static int majorityElement5(int[] num) {
	    int ret = 0;
	    for (int i = 0; i < 32; i++) {
	        int ones = 0, zeros = 0;
	        for (int j = 0; j < num.length; j++) {
	            if ((num[j] & (1 << i)) != 0) {
	                ++ones;
	            } else {
	            	 ++zeros;
	            }	               
	        }
	        if (ones > zeros)
	            ret |= (1 << i);
	    }
	    
	    return ret;
	}
}
