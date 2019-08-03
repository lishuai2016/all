/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-3 上午10:55:04
 */

public class Searcha2DMatrix {

	/**
	 * @author lishuai
	 * @data 2017-1-3 上午10:55:04
Write an efficient algorithm that searches for a value in an m x n matrix. 
This matrix has the following properties:

Integers in each row are sorted from left to right.
The first integer of each row is greater than the last integer of the previous row.
For example,

Consider the following matrix:

[
  [1,   3,  5,  7],
  [10, 11, 16, 20],
  [23, 30, 34, 50]
]
Given target = 3, return true.
	 */

	public static void main(String[] args) {
	

	}
	//4九章
	 public boolean searchMatrix(int[][] matrix, int target) {
	        if (matrix == null || matrix.length == 0) {
	            return false;
	        }
	        if (matrix[0] == null || matrix[0].length == 0) {
	            return false;
	        }
	        
	        int row = matrix.length;
	        int column = matrix[0].length;
	        
	        // find the row index, the last number <= target 
	        int start = 0, end = row - 1;
	        while (start + 1 < end) {
	            int mid = start + (end - start) / 2;
	            if (matrix[mid][0] == target) {
	                return true;
	            } else if (matrix[mid][0] < target) {
	                start = mid;
	            } else {
	                end = mid;
	            }
	        }
	        if (matrix[end][0] <= target) {
	            row = end;
	        } else if (matrix[start][0] <= target) {
	            row = start;
	        } else {
	            return false;
	        }
	        
	        // find the column index, the number equal to target
	        start = 0;
	        end = column - 1;
	        while (start + 1 < end) {
	            int mid = start + (end - start) / 2;
	            if (matrix[row][mid] == target) {
	                return true;
	            } else if (matrix[row][mid] < target) {
	                start = mid;
	            } else {
	                end = mid;
	            }
	        }
	        if (matrix[row][start] == target) {
	            return true;
	        } else if (matrix[row][end] == target) {
	            return true;
	        }
	        return false;
	    }
	
	//3九章
	  public boolean searchMatrix3(int[][] matrix, int target) {
	        if (matrix == null || matrix.length == 0) {
	            return false;
	        }
	        if (matrix[0] == null || matrix[0].length == 0) {
	            return false;
	        }
	        
	        int row = matrix.length, column = matrix[0].length;
	        int start = 0, end = row * column - 1;
	        
	        while (start + 1 < end) {
	            int mid = start + (end - start) / 2;
	            int number = matrix[mid / column][mid % column];
	            if (number == target) {
	                return true;
	            } else if (number < target) {
	                start = mid;
	            } else {
	                end = mid;
	            }
	        }
	        
	        if (matrix[start / column][start % column] == target) {
	            return true;
	        } else if (matrix[end / column][end % column] == target) {
	            return true;
	        }
	        
	        return false;
	    }

//	Use binary search.
//
//	n * m matrix convert to an array => matrix[x][y] => a[x * m + y]
//
//	an array convert to n * m matrix => a[x] =>matrix[x / m][x % m];
//
//	class Solution {
//	public:
//	    bool searchMatrix(vector<vector<int> > &matrix, int target) {
//	        int n = matrix.size();
//	        int m = matrix[0].size();
//	        int l = 0, r = m * n - 1;
//	        while (l != r){
//	            int mid = (l + r - 1) >> 1;
//	            if (matrix[mid / m][mid % m] < target)
//	                l = mid + 1;
//	            else 
//	                r = mid;
//	        }
//	        return matrix[r / m][r % m] == target;
//	    }
//	};
	//2 Don't treat it as a 2D matrix, just treat it as a sorted list;  Use  binary search.二分查找 0ms
	 public static boolean searchMatrix2(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0) {
	    		return false;
	    }
    	int rows = matrix.length;
    	int colums = matrix[0].length;
    	if (target < matrix[0][0] || target > matrix[rows - 1][colums -1]) {
    		return false;
    	}
    	int left = 0;
    	int right = rows * colums - 1;
    	while (left <= right) {
    		int mid = (left + right) >> 1;
    		if (matrix[mid / colums][mid % colums] == target) {
    			return true;
    		} else if (matrix[mid / colums][mid % colums] < target) {
    			left = mid + 1;
    		} else {
    			right = mid - 1;
    		}
    	}		 
		return false;
	 }
	
	//1 思路：首先确定它在哪一行，然后遍历该行即可        0ms
    public static boolean searchMatrix1(int[][] matrix, int target) {
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
