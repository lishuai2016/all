package com.ls.hadoop.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCount {


	/** map
	 * 输入为key:value    key  一行的起始偏移量,这里为Object类型的key ，一行的文本内容Text作为value，这里为Text类型的value
	 * 输出也是为key:value     key为每个切分的单词，value为1，单词的计数
	 */
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
		public void map(Object key, Text value, Context context)
		        throws IOException, InterruptedException {
			//StringTokenizer是字符串分隔解析类型，分隔符是“空格”、“制表符（‘\t’）”、“换行符(‘\n’）”、“回车符（‘\r’）”
			//这里输入数据为空格分割数据
		    StringTokenizer itr = new StringTokenizer(value.toString());
		    while (itr.hasMoreTokens()) {
		        word.set(itr.nextToken());
		        context.write(word, one); //每个切分的单词计数为1
		    }
		}
	}

	/**
	 * reduce方法提供给reduce task进程来调用
	 *
	 * reduce task会将shuffle阶段分发过来的大量kv数据对进行聚合，聚合的机制是相同key的kv对聚合为一组
	 * 然后reduce task对每一组聚合kv调用一次我们自定义的reduce方法
	 * 比如：<hello,1><hello,1><hello,1><tom,1><tom,1><tom,1>
	 *  hello组会调用一次reduce方法进行处理，tom组也会调用一次reduce方法进行处理
	 *  调用时传递的参数：
	 *  		key：一组kv中的key
	 *  		values：一组kv中所有value的迭代器
	 */
	 public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		 private IntWritable result = new IntWritable();
		
		 public void reduce(Text key, Iterable<IntWritable> values,
                            Context context) throws IOException, InterruptedException {
		     int sum = 0;
			 //通过value这个迭代器，遍历这一组kv中所有的value，进行累加
		     for (IntWritable val : values) {
		         sum += val.get();
		     }
		     result.set(sum);
		     context.write(key, result);//输出这个单词的统计结果
		 }
	 }
	
	 public static void main(String[] args) throws Exception {
		if (args == null || args.length < 3) {
			args[0] = "wordcount";
			args[1] = "/input/word.txt";
			args[2] = "/output/wordcountpara1";
		}
	        Configuration conf = new Configuration();
	        Job job = Job.getInstance(conf, args[0]);
		    //重要：指定本job所在的jar包
	        job.setJarByClass(WordCount.class);

		 //设置wordCountJob所用的mapper逻辑类为哪个类
	        job.setMapperClass(TokenizerMapper.class);
	        job.setCombinerClass(IntSumReducer.class);
		 //设置wordCountJob所用的reducer逻辑类为哪个类
	        job.setReducerClass(IntSumReducer.class);

			//设置map阶段输出的kv数据类型(可以不设置)
		    job.setMapOutputKeyClass(Text.class);
		    job.setMapOutputValueClass(IntWritable.class);

			 //设置最终输出的kv数据类型
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);

	        job.setInputFormatClass(NLineInputFormat.class);
	        // 输入文件路径
	        FileInputFormat.addInputPath(job, new Path(args[1]));
	        // 输出文件路径
	        FileOutputFormat.setOutputPath(job, new Path(args[2]));
		 	//提交job给hadoop集群
	        System.exit(job.waitForCompletion(true) ? 0 : 1);
	    }
}
