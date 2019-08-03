package com.ls.lishuai.java.jvm.p1;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/23 10:36
 */
public class NotInitialization {
    public static void main(String[] args) {
        //System.out.println(SubClass.value1);
//SuperClass init!
//123

/**
 * 被动使用类字段演示二：
 * 通过数组定义来引用类，不会触发此类的初始化
 **/
        SubClass[] sca = new SubClass[10];

    }
}
