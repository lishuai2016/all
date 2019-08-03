package com.ls.storm.wordcount;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

public class SplitSentenceBolt extends BaseBasicBolt {
	private static final long serialVersionUID = -5283595260540124273L;
	//该方法只会被调用一次，用来初始化
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
	}
	/**
	 * 接受的参数是RandomSentenceSpout发出的句子，即input的内容是句子 execute方法，将句子切割形成的单词发出
	 */
	public void execute(Tuple input, BasicOutputCollector collector) {
		String sentence = input.getString(0);
		String[] words = sentence.split(" ");
		for (String word : words) {
			word = word.trim();
			if (!word.isEmpty()) {
				word = word.toLowerCase();
				//System.out.println(word);
				collector.emit(new Values(word));
			}
		}
	}
	//消息源可以发射多条消息流stream。多条消息流可以理解为多种类型的数据。
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}
}