/**
 * 
 */
package com.ls.li.Leetcode.bit;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author lishuai
 * @data 2017-1-4 下午2:19:02
 */

public class MaximumProductofWordLengths {

	/**
	 * @author lishuai
	 * @data 2017-1-4 下午2:19:02
Given a string array words, find the maximum value of length(word[i]) * length(word[j]) 
where the two words do not share common letters. 
You may assume that each word will contain only lower case letters. 
If no such two words exist, return 0.

Example 1:
Given ["abcw", "baz", "foo", "bar", "xtfn", "abcdef"]
Return 16
The two words can be "abcw", "xtfn".

Example 2:
Given ["a", "ab", "abc", "d", "cd", "bcd", "abcd"]
Return 4
The two words can be "ab", "cd".

Example 3:
Given ["a", "aa", "aaa", "aaaa"]
Return 0
No such pair of words.


["abc","dd"]   6
	 */

	public static void main(String[] args) {
		String[] s = {"a", "ab", "abc", "d", "cd", "bcd", "abcd"};
		System.out.println(maxProduct(s));
	} 
	//3   52ms和2类似
	 public static int maxProduct(String[] words) {
	        int max = 0;
	        Arrays.sort(words, new Comparator<String>(){
	            public int compare(String a, String b){
	                return b.length() - a.length();
	            }
	        });

	        int[] masks = new int[words.length]; // alphabet masks

	        for(int i = 0; i < masks.length; i++){
	            for(char c: words[i].toCharArray()){
	                masks[i] |= 1 << (c - 'a');
	            }
	        }
	    
	        for(int i = 0; i < masks.length; i++){
	            if(words[i].length() * words[i].length() <= max) break; //prunning
	            for(int j = i + 1; j < masks.length; j++){
	                if((masks[i] & masks[j]) == 0){
	                    max = Math.max(max, words[i].length() * words[j].length());
	                    break; //prunning
	                }
	            }
	        }

	        return max;
	    }
	
	
	//2    35ms 思路：使用一个int类型的数编码一个字符串
	public static int maxProduct2(String[] words) {
		if (words == null || words.length == 0)
			return 0;
		int len = words.length;
		int[] value = new int[len];
		for (int i = 0; i < len; i++) {
			String tmp = words[i];
			value[i] = 0;
			for (int j = 0; j < tmp.length(); j++) {
				value[i] |= 1 << (tmp.charAt(j) - 'a');
			}
		}
		int maxProduct = 0;
		for (int i = 0; i < len; i++)
			for (int j = i + 1; j < len; j++) {
				if ((value[i] & value[j]) == 0 && (words[i].length() * words[j].length() > maxProduct))
					maxProduct = words[i].length() * words[j].length();
			}
		return maxProduct;
	}
	
	
	//1 最原始的的方法 时间复杂度N*N   236ms
    public static int maxProduct1(String[] words) {
    	if (words == null || words.length < 2) return 0;
    	int max = 0;
    	for (int i = 0; i < words.length - 1; i++) {
    		for (int j = i + 1; j < words.length; j++) {
    			if (commonLetters(words[i],words[j])) {
    				max = Math.max(max, words[i].length() * words[j].length());
    			}
    		}
    	}
        return max;
    }
    //判断两分字符串是否有重复元素
    public static boolean commonLetters(String s1, String s2) {
    	int[] dp = new int[26];
    	for (int i = 0; i < s1.length(); i++) {
    		dp[s1.charAt(i) - 'a']++;
    	}
    	for (int i = 0; i < s2.length(); i++) {
    		if (dp[s2.charAt(i) - 'a'] != 0) return false;
    	}
    	return true;
    }
}
