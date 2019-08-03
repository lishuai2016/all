/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-1 上午8:31:37
 */

public class TwoSumIIInputarrayissorted {

	/**
	 * @author lishuai
	 * @data 2016-12-1 上午8:31:37
Given an array of integers that is already sorted in ascending order, 
find two numbers such that they add up to a specific target number.

The function twoSum should return indices of the two numbers such that they add up to the target, 
where index1 must be less than index2. Please note that your returned answers (both index1 and index2) are not zero-based.

You may assume that each input would have exactly one solution.

Input: numbers={2, 7, 11, 15}, target=9
Output: index1=1, index2=2
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a={ 2,3,4};
		
		System.out.println(twoSum3(a,6));
	}
	//1 滑动窗口 连个指针   时间复杂度n
	 public static int[] twoSum(int[] numbers, int target) {		 
		int[] indice = new int[2];
	    if (numbers == null || numbers.length < 2) return indice;
	    int left = 0, right = numbers.length - 1;
	    while (left < right) {
	        int v = numbers[left] + numbers[right];
	        if (v == target) {
	            indice[0] = left + 1;
	            indice[1] = right + 1;
	            break;
	        } else if (v > target) {
	            right --;
	        } else {
	            left ++;
	        }
	    }
	    return indice;
		 
	 }
	 //2 二叉搜索  binary search   时间复杂度n*logN
	 public static int[] twoSum3(int[] numbers, int target) {
		 int[] res=new int[2];
		    for(int i=0; i<numbers.length-1; i++) {
		        int start=i+1, end=numbers.length-1, gap=target-numbers[i];
		        while(start <= end) {
		            int m = start+(end-start)/2;
		            if(numbers[m] == gap) {		            	
		            	res[0]=i+1;
		        		res[1]=m+1;	
		        		return res;
		            }else if(numbers[m] > gap) end=m-1;
		            else start=m+1;
		        }
		    }
		    return res;
		 
	 }
	
	//3 Time Limit Exceeded
	 public static int[] twoSum2(int[] numbers, int target) {
    	int[] res=new int[2];
    	//找到第一个大于目标值得位置
    	int index=numbers.length;
    	for(int i=0;i<numbers.length;i++){
    		if(numbers[i]>target){
    			index=i;
    		}    		
    	}
    	
    	for(int i=0;i<index-1;i++){
    		for(int j=i+1;j<index;j++){
    			if(numbers[i]+numbers[j]==target){
    				res[0]=i+1;
    				res[1]=j+1;
    			}
    			
    		}
    	}
    	return res;
	 }
	
	//4 回溯法 Time Limit Exceeded
    public static int[] twoSum1(int[] numbers, int target) {
    	int index=numbers.length;
    	for(int i=0;i<numbers.length;i++){
    		if(numbers[i]>target){
    			index=i;
    		}    		
    	}
    	
    	int[] res=new int[2];
    	if(target==0) {
    		res[0]=1;
    		res[1]=2;
    		return res;
    	}
    	backtracking(res,new ArrayList<Integer>(), numbers, target, 0,2,index);   	
    	return res;
    }
    
    public static void backtracking(int[] res,List<Integer> list,int[] numbers,int target ,int start,int k,int index){
    	if(k==0&&target==0){
    		res[0]=list.get(0);
    		res[1]=list.get(1);
    		return ;
    	}
    	for(int i=start;i<index&&target>0&&k>0;i++){
    		list.add(i+1);
    		backtracking(res,list, numbers, target-numbers[i], i+1, k-1,index);
    		list.remove(list.size()-1);
    	}
    }
}
