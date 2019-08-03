/**
 * 
 */
package com.ls.li.Leetcode.string;

/**
 * @author lishuai
 * @data 2016-12-15 上午9:23:41
 */

public class CountandSay {

	/**
	 * @author lishuai
	 * @data 2016-12-15 上午9:23:41
The count-and-say sequence is the sequence of integers beginning as follows:
1, 11, 21, 1211, 111221, ...

1 is read off as "one 1" or 11.
11 is read off as "two 1s" or 21.
21 is read off as "one 2, then one 1" or 1211.
Given an integer n, generate the nth sequence.

Note: The sequence of integers will be represented as a string.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	

	 public String countAndSay(int n) {
	       
			return "";
	    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 public String countAndSay0(int n) {
	        //在尾部加*可用于统计连续有多少个相同的元素
	        if(n<=0) return "";
			if(n==1) return "1";
			String s=countAndSay(n-1)+"*";//为了str末尾的标记，方便循环读数
			String back="";
			int count=1;
			for(int i=0;i<s.length()-1;i++){
				if(s.charAt(i)==s.charAt(i+1)){
					count++;//计数增加
				}else{
					back=back+count+s.charAt(i);//上面的*标记这里方便统一处理
					count=1;//初始化
				}
			}
			return back;
	    }
}
