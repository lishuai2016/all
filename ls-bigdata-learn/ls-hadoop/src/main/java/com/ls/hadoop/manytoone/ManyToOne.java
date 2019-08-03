package com.ls.hadoop.manytoone;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;

/**
 * Created by lishuai on 2018/2/4.
 *
 *
 * （1）需求

 要计算的目标文件中有大量的小文件，会造成分配任务和资源的开销比实际的计算开销还打，这就产生了效率损耗。

 需要先把一些小文件合并成一个大文件。
 */
public class ManyToOne {
    public static class ManyToOneMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

       private Text filenamekey;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            InputSplit split = context.getInputSplit();
            Path path = ((FileSplit)split).getPath();
            filenamekey = new Text(path.toString());
        }

        @Override
        protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
           context.write(filenamekey,value);
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "ManyToOne");
        job.setJarByClass(ManyToOne.class);
        job.setMapperClass(ManyToOneMapper.class);


        job.setInputFormatClass(MyInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/word.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/wordcount17"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
