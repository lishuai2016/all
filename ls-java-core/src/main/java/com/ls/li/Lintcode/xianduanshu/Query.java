/**
 * 
 */
package com.ls.li.Lintcode.xianduanshu;

/**
 * @author lishuai
 * @data 2017-4-5 下午4:41:33
 */

public class Query {

	/**
	 * @author lishuai
	 * @data 2017-4-5 下午4:41:33
对于一个有n个数的整数数组，在对应的线段树中, 根节点所代表的区间为0-n-1, 每个节点有一个额外的属性max，值为该节点所代表的数组区间start到end内的最大值。

为SegmentTree设计一个 query 的方法，接受3个参数root, start和end，线段树root所代表的数组中子区间[start, end]内的最大值。


样例
对于数组 [1, 4, 2, 3], 对应的线段树为：

                  [0, 3, max=4]
                 /             \
          [0,1,max=4]        [2,3,max=3]
          /         \        /         \
   [0,0,max=1] [1,1,max=4] [2,2,max=2], [3,3,max=3]
query(root, 1, 1), return 4

query(root, 1, 2), return 4

query(root, 2, 3), return 3

query(root, 0, 2), return 4
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public int max = Integer.MIN_VALUE;
	public int query(SegmentTreeNode root, int start, int end) {
        // write your code here
		traverse(root, start, end);
		return max;
    }
	public void traverse(SegmentTreeNode root, int start, int end) {
		if (root != null) {
			if (start >= root.start && start <= root.end && root.max > max) {
				max = root.max;
			}
			if (end >= root.start && end <= root.end && root.max > max) {
				max = root.max;
			}
			traverse(root.left, start, end);
			traverse(root.right, start, end);
		}
	}
}
