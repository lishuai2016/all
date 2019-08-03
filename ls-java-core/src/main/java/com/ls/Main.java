package com.ls;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-03-23 20:30
 */
public class Main {


    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        int nums = rows * cols;

        int left = 0;
        int right = nums - 1;
        int index =  (right - left ) / 2;
        while (index >= left && index <= right && left <= right) {
            int row = index / cols;
            int col = index % cols;
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] > target) {
                right = index - 1;
            } else {
                left = index + 1;
            }
            index = (right - left ) / 2;//更新索引
            if (left == right && matrix[left / cols][left % cols] == target) {
                return true;
            }
        }
        return false;
    }

    public static int[] twoSum(int[] numbers, int target) {
        int[] res = new int[2];
        backtracting(numbers,res,new ArrayList<Integer>(),0,numbers.length,target,2);
        return res;
    }

    public static void backtracting(int[] numbers, int[] res, List<Integer> list, int start, int end, int target, int k) {
        if (k == 0 && target == 0) {
            res[0] = list.get(0);
            res[1] = list.get(1);
            return;
        }
        for (int i = start; i < end && k > 0; i++) {
            list.add(i + 1);
            backtracting(numbers,res,list,i + 1,end,target - numbers[i],k - 1);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
//        System.out.println( 0 / 4);
//        System.out.println( 0 % 4);
//        int[][] arr = {{1,  1},{2,2}};
//        boolean b = searchMatrix(arr, 2);
        int[] arr = new int[]{-1 ,0};
        int[] res = new int[2];
        twoSum(arr,-1);
        System.out.println( res);


//        ListNode[] lists = new ListNode[3];
//        ListNode head10 = new ListNode(1);
//        ListNode head11 = new ListNode(4);
//        ListNode head12 = new ListNode(5);
//        head10.next = head11;
//        head11.next = head12;
//
//        ListNode head20 = new ListNode(1);
//        ListNode head21= new ListNode(3);
//        ListNode head22 = new ListNode(4);
//        head20.next = head21;
//        head21.next = head22;
//
//        ListNode head31 = new ListNode(2);
//        ListNode head32 = new ListNode(6);
//        head31.next = head32;
//        lists[0] = head10;
//        lists[1] = head20;
//        lists[2] = head31;
//        mergeKLists(lists);

    }

    public static ListNode mergeKLists(ListNode[] lists) {
        ListNode dummy = new ListNode(-1);
        ListNode p = dummy;
        while (true) {
            int min = -1;
            //找数组中第一个非空的链表当最小的下标
            for (int i = 0; i < lists.length && lists[i] != null; i++) {
                min = i;
                break;
            }
            if (min == -1) break;//跳出循环
            //找最小值下标
            for (int j = 0; j < lists.length && lists[j] != null; j++) {
                if (lists[j].val < lists[min].val) {
                    min = j;
                }
            }
            ListNode node = lists[min];
            ListNode temp = lists[min].next;//保存当前值的下一个节点
            lists[min].next = null;//断开之前的链接

            p.next = node;//保存结果
            p = p.next;

            lists[min] = temp;//更新数组中存放的对象
        }
        return dummy.next;

    }



}
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
