/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-2 上午9:49:23
 */

public class ContainerWithMostWater {

	/**
	 * @author lishuai
	 * @data 2016-12-2 上午9:49:23
Given n non-negative integers a1, a2, ..., an, where each represents a point at coordinate (i, ai). 
n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0).
Find two lines, which together with x-axis forms a container, such that the container contains the most water.

Note: You may not slant the container.
1,2,3   2
1,2,3,4   4     
	 */

	public static void main(String[] args) {
		int[] a = {1,2,3,4};
		System.out.println(maxArea(a));

	}
	//1 思路简单（两个for循环）  Time Limit Exceeded   时间复杂度N*N
    public static int maxArea1(int[] height) { 
    	int max = 0;
        for (int i = 0;i < height.length;i++) {
        	for (int j = i + 1;j < height.length;j++) {
        		int min = height[i] < height[j] ? height[i] : height[j];
        		if (min * (j-i) > max) {
        			max = min * (j-i);
        		}
        	}
        }
    	return max;
    }
    /**
     *原理解释1（假设矛盾法）
 In this problem, the smart scan way is to set two pointers initialized at both ends of the array. 
 Every time move the smaller value pointer to inner array. Then after the two pointers meet, 
 all possible max cases have been scanned and the max situation is 100% reached somewhere in the scan.
  Following is a brief prove of this.

Given a1,a2,a3.....an as input array. Lets assume a10 and a20 are the max area situation. 
We need to prove that a10 can be reached by left pointer and during the time left pointer stays 
at a10, a20 can be reached by right pointer. 
That is to say, the core problem is to prove: when left pointer is at a10 and right pointer is at a21,
 the next move must be right pointer to a20.

Since we are always moving the pointer with the smaller value, i.e. if a10 > a21, 
we should move pointer at a21 to a20, as we hope. Why a10 >a21? Because if a21>a10, 
then area of a10 and a20 must be less than area of a10 and a21. 
Because the area of a10 and a21 is at least height[a10] * (21-10) 
while the area of a10 and a20 is at most height[a10] * (20-10).
 So there is a contradiction of assumption a10 and a20 has the max area.
  So, a10 must be greater than a21, then next move a21 has to be move to a20. 
  The max cases must be reached.
  
  原理解释2（觉得这个更好）
  The O(n) solution with proof by contradiction doesn't look intuitive enough to me. 
  Before moving on, read the algorithm first if you don't know it yet.

Here's another way to see what happens in a matrix representation:

Draw a matrix where the row is the first line, and the column is the second line. For example, say n=6.

In the figures below, x means we don't need to compute the volume for that case: (1) On the diagonal,
 the two lines are overlapped; (2) The lower left triangle area of the matrix is symmetric to the upper right area.

We start by computing the volume at (1,6), denoted by o. Now if the left line is shorter than the right line, 
then all the elements left to (1,6) on the first row have smaller volume, 
so we don't need to compute those cases (crossed by ---).

  1 2 3 4 5 6
1 x ------- o
2 x x
3 x x x 
4 x x x x
5 x x x x x
6 x x x x x x
Next we move the left line and compute (2,6). Now if the right line is shorter, all cases below (2,6) are eliminated.

  1 2 3 4 5 6
1 x ------- o
2 x x       o
3 x x x     |
4 x x x x   |
5 x x x x x |
6 x x x x x x
And no matter how this o path goes, we end up only need to find the max value on this path, which contains n-1 cases.

  1 2 3 4 5 6
1 x ------- o
2 x x - o o o
3 x x x o | |
4 x x x x | |
5 x x x x x |
6 x x x x x x
     */
    
    
    //2 俩个指针，一前一后移动
    public static  int maxArea2(int[] height) {
        int left = 0, right = height.length - 1;
    	int maxArea = 0;

    	while (left < right) {
    		maxArea = Math.max(maxArea, Math.min(height[left], height[right])
    				* (right - left));
    		if (height[left] < height[right])
    			left++;
    		else
    			right--;
    	}

    	return maxArea;
    }

    
    
    
    //自己默写
    public static  int maxArea(int[] height) {
        int max = 0;
        int left = 0;
        int right = height.length - 1;
        while(left < right) {
        	max = Math.max(max, Math.min(height[left], height[right]) * (right - left));
        	if (height[left] < height[right]) {
        		left++;
        	}else {
        		right--;
        	}
        }
        return max;
    }
}
