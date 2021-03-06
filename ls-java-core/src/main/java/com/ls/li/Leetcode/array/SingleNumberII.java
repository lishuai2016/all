/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishuai
 * @data 2017-1-4 上午9:02:21
 */

public class SingleNumberII {

	/**
	 * @author lishuai
	 * @data 2017-1-4 上午9:02:21
Given an array of integers, every element appears three times except for one. Find that single one.

Note:
Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
	 */

	public static void main(String[] args) {
		int[] a = {1, 2, 1,3,1, 2, 2};
		singleNumber2(a);
		System.out.println(~1);

	}
	//3九章    思路统计数组中所有数字的对应32位中的每一位1的总数，然后模3剩余对应的就是要找那个数的组成部分，然后拼接即可   7ms
	public int singleNumber3(int[] A) {
        if (A == null || A.length == 0) {
            return -1;
        }
        int result=0;
        int[] bits=new int[32];
        for (int i = 0; i < 32; i++) {
            for(int j = 0; j < A.length; j++) {
                bits[i] += A[j] >> i & 1;
                bits[i] %= 3;
            }

            result |= (bits[i] << i);
        }
        return result;
    }
	/**
The code seems tricky and hard to understand at first glance.
However, if you consider the problem in Boolean algebra form, everything becomes clear.

What we need to do is to store the number of '1's of every bit. 
Since each of the 32 bits follow the same rules, 
we just need to consider 1 bit. We know a number appears 3 times at most, 
so we need 2 bits to store that. Now we have 4 state, 00, 01, 10 and 11, but we only need 3 of them.

In this solution, 00, 01 and 10 are chosen. Let 'ones' represents the first bit, 
'twos' represents the second bit. Then we need to set rules for 'ones' and 'twos' so that they act as we hopes. 
The complete loop is 00->10->01->00(0->1->2->3/0).

For 'ones', we can get 'ones = ones ^ A[i]; if (twos == 1) then ones = 0', 
that can be tansformed to 'ones = (ones ^ A[i]) & ~twos'.

Similarly, for 'twos', we can get 'twos = twos ^ A[i]; if (ones* == 1) then twos = 0' 
and 'twos = (twos ^ A[i]) & ~ones'. Notice that 'ones*' is the value of 'ones' after calculation, 
that is why twos is calculated later.

Here is another example. If a number appears 5 times at most, we can write a program using the same method. 
Now we need 3 bits and the loop is 000->100->010->110->001. The code looks like this:

int singleNumber(int A[], int n) {
	int na = 0, nb = 0, nc = 0;
	for(int i = 0; i < n; i++){
		nb = nb ^ (A[i] & na);
		na = (na ^ A[i]) & ~nc;
		nc = nc ^ (A[i] & ~na & ~nb);
	}
	return na & ~nb & ~nc;
}
Or even like this:

int singleNumber(int A[], int n) {
	int twos = 0xffffffff, threes = 0xffffffff, ones = 0;
	for(int i = 0; i < n; i++){
		threes = threes ^ (A[i] & twos);
		twos = (twos ^ A[i]) & ~ones;
		ones = ones ^ (A[i] & ~twos & ~threes);
	}
	return ones;
}
	 */
	//2 位操作  1ms
	public static int singleNumber2(int[] A) {
	    int ones = 0, twos = 0;
	    for(int i = 0; i < A.length; i++){
	        ones = (ones ^ A[i]) & ~twos;
	        twos = (twos ^ A[i]) & ~ones;
	    }
	    return ones;
	}
	/**
这是一个更快一些的解法，利用三个变量分别保存各个二进制位上 1 出现一次、两次、三次的分布情况，最后只需返回变量一就行了
A XOR B = (A+B)%进制。


如果是10进制，7 XOR 8 = (7+8)%10 = 5
int singleNumber(int A[], int n) {
    int ones = 0, twos = 0, xthrees = 0;
    for(int i = 0; i < n; ++i) {
        twos |= (ones & A[i]);
        ones ^= A[i];
        xthrees = ~(ones & twos);
        ones &= xthrees;
        twos &= xthrees;
    }

    return ones;
}

这个是用ones 和 twos 分别表示两个bit，要实现计算3的倍数，这俩bit的变化规律是 (注意实际运算中A[ i ]就相当于1)
twos     ones
  0          0
  0          1
  1          0
  1          1
  0          0
当ones和twos都达到1时，用xthrees把他们都重置为0，这就是xthrees开始那三行的作用
那么前两行计数  他利用的规律是
第一行 计算twos ： 当ones是0时，twos不变，（利用了 x|0==x） 当ones是1，twos变为1 ( ones 是1 twos 一定是0 而 0|x==x）
第二行 计算ones ： 简单的xor，偶数次为0，奇数次为1
	 */
	int singleNumber22(int A[], int n) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        int one = 0;
        int two = 0;
        int three = 0;
        for(int i = 0 ; i < n ; i++){
            two |= A[i] & one;
            one = A[i] ^ one;
            three = ~(one&two);
            one &= three;
            two &= three;
        }
        return one;
    }
	
	//1使用一个map统计，时间复杂度N，空间复杂度N/3   16ms
    public static int singleNumber1(int[] nums) {
        int res = Integer.MIN_VALUE;
        if (nums == null || nums.length == 0) return res;
        Map<Integer,Integer> map = new HashMap<>();
        for (int i : nums) {
        	map.put(i, map.get(i) != null ? map.get(i) + 1 : 1);
        }
        for (Integer i : map.keySet()) {
        	if (map.get(i) == 1) {
        		return i;
        	}
        }
        return res;   	
    }
}
