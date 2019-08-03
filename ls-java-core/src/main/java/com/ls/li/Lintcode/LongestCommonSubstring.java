/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 上午11:31:24
 */

public class LongestCommonSubstring {

	/**
	 * @author lishuai
	 * @data 2017-3-30 上午11:31:24
给出两个字符串，找到最长公共子串，并返回其长度。



 注意事项

子串的字符应该连续的出现在原字符串中，这与子序列有所不同。

您在真实的面试中是否遇到过这个题？ Yes
样例
给出A=“ABCD”，B=“CBCE”，返回 2
  A B C D
C
B
C
E

[
[0, 0, 0, 0, 0], 
[0, 0, 0, 0, 0], 
[0, 0, 1, 0, 0], 
[0, 1, 0, 2, 0], 
[0, 0, 0, 0, 0]
]

	 */

	public static void main(String[] args) {
		System.out.println(longestCommonSubstring("ABCD","CBCE"));

	}
	 public static int longestCommonSubstring(String A, String B) {	       
	        int n = A.length();
	        int m = B.length();
	        int[][] f = new int[n + 1][m + 1];	        	       
	        for (int i = 1; i <= n; i++) {
	            for (int j = 1; j <= m; j++) {
	                if (A.charAt(i - 1) == B.charAt(j - 1)) {
	                    f[i][j] = f[i - 1][j - 1] + 1;
	                } else {
	                    f[i][j] = 0;
	                }
	            }
	        }
	        int max = 0;
	        for (int i = 1; i <= n; i++) {
	            for (int j = 1; j <= m; j++) {
	                max = Math.max(max, f[i][j]);
	            }
	        }	        
	        return max;
	    }
}
