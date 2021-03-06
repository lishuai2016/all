/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-5 下午4:23:54
 */

public class SpiralMatrix {

	/**
	 * @author lishuai
	 * @data 2016-12-5 下午4:23:54
Given a matrix of m x n elements (m rows, n columns), return all elements of the matrix in spiral order.

For example,
Given the following matrix:

[
 [ 1, 2, 3 ],
 [ 4, 5, 6 ],
 [ 7, 8, 9 ]
]
You should return [1,2,3,6,9,8,7,4,5].
	 */

	public static void main(String[] args) {
		int[][] a ={{ 1, 2, 3},{4, 5, 6},{7, 8, 9}};
		System.out.println(spiralOrder(a));
	}
	//2 九章答案 和1差不多，也是分四步一个大循环，减少2行2列，当只有一行或者一列的时候跳出本次循环开始下一次循环
	 public static List<Integer> spiralOrder(int[][] matrix) {
	        List<Integer> rst = new ArrayList<Integer>();
	        if(matrix == null || matrix.length == 0) 
	            return rst;
	        
	        int rows = matrix.length;
	        int cols = matrix[0].length;
	        int count = 0;
	        while(count * 2 < rows && count * 2 < cols){
	        	//right
	            for(int i = count; i < cols-count; i++)
	                rst.add(matrix[count][i]);
	            
	            //down
	            for(int i = count+1; i< rows-count; i++)
	                rst.add(matrix[i][cols-count-1]);
	            
	            if(rows - 2 * count == 1 || cols - 2 * count == 1)  // if only one row /col remains
	                break;
	            //left    
	            for(int i = cols-count-2; i>=count; i--)
	                rst.add(matrix[rows-count-1][i]);
	            //up 
	            for(int i = rows-count-2; i>= count+1; i--)
	                rst.add(matrix[i][count]);
	            
	            count++;
	        }
	        return rst;
	    }
	
	/**
This is a very simple and easy to understand solution. I traverse right and increment rowBegin,
 then traverse down and decrement colEnd, then I traverse left and decrement rowEnd, 
 and finally I traverse up and increment colBegin.

The only tricky part is that when I traverse left or up I have to check 
whether the row or col still exists to prevent duplicates. 
If anyone can do the same thing without that check, please let me know!Any comments greatly appreciated.
	 */
	//1 思想:逐渐缩小范围，走right，down，left，up为一个大循环
	 public static List<Integer> spiralOrder1(int[][] matrix) {
	        
	        List<Integer> res = new ArrayList<Integer>();
	        
	        if (matrix.length == 0) {
	            return res;
	        }
	        
	        int rowBegin = 0;
	        int rowEnd = matrix.length-1;
	        int colBegin = 0;
	        int colEnd = matrix[0].length - 1;
	        
	        while (rowBegin <= rowEnd && colBegin <= colEnd) {
	            // Traverse Right
	            for (int j = colBegin; j <= colEnd; j ++) {
	                res.add(matrix[rowBegin][j]);
	            }
	            rowBegin++;
	            
	            // Traverse Down
	            for (int j = rowBegin; j <= rowEnd; j ++) {
	                res.add(matrix[j][colEnd]);
	            }
	            colEnd--;
	            //只有一行会出现重复元素，故加判断
	            if (rowBegin <= rowEnd) {
	                // Traverse Left
	                for (int j = colEnd; j >= colBegin; j --) {
	                    res.add(matrix[rowEnd][j]);
	                }
	            }
	            rowEnd--;
	            
	            if (colBegin <= colEnd) {
	                // Traver Up
	                for (int j = rowEnd; j >= rowBegin; j --) {
	                    res.add(matrix[j][colBegin]);
	                }
	            }
	            colBegin ++;
	        }
	        
	        return res;
	    }
	
	//0 参照写的
    public static List<Integer> spiralOrder0(int[][] matrix) {
    	List<Integer> res = new ArrayList<Integer>();
    	if (matrix.length == 0) return res;
    	int rowstart = 0;
    	int rowend = matrix.length - 1;
    	int columstart = 0;
    	int columend = matrix[0].length - 1;
    	while (rowstart <= rowend && columstart <= columend) {
    		//right
    		for (int i = columstart;i <= columend;i++) {
    			res.add(matrix[rowstart][i]);
    		}
    		rowstart++;
    		//down
    		for (int i = rowstart;i <= rowend;i++) {
    			res.add(matrix[i][columend]);
    		}
    		columend--;
    		//left
    		if (rowstart <= rowend) {
    			for (int i = columend;i >= columstart;i--) {
    				res.add(matrix[rowend][i]);
    			}   			
    		}
    		rowend--;
    		//up
    		if (columstart <= columend) {
    			for (int i = rowend;i >= rowstart;i--) {
    				res.add(matrix[i][columstart]);
    			}   			
    		}
    		columstart++;
    	}
    	
        
    	return res;
    }
}
