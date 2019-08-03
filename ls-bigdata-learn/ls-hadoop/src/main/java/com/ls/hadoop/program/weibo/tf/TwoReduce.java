package com.ls.hadoop.program.weibo.tf;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TwoReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	protected void reduce(Text key, Iterable<IntWritable> arg1,
                          Context context)
			throws IOException, InterruptedException {
		
		int sum =0;
		for( IntWritable i :arg1 ){
			sum= sum+i.get();
		}
		
		context.write(key, new IntWritable(sum));
	}

}
