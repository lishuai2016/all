/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-20 下午1:53:07
 */

public class RemoveDuplicatesfromSortedList {

	/**
	 * @author lishuai
	 * @data 2016-12-20 下午1:53:07
Given a sorted linked list, delete all duplicates such that each element appear only once.

For example,
Given 1->1->2, return 1->2.
Given 1->1->2->3->3, return 1->2->3.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(1);
		ListNode l3 = new ListNode(1);
		ListNode l4 = new ListNode(1);
		ListNode l5 = new ListNode(1);
		l1.next = l2;
		l2.next = l3;
		l3.next = l4;
		l4.next = l5;
		deleteDuplicates0(l1);
	}
	//3递归实现
	public ListNode deleteDuplicates(ListNode head) {
        if(head == null || head.next == null) return head;
        head.next = deleteDuplicates(head.next);
        return head.val == head.next.val ? head.next : head;
	}
	
	
	//2九章（个人觉得比1优化一点，判断的优化）
	 public static ListNode deleteDuplicates2(ListNode head) {
	        if (head == null) {
	            return null;
	        }

	        ListNode node = head;
	        while (node.next != null) {
	            if (node.val == node.next.val) {
	                node.next = node.next.next;
	            } else {
	                node = node.next;
	            }
	        }
	        return head;
	    }
	//1一个指针（效率比0高一点）
    public static ListNode deleteDuplicates1(ListNode head) { 
    	if (head == null || head.next == null) return head;
    	ListNode p = head;
    	while (p != null && p.next != null) {
    		if (p.val == p.next.val) {
    			p.next = p.next.next;
    		} else p = p.next;
    	}    	
    	return head;
    }
    
    //0两个指针
    public static ListNode deleteDuplicates0(ListNode head) {
        //只有一个元素
       	if(head == null || head.next == null) return head;
       	
       	ListNode pNode = head;//前指针
       	ListNode nextNode = head.next;  //后指针
       
   		while(pNode != null && nextNode != null){
   			if(pNode.val == nextNode.val) {
   				pNode.next = nextNode.next;//删除节点
   				//pNode=pNode.next;//修改头指针
   				nextNode = nextNode.next;//修改尾指针
   			} else {
   				pNode=nextNode;//修改头指针
   				nextNode=nextNode.next;//修改尾指针
   			}
   		}
           return head;   
       }
}
