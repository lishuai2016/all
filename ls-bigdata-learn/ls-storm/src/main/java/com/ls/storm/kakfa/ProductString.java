package com.ls.storm.kakfa;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;

/**
 * Created by FromX on 2017/3/17.
 * 产生随机的字符串
 */
public class ProductString {


    public static void main(String[] args) throws InterruptedException {
        String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
                "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature"
                ,"Pay to Caesar what belongs to Caesar and God what belongs to God"};
        Random _rand = new Random();
        Properties props = new Properties();
        props.put("bootstrap.servers", "c2.wb.com:6667,c1.wb.com:6667,m1.wb.com:6667");
        //props.put("ack", "1");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("kafka.partitions", 12);

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i=0;i<1000;i++){
            Thread.sleep(1000);
            String data =sentences[_rand.nextInt(sentences.length)];
            producer.send(new ProducerRecord<String, String>("TestStringTopic",data));
            System.out.println("sendData--------->"+data);
        }


    }
}
