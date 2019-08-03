package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:48
 */
public class Output {
    public static String printWhenInit(String s){
        System.out.println(s);
        return s.substring(s.indexOf(" "));
    }
}
