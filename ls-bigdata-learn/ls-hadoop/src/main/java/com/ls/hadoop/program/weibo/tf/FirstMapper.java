package com.ls.hadoop.program.weibo.tf;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * 第一个MR，计算TF和计算N(微博总数)
 * @author root
 *
 */
public class FirstMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	protected void map(LongWritable key, Text value,
                       Context context)
			throws IOException, InterruptedException {
		String[]  v =value.toString().trim().split("\t");
		if(v.length>=2){
			String id=v[0].trim();
			String content =v[1].trim();

			StringReader sr =new StringReader(content);
			IKSegmenter ikSegmenter =new IKSegmenter(sr, true);
			Lexeme word = null;
			while( (word = ikSegmenter.next()) !=null ){
				String w= word.getLexemeText();
				context.write(new Text(w+"_"+id), new IntWritable(1));
			}
			context.write(new Text("count"), new IntWritable(1));
		} else {
			System.out.println(value.toString()+"-------------");
		}
	}
	
	
	
}
