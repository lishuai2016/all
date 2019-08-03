package com.ls.storm.kakfa;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 
 * @author FromX
 *
 */
public class KProducer {

	public static void main(String[] args) throws InterruptedException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "slave1.com:6667,slave2.com:6667,slave3.com:6667");
		//props.put("acks", "1");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("kafka.partitions", 12);
		
		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 50; i++){
			System.out.println("key-->key"+i+"  value-->vvv"+i);
			producer.send(new ProducerRecord<String, String>("aaa", "key"+i, "vvv"+i));
			Thread.sleep(1000);
		}
			  
		producer.close();
	}
}
