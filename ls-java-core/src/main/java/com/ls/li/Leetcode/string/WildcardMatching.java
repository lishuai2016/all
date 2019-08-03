/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.TreeSet;

/**
 * @author lishuai
 * @data 2016-12-15 上午9:29:15
 */

public class WildcardMatching {

	/**
	 * @author lishuai
	 * @data 2016-12-15 上午9:29:15
Implement wildcard pattern matching with support for '?' and '*'.

'?' Matches any single character.
'*' Matches any sequence of characters (including the empty sequence).

The matching should cover the entire input string (not partial).

The function prototype should be:
bool isMatch(const char *s, const char *p)

Some examples:
isMatch("aa","a") → false
isMatch("aa","aa") → true
isMatch("aaa","aa") → false
isMatch("aa", "*") → true
isMatch("aa", "a*") → true
isMatch("ab", "?*") → true
isMatch("aab", "c*a*b") → false



"ab*"
"a*"
	 */

	public static void main(String[] args) {
		

	}
	//2九章
	public boolean isMatch(String s, String p) {
	    // without this optimization, it will fail for large data set
	    int plenNoStar = 0;
	    for (char c : p.toCharArray())
	        if (c != '*') plenNoStar++;
	    if (plenNoStar > s.length()) return false;

	    s = " " + s;
	    p = " " + p;
	    int slen = s.length();
	    int plen = p.length();

	    boolean[] dp = new boolean[slen];
	    TreeSet<Integer> firstTrueSet = new TreeSet<Integer>();
	    firstTrueSet.add(0);
	    dp[0] = true;

	    boolean allStar = true;
	    for (int pi = 1; pi < plen; pi++) {
	        if (p.charAt(pi) != '*')
	            allStar = false;
	        for (int si = slen - 1; si >= 0; si--) {
	            if (si == 0) {
	                dp[si] = allStar ? true : false;
	            } else if (p.charAt(pi) != '*') {
	                if (s.charAt(si) == p.charAt(pi) || p.charAt(pi) == '?') dp[si] = dp[si-1];
	                else dp[si] = false;
	            } else {
	                int firstTruePos = firstTrueSet.isEmpty() ? Integer.MAX_VALUE : firstTrueSet.first();
	                if (si >= firstTruePos) dp[si] = true;
	                else dp[si] = false;
	            }
	            if (dp[si]) firstTrueSet.add(si);
	            else firstTrueSet.remove(si);
	        }
	    }
	    return dp[slen - 1];
	}
	/**
	The basic idea is to have one pointer for the string and one pointer for the pattern. 
	This algorithm iterates at most length(string) + length(pattern) times, 
	for each iteration, at least one pointer advance one step.
	 */  
	//1
	boolean comparison1(String str, String pattern) {
        int s = 0, p = 0, match = 0, starIdx = -1;            
        while (s < str.length()) {
            // advancing both pointers
            if (p < pattern.length()  && (pattern.charAt(p) == '?' || str.charAt(s) == pattern.charAt(p))) {
                s++;
                p++;
            }
            // * found, only advancing pattern pointer
            else if (p < pattern.length() && pattern.charAt(p) == '*') {
                starIdx = p;
                match = s;
                p++;
            }
           // last pattern pointer was *, advancing string pointer
            else if (starIdx != -1) {
                p = starIdx + 1;
                match++;
                s = match;
            }
           //current pattern pointer is not star, last patter pointer was not *
          //characters do not match
            else return false;
        }
        
        //check for remaining characters in pattern
        while (p < pattern.length() && pattern.charAt(p) == '*')
            p++;
        
        return p == pattern.length();
	}
	//0
    public static boolean isMatch0(String s, String p) {
    	if ((s == null && p != null) || (s != null && p == null) || (s == null && p == null)) return false;
    	if (s.equals(p)) return true;
    	int i = 0;
    	int j = 0;
    	while (i < s.length() && j < p.length()) {
    		if (s.charAt(i) == p.charAt(j)) {
    			i++;
    			j++;
    		} else if (p.charAt(j) == '*') {
    			
    		} else if (p.charAt(j) == '?') {
    			
    		} else return false;
    	}
    	
    	
        return true;
    }
}
