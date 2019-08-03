package com.ls.storm.example.double11;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Describe: 请补充类描述
 * Author:   maoxiangyi
 */
public class FilterMessageBlot extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //读取订单数据
        String paymentInfoStr = input.getStringByField("paymentInfo");

        //将订单数据解析成JavaBean
        PaymentInfo paymentInfo = new Gson().fromJson(paymentInfoStr, PaymentInfo.class);
        // 过滤订单时间,如果订单时间在2015.11.11这天才进入下游开始计算
        Date date = paymentInfo.getCreateOrderTime();
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 9) {
            return;
        }
        collector.emit(new Values(paymentInfoStr));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("message"));
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }
}
