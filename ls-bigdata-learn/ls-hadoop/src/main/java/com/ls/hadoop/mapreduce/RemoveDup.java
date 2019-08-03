package com.ls.hadoop.mapreduce;

/**
 * Created by lishuai on 2018/2/3.
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 去重处理(Distinct)
 * <p>
 * 类似于db中的select distinct(x) from table
 * 2
 * 8
 * 8
 * 3
 * 2
 * 3
 * 5
 * 3
 * 0
 * 2
 * 7
 * <p>
 * 变为
 * 0
 * 2
 * 3
 * 5
 * 7
 * 8
 */


public class RemoveDup {

    public static class RemoveDupMapper
            extends Mapper<Object, Text, Text, NullWritable> {

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            context.write(value, NullWritable.get());
            //System.out.println("map: key=" + key + ",value=" + value);
        }

    }

    public static class RemoveDupReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
        public void reduce(Text key, Iterable<NullWritable> values, Context context)
                throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
            //System.out.println("reduce: key=" + key);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
//        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length < 2) {
//            System.err.println("Usage: RemoveDup <in> [<in>...] <out>");
//            System.exit(2);
//        }
//
//        //删除输出目录(可选,省得多次运行时,总是报OUTPUT目录已存在)
//        HDFSUtil.deleteFile(conf, otherArgs[otherArgs.length - 1]);

        Job job = Job.getInstance(conf, "RemoveDup");
        job.setJarByClass(RemoveDup.class);
        job.setMapperClass(RemoveDupMapper.class);
        job.setCombinerClass(RemoveDupReducer.class);
        job.setReducerClass(RemoveDupReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, args[0]);
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
//        for (int i = 0; i < otherArgs.length - 1; ++i) {
//            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
//        }
//        FileOutputFormat.setOutputPath(job,
//                new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


}
