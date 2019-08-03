/**
 * 
 */
package com.ls.li.Leetcode.array;

/**
 * @author lishuai
 * @data 2016-12-2 下午4:31:01
 */

public class NextPermutation {

	/**
	 * @author lishuai
	 * @data 2016-12-2 下午4:31:01
Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.

If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending order).

The replacement must be in-place, do not allocate extra memory.

Here are some examples. Inputs are in the left-hand column and its corresponding outputs are in the right-hand column.
1,2,3 → 1,3,2
3,2,1 → 1,2,3
1,1,5 → 1,5,1
[1,3,4,7,0,8,6]    ->[1,3,4,7,6,0,8]
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a={1,2,3};
		//System.out.println(1^2^2);
		nextPermutation(a);
	}
	
    public static void nextPermutation(int[] nums) {   	
        for (int i = 0;i < nums.length - 1;i++) {
        	if (nums[i + 1] <= nums[i]) {
        		//把i+1之后的元素全部后移        		
        		int end = nums[nums.length - 1];
        		int j = nums.length - 1;
        		while (j >= i+1) {
        			nums[j] = nums[j-1];
        			j--;
        		}
        		nums[i + 1] = end;
        		break;
        	} else {
        		
        	}
        }
        System.out.println();
    }

}
