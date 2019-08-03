/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 下午3:25:21
 */

public class HouseRobber {

	/**
	 * @author lishuai
	 * @data 2017-3-30 下午3:25:21
假设你是一个专业的窃贼，准备沿着一条街打劫房屋。每个房子都存放着特定金额的钱。你面临的唯一约束条件是：
相邻的房子装着相互联系的防盗系统，且 当相邻的两个房子同一天被打劫时，该系统会自动报警。

给定一个非负整数列表，表示每个房子中存放的钱， 算一算，如果今晚去打劫，你最多可以得到多少钱 在不触动报警装置的情况下。

您在真实的面试中是否遇到过这个题？ Yes
样例
给定 [3, 8, 4], 返回 8.
	 */

	public static void main(String[] args) {
		int[] a = {3, 8, 4};

	}
	public long houseRobber(int[] A) {
        // write your code here
		if (A == null || A.length == 0) {
			return 0;
		}
		long[] dp = new long[A.length + 1];
		dp[0] = 0;
		dp[1] = A[0];
		for (int i = 2; i <= A.length; i++) {
			dp[i] = Math.max(dp[i - 1], dp[i - 2] + A[i - 1]);
		}
		return dp[A.length];
		
		
    }
	
	//---方法二---
    public long houseRobber2(int[] A) {
        // write your code here
        int n = A.length;
        if(n == 0)
            return 0;
        long []res = new long[2];
        
        
        res[0] = 0;
        res[1] = A[0];
        for(int i = 2; i <= n; i++) {
            res[i%2] = Math.max(res[(i-1)%2], res[(i-2)%2] + A[i-1]);
        }
        return res[n%2];
    }
}
