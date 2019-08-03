package com.ls.hadoop.program.weibo.weather;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RunJob {

	public static void main(String[] args) {
		Configuration config =new Configuration();
		config.set("fs.defaultFS", "hdfs://node1:8020");
		config.set("yarn.resourcemanager.hostname", "node1");
//		config.set("mapred.jar", "C:\\Users\\Administrator\\Desktop\\wc.jar");
//		config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
		try {
			FileSystem fs = FileSystem.get(config);
			
			Job job = Job.getInstance(config);
			job.setJarByClass(RunJob.class);
			
			job.setJobName("weather");
			
			job.setMapperClass(WeatherMapper.class);
			job.setReducerClass(WeatherReducer.class);
			job.setMapOutputKeyClass(MyKey.class);
			job.setMapOutputValueClass(DoubleWritable.class);
			
			job.setPartitionerClass(MyPartitioner.class);
			job.setSortComparatorClass(MySort.class);
			job.setGroupingComparatorClass(MyGroup.class);
			
			job.setNumReduceTasks(3);
			
			job.setInputFormatClass(KeyValueTextInputFormat.class);
			
			FileInputFormat.addInputPath(job, new Path("/usr/input/weather"));
			
			Path outpath =new Path("/usr/output/weather");
			if(fs.exists(outpath)){
				fs.delete(outpath, true);
			}
			FileOutputFormat.setOutputPath(job, outpath);
			
			boolean f= job.waitForCompletion(true);
			if(f){
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//key：每行第一个隔开符左边为key，右边为value
	static class WeatherMapper extends Mapper<Text, Text, MyKey, DoubleWritable> {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		NullWritable v = NullWritable.get();
		protected void map(Text key, Text value,
                           Context context)
				throws IOException, InterruptedException {
			try {
				Date date =sdf.parse(key.toString());
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				int year =c.get(Calendar.YEAR);
				int month =c.get(Calendar.MONTH);
				
				double hot = Double.parseDouble(value.toString().substring(0, value.toString().lastIndexOf("c")));
				MyKey k =new MyKey();
				k.setYear(year);
				k.setMonth(month);
				k.setHot(hot);
				context.write(k, new DoubleWritable(hot));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static class WeatherReducer extends Reducer<MyKey, DoubleWritable, Text, NullWritable> {
		protected void reduce(MyKey arg0, Iterable<DoubleWritable> arg1,
				Context arg2)
				throws IOException, InterruptedException {
			int i=0;
			for(DoubleWritable v :arg1){
				i++;
				String msg =arg0.getYear()+"\t"+arg0.getMonth()+"\t"+v.get();
				arg2.write(new Text(msg), NullWritable.get());
				if(i==3){
					break;
				}
			}
		}
	}
}
