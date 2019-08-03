<!-- TOC -->

- [第220讲-Spark Streaming实战开发进阶-flume安装](#第220讲-spark-streaming实战开发进阶-flume安装)
    - [1、安装flume](#1安装flume)
    - [2、修改flume配置文件](#2修改flume配置文件)
    - [3、创建需要的文件夹](#3创建需要的文件夹)
    - [4、启动flume-agent](#4启动flume-agent)
    - [5、测试flume](#5测试flume)
- [第221讲-Spark Streaming实战开发进阶-接收flume实时数据流-flume风格的基于push的方式](#第221讲-spark-streaming实战开发进阶-接收flume实时数据流-flume风格的基于push的方式)
- [第222讲-Spark Streaming实战开发进阶-接收flume实时数据流-自定义sink的基于poll的方式](#第222讲-spark-streaming实战开发进阶-接收flume实时数据流-自定义sink的基于poll的方式)
- [第223讲-Spark Streaming实战开发进阶-高阶技术之自定义Receiver](#第223讲-spark-streaming实战开发进阶-高阶技术之自定义receiver)
- [第224讲-Spark Streaming实战开发进阶-kafka安装](#第224讲-spark-streaming实战开发进阶-kafka安装)
    - [1、安装ZooKeeper包](#1安装zookeeper包)
    - [2、配置zoo.cfg](#2配置zoocfg)
    - [3、设置zk节点标识](#3设置zk节点标识)
    - [4、启动ZooKeeper集群](#4启动zookeeper集群)
    - [5、安装scala 2.11.4](#5安装scala-2114)
    - [6、安装Kafka包](#6安装kafka包)
    - [7、搭建kafka集群](#7搭建kafka集群)
    - [8、启动kafka集群](#8启动kafka集群)
    - [9、测试kafka集群](#9测试kafka集群)
- [第225讲-Spark Streaming实战开发进阶-综合案例3：新闻网站关键指标实时统计](#第225讲-spark-streaming实战开发进阶-综合案例3新闻网站关键指标实时统计)
- [第226讲-Spark Streaming实战开发进阶-综合案例3：页面pv实时统计](#第226讲-spark-streaming实战开发进阶-综合案例3页面pv实时统计)
- [第227讲-Spark Streaming实战开发进阶-综合案例3：页面uv实时统计](#第227讲-spark-streaming实战开发进阶-综合案例3页面uv实时统计)
- [第228讲-Spark Streaming实战开发进阶-综合案例3：注册用户数实时统计](#第228讲-spark-streaming实战开发进阶-综合案例3注册用户数实时统计)
- [第229讲-Spark Streaming实战开发进阶-综合案例3：用户跳出量实时统计](#第229讲-spark-streaming实战开发进阶-综合案例3用户跳出量实时统计)
- [第230讲-Spark Streaming实战开发进阶-综合案例3：版块pv实时统计](#第230讲-spark-streaming实战开发进阶-综合案例3版块pv实时统计)

<!-- /TOC -->


# 第220讲-Spark Streaming实战开发进阶-flume安装

## 1、安装flume
1、将课程提供的flume-ng-1.5.0-cdh5.3.6.tar.gz使用WinSCP拷贝到sparkproject1的/usr/local目录下。
2、对flume进行解压缩：tar -zxvf flume-ng-1.5.0-cdh5.3.6.tar.gz
3、对flume目录进行重命名：mv apache-flume-1.5.0-cdh5.3.6-bin flume
4、配置scala相关的环境变量
vi ~/.bashrc
export FLUME_HOME=/usr/local/flume
export FLUME_CONF_DIR=$FLUME_HOME/conf
export PATH=$FLUME_HOME/bin
source ~/.bashrc


## 2、修改flume配置文件
vi conf/flume-conf.properties

```
#agent1表示代理名称
agent1.sources=source1
agent1.sinks=sink1
agent1.channels=channel1
#配置source1
agent1.sources.source1.type=spooldir
agent1.sources.source1.spoolDir=/usr/local/logs
agent1.sources.source1.channels=channel1
agent1.sources.source1.fileHeader = false
agent1.sources.source1.interceptors = i1
agent1.sources.source1.interceptors.i1.type = timestamp
#配置channel1
agent1.channels.channel1.type=file
agent1.channels.channel1.checkpointDir=/usr/local/logs_tmp_cp
agent1.channels.channel1.dataDirs=/usr/local/logs_tmp
#配置sink1
agent1.sinks.sink1.type=hdfs
agent1.sinks.sink1.hdfs.path=hdfs://sparkproject1:9000/logs
agent1.sinks.sink1.hdfs.fileType=DataStream
agent1.sinks.sink1.hdfs.writeFormat=TEXT
agent1.sinks.sink1.hdfs.rollInterval=1
agent1.sinks.sink1.channel=channel1
agent1.sinks.sink1.hdfs.filePrefix=%Y-%m-%d
```


## 3、创建需要的文件夹

本地文件夹：mkdir /usr/local/logs
HDFS文件夹：hdfs dfs -mkdir /logs


## 4、启动flume-agent
flume-ng agent -n agent1 -c conf -f /usr/local/flume/conf/flume-conf.properties -Dflume.root.logger=DEBUG,console


## 5、测试flume
新建一份文件，移动到/usr/local/logs目录下，flume就会自动上传到HDFS的/logs目录中


```java
package cn.spark.study.sql.upgrade.news;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;

/**
 * 新闻网站关键指标离线统计Spark作业
 * @author Administrator
 *
 */
public class NewsOfflineStatSpark {

	public static void main(String[] args) {
		// 一般来说，在小公司中，可能就是将我们的spark作业使用linux的crontab进行调度
			// 将作业jar放在一台安装了spark客户端的机器上，并编写了对应的spark-submit shell脚本
			// 在crontab中可以配置，比如说每天凌晨3点执行一次spark-submit shell脚本，提交一次spark作业
			// 一般来说，离线的spark作业，每次运行，都是去计算昨天的数据
		// 大公司总，可能是使用较为复杂的开源大数据作业调度平台，比如常用的有azkaban、oozie等
			// 但是，最大的那几个互联网公司，比如说BAT、美团、京东，作业调度平台，都是自己开发的
			// 我们就会将开发好的Spark作业，以及对应的spark-submit shell脚本，配置在调度平台上，几点运行
			// 同理，每次运行，都是计算昨天的数据
		
		// 一般来说，每次spark作业计算出来的结果，实际上，大部分情况下，都会写入mysql等存储
		// 这样的话，我们可以基于mysql，用java web技术开发一套系统平台，来使用图表的方式展示每次spark计算
		// 出来的关键指标
		// 比如用折线图，可以反映最近一周的每天的用户跳出率的变化
		
		// 也可以通过页面，给用户提供一个查询表单，可以查询指定的页面的最近一周的pv变化
		// date pageid pv
		// 插入mysql中，后面用户就可以查询指定日期段内的某个page对应的所有pv，然后用折线图来反映变化曲线
		
		// 拿到昨天的日期，去hive表中，针对昨天的数据执行SQL语句
		String yesterday = getYesterday();
		
		// 创建SparkConf以及Spark上下文
		SparkConf conf = new SparkConf()
				.setAppName("NewsOfflineStatSpark")    
				.setMaster("local");  
		JavaSparkContext sc = new JavaSparkContext(conf);
		HiveContext hiveContext = new HiveContext(sc.sc());
	
		// 开发第一个关键指标：页面pv统计以及排序
		calculateDailyPagePv(hiveContext, yesterday);  
		// 开发第二个关键指标：页面uv统计以及排序
		calculateDailyPageUv(hiveContext, yesterday);
		// 开发第三个关键指标：新用户注册比率统计
		calculateDailyNewUserRegisterRate(hiveContext, yesterday);
		// 开发第四个关键指标：用户跳出率统计
		calculateDailyUserJumpRate(hiveContext, yesterday);
		// 开发第五个关键指标：版块热度排行榜
		calculateDailySectionPvSort(hiveContext, yesterday);
		
		// 关闭Spark上下文
		sc.close();
	}

	/**
	 * 获取昨天的字符串类型的日期
	 * @return 日期
	 */
	private static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);  
		
		Date yesterday = cal.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(yesterday);
	}
	
	/**
	 * 计算每天每个页面的pv以及排序
	 *   排序的好处：排序后，插入mysql，java web系统要查询每天pv top10的页面，直接查询mysql表limit 10就可以
	 *   如果我们这里不排序，那么java web系统就要做排序，反而会影响java web系统的性能，以及用户响应时间
	 */
	private static void calculateDailyPagePv(
			HiveContext hiveContext, String date) {
		String sql = 
				"SELECT "
					+ "date,"
					+ "pageid,"
					+ "pv "
				+ "FROM ( "
					+ "SELECT "
						+ "date,"
						+ "pageid,"
						+ "count(*) pv "
					+ "FROM news_access "
					+ "WHERE action='view' "
					+ "AND date='" + date + "' " 
					+ "GROUP BY date,pageid "
				+ ") t "
				+ "ORDER BY pv DESC ";  
		
		DataFrame df = hiveContext.sql(sql);
	
		// 在这里，我们也可以转换成一个RDD，然后对RDD执行一个foreach算子
		// 在foreach算子中，将数据写入mysql中
		
		df.show();  
	}
	
	/**
	 * 计算每天每个页面的uv以及排序
	 *   Spark SQL的count(distinct)语句，有bug，默认会产生严重的数据倾斜
	 *   只会用一个task，来做去重和汇总计数，性能很差
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailyPageUv(
			HiveContext hiveContext, String date) {
		String sql = 
				"SELECT "
					+ "date,"
					+ "pageid,"
					+ "uv "
				+ "FROM ( "
					+ "SELECT "
						+ "date,"
						+ "pageid,"
						+ "count(*) uv "
					+ "FROM ( "
						+ "SELECT "
							+ "date,"
							+ "pageid,"
							+ "userid "
						+ "FROM news_access "
						+ "WHERE action='view' "
						+ "AND date='" + date + "' "
						+ "GROUP BY date,pageid,userid "
					+ ") t2 "
					+ "GROUP BY date,pageid "
				+ ") t "
				+ "ORDER BY uv DESC ";
		
		DataFrame df = hiveContext.sql(sql);
		
		df.show();
	}
	
	/**
	 * 计算每天的新用户注册比例
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailyNewUserRegisterRate(
			HiveContext hiveContext, String date) {
		// 昨天所有访问行为中，userid为null，新用户的访问总数
		String sql1 = "SELECT count(*) FROM news_access WHERE action='view' AND date='" + date + "' AND userid IS NULL";
		// 昨天的总注册用户数
		String sql2 = "SELECT count(*) FROM news_access WHERE action='register' AND date='" + date + "' ";
	
		// 执行两条SQL，获取结果
		Object result1 = hiveContext.sql(sql1).collect()[0].get(0);
		long number1 = 0L;
		if(result1 != null) {
			number1 = Long.valueOf(String.valueOf(result1));  
		}
		
		Object result2 = hiveContext.sql(sql2).collect()[0].get(0);
		long number2 = 0L;
		if(result2 != null) {
			number2 = Long.valueOf(String.valueOf(result2));  
		}
		
		// 计算结果
		System.out.println("======================" + number1 + "======================");  
		System.out.println("======================" + number2 + "======================");  
		double rate = (double)number2 / (double)number1;
		System.out.println("======================" + formatDouble(rate, 2) + "======================");  
	}
	
	/**
	 * 计算每天的用户跳出率
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailyUserJumpRate(
			HiveContext hiveContext, String date) {
		// 计算已注册用户的昨天的总的访问pv
		String sql1 = "SELECT count(*) FROM news_access WHERE action='view' AND date='" + date + "' AND userid IS NOT NULL ";
		
		// 已注册用户的昨天跳出的总数
		String sql2 = "SELECT count(*) FROM ( SELECT count(*) cnt FROM news_access WHERE action='view' AND date='" + date + "' AND userid IS NOT NULL GROUP BY userid HAVING cnt=1 ) t ";
		
		// 执行两条SQL，获取结果
		Object result1 = hiveContext.sql(sql1).collect()[0].get(0);
		long number1 = 0L;
		if(result1 != null) {
			number1 = Long.valueOf(String.valueOf(result1));  
		}
		
		Object result2 = hiveContext.sql(sql2).collect()[0].get(0);
		long number2 = 0L;
		if(result2 != null) {
			number2 = Long.valueOf(String.valueOf(result2));  
		}
		
		// 计算结果
		System.out.println("======================" + number1 + "======================");  
		System.out.println("======================" + number2 + "======================");  
		double rate = (double)number2 / (double)number1;
		System.out.println("======================" + formatDouble(rate, 2) + "======================");
	}
	
	/**
	 * 计算每天的版块热度排行榜
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailySectionPvSort(
			HiveContext hiveContext, String date) {
		String sql = 
				"SELECT "
					+ "date,"
					+ "section,"
					+ "pv "
				+ "FROM ( "
					+ "SELECT "
						+ "date,"
						+ "section,"
						+ "count(*) pv "
					+ "FROM news_access "
					+ "WHERE action='view' "
					+ "AND date='" + date + "' "
					+ "GROUP BY date,section "
				+ ") t "
				+ "ORDER BY pv DESC ";
		
		DataFrame df = hiveContext.sql(sql);
		  
		df.show();
	}
	
	/**
	 * 格式化小数
	 * @param str 字符串
	 * @param scale 四舍五入的位数
	 * @return 格式化小数
	 */
	private static double formatDouble(double num, int scale) {
		BigDecimal bd = new BigDecimal(num);  
		return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}


```



# 第221讲-Spark Streaming实战开发进阶-接收flume实时数据流-flume风格的基于push的方式

Flume被设计为可以在agent之间推送数据，而不一定是从agent将数据传输到sink中。在这种方式下，Spark Streaming需要启动一个作为Avro Agent的Receiver，来让
flume可以推送数据过来。下面是我们的整合步骤：

前提需要

选择一台机器：
1、Spark Streaming与Flume都可以在这台机器上启动，Spark的其中一个Worker必须运行在这台机器上面
2、Flume可以将数据推送到这台机器上的某个端口

由于flume的push模型，Spark Streaming必须先启动起来，Receiver要被调度起来并且监听本地某个端口，来让flume推送数据。

配置flume

在flume-conf.properties文件中，配置flume的sink是将数据推送到其他的agent中

agent1.sinks.sink1.type = avro
agent1.sinks.sink1.channel = channel1
agent1.sinks.sink1.hostname = 192.168.0.103
agent1.sinks.sink1.port = 8888

配置spark streaming

在我们的spark工程的pom.xml中加入spark streaming整合flume的依赖

groupId = org.apache.spark
artifactId = spark-streaming-flume_2.10
version = 1.5.0

在代码中使用整合flume的方式创建输入DStream

import org.apache.spark.streaming.flume.*;

JavaReceiverInputDStream<SparkFlumeEvent> flumeStream =
	FlumeUtils.createStream(streamingContext, [chosen machine's hostname], [chosen port]);
	
这里有一点需要注意的是，这里监听的hostname，必须与cluster manager（比如Standalone Master、YARN ResourceManager）是同一台机器，这样cluster manager
才能匹配到正确的机器，并将receiver调度在正确的机器上运行。

部署spark streaming应用

打包工程为一个jar包，使用spark-submit来提交作业

启动flume agent

flume-ng agent -n agent1 -c conf -f /usr/local/flume/conf/flume-conf.properties -Dflume.root.logger=DEBUG,console

什么时候我们应该用Spark Streaming整合Kafka去用，做实时计算？
什么使用应该整合flume？

看你的实时数据流的产出频率
1、如果你的实时数据流产出特别频繁，比如说一秒钟10w条，那就必须是kafka，分布式的消息缓存中间件，可以承受超高并发
2、如果你的实时数据流产出频率不固定，比如有的时候是1秒10w，有的时候是1个小时才10w，可以选择将数据用nginx日志来表示，每隔一段时间将日志文件
放到flume监控的目录中，然后呢，spark streaming来计算


```java
package cn.spark.study.streaming;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import scala.Tuple2;

/**
 * 基于Flume Push方式的实时wordcount程序
 * @author Administrator
 *
 */
public class FlumePushWordCount {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("FlumePushWordCount");  
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		
		JavaReceiverInputDStream<SparkFlumeEvent> lines =
				FlumeUtils.createStream(jssc, "192.168.0.103", 8888);  
		
		JavaDStream<String> words = lines.flatMap(
				
				new FlatMapFunction<SparkFlumeEvent, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Iterable<String> call(SparkFlumeEvent event) throws Exception {
						String line = new String(event.event().getBody().array());  
						return Arrays.asList(line.split(" "));   
					}
					
				});
		
		JavaPairDStream<String, Integer> pairs = words.mapToPair(
				
				new PairFunction<String, String, Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, Integer> call(String word) throws Exception {
						return new Tuple2<String, Integer>(word, 1);
					}
					
				});
		
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
				
				new Function2<Integer, Integer, Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Integer call(Integer v1, Integer v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		wordCounts.print();
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
}


```


# 第222讲-Spark Streaming实战开发进阶-接收flume实时数据流-自定义sink的基于poll的方式

除了让flume将数据推送到spark streaming，还有一种方式，可以运行一个自定义的flume sink
1、Flume推送数据到sink中，然后数据缓存在sink中
2、spark streaming用一个可靠的flume receiver以及事务机制从sink中拉取数据

前提条件
1、选择一台可以在flume agent中运行自定义sink的机器
2、将flume的数据管道流配置为将数据传送到那个sink中
3、spark streaming所在的机器可以从那个sink中拉取数据

配置flume

1、加入sink jars，将以下jar加入flume的classpath中

groupId = org.apache.spark
artifactId = spark-streaming-flume-sink_2.10
version = 1.5.1

groupId = org.scala-lang
artifactId = scala-library
version = 2.10.4

groupId = org.apache.commons
artifactId = commons-lang3
version = 3.3.2

2、修改配置文件

agent.sinks.sink1.type = org.apache.spark.streaming.flume.sink.SparkSink
agent.sinks.sink1.hostname = 192.168.0.103
agent.sinks.sink1.port = 8888
agent.sinks.sink1.channel = channel1

配置spark streaming

import org.apache.spark.streaming.flume.*;

JavaReceiverInputDStream<SparkFlumeEvent>flumeStream =
	FlumeUtils.createPollingStream(streamingContext, [sink machine hostname], [sink port]);
	
一定要先启动flume，再启动spark streaming

flume-ng agent -n agent1 -c conf -f /usr/local/flume/conf/flume-conf.properties -Dflume.root.logger=DEBUG,console


```java

package cn.spark.study.streaming;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import scala.Tuple2;

/**
 * 基于Flume Poll方式的实时wordcount程序
 * @author Administrator
 *
 */
public class FlumePollWordCount {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("FlumePollWordCount");  
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		
		JavaReceiverInputDStream<SparkFlumeEvent> lines =
				FlumeUtils.createPollingStream(jssc, "192.168.0.103", 8888);  
		
		JavaDStream<String> words = lines.flatMap(
				
				new FlatMapFunction<SparkFlumeEvent, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Iterable<String> call(SparkFlumeEvent event) throws Exception {
						String line = new String(event.event().getBody().array());  
						return Arrays.asList(line.split(" "));   
					}
					
				});
		
		JavaPairDStream<String, Integer> pairs = words.mapToPair(
				
				new PairFunction<String, String, Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, Integer> call(String word) throws Exception {
						return new Tuple2<String, Integer>(word, 1);
					}
					
				});
		
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
				
				new Function2<Integer, Integer, Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Integer call(Integer v1, Integer v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		wordCounts.print();
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
}

```


# 第223讲-Spark Streaming实战开发进阶-高阶技术之自定义Receiver

spark streaming可以从任何数据源来接收数据，哪怕是除了它内置支持的数据源以外的其他数据源（比如flume、kafka、socket等）。
如果我们想要从spark streaming没有内置支持的数据源中接收实时数据，那么我们需要自己实现一个receiver。

实现一个自定义的receiver

一个自定义的receiver必须实现以下两个方法：onStart()、onStop()。onStart()和onStop()方法必须不能阻塞数据，一般来说，
onStart()方法会启动负责接收数据的线程，onStop()方法会确保之前启动的线程都已经停止了。负责接收数据的线程可以调用
isStopped()方法来检查它们是否应该停止接收数据。

一旦数据被接收了，就可以调用store(data)方法，数据就可以被存储在Spark内部。有一系列的store()重载方法供我们调用，来将数据
每次一条进行存储，或是每次存储一个集合或序列化的数据。

接收线程中的任何异常都应该被捕获以及正确处理，从而避免receiver的静默失败。restart()方法会通过异步地调用onStop()和
onStart()方法来重启receiver。stop()方法会调用onStop()方法来停止receiver。reportError()方法会汇报一个错误消息给driver
，但是不停止或重启receiver。

public class JavaCustomReceiver extends Receiver<String> {

  String host = null;
  int port = -1;

  public JavaCustomReceiver(String host_ , int port_) {
    super(StorageLevel.MEMORY_AND_DISK_2());
    host = host_;
    port = port_;
  }

  public void onStart() {
    // Start the thread that receives data over a connection
    new Thread()  {
      @Override public void run() {
        receive();
      }
    }.start();
  }

  public void onStop() {

  }

  /** Create a socket connection and receive data until receiver is stopped */
  private void receive() {
    Socket socket = null;
    String userInput = null;

    try {
      // connect to the server
      socket = new Socket(host, port);

      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Until stopped or connection broken continue reading
      while (!isStopped() && (userInput = reader.readLine()) != null) {
        System.out.println("Received data '" + userInput + "'");
        store(userInput);
      }
      reader.close();
      socket.close();

      // Restart in an attempt to connect again when server is active again
      restart("Trying to connect again");
    } catch(ConnectException ce) {
      // restart if could not connect to server
      restart("Could not connect", ce);
    } catch(Throwable t) {
      // restart if there is any other error
      restart("Error receiving data", t);
    }
  }
}

在spark streaming中使用自定义的receiver

JavaDStream<String> lines = ssc.receiverStream(new JavaCustomReceiver(host, port));

安装网络服务工具

rpm -ihv netcat-0.7.1-1.i386.rpm

nc -lk 9999


```java


```

# 第224讲-Spark Streaming实战开发进阶-kafka安装

## 1、安装ZooKeeper包
1、将课程提供的zookeeper-3.4.5.tar.gz使用WinSCP拷贝到spark1的/usr/local目录下。
2、对zookeeper-3.4.5.tar.gz进行解压缩：tar -zxvf zookeeper-3.4.5.tar.gz。
3、对zookeeper目录进行重命名：mv zookeeper-3.4.5 zk。
4、配置zookeeper相关的环境变量
vi .bashrc
export ZOOKEEPER_HOME=/usr/local/zk
export PATH=$ZOOKEEPER_HOME/bin
source .bashrc

## 2、配置zoo.cfg
cd zk/conf
mv zoo_sample.cfg zoo.cfg

vi zoo.cfg
修改：dataDir=/usr/local/zk/data
新增：
server.0=spark1:2888:3888	
server.1=spark2:2888:3888
server.2=spark3:2888:3888

## 3、设置zk节点标识

cd zk
mkdir data
cd data

vi myid
0

## 4、启动ZooKeeper集群
1、分别在三台机器上执行：zkServer.sh start。
2、检查ZooKeeper状态：zkServer.sh status。


## 5、安装scala 2.11.4
1、将课程提供的scala-2.11.4.tgz使用WinSCP拷贝到spark1的/usr/local目录下。
2、对scala-2.11.4.tgz进行解压缩：tar -zxvf scala-2.11.4.tgz。
3、对scala目录进行重命名：mv scala-2.11.4 scala
4、配置scala相关的环境变量
vi .bashrc
export SCALA_HOME=/usr/local/scala
export PATH=$SCALA_HOME/bin
source .bashrc
5、查看scala是否安装成功：scala -version
6、按照上述步骤在spark2和spark3机器上都安装好scala。使用scp将scala和.bashrc拷贝到spark2和spark3上即可。


## 6、安装Kafka包
1、将课程提供的kafka_2.9.2-0.8.1.tgz使用WinSCP拷贝到spark1的/usr/local目录下。
2、对kafka_2.9.2-0.8.1.tgz进行解压缩：tar -zxvf kafka_2.9.2-0.8.1.tgz。
3、对kafka目录进行改名：mv kafka_2.9.2-0.8.1 kafka
4、配置kafka
vi /usr/local/kafka/config/server.properties
broker.id：依次增长的整数，0、1、2、3、4，集群中Broker的唯一id
zookeeper.connect=192.168.1.107:2181,192.168.1.108:2181,192.168.1.109:2181
5、安装slf4j
将课程提供的slf4j-1.7.6.zip上传到/usr/local目录下
unzip slf4j-1.7.6.zip
把slf4j中的slf4j-nop-1.7.6.jar复制到kafka的libs目录下面


## 7、搭建kafka集群
1、按照上述步骤在spark2和spark3分别安装kafka。用scp把kafka拷贝到spark2和spark3行即可。
2、唯一区别的，就是server.properties中的broker.id，要设置为1和2


## 8、启动kafka集群
1、在三台机器上分别执行以下命令：
nohup kafka-server-start.sh /usr/local/kafka/config/server.properties &

2、解决kafka Unrecognized VM option 'UseCompressedOops'问题
vi bin/kafka-run-class.sh 
if [ -z "$KAFKA_JVM_PERFORMANCE_OPTS" ]; then
  KAFKA_JVM_PERFORMANCE_OPTS="-server  -XX:+UseCompressedOops -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:+CMSScavengeBeforeRemark -XX:+DisableExplicitGC -Djava.awt.headless=true"
fi
去掉-XX:+UseCompressedOops即可

3、使用jps检查启动是否成功


## 9、测试kafka集群
使用基本命令检查kafka是否搭建成功

kafka-topics.sh --zookeeper 192.168.0.103:2181,192.168.0.104:2181 --topic TestTopic --replication-factor 1 --partitions 1 --create

kafka-console-producer.sh --broker-list 192.168.0.103:9092,192.168.0.104:9092 --topic TestTopic

kafka-console-consumer.sh --zookeeper 192.168.0.103:2181,192.168.0.104:2181 --topic TestTopic --from-beginning





# 第225讲-Spark Streaming实战开发进阶-综合案例3：新闻网站关键指标实时统计

```java
kafka-topics.sh --zookeeper 192.168.0.103:2181,192.168.0.104:2181 --topic news-access --replication-factor 1 --partitions 1 --create
kafka-console-consumer.sh --zookeeper 192.168.0.103:2181,192.168.0.104:2181 --topic news-access --from-beginning

```


# 第226讲-Spark Streaming实战开发进阶-综合案例3：页面pv实时统计

```java
package cn.spark.study.streaming.upgrade.news;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 访问日志Kafka Producer
 * @author Administrator
 *
 */
public class AccessProducer extends Thread {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static Random random = new Random();
	private static String[] sections = new String[] {"country", "international", "sport", "entertainment", "movie", "carton", "tv-show", "technology", "internet", "car"};
	private static int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private static String date;
	
	private Producer<Integer, String> producer;
	private String topic;
	
	public AccessProducer(String topic) {
		this.topic = topic;
		producer = new Producer<Integer, String>(createProducerConfig());  
		date = sdf.format(new Date());  
	}
	
	private ProducerConfig createProducerConfig() {
		Properties props = new Properties();
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", "192.168.0.103:9092,192.168.0.104:9092");
		return new ProducerConfig(props);  
	}
	
	public void run() {
		int counter = 0;

		while(true) {
			for(int i = 0; i < 100; i++) {
				String log = null;
				
				if(arr[random.nextInt(10)] == 1) {
					log = getRegisterLog();
				} else {
					log = getAccessLog();
				}
				
				producer.send(new KeyedMessage<Integer, String>(topic, log));
				
				counter++;
				if(counter == 100) {
					counter = 0;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}  
				}
			}
		}
	}
	
	private static String getAccessLog() {
		StringBuffer buffer = new StringBuffer("");  
		
		// 生成时间戳
		long timestamp = new Date().getTime();
		
		// 生成随机userid（默认1000注册用户，每天1/10的访客是未注册用户）
		Long userid = 0L;
		
		int newOldUser = arr[random.nextInt(10)];
		if(newOldUser == 1) {
			userid = null;
		} else {
			userid = (long) random.nextInt(1000);
		}
		
		// 生成随机pageid（总共1k个页面）
		Long pageid = (long) random.nextInt(1000);  
		
		// 生成随机版块（总共10个版块）
		String section = sections[random.nextInt(10)]; 
		
		// 生成固定的行为，view
		String action = "view"; 
		
		return buffer.append(date).append(" ")  
				.append(timestamp).append(" ")
				.append(userid).append(" ")
				.append(pageid).append(" ")
				.append(section).append(" ")
				.append(action).toString();
	}
	
	private static String getRegisterLog() {
		StringBuffer buffer = new StringBuffer("");
		
		// 生成时间戳
		long timestamp = new Date().getTime();
		
		// 新用户都是userid为null
		Long userid = null;

		// 生成随机pageid，都是null
		Long pageid = null;  
		
		// 生成随机版块，都是null
		String section = null; 
		
		// 生成固定的行为，view
		String action = "register"; 
		
		return buffer.append(date).append(" ")  
				.append(timestamp).append(" ")
				.append(userid).append(" ")
				.append(pageid).append(" ")
				.append(section).append(" ")
				.append(action).toString();
	}
	
	public static void main(String[] args) {
		AccessProducer producer = new AccessProducer("news-access");    
		producer.start();
	}
	
}


```

```java
package cn.spark.study.streaming.upgrade.news;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * 新闻网站关键指标实时统计Spark应用程序
 * @author Administrator
 *
 */
public class NewsRealtimeStatSpark {

	public static void main(String[] args) throws Exception {
		// 创建Spark上下文
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("NewsRealtimeStatSpark");  
		JavaStreamingContext jssc = new JavaStreamingContext(
				conf, Durations.seconds(5));  
		
		// 创建输入DStream
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", 
				"192.168.0.103:9092,192.168.0.104:9092");
		
		Set<String> topics = new HashSet<String>();
		topics.add("news-access");  
		
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(
				jssc, 
				String.class, 
				String.class, 
				StringDecoder.class, 
				StringDecoder.class, 
				kafkaParams, 
				topics);
		
		// 过滤出访问日志
		JavaPairDStream<String, String> accessDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("view".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		// 统计第一个指标：每10秒内的各个页面的pv
		calculatePagePv(accessDStream);  
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
	/**
	 * 计算页面pv
	 * @param accessDStream
	 */
	private static void calculatePagePv(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> pageidDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						
						return new Tuple2<Long, Long>(pageid, 1L);  
					}
					
				});
		
		JavaPairDStream<Long, Long> pagePvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pagePvDStream.print();  
		
		// 在计算出每10秒钟的页面pv之后，其实在真实项目中，应该持久化
		// 到mysql，或redis中，对每个页面的pv进行累加
		// javaee系统，就可以从mysql或redis中，读取page pv实时变化的数据，以及曲线图
	}
	
}


```


# 第227讲-Spark Streaming实战开发进阶-综合案例3：页面uv实时统计

```java
package cn.spark.study.streaming.upgrade.news;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * 新闻网站关键指标实时统计Spark应用程序
 * @author Administrator
 *
 */
public class NewsRealtimeStatSpark {

	public static void main(String[] args) throws Exception {
		// 创建Spark上下文
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("NewsRealtimeStatSpark");  
		JavaStreamingContext jssc = new JavaStreamingContext(
				conf, Durations.seconds(5));  
		
		// 创建输入DStream
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", 
				"192.168.0.103:9092,192.168.0.104:9092");
		
		Set<String> topics = new HashSet<String>();
		topics.add("news-access");  
		
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(
				jssc, 
				String.class, 
				String.class, 
				StringDecoder.class, 
				StringDecoder.class, 
				kafkaParams, 
				topics);
		
		// 过滤出访问日志
		JavaPairDStream<String, String> accessDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("view".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		// 统计第一个指标：每10秒内的各个页面的pv
//		calculatePagePv(accessDStream);  
		// 统计第二个指标：每10秒内的各个页面的uv
		calculatePageUv(accessDStream); 
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
	/**
	 * 计算页面pv
	 * @param accessDStream
	 */
	private static void calculatePagePv(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> pageidDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						
						return new Tuple2<Long, Long>(pageid, 1L);  
					}
					
				});
		
		JavaPairDStream<Long, Long> pagePvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pagePvDStream.print();  
		
		// 在计算出每10秒钟的页面pv之后，其实在真实项目中，应该持久化
		// 到mysql，或redis中，对每个页面的pv进行累加
		// javaee系统，就可以从mysql或redis中，读取page pv实时变化的数据，以及曲线图
	}
	
	/**
	 * 计算页面uv
	 * @param <U>
	 * @param accessDStream
	 */
	private static <U> void calculatePageUv(JavaPairDStream<String, String> accessDStream) {
		JavaDStream<String> pageidUseridDStream = accessDStream.map(
				
				new Function<Tuple2<String,String>, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						Long userid = Long.valueOf("null".equalsIgnoreCase(logSplited[2]) ? "-1" : logSplited[2]);  
						
						return pageid + "_" + userid;
					}
					
				});
		
		JavaDStream<String> distinctPageidUseridDStream = pageidUseridDStream.transform(
				
				new Function<JavaRDD<String>, JavaRDD<String>>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
						return rdd.distinct();
					}
					
				});
		
		JavaPairDStream<Long, Long> pageidDStream = distinctPageidUseridDStream.mapToPair(
				
				new PairFunction<String, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(String str) throws Exception {
						String[] splited = str.split("_");  
						Long pageid = Long.valueOf(splited[0]);  
						return new Tuple2<Long, Long>(pageid, 1L);   
					}
					
				});
		
		JavaPairDStream<Long, Long> pageUvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pageUvDStream.print();
	}
	
}


```


# 第228讲-Spark Streaming实战开发进阶-综合案例3：注册用户数实时统计

```java
package cn.spark.study.streaming.upgrade.news;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * 新闻网站关键指标实时统计Spark应用程序
 * @author Administrator
 *
 */
public class NewsRealtimeStatSpark {

	public static void main(String[] args) throws Exception {
		// 创建Spark上下文
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("NewsRealtimeStatSpark");  
		JavaStreamingContext jssc = new JavaStreamingContext(
				conf, Durations.seconds(5));  
		
		// 创建输入DStream
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", 
				"192.168.0.103:9092,192.168.0.104:9092");
		
		Set<String> topics = new HashSet<String>();
		topics.add("news-access");  
		
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(
				jssc, 
				String.class, 
				String.class, 
				StringDecoder.class, 
				StringDecoder.class, 
				kafkaParams, 
				topics);
		
		// 过滤出访问日志
		JavaPairDStream<String, String> accessDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("view".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		// 统计第一个指标：实时页面pv
//		calculatePagePv(accessDStream);  
		// 统计第二个指标：实时页面uv
//		calculatePageUv(accessDStream); 
		// 统计第三个指标：实时注册用户数
		calculateRegisterCount(lines);  
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
	/**
	 * 计算页面pv
	 * @param accessDStream
	 */
	private static void calculatePagePv(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> pageidDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						
						return new Tuple2<Long, Long>(pageid, 1L);  
					}
					
				});
		
		JavaPairDStream<Long, Long> pagePvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pagePvDStream.print();  
		
		// 在计算出每10秒钟的页面pv之后，其实在真实项目中，应该持久化
		// 到mysql，或redis中，对每个页面的pv进行累加
		// javaee系统，就可以从mysql或redis中，读取page pv实时变化的数据，以及曲线图
	}
	
	/**
	 * 计算页面uv
	 * @param <U>
	 * @param accessDStream
	 */
	private static <U> void calculatePageUv(JavaPairDStream<String, String> accessDStream) {
		JavaDStream<String> pageidUseridDStream = accessDStream.map(
				
				new Function<Tuple2<String,String>, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						Long userid = Long.valueOf("null".equalsIgnoreCase(logSplited[2]) ? "-1" : logSplited[2]);  
						
						return pageid + "_" + userid;
					}
					
				});
		
		JavaDStream<String> distinctPageidUseridDStream = pageidUseridDStream.transform(
				
				new Function<JavaRDD<String>, JavaRDD<String>>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
						return rdd.distinct();
					}
					
				});
		
		JavaPairDStream<Long, Long> pageidDStream = distinctPageidUseridDStream.mapToPair(
				
				new PairFunction<String, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(String str) throws Exception {
						String[] splited = str.split("_");  
						Long pageid = Long.valueOf(splited[0]);  
						return new Tuple2<Long, Long>(pageid, 1L);   
					}
					
				});
		
		JavaPairDStream<Long, Long> pageUvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pageUvDStream.print();
	}
	
	/**
	 * 计算实时注册用户数
	 * @param lines
	 */
	private static void calculateRegisterCount(JavaPairInputDStream<String, String> lines) {
		JavaPairDStream<String, String> registerDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("register".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		JavaDStream<Long> registerCountDStream = registerDStream.count();
		
		registerCountDStream.print();  
		
		// 每次统计完一个最近10秒的数据之后，不是打印出来
		// 去存储（mysql、redis、hbase），选用哪一种主要看你的公司提供的环境，以及你的看实时报表的用户以及并发数量，包括你的数据量
		// 如果是一般的展示效果，就选用mysql就可以
		// 如果是需要超高并发的展示，比如QPS 1w来看实时报表，那么建议用redis、memcached
		// 如果是数据量特别大，建议用hbase
		
		// 每次从存储中，查询注册数量，最近一次插入的记录，比如上一次是10秒前
		// 然后将当前记录与上一次的记录累加，然后往存储中插入一条新记录，就是最新的一条数据
		// 然后javaee系统在展示的时候，可以比如查看最近半小时内的注册用户数量变化的曲线图
		// 查看一周内，每天的注册用户数量的变化曲线图（每天就取最后一条数据，就是每天的最终数据）
	}
	
}


```


# 第229讲-Spark Streaming实战开发进阶-综合案例3：用户跳出量实时统计

```java
package cn.spark.study.streaming.upgrade.news;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * 新闻网站关键指标实时统计Spark应用程序
 * @author Administrator
 *
 */
public class NewsRealtimeStatSpark {

	public static void main(String[] args) throws Exception {
		// 创建Spark上下文
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("NewsRealtimeStatSpark");  
		JavaStreamingContext jssc = new JavaStreamingContext(
				conf, Durations.seconds(5));  
		
		// 创建输入DStream
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", 
				"192.168.0.103:9092,192.168.0.104:9092");
		
		Set<String> topics = new HashSet<String>();
		topics.add("news-access");  
		
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(
				jssc, 
				String.class, 
				String.class, 
				StringDecoder.class, 
				StringDecoder.class, 
				kafkaParams, 
				topics);
		
		// 过滤出访问日志
		JavaPairDStream<String, String> accessDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("view".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		// 统计第一个指标：实时页面pv
//		calculatePagePv(accessDStream);  
		// 统计第二个指标：实时页面uv
//		calculatePageUv(accessDStream); 
		// 统计第三个指标：实时注册用户数
//		calculateRegisterCount(lines);  
		// 统计第四个指标：实时用户跳出数
//		calculateUserJumpCount(accessDStream);  
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
	/**
	 * 计算页面pv
	 * @param accessDStream
	 */
	private static void calculatePagePv(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> pageidDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						
						return new Tuple2<Long, Long>(pageid, 1L);  
					}
					
				});
		
		JavaPairDStream<Long, Long> pagePvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pagePvDStream.print();  
		
		// 在计算出每10秒钟的页面pv之后，其实在真实项目中，应该持久化
		// 到mysql，或redis中，对每个页面的pv进行累加
		// javaee系统，就可以从mysql或redis中，读取page pv实时变化的数据，以及曲线图
	}
	
	/**
	 * 计算页面uv
	 * @param <U>
	 * @param accessDStream
	 */
	private static <U> void calculatePageUv(JavaPairDStream<String, String> accessDStream) {
		JavaDStream<String> pageidUseridDStream = accessDStream.map(
				
				new Function<Tuple2<String,String>, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						Long userid = Long.valueOf("null".equalsIgnoreCase(logSplited[2]) ? "-1" : logSplited[2]);  
						
						return pageid + "_" + userid;
					}
					
				});
		
		JavaDStream<String> distinctPageidUseridDStream = pageidUseridDStream.transform(
				
				new Function<JavaRDD<String>, JavaRDD<String>>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
						return rdd.distinct();
					}
					
				});
		
		JavaPairDStream<Long, Long> pageidDStream = distinctPageidUseridDStream.mapToPair(
				
				new PairFunction<String, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(String str) throws Exception {
						String[] splited = str.split("_");  
						Long pageid = Long.valueOf(splited[0]);  
						return new Tuple2<Long, Long>(pageid, 1L);   
					}
					
				});
		
		JavaPairDStream<Long, Long> pageUvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pageUvDStream.print();
	}
	
	/**
	 * 计算实时注册用户数
	 * @param lines
	 */
	private static void calculateRegisterCount(JavaPairInputDStream<String, String> lines) {
		JavaPairDStream<String, String> registerDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("register".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		JavaDStream<Long> registerCountDStream = registerDStream.count();
		
		registerCountDStream.print();  
		
		// 每次统计完一个最近10秒的数据之后，不是打印出来
		// 去存储（mysql、redis、hbase），选用哪一种主要看你的公司提供的环境，以及你的看实时报表的用户以及并发数量，包括你的数据量
		// 如果是一般的展示效果，就选用mysql就可以
		// 如果是需要超高并发的展示，比如QPS 1w来看实时报表，那么建议用redis、memcached
		// 如果是数据量特别大，建议用hbase
		
		// 每次从存储中，查询注册数量，最近一次插入的记录，比如上一次是10秒前
		// 然后将当前记录与上一次的记录累加，然后往存储中插入一条新记录，就是最新的一条数据
		// 然后javaee系统在展示的时候，可以比如查看最近半小时内的注册用户数量变化的曲线图
		// 查看一周内，每天的注册用户数量的变化曲线图（每天就取最后一条数据，就是每天的最终数据）
	}

	/**
	 * 计算用户跳出数量
	 * @param accessDStream
	 */
	private static void calculateUserJumpCount(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> useridDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						Long userid = Long.valueOf("null".equalsIgnoreCase(logSplited[2]) ? "-1" : logSplited[2]);    
						return new Tuple2<Long, Long>(userid, 1L); 
					}
					
				});
		
		JavaPairDStream<Long, Long> useridCountDStream = useridDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		JavaPairDStream<Long, Long> jumpUserDStream = useridCountDStream.filter(
				
				new Function<Tuple2<Long,Long>, Boolean>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Boolean call(Tuple2<Long, Long> tuple) throws Exception {
						if(tuple._2 == 1) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		JavaDStream<Long> jumpUserCountDStream = jumpUserDStream.count();
		
		jumpUserCountDStream.print();  
	}
	
}


```


# 第230讲-Spark Streaming实战开发进阶-综合案例3：版块pv实时统计

```java
package cn.spark.study.streaming.upgrade.news;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * 新闻网站关键指标实时统计Spark应用程序
 * @author Administrator
 *
 */
public class NewsRealtimeStatSpark {

	public static void main(String[] args) throws Exception {
		// 创建Spark上下文
		SparkConf conf = new SparkConf()
				.setMaster("local[2]")
				.setAppName("NewsRealtimeStatSpark");  
		JavaStreamingContext jssc = new JavaStreamingContext(
				conf, Durations.seconds(5));  
		
		// 创建输入DStream
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", 
				"192.168.0.103:9092,192.168.0.104:9092");
		
		Set<String> topics = new HashSet<String>();
		topics.add("news-access");  
		
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(
				jssc, 
				String.class, 
				String.class, 
				StringDecoder.class, 
				StringDecoder.class, 
				kafkaParams, 
				topics);
		
		// 过滤出访问日志
		JavaPairDStream<String, String> accessDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("view".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		// 统计第一个指标：实时页面pv
		calculatePagePv(accessDStream);  
		// 统计第二个指标：实时页面uv
		calculatePageUv(accessDStream); 
		// 统计第三个指标：实时注册用户数
		calculateRegisterCount(lines);  
		// 统计第四个指标：实时用户跳出数
		calculateUserJumpCount(accessDStream);
		// 统计第五个指标：实时版块pv
		calcualteSectionPv(accessDStream);  
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	
	/**
	 * 计算页面pv
	 * @param accessDStream
	 */
	private static void calculatePagePv(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> pageidDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						
						return new Tuple2<Long, Long>(pageid, 1L);  
					}
					
				});
		
		JavaPairDStream<Long, Long> pagePvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {
			
					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pagePvDStream.print();  
		
		// 在计算出每10秒钟的页面pv之后，其实在真实项目中，应该持久化
		// 到mysql，或redis中，对每个页面的pv进行累加
		// javaee系统，就可以从mysql或redis中，读取page pv实时变化的数据，以及曲线图
	}
	
	/**
	 * 计算页面uv
	 * @param <U>
	 * @param accessDStream
	 */
	private static <U> void calculatePageUv(JavaPairDStream<String, String> accessDStream) {
		JavaDStream<String> pageidUseridDStream = accessDStream.map(
				
				new Function<Tuple2<String,String>, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" "); 
						
						Long pageid = Long.valueOf(logSplited[3]);  
						Long userid = Long.valueOf("null".equalsIgnoreCase(logSplited[2]) ? "-1" : logSplited[2]);  
						
						return pageid + "_" + userid;
					}
					
				});
		
		JavaDStream<String> distinctPageidUseridDStream = pageidUseridDStream.transform(
				
				new Function<JavaRDD<String>, JavaRDD<String>>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
						return rdd.distinct();
					}
					
				});
		
		JavaPairDStream<Long, Long> pageidDStream = distinctPageidUseridDStream.mapToPair(
				
				new PairFunction<String, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(String str) throws Exception {
						String[] splited = str.split("_");  
						Long pageid = Long.valueOf(splited[0]);  
						return new Tuple2<Long, Long>(pageid, 1L);   
					}
					
				});
		
		JavaPairDStream<Long, Long> pageUvDStream = pageidDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		pageUvDStream.print();
	}
	
	/**
	 * 计算实时注册用户数
	 * @param lines
	 */
	private static void calculateRegisterCount(JavaPairInputDStream<String, String> lines) {
		JavaPairDStream<String, String> registerDStream = lines.filter(
				
				new Function<Tuple2<String,String>, Boolean>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Boolean call(Tuple2<String, String> tuple) throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String action = logSplited[5];
						if("register".equals(action)) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		JavaDStream<Long> registerCountDStream = registerDStream.count();
		
		registerCountDStream.print();  
		
		// 每次统计完一个最近10秒的数据之后，不是打印出来
		// 去存储（mysql、redis、hbase），选用哪一种主要看你的公司提供的环境，以及你的看实时报表的用户以及并发数量，包括你的数据量
		// 如果是一般的展示效果，就选用mysql就可以
		// 如果是需要超高并发的展示，比如QPS 1w来看实时报表，那么建议用redis、memcached
		// 如果是数据量特别大，建议用hbase
		
		// 每次从存储中，查询注册数量，最近一次插入的记录，比如上一次是10秒前
		// 然后将当前记录与上一次的记录累加，然后往存储中插入一条新记录，就是最新的一条数据
		// 然后javaee系统在展示的时候，可以比如查看最近半小时内的注册用户数量变化的曲线图
		// 查看一周内，每天的注册用户数量的变化曲线图（每天就取最后一条数据，就是每天的最终数据）
	}

	/**
	 * 计算用户跳出数量
	 * @param accessDStream
	 */
	private static void calculateUserJumpCount(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<Long, Long> useridDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<Long, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						Long userid = Long.valueOf("null".equalsIgnoreCase(logSplited[2]) ? "-1" : logSplited[2]);    
						return new Tuple2<Long, Long>(userid, 1L); 
					}
					
				});
		
		JavaPairDStream<Long, Long> useridCountDStream = useridDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		JavaPairDStream<Long, Long> jumpUserDStream = useridCountDStream.filter(
				
				new Function<Tuple2<Long,Long>, Boolean>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Boolean call(Tuple2<Long, Long> tuple) throws Exception {
						if(tuple._2 == 1) {
							return true;
						} else {
							return false;
						}
					}
					
				});
		
		JavaDStream<Long> jumpUserCountDStream = jumpUserDStream.count();
		
		jumpUserCountDStream.print();  
	}
	
	/**
	 * 版块实时pv
	 * @param accessDStream
	 */
	private static void calcualteSectionPv(JavaPairDStream<String, String> accessDStream) {
		JavaPairDStream<String, Long> sectionDStream = accessDStream.mapToPair(
				
				new PairFunction<Tuple2<String,String>, String, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Tuple2<String, Long> call(Tuple2<String, String> tuple)
							throws Exception {
						String log = tuple._2;
						String[] logSplited = log.split(" ");  
						
						String section = logSplited[4];
						
						return new Tuple2<String, Long>(section, 1L);  
					}
					
				});
		
		JavaPairDStream<String, Long> sectionPvDStream = sectionDStream.reduceByKey(
				
				new Function2<Long, Long, Long>() {

					private static final long serialVersionUID = 1L;
		
					@Override
					public Long call(Long v1, Long v2) throws Exception {
						return v1 + v2;
					}
					
				});
		
		sectionPvDStream.print();
	}
	
}


```


