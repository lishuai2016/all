/**
 * 
 */
package com.ls.li.Leetcode.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-30 上午11:29:11
 */

public class BinaryTreePreorderTraversal {

	/**
	 * @author lishuai
	 * @data 2016-12-30 上午11:29:11
Given a binary tree, return the preorder traversal of its nodes' values.

For example:
Given binary tree {1,#,2,3},
   1
    \
     2
    /
   3
return [1,2,3].

Note: Recursive solution is trivial, could you do it iteratively?
	 */

	public static void main(String[] args) {


	}
	//4  九章分治
	public ArrayList<Integer> preorderTraversal(TreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        // null or leaf
        if (root == null) {
            return result;
        }

        // Divide
        ArrayList<Integer> left = preorderTraversal(root.left);
        ArrayList<Integer> right = preorderTraversal(root.right);

        // Conquer
        result.add(root.val);
        result.addAll(left);
        result.addAll(right);
        return result;
    }
	
	
	//3九章  递归
	 public ArrayList<Integer> preorderTraversal3(TreeNode root) {
	        ArrayList<Integer> result = new ArrayList<Integer>();
	        traverse(root, result);
	        return result;
	    }
	    // 把root为跟的preorder加入result里面
	    private void traverse(TreeNode root, ArrayList<Integer> result) {
	        if (root == null) {
	            return;
	        }

	        result.add(root.val);
	        traverse(root.left, result);
	        traverse(root.right, result);
	    }
	
	
	//2 九章 迭代（推荐）耗时1ms
	public List<Integer> preorderTraversal2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        List<Integer> preorder = new ArrayList<Integer>();
        
        if (root == null) {
            return preorder;
        }
        
        stack.push(root);
        while (!stack.empty()) {
            TreeNode node = stack.pop();
            preorder.add(node.val);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        
        return preorder;
    }
	
	//1借助栈来实现    耗时2ms
    public static  List<Integer> preorderTraversal1(TreeNode root) {
    	List<Integer> res = new ArrayList<>();
    	Stack<TreeNode> stack = new Stack<>();
    	while (root != null || !stack.empty()) {
    		while (root != null) {
    			res.add(root.val);
    			stack.push(root);
    			root = root.left;
    		}
    		root = stack.pop();
    		root = root.right;
    	}
    	return res;
    }
}
