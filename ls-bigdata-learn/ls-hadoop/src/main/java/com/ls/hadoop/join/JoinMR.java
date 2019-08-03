package com.ls.hadoop.join;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


/**
 * Created by lishuai on 2018/2/4.
 * 题目：
 * 需要用MapReduce程序来实现下面这个SQL查询运算：
 select o.id order_id, o.date, o.amount, p.id p_id, p.pname, p.c
 ategory_id, p.price
 from t_order o join t_product p on o.pid = p.id

 实现思路：

 1）定义bean

 把SQL执行结果中的各列封装成一个bean对象，实现序列化。

 bean中还要有一个另外的属性flag，用来标识此对象的数据是订单还是商品。

 2）map处理

 map会处理两个文件中的数据，根据文件名可以知道当前这条数据是订单还是商品。

 对每条数据创建一个bean对象，设置对应的属性，并标识flag（0代表order，1代表product）

 以join的关联项“productid”为key，bean为value进行输出。

 3）reduce处理

 reduce方法接收到pid相同的一组bean对象。

 遍历bean对象集合，如果bean是订单数据，就放入一个新的订单集合中，
 如果是商品数据，就保存到一个商品bean中。然后遍历那个新的订单集合，
 使用商品bean的数据对每个订单bean进行信息补全。

 这样就得到了完整的订单及其商品信息。
 */
public class JoinMR {



    static class myMapper extends Mapper<LongWritable,Text,Text,InfoBean> {
        InfoBean bean = new InfoBean();
        Text k = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");
            FileSplit inputsplit = (FileSplit)context.getInputSplit();
            String filename = inputsplit.getPath().getName();
            String pid = "";
            if (filename.startsWith("order")) {
                pid = fields[2];
                bean.set(Integer.parseInt(fields[0]),fields[1],pid, Integer.parseInt(fields[3]),"",0,0,"0");
            } else {
                pid = fields[0];
                bean.set(0,"",pid,0,fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),"1");
            }
        }
    }

    static class MyReducer extends Reducer<Text,InfoBean,InfoBean,NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<InfoBean> values, Context context) throws IOException, InterruptedException {
            InfoBean pdbean = new InfoBean();
            ArrayList<InfoBean> orderbeans = new ArrayList<InfoBean>();
            try {
                for (InfoBean b:values) {
                    if ("1".equals(b.getFlag())) {
                        BeanUtils.copyProperties(pdbean,b);
                    } else {
                        InfoBean odbean = new InfoBean();
                        BeanUtils.copyProperties(odbean,b);
                        orderbeans.add(odbean);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            for (InfoBean bean : orderbeans) {
                bean.setPname(pdbean.getPname());
                bean.setCategory_id(pdbean.getCategory_id());
                bean.setPrice(pdbean.getPrice());

                context.write(bean, NullWritable.get());
            }


        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(JoinMR.class);
        job.setMapperClass(myMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(InfoBean.class);

        job.setOutputKeyClass(InfoBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(
                "hdfs://192.168.137.127:9000/jointest/"));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(
                "hdfs://192.168.137.127:9000/output/jointest2"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
