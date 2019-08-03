/**
 * 
 */
package com.ls.li.Leetcode.list;



/**
 * @author lishuai
 * @data 2016-12-23 下午5:14:18
 */

public class ConvertSortedListtoBinarySearchTree {

	/**
	 * @author lishuai
	 * @data 2016-12-23 下午5:14:18
Given a singly linked list where elements are sorted in ascending order, convert it to a height balanced BST.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(2);
		ListNode l3 = new ListNode(3);
		ListNode l4 = new ListNode(4);
		ListNode l5 = new ListNode(5);
		l1.next = l2;
		l2.next = l3;
		l3.next = l4;
		l4.next = l5;
		int[] a = {1,2,3,4,5,6};
		
		
		//convertSortedListtoBinarySearchTree(l1);
		convertSortedArraytoBinarySearchTree(a);
	}
	public static TreeNode convertSortedArraytoBinarySearchTree(int[] A) {
		if (A == null || A.length == 0) {
			return null;
		}
		TreeNode res = toBST2(A, 0, A.length);
		return res;
	}
	public static TreeNode toBST2(int[] A, int start, int end){
		if (start == end) {
			return null;
		}
		int mid = start + (end - start) / 2;
		TreeNode node = new TreeNode(A[mid]);
		node.left = toBST2(A, start, mid);
		node.right = toBST2(A, mid + 1, end);
		return node;
	}
	
	
	
	public static TreeNode convertSortedListtoBinarySearchTree(ListNode head) {
		if (head == null) {
			return null;
		}
		TreeNode t  = toBST1(head, null);
		return t;
	}
	public static TreeNode toBST1(ListNode head, ListNode tail){
		if (head == tail) {
			return null;
		}
		ListNode fast = head;
		ListNode slow = head;
		while (fast.next != tail && fast.next.next != tail) {
			fast = fast.next.next;
			slow = slow.next;
		}
		TreeNode node = new TreeNode(slow.val);
		node.left = toBST1(head, slow);
		node.right = toBST1(slow.next, tail);
		return node;
	}
	
	public TreeNode sortedListToBST(ListNode head) {
	    if(head==null) return null;
	    return toBST(head,null);
	}
	public TreeNode toBST(ListNode head, ListNode tail){
	    ListNode slow = head;
	    ListNode fast = head;
	    if(head==tail) return null;
	    
	    while(fast!=tail&&fast.next!=tail){
	        fast = fast.next.next;
	        slow = slow.next;
	    }
	    TreeNode thead = new TreeNode(slow.val);
	    thead.left = toBST(head,slow);
	    thead.right = toBST(slow.next,tail);
	    return thead;
	}
	
//    public static TreeNode sortedListToBST(ListNode head) {
//    	if (head == null) return null;
//    
//    	
//        
//    	
//    	return null;
//    }
//    
//    public static  void insert(ListNode node) {
//    	
//    }
    
}
