package com.ls.storm.example.stormhbase;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 处理流程: flume--> kafka --> storm--> hbase
 * Created by lishuai on 2018/2/9.
 */
public class PersistentWordCount {
    private static final String WORD_SPOUT = "WORD_SPOUT";
    private static final String COUNT_BOLT = "COUNT_BOLT";
    private static final String HBASE_BOLT = "HBASE_BOLT";

    public static void main(String[] args) throws Exception {

//***************************************************************kafkaspout*****************************
        // config kafka spout   kafkaspout topic 的名称  这个topic名称可以在 flume的配置文件中
        String topic = "testflume";
        ZkHosts zkHosts = new ZkHosts("192.168.137.127:2181,192.168.137.126:2181,192.168.137.125:2181");
        //设置偏移量  每次不会从头开始读取 消息队列 而会从 上次读取的结尾读取 /fkshtest 目录用来存储偏移量 fkshApp 对应一个应用
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topic, "/fkshtest", "fkshApp");
        List<String> zkServers = new ArrayList<String>();
        for (String host : zkHosts.brokerZkStr.split(",")) {
            zkServers.add(host.split(":")[0]);
        }
        spoutConfig.zkServers = zkServers;
        spoutConfig.zkPort = 2181;
        //超时时间 100分钟
        spoutConfig.socketTimeoutMs = 60 * 1000 * 100;
        // 定义输出格式为String
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.forceFromStart = true; // 从头开始消费
        //创建  storm自带的kafkaspout
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        // set producer properties.
        Properties props = new Properties();
        props.put("metadata.broker.list", "192.168.137.127:9092,192.168.137.126:9092,192.168.137.125:9092");
        //ack机智 配置
        props.put("request.required.acks", "1"); // 0  1 -1
        props.put("serializer.class", "kafka.serializer.StringEnscoder");
       // conf.put("kafka.broker.properties", props);


//***************************************************************kafkaspout*****************************

        Config config = new Config();

        WordSpout spout = new WordSpout();
        WordCounterBolt bolt = new WordCounterBolt();
        MyHBaseBolt hbase = new MyHBaseBolt();

        // wordSpout ==> countBolt ==> HBaseBolt
        TopologyBuilder builder = new TopologyBuilder();

        //builder.setSpout(WORD_SPOUT, spout, 1);
        builder.setSpout(WORD_SPOUT, kafkaSpout, 2);//kafkaspout
        builder.setBolt("split", new SplitSentenceBolt(), 2).shuffleGrouping(WORD_SPOUT);
        builder.setBolt(COUNT_BOLT, bolt, 2).fieldsGrouping("split", new Fields("word"));
        builder.setBolt(HBASE_BOLT, hbase, 2).fieldsGrouping(COUNT_BOLT, new Fields("word"));

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("word", config, builder.createTopology());
//            Thread.sleep(10000);
//            cluster.killTopology("word");
//            cluster.shutdown();
//            System.exit(0);
        } else {
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }
    }

}
