package com.ls.storm.example.double11;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * Describe: 请补充类描述
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/3.
 */
public class Save2RedisBlot extends BaseBasicBolt {
    private JedisPool pool;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        //change "maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis" in all examples
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setMaxTotal(1000 * 100);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(30);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        /**
         *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
         *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
         */
        pool = new JedisPool(config, "127.0.0.1", 6379);
        super.prepare(stormConf, context);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //读取订单数据
        String paymentInfoStr = input.getStringByField("message");
        //将订单数据解析成JavaBean
        PaymentInfo paymentInfo = new Gson().fromJson(paymentInfoStr, PaymentInfo.class);
        //计算业务订单量
        Jedis jedis = pool.getResource();
        if (paymentInfo != null) {
            //计算订单的总数
            jedis.incrBy("orderTotalNum", 1);
            //计算总的销售额
            jedis.incrBy("orderTotalPrice", paymentInfo.getProductPrice());
            //计算折扣后的销售额
            jedis.incrBy("orderPromotionPrice", paymentInfo.getPromotionPrice());
            //计算实际交易额
            jedis.incrBy("orderTotalRealPay", paymentInfo.getPayPrice());
            jedis.incrBy("userNum", 1);
        }
        System.out.println("订单总数：" + jedis.get("orderTotalNum") +
                "   销售额" + jedis.get("orderTotalPrice") +
                "   交易额" + jedis.get("orderPromotionPrice") +
                "   实际支付：" + jedis.get("orderTotalRealPay") +
                "   下单用户：" + jedis.get("userNum"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
