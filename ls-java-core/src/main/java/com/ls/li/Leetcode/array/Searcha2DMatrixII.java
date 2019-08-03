/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-3 上午11:51:16
 */

public class Searcha2DMatrixII {

	/**
	 * @author lishuai
	 * @data 2017-1-3 上午11:51:16
Write an efficient algorithm that searches for a value in an m x n matrix. 
This matrix has the following properties:

Integers in each row are sorted in ascending from left to right.
Integers in each column are sorted in ascending from top to bottom.
For example,

Consider the following matrix:

[
  [1,   4,  7, 11, 15],
  [2,   5,  8, 12, 19],
  [3,   6,  9, 16, 22],
  [10, 13, 14, 17, 24],
  [18, 21, 23, 26, 30]
]
Given target = 5, return true.

Given target = 20, return false.
	 */

	public static void main(String[] args) {
		int[][] a = {{1,   4,  7, 11, 15},{2,   5,  8, 12, 19},{3,   6,  9, 16, 22},{10, 13, 14, 17, 24},{18, 21, 23, 26, 30}};
		
		 System.out.println(searchMatrix(a,20));
	}
	//3九章（同2从左下开始）
	 public static int searchMatrix(int[][] matrix, int target) {
	        // check corner case
	        if (matrix == null || matrix.length == 0) {
	            return 0;
	        }
	        if (matrix[0] == null || matrix[0].length == 0) {
	            return 0;
	        }
	        
	        // from bottom left to top right
	        int n = matrix.length;
	        int m = matrix[0].length;
	        int x = n - 1;
	        int y = 0;
	        int count = 0;
	        
	        while (x >= 0 && y < m) {
	            if (matrix[x][y] < target) {
	                y++;
	            } else if (matrix[x][y] > target) {
	                x--;
	            } else {
	                count++;
	                x--;
	                y++;
	            }
	        }
	        return count;
	    }
	
	//2 分块缩小范围（从右上开始）  time complexity is O(m+n).
	public static boolean searchMatrix2(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0) {
    		return false;
    	}
    	int rows = matrix.length;
    	int colums = matrix[0].length;
    	if (target < matrix[0][0] || target > matrix[rows - 1][colums -1]) {
    		return false;
    	}
    	int i = 0;
    	int j = colums - 1;
    	while (i <= rows - 1 && j >= 0) {
    		if (matrix[i][j] == target) {
    			return true;
    			//去除一列
    		} else if (matrix[i][j] > target) {
    			j--;
    			//去除一行
    		} else {
    			i++;
    		}
    	}
		return false;
	}
	
	
	//1
	public boolean searchMatrix0(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0) {
    		return false;
    	}
    	int rows = matrix.length;
    	int colums = matrix[0].length;
    	if (target < matrix[0][0] || target > matrix[rows - 1][colums -1]) {
    		return false;
    	}
    	for (int i = 0; i < rows; i++) {    		
    		if (matrix[i][0] <= target && matrix[i][colums - 1] >= target) {
    			for (int j = 0; j < colums; j++) {
    				if (matrix[i][j] == target) {
    					return true;
    				}
    			}
    		}
    	}   	
        return false;
    }
}
