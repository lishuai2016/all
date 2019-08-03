/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-19 下午3:34:31
 */

public class SwapNodesinPairs {

	/**
	 * @author lishuai
	 * @data 2016-12-19 下午3:34:31
Given a linked list, swap every two adjacent nodes and return its head.

For example,
Given 1->2->3->4, you should return the list as 2->1->4->3.

Your algorithm should use only constant space. 
You may not modify the values in the list, only nodes itself can be changed.
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
		swapPairsN2(l1,2);
	}
	public static ListNode swapPairsN2(ListNode head, int n) {
		if (head == null || n < 2) {
			return head;
		}
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode p = dummy;
		boolean flag = true;
		while (p != null) {
			//检测是否有足够的节点
			ListNode q = p.next;
			int i = 0;
			while (q != null && i < n) {
				q = q.next;
				if (q == null) {
					p = null;
					flag = false;
					break;
				}
				i++;
			}
			//保存翻转前的头结点，翻转后的尾节点
			if (flag) {
				ListNode tail = p.next;
				q = p.next;
				ListNode newHead = null;
				for (int j = 0; j < n; j++) {
					ListNode temp = q.next;
					q.next = newHead;
					newHead = q;
					q = temp;
				}
				p.next = newHead;
				tail.next = q;
				p = tail;
			}
		}		
		return dummy.next;
	}
	
	
	public static ListNode swapPairsN(ListNode head, int n) {
		if (head == null || n < 2) {
			return head;
		}
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode p = dummy;
		while (p != null) {
			p = swapPairsN1(p, n);
		}		
		return dummy.next;
	}
	//1 2 3 4 5        2 1 3 4 5
	public static ListNode swapPairsN1(ListNode head, int n) {
		//检测是否有足够的节点
		ListNode p = head.next;
		int i = 0;
		while (p != null && i < n) {
			p = p.next;
			if (p == null) {
				return null;
			}
			i++;
		}
		//保存翻转前的头结点，翻转后的尾节点
		ListNode tail = head.next;
		p = head.next;
		ListNode newHead = null;
		for (int j = 0; j < n; j++) {
			ListNode temp = p.next;
			p.next = newHead;
			newHead = p;
			p = temp;
		}
		head.next = newHead;
		tail.next = p;
		return tail;
	}
	
	
	//4
	public static ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        head = dummy;
        while (head.next != null && head.next.next != null) {
            ListNode n1 = head.next, n2 = head.next.next;
            // head->n1->n2->...
            // => head->n2->n1->...
            head.next = n2;
            n1.next = n2.next;
            n2.next = n1;
            
            // move to next pair
            head = n1;
        }
        
        return dummy.next;
    }
	//3 递归
	 public static ListNode swapPairs3(ListNode head) {
	        if ((head == null)||(head.next == null))
	            return head;
	        ListNode n = head.next;
	        head.next = swapPairs3(head.next.next);
	        n.next = head;
	        return n;
	    }
	
	//2 感觉和1一样
	public static ListNode swapPairs2(ListNode head) {
	    ListNode dummy = new ListNode(0);
	    dummy.next = head;
	    ListNode current = dummy;
	    while (current.next != null && current.next.next != null) {
	        ListNode first = current.next;
	        ListNode second = current.next.next;
	        first.next = second.next;
	        current.next = second;
	        current.next.next = first;
	        current = current.next.next;
	    }
	    return dummy.next;
	}
	
	//1（自己）思路： 可以用三个变量保存中间值，好理解一点
	public static ListNode swapPairs1(ListNode head) {
		 if (head == null || head.next == null) return head;
		 ListNode dummy = new ListNode(0);
		 ListNode tail = dummy;
		 while (head != null && head.next != null) {
			 ListNode temp = head.next.next;
			 ListNode n1 = head;
			 ListNode n2 = head.next;
			 tail.next = n2;
			 n2.next = n1;
			 n1.next = temp;
			 tail = tail.next.next;
			 head = temp;
		 }
		 return dummy.next;
	}
	//0
	 public ListNode swapPairs0(ListNode head) {
	       if(head==null||head.next==null) return head;
	    	ListNode p,q,r,b;
	    	p=head;//1
	    	q=p.next;//2
	    	r=q;
	    	b=q;
	    	while(p!=null&&q!=null){
	    		r.next=p;//2  
	    		r=r.next;//r指向最后一个
	    		if(q.next==null){//第三个不存在
	    			r.next=null;//最后一个
	    			break;
	    		}else{
	    			//第三个存在
	    			p=q.next;// 3
	    			//第四个不存在
	    			if(p.next==null){
	    				r.next=p;
	    			}else{
	    				q=p.next;
	    			}
	    		}
	    		
	    	}
	    	
	    	return b;
	    }
}
