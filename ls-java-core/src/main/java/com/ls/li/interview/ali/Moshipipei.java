/**
 * 
 */
package com.ls.li.interview.ali;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishuai
 * @data 2017-4-28 上午11:18:25
 */

public class Moshipipei {
	public static void main(String[] args) {
//		String s1 = "abba";
		String s1 = "baab";
		String s2 = "北京 杭州 杭州 北京";
		System.out.println(check(s1,s2));

	}
	//p ="abba" s="北京 杭州 杭州 北京"
	public static boolean check(String p,String s) {
		String[] split = s.split(" ");
		char[] charArray = p.toCharArray();
		if (split.length != charArray.length) {
			return false;
		}
		Map<Character,String> map = new HashMap<Character,String>();
		for (int i = 0; i < split.length; i++) {
			if (map.containsKey(charArray[i])) {
				String value = map.get(charArray[i]);
				if (value.equals(split[i])) {
					continue;
				} else {
					return false;
				}
			} else {
				map.put(charArray[i], split[i]);
			}
		}				
		return true;
	}
}
