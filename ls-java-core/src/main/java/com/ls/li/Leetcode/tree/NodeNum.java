/**
 * 
 */
package com.ls.li.Leetcode.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


/**
 * @author lishuai
 * @data 2016-12-30 下午2:50:33
 */

public class NodeNum {

	/**
	 * @author lishuai
	 * @data 2016-12-30 下午2:50:33
	 * @param args
	 */
	/* 
    1  
   / \  
  2   3  
 / \   \  
4   5   6      
*/  
	public static void main(String[] args) {
		TreeNode r1 = new TreeNode(1);
        TreeNode r2 = new TreeNode(2);
        TreeNode r3 = new TreeNode(3);
        TreeNode r4 = new TreeNode(4);
        TreeNode r5 = new TreeNode(5);
        TreeNode r6 = new TreeNode(6);
        r1.left = r2;
        r1.right = r3;
        r2.left = r4;
        r2.right = r5;
        r3.right = r6;
       
        
//        System.out.println( getNodeNumRec(r1));
//        System.out.println( getNodeNum(r1));
        System.out.println( getNodeNum3(r1));
	}
	//1递归
	public static int getNodeNumRec(TreeNode root) {
		if (root == null) return 0;
		return getNodeNumRec(root.left) + getNodeNumRec(root.right) + 1;
	}
	//2迭代 原理同先序遍历，借助一个栈，时间复杂度N
	public static int getNodeNum(TreeNode root) {
		int res = 0;
		if (root == null) return res;
		Stack<TreeNode> stack = new Stack<>();
		stack.push(root);
		while (!stack.empty()) {
			TreeNode node = stack.pop();
			res++;
			if (node.left != null) stack.push(node.left);
			if (node.right != null) stack.push(node.right); 
		}
		return res;
	}
	
	 /** 
     *  求二叉树中的节点个数迭代解法O(n)：基本思想同LevelOrderTraversal， 
     *  即用一个Queue，在Java里面可以用LinkedList来模拟  
     */  
	//3 迭代 借助队列实现
    public static int getNodeNum3(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        Queue<TreeNode> q = new LinkedList<TreeNode>();
        q.offer(root);
        
        int cnt = 0;
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node.left != null) {
                q.offer(node.left);
            }
            
            if (node.right != null) {
                q.offer(node.right);
            }
            
            cnt++;
        }
        
        return cnt;
    }
}
