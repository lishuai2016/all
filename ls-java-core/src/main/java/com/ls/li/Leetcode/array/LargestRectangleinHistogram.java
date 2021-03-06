/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.Stack;

/**
 * @author lishuai
 * @data 2017-1-3 下午2:19:08
 */

public class LargestRectangleinHistogram {

	/**
	 * @author lishuai
	 * @data 2017-1-3 下午2:19:08
Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, 
find the area of largest rectangle in the histogram.


Above is a histogram where width of each bar is 1, given height = [2,1,5,6,2,3].


The largest rectangle is shown in the shaded area, which has area = 10 unit.

For example,
Given heights = [2,1,5,6,2,3],
return 10.
	 */

	public static void main(String[] args) {
		int[] a = {2,1,5,6,2,3};
		System.out.println(largestRectangleArea1(a));
	}
	//2没理解
	public static int largestRectangleArea(int[] height) {	
			Stack<Integer> stack = new Stack<Integer>();
			int max_area = 0;
			
			for(int i=0; i<=height.length; ++i){
				int height_bound = (i == height.length) ? 0 : height[i];
				
				while(!stack.isEmpty()){
					int h = height[stack.peek()];
					
					// calculate the area for every ascending slope.
					if(h < height_bound){
						break;
					}
					stack.pop();
					
					// at the end, the area with the height of the minimal element.
					int index_bound = stack.isEmpty() ? -1 : stack.peek();
					max_area = Math.max(max_area, h*((i-1)-index_bound));
				}				
				stack.push(i);
			}			
			return max_area;		
    }
	//1九章 	借助一个递增栈（维护一个左边的边界）
	public static int largestRectangleArea1(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        
        Stack<Integer> stack = new Stack<Integer>();
        int max = 0;
        for (int i = 0; i <= height.length; i++) {
            int curt = (i == height.length) ? -1 : height[i];
            while (!stack.isEmpty() && curt <= height[stack.peek()]) {
                int h = height[stack.pop()];
                int w = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, h * w);
            }
            stack.push(i);
        }
        
        return max;
    }
	//0
	public static int largestRectangleArea0(int[] heights) {	
	    int res = 0;
	    if (heights == null || heights.length == 0)  return res;
	    Stack<Integer> stack = new Stack<>();
	    for (int i = 0; i <= heights.length; i++) {
	    	int cur = (i == heights.length) ? -1 : heights[i];
	    	while (!stack.empty() && cur <= heights[stack.peek()]) {
	    		int h = heights[stack.pop()];
	    		int w = stack.empty() ? i : i - stack.peek() - 1;
	    		res = Math.max(res, h * w);
	    	}
	    	stack.push(i);
	    }	    
		return res;
	}
}
