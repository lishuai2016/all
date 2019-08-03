/**
 * 
 */
package com.ls.li.Leetcode.stack;

import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-15 下午1:48:36
 */

public class TrappingRainWater {

	/**
	 * @author lishuai
	 * @data 2016-12-15 下午1:48:36
Given n non-negative integers representing an elevation map where the width of each bar is 1, 
compute how much water it is able to trap after raining.

For example, 
Given [0,1,0,2,1,0,1,3,2,1,2,1], return 6.
	 */

	public static void main(String[] args) {
		int[] t = {0,1,0,2,1,0,1,3,2,1,2,1};
		System.out.println(trapRainWater4(t));

	}
	//4九章 二指针
	public static int trapRainWater4(int[] heights) {
        // write your code here
        int left = 0, right = heights.length - 1; 
        int res = 0;
        if(left >= right)
            return res;
        int leftheight = heights[left];
        int rightheight = heights[right];
        while(left < right) {
            if(leftheight < rightheight) {
                left ++;
                if(leftheight > heights[left]) {
                    res += (leftheight - heights[left]);
                } else {
                    leftheight = heights[left];
                }
            } else {
                right --;
                if(rightheight > heights[right]) {
                    res += (rightheight - heights[right]);
                } else {
                    rightheight = heights[right];
                }
            }
        }
        return res;
    }
	//3九章 [0,1,0,2,1,0,1,3,2,1,2,1], return 6.
	public static int trapRainWater3(int[] heights) {
        if (heights.length == 0) {
            return 0;
        }
        
        int[] maxHeights = new int[heights.length + 1];
        maxHeights[0] = 0;
        for (int i = 0; i < heights.length; i++) {
            maxHeights[i + 1] = Math.max(maxHeights[i], heights[i]);
        }
        
        int max = 0, area = 0;
        for (int i = heights.length - 1; i >= 0; i--) {
            area += Math.min(max, maxHeights[i]) > heights[i]
                    ? Math.min(max, maxHeights[i]) - heights[i]
                    : 0;
            max = Math.max(max, heights[i]);
        }
        
        return area;
    }
	
	/**
Here is my idea: instead of calculating area by height*width, 
we can think it in a cumulative way. In other words, sum water amount of each bin(width=1).
Search from left to right and maintain a max height of left and right separately, 
which is like a one-side wall of partial container. 
Fix the higher one and flow water from the lower part. 
For example, if current height of left is lower, we fill water in the left bin. 
Until left meets right, we filled the whole container.
	 */
	//2 二指针
	public static int trap(int A[], int n) {
	        int left = 0; int right = n-1;
	        int res = 0;
	        int maxleft = 0, maxright = 0;
	        while(left <= right){
	            if(A[left] <= A[right]) {
	                if(A[left] >= maxleft) maxleft = A[left];
	                else res += maxleft - A[left];
	                left++;
	            } else{
	                if(A[right] >= maxright) maxright = A[right];
	                else res += maxright - A[right];
	                right--;
	            }
	        }
	        return res;
	    }
	//1 DP有问题
    public static int trap(int[] height) {
    	if (height == null || height.length == 0) return 0;
        Stack<Integer> stack = new Stack<Integer>();
    	int sum = 0;
    	int curMax = 0;
    	int index = 0;
    	while (index < height.length) {
    		if (height[index] != 0) {
    			curMax = height[index];
        		break;
    		}
    		index++;
    	}
    	stack.push(height[index]);
    	for (int i = index + 1;i < height.length;i++) {	
    		if (height[i] >= curMax) {
    			int high = Math.min(curMax, height[i]);
    			int localsum = high * stack.size();
    			while (!stack.empty()) {
    				localsum -= stack.pop();
    			}
    			sum += localsum;
    			stack.push(height[i]);    			
    			curMax = height[i];
    		} else {
    			stack.push(height[i]);
    		}
    		
    	}    
    	return sum;
    }
}
