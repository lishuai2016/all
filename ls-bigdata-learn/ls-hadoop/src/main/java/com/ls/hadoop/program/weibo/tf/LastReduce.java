package com.ls.hadoop.program.weibo.tf;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class LastReduce extends Reducer<Text, Text, Text, Text> {
	
	protected void reduce(Text key, Iterable<Text> arg1,
                          Context context)
			throws IOException, InterruptedException {
		
		StringBuffer sb =new StringBuffer();
		
		for( Text i :arg1 ){
			sb.append(i.toString()+"\t");
		}
		
		context.write(key, new Text(sb.toString()));
	}

}
