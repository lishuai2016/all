package com.ls.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @program: ls-bigdata-learn
 * @author: lishuai
 * @create: 2018-12-10 21:00
 */
@Component
public class KafkaReceiver {

    @KafkaListener(topics = {"lishuai"})
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();

            System.out.println("----------------- record =" + record);
            System.out.println("------------------ message =" + message);
        }

    }
}
