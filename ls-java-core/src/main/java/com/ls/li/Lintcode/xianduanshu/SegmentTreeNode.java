/**
 * 
 */
package com.ls.li.Lintcode.xianduanshu;

/**
 * @author lishuai
 * @data 2017-4-5 下午4:11:37
 */
public class SegmentTreeNode {
	
	public int start, end, max;
	public SegmentTreeNode left, right;
	public SegmentTreeNode(int start, int end, int max) {
		this.start = start;
		this.end = end;
		this.max = max;
		this.left = this.right = null;
	}
	public SegmentTreeNode(int start, int end) {
		this.start = start;
		this.end = end;
		this.left = this.right = null;
	}
}

