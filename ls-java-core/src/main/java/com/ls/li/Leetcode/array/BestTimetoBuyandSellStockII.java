/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-3 下午4:40:51
 */

public class BestTimetoBuyandSellStockII {

	/**
	 * @author lishuai
	 * @data 2017-1-3 下午4:40:51
Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete as many transactions as you like 
(ie, buy one and sell one share of the stock multiple times). 
However, you may not engage in multiple transactions at the same time 
(ie, you must sell the stock before you buy again).
Input: [7, 1, 5, 3, 6, 4]   1 5 ->4  3 6 -> 3    3 + 4=7
	 */

	public static void main(String[] args) {
		int[] a = {7, 1, 5, 3, 6, 4};
		System.out.println(maxProfit1(a));
	}
    public static int maxProfit(int[] prices) {
    	if (prices == null || prices.length == 0) return 0;
        int res = 0;
        for (int i = 0; i < prices.length - 1; i++) {
        	if (prices[i + 1] > prices[i]) {
        		res += prices[i + 1] - prices[i];
        	}
        }
        return res;
    }
    //1九章
    public static int maxProfit1(int[] prices) {
        int profit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            int diff = prices[i+1] - prices[i];
            if (diff > 0) {
                profit += diff;
            }
        }
        return profit;
    }
}
