/**
 * 
 */
package com.ls.li.Leetcode.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-16 上午8:43:07
 */

public class InsertionSortList {

	/**
	 * @author lishuai
	 * @data 2016-12-16 上午8:43:08
Sort a linked list using insertion sort.
	 */

	public static void main(String[] args) {
		ListNode l1 = new ListNode(4);
		ListNode l2 = new ListNode(2);
		ListNode l3 = new ListNode(1);
		ListNode l4 = new ListNode(3);
		
		l1.next = l2;
		l2.next = l3;
		l3.next = l4;
		 List<Integer> list = new ArrayList<Integer>();
		 Integer[] a = null;
	        list.toArray(a);
		
		
		System.out.println(insertionSortList(l1));

	}
	
	
	//3九章 和法2类似
	 public static ListNode insertionSortList(ListNode head) {
	        ListNode dummy = new ListNode(0);
	        // 这个dummy的作用是，把head开头的链表一个个的插入到dummy开头的链表里
	        // 所以这里不需要dummy.next = head;

	        while (head != null) {
	            ListNode node = dummy;
	            while (node.next != null && node.next.val < head.val) {
	                node = node.next;
	            }
	            ListNode temp = head.next;
	            // head为要插入的节点的后面，拼接插入指定位置之后排好序的；然后把head插入到指定位置
	            head.next = node.next;
	            node.next = head;
	            head = temp;
	        }

	        return dummy.next;
	    }
	
	//2  优化1的代码
	public static ListNode insertionSortList2(ListNode head) {
		if( head == null ){
			return head;
		}
		
		ListNode helper = new ListNode(0); //new starter of the sorted list
		ListNode cur = head; //the node will be inserted
		ListNode pre = helper; //insert node between pre and pre.next
		ListNode next = null; //the next node will be inserted
		//not the end of input list
		while( cur != null ){
			next = cur.next;
			//find the right place to insert
			while( pre.next != null && pre.next.val < cur.val ){
				pre = pre.next;
			}
			//insert between pre and pre.next
			cur.next = pre.next;
			pre.next = cur;
			//回到初始点
			pre = helper;
			//更新下一个要插入的值
			cur = next;
		}
		
		return helper.next;
	}
	
	
	//1 (自己的)思路：用两个指针分别指向排好序链表的头和尾部，提高针对在表头插入和表未插入的效率；针对中间插入的情况，遍历查找要插入位置的前一个元素
    public static ListNode insertionSortList1(ListNode head) {
    	if (head == null || head.next == null) return head;
    	ListNode firstPoint = head;
    	head = head.next;
    	//把待插入的节点和后面的节点断开，否者有可能形成环
    	firstPoint.next = null;
    	ListNode start = firstPoint;
    	ListNode end = firstPoint;
    	while (head != null) {
    		ListNode insertPosition = end;
    		ListNode insertPoint = head;
    		head = head.next;
    		//把待插入的节点和后面的节点断开，否者有可能形成环
    		insertPoint.next = null;
    		int temp = insertPoint.val;
    		ListNode p = start;
    		//p是循环遍历已经排好序链表的指针，查找要插入位置的前一个元素
    		while (p != null && p.next != null && start.val < temp && end.val > temp) {
    			if (p.next.val > temp) {
    				insertPosition = p;
    				break;
    			}
    			p = p.next;
    		}  		
    		if (start.val >= temp) {
    			//头插
    			insertPoint.next = start;
    			start = insertPoint; 
    		} else if (end.val <= temp) {
    			//尾插
    			end.next = insertPoint;
    			end = insertPoint;   			
    		} else {
    			//中间插入
    			ListNode t = insertPosition.next;
    			insertPosition.next = insertPoint;
    			insertPoint.next = t;
    		}   		
    	}
    	return start;
    }
	
	
	
	
}
