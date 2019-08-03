package com.ls.lishuai.java.concurrent.produce_consumer;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/20 13:59
 */
public class TestStorage {
    public static void main(String[] args) {
        final Storage2 storage = new Storage2();

        for(int i=1;i<6;i++) {
            final   int  finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    storage.produce(String.format("生成者%d:", finalI));
                }
            }).start();
        }

        for(int i=1;i<4;i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    storage.consume(String.format("消费者%d:", finalI));
                }
            }).start();
        }
    }
}
