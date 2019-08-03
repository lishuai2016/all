/**
 * 
 */
package com.ls.li.Leetcode.list;

import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-20 下午4:15:07
 */

public class ReorderList {

	/**
	 * @author lishuai
	 * @data 2016-12-20 下午4:15:07
Given a singly linked list L: L0→L1→…→Ln-1→Ln,
reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…

You must do this in-place without altering the nodes' values.

For example,
Given {1,2,3,4}, reorder it to {1,4,2,3}.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(2);
		ListNode l3 = new ListNode(3);
		ListNode l4 = new ListNode(4);
		ListNode l5 = new ListNode(5);
		ListNode l6 = new ListNode(6);
		l1.next = l2;
		l2.next = l3;
		l3.next = l4;
		l4.next = l5;
		l5.next = l6;
		reorderList2(l1);
	}
	
	
	
	//3九章 和2的原理类似(先找中间节点，然后翻转中间节点以后的元素成一个新的链表，然后拼接)
	 private static ListNode reverse(ListNode head) {
	        ListNode newHead = null;
	        while (head != null) {
	            ListNode temp = head.next;
	            head.next = newHead;
	            newHead = head;
	            head = temp;
	        }
	        return newHead;
	    }

	    private static void merge(ListNode head1, ListNode head2) {
	        int index = 0;
	        ListNode dummy = new ListNode(0);
	        while (head1 != null && head2 != null) {
	            if (index % 2 == 0) {
	                dummy.next = head1;
	                head1 = head1.next;
	            } else {
	                dummy.next = head2;
	                head2 = head2.next;
	            }
	            dummy = dummy.next;
	            index ++;
	        }
	        if (head1 != null) {
	            dummy.next = head1;
	        } else {
	            dummy.next = head2;
	        }
	    }

	    private static ListNode findMiddle(ListNode head) {
	        ListNode slow = head, fast = head.next;
	        while (fast != null && fast.next != null) {
	            fast = fast.next.next;
	            slow = slow.next;
	        }
	        return slow;
	    }

	    public static void reorderList(ListNode head) {
	        if (head == null || head.next == null) {
	            return;
	        }

	        ListNode mid = findMiddle(head);
	        ListNode tail = reverse(mid.next);
	        mid.next = null;

	        merge(head, tail);
	    }
	//2
	public static void reorderList2(ListNode head) {
        if( head==null || head.next == null) return;
        
        //Find the middle of the list
        ListNode p1 = head;
        ListNode p2 = head;
        while(p2.next != null && p2.next.next != null){ 
            p1=p1.next;
            p2=p2.next.next;
        }
        
        //Reverse the half after middle  1->2->3->4->5->6 to 1->2->3->6->5->4
        ListNode preMiddle = p1;
        ListNode preCurrent = p1.next;
        while(preCurrent.next != null){
            ListNode current = preCurrent.next;
            preCurrent.next = current.next;
            current.next = preMiddle.next;
            preMiddle.next = current;
        }
        
        //Start reorder one by one  1->2->3->6->5->4 to 1->6->2->5->3->4
        p1 = head;
        p2 = preMiddle.next;
        while(p1 != preMiddle){
            preMiddle.next = p2.next;
            p2.next = p1.next;
            p1.next = p2;
            p1 = p2.next;
            p2 = preMiddle.next;
        }
    }
	//1 思路：是把元素压入栈中，然后弹出拼接实现（时间复杂度N，空间复杂度可以为N/2）
    public static void reorderList1(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) return;
        Stack<ListNode> stack = new Stack<ListNode>();
        ListNode p = head;
        int length = 0;
        while (p != null) {
        	length++;
        	stack.add(p);
        	p = p.next;
        }
        p = head;
        for (int i = 0;i < length / 2;i++) {
        	ListNode temp = p.next;
        	ListNode node = stack.pop();
        	p.next = node;
        	node.next = temp;
        	p = temp;	
        }
        p.next = null;
        System.out.println();
    }

}
