/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 上午9:29:57
 */

public class LongestCommonSubsequence {

	/**
	 * @author lishuai
	 * @data 2017-3-30 上午9:29:57
给出两个字符串，找到最长公共子序列(LCS)，返回LCS的长度。

最长公共子序列的定义：

最长公共子序列问题是在一组序列（通常2个）中找到最长公共子序列（注意：不同于子串，LCS不需要是连续的子串）。
该问题是典型的计算机科学问题，是文件差异比较程序的基础，在生物信息学中也有所应用。

给出"ABCD" 和 "EDCA"，这个LCS是 "A" (或 D或C)，返回1

给出 "ABCD" 和 "EACB"，这个LCS是"AC"返回 2

[
[0, 0, 0, 0, 0], 
[0, 0, 0, 0, 1], 
[0, 0, 0, 0, 1], 
[0, 0, 0, 1, 1], 
[0, 0, 1, 1, 1]
]


[
[0, 0, 0, 0, 0], 
[0, 0, 1, 1, 1], 
[0, 0, 1, 1, 2], 
[0, 0, 1, 2, 2], 
[0, 0, 1, 2, 2]
]
	 */

	public static void main(String[] args) {
		
		
		System.out.println(longestCommonSubsequence("ABCD","EACB"));

	}
	public static int longestCommonSubsequence(String A, String B) {
        int n = A.length();
	    int m = B.length();
        int f[][] = new int[n + 1][m + 1];
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                f[i][j] = Math.max(f[i - 1][j], f[i][j - 1]);
                if(A.charAt(i - 1) == B.charAt(j - 1))
                    f[i][j] = f[i - 1][j - 1] + 1;
            }
        }
        return f[n][m];
    }
}
