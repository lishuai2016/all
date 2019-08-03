/**
 * 
 */
package com.ls.li.Leetcode.list;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-19 下午12:00:02
 */

public class MergekSortedLists {

	/**
	 * @author lishuai
	 * @data 2016-12-19 下午12:00:02
Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
	 */

	public static void main(String[] args) {
		ListNode n1 = new ListNode(1);
		ListNode n2 = new ListNode(3);
		ListNode n3 = new ListNode(5);
		ListNode n4 = new ListNode(7);
		
		ListNode n5 = new ListNode(2);
		ListNode n6 = new ListNode(4);
		ListNode n7 = new ListNode(6);
		ListNode n8 = new ListNode(8);
		n1.next = n2;
		n2.next = n3;
		n3.next = n4;
		
		n5.next = n6;
		n6.next = n7;
		n7.next = n8;
		ListNode[] lists = new ListNode[2];
		lists[0] = n1;
		lists[1] = n5;
		//mergeKLists(lists);
		List<ListNode> l = new ArrayList<ListNode>();
		l.add(n5);
		l.add(n1);
		mergeKLists(l);
	}
	//4九章  merge two by two（两两合并）
	 public static ListNode mergeKLists(List<ListNode> lists) {
	        if (lists == null || lists.size() == 0) {
	            return null;
	        }
	        
	        while (lists.size() > 1) {
	            List<ListNode> new_lists = new ArrayList<ListNode>();
	            for (int i = 0; i + 1 < lists.size(); i += 2) {
	                ListNode merged_list = merge(lists.get(i), lists.get(i+1));
	                new_lists.add(merged_list);
	            }
	            //处理奇数时的最后一个
	            if (lists.size() % 2 == 1) {
	                new_lists.add(lists.get(lists.size() - 1));
	            }
	            lists = new_lists;
	        }	        
	        return lists.get(0);
	    }
	    
	    private static ListNode merge(ListNode a, ListNode b) {
	        ListNode dummy = new ListNode(0);
	        ListNode tail = dummy;
	        while (a != null && b != null) {
	            if (a.val < b.val) {
	                tail.next = a;
	                a = a.next;
	            } else {
	                tail.next = b;
	                b = b.next;
	            }
	            tail = tail.next;
	        }
	        
	        if (a != null) {
	            tail.next = a;
	        } else {
	            tail.next = b;
	        }
	        
	        return dummy.next;
	    }
	
	//3 九章递归分解 Divide & Conquer
	public static ListNode mergeKLists3(List<ListNode> lists) {
        if (lists.size() == 0) {
            return null;
        }
        return mergeHelper(lists, 0, lists.size() - 1);
    }
    
    private static ListNode mergeHelper(List<ListNode> lists, int start, int end) {
        if (start == end) {
            return lists.get(start);
        }
        
        int mid = start + (end - start) / 2;
        ListNode left = mergeHelper(lists, start, mid);
        ListNode right = mergeHelper(lists, mid + 1, end);
        return mergeTwoLists(left, right);
    }
    
    private static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                tail.next = list1;
                tail = list1;
                list1 = list1.next;
            } else {
                tail.next = list2;
                tail = list2;
                list2 = list2.next;
            }
        }
        if (list1 != null) {
            tail.next = list1;
        } else {
            tail.next = list2;
        }
        
        return dummy.next;
    }
	/**
	Suppose the total number of nodes is n The total time complexity if (n * log k) .
	For a priority queue, insertion takes logK time
	 */
	//2 优先级队列的特性，最小的元素在最前面
	 public static ListNode mergeKLists2(List<ListNode> lists) {
	        if (lists == null || lists.size() == 0) return null;
	        
	        PriorityQueue<ListNode> queue= new PriorityQueue<ListNode>(lists.size(),new Comparator<ListNode>(){
	            @Override
	            public int compare(ListNode o1, ListNode o2){
	                if (o1.val < o2.val) return -1;
	                else if (o1.val == o2.val) return 0;
	                else  return 1;
	            }
	        });	        
	        ListNode dummy = new ListNode(0);
	        ListNode tail=dummy;
	        
	        for (ListNode node:lists)
	            if (node!=null)
	                queue.add(node);
	        
	        while (!queue.isEmpty()){
	            tail.next=queue.poll();
	            tail=tail.next;	            
	            if (tail.next!=null)
	                queue.add(tail.next);
	        }
	        return dummy.next;
	    }
	
	
	//外循环为遍历数组，只要有一个不为null，一直遍历；	
	//内循环还为遍历数组。第一步找到最小的元素在k个链表中哪一个；第二步更新那个元素最小的链表的头指针		
	//1 Time Limit Exceeded
    public static ListNode mergeKLists1(ListNode[] lists) {
    	if (lists == null || lists.length == 0) return null;
    	if (lists.length == 1) return lists[0];
    	ListNode dummy = new ListNode(0);
    	ListNode p = dummy;
    	boolean flag = true;
    	while (flag) {
    		int count = lists.length;
    		int j = 0;
    		int localMin = Integer.MAX_VALUE;
    		int localMinIndex = -1;
    		for (int i = 0;i < lists.length;i++) {
        		if (lists[i] == null) count--;       		
        	}
    		if (count == 0) {
    			flag = false;
    			continue;
    		} else {
    			while (j < lists.length) {
    				if (lists[j] != null && lists[j].val < localMin) {
    					localMin = lists[j].val;
    					localMinIndex = j;
    				}
    				j++;
    			}
    		}
    		p.next = lists[localMinIndex];
    		lists[localMinIndex] = lists[localMinIndex].next;
    		p = p.next;
    	}   
    	return dummy.next;
    }
    //2的副本数组形式
    public static ListNode mergeKLists21(ListNode[] lists) {
    	if (lists == null || lists.length == 0) return null;
    	Queue<ListNode> queue = new PriorityQueue<ListNode>(lists.length,new Comparator<ListNode>() {
			@Override
			public int compare(ListNode o1, ListNode o2) {
				return o1.val - o2.val;
			}    		
		});
    	ListNode dummy = new ListNode(0);
    	ListNode tail = dummy;
    	for (ListNode node : lists) if (node != null) queue.add(node);
    	while (!queue.isEmpty()) {
    		tail.next = queue.poll();
    		tail = tail.next;
    		if (tail.next != null) queue.add(tail.next);
    	}
    	return dummy.next;
    }
}
