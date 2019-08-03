package com.ls.lishuai.jvm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/8 11:17
 */
public class PermGenOomMock {
    public static void main(String[] args) {
        URL url = null;
        List<ClassLoader> classLoaderList = new ArrayList<ClassLoader>();
        try {
            url = new File("C:/Users/lishuai29/Desktop/temp").toURI().toURL();
            URL[] urls = {url};
            while (true){
                ClassLoader loader = new URLClassLoader(urls);
                classLoaderList.add(loader);
                loader.loadClass("com.paddx.test.memory.Test");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
