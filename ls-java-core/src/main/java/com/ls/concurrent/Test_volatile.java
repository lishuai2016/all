package com.ls.concurrent;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08
 */
public class Test_volatile {
    private volatile int data=0;
    //线程1会读取和修改data变量的值
    //线程2会读取data变量的值
}
