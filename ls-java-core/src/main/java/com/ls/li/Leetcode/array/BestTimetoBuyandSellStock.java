/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-3 下午4:12:18
 */

public class BestTimetoBuyandSellStock {

	/**
	 * @author lishuai
	 * @data 2017-1-3 下午4:12:18
Say you have an array for which the ith element is the price of a given stock on day i.

If you were only permitted to complete at most one transaction (ie, buy one and sell one share of the stock), 
design an algorithm to find the maximum profit.

Example 1:
Input: [7, 1, 5, 3, 6, 4]
Output: 5

max. difference = 6-1 = 5 (not 7-1 = 6, as selling price needs to be larger than buying price)
Example 2:
Input: [7, 6, 4, 3, 1]
Output: 0

In this case, no transaction is done, i.e. max profit = 0.
	 */

	public static void main(String[] args) {
		int[] a = {1,1};
		System.out.println(maxProfit(a));
	}
	//3 Kadane's Algorithm (类似Maximum Subarray)
	public static int maxProfit(int[] prices) {
		int maxCur = 0, maxSoFar = 0;
        for(int i = 1; i < prices.length; i++) {
            maxCur = Math.max(0, maxCur += prices[i] - prices[i-1]);
            maxSoFar = Math.max(maxCur, maxSoFar);
        }
        return maxSoFar;
    }
	//1  Time Limit Exceeded
    public static int maxProfit1(int[] prices) {
    	if (prices == null || prices.length == 0) return 0;
    	int res = 0;
    	for (int i = prices.length - 1; i > 0; i--) {
    		for (int j = i - 1; j >= 0; j--) {
    			res = Math.max(res, prices[i] - prices[j]);
    		}
    	}
    	return res;
    }
    //2九章 思路：维持一个当前能看到的最小值，然后拿当前遍历值和最小值得差和全局变量比较即可
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;  //just remember the smallest price
        int profit = 0;
        for (int i : prices) {
            min = i < min ? i : min;
            profit = (i - min) > profit ? i - min : profit;
        }
        
        return profit;
    }
}
