/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-20 上午11:30:47
 */

public class RotateList {

	/**
	 * @author lishuai
	 * @data 2016-12-20 上午11:30:47
Given a list, rotate the list to the right by k places, where k is non-negative.

For example:
Given 1->2->3->4->5->NULL and k = 2,
return 4->5->1->2->3->NULL.
把最后的k个排到前面
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
		rotateRight(l1,2);
	}
	
	private static int getLength(ListNode head) {
        int length = 0;
        while (head != null) {
            length ++;
            head = head.next;
        }
        return length;
    }
    //2九章 （暂时没理解）
    public static ListNode rotateRight(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        
        int length = getLength(head);
        n = n % length;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;
        
        ListNode tail = dummy;
        for (int i = 0; i < n; i++) {
            head = head.next;
        }
        
        while (head.next != null) {
            tail = tail.next;
            head = head.next;
        }
        
        head.next = dummy.next;
        dummy.next = tail.next;
        tail.next = null;
        return dummy.next;
    }
	
	//1思路：先遍历一遍记录链表的总长度和尾节点，然后再遍历前部分N-K，然后拼接即可
    public static ListNode rotateRight1(ListNode head, int k) {
    	if (head == null || head.next == null) return head;
    	ListNode newHead = null;
    	//链表的尾节点
    	ListNode tail = null;
    	ListNode p = head;
        int length = 0;
        while (p != null) {
        	length++;
        	tail = p.next;
        	p = p.next;
        }
        int i = k % length;
    	if (i == 0) return head;
    	else {
    		p = head;    		
    		for (int j = 1;j < length - i;j++) p = p.next;   		
    	}
    	newHead = p.next;
    	p.next = null;
    	tail.next = head;
    	return newHead;
    }
}
