/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishuai
 * @data 2016-12-9 上午9:22:46
 */

public class RansomNote {

	/**
	 * @author lishuai
	 * @data 2016-12-9 上午9:22:46
Given an arbitrary ransom note string and another string containing letters from all the magazines, 
write a function that will return true if the ransom note can be constructed from the magazines ;
 otherwise, it will return false.

Each letter in the magazine string can only be used once in your ransom note.

Note:
You may assume that both strings contain only lowercase letters.

canConstruct("a", "b") -> false
canConstruct("aa", "ab") -> false
canConstruct("aa", "aab") -> true
	 */

	public static void main(String[] args) {
		
		System.out.println(canConstruct("aa", "aab"));

	}
	//2 思路：通过一个26长度的数组来统计各个字符的数量
	public static boolean canConstruct(String ransomNote, String magazine) {
		int[] m = new int[26];
		for (int i = 0;i < magazine.length();i++) m[magazine.charAt(i) - 'a']++;
		for (int i = 0;i < ransomNote.length();i++) if (--m[ransomNote.charAt(i) - 'a'] < 0) return false;
		return true;
	}
	
	
	//1 思路：根据StringBuffer的特性，可以根据字符的索引删除元素
	public static boolean canConstruct1(String ransomNote, String magazine) {
		if (ransomNote.length() == 0) return true;
		if (magazine.length() == 0) return false;
		if(magazine.length() < ransomNote.length()) return false;
		StringBuffer s = new StringBuffer(magazine);
		for (int i = 0;i < ransomNote.length();i++) {			
			int index = s.toString().indexOf(ransomNote.charAt(i));
			if (index == -1) return false;
			s.deleteCharAt(index);						
		}
		return true;
	}
	//0 思路：用map分别统计两个字符串中各个字符的数量，然后比较字符的个数来判断
	public boolean canConstruct0(String ransomNote, String magazine) {
        if(magazine.length()<ransomNote.length()){
			 return false;
		 }else{
			 if(magazine.contains(ransomNote)){
				 return true;
			 }
			 char[] c1=magazine.toCharArray();
			 Map<String, Integer> m1=new HashMap<String, Integer>();
			 for(int i=0;i<c1.length;i++){
				 if( m1.containsKey(""+c1[i])){
					 Integer v=m1.get(""+c1[i]);
					 m1.put(""+c1[i], v+1);
					 
				 }else{
					 m1.put(""+c1[i], 1);
				 }				
				
			 }
			 char[] c2=ransomNote.toCharArray();
			 Map<String, Integer> m2=new HashMap<String, Integer>();
			 for(int i=0;i<c2.length;i++){
				 if( m2.containsKey(""+c2[i])){
					 Integer v=m2.get(""+c2[i]);
					 m2.put(""+c2[i], v+1);
					 
				 }else{
					 m2.put(""+c2[i], 1);
				 }				
				
			 }
			 
			 for(String s:m2.keySet()){
				 if(m1.containsKey(s)){
					if(m1.get(s)<m2.get(s)){
						return false;
					}
				 }else{
					 return false;
				 }
				
				
			 }
			 return true;
		 }
   }
}
