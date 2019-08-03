package com.ls.hadoop.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * 按照词频做倒排
 *
 *
 I,the	4
 be,we	3
 are,do,will	2
 But,Of,They,all,am,bad,behavior,best,but,dog,dreams,eternal,fever,flame,guard,have,in,it,not,of,say,to,watcher,way,what,your	1
 */
public class WordCount2 {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
//简单每一行按照分隔符分割为多个单词，然后统计
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        //定义treeMap来保持统计结果,由于treeMap是按key升序排列的,这里要人为指定Comparator以实现倒排
        //这里先使用统计数为key，被统计的单词为value
        private TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                return y.compareTo(x);
            }
        });

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //reduce后的结果放入treeMap,而不是向context中记入结果
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            if (treeMap.containsKey(sum)){  //具有相同单词数的单词之间用逗号分隔
                String value = treeMap.get(sum) + "," + key.toString();
                treeMap.put(sum,value);
            } else {
                treeMap.put(sum, key.toString());
            }
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            //将treeMap中的结果,按value-key顺序写入contex中
            for (Integer key : treeMap.keySet()) {
                context.write(new Text(treeMap.get(key)), new IntWritable(key));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 3) {
            args[0] = "wordcount2";
            args[1] = "/input/wordcount2.txt";
            args[2] = "/output/wordcount2";
        }
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, args[0]);
        job.setJarByClass(WordCount2.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 输入文件路径
        FileInputFormat.addInputPath(job, new Path(args[1]));
        // 输出文件路径
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


}