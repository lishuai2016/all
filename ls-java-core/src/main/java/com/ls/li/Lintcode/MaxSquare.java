/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 上午9:31:37
 */

public class MaxSquare {

	/**
	 * @author lishuai
	 * @data 2017-3-30 上午9:31:37
在一个二维01矩阵中找到全为1的最大正方形

1 0 1 0 0
1 0 1 1 1
1 1 1 1 1
1 0 0 1 0
返回 4

[[1, 0, 1, 0, 0], 
[1, 0, 1, 1, 1], 
[1, 1, 1, 2, 2], 
[1, 0, 0, 1, 0]]
	 */

	public static void main(String[] args) {
		int[][] a = {{1 ,0 ,1 ,0 ,0},{1, 0, 1 ,1, 1},{1 ,1 ,1 ,1 ,1},{1, 0 ,0 ,1 ,0}};
		maxSquare1(a);
	}
	public static int t(int[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}
		int res = 0;
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] dp = new int[m][n];
		for (int i = 0; i < m; i++) {
			dp[i][0] = matrix[i][0];
			res = Math.max(res, dp[i][0]);
		}
		for (int i = 0; i < n; i++) {
			dp[0][i] = matrix[0][i];
			res = Math.max(res, dp[0][i]);
		}
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (matrix[i][j] == 1) {
					dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
				} else {
					dp[i][j] = 0;
				}
				res = Math.max(dp[i][j], res);
			}
		}
		return res * res;
	}
	
	public static int maxSquare1(int[][] matrix) {
        // write your code here
        int ans = 0;
        int n = matrix.length;
        int m;
        if(n > 0)
            m = matrix[0].length;
        else 
            return ans;
        int [][]res = new int [n][m];
        for(int i = 0; i < n; i++){
            res[i][0] = matrix[i][0];
            ans = Math.max(res[i][0] , ans);
            for(int j = 1; j < m; j++) {
                if(i > 0) {
                    if(matrix[i][j] > 0) {
                        res[i][j] = Math.min(res[i - 1][j],Math.min(res[i][j-1], res[i-1][j-1])) + 1;
                    } else {
                        res[i][j] = 0;
                    }
                    
                } else {
                    res[i][j] = matrix[i][j];
                }
                ans = Math.max(res[i][j], ans);
            }
        }
        return ans*ans;
    }
    
    public int maxSquare(int[][] matrix) {
        // write your code here
        int ans = 0;
        int n = matrix.length;
        int m;
        if(n > 0)
            m = matrix[0].length;
        else 
            return ans;
        int [][]res = new int [2][m];
        for(int i = 0; i < n; i++){
            res[i%2][0] = matrix[i][0];
            ans = Math.max(res[i%2][0] , ans);
            for(int j = 1; j < m; j++) {
                if(i > 0) {
                    if(matrix[i][j] > 0) {
                        res[i%2][j] = Math.min(res[(i - 1)%2][j],Math.min(res[i%2][j-1], res[(i-1)%2][j-1])) + 1;
                    } else {
                        res[i%2][j] = 0;
                    }
                    
                }
                else {
                    res[i%2][j] = matrix[i%2][j];
                }
                ans = Math.max(res[i%2][j], ans);
            }
        }
        return ans*ans;
    }
}
