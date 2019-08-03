package com.ls.hadoop.groupcomparator;


import com.ls.hadoop.bean.OrderBean;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by lishuai on 2018/2/4.
 */
public class ItemIdPartitioner extends Partitioner<OrderBean, NullWritable> {

    @Override
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int i) {
        return (orderBean.getItemid().hashCode() & Integer.MAX_VALUE) % i;
    }
}
