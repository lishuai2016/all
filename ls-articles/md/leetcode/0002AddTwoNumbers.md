# 2. Add Two Numbers 两数相加

[add-two-numbers](https://leetcode-cn.com/problems/add-two-numbers/)

## 题目描述
给出两个 **非空** 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 **逆序** 的方式存储的，并且它们的每个节点只能存储 **一位** 数字。

如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。

您可以假设除了数字 0 之外，这两个数都不会以 0 开头。

示例：
输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
输出：7 -> 0 -> 8
原因：342 + 465 = 807

示例：
输入
[1,4,3]
[5,6,4]
输出
[6,0,8]
备注：需要读懂题意，链表的头结点是最低位

## 题目描述

## 思路
- 思路1：逐个相加，并且需要考虑到进为以及会出现比原先链表长度大的情况，这样最后一个节点为最高位，最后再次把结果翻转一下即可； 
- 思路2：借助一个dummy节点，可以避免一次链表的翻转[推荐，代码比较简洁，是思路1的简化版]


## 答案

### 思路1
```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head1 = l1;
        ListNode head2 = l2;
        
        ListNode head = null;
        
        int flag = 0;
        while (head1 != null && head2 != null) {
            int val = head1.val + head2.val + flag;
            if (val >= 10) {
                val -= 10;
                flag = 1;
            } else {
                flag = 0;
            }
            ListNode node = new ListNode(val);
            node.next = head;
            head = node;
            
            head1 = head1.next;
            head2 = head2.next;
        }
        
        while (head1 != null) {
            int val = head1.val + flag;
            if (val >= 10) {
                val -= 10;
                flag = 1;
            } else {
                flag = 0;
            }
            ListNode node = new ListNode(val);
            node.next = head;
            head = node;
            
            head1 = head1.next;
        }
        
         while (head2 != null) {
            int val = head2.val + flag;
            if (val >= 10) {
                val -= 10;
                flag = 1;
            } else {
                flag = 0;
            }
            ListNode node = new ListNode(val);
            node.next = head;
            head = node;
            
            head2 = head2.next;
        }
        
        if (flag == 1) {
            ListNode node = new ListNode(1);
            node.next = head;
            head = node;
        }
        
        
        return reverseList(head); //输出各位数是头结点
    }
    
    
    public ListNode reverseList(ListNode head) {
        
        ListNode newhead = null;
        while (head != null) {   //head != null && head.next != null  注意该条件不能是这样的，否则丢失最后一个节点
            ListNode temp = head.next;
            head.next = newhead;
            newhead = head;
            head = temp;
        }
        return newhead;
    }
}
```

### 思路2

```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);//指向链表的头节点
        ListNode p = dummy; //游标指针，指向链表的最后一个节点
        int sum  = 0;
        while (l1 != null || l2 != null) {
            sum /= 10; //这里相当于进位处理
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            ListNode node = new ListNode(sum % 10);
            p.next = node;
            p = node;
        }
        if (sum / 10 == 1) {
            p.next = new ListNode(1);
        }
        return dummy.next;
    }
    
   
}
```

