/**
 * 
 */
package com.ls.li.Leetcode.bit;

/**
 * @author lishuai
 * @data 2017-1-5 下午4:25:37
 */

public class CountingBits {

	/**
	 * @author lishuai
	 * @data 2017-1-5 下午4:25:37
Given a non negative integer number num. 
For every numbers i in the range 0 ≤ i ≤ num 
calculate the number of 1's in their binary representation and return them as an array.

Example:
For num = 5 you should return [0,1,1,2,1,2].
0        0
1		1
10      2
11      3
100      4
101      5
110      6
111      7

Follow up:

It is very easy to come up with a solution with run time O(n*sizeof(integer)).
 But can you do it in linear time O(n) /possibly in a single pass?
Space complexity should be O(n).
Can you do it like a boss? Do it without using any builtin function like __builtin_popcount 
in c++ or in any other language.


Hint:

You should make use of what you have produced already.
Divide the numbers in ranges like [2-3], [4-7], [8-15] and so on. And try to generate new range from previous.
Or does the odd/even status of the number help you in calculating the number of 1s?
	 */

	public static void main(String[] args) {
		

	}
	/**
This uses the hint from the description about using ranges. Basically, 
the numbers in one range are equal to 1 plus all of the numbers in the ranges before it. 
If you write out the binary numbers, you can see that numbers 8-15 have the same pattern as 0-7 
but with a 1 at the front.

My logic was to copy the previous values (starting at 0) until a power of 2 was hit (new range), 
at which point we just reset the t pointer back to 0 to begin the new range.
	 */
	//3     二指针  3ms  感觉2和3主要思想差不多，根据二进制表示的特点
	public int[] countBits(int num) {
	    int[] ret = new int[num+1];
	    ret[0] = 0;
	    int pow = 1;
	    for(int i = 1, t = 0; i <= num; i++, t++) {
	        if(i == pow) {
	            pow *= 2;
	            t = 0;
	        }
	        ret[i] = ret[t] + 1;
	    }
	    return ret;
	}
	//2优解 An easy recurrence for this problem is f[i] = f[i / 2] + i % 2.     2ms
	public int[] countBits2(int num) {
	    int[] f = new int[num + 1];
	    for (int i = 1; i <= num; i++) {
	    	f[i] = f[i >> 1] + (i & 1);
	    }
	    return f;
	}
	//1思路：直接使用内建函数统计每个数对应二进制1的个数  3ms(使用内建函数3ms，不使用7ms)
    public static int[] countBits1(int num) {
    	int[] res = new int[num + 1];
    	for (int i = 0; i <= num; i++) {
    		//res[i] = Integer.bitCount(i);
    		int count = 0;
    		int temp = i;
    		while (temp != 0) {
    			temp = temp & (temp - 1);
    			count++;
    		}
    		res[i] = count;
    	}   	
        return res;
    }
    //0 最原始的的方案  24ms O(n*sizeof(integer))
    public int[] countBits0(int num) {
        int[] back=new int[num+1];
    	for(int i=0;i<=num;i++){
    		String s=Integer.toBinaryString(i);
    		char[] c=s.toCharArray();
    		int count=0;
    		for(int j=0;j<c.length;j++){
    			if(c[j]=='1'){
    				count++;
    			}
    		}
    		back[i]=count;
    	}
   
    	return back;
    }
}
