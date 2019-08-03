package com.ls.lishuai.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 18:02
 方法区异常：
 -XX:PermSize20m -XX:MaxPermSize20m
 *
 * 总结：如果堆空间没有用完也抛出了OOM，有可能是永久区导致的。

　　 堆空间实际占用非常少，但是永久区溢出 一样抛出OOM。
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        // 使用List保持着常量池引用，避免Full GC回收常量池行为
        List<String> list = new ArrayList<String>();
        // 10MB的PermSize在integer范围内足够产生OOM了
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }



//    public static void main(String[] args) {
//
//            String str1 = new StringBuilder("中国").append("钓鱼岛").toString();
//            System.out.println(str1.intern() == str1);
//
//            String str2 = new StringBuilder("ja").append("va").toString();
//            System.out.println(str2.intern() == str2);
//        }
}
