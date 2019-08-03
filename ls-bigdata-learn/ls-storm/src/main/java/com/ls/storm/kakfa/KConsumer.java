package com.ls.storm.kakfa;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * 
 * @author FromX
 *
 */
public class KConsumer {

	public KafkaConsumer<String, String> getConsmer() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "c1.wb3.com:6667,n1.wb1.com:6667");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		return consumer;
	}

	
	
	public static void main(String[] args) {
		KConsumer kconsumer =  new KConsumer();
		KafkaConsumer<String, String> consumer = kconsumer.getConsmer();
		
		consumer.subscribe(Arrays.asList("aaa"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records)
				System.out.println("offset =  "+record.offset()+", key = "+record.key()+", value = "+ record.value());
		}
	}
}
