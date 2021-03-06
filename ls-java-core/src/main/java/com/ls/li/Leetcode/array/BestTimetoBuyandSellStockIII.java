/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-3 下午4:44:06
 */

public class BestTimetoBuyandSellStockIII {

	/**
	 * @author lishuai
	 * @data 2017-1-3 下午4:44:06
Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete at most two transactions.

Note:
You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).

0 0 4 4 5 5
5 5 3 3 0 0
	 */

	public static void main(String[] args) {
		int[] a = {7, 1, 5, 3, 6, 4};
		System.out.println(maxProfit1(a));

	}
    public static int maxProfit(int[] prices) {
        int res = 0;
        
        return res;
    }
    /**
The thinking is simple and is inspired by the best solution from Single Number II 
(I read through the discussion after I use DP).
Assume we only have 0 money at first;
4 Variables to maintain some interested 'ceilings' so far:
The maximum of if we've just buy 1st stock, if we've just sold 1nd stock, 
if we've just buy 2nd stock, if we've just sold 2nd stock.
Very simple code too and work well. I have to say the logic is simple than those in Single Number II.

public class Solution {
    
}
     */
    
    public int maxProfit3(int[] prices) {
        int hold1 = Integer.MIN_VALUE, hold2 = Integer.MIN_VALUE;
        int release1 = 0, release2 = 0;
        for(int i:prices){                              // Assume we only have 0 money at first
            release2 = Math.max(release2, hold2+i);     // The maximum if we've just sold 2nd stock so far.
            hold2    = Math.max(hold2,    release1-i);  // The maximum if we've just buy  2nd stock so far.
            release1 = Math.max(release1, hold1+i);     // The maximum if we've just sold 1nd stock so far.
            hold1    = Math.max(hold1,    -i);          // The maximum if we've just buy  1st stock so far. 
        }
        return release2; ///Since release1 is initiated as 0, so release2 will always higher than release1.
    }
    /**
Solution is commented in the code. Time complexity is O(kn), space complexity can be O(n) 
because this DP only uses the result from last step. 
But for cleaness this solution still used O(kn) space complexity to preserve similarity to the equations in the comments.

class Solution {
public:
    int maxProfit(vector<int> &prices) {
        // f[k, ii] represents the max profit up until prices[ii] (Note: NOT ending with prices[ii]) using at most k transactions. 
        // f[k, ii] = max(f[k, ii-1], prices[ii] - prices[jj] + f[k-1, jj]) { jj in range of [0, ii-1] }
        //          = max(f[k, ii-1], prices[ii] + max(f[k-1, jj] - prices[jj]))
        // f[0, ii] = 0; 0 times transation makes 0 profit
        // f[k, 0] = 0; if there is only one price data point you can't make any money no matter how many times you can trade
        if (prices.size() <= 1) return 0;
        else {
            int K = 2; // number of max transation allowed
            int maxProf = 0;
            vector<vector<int>> f(K+1, vector<int>(prices.size(), 0));
            for (int kk = 1; kk <= K; kk++) {
                int tmpMax = f[kk-1][0] - prices[0];
                for (int ii = 1; ii < prices.size(); ii++) {
                    f[kk][ii] = max(f[kk][ii-1], prices[ii] + tmpMax);
                    tmpMax = max(tmpMax, f[kk-1][ii] - prices[ii]);
                    maxProf = max(f[kk][ii], maxProf);
                }
            }
            return maxProf;
        }
    }
};
     */
    //2 dp
    public int maxProfit2(int[] prices) {
        if(prices == null || prices.length == 0) return 0;

        int[] dp = new int[prices.length];//k == 0, dp[i] = 0
        int K = 2, tmpMax = 0;  
        for(int k =1; k<=K; k++) {
            tmpMax = dp[0] - prices[0];
            dp[0] = 0;
            for(int i = 1; i<prices.length; i++) {
                tmpMax = Math.max(tmpMax, dp[i] - prices[i]);
                dp[i] = Math.max(dp[i-1], prices[i]+tmpMax);
            }
        }
        return dp[prices.length -1];
    }
    
    //1九章
    public static int maxProfit1(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int[] left = new int[prices.length];
        int[] right = new int[prices.length];

        // DP from left to right;
        left[0] = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            min = Math.min(prices[i], min);
            left[i] = Math.max(left[i - 1], prices[i] - min);
        }

        // DP from right to left;
        right[prices.length - 1] = 0;
        int max = prices[prices.length - 1];
        for (int i = prices.length - 2; i >= 0; i--) {
            max = Math.max(prices[i], max);
            right[i] = Math.max(right[i + 1], max - prices[i]);
        }

        int profit = 0;
        for (int i = 0; i < prices.length; i++){
            profit = Math.max(left[i] + right[i], profit);  
        }

        return profit;
    }
}
