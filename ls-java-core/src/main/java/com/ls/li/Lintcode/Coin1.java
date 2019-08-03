/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-29 下午5:53:38
 */

public class Coin1 {

	/**
	 * @author lishuai
	 * @data 2017-3-29 下午5:53:38
有 n 个硬币排成一条线。两个参赛者轮流从右边依次拿走 1 或 2 个硬币，直到没有硬币为止。拿到最后一枚硬币的人获胜。

请判定 第一个玩家 是输还是赢？


n = 1, 返回 true.

n = 2, 返回 true.

n = 3, 返回 false.

n = 4, 返回 true.

n = 5, 返回 true.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public boolean firstWillWin(int n) {
        // write your code here
        int []dp = new int[n+1];
        
        return MemorySearch(n, dp);
        
    }
	//1
    boolean MemorySearch(int n, int []dp) { // 0 is empty, 1 is false, 2 is true
        if(dp[n] != 0) {
            if(dp[n] == 1)
                return false;
            else
                return true;
        }
        if(n <= 0) {
            dp[n] = 1;
        } else if(n == 1) {
            dp[n] = 2;
        } else if(n == 2) {
            dp[n] = 2;
        } else if(n == 3) {
            dp[n] = 1;
        } else {
            if((MemorySearch(n-2, dp) && MemorySearch(n-3, dp)) || 
                (MemorySearch(n-3, dp) && MemorySearch(n-4, dp) )) {
                dp[n] = 2;
            } else {
                dp[n] = 1;
            }
        }
        if(dp[n] == 2) 
            return true;
        return false;
    }

}

class Solution {
    /**
     * @param n: an integer
     * @return: a boolean which equals to true if the first player will win
     */
    public boolean firstWillWin(int n) {
        // write your code here
        boolean []dp = new boolean[n+1];
        boolean []flag = new boolean[n+1];
        return MemorySearch(n, dp, flag);
    }
    boolean MemorySearch(int i, boolean []dp, boolean []flag) {
        if(flag[i] == true) {
            return dp[i];
        }
        if(i == 0) {
            dp[i] = false;
        } else if(i == 1) {
            dp[i] = true;
        } else if(i == 2) {
            dp[i] = true;
        } else {
            dp[i] = !MemorySearch(i-1, dp, flag) || !MemorySearch(i-2, dp, flag);
        }
        flag[i] =true;
        return dp[i];
    }
}