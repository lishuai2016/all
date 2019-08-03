package com.ls.lishuai.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:28
 *
Xmx=20.0M
free mem=17.4251708984375M
total mem=20.0M

-Xmx20m -Xms20m 最大堆和最小堆

 */
public class Main {

    public static void main(String[] args){
        //byte[] b = new byte[1 * 1024 * 1024]; System.out.println("分配了1M空间给数组");
        byte[] b = new byte[1 * 1024 * 1024];

        System.out.println("Xmx=" + Runtime.getRuntime().maxMemory() / 1024.0 / 1024 + "M"); //系统的最大空间
        System.out.println("free mem=" + Runtime.getRuntime().freeMemory() / 1024.0 / 1024 + "M"); //系统的空闲空间
        System.out.println("total mem=" + Runtime.getRuntime().totalMemory() / 1024.0 / 1024 + "M"); //当前总空间

    }

}


