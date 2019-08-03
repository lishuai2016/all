/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lishuai
 * @data 2016-12-9 上午10:10:01
 */

public class ReverseVowelsofaString {

	/**
	 * @author lishuai
	 * @data 2016-12-9 上午10:10:01
Write a function that takes a string as input and reverse only the vowels of a string.

Example 1:
Given s = "hello", return "holle".

Example 2:
Given s = "leetcode", return "leotcede".

Note:
The vowels does not include the letter "y".
	 */

	public static void main(String[] args) {
		System.out.println(reverseVowels("leetcode"));

	}
	//5 九章答案   统计元音字符的位置和数量，然后操作
	public static String reverseVowels(String s) {
        int[] pos = new int[s.length()];
        int cnt = 0;
        HashSet<Character> vowel = new HashSet<Character>();
        vowel.add('a');
        vowel.add('e');
        vowel.add('i');
        vowel.add('o');
        vowel.add('u');
        vowel.add('A');
        vowel.add('E');
        vowel.add('I');
        vowel.add('O');
        vowel.add('U');
        //统计元音字符的位置和数量
        for (int i = 0; i < s.length(); i++) {
            if (vowel.contains(s.charAt(i))) {
                pos[cnt] = i;
                cnt++;
            }
        }
        
        char[] ans = new char[s.length()];
        ans = s.toCharArray();
        for (int i = 0; i < cnt; i++) {
            ans[pos[i]] = s.charAt(pos[cnt - i - 1]);
        }
        return String.valueOf(ans);
    }
	
	//4 use statically declared String as the dictionary and use the indexOf function to avoid String comparison
	public static String reverseVowels4(String s) {
		 String vowels = "aeiouAEIOU";
	    int first = 0, last = s.length() - 1;
	    char[] array = s.toCharArray();
	    while(first < last){
	        while(first < last && vowels.indexOf(array[first]) == -1){
	            first++;
	        }
	        while(first < last && vowels.indexOf(array[last]) == -1){
	            last--;
	        }
	        char temp = array[first];
	        array[first] = array[last];
	        array[last] = temp;
	        first++;
	        last--;
	    }
	    return new String(array);
	}
	//3 和1，2的思想一致
	public static String reverseVowels3(String s) {
	    if(s == null || s.length()==0) return s;
	    String vowels = "aeiouAEIOU";
	    char[] chars = s.toCharArray();
	    int start = 0;
	    int end = s.length()-1;
	    while(start<end){
	        
	        while(start<end && !vowels.contains(chars[start]+"")){
	            start++;
	        }
	        
	        while(start<end && !vowels.contains(chars[end]+"")){
	            end--;
	        }
	        
	        char temp = chars[start];
	        chars[start] = chars[end];
	        chars[end] = temp;
	        
	        start++;
	        end--;
	    }
	    return new String(chars);
	}
	
//2 方法1的优化(好像在set中查询耗时)
	public static  String reverseVowels2(String s) {
		Set<Character> set = new HashSet<Character>();
		set.add('a');
		set.add('e');
		set.add('i');
		set.add('o');
		set.add('u');
		set.add('A');
		set.add('E');
		set.add('I');
		set.add('O');
		set.add('U');		
    	StringBuffer sb = new StringBuffer(s);
    	int start = 0;
    	int end = s.length() - 1; 
    	while (start < end) {
    		if (!set.contains(s.charAt(start))) {
    			start++;
    			continue;
    		} 
    		if (!set.contains(s.charAt(end))) {
    			end--;
    			continue;
    		}
    		if (start < end && s.charAt(start) != s.charAt(end)) {
    			char temp = sb.charAt(start);
    			sb.setCharAt(start, sb.charAt(end));
    			sb.setCharAt(end, temp);   			
    		}    
    		start++;
			end--;
    	}        
    	return sb.toString();
    }
	//1 思路：两个指针，用一个StringBuffer的性质可以改变指定位的字符特性，交换前后字符即可
    public static  String reverseVowels1(String s) {
    	StringBuffer sb = new StringBuffer(s);
    	int start = 0;
    	int end = s.length() - 1; 
    	while (start < end) {
    		if (s.charAt(start) != 'a' && s.charAt(start) != 'e' && s.charAt(start) != 'i' && s.charAt(start) != 'o' && s.charAt(start) != 'u'
    			&&	s.charAt(start) != 'A' && s.charAt(start) != 'E' && s.charAt(start) != 'I' && s.charAt(start) != 'O' && s.charAt(start) != 'U'
    				) {
    			start++;
    			continue;
    		} 
    		if (s.charAt(end) != 'a' && s.charAt(end) != 'e' && s.charAt(end) != 'i' && s.charAt(end) != 'o' && s.charAt(end) != 'u'
    			&& s.charAt(end) != 'A' && s.charAt(end) != 'E' && s.charAt(end) != 'I' && s.charAt(end) != 'O' && s.charAt(end) != 'U'	
    				) {
    			end--;
    			continue;
    		}
    		if (start < end && s.charAt(start) != s.charAt(end)) {
    			char temp = sb.charAt(start);
    			sb.setCharAt(start, sb.charAt(end));
    			sb.setCharAt(end, temp);   			
    		}    
    		start++;
			end--;
    	}        
    	return sb.toString();
    }

}
