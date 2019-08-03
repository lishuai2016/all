/**
 * 
 */
package com.ls.li.Leetcode.string;

/**
 * @author lishuai
 * @data 2016-12-14 上午11:27:20
 */

public class StringtoInteger {

	/**
	 * @author lishuai
	 * @data 2016-12-14 上午11:27:20
Implement atoi to convert a string to an integer.

Hint: Carefully consider all possible input cases. 
If you want a challenge, please do not see below and ask yourself what are the possible input cases.

Notes: 
It is intended for this problem to be specified vaguely (ie, no given input specs). 
You are responsible to gather all the input requirements up front.

Requirements for atoi:
The function first discards as many whitespace characters as necessary 
until the first non-whitespace character is found. 
Then, starting from this character, takes an optional initial plus 
or minus sign followed by as many numerical digits as possible, and interprets them as a numerical value.

The string can contain additional characters after those that form the integral number, 
which are ignored and have no effect on the behavior of this function.

If the first sequence of non-whitespace characters in str is not a valid integral number, 
or if no such sequence exists because either str is empty or it contains only whitespace characters,
 no conversion is performed.

If no valid conversion could be performed, a zero value is returned. 
If the correct value is out of the range of representable values, 
INT_MAX (2147483647) or INT_MIN (-2147483648) is returned.
	 */

	public static void main(String[] args) {
		System.out.println(myAtoi("   118100977011111111111111111111111111111"));

	}
	//九章   3 和1、2差不多，它采用long型变量保存中间变量
	public static int myAtoi(String str) {
        // Start typing your Java solution below
        // DO NOT write main() function
        if(str == null) {
            return 0;
        }
        str = str.trim();
        if (str.length() == 0) {
            return 0;
        }
            
        int sign = 1;
        int index = 0;
    
        if (str.charAt(index) == '+') {
            index++;
        } else if (str.charAt(index) == '-') {
            sign = -1;
            index++;
        }
        long num = 0;
        for (; index < str.length(); index++) {
            if (str.charAt(index) < '0' || str.charAt(index) > '9')
                break;
            num = num * 10 + (str.charAt(index) - '0');
            if (num > Integer.MAX_VALUE ) {
                break;
            }
        }   
        if (num * sign >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (num * sign <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)num * sign;
    }
	//2 原理和1差不多，但是简化代码（从前拼）
	public static int myAtoi2(String str) {
	    int index = 0, sign = 1, total = 0;
	    //1. Empty string
	    if(str.length() == 0) return 0;
	    //2. Remove Spaces
	    while(str.charAt(index) == ' ' && index < str.length())
	        index ++;
	    //3. Handle signs
	    if(str.charAt(index) == '+' || str.charAt(index) == '-'){
	        sign = str.charAt(index) == '+' ? 1 : -1;
	        index ++;
	    }	    
	    //4. Convert number and avoid overflow
	    while(index < str.length()){
	        int digit = str.charAt(index) - '0';
	        if(digit < 0 || digit > 9) break;
	        //check if total will be overflow after 10 times and add digit
	        if(Integer.MAX_VALUE/10 < total || Integer.MAX_VALUE/10 == total && Integer.MAX_VALUE %10 < digit)
	            return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
	        total = 10 * total + digit;
	        index ++;
	    }
	    return total * sign;
	}
	
	//1 思路：截取前面连续的数字组合，遇到非数字结束；然后判断数的大小是否超出（从截取的字符串后面拼数字）
    public static int myAtoi1(String str) {
    	if (str == null || str.length() == 0 || str.trim().length() == 0) return 0;
    	int res = 0;
    	String trim = str.trim();
    	char c = trim.charAt(0);
    	//判断第一个字符是否为+-号
    	if (c == '+') {    		
    		int end = 0;
    		for (int i = 1;i < trim.length();i++) {
        		if(!Character.isDigit(trim.charAt(i))) break;
        		else end = i;
        	}
            if (end == 0) return 0;  
            if (end > 10) return 2147483647;
            int mul = 1;
            String value = trim.substring(1, end + 1);          
            for (int i = value.length() - 1;i >=0;i--) {           	
            	int digit = Character.getNumericValue(value.charAt(i));
            	if (res > 147483647 && digit >= 2) return 2147483647;
            	res += digit * mul;
            	mul *=10;
            }    	    		
    	} else if (c == '-') {
    		int end = 0;
    		for (int i = 1;i < trim.length();i++) {
        		if(!Character.isDigit(trim.charAt(i))) break;
        		else end = i;
        	}
            if (end == 0) return 0; 
            if (end > 10) return -2147483648;
            int mul = 1;
            String value = trim.substring(1, end + 1); 
            
            for (int i = value.length() - 1;i >=0;i--) {           	
            	int digit = Character.getNumericValue(value.charAt(i));
            	if (res > 147483648 && digit >= 2) return -2147483648;
            	res += digit * mul;
            	mul *=10;
            }    	    		
    	} else {
    		int end = -1;    	
        	for (int i = 0;i < trim.length();i++) {
        		if(!Character.isDigit(trim.charAt(i))) break;
        		else end = i;
        	}
            if (end == -1) return 0; 
            if (end + 1 > 10) return 2147483647;
            int mul = 1;
            String value = trim.substring(0, end + 1); 
            for (int i = value.length() - 1;i >=0;i--) {           	
            	int digit = Character.getNumericValue(value.charAt(i));
            	if (res > 147483647 && digit >= 2) return 2147483647;
            	res += digit * mul;
            	mul *=10;
            }    	
    	}
    	if (c == '-') return res * -1;
    	else return res;
    }

}
