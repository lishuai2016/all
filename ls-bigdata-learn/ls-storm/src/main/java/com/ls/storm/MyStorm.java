package com.ls.storm;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Describe: 请补充类描述
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/12/30.
 */
public class MyStorm {
    private Random random = new Random();

    //接受spout发出数据--队列1
    private BlockingQueue sentenceQueue = new ArrayBlockingQueue(50000);
    //接受bolt1发出的数据--队列2
    private BlockingQueue wordQueue = new ArrayBlockingQueue(50000);


    // 用来保存最后计算的结果key=单词，value=单词个数
    Map<String, Integer> counters = new HashMap<String, Integer>();

    //用来发送句子
    public void nextTuple() {
        String[] sentences = new String[]{"the cow jumped over the moon",
                "an apple a day keeps the doctor away",
                "four score and seven years ago",
                "snow white and the seven dwarfs", "i am at two with nature"};
        String sentence = sentences[random.nextInt(sentences.length)];
        try {
            sentenceQueue.put(sentence);
            System.out.println("send sentence:" + sentence);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //用来切割句子  bolt2-->execute--split
    public void split(String sentence) {
        System.out.println("resv sentence" + sentence);
        String[] words = sentence.split(" ");
        for (String word : words) {
            word = word.trim();
            if (!word.isEmpty()) {
                word = word.toLowerCase();
                wordQueue.add(word);
                System.out.println("split word:" + word);
            }
        }

    }

    //用来计算单词  bolt3-->execute
    public void wordcounter(String word) {
        if (!counters.containsKey(word)) {
            counters.put(word, 1);
        } else {
            Integer c = counters.get(word) + 1;
            counters.put(word, c);
        }
        System.out.println("print map:" + counters);
    }


    public static void main(String[] args) {
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        MyStorm myStorm = new MyStorm();
        //发射句子到sentenceQuequ
        executorService.submit(new MySpout(myStorm));
        //接受一个句子，并将句子切割
        executorService.submit(new MyBoltSplit(myStorm));
        //接受一个单词，并进行据算
        executorService.submit(new MyBoltWordCount(myStorm));
    }

    public BlockingQueue getSentenceQueue() {
        return sentenceQueue;
    }

    public void setSentenceQueue(BlockingQueue sentenceQueue) {
        this.sentenceQueue = sentenceQueue;
    }

    public BlockingQueue getWordQueue() {
        return wordQueue;
    }

    public void setWordQueue(BlockingQueue wordQueue) {
        this.wordQueue = wordQueue;
    }
}

class MySpout extends Thread {

    private MyStorm myStorm;

    public MySpout(MyStorm myStorm) {
        this.myStorm = myStorm;
    }

    @Override
    public void run() {
        //storm框架在循环调用spout的netxTuple方法
        while (true) {
            myStorm.nextTuple();
            try {
                this.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyBoltWordCount extends Thread {

    private MyStorm myStorm;


    @Override
    public void run() {
        while (true) {
            try {
                String word = (String) myStorm.getWordQueue().take();
                myStorm.wordcounter(word);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public MyBoltWordCount(MyStorm myStorm) {
        this.myStorm = myStorm;
    }
}

class MyBoltSplit extends Thread {

    private MyStorm myStorm;


    @Override
    public void run() {
        while (true) {
            try {
                String sentence = (String) myStorm.getSentenceQueue().take();
                myStorm.split(sentence);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public MyBoltSplit(MyStorm myStorm) {
        this.myStorm = myStorm;
    }
}
