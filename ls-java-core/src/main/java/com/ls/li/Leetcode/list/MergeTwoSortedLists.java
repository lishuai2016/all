/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-19 上午11:40:56
 */

public class MergeTwoSortedLists {

	/**
	 * @author lishuai
	 * @data 2016-12-19 上午11:40:56
Merge two sorted linked lists and return it as a new list. 
The new list should be made by splicing together the nodes of the first two lists.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//3九章 和1一样
	 public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
	        ListNode dummy = new ListNode(0);
	        ListNode lastNode = dummy;
	        
	        while (l1 != null && l2 != null) {
	            if (l1.val < l2.val) {
	                lastNode.next = l1;
	                l1 = l1.next;
	            } else {
	                lastNode.next = l2;
	                l2 = l2.next;
	            }
	            lastNode = lastNode.next;
	        }
	        
	        if (l1 != null) {
	            lastNode.next = l1;
	        } else {
	            lastNode.next = l2;
	        }
	        
	        return dummy.next;
	    }
	//2递归实现
	public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        if(l1 == null){
            return l2;
        }
        if(l2 == null){
            return l1;
        }        
        ListNode mergeHead;
        if(l1.val < l2.val){
            mergeHead = l1;
            mergeHead.next = mergeTwoLists(l1.next, l2);
        } else {
            mergeHead = l2;
            mergeHead.next = mergeTwoLists(l1, l2.next);
        }
        return mergeHead;
    }
	
	//1 新增一个节点
	public ListNode mergeTwoLists1(ListNode l1, ListNode l2) {
		if (l1 == null) return l2;
		if (l2 == null) return l1;
		ListNode start = new ListNode(0);
		ListNode p = start;
		while (l1 != null && l2 != null) {
			if (l1.val < l2.val) {
				p.next = l1;
				l1 = l1.next;
			} else {
				p.next = l2;
				l2 = l2.next;				
			}
			p = p.next;			
		}	
		if (l1 != null) p.next = l1;			
		if (l2 != null) p.next = l2;		
		return start.next;
	}
	
	//0 没有新增节点
	public ListNode mergeTwoLists0(ListNode l1, ListNode l2) {
		if(l1==null&&l2==null) return null;
        if(l1==null&&l2!=null) return l2;
        if(l1!=null&&l2==null) return l1;
        ListNode p,q,c;//p是l1的当前节点    q是l2的当前节点   c是新生成链表的当前节点
        //先找到新链表的第一个节点
        if(l1.val<=l2.val){
        	c=l1;
        	p=l1.next;
            q=l2;
        }else{
        	c=l2;
        	p=l1;
            q=l2.next;
        }
        //循环比较
        while(p!=null&&q!=null){
        	if(p.val<=q.val){
        		c.next=p;
        		p=p.next;
        		c=c.next;
        	}else{
        		c.next=q;
        		q=q.next;
        		c=c.next;
        	}
        }
        while(p!=null){
        	c.next=p;
    		p=p.next;
    		c=c.next;
        }
        while(q!=null){
        	c.next=q;
    		q=q.next;
    		c=c.next;
        }
        if(l1.val<=l2.val){
        	return l1;
        }else{
        	return l2;
        }
	}
}
