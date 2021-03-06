/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-14 下午3:23:44
 */

public class ValidParentheses {

	/**
	 * @author lishuai
	 * @data 2016-12-14 下午3:23:44
Given a string containing just the characters '(', ')', '{', '}', '[' and ']', 
determine if the input string is valid.

The brackets must close in the correct order, "()" and "()[]{}" are all valid but "(]" and "([)]" are not.
	 */

	public static void main(String[] args) {
		System.out.println(isValid1("("));

	}
	//3九章    和1，2思想一样
	 public static boolean isValidParentheses(String s) {
	        Stack<Character> stack = new Stack<Character>();
	        for (Character c : s.toCharArray()) {
		    if ("({[".contains(String.valueOf(c))) {
	                stack.push(c);
	            } else {
	               if (!stack.isEmpty() && is_valid(stack.peek(), c)) {
	                   stack.pop();
	               } else {
	                   return false;
	               }
	           }
	       }
	       return stack.isEmpty();
	    }

	    private static boolean is_valid(char c1, char c2) {
	        return (c1 == '(' && c2 == ')') || (c1 == '{' && c2 == '}')
	            || (c1 == '[' && c2 == ']');
	    }
	
	//2 和1一样优化代码
	public static boolean isValid2(String s) {
	        Stack<Character> stack = new Stack<Character>();
	        // Iterate through string until empty
	        for(int i = 0; i<s.length(); i++) {
	            // Push any open parentheses onto stack
	            if(s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{')
	                stack.push(s.charAt(i));
	            // Check stack for corresponding closing parentheses, false if not valid
	            else if(s.charAt(i) == ')' && !stack.empty() && stack.peek() == '(')
	                stack.pop();
	            else if(s.charAt(i) == ']' && !stack.empty() && stack.peek() == '[')
	                stack.pop();
	            else if(s.charAt(i) == '}' && !stack.empty() && stack.peek() == '{')
	                stack.pop();
	            else
	                return false;
	        }
	        // return true if no open parentheses left in stack
	        return stack.empty();
	    }
	
	//1 使用栈实现
	public static boolean isValid1(String s) {
		if (s == null || s.length() == 0) return true;
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0;i < s.length();i++) {
			if (s.charAt(i) == '(' || s.charAt(i) == '{' ||s.charAt(i) == '[') {
				stack.push(s.charAt(i));
			} else if (s.charAt(i) == ')') {
				if (stack.size() > 0 && (char)stack.pop() == '(') continue;
				else return false;
			} else if (s.charAt(i) == '}') {
				if (stack.size() > 0 && (char)stack.pop() == '{') continue;
				else return false;
			} else if (s.charAt(i) == ']') {
				if (stack.size() > 0 && (char)stack.pop() == '[') continue;
				else return false;
			} 
		}
//		if (stack.size() != 0) return false;
//		else return true;
		return stack.empty();
	}
	
	//0 用list来模拟栈的效果
	public static boolean isValid0(String s) {
//         这一题是典型的使用压栈的方式解决的问题，题目中还有一种valid情况没有说明，需要我们自己考虑的，就是"({[]})"这种层层嵌套但
// 可以完全匹配的，也是valid的一种。解题思路是这样的：我们对字符串S中的每一个字符C，如果C不是右括号，就压入栈stack中。
// 如果C是右括号，判断stack是不是空的，空则说明没有左括号，直接返回not valid，非空就取出栈顶的字符pre来对比，如果是匹配
// 的，就弹出栈顶的字符，继续取S中的下一个字符；如果不匹配，说明不是valid的，直接返回。当我们遍历了一次字符串S后，注意
// 这里还有一种情况，就是stack中还有残留的字符没有得到匹配，即此时stack不是空的，这时说明S不是valid的，因为只要valid，一
// 定全都可以得到匹配使左括号弹出。
        if(s.length()%2!=0) return false;
    	List<Character> list=new ArrayList<Character>();
    	
	    for(int i=0;i<s.length();i++){
	    		if(s.charAt(i)=='('||s.charAt(i)=='['||s.charAt(i)=='{'){    			
	    			list.add(0, s.charAt(i)); 
	    		}else{
	    			if(list.isEmpty()) return false;
	    			if(s.charAt(i)==')'&&list.get(0)!='('){
	        			return false;
	        		}else if(s.charAt(i)==']'&&list.get(0)!='['){
	        			return false;
	        		}else if(s.charAt(i)=='}'&&list.get(0)!='{'){
	        			return false;
	        		}else{
	        			list.remove(0);
	        		}  			
	    		}
	    	}
	     if(!list.isEmpty()) return false;
	    	return true;
	 }
}
