/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-11-29 下午3:52:21
 */

public class ProductofArrayExceptSelf {

	/**
	 * @author lishuai
	 * @data 2016-11-29 下午3:52:21
238. Product of Array Except Self


Given an array of n integers where n > 1, nums, 
return an array output such that output[i] is equal to the product of all the elements of nums except nums[i].

Solve it without division and in O(n).

For example, given [1,2,3,4], return [24,12,8,6].

Follow up:
Could you solve it with constant space complexity? 
(Note: The output array does not count as extra space for the purpose of space complexity analysis.)



	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a={1,2,3,4};
		productExceptSelf(a);
	}
	/**
	 
Given numbers [2, 3, 4, 5], regarding the third number 4, 
the product of array except 4 is 2*3*5 which consists of two parts: 
left 2*3 and right 5. The product is left*right. We can get lefts and rights:

Numbers:     2    3    4     5
Lefts:            2  2*3 2*3*4
Rights:  3*4*5  4*5    5      
Let’s fill the empty with 1:

Numbers:     2    3    4     5
Lefts:       1    2  2*3 2*3*4
Rights:  3*4*5  4*5    5     1
We can calculate lefts and rights in 2 loops. The time complexity is O(n).

We store lefts in result array. 
If we allocate a new array for rights. The space complexity is O(n). 
To make it O(1), we just need to store it in a variable which is right  
	 */
	
	
//0 思路是把乘机分成左右两个部分	
	public static int[] productExceptSelf(int[] nums) {
		int n=nums.length;
		int[] res=new int[n];
		int left=1;
		for(int i=0;i<n;i++){
			if(i>0) left*=nums[i-1];
			res[i]=left;
		}
		int right=1;
		for(int j=n-1;j>=0;n--){
			if(j<n-1) right*=nums[j+1];
			res[j]*=right;
		}
		
		return res;
		
	}
	//1 首先考虑有几个0的问题，然后在数组没有0的情况下，先求所有元素的乘集，然后遍历时除掉本身即可
    public static int[] productExceptSelf1(int[] nums) {
    	//考虑有几个零的问题 
    	int[] back=new int[nums.length];
    	int indexZero=0;
    	int zeroCount=0;
    	for(int i=0;i<nums.length;i++){
    		if(nums[i]==0){
    			indexZero=i;
    			zeroCount++;
    		}
    	}
    	if(zeroCount>1){
    		//所有都为零
    		for(int i=0;i<nums.length;i++){
    			back[i]=0;
    		}
    	}else if(zeroCount==1){
    		//只有一个不为零
    		int sum=1;
    		for(int i=0;i<nums.length;i++){
    			if(i!=indexZero){
    				sum*=nums[i];
    			}
    		}
    		for(int i=0;i<nums.length;i++){
    			if(i!=indexZero){
    				back[i]=0;
    			}else{
    				back[i]=sum;
    			}
    		}
    	}else{
    		//没有零元素
    		int sum=1;
    		for(int i=0;i<nums.length;i++){    			
    				sum*=nums[i];
    		}
    		for(int i=0;i<nums.length;i++){  
    			back[i]=sum/nums[i];
    		}
    	}
    	
    	return back;
    }
    //2 超时不满足时间复杂度
    public static int[] productExceptSelf2(int[] nums) {
    	int[] back=new int[nums.length];
    	for(int i=0;i<nums.length;i++){
    		int temp=1;
    		for(int j=0;j<nums.length;j++){
    			if(i!=j){
    				temp*=nums[j];
    			}
    		}
    		back[i]=temp;
    	}	
    	return back;
    }
    //3 和0的思路一样，不一样的写法
    public static int[] productExceptSelf3(int[] nums) {
		int n = nums.length;
	    int[] res = new int[n];
	    res[0] = 1;
	    for (int i = 1; i < n; i++) {
	        res[i] = res[i - 1] * nums[i - 1];
	    }
	    int right = 1;
	    for (int i = n - 1; i >= 0; i--) {
	        res[i] *= right;
	        right *= nums[i];
	    }
	    return res;
		
	}

}
