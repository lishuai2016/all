package com.ls.hadoop.program.weibo.tf;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * c1_001,2
 * c2_001,1
 * count,10000
 * @author root
 *
 */
public class FirstReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	protected void reduce(Text arg0, Iterable<IntWritable> arg1,
                          Context arg2)
			throws IOException, InterruptedException {
		
		int sum =0;
		for( IntWritable i :arg1 ){
			sum= sum+i.get();
		}
		if(arg0.equals(new Text("count"))){
			System.out.println(arg0.toString() +"___________"+sum);
		}
		arg2.write(arg0, new IntWritable(sum));
	}

}
