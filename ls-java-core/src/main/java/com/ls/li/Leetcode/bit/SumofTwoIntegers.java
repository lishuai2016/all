/**
 * 
 */
package com.ls.li.Leetcode.bit;

/**
 * @author lishuai
 * @data 2017-1-5 下午5:47:23
 */

public class SumofTwoIntegers {

	/**
	 * @author lishuai
	 * @data 2017-1-5 下午5:47:23
Calculate the sum of two integers a and b, but you are not allowed to use the operator + and -.

Example:
Given a = 1 and b = 2, return 3.
01
11
	 */

	public static void main(String[] args) {
		System.out.println(2&(~1));

	}
	
	 public int getSum(int a, int b) { 
		 if (a == 0) {
			 return b;
		 }
		 if (b == 0) {
			 return a;
		 } 
		 while (b != 0) {
			 int carry = a & b;
			 a = a ^ b;
			 b = carry << 1;
		 }
		 return a;
	 }
	//2
    public static int getSum5(int a, int b) {
	   int result = a ^ b; // 按位加
       int carray = (a & b) << 1; // 计算进位
       if(carray != 0) return  getSum5(result,carray); //判断进位与处理
       return result;
    }
 /**   
 I have been confused about bit manipulation for a very long time. So I decide to do a summary about it here.

"&" AND operation, for example, 2 (0010) & 7 (0111) => 2 (0010)

"^" XOR operation, for example, 2 (0010) ^ 7 (0111) => 5 (0101)

"~" NOT operation, for example, ~2(0010) => -3 (1101) what???
 Don't get frustrated here. It's called two's complement.

1111 is -1, in two's complement

1110 is -2, which is ~2 + 1, ~0010 => 1101, 1101 + 1 = 1110 => 2

1101 is -3, which is ~3 + 1

so if you want to get a negative number, you can simply do ~x + 1

Reference:

https://en.wikipedia.org/wiki/Two%27s_complement

https://www.cs.cornell.edu/~tomf/notes/cps104/twoscomp.html

For this, problem, for example, we have a = 1, b = 3,

In bit representation, a = 0001, b = 0011,

First, we can use "and"("&") operation between a and b to find a carry.

carry = a & b, then carry = 0001

Second, we can use "xor" ("^") operation between a and b to find the different bit, and assign it to a,

Then, we shift carry one position left and assign it to b, b = 0010.

Iterate until there is no carry (or b == 0)
  */
    
 //2 Iterative
    public int getSum4(int a, int b) {
    	if (a == 0) return b;
    	if (b == 0) return a;

    	while (b != 0) {
    		int carry = a & b;
    		a = a ^ b;
    		b = carry << 1;
    	}
    	
    	return a;
    }


    //1 Recursive
    public int getSum2(int a, int b) {
    	return (b == 0) ? a : getSum2(a ^ b, (a & b) << 1);
    }

    // Get negative number
    public int negate(int x) {
    	return ~x + 1;
    }
}
