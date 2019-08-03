/**
 * 
 */
package com.ls.li.Leetcode.tree;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-30 下午4:48:28
 */

public class TreeDepth {

	/**
	 * @author lishuai
	 * @data 2016-12-30 下午4:48:28
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
                
       System.out.println(inorderTraversal2(r1));
	}
	
	
	public static List<Integer> inorderTraversal2(TreeNode root) {
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

	
	
	
	//1递归实现
//	 public static int getTreeDepthRec(TreeNode root) {
//     	if (root == null) return 0; 
//     	return Math.max(getTreeDepthRec(root.left), getTreeDepthRec(root.right)) + 1;       	
//     }
//	 public static int getTreeDepth(TreeNode root) {
//		 int res = 0;
//		 
//		 
//		 return 0;
//	 }
}
