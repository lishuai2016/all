package com.ls.hadoop.program.shortestpath;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public final class Main {


  public static final String TARGET_NODE = "shortestpath.targetnode";

  public static void main(String... args) throws Exception {

    String startNode = "dee";
    String targetNode = "joe";
    String inputFile = "/usr/input/shortestpath";
    String outputDir = "/usr/output/";

    iterate(startNode, targetNode, inputFile, outputDir);
  }
 public static Configuration conf = new Configuration();
  static{
	  
	  conf.set("fs.defaultFS", "hdfs://node15:9000");
	  conf.set("yarn.resourcemanager.hostname", "node15");
  }

  public static void iterate(String startNode, String targetNode,
                             String input, String output)
      throws Exception {

    Path outputPath = new Path(output);
    outputPath.getFileSystem(conf).delete(outputPath, true);
    outputPath.getFileSystem(conf).mkdirs(outputPath);

    Path inputPath = new Path(outputPath, "input.txt");

    createInputFile(new Path(input), inputPath, startNode);

    int iter = 1;

    while (true) {

      Path jobOutputPath =
          new Path(outputPath, String.valueOf(iter));

      System.out.println("======================================");
      System.out.println("=  Iteration:    " + iter);
      System.out.println("=  Input path:   " + inputPath);
      System.out.println("=  Output path:  " + jobOutputPath);
      System.out.println("======================================");

      if(findShortestPath(inputPath, jobOutputPath, startNode, targetNode)) {
        break;
      }
      inputPath = jobOutputPath;
      iter++;
    }
  }

  public static void createInputFile(Path file, Path targetFile,
                                     String startNode)
      throws IOException {
    FileSystem fs = file.getFileSystem(conf);

    OutputStream os = fs.create(targetFile);
    LineIterator iter = IOUtils
        .lineIterator(fs.open(file), "UTF8");
    while (iter.hasNext()) {
      String line = iter.nextLine();

      String[] parts = StringUtils.split(line);
      int distance = Node.INFINITE;
      if (startNode.equals(parts[0])) {
        distance = 0;
      }
      IOUtils.write(parts[0] + '\t' + String.valueOf(distance) + "\t\t",
          os);
      IOUtils.write(StringUtils.join(parts, '\t', 1, parts.length), os);
      IOUtils.write("\n", os);
    }

    os.close();
  }

  public static boolean findShortestPath(Path inputPath,
                                         Path outputPath, String startNode,
                                         String targetNode)
      throws Exception {
    conf.set(TARGET_NODE, targetNode);

    Job job = new Job(conf);
    job.setJarByClass(Main.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setInputFormatClass(KeyValueTextInputFormat.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(job, inputPath);
    FileOutputFormat.setOutputPath(job, outputPath);

    if (!job.waitForCompletion(true)) {
      throw new Exception("Job failed");
    }

    Counter counter = job.getCounters()
        .findCounter(Reduce.PathCounter.TARGET_NODE_DISTANCE_COMPUTED);

    if(counter != null && counter.getValue() > 0) {
      CounterGroup group = job.getCounters().getGroup(Reduce.PathCounter.PATH.toString());
      Iterator<Counter> iter = group.iterator();
      iter.hasNext();
      String path = iter.next().getName();
      System.out.println("==========================================");
      System.out.println("= Shortest path found, details as follows.");
      System.out.println("= ");
      System.out.println("= Start node:  " + startNode);
      System.out.println("= End node:    " + targetNode);
      System.out.println("= Hops:        "  + counter.getValue());
      System.out.println("= Path:        "  + path);
      System.out.println("==========================================");
      return true;
    }
    return false;
  }


//  public static String getNeighbor(String str){
//	  return str.split(",")[0];
//  }
//  public static int getNeighborDis(String str){
//	  return Integer.parseInt(str.split(",")[1]);
//  }

}