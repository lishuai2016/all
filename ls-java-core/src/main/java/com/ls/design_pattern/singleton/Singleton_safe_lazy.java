package com.ls.design_pattern.singleton;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:04
 */
public class Singleton_safe_lazy {
    private Singleton_safe_lazy() {

    }
    private static Singleton_safe_lazy instance = new Singleton_safe_lazy();
    public static Singleton_safe_lazy getInstance() {
        return instance;
    }
}
