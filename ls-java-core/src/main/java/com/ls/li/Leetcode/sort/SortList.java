/**
 * 
 */
package com.ls.li.Leetcode.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-16 上午11:08:18
 */

public class SortList {

	/**
	 * @author lishuai
	 * @data 2016-12-16 上午11:08:18
Sort a linked list in O(n log n) time using constant space complexity.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * Definition for singly-linked list.
	 * class ListNode {
	 *     int val;
	 *     ListNode next;
	 *     ListNode(int x) {
	 *         val = x;
	 *         next = null;
	 *     }
	 * }
	 */
	
	/*
    The Solution 2:
    Quick Sort.
    使用快排也可以解决。但是注意，要加一个优化才可以过大数据，就是判断一下是不是整个链条是相同的节点，比如2 2 2 2 2 2 2 ，这样的就直接扫一次不用执行

快排，否则它会是N平方的复杂度。
    */
	//5 快速排序（暂时没理解）
    public ListNode sortList(ListNode head) {
        if (head == null) {
            return null;
        }
        
        // Sort the list from 0 to len - 1
        return quickSort(head);
    }
    
    // The quick sort algorithm
    
    // All the elements are the same!
    public boolean isDuplicate(ListNode head) {
        while (head != null) {
            if (head.next != null && head.next.val != head.val) {
                return false;
            }            
            
            head = head.next;
        }
        
        return true;
    }
    
    public ListNode quickSort(ListNode head) {
        if (head == null) {
            return null;
        }
        
        // 如果整个链是重复的，直接跳过。
        if (isDuplicate(head)) {
            return head;
        }
        
        // Use the head node to be the pivot.
        ListNode headNew = partition(head, head.val);
        
        // Find the pre position of the pivoit.
        ListNode cur = headNew;
        
        ListNode dummy = new ListNode(0);
        dummy.next = headNew;
        
        ListNode pre = dummy;
        
        // Find the pre node and the position of the piviot.
        while (cur != null) {
            if (cur.val == head.val) {
                break;
            }
            
            // move forward.
            cur = cur.next;
            pre = pre.next;
        }
        
        // Cut the link to be three parts.
        pre.next = null;
        
        // Get the left link;
        ListNode left = dummy.next;
        
        // Get the right link.
        ListNode right = cur.next;
        cur.next = null;
        
        // Recurtion to call quick sort to sort left and right link.
        left = quickSort(left);
        right = quickSort(right);
        
        // Link the three part together.
        
        // Link the first part and the 2nd part.
        if (left != null) {
            dummy.next = left;
            
            // Find the tail of the left link.
            while (left.next != null) {
                left = left.next;
            }
            left.next = cur;
        } else {
            dummy.next = cur;
        }
        
        cur.next = right;
        
        // The new head;
        return dummy.next;
    }
    
    // Return the new head;
    public ListNode partition(ListNode head, int x) {
        if (head == null) {
            return null;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode pre = dummy;
        ListNode cur = head;
        
        // Record the big list.
        ListNode bigDummy = new ListNode(0);
        ListNode bigTail = bigDummy;
        
        while (cur != null) {
            if (cur.val >= x) {
                // Unlink the cur;
                pre.next = cur.next;
               
                // Add the cur to the tail of the new link.
                bigTail.next = cur;
                cur.next = null;
               
                // Refresh the bigTail.
                bigTail = cur;
               
                // 移除了一个元素的时候，pre不需要修改，因为cur已经移动到下一个位置了。
            } else {
                pre = pre.next;
            }
            
            cur = pre.next;
        }
        
        // Link the Big linklist to the smaller one.
        pre.next = bigDummy.next;
        
        return dummy.next;
    }
	
	
//4    和3原理一样，但是比3可读性强          使用Merge Sort, 空间复杂度是 O(logN) 因为使用了栈空间
	    public static ListNode sortList4(ListNode head) {
	        // Nodes should be more than 2.
	        if (head == null || head.next == null) {
	            return head;
	        }
	        
	        // get the mid node.
	        ListNode midPre = getMidPre(head);
	        
	        // Cut the two list.
	        ListNode right = midPre.next;
	        midPre.next = null;
	        
	        // Sort the left side and the right side.
	        ListNode left = sortList4(head);
	        right = sortList4(right);
	        
	        // Merge the two sides together.
	        return merge(left, right);
	    }
	    
	    // get the pre node before mid.
	    public static ListNode getMidPre1(ListNode head) {
	        ListNode slow = head;
	        ListNode fast = head;
	        
	        while (fast != null && fast.next != null && fast.next.next != null) {
	            slow = slow.next;
	            fast = fast.next.next;
	        }
	        
	        return slow;
	    }
	    
	    // get the pre node before mid.
	    public  static ListNode getMidPre(ListNode head) {
	        ListNode slow = head;
	        
	        // fast提前一点儿。这样就可以得到前一个节点喽。
	        ListNode fast = head.next;
	        
	        while (fast != null && fast.next != null) {
	            slow = slow.next;
	            fast = fast.next.next;
	        }
	        
	        return slow;
	    }
	    
	    public static ListNode merge(ListNode head1, ListNode head2) {
	        ListNode dummy = new ListNode(0);
	        ListNode cur = dummy;
	        
	        while (head1 != null && head2 != null) {
	            if (head1.val < head2.val) {
	                cur.next = head1;
	                head1 = head1.next;
	            } else {
	                cur.next = head2;
	                head2 = head2.next;
	            }
	            
	            cur = cur.next;
	        }
	        
	        if (head1 != null) {
	            cur.next = head1;
	        } else {
	            cur.next = head2;
	        }
	        
	        return dummy.next;
	    }

	
	//3 使用归并排序     使用Merge Sort, 空间复杂度是 O(logN) 因为使用了栈空间
	public ListNode sortList3(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode f = head.next.next;
        ListNode p = head;
        while (f != null && f.next != null) {
            p = p.next;
            f =  f.next.next;
        }
        ListNode h2 = sortList3(p.next);
        p.next = null;
        return merge1(sortList3(head), h2);
    }
    public ListNode merge1(ListNode h1, ListNode h2) {
        ListNode hn = new ListNode(Integer.MIN_VALUE);
        ListNode c = hn;
        while (h1 != null && h2 != null) {
            if (h1.val < h2.val) {
                c.next = h1;
                h1 = h1.next;
            }
            else {
                c.next = h2;
                h2 = h2.next;
            }
            c = c.next;
        }
        if (h1 != null)
            c.next = h1;
        if (h2 != null)
            c.next = h2;
        return hn.next;
    }
	//2思路：把链表转换为数组然后快速排序，最后生成新的链表
	public static ListNode sortList2(ListNode head) {
        if (head == null) return head;
        ListNode helper = new ListNode(0);
        ListNode end = helper;
        List<Integer> list = new ArrayList<Integer>();
        while (head != null) {
        	list.add(head.val);
        	head = head.next;
        }
        int[] a = new int[list.size()];
        for (int i = 0;i < list.size();i++) {
        	a[i] = list.get(i);
        }
        quick_sort(a,0,a.length);
        for (int i = 0;i < a.length;i++) {
        	ListNode node = new ListNode(a[i]);
        	end.next = node;
        	end = node;
        }
    	return helper.next;
    }
	//快速排序
	public static void quick_sort(int[] a,int l,int r) {
		if (l < r) {
			int x = a[l];
			int i = l;
			int j = r;
			while (i < j) {
				while (i < j && a[j] >= x) j--;
				if (i < j) a[i++] = a[j];
				while (i < j && a[i] <= x) i++;
				if (i < j) a[j--] = a[i];
			}	
			a[i] = x;
			quick_sort(a,l,i - 1);
			quick_sort(a,i + 1,r);
		}
	}
	
	
	//1 插入排序N*N
    public static ListNode sortList1(ListNode head) {
        if (head == null) return head;
        ListNode helper = new ListNode(0);
        while (head != null) {
        	ListNode temp = head.next;
        	ListNode node = helper;
        	while (node.next != null && node.next.val < head.val) node = node.next;
        	head.next = node.next;
        	node.next = head;
        	head = temp;       	
        }    
    	return helper.next;
    }
}
