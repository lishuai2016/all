/**
 * 
 */
package com.ls.li.Leetcode.tree;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-30 上午11:36:09
 */

public class BinaryTreePostorderTraversal {

	/**
	 * @author lishuai
	 * @data 2016-12-30 上午11:36:09
Given a binary tree, return the postorder traversal of its nodes' values.

For example:
Given binary tree {1,#,2,3},
   1
    \
     2
    /
   3
return [3,2,1].

Note: Recursive solution is trivial, could you do it iteratively?
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//1九章   Recursive
	public ArrayList<Integer> postorderTraversal(TreeNode root) {
	    ArrayList<Integer> result = new ArrayList<Integer>();

	    if (root == null) {
	        return result;
	    }

	    result.addAll(postorderTraversal(root.left));
	    result.addAll(postorderTraversal(root.right));
	    result.add(root.val);

	    return result;   
	}
	
	//2九章 Iterative     2ms
	public ArrayList<Integer> postorderTraversal2(TreeNode root) {
	    ArrayList<Integer> result = new ArrayList<Integer>();
	    Stack<TreeNode> stack = new Stack<TreeNode>();
	    TreeNode prev = null; // previously traversed node
	    TreeNode curr = root;

	    if (root == null) {
	        return result;
	    }

	    stack.push(root);
	    while (!stack.empty()) {
	        curr = stack.peek();
	        if (prev == null || prev.left == curr || prev.right == curr) { // traverse down the tree
	            if (curr.left != null) {
	                stack.push(curr.left);
	            } else if (curr.right != null) {
	                stack.push(curr.right);
	            }
	        } else if (curr.left == prev) { // traverse up the tree from the left
	            if (curr.right != null) {
	                stack.push(curr.right);
	            }
	        } else { // traverse up the tree from the right
	            result.add(curr.val);
	            stack.pop();
	        }
	        prev = curr;
	    }

	    return result;
	}
	/**
pre-order traversal is root-left-right, and post order is left-right-root. 
modify the code for pre-order to make it root-right-left, 
and then reverse the output so that we can get left-right-root .

1、Create an empty stack, Push root node to the stack.
2、Do following while stack is not empty.
	2.1. pop an item from the stack and print it.
	
	2.2. push the left child of popped item to stack.
	
	2.3. push the right child of popped item to stack.

3、reverse the ouput.
	 */
	
	//3    2ms
	public List<Integer> postorderTraversal3(TreeNode root) {
		List<Integer> results = new ArrayList<Integer>();
		Deque<TreeNode> stack = new ArrayDeque<TreeNode>();
		while (!stack.isEmpty() || root != null) {
			if (root != null) {
				stack.push(root);
				results.add(root.val);
				root = root.right;
			} else {
				root = stack.pop().left;
			}
		}
		Collections.reverse(results);
		return results;
	}
}
