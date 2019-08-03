package com.ls.hadoop.mr2;


import com.ls.hadoop.bean.FlowBean;
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

/**
 * 添加自定义分区统计流量
 * Created by lishuai on 2018/2/4.
 */
public class FlowCount2 {
    static class FlowCountMapper extends Mapper<LongWritable ,Text, Text,FlowBean> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable ,Text , Text,FlowBean>.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            String phone = fields[0];
            long upflow = Long.parseLong(fields[1]);
            long downflow = Long.parseLong(fields[2]);
            context.write(new Text(phone),new FlowBean(upflow,downflow));
        }
    }

    static class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {

        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context context) throws IOException, InterruptedException {
            long sum_upflow = 0;
            long sum_downflow = 0;
            for (FlowBean f : values) {
                sum_upflow += f.getUpFlow();
                sum_downflow += f.getDownFlow();
            }
            context.write(key,new FlowBean(sum_upflow,sum_downflow));
        }
    }
    public static void main(String[] args) throws Exception {


        Configuration conf = new Configuration();


        Job job = Job.getInstance(conf, "FlowCount");
        job.setJarByClass(FlowCount2.class);

        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        //加入自定义分区
        job.setPartitionerClass(ProvincePartitioner.class);
        job.setNumReduceTasks(5);//设置reduce任务数目

        //注意这里:由于Mapper与Reducer的输出Key,Value类型不同,所以要单独为Mapper设置类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);


        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/flowcount.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/flowcount4"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
