/**
 * 
 */
package com.ls.li.Leetcode.list;

import java.util.Stack;

/**
 * @author lishuai
 * @data 2016-12-19 下午4:23:06
 */

public class ReverseNodesinkGroup {

	/**
	 * @author lishuai
	 * @data 2016-12-19 下午4:23:06
Given a linked list, reverse the nodes of a linked list k at a time and return its modified list.

If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is.

You may not alter the values in the nodes, only nodes itself may be changed.

Only constant memory is allowed.

For example,
Given this linked list: 1->2->3->4->5

For k = 2, you should return: 2->1->4->3->5

For k = 3, you should return: 3->2->1->4->5
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
		reverseKGroup(l1,3);
	}
	
	//4九章（好理解点）
	public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;        
        head = dummy;
        
        
        while (head.next != null) {
            head = reverseNextK(head, k);
        }
        
        return dummy.next;
    }
    
    // reverse head->n1->..->nk->next..
    // to head->nk->..->n1->next..
    // return n1
    private static ListNode reverseNextK(ListNode head, int k) {
        // check there is enought nodes to reverse
        ListNode next = head; // next is not null
        for (int i = 0; i < k; i++) {
            if (next.next == null) {
                return next;
            }
            next = next.next;
        }
        
        // reverse
        ListNode n1 = head.next;
        ListNode prev = head, curt = n1;
        for (int i = 0; i < k; i++) {
            ListNode temp = curt.next;
            curt.next = prev;
            prev = curt;
            curt = temp;
        }
        
        n1.next = curt;
        head.next = prev;
        return n1;
    }
	
	
	
	//3
	public static ListNode reverseKGroup3(ListNode head, int k) {
        if (head==null||head.next==null||k<2) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode tail = dummy, prev = dummy,temp;
        int count;
        while(true){
            count =k;
            while(count>0&&tail!=null){
                count--;
                tail=tail.next;
            } 
            if (tail==null) break;//Has reached the end
            

            head=prev.next;//for next cycle
        // prev-->temp-->...--->....--->tail-->....
        // Delete @temp and insert to the next position of @tail
        // prev-->...-->...-->tail-->head-->...
        // Assign @temp to the next node of @prev
        // prev-->temp-->...-->tail-->...-->...
        // Keep doing until @tail is the next node of @prev
            while(prev.next!=tail){
                temp=prev.next;//Assign
                prev.next=temp.next;//Delete
                
                temp.next=tail.next;
                tail.next=temp;//Insert
                
            }
            
            tail=head;
            prev=head;
            
        }
        return dummy.next;
        
    }
	
	
	//2 recursive
	public static ListNode reverseKGroup2(ListNode head, int k) {
	    ListNode curr = head;
	    int count = 0;
	    while (curr != null && count != k) { // find the k+1 node
	        curr = curr.next;
	        count++;
	    }
	    if (count == k) { // if k+1 node is found
	        curr = reverseKGroup2(curr, k); // reverse list with k+1 node as head
	        // head - head-pointer to direct part, 
	        // curr - head-pointer to reversed part;
	        while (count-- > 0) { // reverse current k-group: 
	            ListNode tmp = head.next; // tmp - next head in direct part
	            head.next = curr; // preappending "direct" head to the reversed list 
	            curr = head; // move head of reversed part to a new node
	            head = tmp; // move "direct" head to the next node in direct part
	        }
	        head = curr;
	    }
	    return head;
	}
	
	
	//1通过栈来实现分组（需要注意最后一个栈数据要是和k一样需要单独处理）,有可能用O（N）的空间
    public static ListNode reverseKGroup1(ListNode head, int k) {
    	if (head == null || k <= 1) return head;
    	 ListNode dummy = new ListNode(0);
    	 ListNode tail = dummy;
    	 Stack<ListNode> stack = new Stack<ListNode>();
    	 while (head != null) {
    		 if (stack.size() < k) {
    			 stack.add(head);
    			 head = head.next;
    			 continue;
    		 } else {
    			 while (!stack.empty()) {
    				 ListNode temp = stack.pop();
    				 tail.next = temp;
    				 tail = tail.next;
    			 }   			
    		 }
    	 }
    	 //对于最后一个栈数据的处理
    	 if (!stack.empty() && stack.size() == k) {
    		 while (!stack.empty()) {
    			tail.next = stack.pop();
    			tail = tail.next;
    		 }
    		 tail.next = null;
    	 } else if (!stack.empty()) {
    		 ListNode start = stack.pop();
    		 ListNode temp = null;
    		 while (!stack.empty()) {
    			 temp = stack.pop();
    			 temp.next = start;
    			 start = temp;
    		 }
    		 if (temp == null)  tail.next = start;
    		 else  tail.next = temp;
    	 }   	 
    	return dummy.next;
    }
}
