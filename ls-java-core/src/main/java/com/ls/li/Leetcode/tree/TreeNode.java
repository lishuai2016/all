/**
 * 
 */
package com.ls.li.Leetcode.tree;

/**
 * @author lishuai
 * @data 2016-12-23 下午4:41:49
 */

public class TreeNode {
	int val;
	public TreeNode left;
	public TreeNode right;
	public TreeNode(int x) { val = x; }
	
	
	
	public void insert(TreeNode root, int data){     //向二叉树中插入子节点
		  if (data > root.val) {
			  if (root.right == null){
			    root.right = new TreeNode(data);
			  } else {
			    this.insert(root.right, data);
			  }
		  } else {                                          //二叉树的右节点都比根节点大
			  if (root.left == null){
				  root.left = new TreeNode(data);
			  } else {
				  this.insert(root.left, data);
			  }
		  }
	}
}
