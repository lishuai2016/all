/**
 * 
 */
package com.ls.li.Leetcode.bit;

/**
 * @author lishuai
 * @data 2017-1-5 上午10:40:44
 */

public class ReverseBits {

	/**
	 * @author lishuai
	 * @data 2017-1-5 上午10:40:44
Reverse bits of a given 32 bits unsigned integer.

For example, given input 43261596 (represented in binary as 00000010100101000001111010011100), 
return 964176192 (represented in binary as 00111001011110000010100101000000).

Follow up:
If this function is called many times, how would you optimize it?

Related problem: Reverse Integer
	 */

	public static void main(String[] args) {
		System.out.println( Integer.reverse(43261596));
		
	}
	//2使用内建函数Integer.reverse(n)  2ms
	 public static int reverseBits2(int n) {		
		 return Integer.reverse(n);
		 
	 }
	//1思路：使用内建函数转化为二进制字符串，然后判断是否够32位，不够的话补齐，然后逆序读   18ms
    public static int reverseBits1(int n) {
    	int res = 0;
        String s = Integer.toBinaryString(n);
        if (s.length() < 32) {
        	StringBuilder sb = new StringBuilder();
        	for (int i = 0; i < 32 - s.length(); i++) {
        		sb.append('0');
        	}
        	s = sb.toString() + s;
        }
        System.out.println(s);
        for (int i = 31; i >= 0; i--) {
        	int b = s.charAt(i) - '0';
        	b <<= i;
        	res |= b;        	
        }
    	return res;
    }
    //3 九章  3ms
    public static int reverseBits(int n) {
        int reversed = 0;
        for (int i = 0; i < 32; i++) {
            reversed = (reversed << 1) | (n & 1);
            n = (n >> 1);
        }
        return reversed;
    }
}
