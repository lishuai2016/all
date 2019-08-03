/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-20 下午2:15:29
 */

public class RemoveDuplicatesfromSortedListII {

	/**
	 * @author lishuai
	 * @data 2016-12-20 下午2:15:29
Given a sorted linked list, delete all nodes that have duplicate numbers, 
leaving only distinct numbers from the original list.

For example,
Given 1->2->3->3->4->4->5, return 1->2->5.
Given 1->1->1->2->3, return 2->3.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(1);
		ListNode l3 = new ListNode(2);
		ListNode l4 = new ListNode(3);
		ListNode l5 = new ListNode(4);
		ListNode l6 = new ListNode(4);
		ListNode l7 = new ListNode(5);
		l1.next = l2;
//		l2.next = l3;
//		l3.next = l4;
//		l4.next = l5;
//		l5.next = l6;
//		l6.next = l7;
		deleteDuplicates11(l1);
	}
	//2九章(借助一个变量直接移动，朝后找第一个不等于的数字)
	public static ListNode deleteDuplicates(ListNode head) {
        if(head == null || head.next == null)
            return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;

        while (head.next != null && head.next.next != null) {
            if (head.next.val == head.next.next.val) {
                int val = head.next.val;
                while (head.next != null && head.next.val == val) {
                    head.next = head.next.next;
                }            
            } else {
                head = head.next;
            }
        }
        
        return dummy.next;
    }
	
	//Given 1->2->3->3->4->4->5, return 1->2->5.
	//Given 1->1->1->2->3, return 2->3.
	//1
	public static ListNode deleteDuplicates1(ListNode head) {
        if(head == null) return null;
        ListNode FakeHead = new ListNode(0);
        FakeHead.next = head;
        ListNode pre = FakeHead;
        ListNode cur = head;
        while(cur != null){
            while(cur.next != null && cur.val == cur.next.val) {
                cur = cur.next;
            }
            if(pre.next == cur){
                pre = pre.next;
            } else {
                pre.next = cur.next;
            }
            cur = cur.next;
        }
        return FakeHead.next;
    }
	
	
	
	//1的副本
    public static ListNode deleteDuplicates11(ListNode head) {
    	if (head == null || head.next == null) return head;
    	ListNode dummy = new ListNode(0);
    	dummy.next = head;
    	ListNode pre = dummy;
    	ListNode cur = pre.next;
    	while (cur != null) {
    		//内循环的作用把指针移动到相同元素的最后一个
    		while (cur.next != null && cur.val == cur.next.val) cur = cur.next;
    		if (pre.next == cur) pre = pre.next;
    		else pre.next = cur.next;
    		cur = cur.next;
    	}
    	return dummy.next;
    }
}
