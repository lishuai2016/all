package com.ls.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-23 13:49
 */
public class HeapOOMError {
    public static void main(String[] args) {
        System.out.println();
        List<byte[]> list = new ArrayList<>();
        int i = 0;
        while (true) {
            list.add(new  byte[5*1024*1024]);
            System.out.println("count:"+(++i));
        }
    }
}
