package com.ls.hadoop.manytoone;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Created by lishuai on 2018/2/4.
 *
 * Inputformat使用我们自己的RecordReader，RecordReader负责实现一次读取一个完整文件封装为key value。

 map接收到文件内容，然后以文件名为key，以文件内容为value，向外输出的格式要注意，要使用SequenceFileOutPutFormat（用来输出对象）。

 因为reduce收到的key value都是对象，不是普通的文本，reduce默认的输出格式是TextOutputFormat，使用它的话，最终输出的内容就是对象ID，
 所以要使用SequenceFileOutPutFormat进行输出。
 */
public class MyInputFormat extends FileInputFormat<NullWritable, BytesWritable> {

    //保证每个小文件不可分
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        MyRecordReader recordReader = new MyRecordReader();
        recordReader.initialize(inputSplit,taskAttemptContext);
        return recordReader;
    }

}
