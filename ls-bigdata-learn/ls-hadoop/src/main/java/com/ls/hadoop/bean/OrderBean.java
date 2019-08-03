package com.ls.hadoop.bean;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by lishuai on 2018/2/4.
 */
public class OrderBean implements WritableComparable<OrderBean> {

    public OrderBean() {

    }

    public OrderBean(Text itemid, DoubleWritable amount) {
        this.itemid = itemid;
        this.amount = amount;
    }
    private Text itemid;

    private DoubleWritable amount;


    public DoubleWritable getAmount() {
        return amount;
    }

    public void setAmount(DoubleWritable amount) {
        this.amount = amount;
    }

    public Text getItemid() {
        return itemid;
    }

    public void setItemid(Text itemid) {
        this.itemid = itemid;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "itemid=" + itemid +
                ", amount=" + amount +
                '}';
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(itemid.toString());
        dataOutput.writeDouble(amount.get());

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        itemid = new Text(dataInput.readUTF());
        amount = new DoubleWritable(dataInput.readDouble());
    }

    public int compareTo(OrderBean o) {
        int ret = this.itemid.compareTo(o.itemid);
        if (ret == 0) {
            ret = -this.amount.compareTo(o.amount);
        }
        return ret;
    }
}
