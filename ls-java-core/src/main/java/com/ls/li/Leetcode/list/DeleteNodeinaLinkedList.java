/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-23 上午9:16:20
 */

public class DeleteNodeinaLinkedList {

	/**
	 * @author lishuai
	 * @data 2016-12-23 上午9:16:20
Write a function to delete a node (except the tail) in a singly linked list, given only access to that node.

Supposed the linked list is 1 -> 2 -> 3 -> 4 and you are given the third node with value 3,
 the linked list should become 1 -> 2 -> 4 after calling your function.
 
 题意根据指定位置删除节点
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void deleteNode(ListNode node) {
		// if(node==null) return;  这一句可以不要
	     node.val = node.next.val;  
	     node.next = node.next.next; 
	}
}
