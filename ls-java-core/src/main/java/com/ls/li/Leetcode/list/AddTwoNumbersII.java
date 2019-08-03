/**
 * 
 */
package com.ls.li.Leetcode.list;

import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-22 下午4:52:43
 */

public class AddTwoNumbersII {

	/**
	 * @author lishuai
	 * @data 2016-12-22 下午4:52:43
You are given two linked lists representing two non-negative numbers. 
The most significant digit comes first and each of their nodes contain a single digit.
 Add the two numbers and return it as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

Follow up:
What if you cannot modify the input lists? In other words, reversing the lists is not allowed.

Example:

Input: (7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 8 -> 0 -> 7



[2,4,3]
[5,6,4]
	 */

	public static void main(String[] args) {
		ListNode n1 = new ListNode(7);
		ListNode n2 = new ListNode(2);
		ListNode n3 = new ListNode(4);		
		ListNode n4 = new ListNode(3);
		
		ListNode n5 = new ListNode(5);
		ListNode n6 = new ListNode(6);
		ListNode n7 = new ListNode(4);
		
		n1.next = n2;
		n2.next = n3;
		n3.next = n4;
				
		n5.next = n6;
		n6.next = n7;

	}
	//2 思路：和1类似，用栈但是优化1的代码
	 public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
	        Stack<Integer> s1 = new Stack<Integer>();
	        Stack<Integer> s2 = new Stack<Integer>();
	        
	        while(l1 != null) {
	            s1.push(l1.val);
	            l1 = l1.next;
	        };
	        while(l2 != null) {
	            s2.push(l2.val);
	            l2 = l2.next;
	        }
	        
	        int sum = 0;
	        ListNode list = new ListNode(0);
	        while (!s1.empty() || !s2.empty()) {
	            if (!s1.empty()) sum += s1.pop();
	            if (!s2.empty()) sum += s2.pop();
	            list.val = sum % 10;
	            ListNode head = new ListNode(sum / 10);
	            head.next = list;
	            list = head;
	            sum /= 10;
	        }
	        
	        return list.val == 0 ? list.next : list;
	    }
	
	
	//1思路：用两个栈保存数据
    public static ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
    	if (l1 == null) return l2;
    	if (l2 == null) return l1;
    	ListNode p = l1;
    	ListNode q = l2;
    	Stack<Integer> stack1 = new Stack<Integer>();
    	Stack<Integer> stack2 = new Stack<Integer>();
    	while (p != null) {
    		stack1.add(p.val);
    		p = p.next;
    	}
    	while (q != null) {
    		stack2.add(q.val);
    		q = q.next;
    	}
    	ListNode newHead = null;
    	int length1 = stack1.size();
    	int length2 = stack2.size();
    	int length = length1 < length2 ? length1 : length2;
    	int flag = 0;
    	for (int i = 0;i < length;i++) {
    		int temp = stack1.pop() + stack2.pop() + flag;
    		if (temp > 9) {
    			temp -= 10;
    			flag = 1;
    		} else flag = 0;
    		ListNode node = new ListNode(temp);
    		node.next = newHead;
    		newHead = node;
    	}
    	if (length1 < length2) {
    		while (!stack2.empty()) {
    			int temp = stack2.pop() + flag;
        		if (temp > 9) {
        			temp -= 10;
        			flag = 1;
        		} else flag = 0;
        		ListNode node = new ListNode(temp);
        		node.next = newHead;
        		newHead = node;
    		}   		
    	} else {
    		while (!stack1.empty()) {
				int temp = stack1.pop() + flag;
				if (temp > 9) {
					temp -= 10;
					flag = 1;
				} else flag = 0;
				ListNode node = new ListNode(temp);
				node.next = newHead;
				newHead = node;
    		}
    	}
    	if (flag == 1) {
    		ListNode node = new ListNode(1);
    		node.next = newHead;
    		newHead = node;
    	}    	
    	return newHead;
    }
}
