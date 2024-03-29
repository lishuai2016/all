/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-15 上午9:11:32
 */

public class SubstringwithConcatenationofAllWords {

	/**
	 * @author lishuai
	 * @data 2016-12-15 上午9:11:32
You are given a string, s, and a list of words, words, 
that are all of the same length. 
Find all starting indices of substring(s) in s 
that is a concatenation of each word in words exactly once and without any intervening characters.

For example, given:
s: "barfoothefoobarman"
words: ["foo", "bar"]

You should return the indices: [0,9].
(order does not matter).

题意：把给的单词在拼接在一起在s中出现的索引

考虑：单词可重复
s
barfoobarfoo"
["foo","bar"]
[0,3,6]

	 */

	public static void main(String[] args) {
		String[] s = {"foo", "bar"};
		System.out.println(findSubstring("barfoothefoobarman",s));

	}
	//0
    public List<Integer> findSubstring0(String s, String[] words) {
    	List<Integer> res = new ArrayList<Integer>();
    	if (s == null || words == null || s.length() == 0 || words.length == 0|| s.length() < words[0].length() * words.length) return res;
    	Map<String, Integer> toFind = new HashMap<String, Integer>();     
        int m = words.length, n = words[0].length();
        for (int i = 0; i < m; i ++){
             if (!toFind.containsKey(words[i])) toFind.put(words[i], 1);
             else toFind.put(words[i], toFind.get(words[i]) + 1);             
        }
    	for (int i = 0;i < s.length();i++) {
    		int k = 0;
    		String temp = s.substring(i,i + n);
    		if (toFind.containsKey(temp)) {
    			while (k < m) {
    				
    			}
    		}
    	}
        
        
    	return res;
    }
    //3通过（有点复杂暂时没理解）
    public static List<Integer> findSubstring(String s, String[] words) {
    	int N = s.length();
    	List<Integer> indexes = new ArrayList<Integer>(s.length());
    	if (words.length == 0) {
    		return indexes;
    	}
    	int M = words[0].length();
    	if (N < M * words.length) {
    		return indexes;
    	}
    	int last = N - M + 1;
    	
    	//map each string in words array to some index and compute target counters
    	Map<String, Integer> mapping = new HashMap<String, Integer>(words.length);
    	int [][] table = new int[2][words.length];
    	int failures = 0, index = 0;
    	for (int i = 0; i < words.length; ++i) {
    		Integer mapped = mapping.get(words[i]);
    		if (mapped == null) {
    			++failures;
    			mapping.put(words[i], index);
    			mapped = index++;
    		}
    		++table[0][mapped];
    	}
    	
    	//find all occurrences at string S and map them to their current integer, -1 means no such string is in words array
    	int [] smapping = new int[last];
    	for (int i = 0; i < last; ++i) {
    		String section = s.substring(i, i + M);
    		Integer mapped = mapping.get(section);
    		if (mapped == null) {
    			smapping[i] = -1;
    		} else {
    			smapping[i] = mapped;
    		}
    	}
    	
    	//fix the number of linear scans
    	for (int i = 0; i < M; ++i) {
    		//reset scan variables
    		int currentFailures = failures; //number of current mismatches
    		int left = i, right = i;
    		Arrays.fill(table[1], 0);
    		//here, simple solve the minimum-window-substring problem
    		while (right < last) {
    			while (currentFailures > 0 && right < last) {
    				int target = smapping[right];
    				if (target != -1 && ++table[1][target] == table[0][target]) {
    					--currentFailures;
    				}
    				right += M;
    			}
    			while (currentFailures == 0 && left < right) {
    				int target = smapping[left];
    				if (target != -1 && --table[1][target] == table[0][target] - 1) {
    					int length = right - left;
    					//instead of checking every window, we know exactly the length we want
    					if ((length / M) ==  words.length) {
    						indexes.add(left);
    					}
    					++currentFailures;
    				}
    				left += M;
    			}
    		}
    		
    	}
    	return indexes;
    }
    //2 Time Limit Exceeded类似于1
    public static List<Integer> findSubstring2(String S, String[] L) {
        List<Integer> res = new ArrayList<Integer>();
        if (S == null || L == null || L.length == 0) return res;
        int len = L[0].length(); // length of each word
        
        Map<String, Integer> map = new HashMap<String, Integer>(); // map for L
        for (String w : L) map.put(w, map.containsKey(w) ? map.get(w) + 1 : 1);
        
        for (int i = 0; i <= S.length() - len * L.length; i++) {
            Map<String, Integer> copy = new HashMap<String, Integer>(map);
            for (int j = 0; j < L.length; j++) { // checkc if match
                String str = S.substring(i + j*len, i + j*len + len); // next word
                if (copy.containsKey(str)) { // is in remaining words
                    int count = copy.get(str);
                    if (count == 1) copy.remove(str);
                    else copy.put(str, count - 1);
                    if (copy.isEmpty()) { // matches
                        res.add(i);
                        break;
                    }
                } else break; // not in L
            }
        }
        return res;
    }
	//1九章 Time Limit Exceeded 
	public static List<Integer> findSubstring1(String S, String[] L) {
        List<Integer> result = new ArrayList<Integer>();
        Map<String, Integer> toFind = new HashMap<String, Integer>();
        Map<String, Integer> found = new HashMap<String, Integer>();
        int m = L.length, n = L[0].length();
        for (int i = 0; i < m; i ++){
            if (!toFind.containsKey(L[i])) toFind.put(L[i], 1);
            else toFind.put(L[i], toFind.get(L[i]) + 1);
        }
        for (int i = 0; i <= S.length() - n * m; i ++){
            found.clear();
            int j;
            for (j = 0; j < m; j ++){
                int k = i + j * n;
                String stub = S.substring(k, k + n);
                if (!toFind.containsKey(stub)) break;
                if(!found.containsKey(stub)) found.put(stub, 1);
                else found.put(stub, found.get(stub) + 1);
                if (found.get(stub) > toFind.get(stub)) break;
            }
            if (j == m) result.add(i);
        }
        return result;
    }
	
}
