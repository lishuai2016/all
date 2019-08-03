/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-23 下午2:43:35
 */

public class LinkedListCycle {

	/**
	 * @author lishuai
	 * @data 2016-12-23 下午2:43:35
Given a linked list, determine if it has a cycle in it.

         a->b->c->d
         		^	->e
         		f<-
Follow up:
Can you solve it without using extra space?


n
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//九章    2和1思路一样
	 public Boolean hasCycle(ListNode head) {
	        if (head == null || head.next == null) {
	            return false;
	        }

	        ListNode fast, slow;
	        fast = head.next;
	        slow = head;
	        while (fast != slow) {
	            if(fast==null || fast.next==null)
	                return false;
	            fast = fast.next.next;
	            slow = slow.next;
	        } 
	        return true;
	    }
	//1 思路：二指针，一个一次走一步，一个一次走两步。出现相遇点则有环
    public boolean hasCycle1(ListNode head) {
    	if (head == null || head.next == null) return false;
    	ListNode slow = head;  
        ListNode fast = head;             
        while(fast != null && fast.next != null) {  
             slow = slow.next;  
             fast = fast.next.next;  
             if(slow == fast) return true;  
        }  
        return false;  
    }
}
