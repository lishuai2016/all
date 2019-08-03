package com.ls.hadoop.eachothercommonfriends;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by lishuai on 2018/2/5.
 */
public class StepSecond {


    static class StepSecondMapper extends Mapper<LongWritable,Text,Text,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] arr = line.split("\t");
            String user = arr[0];
            String[] friends = arr[1].split(",");

            Arrays.sort(friends);

            for (int i=0;i < friends.length - 1;i++) {
                for (int j = i+1;j < friends.length;j++) {
                    context.write(new Text(friends[i] + "-" + friends[j]),new Text(user));
                }
            }
        }
    }

    static class StepSecondReducer extends Reducer<Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer buf = new StringBuffer();
            for (Text user : values) {
                buf.append(user).append(" ");
            }
            context.write(key,new Text(buf.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "StepSecond");
        job.setJarByClass(StepSecond.class);

        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setMapperClass(StepSecondMapper.class);
        job.setReducerClass(StepSecondReducer.class);




        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/friends/part-r-00000"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/friends21"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);


    }
}
