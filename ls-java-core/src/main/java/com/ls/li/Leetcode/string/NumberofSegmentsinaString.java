/**
 * 
 */
package com.ls.li.Leetcode.string;

/**
 * @author lishuai
 * @data 2016-12-8 下午4:37:13
 */

public class NumberofSegmentsinaString {

	/**
	 * @author lishuai
	 * @data 2016-12-8 下午4:37:13
Count the number of segments in a string, 
where a segment is defined to be a contiguous sequence of non-space characters.

Please note that the string does not contain any non-printable characters.

Example:

Input: "Hello, my name is John"
Output: 5
	 */

	public static void main(String[] args) {
//		String[] s =  "     foo    bar".split("\\s+");
//		System.out.println(s.length);
		System.out.println(countSegments(""));

	}
	//3 和1思想一样，但是更简洁
	public static int countSegments(String s) {
	    String trimmed = s.trim();
	    if (trimmed.length() == 0) return 0;
	    else return trimmed.split("\\s+").length;
	}
	//2 一位一位的判断，根据当前位不是空格而前一位是空格来表示一个片段
	public static int countSegments2(String s) {
	    int res=0;
	    for(int i=0; i<s.length(); i++)
	        if(s.charAt(i)!=' ' && (i==0 || s.charAt(i-1)==' '))
	            res++;        
	    return res;
	}
	//1 使用正则表达式表示一个或者多个空格，分隔字符串，然后计算长度
    public static int countSegments1(String s) {
    	if (s == null ||s.equals("") || s.equals("\\s+")) return 0;
    	if (s.startsWith(" ")) {
    		if (s.split("\\s+").length > 0) return s.split("\\s+").length - 1;
    		else return 0;
    	} else return s.split("\\s+").length;
    }
}
