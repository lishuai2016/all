/**
 * 
 */
package com.ls.li.Leetcode.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author lishuai
 * @data 2016-12-30 上午10:13:45
 */

public class BinaryTreeLevelOrderTraversal {

	/**
	 * @author lishuai
	 * @data 2016-12-30 上午10:13:45
Given a binary tree, return the level order traversal of its nodes' values. 
(ie, from left to right, level by level).

For example:
Given binary tree [3,9,20,null,null,15,7],
    3
   / \
  9  20
    /  \
   15   7
return its level order traversal as:
[
  [3],
  [9,20],
  [15,7]
]
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
        System.out.println( );
     
	}
	
	//0 用一个队列实现
    public List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        List<List<Integer>> wrapList = new LinkedList<List<Integer>>();
        
        if(root == null) return wrapList;
        
        queue.offer(root);
        while(!queue.isEmpty()){
            int levelNum = queue.size();
            List<Integer> subList = new LinkedList<Integer>();
            for(int i=0; i<levelNum; i++) {
                if(queue.peek().left != null) queue.offer(queue.peek().left);
                if(queue.peek().right != null) queue.offer(queue.peek().right);
                subList.add(queue.poll().val);
            }
            wrapList.add(subList);
        }
        return wrapList;
    }
	
	//1 BFS
	public List<List<Integer>> levelOrder1(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            ArrayList<Integer> level = new ArrayList<Integer>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode head = queue.poll();
                level.add(head.val);
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
            result.add(level);
        }

        return result;
    }
	//2 DFS
	 public ArrayList<ArrayList<Integer>> levelOrder2(TreeNode root) {
	        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
	        
	        if (root == null) {
	            return results;
	        }
	        
	        int maxLevel = 0;
	        while (true) {
	            ArrayList<Integer> level = new ArrayList<Integer>();
	            dfs(root, level, 0, maxLevel);
	            if (level.size() == 0) {
	                break;
	            }
	            
	            results.add(level);
	            maxLevel++;
	        }
	        
	        return results;
	    }
	    
	    private void dfs(TreeNode root,
	                     ArrayList<Integer> level,
	                     int curtLevel,
	                     int maxLevel) {
	        if (root == null || curtLevel > maxLevel) {
	            return;
	        }
	        
	        if (curtLevel == maxLevel) {
	            level.add(root.val);
	            return;
	        }
	        
	        dfs(root.left, level, curtLevel + 1, maxLevel);
	        dfs(root.right, level, curtLevel + 1, maxLevel);
	    }
	//3 BFS. two queues
	    public ArrayList<ArrayList<Integer>> levelOrder3(TreeNode root) {
	        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
	        if (root == null) {
	            return result;
	        }
	        
	        ArrayList<TreeNode> Q1 = new ArrayList<TreeNode>();
	        ArrayList<TreeNode> Q2 = new ArrayList<TreeNode>();

	        Q1.add(root);
	        while (Q1.size() != 0) {
	            ArrayList<Integer> level = new ArrayList<Integer>();
	            Q2.clear();
	            for (int i = 0; i < Q1.size(); i++) {
	                TreeNode node = Q1.get(i);
	                level.add(node.val);
	                if (node.left != null) {
	                    Q2.add(node.left);
	                }
	                if (node.right != null) {
	                    Q2.add(node.right);
	                }
	            }
	            
	            // swap q1 and q2
	            ArrayList<TreeNode> temp = Q1;
	            Q1 = Q2;
	            Q2 = temp;
	            
	            // add to result
	            result.add(level);
	        }
	        
	        return result;
	    }
	    
	    //4 BFS, queue with dummy node
	    public ArrayList<ArrayList<Integer>> levelOrder4(TreeNode root) {
	        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
	        if (root == null) {
	            return result;
	        }
	        
	        Queue<TreeNode> Q = new LinkedList<TreeNode>();
	        Q.offer(root);
	        Q.offer(null); // dummy node
	        
	        ArrayList<Integer> level = new ArrayList<Integer>();
	        while (!Q.isEmpty()) {
	            TreeNode node = Q.poll();
	            if (node == null) {
	                if (level.size() == 0) {
	                    break;
	                }
	                result.add(level);
	                level = new ArrayList<Integer>();
	                Q.offer(null); // add a new dummy node
	                continue;
	            }
	            
	            level.add(node.val);
	            if (node.left != null) {
	                Q.offer(node.left);
	            }
	            if (node.right != null) {
	                Q.offer(node.right);
	            }
	        }
	        
	        return result;
	    }
}
