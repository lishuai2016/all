/**
 * 
 */
package com.ls.li.Leetcode.hashtable;

import java.util.Arrays;

/**
 * @author lishuai
 * @data 2016-12-29 上午11:11:16
 */

public class ValidSudoku {

	/**
	 * @author lishuai
	 * @data 2016-12-29 上午11:11:16
The Sudoku board could be partially filled, where empty cells are filled with the character '.'.

Note:
A valid Sudoku board (partially filled) is not necessarily solvable. 
Only the filled cells need to be validated.
	 */

	public static void main(String[] args) {
		

	}
    public boolean isValidSudoku(char[][] board) {
        
    	
    	
    	return true;
    }
    //1 九章
    public boolean isValidSudoku1(char[][] board) {
        boolean[] visited = new boolean[9];
        
        // row
        for(int i = 0; i<9; i++){
            Arrays.fill(visited, false);
            for(int j = 0; j<9; j++){
                if(!process(visited, board[i][j]))
                    return false;
            }
        }
        
        //col
        for(int i = 0; i<9; i++){
            Arrays.fill(visited, false);
            for(int j = 0; j<9; j++){
                if(!process(visited, board[j][i]))
                    return false;
            }
        }
        
        // sub matrix
        for(int i = 0; i<9; i+= 3){
            for(int j = 0; j<9; j+= 3){
                Arrays.fill(visited, false);
                for(int k = 0; k<9; k++){
                    if(!process(visited, board[i + k/3][ j + k%3]))
                    return false;                   
                }
            }
        }
        return true;
        
    }
    
    private boolean process(boolean[] visited, char digit){
        if(digit == '.'){
            return true;
        }
        
        int num = digit - '0';
        if ( num < 1 || num > 9 || visited[num-1]){
            return false;
        }
        
        visited[num-1] = true;
        return true;
    }
    
}
