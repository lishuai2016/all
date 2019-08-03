//package com.ls.lishuai.xianliu;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/8/3 9:33
// */
//public class Main {
//
//    public static void main(String[] args) throws Exception {
//        Field f = Unsafe.class.getDeclaredField("theUnsafe");
//        f.setAccessible(true);
//        Unsafe u = (Unsafe) f.get(null);
//        System.out.println(u);
//
//        int[] arr = {1,2,3,4,5,6,7,8,9,10};
//        int b = u.arrayBaseOffset(int[].class);
//        System.out.println(b);
//        int s = u.arrayIndexScale(int[].class);
//        System.out.println(s);
//        u.putInt(arr, (long)b+s*9, 1);
//        for(int i=0;i<10;i++){
//            int v = u.getInt(arr, (long)b+s*i);
//            System.out.print(v+"   ");
//        }
//    }
//
//
//}
