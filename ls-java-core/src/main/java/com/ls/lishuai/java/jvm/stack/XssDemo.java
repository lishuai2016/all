package com.ls.lishuai.java.jvm.stack;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/24 10:53
 */
/**
 * -Xss128K   deep of calling = 675
 * -Xss256K   deep of calling = 1686
 *
 * Xss越大，每个线程的大小就越大，占用的内存越多，能容纳的线程就越少
 * Xss越小，则递归的深度越小，容易出现栈溢出  java.lang.StackOverflowError
 * 减少局部变量的声明，可以节省栈帧大小，增加调用深度
 */
public class XssDemo {
    private static int count=0;
    public static void recursion(){
        //减少局部变量的声明，可以节省栈帧大小，增加调用深度
        long a=1,b=2,c=3,d=4,e=5,f=6,q=7,x=8,y=9,z=10;
        count++;
        recursion();
    }
    public static void main(String args[]){
        try{
            recursion();
        }catch(Throwable e){
            System.out.println("deep of calling = "+count);
            e.printStackTrace();
        }
    }
}
