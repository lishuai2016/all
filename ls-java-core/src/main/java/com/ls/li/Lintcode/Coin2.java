/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 下午5:01:15
 */

public class Coin2 {

	/**
	 * @author lishuai
	 * @data 2017-3-30 下午5:01:15
有 n 个不同价值的硬币排成一条线。两个参赛者轮流从左边依次拿走 1 或 2 个硬币，直到没有硬币为止。计算两个人分别拿到的硬币总价值，价值高的人获胜。

请判定 第一个玩家 是输还是赢？


样例
给定数组 A = [1,2,2], 返回 true.

给定数组 A = [1,2,4], 返回 false.
	 */

	public static void main(String[] args) {
		int[] v = {1,2,2};
		firstWillWin(v);
	}
//	public boolean firstWillWin(int[] values) {
//        // write your code here
//		return false;
//    }
	
	
	
	public static boolean firstWillWin(int[] values) {
        // write your code here
        int n = values.length;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; ++i)
            sum[i] = sum[i -  1] + values[n - i];

        int[] dp = new int[n + 1];
        dp[1] = values[n - 1];
        for (int i = 2; i <= n; ++i)
            dp[i] = Math.max(sum[i] - dp[i - 1], sum[i] - dp[i - 2]);
            
        return dp[n]  > sum[n] / 2;
    }
	
	
	
}
