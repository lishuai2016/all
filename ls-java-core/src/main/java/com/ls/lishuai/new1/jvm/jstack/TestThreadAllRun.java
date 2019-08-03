package com.ls.lishuai.new1.jvm.jstack;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/26 14:00
 */
public class TestThreadAllRun {

    public static void main(String[] args) throws Exception{


            Thread t1 = new Thread("TestThreadAllRun"){
                @Override
                public void run() {
                    try {
                       while (true) {

                       }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t1.start();
            t1.join();


    }
}
