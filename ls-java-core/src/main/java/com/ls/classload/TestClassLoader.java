package com.ls.classload;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 22:57
 */
public class TestClassLoader {
    public static void main(String[] args) {
        ClassLoader loader = TestClassLoader.class.getClassLoader();
        System.out.println(loader.toString());
        System.out.println(loader.getParent().toString());
        System.out.println(loader.getParent().getParent());
    }
}
