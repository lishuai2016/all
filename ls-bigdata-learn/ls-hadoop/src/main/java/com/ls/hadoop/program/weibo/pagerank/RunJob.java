package com.ls.hadoop.program.weibo.pagerank;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class RunJob {
	
	public static enum Mycounter{
		my
	}

	public static void main(String[] args) {
		Configuration config =new Configuration();
		config.set("fs.defaultFS", "hdfs://node1:8020");
		config.set("yarn.resourcemanager.hostname", "node1");
		double d =0.001;
		int i=0;
		while(true){
			i++;
			try {
				config.setInt("runCount", i);
				FileSystem fs = FileSystem.get(config);
				Job job = Job.getInstance(config);
				job.setJarByClass(RunJob.class);
				job.setJobName("pr"+i);
				job.setMapperClass(PageRankMapper.class);
				job.setReducerClass(PageRankReducer.class);
				job.setMapOutputKeyClass(Text.class);
				job.setMapOutputValueClass(Text.class);
				job.setInputFormatClass(KeyValueTextInputFormat.class);
				Path inputPath =new Path("/usr/input/pagerank.txt");
				if(i>1){
					inputPath =new Path("/usr/output/pr"+(i-1));
				}
				FileInputFormat.addInputPath(job, inputPath);
				
				Path outpath =new Path("/usr/output/pr"+i);
				if(fs.exists(outpath)){
					fs.delete(outpath, true);
				}
				FileOutputFormat.setOutputPath(job, outpath);
				
				boolean f= job.waitForCompletion(true);
				if(f){
					System.out.println("success.");
					long sum= job.getCounters().findCounter(Mycounter.my).getValue();
					System.out.println(sum);
					double avgd= sum/4000.0;
					if(avgd<d){
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	static class PageRankMapper extends Mapper<Text, Text, Text, Text> {
		protected void map(Text key, Text value,
                           Context context)
				throws IOException, InterruptedException {
			int runCount= context.getConfiguration().getInt("runCount", 1);
			String page =key.toString();
			Node node =null;
			if(runCount==1){
				node =Node.fromMR("1.0"+"\t"+value.toString());
			}else{
				node =Node.fromMR(value.toString());
			}
			context.write(new Text(page), new Text(node.toString()));//A:1.0	B	D
			if(node.containsAdjacentNodes()){
				double outValue =node.getPageRank()/node.getAdjacentNodeNames().length;
				for (int i = 0; i < node.getAdjacentNodeNames().length; i++) {
					String outPage = node.getAdjacentNodeNames()[i];
					context.write(new Text(outPage), new Text(outValue+""));//B:0.5  D:0.5
				}
			}
		}
	}
	
	static class PageRankReducer extends Reducer<Text, Text, Text, Text> {
		protected void reduce(Text arg0, Iterable<Text> arg1,
                              Context arg2)
				throws IOException, InterruptedException {
			double sum =0.0;
			Node sourceNode =null;
			for(Text i:arg1){
				Node node =Node.fromMR(i.toString());
				if(node.containsAdjacentNodes()){
					sourceNode =node;
				}else{
					sum=sum+node.getPageRank();
				}
			}
			
			double newPR=(0.15/4)+(0.85*sum);
			System.out.println("*********** new pageRank value is "+newPR);
			
			//把新的pr值和计算之前的pr比较
			double d= newPR -sourceNode.getPageRank();
			
			int j=(int)( d*1000.0);
			j= Math.abs(j);
			System.out.println(j+"___________");
			arg2.getCounter(Mycounter.my).increment(j);;
			
			sourceNode.setPageRank(newPR);
			arg2.write(arg0, new Text(sourceNode.toString()));
		}
	}
}
