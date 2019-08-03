/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-29 下午4:02:43
 */

public class BackPack {

	/**
	 * @author lishuai
	 * @data 2017-3-29 下午4:02:43
	 在n个物品中挑选若干物品装入背包，最多能装多满？假设背包的大小为m，每个物品的大小为A[i]
如果有4个物品[2, 3, 5, 7]

如果背包的大小为11，可以选择[2, 3, 5]装入背包，最多可以装满10的空间。

如果背包的大小为12，可以选择[2, 3, 7]装入背包，最多可以装满12的空间。

函数需要返回最多能装满的空间大小。
	 */

	public static void main(String[] args) {
		int[] a = {2, 3, 5, 7};
		 System.out.println(backPack(11,a));
	}
	 public static int backPack(int m, int[] A) {
	        boolean f[][] = new boolean[A.length + 1][m + 1];
	        f[0][0] = true;
	        for (int i = 1; i <= A.length; i++) {
	            for (int j = 0; j <= m; j++) {
	                f[i][j] = f[i - 1][j];
	                if (j >= A[i-1] && f[i-1][j - A[i-1]]) {
	                    f[i][j] = true;
	                }
	            } // for j
	        } // for i
	        for (int i = 0; i <= A.length; i++) {
	            for (int j = 0; j <= m; j++) {
	                    if(f[i][j])
	                        System.out.print(1);
	                    else
	                        System.out.print(0);
	                   
	            } // for j
	            System.out.println();
	        }
	        
	        for (int i = m; i >= 0; i--) {
	            if (f[A.length][i]) {
	                return i;
	            }
	        }
	        
	        return 0;
	    }
	
	
//	public int backPack(int m, int[] A) {
//		
//		return 0;
//	}
}
