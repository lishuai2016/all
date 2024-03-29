/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-6 上午8:38:50
 */

public class UniquePaths {

	/**
	 * @author lishuai
	 * @data 2016-12-6 上午8:38:51
A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).

The robot can only move either down or right at any point in time. 
The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).

How many possible unique paths are there?
Note: m and n will be at most 100.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//1 Dp
    public static int uniquePaths(int m, int n) {
    	if (m < 1 || n < 1) return 0;
        int[][] dp = new int[m][n];
        dp[0][0] = 1;
        //第一行
        for (int i = 1;i < n;i++) {
        	dp[0][i] = 1;
        }
    	//第一列
        for (int i = 1;i < m;i++) {
        	dp[i][0] = 1;
        }
        for (int i = 1;i < m;i++) {
        	for (int j = 1;j < n;j++) {
        		dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
        	}
        }
    	return dp[m - 1][n - 1];
    }
}
