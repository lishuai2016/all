/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-19 下午5:44:23
 */

public class ReverseLinkedList {

	/**
	 * @author lishuai
	 * @data 2016-12-19 下午5:44:23
Reverse a singly linked list.
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
		reverseList00(l1);
	}
	
	
	
	 public static ListNode reverseList00(ListNode head) {
		 ListNode newHead = null;
		 while (head != null) {
			 ListNode temp = head.next;
			 head.next = newHead;
			 newHead = head;
			 head = temp;
		 }		 
		 return newHead;
	 }
	//3 recursive 递归
	public ListNode reverseList(ListNode head) {
	    /* recursive solution */
	    return reverseListInt(head, null);
	}

	private ListNode reverseListInt(ListNode head, ListNode newHead) {
	    if (head == null)
	        return newHead;
	    ListNode next = head.next;
	    head.next = newHead;
	    return reverseListInt(next, head);
	}
	//2这个最简洁
	public static ListNode reverseList2(ListNode head) {
	    /* iterative solution */
	    ListNode newHead = null;
	    while (head != null) {
	        ListNode next = head.next;
	        head.next = newHead;
	        newHead = head;
	        head = next;
	    }
	    return newHead;
	}
	//九章
	public ListNode reverse20(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode temp = head.next;
            head.next = prev;
            prev = head;
            head = temp;
        }
        return prev;
    }
	//1
    public static ListNode reverseList1(ListNode head) { 
    	if (head == null || head.next == null) return head;
    	ListNode tail = head;
    	ListNode p = tail.next;
    	tail.next = null;
    	while (p != null) {
    		ListNode n1 = p;
    		ListNode n2 = n1.next;
    		n1.next = tail;
    		tail = n1;
    		p = n2;
    	}   	
    	return tail;
    }
    
    //0
    public static ListNode reverseList0(ListNode head) {
        if(head==null||head.next==null){return head;}
    	ListNode nextNode=head.next;
    	ListNode pNode=head;
    	head.next=null;
    	while(pNode!=null&&nextNode!=null){   		
    		ListNode temp=nextNode.next;
    		nextNode.next=pNode;
    		pNode=nextNode;
    		nextNode=temp;
    	}    	   	
    	return pNode;        
    }
}
