/**
 * 
 */
package com.ls.li.Leetcode.list;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-19 上午8:49:46
 */

public class AddTwoNumbers {

	/**
	 * @author lishuai
	 * @data 2016-12-19 上午8:49:46
You are given two linked lists representing two non-negative numbers. 
The digits are stored in reverse order and each of their nodes contain a single digit.
 Add the two numbers and return it as a linked list.

Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8


[1,4,3]
[5,6,4]

[6,0,8]
	 */

	public static void main(String[] args) {
		ListNode n1 = new ListNode(1);
		ListNode n2 = new ListNode(4);
		ListNode n3 = new ListNode(5);
		
		ListNode n4 = new ListNode(5);
		ListNode n5 = new ListNode(6);
		ListNode n6 = new ListNode(4);
		
		n1.next = n2;
		n2.next = n3;
		
		n4.next = n5;
		n5.next = n6;
		
		addTwoNumbers(n1,n4);

	}
	//3九章
	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if(l1 == null && l2 == null) {
            return null;
        }            
        ListNode head = new ListNode(0);
        ListNode point = head;
        int carry = 0;
        while(l1 != null && l2!=null){
            int sum = carry + l1.val + l2.val;
            point.next = new ListNode(sum % 10);
            carry = sum / 10;
            l1 = l1.next;
            l2 = l2.next;
            point = point.next;
        }
        
        while(l1 != null) {
            int sum =  carry + l1.val;
            point.next = new ListNode(sum % 10);
            carry = sum /10;
            l1 = l1.next;
            point = point.next;
        }
        
        while(l2 != null) {
            int sum =  carry + l2.val;
            point.next = new ListNode(sum % 10);
            carry = sum /10;
            l2 = l2.next;
            point = point.next;
        }
        
        if (carry != 0) {
            point.next = new ListNode(carry);
        }
        return head.next;
    }
	
	
	//2  只用一个循环简化1的代码（比较简洁）
	public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode c1 = l1;
        ListNode c2 = l2;
        ListNode sentinel = new ListNode(0);
        ListNode d = sentinel;
        int sum = 0;
        while (c1 != null || c2 != null) {
            sum /= 10;
            if (c1 != null) {
                sum += c1.val;
                c1 = c1.next;
            }
            if (c2 != null) {
                sum += c2.val;
                c2 = c2.next;
            }
            d.next = new ListNode(sum % 10);
            d = d.next;
        }
        if (sum / 10 == 1)
            d.next = new ListNode(1);
        return sentinel.next;
    }
	//1 思路，类似于两个有序链表合成一个
	public static ListNode addTwoNumbers1(ListNode l1, ListNode l2) {	 
		if (l1 == null) return l2;
		if (l2 == null) return l1;
		List<Integer> list = new ArrayList<Integer>();
		int flag = 0;
		boolean isl1Length = false;
		ListNode p1 = l1;
		ListNode p2 = l2;
		while (p1 != null && p2 != null) {
			int temp = p1.val + p2.val + flag;
			if (temp > 9) {
				temp -= 10;
				flag = 1;
			} else flag = 0;
			list.add(temp);
			p1 = p1.next;
			p2 = p2.next;
		}
		while (p1 != null) {
			isl1Length = true;
			int temp = p1.val + flag;
			if (temp > 9) {
				temp -= 10;
				flag = 1;
			} else flag = 0;
			list.add(temp);
			p1 = p1.next;
		}
		while (p2 != null) {
			int temp = p2.val + flag;
			if (temp > 9) {
				temp -= 10;
				flag = 1;
			} else flag = 0;
			list.add(temp);
			p2 = p2.next;
		}
		if (flag == 1) list.add(1);
		ListNode head = null;
		if (isl1Length) head = l1;
		else head = l2;
		ListNode p = head;
		int k = 0;
		while (p != null) {
			p.val = list.get(k);			
			if (p.next == null && k + 1 < list.size()) {
				ListNode node = new ListNode(list.get(k + 1));
				p.next = node;
				break;
			} else {
				p = p.next;
				k++;
			}
		}
		return head;
		
		
		
//		ListNode head = new ListNode(list.get(0));
//		ListNode p = head;
//		for (int i = 1;i < list.size();i++) {
//			ListNode node = new ListNode(list.get(i));
//			p.next = node;
//			p = node;
//		}		
		
	}
	//0和1类似（最多新建一个节点）
	public static ListNode addTwoNumbers0(ListNode l1, ListNode l2) {	
	
	int a=0,b=0;
    ListNode p,q;
    ListNode tempNode=null;
    p=l1;
    q=l2;
   while(p!=null){
	   a++;
	   p=p.next;
   }
   while(q!=null){
	   b++;
	   q=q.next;
   }
   System.out.println("l1的长度为："+a);
   System.out.println("l2的长度为："+b);
   int temp;
   int flag=0;
   int m=0,n=0;
   //返回l1
   if(a>=b){
	   p=l1;
	   q=l2;
	   while(q!=null){
		   temp=p.val+q.val+flag;
		   if(temp>=10){
		    	p.val=temp-10;
		    	flag=1;
		    }else{
		    	p.val=temp;
		    	flag=0;
		    }
		   m++;
		   if(m==a){
			   tempNode=p; 
		   }
		   p=p.next;			   
		   q=q.next;
	   }
	   while(p!=null){
		   temp=p.val+flag;
		   if(temp>=10){
		    	p.val=temp-10;
		    	flag=1;
		    }else{
		    	p.val=temp;
		    	flag=0;
		    }
		   m++;
		   if(m==a){
			   tempNode=p; 
		   }
		   p=p.next;			  
		   
	   }
	   if(flag==1){
		   ListNode l=new ListNode(1);
		   tempNode.next=l;
	       l.next=null;
	   }
	   
	   ListNode r=l1;
	   while(r!=null){
		   System.out.println(r.val);
		   r=r.next;
	   }
	   return l1;
	//返回l2
   }else{
	   
	   p=l1;
	   q=l2;
	   while(p!=null){
		   temp=p.val+q.val+flag;
		   if(temp>=10){
		    	q.val=temp-10;
		    	flag=1;
		    }else{
		    	q.val=temp;
		    	flag=0;
		    }
		   n++;
		   if(n==b){
			   tempNode=q; 
		   }
		   p=p.next;			   
		   q=q.next;
	   }
	   while(q!=null){
		   temp=q.val+flag;
		   if(temp>=10){
		    	q.val=temp-10;
		    	flag=1;
		    }else{
		    	q.val=temp;
		    	flag=0;
		    }
		   n++;
		   if(n==b){
			   tempNode=q; 
		   }
		   q=q.next;			  
		   
	   }
	   if(flag==1){
		   ListNode l=new ListNode(1);
		   tempNode.next=l;
	       l.next=null;
	   }
	   
	   ListNode r=l2;
	   while(r!=null){
		   System.out.println(r.val);
		   r=r.next;
	   }
	   return l2;
   }
	}
}
