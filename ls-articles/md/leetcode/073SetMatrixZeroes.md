# 73. Set Matrix Zeroes


```java
/**
 *
 */
package array;

/**
 * @author lishuai
 * @data 2016-12-6 上午11:01:26
 */

public class SetMatrixZeroes {

     /**
      * @author lishuai
      * @data 2016-12-6 上午11:01:26
Given a m x n matrix, if an element is 0, set its entire row and column to 0. Do it in place.

Follow up:
Did you use extra space?
A straight forward solution using O(mn) space is probably a bad idea.
A simple improvement uses O(m + n) space, but still not the best solution.
Could you devise a constant space solution?

1 4 2
3 0 5
6 7 8
      */

     public static void main(String[] args) {
          int[][] a = {{1,4,2},{3,0,5},{6,7,8}};
     }

     //0
    public static void setZeroes0(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
          return;
        }
        int rows = matrix.length;
        int colums = matrix[0].length;
     boolean row0 = false;
     boolean colum0 = false;
     for (int i = 0; i < rows; i++) {
          if (matrix[i][0] == 0) {
              colum0 = true;
              break;
          }
     }
     for (int i = 0; i < colums; i++) {
          if (matrix[0][i] == 0) {
              row0 = true;
              break;
          }
     }
     for (int i = 1; i < rows; i++) {
          for (int j = 1; j < colums; j++) {
              if (matrix[i][j] == 0) {
                   matrix[0][j] = 0;
                   matrix[i][0] = 0;
              }
          }
     }
     for (int i = 1; i < rows; i++) {
          for (int j = 1; j < colums; j++) {
              if (matrix[0][j] == 0 || matrix[i][0] == 0) {
                   matrix[i][j] = 0;
              }
          }
     }
     if (row0) {
          for (int i = 0; i < colums; i++) {
              matrix[0][i] = 0;
          }
     }
     if (colum0) {
          for (int i = 0; i < rows; i++) {
              matrix[i][0] = 0;
          }
     }


    }

    //1 九章
    // using O(m+n) is easy, to enable O(1), we have to use the space within the matrix
    //思想：第一步，首先把第一行和第一列遍历看看是否有0，设置两个标志位；
    //第二步，然后从第二行第二列开始遍历，要是遇到一个0，把它对应的第一行和第一列元素设置为0，作为标志
    //第三步，再次从第二行第二列开始遍历，判断要是其对应的第一行或者第一列元素设置为0，就把该元素设置为0
    //第四步，根据第一步的标志判断是否需要对第一步进行处理
   public void setZeroes1(int[][] matrix) {
       if(matrix == null || matrix.length == 0)
           return;

       int rows = matrix.length;
       int cols = matrix[0].length;

       boolean empty_row0 = false;
       boolean empty_col0 = false;
       for(int i = 0; i < cols; i++){
           if(matrix[0][i] == 0){
               empty_row0 = true;
               break;
           }
       }

       for(int i = 0; i < rows; i++){
           if(matrix[i][0] == 0){
               empty_col0 = true;
               break;
           }
       }

       for(int i = 1; i < rows; i++) {
           for(int j =1; j<cols; j++){
               if(matrix[i][j] == 0){
                   matrix[0][j] = 0;
                   matrix[i][0] = 0;
               }
           }
       }

       for(int i = 1; i<rows; i++) {
           for (int j=1; j< cols; j++) {
               if(matrix[0][j] == 0 || matrix[i][0] == 0)
                   matrix[i][j] = 0;
           }
       }

       if(empty_row0){
           for(int i = 0; i < cols; i++){
               matrix[0][i] = 0;
           }
       }

       if(empty_col0){
           for(int i = 0; i < rows; i++){
               matrix[i][0] = 0;
           }
       }

   }
   /**
  My idea is simple: store states of each row in the first of that row,
  and store states of each column in the first of that column.
  Because the state of row0 and the state of column0 would occupy the same cell,
  I let it be the state of row0, and use another variable "col0" for column0.
  In the first phase, use matrix elements to set states in a top-down way.
  In the second phase, use states to set matrix elements in a bottom-up way.
1 4 2
3 0 5
6 7 8
    */
   //2 和1的思路类似简化代码(有点不太好理解)
   //单独处理第一列
   public void setZeroes(int[][] matrix) {
        int col0 = 1, rows = matrix.length, cols = matrix[0].length;

         for (int i = 0; i < rows; i++) {
             if (matrix[i][0] == 0) col0 = 0;
             for (int j = 1; j < cols; j++)
                 if (matrix[i][j] == 0)
                     matrix[i][0] = matrix[0][j] = 0;
         }

         for (int i = rows - 1; i >= 0; i--) {
             for (int j = cols - 1; j >= 1; j--)
                 if (matrix[i][0] == 0 || matrix[0][j] == 0)
                     matrix[i][j] = 0;
             if (col0 == 0) matrix[i][0] = 0;
         }
   }
}



```