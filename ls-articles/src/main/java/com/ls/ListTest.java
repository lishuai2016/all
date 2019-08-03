package com.ls;

/**
 * @program: ls-articles
 * @author: lishuai
 * @create: 2019-05-26 12:42
 */
public class ListTest {



    public static void main(String[] args) throws Exception {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(2);
        ListNode l4 = new ListNode(3);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
       // l2.next = l3;
        //l3.next = l4;
       // l4.next = l5;

        //ListNode reverse = reverse(l1);

       // ListNode reverseBetween = reverseBetween(l1, 2, 4);

        isPalindrome(l1);
        System.out.println("hello1111");

    }

    public static boolean isPalindrome(ListNode head) {
        //0、翻转链表
        ListNode newhead = reverse(head);

        //1、计算链表的长度
        int len = 0;
        ListNode cur = head;
        while (cur != null) {
            cur = cur.next;
            len++;
        }
        //2、对比len/2个元素是否一致
        for (int i = 0; i < len / 2; i++) {
            if (head.val != newhead.val) {
                return false;
            }
            head = head.next;
            newhead = newhead.next;
        }
        return true;
    }

//    public static boolean isPalindrome(ListNode head) {
//        //0、保存原来链表
//        ListNode oldhead = head;
//
//        //1、翻转链表并且计算链表的长度
//        int len = 0;
//        ListNode newhead = null;
//        while (head != null) {
//            ListNode next = head.next;
//            head.next = newhead;
//            newhead = head;
//            head = next;
//            len++;
//        }
//        //2、对比len/2个元素是否一致
//        for (int i = 0; i < len / 2; i++) {
//            if (oldhead.val != newhead.val) {
//                return false;
//            }
//            oldhead = oldhead.next;
//            newhead = newhead.next;
//        }
//        return true;
//    }


    public static ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode pre = dummy;

        while(pre != null && pre.next != null && m > 1) {//找到要开始翻转节点的前一个节点
            pre = pre.next;
            m--;
        }

        ListNode rhead = pre.next;//保存翻转部分的头部节点

        ListNode rtail = pre.next;//找要翻转的尾部节点
        int i = n - m;
        while (rtail != null && i > 0) {
            rtail = rtail.next;
            i--;
        }
        ListNode tail = rtail.next;//rtail 这里理论上不会为null,其为链表的尾部
        rtail.next = null;//断开

        ListNode temp = reverse(pre.next);//翻转

        pre.next = temp;
        rhead.next = tail;

        return dummy.next;
    }


    public static ListNode reverse(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newhead = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = newhead;
            newhead = head;
            head = next;
        }
        return newhead;
    }
}
