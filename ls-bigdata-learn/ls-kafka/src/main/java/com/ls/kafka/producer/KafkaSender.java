package com.ls.kafka.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ls.kafka.beans.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @program: ls-bigdata-learn
 * @author: lishuai
 * @create: 2018-12-10 20:57
 */

@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        System.out.println("生产者产生消息："+gson.toJson(message));
        kafkaTemplate.send("lishuai", gson.toJson(message));
    }
}
