package com.ls.design_pattern.singleton;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 13:25
 */
public class Singleton_unsafe {

    private static Singleton_unsafe instance;
    private Singleton_unsafe(){

    }
    public static Singleton_unsafe getInstance() {
        if (instance == null) {
            instance = new Singleton_unsafe();
        }
        return instance;
    }
}
