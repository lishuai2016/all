/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lishuai
 * @data 2016-11-30 上午10:33:49
 */

public class ContainsDuplicateII {

	/**
	 * @author lishuai
	 * @data 2016-11-30 上午10:33:49
Given an array of integers and an integer k, 
find out whether there are two distinct indices i and j in the array such that nums[i] = nums[j] 
and the difference between i and j is at most k.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a={0,1,2,3,1};
		containsNearbyDuplicate1(a,3);

	}

//1 思路：通过map来保存数组中的元素，要是已经存在即有重复，判断是否满足两者元素下标之差小于等于k,要是满足返回true，否则false
   public static boolean containsNearbyDuplicate(int[] nums, int k) {
        if(nums.length<2){
			return false;
		}
        Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        for(int i=0;i<nums.length;i++){
        	if(map.containsKey(nums[i])&&(i-map.get(nums[i])<=k)){
        		return true;
        	}
        	map.put(nums[i], i);
        }
        return false;
        
    }
   //2 思路：通过hashset实现，由于查询两个元素下标不超高k，因此当i大于k时还没结束，可以移除前面的元素，这样节省空间
   public static boolean containsNearbyDuplicate1(int[] nums, int k) {
	   
	   Set<Integer> set = new HashSet<Integer>();
       for(int i = 0; i < nums.length; i++){
           if(i > k) set.remove(nums[i-k-1]);
           if(!set.add(nums[i])) return true;
       }
       return false;
	   
	   
   }
}
