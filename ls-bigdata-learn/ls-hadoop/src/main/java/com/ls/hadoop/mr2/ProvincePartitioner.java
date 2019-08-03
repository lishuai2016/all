package com.ls.hadoop.mr2;


import com.ls.hadoop.bean.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

/**
 * 自定义分区器  在map端完成
 * Created by lishuai on 2018/2/4.
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean> {


    public static HashMap<String, Integer> map = new HashMap<String, Integer>();
    static {
        map.put("131",1);
        map.put("132",2);
        map.put("133",3);
        map.put("134",4);
    }

    /**
     * 这样使得最后的输出为5个文件，其中分区的编号从0开始
     * @param text
     * @param flowBean
     * @param i
     * @return
     */
    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {
        String prefix = text.toString().substring(0,3);
        Integer id = map.get(prefix);
        return id == null ? 0 : id;
    }
}
