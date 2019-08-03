package com.ls.hadoop.ik;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;

/**
 * 通过IK分词器进行中文词频统计
 * 我 文档1中，出现20次
 * 祖国 文档2中，出现10次
 */
public class WcAndIk {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: wordcount <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = new Job(conf, "word count");
        job.setJarByClass(WcAndIk.class);


        //修改地方1：将TokenizerMapper修改成你自己的Mapper类
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(MyCombiner.class);

        //修改地方2：将IntSumReducer修改成你自己的IntSumReducer类
        job.setReducerClass(IntSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[(otherArgs.length - 1)]));

        System.exit((job.waitForCompletion(true)) ? 0 : 1);
    }

    public static class IntSumReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //---->  村    1.log:4
            //---->  村    2.log:7
            //---->  村  {1.log:4,2.log7}


            StringBuffer stringBuffer = new StringBuffer();
            for (Text text:values){
                stringBuffer.append("\t");
                stringBuffer.append("-");
                stringBuffer.append(new String(text.getBytes()));
            }
            context.write(key, new Text(stringBuffer.toString()));
        }
    }

    public static class MyCombiner extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (Text value : values) {
                sum += Integer.parseInt(new String(value.toString()));
            }
            //村：1.log    4
            //---->  村    1.log:4

            // 2、切割Key
            String[] arrStr = key.toString().split(":");
            //3、拼接
            String outKey = arrStr[0];
            String outValue = arrStr[1] + ":" + sum;
            context.write(new Text(outKey), new Text(outValue));
        }
    }

    public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            //1、通过InputFormat的读取block，逐行获取数据
            //2、获取文件名称
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            String fileName = fileSplit.getPath().getName();
            //3、进行分词
            byte[] bytes = value.getBytes();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Reader reader = new InputStreamReader(inputStream);
            IKSegmenter ikSegmenter = new IKSegmenter(reader, true);
            Lexeme t;
            while ((t = ikSegmenter.next()) != null) {
                context.write(new Text(t.getLexemeText() + ":" + fileName), new Text("1"));
            }
            //word:documentName count
            //村：1.log    1
            //村：1.log    1
            //村：1.log    1
            //村：1.log    1
        }
    }


}
