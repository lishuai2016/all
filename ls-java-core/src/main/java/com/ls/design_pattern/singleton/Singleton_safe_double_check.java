package com.ls.design_pattern.singleton;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:09
 */
public class Singleton_safe_double_check {
    private Singleton_safe_double_check() {

    }
    private static volatile Singleton_safe_double_check instance;

    public static Singleton_safe_double_check getInstance() {
        if (instance == null) {
            synchronized (Singleton_safe_double_check.class) {
                if (instance == null) {
                    instance = new Singleton_safe_double_check();
                }
            }
        }
        return instance;
    }
}
