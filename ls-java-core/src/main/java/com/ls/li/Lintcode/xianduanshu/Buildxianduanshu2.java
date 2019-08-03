/**
 * 
 */
package com.ls.li.Lintcode.xianduanshu;

/**
 * @author lishuai
 * @data 2017-4-5 下午4:36:10
 */

public class Buildxianduanshu2 {

	/**
	 * @author lishuai
	 * @data 2017-4-5 下午4:36:10
线段树是一棵二叉树，他的每个节点包含了两个额外的属性start和end用于表示该节点所代表的区间。start和end都是整数，并按照如下的方式赋值:

根节点的 start 和 end 由 build 方法所给出。
对于节点 A 的左儿子，有 start=A.left, end=(A.left + A.right) / 2。
对于节点 A 的右儿子，有 start=(A.left + A.right) / 2 + 1, end=A.right。
如果 start 等于 end, 那么该节点是叶子节点，不再有左右儿子。
对于给定数组设计一个build方法，构造出线段树

max为区间最大值

样例
给出[3,2,1,4]，线段树将被这样构造

                 [0,  3] (max = 4)
                  /            \
        [0,  1] (max = 3)     [2, 3]  (max = 4)
        /        \               /             \
[0, 0](max = 3)  [1, 1](max = 2)[2, 2](max = 1) [3, 3] (max = 4)
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static SegmentTreeNode build(int[] A) {
        // write your code here
		if (A == null || A.length == 0) {
			return null;
		}
		if (A.length == 1) {
			return new SegmentTreeNode(0,0,A[0]);
		}
		int max = getMax(A, 0, A.length - 1);
		SegmentTreeNode root = new SegmentTreeNode(0, A.length - 1, max);
		b(A, root, 0, A.length - 1);
		return root;
    }
	
	public static void b(int[] A ,SegmentTreeNode root, int start, int end) {
		if (start < end) {			
			int mid = (start + end) / 2;
			int max1 = getMax(A, start, mid);
			int max2 = getMax(A, mid + 1, end);
			SegmentTreeNode left = new SegmentTreeNode(start, mid, max1);
			SegmentTreeNode right = new SegmentTreeNode(mid + 1, end, max2);
			root.left = left;
			root.right = right;
			b(A, root.left, start, mid);
			b(A, root.right, mid + 1, end);
		} 
	}
	
	
	public static int getMax(int[] a, int start, int end) {		
		int max = a[start];
		for (int i = start + 1; i <= end; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	} 
}
