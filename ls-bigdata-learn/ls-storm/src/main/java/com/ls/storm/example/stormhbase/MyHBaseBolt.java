package com.ls.storm.example.stormhbase;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lishuai on 2018/2/9.
 */
public class MyHBaseBolt extends BaseBasicBolt {
    private Connection connection;
    private Table table;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum","master.hadoop,slave1.hadoop,slave2.hadoop");
        try {
            connection = ConnectionFactory.createConnection(config);
//示例都是对同一个table进行操作，因此直接将Table对象的创建放在了prepare，在bolt执行过程中可以直接重用。
            table = connection.getTable(TableName.valueOf("WordCount"));
        } catch (IOException e) {
            //do something to handle exception
        }
    }
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //从tuple中获取单词
        String word = tuple.getString(0);
        //从tuple中获取计数，这里转换为String只是为了示例运行后存入hbase的计数值能够直观显示。
        String count = tuple.getInteger(1).toString();
        try {
            //以各个单词作为row key
            Put put = new Put(Bytes.toBytes(word));
            //将被计数的单词写入cf:words列
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("words"), Bytes.toBytes(word));
            //将单词的计数写入cf:counts列
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("counts"), Bytes.toBytes(count));
            table.put(put);
        } catch (IOException e) {
            //do something to handle exception
        }
    }
    @Override
    public void cleanup() {
        //关闭table
        try {
            if(table != null) table.close();
        } catch (Exception e){
            //do something to handle exception
        } finally {
            //在finally中关闭connection
            try {
                connection.close();
            } catch (IOException e) {
                //do something to handle exception
            }
        }
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //示例中本bolt不向外发射数据，所以没有再做声明
    }
}

