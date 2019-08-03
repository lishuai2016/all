package com.ls.design_pattern.Builder.example;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:18
 */
public class Client {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        final int count = 26;
        for (int i = 0; i < count; i++) {
            sb.append((char) ('a' + i));
        }
        System.out.println(sb.toString());
    }
}
