/**
 * 
 */
package com.ls.li.Leetcode.string;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-9 下午7:40:56
 */

public class RestoreIPAddresses {

	/**
	 * @author lishuai
	 * @data 2016-12-9 下午7:40:56
Given a string containing only digits, 
restore it by returning all possible valid IP address combinations.

For example:
Given "25525511135",

return ["255.255.11.135", "255.255.111.35"]. (Order does not matter)
	 */

	public static void main(String[] args) {
		System.out.println(restoreIpAddresses("255255111135"));

	}
	//2
	public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<String>();
        int len = s.length();
        for(int i = 1; i<4 && i<len-2; i++){
            for(int j = i+1; j<i+4 && j<len-1; j++){
                for(int k = j+1; k<j+4 && k<len; k++){
                    String s1 = s.substring(0,i), s2 = s.substring(i,j), s3 = s.substring(j,k), s4 = s.substring(k,len);
                    if(isValid(s1) && isValid(s2) && isValid(s3) && isValid(s4)){
                        res.add(s1+"."+s2+"."+s3+"."+s4);
                    }
                }
            }
        }
        return res;
    }
    public static   boolean isValid(String s){
        if(s.length()>3 || s.length()==0 || (s.charAt(0)=='0' && s.length()>1) || Integer.parseInt(s)>255)
            return false;
        return true;
    }
	
	
	//1九章
	public static ArrayList<String> restoreIpAddresses1(String s) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        
        if(s.length() <4 || s.length() > 12)
            return result;
        
        helper(result, list, s , 0);
        return result;
    }
    
    public  static void helper(ArrayList<String> result, ArrayList<String> list, String s, int start){
        if(list.size() == 4){
            if(start != s.length())
                return;
            
            StringBuffer sb = new StringBuffer();
            for(String tmp: list){
                sb.append(tmp);
                sb.append(".");
            }
            sb.deleteCharAt(sb.length()-1);
            result.add(sb.toString());
            return;
        }
        
        for(int i=start; i<s.length() && i < start+3; i++){
            String tmp = s.substring(start, i+1);
            if(isvalid(tmp)){
                list.add(tmp);
                helper(result, list, s, i+1);
                list.remove(list.size()-1);
            }
        }
    }
    
    private static boolean isvalid(String s){
        if(s.charAt(0) == '0')
            return s.equals("0"); // to eliminate cases like "00", "10"
        int digit = Integer.valueOf(s);
        return digit >= 0 && digit <= 255;
    }
	
	
	//0
    public static List<String> restoreIpAddresses0(String s) {
    	List<String> res = new ArrayList<String>();
    	if (s == null || s.length() < 4 || s.length() > 12) return res;
    	if (s.length() == 12) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(s.substring(0,3)).append(".");
    		sb.append(s.substring(3,6)).append(".");
    		sb.append(s.substring(6,9)).append(".");
    		sb.append(s.substring(9));
    		res.add(sb.toString());
    		return res;
    	} else {
    		
    	}
    	
    	return res;
    }

}
