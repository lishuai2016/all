/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-6 上午8:48:10
 */

public class UniquePathsII {

	/**
	 * @author lishuai
	 * @data 2016-12-6 上午8:48:10
Follow up for "Unique Paths":

Now consider if some obstacles are added to the grids. How many unique paths would there be?

An obstacle and empty space is marked as 1 and 0 respectively in the grid.

For example,
There is one obstacle in the middle of a 3x3 grid as illustrated below.

[
  [0,0,0],
  [0,1,0],
  [0,0,0]
]
The total number of unique paths is 2.

Note: m and n will be at most 100.
	 */

	public static void main(String[] args) {
		int[][] a = new int[1][1];
		a[0][0] = 1;
		uniquePathsWithObstacles(a);
	}
	//1 DP 维护一个二维DP，对应位置填写可达的路径数，不可达设置为0   
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
    	if (obstacleGrid == null || obstacleGrid.length == 0 || obstacleGrid[0].length == 0) return 0;
    	int m = obstacleGrid.length;
    	int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        //初始的赋值需要注意
        if (obstacleGrid[0][0] == 0) dp[0][0] = 1; 
        else dp[0][0] = 0;
        //第一行
        for (int i = 1;i < n;i++) {
        	if (obstacleGrid[0][i] != 1 && dp[0][i - 1] != 0) dp[0][i] = 1;
        	else dp[0][i] = 0;
        }
    	//第一列
        for (int i = 1;i < m;i++) {
        	if (obstacleGrid[i][0] != 1 && dp[i - 1][0] != 0) dp[i][0] = 1;
        	else dp[i][0] = 0;        	
        }
        for (int i = 1;i < m;i++) {
        	for (int j = 1;j < n;j++) {
        		if (obstacleGrid[i][j] != 1) {
//        			if (dp[i - 1][j] == 0 && dp[i][j - 1] == 0) {
//        				dp[i][j] = 0;
//        			} else if (dp[i - 1][j] == 0 && dp[i][j - 1] != 0) {
//        				dp[i][j] = dp[i][j - 1];
//        			} else if (dp[i - 1][j] != 0 && dp[i][j - 1] == 0) {
//        				dp[i][j] = dp[i - 1][j];
//        			} else {
//        				dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
//        			}	
        			dp[i][j] = dp[i - 1][j] + dp[i][j - 1];//因为不可达设置为0，所以可以简化上面的代码
        		} else dp[i][j] = 0;
        	}
        }
    	return dp[m - 1][n - 1];
    }
}
