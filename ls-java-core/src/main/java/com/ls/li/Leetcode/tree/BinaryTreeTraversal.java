/**
 * 
 */
package com.ls.li.Leetcode.tree;

/**
 * @author lishuai
 * @data 2016-12-23 下午4:50:07
 */

public class BinaryTreeTraversal {

	/**
	 * @author lishuai
	 * @data 2016-12-23 下午4:50:07
	 * @param args
	 */

	public static void main(String[] args) {
		int[] array = {12,76,35,22,16,48,90,46,9,40};
		TreeNode root = new TreeNode(array[0]);   //创建二叉树
		  for(int i=1;i<array.length;i++){
		   root.insert(root, array[i]);       //向二叉树中插入数据
		  }
		  System.out.println("先根遍历：");
		  preOrder(root);
		  System.out.println();
		  System.out.println("中根遍历：");
		  inOrder(root);
		  System.out.println();
		  System.out.println("后根遍历：");
		  postOrder(root);

	}
	public static void preOrder(TreeNode root) {  //先根遍历
		  if (root != null) {
			  System.out.print(root.val + "-");
			  preOrder(root.left);
			  preOrder(root.right);
		  }
	}
		 
	public static void inOrder(TreeNode root) {     //中根遍历
		if(root != null) {
			  inOrder(root.left);
			  System.out.print(root.val + "--");
			  inOrder(root.right);
		}
	}
		 
	public static void postOrder(TreeNode root) {    //后根遍历
		if(root != null) {
			postOrder(root.left);
			postOrder(root.right);
			System.out.print(root.val + "---");
		}
	}
}
