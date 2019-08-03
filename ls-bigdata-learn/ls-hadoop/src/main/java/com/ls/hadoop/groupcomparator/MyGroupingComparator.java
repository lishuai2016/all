package com.ls.hadoop.groupcomparator;


import com.ls.hadoop.bean.OrderBean;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 在reduce端,分组的时候使用
 * Created by lishuai on 2018/2/4.
 */
public class MyGroupingComparator extends WritableComparator {

    public MyGroupingComparator() {
        super(OrderBean.class,true);
    }


    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean o1 = (OrderBean)a;
        OrderBean o2 = (OrderBean)b;
        return o1.getItemid().compareTo(o2.getItemid());
    }
}
