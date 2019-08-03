/**
 * 
 */
package com.ls.li.Leetcode.hashtable;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-29 上午11:57:14
 */

public class MaximalRectangle {

	/**
	 * @author lishuai
	 * @data 2016-12-29 上午11:57:14
Given a 2D binary matrix filled with 0's and 1's, 
find the largest rectangle containing only 1's and return its area.

For example, given the following matrix:

1 0 1 0 0
1 0 1 1 1
1 1 1 1 1
1 0 0 1 0
Return 6.
	 */

	public static void main(String[] args) {
		char[][] a = {{'1', '0' ,'1' ,'0' ,'0'},{'1', '0' ,'1', '1' ,'1'},{'1', '1' ,'1' ,'1' ,'1'},{'1' ,'0' ,'0' ,'1' ,'0'}};
		maximalRectangle(a);
	}
	
	/**
The DP solution proceeds row by row, starting from the first row. 
Let the maximal rectangle area at row i and column j be computed by 
[right(i,j) - left(i,j)]*height(i,j).

All the 3 variables left, right, and height can be determined by the information from previous row,
 and also information from the current row. So it can be regarded as a DP solution. 
 The transition equations are:

left(i,j) = max(left(i-1,j), cur_left), cur_left can be determined from the current row
right(i,j) = min(right(i-1,j), cur_right), cur_right can be determined from the current row
height(i,j) = height(i-1,j) + 1, if matrix[i][j]=='1';
height(i,j) = 0, if matrix[i][j]=='0'
The code is as below. The loops can be combined for speed 
but I separate them for more clarity of the algorithm.      


If you think this algorithm is not easy to understand, you can try this example:

0 0 0 1 0 0 0 
0 0 1 1 1 0 0 
0 1 1 1 1 1 0
The vector "left" and "right" from row 0 to row 2 are as follows

row 0:

l: 0 0 0 3 0 0 0
r: 7 7 7 4 7 7 7
row 1:

l: 0 0 2 3 2 0 0
r: 7 7 5 4 5 7 7 
row 2:

l: 0 1 2 3 2 1 0
r: 7 6 5 4 5 6 7
The vector "left" is computing the left boundary. Take (i,j)=(1,3) for example. 
On current row 1, the left boundary is at j=2. However, because matrix[1][3] is 1, 
you need to consider the left boundary on previous row as well, which is 3. 
So the real left boundary at (1,3) is 3.

I hope this additional explanation makes things clearer.
	 */
	//2
    public static int maximalRectangle(char[][] matrix) {
    	if (matrix == null || matrix.length == 0) {
            return 0;
        }
        int m = matrix.length;
        int n = matrix[0].length;
        int[] left = new int[n];
        int[] right = new int[n];
        int[] height = new int[n];
        int res = 0;
        Arrays.fill(right, n);
        Arrays.fill(height, 0);
        for (int i = 0; i < m; i++) {
            int cur_left = 0, cur_right = n;
            for (int j = 0; j < n; j++) { // compute height (can do this from either side)
                if (matrix[i][j] == '1') {
                    height[j]++;
                } else {
                    height[j] = 0;
                }
            }
            for (int j = 0; j < n; j++) {// compute left (from left to right)
                if (matrix[i][j] == '1') {
                    left[j] = Math.max(left[j], cur_left);
                } else {
                    cur_left = j + 1;
                    left[j] = 0;
                }
            }
            for (int j = n - 1; j >= 0; j--) {// compute right (from right to left)
                if (matrix[i][j] == '1') {
                    right[j] = Math.min(right[j], cur_right);
                } else {
                    cur_right = j;
                    right[j] = n;
                }
            }
            for (int j = 0; j < n; j++) {// compute the area of rectangle (can do this from either side)
                res = Math.max(res, (right[j] - left[j])*height[j]);
            }
        }
        return res;
    }
    //九章
    public static int maximalRectangle1(char[][] matrix) {
        if (matrix.length < 1) return 0;
        int n = matrix.length;
        if (n == 0) return 0;
        int m = matrix[0].length;
        if (m == 0) return 0;
        int[][] height = new int[n][m];

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (i == 0)
                    height[i][j] = ((matrix[i][j] == '1') ? 1 : 0);
                else
                    height[i][j] += ((matrix[i][j] == '1') ? height[i-1][j] + 1 : 0);
            }
        }

        int answer = 0;
        for (int i = 0; i < n; ++i) {
            Stack<Integer> S = new Stack<Integer>();  
            for (int j = 0; j < m; j++) {
                while (!S.empty() && height[i][j] < height[i][S.peek()]) {
                    int pos = S.peek();
                    S.pop();
                    answer = Math.max(answer, height[i][pos]*(S.empty()? j : j-S.peek()-1));
                }
                S.push(j);
            }
            while (!S.empty()) {
                int pos = S.peek();
                S.pop();
                answer = Math.max(answer, height[i][pos]*(S.empty()? m : m-S.peek()-1));
            }
        }
        return answer;
    }
}
