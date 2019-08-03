/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-20 上午10:54:35
 */

public class PartitionList {

	/**
	 * @author lishuai
	 * @data 2016-12-20 上午10:54:35
Given a linked list and a value x, partition it such that all nodes less than x come 
before nodes greater than or equal to x.

You should preserve the original relative order of the nodes in each of the two partitions.

For example,
Given 1->4->3->2->5->2 and x = 3,
return 1->2->2->4->3->5.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(4);
		ListNode l3 = new ListNode(3);
		ListNode l4 = new ListNode(2);
		ListNode l5 = new ListNode(5);
		ListNode l6 = new ListNode(2);
		l1.next = l2;
		l2.next = l3;
		l3.next = l4;
		l4.next = l5;
		l5.next = l6;
		partition(l1,3);
	}
	
	//2九章 基本和1一样（优化了遍历时的指针更新）
	public static ListNode partition(ListNode head, int x) {
        if (head == null) {
            return null;
        }
        
        ListNode leftDummy = new ListNode(0);
        ListNode rightDummy = new ListNode(0);
        ListNode left = leftDummy, right = rightDummy;
        
        while (head != null) {
            if (head.val < x) {
                left.next = head;
                left = head;
            } else {
                right.next = head;
                right = head;
            }
            head = head.next;
        }
        
        right.next = null;
        left.next = rightDummy.next;
        return leftDummy.next;
    }
	//1思路：切成两个，然后在拼接在一起即可
    public static ListNode partition1(ListNode head, int x) {
    	if (head == null || head.next == null) return head;
    	ListNode dummy1 = new ListNode(0);
    	ListNode dummy2 = new ListNode(0);
    	ListNode firstStart = dummy1;
    	ListNode secondStart = dummy2;
    	ListNode p = head;
    	while (p != null) {
    		if (p.val < x) {    			
    			dummy1.next = p;
    			dummy1 = dummy1.next;    			
    		} else {    			
    			dummy2.next = p;
    			dummy2 = dummy2.next;   			
    		}
    		p = p.next;
    	}
    	//把第二部分的尾指针设置为null，然后拼接
    	dummy2.next = null;
    	dummy1.next = secondStart.next;
    	return firstStart.next;
    }
}
