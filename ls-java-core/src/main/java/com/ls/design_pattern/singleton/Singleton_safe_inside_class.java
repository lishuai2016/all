package com.ls.design_pattern.singleton;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:24
 */
public class Singleton_safe_inside_class {
    private Singleton_safe_inside_class() {

    }
    private static Singleton_safe_inside_class instance;

    public static Singleton_safe_inside_class getInstance() {
        return SingletonHolder.instance;
    }

    //内部的静态类
    private static class SingletonHolder {
        private static final Singleton_safe_inside_class instance = new Singleton_safe_inside_class();
    }
}
