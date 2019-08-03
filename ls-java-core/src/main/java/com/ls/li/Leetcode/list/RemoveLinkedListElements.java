/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-23 上午8:56:57
 */

public class RemoveLinkedListElements {

	/**
	 * @author lishuai
	 * @data 2016-12-23 上午8:56:57
Remove all elements from a linked list of integers that have value val.

Example
Given: 1 --> 2 --> 6 --> 3 --> 4 --> 5 --> 6, val = 6
Return: 1 --> 2 --> 3 --> 4 --> 5
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//2迭代
	public static ListNode removeElements2(ListNode head, int val) {
        if (head == null) return null;
        head.next = removeElements2(head.next, val);
        return head.val == val ? head.next : head;
}
	
	//1 和九章一样
    public static ListNode removeElements1(ListNode head, int val) {    	
        //if (head == null) return head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode p = dummy;
        while (p.next != null) {
        	if (p.next.val == val) {
        		p.next = p.next.next;
        	} else p = p.next;
        }   	
    	return dummy.next;
    }
}
