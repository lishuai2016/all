/**
 * 
 */
package com.ls.li.Leetcode.string;

/**
 * @author lishuai
 * @data 2016-12-9 上午11:08:21
 */

public class ReverseString {

	/**
	 * @author lishuai
	 * @data 2016-12-9 上午11:08:21
Write a function that takes a string as input and returns the string reversed.

Example:
Given s = "hello", return "olleh".
	 */

	public static void main(String[] args) {
		System.out.println(reverseString("hello"));

	}
	//1 二指针和0类似
    public static String reverseString(String s) {
    	if (s == null || s.length() ==0) return new String();
    	int start = 0;
    	int end = s.length() - 1;
    	char[] c = s.toCharArray();
    	while (start < end) {
    		char temp = c[start];
    		c[start] = c[end];
    		c[end] = temp;
    		start++;
    		end--;
    	}
    	//return new String(c);
    	return String.valueOf(c);
    }
    
    //0 
    public String reverseString0(String s) {
        char [] s1 = s.toCharArray();
 		for(int i = 0;i<(s1.length/2);i++){
 			char c = s1[i];
 			s1[i]=s1[s1.length-1-i];
 			s1[s1.length-1-i]=c;
 		}		

// 		StringBuffer sb = new StringBuffer();
// 		for(int j = 0;j<=s1.length-1;j++){
// 			sb.append(s1[j]);
// 		}
// 		
// 		return sb.toString();
		return String.valueOf(s1);
         
     }
}
