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
public class Main {
    public static void main(String[] args) throws Exception {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        set1.add("a");
        set1.add("b");
        set1.add("c");
        set1.add("d");

        set2.add("c");
        set2.add("d");
        set2.add("e");
        set2.add("f");
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);
        System.out.println(intersectionSet(new HashSet<>(set1),new HashSet<>(set2)));
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);
        System.out.println(differenceSet(new HashSet<>(set1),new HashSet<>(set2)));
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);
        System.out.println(differenceSet(new HashSet<>(set2),new HashSet<>(set1)));
        System.out.println("set1 " + set1);
        System.out.println("set2 " + set2);





    }


    public static Set<String> intersectionSet(Set<String> set1, Set<String> set2) {
        set1.retainAll(set2);
        return set1;
    }


    public static Set<String> differenceSet(Set<String> set1, Set<String> set2) {
        set1.removeAll(set2);
        return set1;
    }
}
