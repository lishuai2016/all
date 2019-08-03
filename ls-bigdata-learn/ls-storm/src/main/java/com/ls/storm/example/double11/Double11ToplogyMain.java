package com.ls.storm.example.double11;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * 程序说明:
 * 根据双十一当天的订单mq，快速计算当天的订单量、销售金额
 * 思路：
 * 1,支付系统发送mq到kafka集群中，编写storm程序消费kafka的数据并计算实时的订单数量、订单数量
 * 2,将计算的实时结果保存在redis中
 * 3,外部程序实时展示结果
 * 程序设计
 * 数据产生：编写kafka数据生产者，模拟订单系统发送mq
 * 数据输入：使用PaymentSpout消费kafka中的数据
 * 数据计算：使用CountBolt对数据进行统计
 * 数据存储：使用Sava2RedisBolt对数据进行存储
 * 数据展示：编写java app客户端，对数据进行展示，展示方式为打印在控制台。
 * Data:     2015/11/3.
 */
public class Double11ToplogyMain {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("readPaymentInfo", new TestPaymentInfoSpout(),10);
        builder.setBolt("processIndex", new FilterMessageBlot(), 2).shuffleGrouping("readPaymentInfo");
        builder.setBolt("saveResult2Redis", new Save2RedisBlot(), 2).shuffleGrouping("processIndex");
        Config conf = new Config();
        conf.setDebug(true);
        if (args != null && args.length > 0) {
            conf.setNumWorkers(2);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("double11", conf, builder.createTopology());
			Utils.sleep(10000);
//			cluster.shutdown();
        }
    }
}
