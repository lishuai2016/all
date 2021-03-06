/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2017-1-3 上午9:52:54
 */

public class RotateImage {

	/**
	 * @author lishuai
	 * @data 2017-1-3 上午9:52:54
You are given an n x n 2D matrix representing an image.

Rotate the image by 90 degrees (clockwise).

Follow up:
Could you do this in-place?

123
456
789

741
852
963

	 */
	/*
	 * clockwise rotate
	 * first reverse up to down, then swap the symmetry 
	 * 1 2 3     7 8 9     7 4 1
	 * 4 5 6  => 4 5 6  => 8 5 2
	 * 7 8 9     1 2 3     9 6 3
	*/
//	void rotate(vector<vector<int> > &matrix) {
//	    reverse(matrix.begin(), matrix.end());
//	    for (int i = 0; i < matrix.size(); ++i) {
//	        for (int j = i + 1; j < matrix[i].size(); ++j)
//	            swap(matrix[i][j], matrix[j][i]);
//	    }
//	}

	/*
	 * anticlockwise rotate
	 * first reverse left to right, then swap the symmetry
	 * 1 2 3     3 2 1     3 6 9
	 * 4 5 6  => 6 5 4  => 2 5 8
	 * 7 8 9     9 8 7     1 4 7
	*/
//	void anti_rotate(vector<vector<int> > &matrix) {
//	    for (auto vi : matrix) reverse(vi.begin(), vi.end());
//	    for (int i = 0; i < matrix.size(); ++i) {
//	        for (int j = i + 1; j < matrix[i].size(); ++j)
//	            swap(matrix[i][j], matrix[j][i]);
//	    }
//	}
	
	
	public static void main(String[] args) {
		 int[][] a = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
	     rotate2(a);
	     System.out.println(a[1].length);


	}
	//0
    public static void rotate0(int[][] matrix) {
    	if (matrix == null || matrix.length == 0) {
    		return;
    	}
        for (int i = 0; i < matrix.length / 2; i++) {
        	int[] temp = matrix[i];
        	matrix[i] = matrix[ matrix.length - 1 - i];
        	matrix[ matrix.length - 1 - i] = temp;
        }
        for (int i = 0; i < matrix.length; i++) {
        	for (int j = i + 1; j < matrix[0].length; j++) {
        		int temp = matrix[i][j];
        		matrix[i][j] = matrix[j][i];
        		matrix[j][i] = temp;
        	}
        }
    }
 
    //3 这个答案觉得不错  2ms
    public static void rotate(int[][] matrix) {
        for(int i = 0; i < matrix.length / 2; i++){
            int[] temp = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }

        for(int i = 0; i < matrix.length; i++){
            for(int j = i+1; j < matrix[i].length; j++){
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }
    /**
    123   1 2 3 4            13 9 5 1
    456   5 6 7 8			14 10 6 2
    789	  9 10 11 12		15 11 7 3
    	  13 14 15 16		16 12 8 4

    741
    852
    963
         */
    //2九章答案(不太好理解) 直接交换 2ms
    public static void rotate2(int[][] matrix) {
           if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
               return;
           }
           int length = matrix.length;

           for (int i = 0; i < length / 2; i++) {
               for (int j = 0; j < (length + 1) / 2; j++) {
                   int tmp = matrix[i][j];//1
                   matrix[i][j] = matrix[length - j - 1][i];// 7
                   matrix[length -j - 1][i] = matrix[length - i - 1][length - j - 1];//9
                   matrix[length - i - 1][length - j - 1] = matrix[j][length - i - 1];//3
                   matrix[j][length - i - 1] = tmp;//1
               }
           }
       }

   // 1最原始的方法 时间复杂度N*N。
   //思路：观察旋转九十度的规律，旋转之前的第一行变成最后一列
   public static void rotate1(int[][] matrix) {
       int[][] temp = new int[matrix.length][matrix[0].length];
       for (int i = 0;i < matrix.length;i++) {
           for (int j = 0;j < matrix.length;j++) {
               temp[i][j] = matrix[i][j];
           }
       }
       for (int i = 0;i < matrix.length;i++) {
           for (int j = 0;j < matrix.length;j++) {
               matrix[i][j] = temp[matrix.length - 1 - j][i];
           }
       }
   }

}
