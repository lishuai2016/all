/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-22 下午5:01:37
 */

public class OddEvenLinkedList {

	/**
	 * @author lishuai
	 * @data 2016-12-22 下午5:01:37
Given a singly linked list, group all odd nodes together followed by the even nodes.
 Please note here we are talking about the node number and not the value in the nodes.

You should try to do it in place. The program should run in O(1) space complexity and O(nodes) time complexity.

Example:
Given 1->2->3->4->5->NULL,
return 1->3->5->2->4->NULL.

Note:
The relative order inside both the even and odd groups should remain as it was in the input. 
The first node is considered odd, the second node even and so on ...


Input:
[2,1,4,3,6,5,7,8]
Output:
[2,1,3,5,7,4,6,8]
Expected:
[2,4,6,7,1,3,5,8]
	 */

	public static void main(String[] args) {
		ListNode n1 = new ListNode(1);
		ListNode n2 = new ListNode(2);
		ListNode n3 = new ListNode(3);
		ListNode n4 = new ListNode(4);
		ListNode n5 = new ListNode(5);
		ListNode n6 = new ListNode(6);
		ListNode n7 = new ListNode(7);
		
		n1.next = n2;
		n2.next = n3;
		n3.next = n4;
		n4.next = n5;		
		n5.next = n6;
		n6.next = n7;

		oddEvenList(n1);
	}
	/**
Given 1->2->3->4->5->NULL,
return 1->3->5->2->4->NULL.
	 */
	//2 比1简洁
    public static ListNode oddEvenList(ListNode head) {
    	if (head != null) {    	    
            ListNode odd = head, even = head.next, evenHead = even;
            while (even != null && even.next != null) {
                odd.next = odd.next.next; 
                even.next = even.next.next; 
                odd = odd.next;
                even = even.next;
            }
            odd.next = evenHead; 
        }
        return head;
    }
	//1思路：分成两个链表然后拼接，
	//需要注意的是当节点个数为偶数时，由于一次移动两个节点，会出现最后一个节点连在奇数的尾部，需要处理一下；
	//当节点数为奇数时需要处理一下，最后一个偶数节点的指向为null
    public static ListNode oddEvenList1(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode evenHead = new ListNode(0);
        ListNode q = evenHead;
        ListNode p = head;
        while (p.next != null && p.next.next != null) {
        	q.next = p.next;
        	q = q.next;
        	p.next = p.next.next;      
        	p = p.next;
        }    
        //p始终指向最后一个奇数位置，要是节点数为偶数的话遍历一遍p后面还会有一个节点，把p后的节点连接到evenHead上，然后把p的指向为null
        if (p.next != null) {
        	q.next = p.next;
        	p.next = null;
        } else q.next = null;       
        p.next = evenHead.next;
    	return head;
    }
}
