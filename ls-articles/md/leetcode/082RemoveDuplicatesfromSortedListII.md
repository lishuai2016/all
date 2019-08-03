# 82. 删除排序链表中的重复元素 II

- [remove-duplicates-from-sorted-list-ii](https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/)

## 描述

给定一个排序链表，删除所有含有重复数字的节点，只保留原始链表中 没有重复出现 的数字。

示例 1:

输入: 1->2->3->3->4->4->5
输出: 1->2->5

示例 2:

输入: 1->1->1->2->3
输出: 2->3

## 思路

- 思路1
使用两个指针和一个是否出现重复数字标记flag变量

- 思路2
原理是首先判断连着的节点是否有重复值，是的话，使用中间变量保存重复节点的val，然后把包含该value节点的值都删除掉



## 题解



### 思路1

```
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode cur = dummy;
        while (cur != null && cur.next != null) {
            boolean flag = false;
            ListNode node = cur.next;//基准节点
            
            ListNode next = node.next; //第一个不同的节点
            while (next != null && node.val == next.val) {
                next = next.next;
                flag = true;
            }
            if (flag) {
                cur.next = next; //需要删除操作
            } else {
                cur = cur.next; //不进行删除的时候才进行游标指针的后移，否则会保留一个重复值
            }
        }
        return dummy.next;
    }
}

```

对上面进行的优化
```
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur != null) {
            while (cur.next != null && cur.val == cur.next.val) {//找第一个不重复的值
                cur = cur.next;
            }
            if (pre.next == cur) {//没有重复值，前指针后移一步
                pre = pre.next;
            } else {
                pre.next = cur.next;//有重复值，进行删除
            }
            cur = cur.next;
        }
        return dummy.next;
    }
}
```



### 思路2
```
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode pre = dummy;
        head = dummy;
        while (head.next != null && head.next.next != null) {
            if (head.next.val == head.next.next.val) {
                int val = head.next.val;
                while (head.next != null && head.next.val == val) {
                    head.next = head.next.next;
                }
            } else {
                head = head.next;
            }
        }
        return dummy.next;
    }
}

```