package com.ls.lishuai.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 18:18
 *
 *
Xss：设置栈空间的大小。通常只有几百K，决定了函数调用的深度
每个线程都有独立的栈空间，局部变量、参数 分配在栈上
注：栈空间是每个线程私有的区域。栈里面的主要内容是栈帧，而栈帧存放的是局部变量表，局部变量表的内容是：局部变量、参数。
我们来看下面这段代码：（没有出口的递归调用）


如果设置栈大小为128k：-Xss128K   (306这个数字大概，每次运行会有浮动)
如果设置栈大小为256k：-Xss256K    （758）

 */
public class TestStackDeep {
    private static int count = 0;
    public static void recursion(long a, long b, long c) {
        long e = 1, f = 2, g = 3, h = 4, i = 5, k = 6, q = 7, x = 8, y = 9, z = 10;
        count++;
        recursion(a, b, c);
    }
    public static void main(String args[]) {
        try {
            recursion(0L, 0L, 0L);
        } catch (Throwable e) {
            System.out.println("deep of calling = " + count);
            e.printStackTrace();
        }
    }
}
