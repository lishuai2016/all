package com.ls.hadoop;

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
public class Sum {

    public static class SumMapper
            extends Mapper<LongWritable, Text, LongWritable, NullWritable> {

        public long sum = 0;

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            sum += Long.parseLong(value.toString());
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(sum), NullWritable.get());
        }

    }

    public static class SumReducer extends Reducer<LongWritable, NullWritable, LongWritable, NullWritable> {

        public long sum = 0;

        public void reduce(LongWritable key, Iterable<NullWritable> values, Context context)
                throws IOException, InterruptedException {
            sum += key.get();
        }


        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(sum), NullWritable.get());
        }

    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();


        Job job = Job.getInstance(conf, "Sum");
        job.setJarByClass(Sum.class);
        job.setMapperClass(SumMapper.class);
        job.setCombinerClass(SumReducer.class);
        job.setReducerClass(SumReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(NullWritable.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/removedup.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/sum"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


}