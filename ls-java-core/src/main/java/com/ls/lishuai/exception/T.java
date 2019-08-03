package com.ls.lishuai.exception;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 9:01
 * System.out.println(e.getMessage());  //输出：我抛出的异常是：11111111111111
 */
public class T {
    public static void main(String[] args){
        try {
            method();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void method() throws Exception {
        try {
            method3();
        } catch (Exception e) {
           throw e;
        }
    }
    public static void method3() throws Exception{
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            throw new Exception("除数不能为0",e);
        }
    }


    public static void method1() throws Exception{
        throw new Exception("我抛出的异常是：11111111111111");
    }
}
