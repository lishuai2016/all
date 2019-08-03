/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-23 上午10:43:08
 */

public class IntersectionofTwoLinkedLists {

	/**
	 * @author lishuai
	 * @data 2016-12-23 上午10:43:08
Write a program to find the node at which the intersection of two singly linked lists begins.


For example, the following two linked lists:

A:          a1 → a2
                   	↘
                     c1 → c2 → c3
                   	↗            
B:     b1 → b2 → b3
begin to intersect at node c1.


Notes:

If the two linked lists have no intersection at all, return null.
The linked lists must retain their original structure after the function returns.
You may assume there are no cycles anywhere in the entire linked structure.
Your code should preferably run in O(n) time and use only O(1) memory.
	 */

	public static void main(String[] args) {
		

	}
	//3九章    根据找一个链表的环节点的入口
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        
        // get the tail of list A.
        ListNode node = headA;
        while (node.next != null) {
            node = node.next;
        }
        node.next = headB;
        ListNode result = listCycleII(headA);
        node.next = null;
        return result;
    }
    
    private ListNode listCycleII(ListNode head) {
        ListNode slow = head, fast = head.next;
        
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return null;
            }
            
            slow = slow.next;
            fast = fast.next.next;
        }
        
        slow = head;
        fast = fast.next;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        return slow;
    }
	//2 时间复杂度log（M+N）
	public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
	    //boundary check
	    if(headA == null || headB == null) return null;
	    
	    ListNode a = headA;
	    ListNode b = headB;
	    
	    //if a & b have different len, then we will stop the loop after second iteration
	    while( a != b){
	    	//for the end of first iteration, we just reset the pointer to the head of another linkedlist
	        a = a == null? headB : a.next;
	        b = b == null? headA : b.next;    
	    }
	    
	    return a;
	}
	/**
查找两个链表的第一个公共节点，如果两个节点的尾节点相同，肯定存在公共节点
方法： 长的链表开始多走 （h1的数量 - h2的数量）步，然后和短链表同步往下走，遇到的第一个相同的节点就是最早的公共节点
	 */
	//1
	public static ListNode getIntersectionNode1(ListNode headA, ListNode headB) {
		if (headA == null || headB == null) return null;
		ListNode p = headA;
		ListNode q = headB;
		int countA = 0;
		int countB = 0;
		while (p != null) {
			countA++;
			if (p.next == null) break;
			p = p.next;
		}
		while (q != null) {
			countB++;
			if (q.next == null) break;
			q = q.next;
		}
		//p == q说明相交
		if (p == q) {
			p = headA;
			q = headB;
			if (countA <= countB) for (int i = 0;i < countB - countA;i++) q = q.next;			
			else for (int i = 0;i < countA - countB;i++) p = p.next;
			while (p != q) {
				p = p.next;
				q = q.next;
			}			
			return p;
		} else return null;
	}
	
	//0
	public ListNode getIntersectionNode0(ListNode headA, ListNode headB) {
	       if(headA==null||headB==null) return null;
	    	ListNode p,q;
	    	p=headA; 
	    	q=headB;
	    	int aLength=0;
	    	while(p!=null){
	    		aLength++;
	    		p=p.next;
	    	}
	    	int bLength=0;
	    	while(q!=null){
	    		bLength++;
	    		q=q.next;
	    	}
	    	int step=0;
	    	if(aLength>bLength){
	    		step=aLength-bLength;
	    		p=headA; 
	    		int k=0;
	    		while(k<step){
	    			p=p.next;
	    			k++;
	    		}
	    		ListNode r=headB;
	    		while(p!=null&&r!=null){
	    			if(p.equals(r)){
	    				return p;
	    			}
	    			p=p.next;
	    			r=r.next;
	    		}
	    	}else{
	    		step=bLength-aLength;
	    		q=headB;
	    		int k=0;
	    		while(k<step){
	    			q=q.next;
	    			k++;
	    		}
	    		ListNode r=headA;
	    		while(q!=null&&r!=null){
	    			if(q.equals(r)){
	    				return q;
	    			}
	    			q=q.next;
	    			r=r.next;
	    		}
	    	}
	    	return null;
	    }
}
