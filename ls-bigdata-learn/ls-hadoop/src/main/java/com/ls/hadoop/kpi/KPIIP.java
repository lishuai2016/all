package com.ls.hadoop.kpi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lishuai on 2018/2/5.
 * 统计访问的ip访问次数
 */
public class KPIIP {


    static class KPIIPMapper extends Mapper<Object, Text, Text, Text> {
        private Text word = new Text();
        private Text ips = new Text();
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            KPI kpi = KPI.filterIPs(value.toString());
            if (kpi.isValid()) {
                //word.set(kpi.getRequest());
                word.set(kpi.getRemote_addr());
                ips.set(kpi.getRemote_addr());
                context.write(word,ips);
            }
        }
    }
    static class KPIIPReducer extends Reducer<Text, Text, Text, Text> {
        private Text result = new Text();
        private Set<String> count = new HashSet<String>();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value:values) {
                count.add(value.toString());
            }
            result.set(String.valueOf(count.size()));
            context.write(key,result);
        }
    }
    public static void main(String[] args) throws Exception {



        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "KPIIP");
        job.setJarByClass(KPIIP.class);
        job.setMapperClass(KPIIPMapper.class);
        job.setCombinerClass(KPIIPReducer.class);
        job.setReducerClass(KPIIPReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/log_kpi/access.log.10"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/kpiip2"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);


    }
}
