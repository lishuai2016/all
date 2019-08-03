/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-12-19 上午10:25:34
 */

public class FindKtoTail {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        n1.next = n2;
      n2.next = n3;
//      n3.nextNode = n4;
//      n4.nextNode = n5;
//      n5.nextNode = n6;
        System.out.println(findKtoTail(n1, 1));
    }
    //1要求只遍历一次链表(二指针)
    public static ListNode findKtoTail(ListNode head,int k) {
        if (head == null || k <= 0) return null;
        int count = 1;
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && count < k) {
            fast = fast.next;
            count++;
        }
        if (fast == null) return null;
        while(fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }
    //2稍微简化一下1
    public static ListNode findKtoTail1(ListNode head,int k) {
        if (head == null || k <= 0) return null;
        ListNode fast = head;
        ListNode slow = head;
        for (int i = 0;i < k - 1;i++) {
            if (fast.next != null) fast = fast.next;
            else return null;
        }
        while(fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }
}
