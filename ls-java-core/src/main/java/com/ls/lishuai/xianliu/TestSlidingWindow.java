package com.ls.lishuai.xianliu;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/3 10:48
 */
public class TestSlidingWindow {
    public static void main(String[] args) {

        final SlidingWindow window = new SlidingWindow(1,60);
        for (int i = 0;i < 10;i++) {
            new Thread(){
                @Override
                public void run() {
                    if (window.allow(5)) {
                        System.out.println("执行业务操作");
                    } else {
                        System.out.println("限流");
                    }
                }
            }.start();
        }
        System.out.println();
    }
}
