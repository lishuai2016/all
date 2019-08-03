package com.ls.thread;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-04
 */
public class GcExample {

    private static class E {
        public static final int[] a = new int[10*1024];
    }

    public static void main(String[] args) {
        System.out.println("hello");
        while (true) {
            new E();
        }
    }
}
