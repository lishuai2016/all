package com.ls.hadoop.hdfs;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: lishuai
 * @CreateDate: 2018/9/9 17:17
set1 [a, b, c, d]
set2 [c, d, e, f]
[c, d]
set1 [a, b, c, d]
set2 [c, d, e, f]
[a, b]
set1 [a, b, c, d]
set2 [c, d, e, f]
[e, f]
set1 [a, b, c, d]
set2 [c, d, e, f]
 */
public class Main2 {
    public static void main(String[] args) throws Exception {
        Set<FIleBean> set1 = new HashSet<>();
        Set<FIleBean> set2 = new HashSet<>();

        FIleBean f1 = new FIleBean();
        f1.setFilename("/a");
        f1.setFilesize(10);
        f1.setFiletype(1);


        FIleBean f2 = new FIleBean();
        f2.setFilename("/b");
        f2.setFilesize(10);
        f2.setFiletype(1);


        FIleBean f3 = new FIleBean();
        f3.setFilename("/c");
        f3.setFilesize(10);
        f3.setFiletype(1);

        set1.add(f1);
        set1.add(f2);

        set2.add(f1);
        set2.add(f3);



        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);
        System.out.println("set1和set2的交集"+intersectionSet(new HashSet<FIleBean>(set1),new HashSet<FIleBean>(set2)));
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);
        System.out.println("set1-set2差集"+differenceSet(new HashSet<FIleBean>(set1),new HashSet<FIleBean>(set2)));
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);
        System.out.println("set2-set1差集"+differenceSet(new HashSet<FIleBean>(set2),new HashSet<FIleBean>(set1)));
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);





    }


    public static Set<FIleBean> intersectionSet(Set<FIleBean> set1, Set<FIleBean> set2) {
        set1.retainAll(set2);
        return set1;
    }


    public static Set<FIleBean> differenceSet(Set<FIleBean> set1, Set<FIleBean> set2) {
        set1.removeAll(set2);
        return set1;
    }
}
