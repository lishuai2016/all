/**
 * 
 */
package com.ls.li.Lintcode.xianduanshu;

/**
 * @author lishuai
 * @data 2017-4-5 下午4:01:25
 */

public class Buildxianduanshu {

	/**
	 * @author lishuai
	 * @data 2017-4-5 下午4:01:25
线段树是一棵二叉树，他的每个节点包含了两个额外的属性start和end用于表示该节点所代表的区间。start和end都是整数，并按照如下的方式赋值:

根节点的 start 和 end 由 build 方法所给出。
对于节点 A 的左儿子，有 start=A.left, end=(A.left + A.right) / 2。
对于节点 A 的右儿子，有 start=(A.left + A.right) / 2 + 1, end=A.right。
如果 start 等于 end, 那么该节点是叶子节点，不再有左右儿子。
实现一个 build 方法，接受 start 和 end 作为参数, 然后构造一个代表区间 [start, end] 的线段树，返回这棵线段树的根。


说明
线段树(又称区间树), 是一种高级数据结构，他可以支持这样的一些操作:

查找给定的点包含在了哪些区间内
查找给定的区间包含了哪些点
见百科：
线段树
区间树

样例
比如给定start=1, end=6，对应的线段树为：

               [1,  6]
             /        \
      [1,  3]           [4,  6]
      /     \           /     \
   [1, 2]  [3,3]     [4, 5]   [6,6]
   /    \           /     \
[1,1]   [2,2]     [4,4]   [5,5]
	 */

	public static void main(String[] args) {
		SegmentTreeNode n = build(1,6);
		System.out.println(n);
	}
	public static SegmentTreeNode build(int start, int end) {
        // write your code here
		if (start > end) {
			return null;
		}
		if (start == end) {
			return new SegmentTreeNode(start,start);
		}
		SegmentTreeNode root = new SegmentTreeNode(start,end);
		b(root,start,end);
		return root;
    }
	public static void b(SegmentTreeNode root, int start, int end) {
		if (start < end) {
			int mid = (start + end) / 2;
			SegmentTreeNode left = new SegmentTreeNode(start, mid);
			SegmentTreeNode right = new SegmentTreeNode(mid + 1, end);
			root.left = left;
			root.right = right;
			b(root.left, start, mid);
			b(root.right, mid + 1, end);
		} 
	}
}
