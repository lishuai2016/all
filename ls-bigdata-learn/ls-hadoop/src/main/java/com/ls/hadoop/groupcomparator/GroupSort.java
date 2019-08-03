package com.ls.hadoop.groupcomparator;



import com.ls.hadoop.bean.OrderBean;
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
 *
 * 题目：计算出每组订单中金额最大的记录
 *
 *
 *
 * Created by lishuai on 2018/2/4.
 *1、根据订单号的hashcode分区，可以保证订单号相同的在同一个分区，以便reduce中接收到同一个订单的全部记录。
 *同分区的数据是序的，这就用到了bean中的比较方法，可以让订单号相同的记录按照金额从大到小排序。
 *
 * 2、因为map的结果数据中key是bean，不是普通数据类型，所以需要使用自定义的比较器来分组，就使用bean中的订单号来比较。

 例如读取到分区1的数据：
 <{ Order_0000001   222.8 }, null>,
 <{ Order_0000001   25.8 }, null>,
 <{ Order_0000003   222.8 }, null>

 进行比较，前两条数据的订单号相同，放入一组，默认是以第一条记录的key作为这组记录的key。

 分组后的形式如下：
 <{ Order_0000001 222.8 }, [null, null]>,
 <{ Order_0000003 222.8 }, [null]>

 在reduce方法中收到的每组记录的key就是我们最终想要的结果，所以直接输出到文件就可以了。
 */
public class GroupSort {
    static class GroupSortMapper extends Mapper<LongWritable ,Text, OrderBean,NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            context.write(new OrderBean(new Text(fields[0]),new DoubleWritable(Double.parseDouble(fields[1]))), NullWritable.get());
        }
    }

    static class GroupSortReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "GroupSort");
        job.setJarByClass(GroupSort.class);

        job.setMapperClass(GroupSortMapper.class);
        job.setReducerClass(GroupSortReducer.class);

        //自定义分组比较器
        job.setGroupingComparatorClass(MyGroupingComparator.class);

        //加入自定义分区
        job.setPartitionerClass(ItemIdPartitioner.class);
        job.setNumReduceTasks(2);//设置reduce任务数目



        //输出
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/input/groupsort.txt"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/groupsort1"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
