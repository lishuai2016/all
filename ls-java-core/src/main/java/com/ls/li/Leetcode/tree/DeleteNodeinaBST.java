/**
 * 
 */
package com.ls.li.Leetcode.tree;

/**
 * @author lishuai
 * @data 2016-12-30 下午1:52:04
 */

public class DeleteNodeinaBST {

	/**
	 * @author lishuai
	 * @data 2016-12-30 下午1:52:04
Given a root node reference of a BST and a key, delete the node with the given key in the BST. Return the root node reference (possibly updated) of the BST.

Basically, the deletion can be divided into two stages:

Search for a node to remove.
If the node is found, delete the node.
Note: Time complexity should be O(height of tree).

Example:

root = [5,3,6,2,4,null,7]
key = 3

    5
   / \
  3   6
 / \   \
2   4   7

Given key to delete is 3. So we find the node with value 3 and delete it.

One valid answer is [5,4,6,2,null,null,7], shown in the following BST.

    5
   / \
  4   6
 /     \
2       7

Another valid answer is [5,2,6,null,4,null,7].

    5
   / \
  2   6
   \   \
    4   7
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
