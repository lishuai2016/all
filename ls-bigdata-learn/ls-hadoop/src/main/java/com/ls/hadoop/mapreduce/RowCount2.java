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
public class RowCount2 {


    public static class RowCount2Mapper
            extends Mapper<LongWritable, Text, LongWritable, NullWritable> {

        public long count = 0;

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            count += 1;
            //context.write(new LongWritable(count), NullWritable.get());
        }

        /**
         * 注意: 这里context.write(xxx)只能写在cleanup方法中, 该方法在Mapper和Reducer接口中都有,
         * 在map方法及reduce方法执行完后,会触发cleanup方法.
         * 把context.write(xxx)写在map和reduce方法中试试看,结果会出现多行记录,而不是预期的仅1个数字.
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(count), NullWritable.get());
        }

    }

    public static class RowCount2Reducer extends Reducer<LongWritable, NullWritable, LongWritable, NullWritable> {

        public long count = 0;

        public void reduce(LongWritable key, Iterable<NullWritable> values, Context context)
                throws IOException, InterruptedException {
            count += key.get();
            //context.write(new LongWritable(count), NullWritable.get());
        }


        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(count), NullWritable.get());
        }

    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "RowCount2");
        job.setJarByClass(RowCount2.class);
        job.setMapperClass(RowCount2Mapper.class);
        job.setCombinerClass(RowCount2Reducer.class);
        job.setReducerClass(RowCount2Reducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(NullWritable.class);
        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/removedup.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/rowcount22"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
