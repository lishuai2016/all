/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-29 下午4:44:04
 */

public class BackPackII {

	/**
	 * @author lishuai
	 * @data 2017-3-29 下午4:44:04
给出n个物品的体积A[i]和其价值V[i]，将他们装入一个大小为m的背包，最多能装入的总价值有多大？

A[i], V[i], n, m均为整数。你不能将物品进行切分。你所挑选的物品总体积需要小于等于给定的m。

对于物品体积[2, 3, 5, 7]和对应的价值[1, 5, 2, 4], 假设背包大小为10的话，最大能够装入的价值为9。
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {2, 3, 5, 7};
		int[] v = {1, 5, 2, 4};
		backPackII(10, a, v);
	}
	public static int backPackII(int m, int[] A, int V[]) {
        // write your code here
        int[] f = new int[m+1];
        for (int i = 0; i <=m ; ++i) f[i] = 0;
        int n = A.length , i, j;
        for(i = 0; i < n; i++){
            for(j = m; j >= A[i]; j--){
                if (f[j] < f[j - A[i]] + V[i])
                    f[j] = f[j - A[i]] + V[i];
            }
        }
        for (int k = 0; k <=m ; ++k) {
        	System.out.print(f[k] + "  ");
        }
        return f[m];
    }
}
