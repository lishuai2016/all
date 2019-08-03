/**
 * 
 */
package com.ls.li.Leetcode.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-23 下午4:40:41
 */

public class BinaryTreeInorderTraversal {

	/**
	 * @author lishuai
	 * @data 2016-12-23 下午4:40:41
Given a binary tree, return the inorder traversal of its nodes' values.

For example:
Given binary tree [1,null,2,3],
   1
    \
     2
    /
   3
return [1,3,2].

Note: Recursive solution is trivial, could you do it iteratively?
	 */
	static List<Integer> res = new ArrayList<Integer>();
	
	
	public static void main(String[] args) {
		

	}
	//3九章
	 public ArrayList<Integer> inorderTraversal(TreeNode root) {
	        Stack<TreeNode> stack = new Stack<TreeNode>();
	        ArrayList<Integer> result = new ArrayList<Integer>();
	        TreeNode curt = root;
	        while (curt != null || !stack.empty()) {
	            while (curt != null) {
	                stack.add(curt);
	                curt = curt.left;
	            }
	            curt = stack.peek();
	            stack.pop();
	            result.add(curt.val);
	            curt = curt.right;
	        }
	        return result;
	    }
	
	//2 和1类似简化一点（用时是1的一半）
	public List<Integer> inorderTraversal2(TreeNode root) {
	    List<Integer> list = new ArrayList<Integer>();

	    Stack<TreeNode> stack = new Stack<TreeNode>();
	    TreeNode cur = root;

	    while(cur != null || !stack.empty()){
	        while(cur != null){
	            stack.add(cur);
	            cur = cur.left;
	        }
	        cur = stack.pop();
	        list.add(cur.val);
	        cur = cur.right;
	    }

	    return list;
	}
	
	
	//1迭代实现 
	public static List<Integer> inorderTraversal1(TreeNode root) { 
		Stack<TreeNode> stack = new Stack<>();
		while (root != null || !stack.empty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			if (!stack.empty()) {
				root = stack.pop();
				res.add(root.val);
				root = root.right;
			}
		}		
		return res;
	}
	
	
	
	//0 递归实现不符合题意
    public static List<Integer> inorderTraversal0(TreeNode root) {
    	if (root == null) return res;
    	inOrder(root);    
        return res;
    }
    public static void inOrder(TreeNode root) {     //中根遍历
		if(root != null) {
			  inOrder(root.left);
			  res.add(root.val);
			  System.out.print(root.val + "--");
			  inOrder(root.right);
		}
	}
}
