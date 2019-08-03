/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-6 上午10:16:53
 */

public class PlusOne {

	/**
	 * @author lishuai
	 * @data 2016-12-6 上午10:16:53
Given a non-negative number represented as an array of digits, plus one to the number.

The digits are stored such that the most significant digit is at the head of the list. 高位在前，低位在后
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 The complexity is O(1)
     f(n) = 9/10 + 1/10 * O(n-1)
      ==>  O(n) =  10 / 9 = 1.1111 = O(1)
	 */	
    //2九章算法答案(觉得效率并不是很高)
    public static int[] plusOne(int[] digits) {
        int carries = 1;
        for(int i = digits.length-1; i>=0 && carries > 0; i--){  // fast break when carries equals zero
            int sum = digits[i] + carries;
            digits[i] = sum % 10;
            carries = sum / 10;
        }
        if(carries == 0)
            return digits;
            
        int[] rst = new int[digits.length+1];
        rst[0] = 1;
        for(int i=1; i< rst.length; i++){
            rst[i] = digits[i-1];
        }
        return rst;
    }
	//1 感觉和0的算法基本一致，但是简化代码
	public static int[] plusOne1(int[] digits) { 
		int n = digits.length;
	    for(int i = n-1; i >= 0; i--) {
	        if(digits[i] < 9) {
	            digits[i]++;
	            return digits;
	        }	        
	        digits[i] = 0;
	    }	    
	    int[] newNumber = new int [n+1];
	    newNumber[0] = 1;
	    
	    return newNumber;
	}
	//效率：N
	//0 思想：需要判断数组的最后一个和最高位那个是否为9，不为9的直接加1返回；是9的继续朝前找，知道找到第一个不是9的或者遍历完所有
	public static int[] plusOne0(int[] digits) {
		for(int i = digits.length-1;i >= 0;i--){
			if(i == 0){
				if(digits[0] == 9){
					digits[0] = 0;
					int[] b = new int[digits.length + 1];
					System.arraycopy(digits, 0, b, 1, digits.length);
					b[0] = 1;
					return b;
				}
			}						
			if(digits[i] != 9){
				digits[i] = digits[i] + 1;
				break;
			} else digits[i] = 0;			
		}
		return digits;
	}
}
