package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/23 10:11
 */
public class Test {
    static {
        i = 0;  //  给变量复制可以正常编译通过

    }
    static int i = 1;

    static int j = 1;

    static{
        j = 2;
        System.out.println(i);  // 这句编译器会提示“非法向前引用”
    }

    public static void main(String[] args){
        System.out.println(Test.i);  //1
        System.out.println(Test.j); //2
    }

}
