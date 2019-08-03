package com.ls.storm.example.double11;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Describe: 请补充类描述
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/3.
 */
public class PaymentInfoSpout extends BaseRichSpout {
    private static final String TOPIC = "paymentInfo";
    private Properties props;
    private ConsumerConnector consumer;
    private SpoutOutputCollector collector;
    //ArrayBlockingQueue是一个由数组支持的有界阻塞队列。此队列按 FIFO（先进先出）原则对元素进行排序。
    // 队列的头部 是在队列中存在时间最长的元素
    private  ArrayBlockingQueue<String>  paymentInfoQueue = new ArrayBlockingQueue<String>(100);

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("paymentInfo"));
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector =collector;

        props = new Properties();
        props.put("zookeeper.connect", "zk01:2181,zk02:2181,zk03:2181");
        props.put("group.id", "testGroup");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(TOPIC).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            paymentInfoQueue.add(new String(it.next().message()));
        }
    }

    @Override
    public void nextTuple() {
        try {
            collector.emit(new Values(paymentInfoQueue.take()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
