/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-7 下午5:50:52
 */

public class Triangle {

	/**
	 * @author lishuai
	 * @data 2016-12-7 下午5:50:52
Given a triangle, find the minimum path sum from top to bottom. 
Each step you may move to adjacent numbers on the row below.

For example, given the following triangle
[
     [2],
    [3,4],
   [6,5,7],
  [4,1,8,3]
]
The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).

Note:
Bonus point if you are able to do this using only O(n) extra space, 
where n is the total number of rows in the triangle.
	 */

	public static void main(String[] args) {
		List<List<Integer>> list = new 	ArrayList<List<Integer>>();
		list.add(Arrays.asList(2));
		list.add(Arrays.asList(3,4));
		list.add(Arrays.asList(6,5,7));
		list.add(Arrays.asList(4,1,8,3));
		
		//minimumTotal(list);

	}
	//3 九章 Bottom-Up
	 public int minimumTotal(int[][] triangle) {
	        if (triangle == null || triangle.length == 0) {
	            return -1;
	        }
	        if (triangle[0] == null || triangle[0].length == 0) {
	            return -1;
	        }
	        
	        // state: f[x][y] = minimum path value from x,y to bottom
	        int n = triangle.length;
	        int[][] f = new int[n][n];
	        
	        // initialize 
	        for (int i = 0; i < n; i++) {
	            f[n - 1][i] = triangle[n - 1][i];
	        }
	        
	        // bottom up
	        for (int i = n - 2; i >= 0; i--) {
	            for (int j = 0; j <= i; j++) {
	                f[i][j] = Math.min(f[i + 1][j], f[i + 1][j + 1]) + triangle[i][j];
	            }
	        }
	        
	        // answer
	        return f[0][0];
	    }
	
	
	//2 九章0: top-down
	 public int minimumTotal2(int[][] triangle) {
	        if (triangle == null || triangle.length == 0) {
	            return -1;
	        }
	        if (triangle[0] == null || triangle[0].length == 0) {
	            return -1;
	        }
	        
	        // state: f[x][y] = minimum path value from 0,0 to x,y
	        int n = triangle.length;
	        int[][] f = new int[n][n];
	        
	        // initialize 
	        f[0][0] = triangle[0][0];
	        for (int i = 1; i < n; i++) {
	            f[i][0] = f[i - 1][0] + triangle[i][0];
	            f[i][i] = f[i - 1][i - 1] + triangle[i][i];
	        }
	        
	        // top down
	        for (int i = 1; i < n; i++) {
	            for (int j = 1; j < i; j++) {
	                f[i][j] = Math.min(f[i - 1][j], f[i - 1][j - 1]) + triangle[i][j];
	            }
	        }
	        
	        // answer
	        int best = f[n - 1][0];
	        for (int i = 1; i < n; i++) {
	            best = Math.min(best, f[n - 1][i]);
	        }
	        return best;
	    }
	
	
	//1
	public static int minimumTotal1(List<List<Integer>> triangle) {
	    Deque<Integer> queue = new LinkedList<Integer>();
	    int count=triangle.size();
	    queue.add(triangle.get(0).get(0));
	    for (int i=1;i<count;i++){
	        List<Integer> list= triangle.get(i);
	        for (int j=0;j<=i;j++){
	        	int min=0;
	            if (j==0)
	            	 min=list.get(0)+queue.peekFirst();               	
	            else if (j==i)
	            	 min =list.get(j)+queue.pollFirst();              	
	            else
	            	min = Math.min(queue.pollFirst(),queue.peekFirst())+list.get(j);              	               
	            queue.addLast(min);               
	        }
	    }
	    int result=Integer.MAX_VALUE;
	    for (int i=0;i<count;i++)
	    	result=Math.min(result, queue.pollFirst());
	    return result;
	}
	//0
    public static int minimumTotal0(List<List<Integer>> triangle) {
    	if (triangle == null || triangle.size() == 0 || triangle.get(0).size() == 0) return 0;
    	int n = triangle.size();
    	int minsum = triangle.get(0).get(0);
        for (int i = 1;i < n - 1;i++) {
        	List<Integer> list = triangle.get(i);
        	int left = list.get(i - 1);
        	int right = list.get(i);
        	if (left < right) {
        		minsum += left;
        		for (int j = i + 1;j < n;j++) {
        			int size = triangle.get(j).size() - 1;
        			triangle.get(j).remove(size);
        		}
        		
        			
        		
        	} else if (left > right) {
        		minsum += right;
        		for (int j = i + 1;j < n;j++) 
        			triangle.get(j).remove(0);
        	}
        }
    	
    	
    	return 0;
    }
}
