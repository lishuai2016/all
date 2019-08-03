package com.ls.hadoop.program.weibo.tf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class LastJob {

	public static void main(String[] args) {
		Configuration config =new Configuration();
//		config.set("fs.defaultFS", "hdfs://node1:8020");
//		config.set("yarn.resourcemanager.hostname", "node1");
		config.set("mapred.jar", "C:\\Users\\Administrator\\Desktop\\weibo3.jar");
		try {
			FileSystem fs = FileSystem.get(config);
//			JobConf job =new JobConf(config);
			Job job = Job.getInstance(config);
			job.setJarByClass(LastJob.class);
			job.setJobName("weibo3");
			
//			DistributedCache.addCacheFile(uri, conf);
			//2.5 因为数据量小，直接缓存到内存中
			//把微博总数加载到内存
			job.addCacheFile(new Path("/usr/output/weibo1/part-r-00003").toUri());
			//把df加载到内存
			job.addCacheFile(new Path("/usr/output/weibo2/part-r-00000").toUri());
			
			
			
			
			
			//设置map任务的输出key类型、value类型
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
//			job.setMapperClass();
			job.setMapperClass(LastMapper.class);
			job.setReducerClass(LastReduce.class);
			
			//mr运行时的输入数据从hdfs的哪个目录中获取
			FileInputFormat.addInputPath(job, new Path("/usr/output/weibo1"));
			Path outpath =new Path("/usr/output/weibo3");
			if(fs.exists(outpath)){
				fs.delete(outpath, true);
			}
			FileOutputFormat.setOutputPath(job,outpath );
			
			boolean f= job.waitForCompletion(true);
			if(f){
				System.out.println("执行job成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
