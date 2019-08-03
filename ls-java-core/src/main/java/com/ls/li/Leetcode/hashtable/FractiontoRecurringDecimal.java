/**
 * 
 */
package com.ls.li.Leetcode.hashtable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishuai
 * @data 2016-12-29 下午3:44:08
 */

public class FractiontoRecurringDecimal {

	/**
	 * @author lishuai
	 * @data 2016-12-29 下午3:44:08
Given two integers representing the numerator and denominator of a fraction, 
return the fraction in string format.

If the fractional part is repeating, enclose the repeating part in parentheses.

For example,

Given numerator = 1, denominator = 2, return "0.5".
Given numerator = 2, denominator = 1, return "2".
Given numerator = 2, denominator = 3, return "0.(6)".
Hint:

1、No scary math, just apply elementary math knowledge. Still remember how to perform a long division?
2、Try a long division on 4/9, the repeating part is obvious. Now try 4/333. Do you see a pattern?
3、Be wary of edge cases! List out as many test cases as you can think of and test your code thoroughly.


1
6
Output:
"0.16666666666666666"
Expected:
"0.1(6)"
Stdout:
0.16666666666666666
	 */

	public static void main(String[] args) {
		System.out.println((1>0) ^ (2>3));
		
		// System.out.println(fractionToDecimal(4,333));

	}
	//2 和1思路类似
	public static String fractionToDecimal(int numerator, int denominator) {
	    if (denominator == 0)
	        return "NaN";
	    if (numerator == 0)
	        return "0";
	    StringBuilder result = new StringBuilder();
	    Long n = new Long(numerator);
	    Long d = new Long(denominator);
	    // negative or positive
	    if (n*d < 0)
	        result.append("-");
	    n = Math.abs(n);
	    d = Math.abs(d);
	    result.append(Long.toString(n / d));
	    // result is integer or float
	    if (n % d == 0)
	        return result.toString();
	    else
	        result.append(".");
	    // deal with the float part
	    // key is the remainder, value is the start positon of possible repeat numbers
	    HashMap<Long, Integer> map = new HashMap<Long, Integer>();
	    Long r = n % d; 
	    while (r > 0) {
	        if (map.containsKey(r)) {
	            result.insert(map.get(r), "(");
	            result.append(")");
	            break;
	        }
	        map.put(r, result.length());
	        r *= 10;
	        result.append(Long.toString(r / d));
	        r %= d;
	    }
	    return result.toString();
	}
	
	/**
The important thing is to consider all edge cases while thinking this problem through, including: 
negative integer, possible overflow, etc.

Use HashMap to store a remainder and its associated index while doing the division 
so that whenever a same remainder comes up, we know there is a repeating fractional part.

Please comment if you see something wrong or can be improved. Cheers!
	 */
	
	
	//1  根据手写除法运算的原理，出现重复的余数说明有重复的序列；使用map标记余数和此时的结果长度(为何把他们变成long型？？下面乘的时候有可能溢出？？)
	public static String fractionToDecimal1(int numerator, int denominator) {
		if (denominator == 0)
	        return "NaN";
        if (numerator == 0) {
            return "0";
        }
        StringBuilder res = new StringBuilder();
        // "+" or "-"
        res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
        long num = Math.abs((long)numerator);
        long den = Math.abs((long)denominator);
        
        // integral part
        res.append(num / den);
        num %= den;
        if (num == 0) {
            return res.toString();
        }
        
        // fractional part
        res.append(".");
        HashMap<Long, Integer> map = new HashMap<Long, Integer>();
        map.put(num, res.length());
        while (num != 0) {
            num *= 10;
            res.append(num / den);
            num %= den;
            if (map.containsKey(num)) {
                int index = map.get(num);
                res.insert(index, "(");
                res.append(")");
                break;
            } else {
                map.put(num, res.length());
            }
        }
        return res.toString();
    }
	
	
	//0
    public static String fractionToDecimal0(int numerator, int denominator) {
        if (denominator == 0) return "NaN";
        if (numerator == 0) return "0";
        StringBuilder res = new StringBuilder();
        if ((numerator > 0) ^ (denominator > 0)) res.append("-");
        Long n = new Long(numerator);
        Long d = new Long(denominator);
        n = Math.abs(n);
        d = Math.abs(d);
        res.append(n / d);
        n %= d;
        if (n == 0) return res.toString();
        res.append(".");
        Map<Long,Integer> map = new HashMap<>();
        map.put(n, res.length());
        while (n != 0) {
        	n *= 10;
        	res.append(n / d);
        	n %= d;
        	if (map.containsKey(n)) {
        		int index = map.get(n);
        		res.insert(index, "(");
        		res.append(")");
        		break;
        	} else map.put(n, res.length());
        }
        return res.toString();
    }
}
