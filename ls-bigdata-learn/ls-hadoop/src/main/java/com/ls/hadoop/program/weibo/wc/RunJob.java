package com.ls.hadoop.program.weibo.wc;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RunJob {

	public static void main(String[] args) {
		Configuration config =new Configuration();
//		config.set("fs.defaultFS", "hdfs://node1:8020");
//		config.set("yarn.resourcemanager.hostname", "node1");
		config.set("mapred.jar", "C:\\Users\\Administrator\\Desktop\\wc.jar");
		try {
			FileSystem fs = FileSystem.get(config);
			
			Job job = Job.getInstance(config);
			job.setJarByClass(RunJob.class);
			
			job.setJobName("wc");
			
			job.setMapperClass(WordCountMapper.class);
			job.setReducerClass(WordCountReducer.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			
			FileInputFormat.addInputPath(job, new Path("/usr/input/"));
			
			Path outpath =new Path("/usr/output/wc");
			if(fs.exists(outpath)){
				fs.delete(outpath, true);
			}
			FileOutputFormat.setOutputPath(job, outpath);
			
			boolean f= job.waitForCompletion(true);
			if(f){
				System.out.println("job����ִ�гɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
