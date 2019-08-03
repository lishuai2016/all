package com.ls.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * Created by lishuai on 2018/2/3.
 */
public class Max {

    public static class MaxMapper
            extends Mapper<LongWritable, Text, LongWritable, NullWritable> {

        public long max = Long.MIN_VALUE;

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            max = Math.max(Long.parseLong(value.toString()), max);
        }

        protected void cleanup(Mapper.Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(max), NullWritable.get());
        }

    }

    public static class MaxReducer extends Reducer<LongWritable, NullWritable, LongWritable, NullWritable> {

        public long max = Long.MIN_VALUE;

        public void reduce(LongWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {

            max = Math.max(max, key.get());

        }


        protected void cleanup(Reducer.Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(max), NullWritable.get());
        }

    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Max");
        job.setJarByClass(Max.class);
        job.setMapperClass(MaxMapper.class);
        job.setCombinerClass(MaxReducer.class);
        job.setReducerClass(MaxReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(NullWritable.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/removedup.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/max"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


}
