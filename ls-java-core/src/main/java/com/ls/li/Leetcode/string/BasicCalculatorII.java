/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.Date;
import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-9 下午2:21:40
 */

public class BasicCalculatorII {

	/**
	 * @author lishuai
	 * @data 2016-12-9 下午2:21:40
Implement a basic calculator to evaluate a simple expression string.

The expression string contains only non-negative integers, +, -, *, / operators 
and empty spaces . The integer division should truncate toward zero.

You may assume that the given expression is always valid.

Some examples:
"3+2*2" = 7
" 3/2 " = 1
" 3+5 / 2 " = 5
Note: Do not use the eval built-in library function.
	 */

	public static void main(String[] args) {
		//System.out.println(calculate1("3-2*2"));
		System.out.println(new Date());


	}
	//3
	public static int calculate3(String s) {
        Stack<Integer> stack = new Stack<>();
        int result = 0;
        for (int i = 0, num = 0, op = '+'; i < s.length(); i++) {   // op is last operator
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }
            if ("+-*/".indexOf(c) >= 0 || i == s.length() - 1) {    // must be 'if' or i=len-1 won't reach here
                if ("*/".indexOf(op) >= 0)                          // subtract top before mul/div
                    result -= stack.peek();
                switch (op) {
                    case '+': stack.push(num); break;
                    case '-': stack.push(-num); break;
                    case '*': stack.push(stack.pop() * num); break; // only non-negative int, impossible '2*-1'
                    case '/': stack.push(stack.pop() / num); break;
                }
                num = 0;
                op = c;
                result += stack.peek();
            } /* else whitespace */
        }
        return result;
    }
	
	
	//2
	public static int calculate2(String s) {
	    Stack<Integer> stack = new Stack<>();
	    int num = 0, res = 0;
	    char sign = '+';
	    char[] sArray = s.toCharArray();
	    for(int i=0; i<sArray.length; i++) {
	        char c = sArray[i];
	        if(c >= '0' && c <= '9') {
	            if(10 * num + (c - '0') > Integer.MAX_VALUE) num = Integer.MAX_VALUE;
	            else num = 10 * num + (c - '0');
	        }
	        
	        if(c == '+' || c == '-' || c == '*' || c == '/' || i == sArray.length-1) {
	            if(sign == '+' || sign == '-') {
	                int tmp = sign == '+' ? num : -num;
	                stack.push(tmp);
	                res += tmp;
	            } else {
	                res -= stack.peek();
	                int tmp = sign == '*' ? stack.pop() * num : stack.pop() / num;
	                stack.push(tmp);
	                res += tmp;
	            }
	            sign = c;
	            num = 0;
	        }
	    }
	    return res;
	}
	//1 栈实现 
	public static int calculate1(String s) {
	    int len;
	    if(s==null || (len = s.length())==0) return 0;
	    Stack<Integer> stack = new Stack<Integer>();
	    int num = 0;
	    char sign = '+';
	    for(int i=0;i<len;i++){
	        if(Character.isDigit(s.charAt(i))){
	            num = num*10+s.charAt(i)-'0';
	        }
	        if((!Character.isDigit(s.charAt(i)) &&' '!=s.charAt(i)) || i==len-1){
	            if(sign=='-'){
	                stack.push(-num);
	            }
	            if(sign=='+'){
	                stack.push(num);
	            }
	            if(sign=='*'){
	                stack.push(stack.pop()*num);
	            }
	            if(sign=='/'){
	                stack.push(stack.pop()/num);
	            }
	            sign = s.charAt(i);
	            num = 0;
	        }
	    }

	    int re = 0;
	    for(int i:stack){
	        re += i;
	    }
	    return re;
	}
	
	//0 自己默写
    public static int calculate0(String s) {
    	if (s == null || s.length() == 0) return 0;
    	int num = 0;
    	char sign = '+';
    	Stack<Integer> stack = new Stack<Integer>();
    	for (int i = 0;i < s.length();i++) {
    		if (Character.isDigit(s.charAt(i))) num = num * 10 + s.charAt(i) -'0';
    		if ((!Character.isDigit(s.charAt(i)) &&' '!=s.charAt(i))|| i == s.length() - 1) {
    			if (sign == '-') stack.push(-num);
    			if (sign == '+') stack.push(num);
    			if (sign == '*') stack.push(stack.pop() * num);
    			if (sign == '/') stack.push(stack.pop() / num);
    			sign = s.charAt(i);
	            num = 0;
    		}   		
    	}
    	int re = 0;
	    for(int i:stack){
	        re += i;
	    }
	    return re;
    }
}
