package com.ls.li.Leetcode.tree;

import java.util.Stack;

public class BinTree {    
    private char date;    
    private BinTree lchild;
    private BinTree rchild;
    
    public BinTree(char c) {    
        date = c;    
    }    
    
    // 先序遍历递归     
    public static void preOrder(BinTree t) {
        if (t == null) {    
            return;    
        }    
        System.out.print(t.date);    
        preOrder(t.lchild);    
        preOrder(t.rchild);    
    }    
    
    // 中序遍历递归     
    public static void InOrder(BinTree t) {
        if (t == null) {    
            return;    
        }    
        InOrder(t.lchild);    
        System.out.print(t.date);    
        InOrder(t.rchild);    
    }    
    
    // 后序遍历递归     
    public static void PostOrder(BinTree t) {
        if (t == null) {    
            return;    
        }    
        PostOrder(t.lchild);    
        PostOrder(t.rchild);    
        System.out.print(t.date);    
    }    
    
    // 先序遍历非递归     
    public static void preOrder2(BinTree t) {
        Stack<BinTree> s = new Stack<BinTree>();
        while (t != null || !s.empty()) {    
            while (t != null) {    
                System.out.print(t.date);    
                s.push(t);    
                t = t.lchild;    
            }    
            if (!s.empty()) {    
                t = s.pop();    
                t = t.rchild;    
            }    
        }    
    }    
    
    // 中序遍历非递归     
    public static void InOrder2(BinTree t) {
        Stack<BinTree> s = new Stack<BinTree>();
        while (t != null || !s.empty()) {    
            while (t != null) {    
                s.push(t);    
                t = t.lchild;    
            }    
            if (!s.empty()) {    
                t = s.pop();    
                System.out.print(t.date);    
                t = t.rchild;    
            }    
        }    
    }    
    
    // 后序遍历非递归     
    public static void PostOrder2(BinTree t) {
        Stack<BinTree> s = new Stack<BinTree>();
        Stack<Integer> s2 = new Stack<Integer>();    
        Integer i = new Integer(1);    
        while (t != null || !s.empty()) {    
            while (t != null) {    
                s.push(t);    
                s2.push(new Integer(0));    
                t = t.lchild;    
            }    
            while (!s.empty() && s2.peek().equals(i)) {    
                s2.pop();    
                System.out.print(s.pop().date);    
            }    
    
            if (!s.empty()) {    
                s2.pop();    
                s2.push(new Integer(1));    
                t = s.peek();    
                t = t.rchild;    
            }    
        }    
    }    
    
    public static void main(String[] args) {    
        BinTree b1 = new BinTree('a');
        BinTree b2 = new BinTree('b');
        BinTree b3 = new BinTree('c');
        BinTree b4 = new BinTree('d');
        BinTree b5 = new BinTree('e');
    
        /**  
         *      a   
         *     / \  
         *    b   c  
         *   / \  
         *  d   e  
         */    
        b1.lchild = b2;    
        b1.rchild = b3;    
        b2.lchild = b4;    
        b2.rchild = b5;    
    
//        BinTree.preOrder(b1);    
//        System.out.println();    
//        BinTree.preOrder2(b1);    
//        System.out.println();    
//        BinTree.InOrder(b1);    
//        System.out.println();    
//        BinTree.InOrder2(b1);    
//        System.out.println();    
//        BinTree.PostOrder(b1);    
        System.out.println();    
        BinTree.PostOrder2(b1);
    }    
}    