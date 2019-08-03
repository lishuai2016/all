package com.ls.lishuai.exception;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 10:08
 */
public class T1 {
    public static void main(String[] args){
        int re = bar2();
        System.out.println(re);
    }

    private static int bar()
    {
        try{
            return 5;
        } finally{
            System.out.println("finally");
        }
    }

    private static int bar1()
    {
        try{
            int b = 1 / 0;
            return 5;
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }finally{
            System.out.println("finally");
        }
    }

    private static int bar2()
    {
        try{
            int b = 1 / 0;
            return 1;
        } catch (Exception e){
            return 2;
        }finally{
            System.out.println("finally");
            return 3;
        }
    }
    private static int bar3()
    {
        try{
            int b = 1 / 0;
            return 1;
        } catch (Exception e){
            return 2;
        }
    }
}
