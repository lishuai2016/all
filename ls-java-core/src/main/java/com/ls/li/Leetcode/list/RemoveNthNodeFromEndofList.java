/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-19 上午9:59:36
 */

public class RemoveNthNodeFromEndofList {

	/**
	 * @author lishuai
	 * @data 2016-12-19 上午9:59:36
Given a linked list, remove the nth node from the end of list and return its head.

For example,

   Given linked list: 1->2->3->4->5, and n = 2.

   After removing the second node from the end, the linked list becomes 1->2->3->5.
Note:
Given n will always be valid.
Try to do this in one pass.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(2);
		l1.next = l2;
		removeNthFromEnd1(l1, 10);
	}
	//2 
	public static ListNode removeNthFromEnd2(ListNode head, int n) {
	    
	    ListNode start = new ListNode(0);
	    ListNode slow = start, fast = start;
	    slow.next = head;
	    
	    //Move fast in front so that the gap between slow and fast becomes n
	    for(int i=1; i<=n+1; i++)   {
	        fast = fast.next;
	    }
	    //Move fast to the end, maintaining the gap
	    while(fast != null) {
	        slow = slow.next;
	        fast = fast.next;
	    }
	    //Skip the desired node
	    slow.next = slow.next.next;
	    return start.next;
	}
	
	//1	二指针一个先走n步(考虑到n可能为无效输入)
	 public static ListNode removeNthFromEnd1(ListNode head, int n) { 
		 if (head == null || n <= 0) return head;
		 ListNode start = new ListNode(0);
		 ListNode slow = start, fast = start;
		 slow.next = head;
		 int count = 0;
		 while (fast != null && count < n) {
			 fast = fast.next;
			 count++;
		 }
		 if (fast == null) return head;
		 while (fast.next != null) {
			 fast = fast.next;
			 slow = slow.next;
		 }
		 slow.next = slow.next.next;
		 return start.next;		 
	 }
	 //0 
	 public static ListNode removeNthFromEnd0(ListNode head, int n) { 
		//把顺序调整过来计算
	    	ListNode r=head;
	    	int i=0;
	    	while(r!=null){
	    		i++;
	    		r=r.next;
	    	}
	    	n=i-n+1;
	    if(n==1) return head.next;
	    	ListNode p,q;
	    	p=head;
	    	q=head.next;
	    	int k=2;
	    	while(p!=null&&q!=null){
	    		if(n==k){
	    			p.next=q.next;
	    			break;
	    		}
	    		p=p.next;
	    		q=q.next;
	    		k++;
	    	}      
	    	return head;
	 }
}
