# 83. Remove Duplicates from Sorted List

- [remove-duplicates-from-sorted-list](https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/)


## 描述
```
给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。

示例 1:
输入: 1->1->2
输出: 1->2

示例 2:
输入: 1->1->2->3->3
输出: 1->2->3
```




## 思路
拿当前值和下一个节点的值比较是否相等，不相等的话指针后移；相等的话，修改当前节点执行下下个节点，游标指针不移动


## 题解
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
        ListNode cur = head;
        while (cur != null && cur.next != null) {
           if (cur.val == cur.next.val) {
               cur.next = cur.next.next;
           } else {
               cur = cur.next;
           }
        }
        return head;
    }
}

```