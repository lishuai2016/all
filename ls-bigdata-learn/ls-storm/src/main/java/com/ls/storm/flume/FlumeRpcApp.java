package com.ls.storm.flume;


import org.apache.flume.api.RpcClient;

import java.util.Random;

/**
 * Created by FromX on 2017/3/15.
 *
 * 向flume监听的端口发送数据
 *
 */
public class FlumeRpcApp {


    public static void main(String[] args) {

        RpcClient client = FlumeRPCClient.createClient("172.17.201.70", 44444);
        Random _rand = new Random();
        //随机发送句子

        String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
                "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature"
                ,"难道 没有 中文 吗"};

        for (int i=0;i<100;i++){
            String data =sentences[_rand.nextInt(sentences.length)];
            FlumeRPCClient.sendData(client, data);
            System.out.println("sendData--------->"+data);
        }
        client.close();

    }
}
