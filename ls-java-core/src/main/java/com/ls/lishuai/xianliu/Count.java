package com.ls.lishuai.xianliu;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/2 20:21
 *
 * https://mp.weixin.qq.com/s?__biz=MjM5NzMyMjUwMg==&mid=2247484653&idx=1&sn=d3ebb9d82e90cbdc937a6dd8ba819500&chksm=a6da8e8491ad07920daf6c6db4e52fa6e1904a88938543d62e5a26a1a94b870cd6ddcc2ffeb7&mpshare=1&scene=1&srcid=0802IBoMhH617M4NNNTKSWNk#rd
 */
public class Count {

    private static long timestamp = System.currentTimeMillis();
    private static long limitcount = 100;
    private static long interval = 1000;
    private static long reqcount = 0;

    public static boolean grant() {
        long now = System.currentTimeMillis();
        if (now - timestamp < interval) {
            if (reqcount < limitcount) {
                reqcount++;
                return true;
            } else {
                return false;
            }
        } else {
            timestamp = System.currentTimeMillis();
            reqcount = 0;
            return false;
        }
    }

    public static void main(String[] args) {
        for (int i = 0;i < 500;i++) {
            new Thread(){
                @Override
                public void run() {
                   if (grant()) {
                       System.out.println("执行业务操作");
                   } else {
                       System.out.println("限流");
                   }
                }
            }.start();
        }

    }
}
