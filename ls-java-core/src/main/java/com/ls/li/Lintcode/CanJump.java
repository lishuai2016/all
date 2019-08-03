/**
 * 
 */
package com.ls.li.Lintcode;

/**
 * @author lishuai
 * @data 2017-3-30 下午4:06:39
 */

public class CanJump {

	/**
	 * @author lishuai
	 * @data 2017-3-30 下午4:06:39
给出一个非负整数数组，你最初定位在数组的第一个位置。　　　

数组中的每个元素代表你在那个位置可以跳跃的最大长度。　　　　

判断你是否能到达数组的最后一个位置。

 注意事项

这个问题有两个方法，一个是贪心和 动态规划。

贪心方法时间复杂度为O（N）。

动态规划方法的时间复杂度为为O（n^2）。

我们手动设置小型数据集，使大家阔以通过测试的两种方式。这仅仅是为了让大家学会如何使用动态规划的方式解决此问题。
如果您用动态规划的方式完成它，你可以尝试贪心法，以使其再次通过一次。


样例
A = [2,3,1,1,4]，返回 true.

A = [3,2,1,0,4]，返回 false.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//1、dp
	public boolean canJump1(int[] A) {
        boolean[] can = new boolean[A.length];
        can[0] = true;
        
        for (int i = 1; i < A.length; i++) {
            for (int j = 0; j < i; j++) {
                if (can[j] && j + A[j] >= i) {
                    can[i] = true;
                    break;
                }
            }
        }
        
        return can[A.length - 1];
    }
	
	//2、贪心算法
    public boolean canJump(int[] A) {
        // think it as merging n intervals
        if (A == null || A.length == 0) {
            return false;
        }
        int farthest = A[0];
        for (int i = 1; i < A.length; i++) {
            if (i <= farthest && A[i] + i >= farthest) {
                farthest = A[i] + i;
            }
        }
        return farthest >= A.length - 1;
    }
}
