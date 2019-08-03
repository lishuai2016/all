/**
 * 
 */
package com.ls.li.Leetcode.math;

/**
 * @author lishuai
 * @data 2017-1-10 上午10:03:16
 */

public class DivideTwoIntegers {

	/**
	 * @author lishuai
	 * @data 2017-1-10 上午10:03:16
Divide two integers without using multiplication, division and mod operator.

If it is overflow, return MAX_INT.

3/2
4/3


-2147483648
-1

2147483647
	 */

	public static void main(String[] args) {
		String s = "41172319920505857X";
		System.out.println(s.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$"));
		
		//System.out.println(divide(8, 3));
	}
	//2
	 public static int divide(int dividend, int divisor) {
	        if (divisor == 0) {
	             return dividend >= 0? Integer.MAX_VALUE : Integer.MIN_VALUE;
	        }
	        
	        if (dividend == 0) {
	        	
	            return 0;
	        }
	        
	        if (dividend == Integer.MIN_VALUE && divisor == -1) {
	            return Integer.MAX_VALUE;
	        }
	        
	        boolean isNegative = (dividend < 0 && divisor > 0) || (dividend > 0 && divisor < 0);
	                             
	        long a = Math.abs((long)dividend);
	        long b = Math.abs((long)divisor);
	        int result = 0;
	        while(a >= b) {
	            int shift = 0;
	            while(a >= (b << shift)) {
	                shift++;
	            }
	            a -= b << (shift - 1);
	            result += 1 << (shift - 1);
	        }
	        return isNegative ? -result: result;
	    }
	//1
    public static int divide1(int dividend, int divisor) {
    	if (divisor == 0) {
    		return Integer.MAX_VALUE;
    	}
    	if (dividend == 0) {
    		return 0;
    	}
    	int sign = dividend * divisor > 0 ? 1 : -1;
    	dividend = Math.abs(dividend);
    	divisor = Math.abs(divisor);
    	int res = 0;
        while (dividend >= divisor) {
        	dividend -= divisor;
        	res++;
        } 	
    	return sign * res;
    }
}
