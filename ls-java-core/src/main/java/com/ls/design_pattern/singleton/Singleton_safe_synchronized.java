package com.ls.design_pattern.singleton;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03
 */
public class Singleton_safe_synchronized {
    private Singleton_safe_synchronized() {

    }
    private static Singleton_safe_synchronized instance;

    public static synchronized Singleton_safe_synchronized getInstance() {
        if (instance ==null) {
            instance = new Singleton_safe_synchronized();
        }
        return  instance;
    }
}
