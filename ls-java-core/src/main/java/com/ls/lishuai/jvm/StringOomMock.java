package com.ls.lishuai.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/8 11:36
 */
public class StringOomMock {
    static String  base = "string";
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        for (int i=0;i< Integer.MAX_VALUE;i++){
            String str = base + base;
            base = str;
            list.add(str.intern());
        }
    }
}
