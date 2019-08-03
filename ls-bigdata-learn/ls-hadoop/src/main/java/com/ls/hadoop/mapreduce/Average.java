package com.ls.hadoop.mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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
public class Average {

    public static class AvgMapper
            extends Mapper<LongWritable, Text, LongWritable, LongWritable> {

        public long sum = 0;
        public long count = 0;

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            sum += Long.parseLong(value.toString());
            count += 1;
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(sum), new LongWritable(count));
        }

    }

    public static class AvgCombiner extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {

        public long sum = 0;
        public long count = 0;

        public void reduce(LongWritable key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            sum += key.get();
            for (LongWritable v : values) {
                count += v.get();
            }
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(sum), new LongWritable(count));
        }

    }

    public static class AvgReducer extends Reducer<LongWritable, LongWritable, DoubleWritable, NullWritable> {

        public long sum = 0;
        public long count = 0;

        public void reduce(LongWritable key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            sum += key.get();
            for (LongWritable v : values) {
                count += v.get();
            }
        }


        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new DoubleWritable(new Double(sum)/count), NullWritable.get());
        }

    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();


        Job job = Job.getInstance(conf, "Avg");
        job.setJarByClass(Average.class);
        job.setMapperClass(AvgMapper.class);
        job.setCombinerClass(AvgCombiner.class);
        job.setReducerClass(AvgReducer.class);

        //注意这里:由于Mapper与Reducer的输出Key,Value类型不同,所以要单独为Mapper设置类型
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(LongWritable.class);


        job.setOutputKeyClass(DoubleWritable.class);
        job.setOutputValueClass(NullWritable.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/removedup.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/average"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


}
