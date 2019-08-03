/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-23 上午9:24:40
 */

public class PalindromeLinkedList {

	/**
	 * @author lishuai
	 * @data 2016-12-23 上午9:24:40
Given a singly linked list, determine if it is a palindrome.

Follow up:
Could you do it in O(n) time and O(1) space?
	 */

	public static void main(String[] args) {
		ListNode n1 = new ListNode(1);
		ListNode n2 = new ListNode(2);
		ListNode n3 = new ListNode(2);		
		ListNode n4 = new ListNode(1);		
		ListNode n5 = new ListNode(5);
		ListNode n6 = new ListNode(6);
		ListNode n7 = new ListNode(7);
		
		n1.next = n2;
		n2.next = n3;
		n3.next = n4;
//		n4.next = n5;		
//		n5.next = n6;
//		n6.next = n7;
		System.out.println(isPalindrome(n1));
		
	}
	//2九章  1 2 3 2 1
	 public static boolean isPalindrome(ListNode head) {
	        if (head == null) {
	            return true;
	        }
	        
	        ListNode middle = findMiddle(head);
	        middle.next = reverse(middle.next);
	        
	        ListNode p1 = head, p2 = middle.next;
	        while (p1 != null && p2 != null && p1.val == p2.val) {
	            p1 = p1.next;
	            p2 = p2.next;
	        }
	        
	        return p2 == null;
	    }
	    
	    private static ListNode findMiddle(ListNode head) {
	        if (head == null) {
	            return null;
	        }
	        ListNode slow = head, fast = head.next;
	        while (fast != null && fast.next != null) {
	            slow = slow.next;
	            fast = fast.next.next;
	        }
	        
	        return slow;
	    }
	    
	    private static ListNode reverse(ListNode head) {
	        ListNode prev = null;
	        
	        while (head != null) {
	            ListNode temp = head.next;
	            head.next = prev;
	            prev = head;
	            head = temp;
	        }
	        
	        return prev;
	    }
	//1思路：找到中间节点，然后翻转前半部分(链表的奇偶个数)
    public static boolean isPalindrome1(ListNode head) {
    	if (head == null || head.next == null) return true;
    	int count = 0;
    	ListNode p = head;
    	while (p != null) {
    		count++;
    		p = p.next;    			
    	}    	
    	int mid = count / 2;   
    	int i = 0;
    	p = head;
    	ListNode q = null;
		while (i < mid) {
			ListNode temp = p.next;
			p.next = q;
			q = p;
			p = temp;
			i++;
		} 
		//当链表的个数为奇数个时，p当前刚好指向中间，需要后移一步；为偶数个时指向中间两个的后一个，不需要移动
    	if (count % 2 != 0) p = p.next;    	
		while (q != null) {
			if (p.val != q.val) return false;
			p = p.next;
			q = q.next;
		} 		   	
    	return true;
    }
}
