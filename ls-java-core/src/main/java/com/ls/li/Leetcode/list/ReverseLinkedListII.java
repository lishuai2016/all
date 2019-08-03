/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-20 上午8:42:18
 */

public class ReverseLinkedListII {

	/**
	 * @author lishuai
	 * @data 2016-12-20 上午8:42:18
Reverse a linked list from position m to n. Do it in-place and in one-pass.

For example:
Given 1->2->3->4->5->NULL, m = 2 and n = 4,

return 1->4->3->2->5->NULL.

Note:
Given m, n satisfy the following condition:
1 ≤ m ≤ n ≤ length of list.
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
		reverseBetween2(l1,2,4);
	}
	//3九章
	public static ListNode reverseBetween3(ListNode head, int m, int n) {
        if (m >= n || head == null) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;
        
        for (int i = 1; i < m; i++) {
            if (head == null) {
                return null;
            }
            head = head.next;
        }
        
        ListNode premNode = head;
        ListNode mNode = head.next;
        ListNode nNode = mNode, postnNode = mNode.next;
        for (int i = m; i < n; i++) {
            if (postnNode == null) {
                return null;
            }
            ListNode temp = postnNode.next;
            postnNode.next = nNode;
            nNode = postnNode;
            postnNode = temp;
        }
        mNode.next = postnNode;
        premNode.next = nNode;
        
        return dummy.next;
    }
	//2
	public static ListNode reverseBetween2(ListNode head, int m, int n) {
	    if(head == null) return null;
	    ListNode dummy = new ListNode(0); // create a dummy node to mark the head of this list
	    dummy.next = head;
	    ListNode pre = dummy; // make a pointer pre as a marker for the node before reversing
	    for(int i = 0; i<m-1; i++) pre = pre.next;
	    
	    ListNode start = pre.next; // a pointer to the beginning of a sub-list that will be reversed
	    ListNode then = start.next; // a pointer to a node that will be reversed
	    
	    // 1 - 2 -3 - 4 - 5 ; m=2; n =4 ---> pre = 1, start = 2, then = 3
	    // dummy-> 1 -> 2 -> 3 -> 4 -> 5
	    
	    for(int i=0; i<n-m; i++)
	    {
	        start.next = then.next;
	        then.next = pre.next;
	        pre.next = then;
	        then = start.next;
	    }
	    
	    // first reversing : dummy->1 - 3 - 2 - 4 - 5; pre = 1, start = 2, then = 4
	    // second reversing: dummy->1 - 4 - 3 - 2 - 5; pre = 1, start = 2, then = 5 (finish)
	    
	    return dummy.next;
	    
	}
	
	//1的优化
	public static ListNode reverseBetween11(ListNode head, int m, int n) {
    	if (head == null || head.next == null || m == n) return head;
    	ListNode dummy = new ListNode(0);
	    dummy.next = head;
    	ListNode p = dummy;
    	ListNode firstTail = null;
    	ListNode secondTail = null;
    	for (int i = 0;i < m - 1;i++) p = p.next; 
    	
    	
    	firstTail = p;
    	secondTail = p.next;
    	p = p.next;
    	ListNode tail = null;
    	for (int i = 0;i < n - m + 1;i++) {
    		ListNode next = p.next;
			p.next = tail;
			tail = p;
			p = next;  
    	}
    	firstTail.next = tail;
    	secondTail.next = p;
    	return dummy.next;
    }
	//1 思路：分三段来拼凑 
	//第一步找m-1的位置
	//第二步翻转 m 到n的元素并记录头尾节点
	//第三部把三段链接起来
    public static ListNode reverseBetween1(ListNode head, int m, int n) {
    	if (head == null || head.next == null || m == n) return head;
    	ListNode p = head;
    	ListNode firstTail = null;
    	ListNode secondTail = null;
    	int count = 0;
    	if (m == 1) {
    		count++;
    		firstTail = head;
    		secondTail = head.next;
    	} else {
    		while (p != null) {
    			count++;
    			if (count == m - 1) {
    				firstTail = p;
        			secondTail = p.next;
        			break;
        		}   		            
        		p = p.next;
        	}
    	}
    	p = p.next;
    	ListNode tail = null;
    	while (p != null) {
    		count++;
    		if (count < n + 1) {
    			ListNode next = p.next;
    			p.next = tail;
    			tail = p;
    			p = next;   			
    		} else break;
    	}
    	if (m == 1) {
    		secondTail.next = head;
    		secondTail.next.next = p;
    		return tail;
    	} else {
    		firstTail.next = tail;
        	secondTail.next = p;
        	return head;
    	}    	    	
    }
}
