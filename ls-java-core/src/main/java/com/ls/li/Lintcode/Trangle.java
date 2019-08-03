/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-29 下午2:59:00
 */

public class Trangle {

	/**
	 * @author lishuai
	 * @data 2017-3-29 下午2:59:00
比如，给出下列数字三角形：

[
     [2],
    [3,4],
   [6,5,7],
  [4,1,8,3]
]
从顶到底部的最小路径和为11 ( 2 + 3 + 5 + 1 = 11)。

2
3 4
6 5 7
4 1 8 3
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//[[1],[2,3]]
	
	public static int getMin1(int[][] a) {
		if (a == null || a.length == 0 || a[0].length == 0) {
			return 0;
		}
		int minPath = 0;
		int m = a.length;
		int n = a[m - 1].length;
		int[][] res = new int[m][n];
		res[0][0] = a[0][0];
		for (int i = 1; i < m; i++) {
			res[i][0] = res[i - 1][0] + a[i][0];
			res[i][i] = res[i - 1][i - 1] + a[i][i];
		}
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < i; j++) {								
				res[i][j] = (res[i - 1][j] > res[i - 1][j - 1] ? res[i - 1][j - 1] : res[i - 1][j]) + a[i][j];				
			}
		}
		minPath = res[m - 1][0];
		for (int i = 1; i < n; i++) {
			if (res[m - 1][i] < minPath) {
				minPath = res[m - 1][i];
			}
		}
		return minPath;
	}
	

}
