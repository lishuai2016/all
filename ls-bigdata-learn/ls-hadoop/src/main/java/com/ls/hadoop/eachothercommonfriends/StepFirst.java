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

/**
 * Created by lishuai on 2018/2/5.
 *
 *
 *
 * 之前的示例中都是一个MapReduce计算出来的，这里我们使用2个MapReduce来实现。

 1）第1个MapReduce

 map

 找出每个用户都是谁的好友，例如：
 读一行A:B,C,D,F,E,O（A的好友有这些，反过来拆开，这些人中的每一个都是A的好友）
 输出<B,A> <C,A> <D,A> <F,A> <E,A> <O,A>
 再读一行B:A,C,E,K
 输出<A,B> <C,B> <E,B> <K,B>
 ……

 reduce

 key相同的会分到一组，例如：
 <C,A><C,B><C,E><C,F><C,G>......
 Key:C
 value: [ A, B, E, F, G ]

 意义是：C是这些用户的好友。

 遍历value就可以得到：
 A B 有共同好友C
 A E 有共同好友C
 ...
 B E有共同好友 C
 B F有共同好友 C

 输出：
 <A-B,C>
 <A-E,C>
 <A-F,C>
 <A-G,C>
 <B-E,C>
 <B-F,C>
 .....

 2）第2个MapReduce

 对上一步的输出结果进行计算。

 map

 读出上一步的结果数据，组织成key value直接输出

 例如：
 读入一行<A-B,C>
 直接输出<A-B,C>

 reduce

 读入数据，key相同的在一组
 <A-B,C><A-B,F><A-B,G>......

 输出：
 A-B C,F,G,.....

 这样就得出了两个用户间的共同好友列表
 */
public class StepFirst {

    static class FirstMapper extends Mapper<LongWritable,Text,Text,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] arr = line.split(":");
            String user = arr[0];
            String friends = arr[1];
            for (String friend:friends.split(",")) {
                context.write(new Text(friend),new Text(user));
            }
        }
    }

    static class FirstReducer extends Reducer<Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer buf = new StringBuffer();
            for (Text user : values) {
                buf.append(user).append(",");
            }
            context.write(new Text(key),new Text(buf.toString()));//用/t分割
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "StepFirst");
        job.setJarByClass(StepFirst.class);

        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setMapperClass(FirstMapper.class);
        job.setReducerClass(FirstReducer.class);




        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/friends.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/friends"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);


    }
}
