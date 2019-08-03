
<!-- TOC -->

- [第88讲-Spark Streaming：大数据实时计算介绍](#第88讲-spark-streaming大数据实时计算介绍)
- [第89讲-Spark Streaming：DStream以及基本工作原理](#第89讲-spark-streamingdstream以及基本工作原理)
    - [1、Spark Streaming简介](#1spark-streaming简介)
    - [2、Spark Streaming基本工作原理](#2spark-streaming基本工作原理)
    - [3、DStream](#3dstream)
- [第90讲-Spark Streaming：与Storm的对比分析](#第90讲-spark-streaming与storm的对比分析)
    - [1、Spark Streaming与Storm的优劣分析](#1spark-streaming与storm的优劣分析)
    - [2、Spark Streaming与Storm的应用场景](#2spark-streaming与storm的应用场景)
- [第91讲-Spark Streaming：实时wordcount程序开发](#第91讲-spark-streaming实时wordcount程序开发)
- [第92讲-Spark Streaming：StreamingContext详解](#第92讲-spark-streamingstreamingcontext详解)
- [第93讲-Spark Streaming：输入DStream和Receiver详解](#第93讲-spark-streaming输入dstream和receiver详解)
- [第94讲-Spark Streaming：输入DStream之基础数据源以及基于HDFS的实时wordcount程序](#第94讲-spark-streaming输入dstream之基础数据源以及基于hdfs的实时wordcount程序)
- [第95讲-Spark Streaming：输入DStream之Kafka数据源实战（基于Receiver的方式）](#第95讲-spark-streaming输入dstream之kafka数据源实战基于receiver的方式)
    - [1、基于Receiver的方式](#1基于receiver的方式)
    - [2、如何进行Kafka数据源连接](#2如何进行kafka数据源连接)
    - [3、需要注意的要点](#3需要注意的要点)
    - [4、Kafka命令](#4kafka命令)
- [第96讲-Spark Streaming：输入DStream之Kafka数据源实战（基于Direct的方式）](#第96讲-spark-streaming输入dstream之kafka数据源实战基于direct的方式)
    - [1、基于Direct的方式](#1基于direct的方式)
    - [2、Kafka命令](#2kafka命令)
- [第97讲-Spark Streaming：DStream的transformation操作概览](#第97讲-spark-streamingdstream的transformation操作概览)
- [第98讲-Spark Streaming：updateStateByKey以及基于缓存的实时wordcount程序](#第98讲-spark-streamingupdatestatebykey以及基于缓存的实时wordcount程序)
- [第99讲-Spark Streaming：transform以及广告计费日志实时黑名单过滤案例实战](#第99讲-spark-streamingtransform以及广告计费日志实时黑名单过滤案例实战)
- [第100讲-Spark Streaming：window滑动窗口以及热点搜索词滑动统计案例实战](#第100讲-spark-streamingwindow滑动窗口以及热点搜索词滑动统计案例实战)
- [第101讲-Spark Streaming：DStream的output操作以及foreachRDD详解](#第101讲-spark-streamingdstream的output操作以及foreachrdd详解)
    - [1、output操作概览](#1output操作概览)
    - [2、foreachRDD详解](#2foreachrdd详解)
- [第102讲-Spark Streaming：与Spark SQL结合使用之top3热门商品实时统计案例实战](#第102讲-spark-streaming与spark-sql结合使用之top3热门商品实时统计案例实战)
- [第103讲-Spark Streaming：缓存与持久化机制](#第103讲-spark-streaming缓存与持久化机制)
- [第104讲-Spark Streaming：Checkpoint机制](#第104讲-spark-streamingcheckpoint机制)
    - [1、Checkpoint机制概述](#1checkpoint机制概述)
    - [2、何时启用Checkpoint机制？](#2何时启用checkpoint机制)
    - [3、如何启用Checkpoint机制？](#3如何启用checkpoint机制)
    - [4、为Driver失败的恢复机制重写程序（Java）](#4为driver失败的恢复机制重写程序java)
    - [5、为Driver失败的恢复机制重写程序（Scala）](#5为driver失败的恢复机制重写程序scala)
    - [6、配置spark-submit提交参数](#6配置spark-submit提交参数)
    - [7、Checkpoint的说明](#7checkpoint的说明)
- [第105讲-Spark Streaming：部署、升级和监控应用程序](#第105讲-spark-streaming部署升级和监控应用程序)
    - [1、部署应用程序](#1部署应用程序)
        - [启用预写日志机制](#启用预写日志机制)
        - [设置Receiver接收速度](#设置receiver接收速度)
    - [2、升级应用程序](#2升级应用程序)
    - [3、监控应用程序](#3监控应用程序)
- [第106讲-Spark Streaming：容错机制以及事务语义详解](#第106讲-spark-streaming容错机制以及事务语义详解)
    - [1、容错机制的背景](#1容错机制的背景)
    - [2、Spark Streaming容错语义的定义](#2spark-streaming容错语义的定义)
    - [3、Spark Streaming的基础容错语义](#3spark-streaming的基础容错语义)
    - [4、接收数据的容错语义](#4接收数据的容错语义)
    - [5、输出数据的容错语义](#5输出数据的容错语义)
    - [6、Storm的容错语义](#6storm的容错语义)
- [第107讲-Spark Streaming：架构原理深度剖析](#第107讲-spark-streaming架构原理深度剖析)
- [第108讲-Spark Streaming：StreamingContext初始化与Receiver启动原理剖析与源码分析](#第108讲-spark-streamingstreamingcontext初始化与receiver启动原理剖析与源码分析)
- [第109讲-Spark Streaming：数据接收原理剖析与源码分析](#第109讲-spark-streaming数据接收原理剖析与源码分析)
- [第110讲-Spark Streaming：数据处理原理剖析与源码分析（block与batch关系透彻解析）](#第110讲-spark-streaming数据处理原理剖析与源码分析block与batch关系透彻解析)
- [第111讲-Spark Streaming：性能调优](#第111讲-spark-streaming性能调优)
    - [1、数据接收并行度调优](#1数据接收并行度调优)
    - [2、任务启动调优](#2任务启动调优)
    - [3、数据处理并行度调优](#3数据处理并行度调优)
    - [4、数据序列化调优](#4数据序列化调优)
    - [5、batch interval调优（最重要）](#5batch-interval调优最重要)
    - [6、内存调优](#6内存调优)
- [第112讲-课程总结](#第112讲-课程总结)

<!-- /TOC -->

# 第88讲-Spark Streaming：大数据实时计算介绍

![大数据实时计算原理](../../pic/2019-06-08-23-10-29.png)

Spark Streaming，其实就是一种Spark提供的，对于大数据，进行实时计算的一种框架。它的底层，其实，也是基于我们之前讲解的Spark Core的。基本的计算模型，还是基于内存的大数据实时计算模型。而且，它的底层的组件或者叫做概念，其实还是最核心的RDD。

只不多，针对实时计算的特点，在RDD之上，进行了一层封装，叫做DStream。其实，学过了Spark SQL之后，你理解这种封装就容易了。之前学习Spark SQL是不是也是发现，它针对数据查询这种应用，提供了一种基于RDD之上的全新概念，DataFrame，但是，其底层还是基于RDD的。所以，RDD是整个Spark技术生态中的核心。要学好Spark在交互式查询、实时计算上的应用技术和框架，首先必须学好Spark核心编程，也就是Spark Core。

这节课，作为Spark Streaming的第一节课，我们先，给大家讲解一下，什么是大数据实时计算？然后下节课，再来看看Spark Streaming针对实时计算的场景，它的基本工作原理是什么？？


# 第89讲-Spark Streaming：DStream以及基本工作原理

![Spark Streaming基本工作原理](../../pic/2019-06-08-23-13-53.png)

## 1、Spark Streaming简介

Spark Streaming是Spark Core API的一种扩展，它可以用于进行大规模、高吞吐量、容错的实时数据流的处理。它支持从很多种数据源中读取数据，比如Kafka、Flume、Twitter、ZeroMQ、Kinesis或者是TCP Socket。并且能够使用类似高阶函数的复杂算法来进行数据处理，比如map、reduce、join和window。处理后的数据可以被保存到文件系统、数据库、Dashboard等存储中。

![](../../pic/2019-06-08-23-12-03.png)



## 2、Spark Streaming基本工作原理

Spark Streaming内部的基本工作原理如下：接收实时输入数据流，然后将数据拆分成多个batch，比如每收集1秒的数据封装为一个batch，然后将每个batch交给Spark的计算引擎进行处理，最后会生产出一个结果数据流，其中的数据，也是由一个一个的batch所组成的。

![](../../pic/2019-06-08-23-12-35.png)


## 3、DStream


Spark Streaming提供了一种高级的抽象，叫做DStream，英文全称为Discretized Stream，中文翻译为“离散流”，它代表了一个持续不断的数据流。DStream可以通过输入数据源来创建，比如Kafka、Flume和Kinesis；也可以通过对其他DStream应用高阶函数来创建，比如map、reduce、join、window。

DStream的内部，其实一系列持续不断产生的RDD。RDD是Spark Core的核心抽象，即，不可变的，分布式的数据集。DStream中的每个RDD都包含了一个时间段内的数据。

![](../../pic/2019-06-08-23-13-06.png)

对DStream应用的算子，比如map，其实在底层会被翻译为对DStream中每个RDD的操作。比如对一个DStream执行一个map操作，会产生一个新的DStream。但是，在底层，其实其原理为，对输入DStream中每个时间段的RDD，都应用一遍map操作，然后生成的新的RDD，即作为新的DStream中的那个时间段的一个RDD。底层的RDD的transformation操作，其实，还是由Spark Core的计算引擎来实现的。Spark Streaming对Spark Core进行了一层封装，隐藏了细节，然后对开发人员提供了方便易用的高层次的API。

![](../../pic/2019-06-08-23-13-31.png)



# 第90讲-Spark Streaming：与Storm的对比分析

![](../../pic/2019-06-08-23-16-10.png)


## 1、Spark Streaming与Storm的优劣分析

事实上，Spark Streaming绝对谈不上比Storm优秀。这两个框架在实时计算领域中，都很优秀，只是擅长的细分场景并不相同。

Spark Streaming仅仅在吞吐量上比Storm要优秀，而吞吐量这一点，也是历来挺Spark Streaming，贬Storm的人着重强调的。但是问题是，是不是在所有的实时计算场景下，都那么注重吞吐量？不尽然。因此，通过吞吐量说Spark Streaming强于Storm，不靠谱。

事实上，Storm在实时延迟度上，比Spark Streaming就好多了，前者是纯实时，后者是准实时。而且，Storm的事务机制、健壮性 / 容错性、动态调整并行度等特性，都要比Spark Streaming更加优秀。

Spark Streaming，有一点是Storm绝对比不上的，就是：它位于Spark生态技术栈中，因此Spark Streaming可以和Spark Core、Spark SQL无缝整合，也就意味着，我们可以对实时处理出来的中间数据，立即在程序中无缝进行延迟批处理、交互式查询等操作。这个特点大大增强了Spark Streaming的优势和功能。



## 2、Spark Streaming与Storm的应用场景

对于Storm来说：
1、建议在那种需要纯实时，不能忍受1秒以上延迟的场景下使用，比如实时金融系统，要求纯实时进行金融交易和分析
2、此外，如果对于实时计算的功能中，要求可靠的事务机制和可靠性机制，即数据的处理完全精准，一条也不能多，一条也不能少，也可以考虑使用Storm
3、如果还需要针对高峰低峰时间段，动态调整实时计算程序的并行度，以最大限度利用集群资源（通常是在小型公司，集群资源紧张的情况），也可以考虑用Storm
4、如果一个大数据应用系统，它就是纯粹的实时计算，不需要在中间执行SQL交互式查询、复杂的transformation算子等，那么用Storm是比较好的选择

对于Spark Streaming来说：
1、如果对上述适用于Storm的三点，一条都不满足的实时场景，即，不要求纯实时，不要求强大可靠的事务机制，不要求动态调整并行度，那么可以考虑使用Spark Streaming
2、考虑使用Spark Streaming最主要的一个因素，应该是针对整个项目进行宏观的考虑，即，如果一个项目除了实时计算之外，还包括了离线批处理、交互式查询等业务功能，而且实时计算中，可能还会牵扯到高延迟批处理、交互式查询等功能，那么就应该首选Spark生态，用Spark Core开发离线批处理，用Spark SQL开发交互式查询，用Spark Streaming开发实时计算，三者可以无缝整合，给系统提供非常高的可扩展性。




# 第91讲-Spark Streaming：实时wordcount程序开发

1、安装nc工具：yum install nc
2、开发实时wordcount程序



# 第92讲-Spark Streaming：StreamingContext详解


有两种创建StreamingContext的方式：

val conf = new SparkConf().setAppName(appName).setMaster(master);
val ssc = new StreamingContext(conf, Seconds(1));

StreamingContext，还可以使用已有的SparkContext来创建
val sc = new SparkContext(conf)
val ssc = new StreamingContext(sc, Seconds(1));

appName，是用来在Spark UI上显示的应用名称。master，是一个Spark、Mesos或者Yarn集群的URL，或者是local[*]。

batch interval可以根据你的应用程序的延迟要求以及可用的集群资源情况来设置。

一个StreamingContext定义之后，必须做以下几件事情：
1、通过创建输入DStream来创建输入数据源。
2、通过对DStream定义transformation和output算子操作，来定义实时计算逻辑。
3、调用StreamingContext的start()方法，来开始实时处理数据。
4、调用StreamingContext的awaitTermination()方法，来等待应用程序的终止。可以使用CTRL+C手动停止，或者就是让它持续不断的运行进行计算。
5、也可以通过调用StreamingContext的stop()方法，来停止应用程序。

需要注意的要点：
1、只要一个StreamingContext启动之后，就不能再往其中添加任何计算逻辑了。比如执行start()方法之后，还给某个DStream执行一个算子。
2、一个StreamingContext停止之后，是肯定不能够重启的。调用stop()之后，不能再调用start()
3、一个JVM同时只能有一个StreamingContext启动。在你的应用程序中，不能创建两个StreamingContext。
4、调用stop()方法时，会同时停止内部的SparkContext，如果不希望如此，还希望后面继续使用SparkContext创建其他类型的Context，比如SQLContext，那么就用stop(false)。
5、一个SparkContext可以创建多个StreamingContext，只要上一个先用stop(false)停止，再创建下一个即可。




# 第93讲-Spark Streaming：输入DStream和Receiver详解

输入DStream代表了来自数据源的输入数据流。在之前的wordcount例子中，lines就是一个输入DStream（JavaReceiverInputDStream），代表了从netcat（nc）服务接收到的数据流。除了文件数据流之外，所有的输入DStream都会绑定一个Receiver对象，该对象是一个关键的组件，用来从数据源接收数据，并将其存储在Spark的内存中，以供后续处理。

Spark Streaming提供了两种内置的数据源支持；1、基础数据源：StreamingContext API中直接提供了对这些数据源的支持，比如文件、socket、Akka Actor等。
2、高级数据源：诸如Kafka、Flume、Kinesis、Twitter等数据源，通过第三方工具类提供支持。这些数据源的使用，需要引用其依赖。
3、自定义数据源：我们可以自己定义数据源，来决定如何接受和存储数据。

要注意的是，如果你想要在实时计算应用中并行接收多条数据流，可以创建多个输入DStream。这样就会创建多个Receiver，从而并行地接收多个数据流。但是要注意的是，一个Spark Streaming Application的Executor，是一个长时间运行的任务，因此，它会独占分配给Spark Streaming Application的cpu core。从而只要Spark Streaming运行起来以后，这个节点上的cpu core，就没法给其他应用使用了。

使用本地模式，运行程序时，绝对不能用local或者local[1]，因为那样的话，只会给执行输入DStream的executor分配一个线程。而Spark Streaming底层的原理是，至少要有两条线程，一条线程用来分配给Receiver接收数据，一条线程用来处理接收到的数据。因此必须使用local[n]，n>=2的模式。

如果不设置Master，也就是直接将Spark Streaming应用提交到集群上运行，那么首先，必须要求集群节点上，有>1个cpu core，其次，给Spark Streaming的每个executor分配的core，必须>1，这样，才能保证分配到executor上运行的输入DStream，两条线程并行，一条运行Receiver，接收数据；一条处理数据。否则的话，只会接收数据，不会处理数据。

因此，基于此，特此声明，我们本系列课程所有的练习，都是基于local[2]的本地模式，因为我们的虚拟机上都只有一个1个cpu core。但是大家在实际企业工作中，机器肯定是不只一个cpu core的，现在都至少4核了。到时记得给每个executor的cpu core，设置为超过1个即可。


![Receiver和cpu core分配说明](../../pic/2019-06-09-09-14-51.png)


# 第94讲-Spark Streaming：输入DStream之基础数据源以及基于HDFS的实时wordcount程序

1、Socket：之前的wordcount例子，已经演示过了，StreamingContext.socketTextStream()

2、HDFS文件
基于HDFS文件的实时计算，其实就是，监控一个HDFS目录，只要其中有新文件出现，就实时处理。相当于处理实时的文件流。

streamingContext.fileStream<KeyClass, ValueClass, InputFormatClass>(dataDirectory)
streamingContext.fileStream[KeyClass, ValueClass, InputFormatClass](dataDirectory)

Spark Streaming会监视指定的HDFS目录，并且处理出现在目录中的文件。要注意的是，所有放入HDFS目录中的文件，都必须有相同的格式；必须使用移动或者重命名的方式，将文件移入目录；一旦处理之后，文件的内容即使改变，也不会再处理了；基于HDFS文件的数据源是没有Receiver的，因此不会占用一个cpu core。


# 第95讲-Spark Streaming：输入DStream之Kafka数据源实战（基于Receiver的方式）

## 1、基于Receiver的方式
这种方式使用Receiver来获取数据。Receiver是使用Kafka的高层次Consumer API来实现的。receiver从Kafka中获取的数据都是存储在Spark Executor的内存中的，然后Spark Streaming启动的job会去处理那些数据。

然而，在默认的配置下，这种方式可能会因为底层的失败而丢失数据。如果要启用高可靠机制，让数据零丢失，就必须启用Spark Streaming的预写日志机制（Write Ahead Log，WAL）。该机制会同步地将接收到的Kafka数据写入分布式文件系统（比如HDFS）上的预写日志中。所以，即使底层节点出现了失败，也可以使用预写日志中的数据进行恢复。



## 2、如何进行Kafka数据源连接

1、在maven添加依赖
groupId = org.apache.spark
artifactId = spark-streaming-kafka_2.10
version = 1.5.1

2、使用第三方工具类创建输入DStream
 JavaPairReceiverInputDStream<String, String> kafkaStream = 
     KafkaUtils.createStream(streamingContext,
     [ZK quorum], [consumer group id], [per-topic number of Kafka partitions to consume]);

## 3、需要注意的要点
1、Kafka中的topic的partition，与Spark中的RDD的partition是没有关系的。所以，在KafkaUtils.createStream()中，提高partition的数量，只会增加一个Receiver中，读取partition的线程的数量。不会增加Spark处理数据的并行度。

2、可以创建多个Kafka输入DStream，使用不同的consumer group和topic，来通过多个receiver并行接收数据。

3、如果基于容错的文件系统，比如HDFS，启用了预写日志机制，接收到的数据都会被复制一份到预写日志中。因此，在KafkaUtils.createStream()中，设置的持久化级别是StorageLevel.MEMORY_AND_DISK_SER。


## 4、Kafka命令
bin/kafka-topics.sh --zookeeper 192.168.1.107:2181,192.168.1.108:2181,192.168.1.109:2181 --topic TestTopic --replication-factor 1 --partitions 1 --create

bin/kafka-console-producer.sh --broker-list 192.168.1.107:9092,192.168.1.108:9092,192.168.1.109:9092 --topic TestTopic

192.168.1.191:2181,192.168.1.192:2181,192.168.1.193:2181






# 第96讲-Spark Streaming：输入DStream之Kafka数据源实战（基于Direct的方式）

## 1、基于Direct的方式
这种新的不基于Receiver的直接方式，是在Spark 1.3中引入的，从而能够确保更加健壮的机制。替代掉使用Receiver来接收数据后，这种方式会周期性地查询Kafka，来获得每个topic+partition的最新的offset，从而定义每个batch的offset的范围。当处理数据的job启动时，就会使用Kafka的简单consumer api来获取Kafka指定offset范围的数据。

这种方式有如下优点：
1、简化并行读取：如果要读取多个partition，不需要创建多个输入DStream然后对它们进行union操作。Spark会创建跟Kafka partition一样多的RDD partition，并且会并行从Kafka中读取数据。所以在Kafka partition和RDD partition之间，有一个一对一的映射关系。

2、高性能：如果要保证零数据丢失，在基于receiver的方式中，需要开启WAL机制。这种方式其实效率低下，因为数据实际上被复制了两份，Kafka自己本身就有高可靠的机制，会对数据复制一份，而这里又会复制一份到WAL中。而基于direct的方式，不依赖Receiver，不需要开启WAL机制，只要Kafka中作了数据的复制，那么就可以通过Kafka的副本进行恢复。

3、一次且仅一次的事务机制：
    基于receiver的方式，是使用Kafka的高阶API来在ZooKeeper中保存消费过的offset的。这是消费Kafka数据的传统方式。这种方式配合着WAL机制可以保证数据零丢失的高可靠性，但是却无法保证数据被处理一次且仅一次，可能会处理两次。因为Spark和ZooKeeper之间可能是不同步的。
    基于direct的方式，使用kafka的简单api，Spark Streaming自己就负责追踪消费的offset，并保存在checkpoint中。Spark自己一定是同步的，因此可以保证数据是消费一次且仅消费一次。

 JavaPairReceiverInputDStream<String, String> directKafkaStream = 
     KafkaUtils.createDirectStream(streamingContext,
         [key class], [value class], [key decoder class], [value decoder class],
         [map of Kafka parameters], [set of topics to consume]);




## 2、Kafka命令

bin/kafka-topics.sh --zookeeper 192.168.1.107:2181,192.168.1.108:2181,192.168.1.109:2181 --topic TestTopic --replication-factor 1 --partitions 1 --create

bin/kafka-console-producer.sh --broker-list 192.168.1.107:9092,192.168.1.108:9092,192.168.1.109:9092 --topic TestTopic

192.168.1.191:2181,192.168.1.192:2181,192.168.1.193:2181

metadata.broker.list




# 第97讲-Spark Streaming：DStream的transformation操作概览

![](../../pic/2019-06-09-09-19-32.png)

![](../../pic/2019-06-09-09-19-44.png)


# 第98讲-Spark Streaming：updateStateByKey以及基于缓存的实时wordcount程序

updateStateByKey操作，可以让我们为每个key维护一份state，并持续不断的更新该state。

1、首先，要定义一个state，可以是任意的数据类型；

2、其次，要定义state更新函数——指定一个函数如何使用之前的state和新值来更新state。

对于每个batch，Spark都会为每个之前已经存在的key去应用一次state更新函数，无论这个key在batch中是否有新的数据。如果state更新函数返回none，那么key对应的state就会被删除。

当然，对于每个新出现的key，也会执行state更新函数。

注意，updateStateByKey操作，要求必须开启Checkpoint机制。

案例：基于缓存的实时wordcount程序（在实际业务场景中，这个是非常有用的）




# 第99讲-Spark Streaming：transform以及广告计费日志实时黑名单过滤案例实战

transform操作，应用在DStream上时，可以用于执行任意的RDD到RDD的转换操作。它可以用于实现，DStream API中所没有提供的操作。比如说，DStream API中，并没有提供将一个DStream中的每个batch，与一个特定的RDD进行join的操作。但是我们自己就可以使用transform操作来实现该功能。

DStream.join()，只能join其他DStream。在DStream每个batch的RDD计算出来之后，会去跟其他DStream的RDD进行join。

案例：广告计费日志实时黑名单过滤


# 第100讲-Spark Streaming：window滑动窗口以及热点搜索词滑动统计案例实战

Spark Streaming提供了滑动窗口操作的支持，从而让我们可以对一个滑动窗口内的数据执行计算操作。每次掉落在窗口内的RDD的数据，会被聚合起来执行计算操作，然后生成的RDD，会作为window DStream的一个RDD。比如下图中，就是对每三秒钟的数据执行一次滑动窗口计算，这3秒内的3个RDD会被聚合起来进行处理，然后过了两秒钟，又会对最近三秒内的数据执行滑动窗口计算。所以每个滑动窗口操作，都必须指定两个参数，窗口长度以及滑动间隔，而且这两个参数值都必须是batch间隔的整数倍。（Spark Streaming对滑动窗口的支持，是比Storm更加完善和强大的）

![](../../pic/2019-06-09-09-21-26.png)

![](../../pic/2019-06-09-09-21-37.png)

案例：热点搜索词滑动统计，每隔10秒钟，统计最近60秒钟的搜索词的搜索频次，并打印出排名最靠前的3个搜索词以及出现次数




# 第101讲-Spark Streaming：DStream的output操作以及foreachRDD详解

## 1、output操作概览

![](../../pic/2019-06-09-09-22-52.png)

DStream中的所有计算，都是由output操作触发的，比如print()。如果没有任何output操作，那么，压根儿就不会执行定义的计算逻辑。

此外，即使你使用了foreachRDD output操作，也必须在里面对RDD执行action操作，才能触发对每一个batch的计算逻辑。否则，光有foreachRDD output操作，在里面没有对RDD执行action操作，也不会触发任何逻辑。



## 2、foreachRDD详解

通常在foreachRDD中，都会创建一个Connection，比如JDBC Connection，然后通过Connection将数据写入外部存储。

误区一：在RDD的foreach操作外部，创建Connection

这种方式是错误的，因为它会导致Connection对象被序列化后传输到每个Task中。而这种Connection对象，实际上一般是不支持序列化的，也就无法被传输。

dstream.foreachRDD { rdd =>
  val connection = createNewConnection() 
  rdd.foreach { record => connection.send(record)
  }
}

误区二：在RDD的foreach操作内部，创建Connection

这种方式是可以的，但是效率低下。因为它会导致对于RDD中的每一条数据，都创建一个Connection对象。而通常来说，Connection的创建，是很消耗性能的。

dstream.foreachRDD { rdd =>
  rdd.foreach { record =>
    val connection = createNewConnection()
    connection.send(record)
    connection.close()
  }
}

合理方式一：使用RDD的foreachPartition操作，并且在该操作内部，创建Connection对象，这样就相当于是，为RDD的每个partition创建一个Connection对象，节省资源的多了。

dstream.foreachRDD { rdd =>
  rdd.foreachPartition { partitionOfRecords =>
    val connection = createNewConnection()
    partitionOfRecords.foreach(record => connection.send(record))
    connection.close()
  }
}

合理方式二：自己手动封装一个静态连接池，使用RDD的foreachPartition操作，并且在该操作内部，从静态连接池中，通过静态方法，获取到一个连接，使用之后再还回去。这样的话，甚至在多个RDD的partition之间，也可以复用连接了。而且可以让连接池采取懒创建的策略，并且空闲一段时间后，将其释放掉。

dstream.foreachRDD { rdd =>
  rdd.foreachPartition { partitionOfRecords =>
    val connection = ConnectionPool.getConnection()
    partitionOfRecords.foreach(record => connection.send(record))
    ConnectionPool.returnConnection(connection)  
  }
}


案例：改写UpdateStateByKeyWordCount，将每次统计出来的全局的单词计数，写入一份，到MySQL数据库中。

create table wordcount (
  id integer auto_increment primary key,
  updated_time timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  word varchar(255),
  count integer
);



# 第102讲-Spark Streaming：与Spark SQL结合使用之top3热门商品实时统计案例实战

Spark Streaming最强大的地方在于，可以与Spark Core、Spark SQL整合使用，之前已经通过transform、foreachRDD等算子看到，如何将DStream中的RDD使用Spark Core执行批处理操作。现在就来看看，如何将DStream中的RDD与Spark SQL结合起来使用。

案例：每隔10秒，统计最近60秒的，每个种类的每个商品的点击次数，然后统计出每个种类top3热门的商品。


# 第103讲-Spark Streaming：缓存与持久化机制

与RDD类似，Spark Streaming也可以让开发人员手动控制，将数据流中的数据持久化到内存中。对DStream调用persist()方法，就可以让Spark Streaming自动将该数据流中的所有产生的RDD，都持久化到内存中。如果要对一个DStream多次执行操作，那么，对DStream持久化是非常有用的。因为多次操作，可以共享使用内存中的一份缓存数据。

对于基于窗口的操作，比如reduceByWindow、reduceByKeyAndWindow，以及基于状态的操作，比如updateStateByKey，默认就隐式开启了持久化机制。即Spark Streaming默认就会将上述操作产生的Dstream中的数据，缓存到内存中，不需要开发人员手动调用persist()方法。

对于通过网络接收数据的输入流，比如socket、Kafka、Flume等，默认的持久化级别，是将数据复制一份，以便于容错。相当于是，用的是类似MEMORY_ONLY_SER_2。

与RDD不同的是，默认的持久化级别，统一都是要序列化的。


# 第104讲-Spark Streaming：Checkpoint机制

## 1、Checkpoint机制概述

每一个Spark Streaming应用，正常来说，都是要7 * 24小时运转的，这就是实时计算程序的特点。因为要持续不断的对数据进行计算。因此，对实时计算应用的要求，应该是必须要能够对与应用程序逻辑无关的失败，进行容错。

如果要实现这个目标，Spark Streaming程序就必须将足够的信息checkpoint到容错的存储系统上，从而让它能够从失败中进行恢复。有两种数据需要被进行checkpoint：

1、元数据checkpoint——将定义了流式计算逻辑的信息，保存到容错的存储系统上，比如HDFS。当运行Spark Streaming应用程序的Driver进程所在节点失败时，该信息可以用于进行恢复。元数据信息包括了：
  1.1 配置信息——创建Spark Streaming应用程序的配置信息，比如SparkConf中的信息。
  1.2 DStream的操作信息——定义了Spark Stream应用程序的计算逻辑的DStream操作信息。
  1.3 未处理的batch信息——那些job正在排队，还没处理的batch信息。

2、数据checkpoint——将实时计算过程中产生的RDD的数据保存到可靠的存储系统中。

对于一些将多个batch的数据进行聚合的，有状态的transformation操作，这是非常有用的。在这种transformation操作中，生成的RDD是依赖于之前的batch的RDD的，这会导致随着时间的推移，RDD的依赖链条变得越来越长。

要避免由于依赖链条越来越长，导致的一起变得越来越长的失败恢复时间，有状态的transformation操作执行过程中间产生的RDD，会定期地被checkpoint到可靠的存储系统上，比如HDFS。从而削减RDD的依赖链条，进而缩短失败恢复时，RDD的恢复时间。

一句话概括，元数据checkpoint主要是为了从driver失败中进行恢复；而RDD checkpoint主要是为了，使用到有状态的transformation操作时，能够在其生产出的数据丢失时，进行快速的失败恢复。




## 2、何时启用Checkpoint机制？

1、使用了有状态的transformation操作——比如updateStateByKey，或者reduceByKeyAndWindow操作，被使用了，那么checkpoint目录要求是必须提供的，也就是必须开启checkpoint机制，从而进行周期性的RDD checkpoint。

2、要保证可以从Driver失败中进行恢复——元数据checkpoint需要启用，来进行这种情况的恢复。

要注意的是，并不是说，所有的Spark Streaming应用程序，都要启用checkpoint机制，如果即不强制要求从Driver失败中自动进行恢复，又没使用有状态的transformation操作，那么就不需要启用checkpoint。事实上，这么做反而是有助于提升性能的。


## 3、如何启用Checkpoint机制？
1、对于有状态的transformation操作，启用checkpoint机制，定期将其生产的RDD数据checkpoint，是比较简单的。

可以通过配置一个容错的、可靠的文件系统（比如HDFS）的目录，来启用checkpoint机制，checkpoint数据就会写入该目录。使用StreamingContext的checkpoint()方法即可。然后，你就可以放心使用有状态的transformation操作了。

2、如果为了要从Driver失败中进行恢复，那么启用checkpoint机制，是比较复杂的。需要改写Spark Streaming应用程序。

当应用程序第一次启动的时候，需要创建一个新的StreamingContext，并且调用其start()方法，进行启动。当Driver从失败中恢复过来时，需要从checkpoint目录中记录的元数据中，恢复出来一个StreamingContext。


## 4、为Driver失败的恢复机制重写程序（Java）

JavaStreamingContextFactory contextFactory = new JavaStreamingContextFactory() {
  @Override 
  public JavaStreamingContext create() {
    JavaStreamingContext jssc = new JavaStreamingContext(...);  
    JavaDStream<String> lines = jssc.socketTextStream(...);     
    jssc.checkpoint(checkpointDirectory);                       
    return jssc;
  }
};

JavaStreamingContext context = JavaStreamingContext.getOrCreate(checkpointDirectory, contextFactory);
context.start();
context.awaitTermination();


## 5、为Driver失败的恢复机制重写程序（Scala）

def functionToCreateContext(): StreamingContext = {
    val ssc = new StreamingContext(...)  
    val lines = ssc.socketTextStream(...) 
    ssc.checkpoint(checkpointDirectory)   
    ssc
}

val context = StreamingContext.getOrCreate(checkpointDirectory, functionToCreateContext _)
context.start()
context.awaitTermination()


## 6、配置spark-submit提交参数
按照上述方法，进行Spark Streaming应用程序的重写后，当第一次运行程序时，如果发现checkpoint目录不存在，那么就使用定义的函数来第一次创建一个StreamingContext，并将其元数据写入checkpoint目录；当从Driver失败中恢复过来时，发现checkpoint目录已经存在了，那么会使用该目录中的元数据创建一个StreamingContext。

但是上面的重写应用程序的过程，只是实现Driver失败自动恢复的第一步。第二步是，必须确保Driver可以在失败时，自动被重启。

要能够自动从Driver失败中恢复过来，运行Spark Streaming应用程序的集群，就必须监控Driver运行的过程，并且在它失败时将它重启。对于Spark自身的standalone模式，需要进行一些配置去supervise driver，在它失败时将其重启。

首先，要在spark-submit中，添加--deploy-mode参数，默认其值为client，即在提交应用的机器上启动Driver；但是，要能够自动重启Driver，就必须将其值设置为cluster；此外，需要添加--supervise参数。

使用上述第二步骤提交应用之后，就可以让driver在失败时自动被重启，并且通过checkpoint目录的元数据恢复StreamingContext。


## 7、Checkpoint的说明
将RDD checkpoint到可靠的存储系统上，会耗费很多性能。当RDD被checkpoint时，会导致这些batch的处理时间增加。因此，checkpoint的间隔，需要谨慎的设置。对于那些间隔很多的batch，比如1秒，如果还要执行checkpoint操作，则会大幅度削减吞吐量。而另外一方面，如果checkpoint操作执行的太不频繁，那就会导致RDD的lineage变长，又会有失败恢复时间过长的风险。

对于那些要求checkpoint的有状态的transformation操作，默认的checkpoint间隔通常是batch间隔的数倍，至少是10秒。使用DStream的checkpoint()方法，可以设置这个DStream的checkpoint的间隔时长。通常来说，将checkpoint间隔设置为窗口操作的滑动间隔的5~10倍，是个不错的选择。




# 第105讲-Spark Streaming：部署、升级和监控应用程序

## 1、部署应用程序
1、有一个集群资源管理器，比如standalone模式下的Spark集群，Yarn模式下的Yarn集群等。
2、打包应用程序为一个jar包，课程中一直都有演示。
3、为executor配置充足的内存，因为Receiver接受到的数据，是要存储在Executor的内存中的，所以Executor必须配置足够的内存来保存接受到的数据。要注意的是，如果你要执行窗口长度为10分钟的窗口操作，那么Executor的内存资源就必须足够保存10分钟内的数据，因此内存的资源要求是取决于你执行的操作的。
4、配置checkpoint，如果你的应用程序要求checkpoint操作，那么就必须配置一个Hadoop兼容的文件系统（比如HDFS）的目录作为checkpoint目录.
5、配置driver的自动恢复，如果要让driver能够在失败时自动恢复，之前已经讲过，一方面，要重写driver程序，一方面，要在spark-submit中添加参数。


### 启用预写日志机制

预写日志机制，简写为WAL，全称为Write Ahead Log。从Spark 1.2版本开始，就引入了基于容错的文件系统的WAL机制。如果启用该机制，Receiver接收到的所有数据都会被写入配置的checkpoint目录中的预写日志。这种机制可以让driver在恢复的时候，避免数据丢失，并且可以确保整个实时计算过程中，零数据丢失。

要配置该机制，首先要调用StreamingContext的checkpoint()方法设置一个checkpoint目录。然后需要将spark.streaming.receiver.writeAheadLog.enable参数设置为true。

然而，这种极强的可靠性机制，会导致Receiver的吞吐量大幅度下降，因为单位时间内，有相当一部分时间需要将数据写入预写日志。如果又希望开启预写日志机制，确保数据零损失，又不希望影响系统的吞吐量，那么可以创建多个输入DStream，启动多个Rceiver。

此外，在启用了预写日志机制之后，推荐将复制持久化机制禁用掉，因为所有数据已经保存在容错的文件系统中了，不需要在用复制机制进行持久化，保存一份副本了。只要将输入DStream的持久化机制设置一下即可，persist(StorageLevel.MEMORY_AND_DISK_SER)。（之前讲过，默认是基于复制的持久化策略，_2后缀）


### 设置Receiver接收速度

如果集群资源有限，并没有大到，足以让应用程序一接收到数据就立即处理它，Receiver可以被设置一个最大接收限速，以每秒接收多少条单位来限速。

spark.streaming.receiver.maxRate和spark.streaming.kafka.maxRatePerPartition参数可以用来设置，前者设置普通Receiver，后者是Kafka Direct方式。

Spark 1.5中，对于Kafka Direct方式，引入了backpressure机制，从而不需要设置Receiver的限速，Spark可以自动估计Receiver最合理的接收速度，并根据情况动态调整。只要将spark.streaming.backpressure.enabled设置为true即可。

在企业实际应用场景中，通常是推荐用Kafka Direct方式的，特别是现在随着Spark版本的提升，越来越完善这个Kafka Direct机制。优点：1、不用receiver，不会独占集群的一个cpu core；2、有backpressure自动调节接收速率的机制；3、....。

## 2、升级应用程序
由于Spark Streaming应用程序都是7 * 24小时运行的。因此如果需要对正在运行的应用程序，进行代码的升级，那么有两种方式可以实现：

1、升级后的Spark应用程序直接启动，先与旧的Spark应用程序并行执行。当确保新的应用程序启动没问题之后，就可以将旧的应用程序给停掉。但是要注意的是，这种方式只适用于，能够允许多个客户端读取各自独立的数据，也就是读取相同的数据。

2、小心地关闭已经在运行的应用程序，使用StreamingContext的stop()方法，可以确保接收到的数据都处理完之后，才停止。然后将升级后的程序部署上去，启动。这样，就可以确保中间没有数据丢失和未处理。因为新的应用程序会从老的应用程序未消费到的地方，继续消费。但是注意，这种方式必须是支持数据缓存的数据源才可以，比如Kafka、Flume等。如果数据源不支持数据缓存，那么会导致数据丢失。

注意：配置了driver自动恢复机制时，如果想要根据旧的应用程序的checkpoint信息，启动新的应用程序，是不可行的。需要让新的应用程序针对新的checkpoint目录启动，或者删除之前的checkpoint目录。


## 3、监控应用程序

当Spark Streaming应用启动时，Spark Web UI会显示一个独立的streaming tab，会显示Receiver的信息，比如是否活跃，接收到了多少数据，是否有异常等；还会显示完成的batch的信息，batch的处理时间、队列延迟等。这些信息可以用于监控spark streaming应用的进度。

Spark UI中，以下两个统计指标格外重要：
1、处理时间——每个batch的数据的处理耗时
2、调度延迟——一个batch在队列中阻塞住，等待上一个batch完成处理的时间

如果batch的处理时间，比batch的间隔要长的话，而且调度延迟时间持续增长，应用程序不足以使用当前设定的速率来处理接收到的数据，此时，可以考虑增加batch的间隔时间。




# 第106讲-Spark Streaming：容错机制以及事务语义详解


![RDD的基本容错原理](../../pic/2019-06-09-09-37-11.png)

![可靠Receiver的原理](../../pic/2019-06-09-09-37-35.png)


## 1、容错机制的背景
要理解Spark Streaming提供的容错机制，先回忆一下Spark RDD的基础容错语义：

1、RDD，Ressilient Distributed Dataset，是不可变的、确定的、可重新计算的、分布式的数据集。每个RDD都会记住确定好的计算操作的血缘关系，（val lines = sc.textFile(hdfs file); val words = lines.flatMap(); val pairs = words.map(); val wordCounts = pairs.reduceByKey()）这些操作应用在一个容错的数据集上来创建RDD。
2、如果因为某个Worker节点的失败（挂掉、进程终止、进程内部报错），导致RDD的某个partition数据丢失了，那么那个partition可以通过对原始的容错数据集应用操作血缘，来重新计算出来。
3、所有的RDD transformation操作都是确定的，最后一个被转换出来的RDD的数据，一定是不会因为Spark集群的失败而丢失的。

Spark操作的通常是容错文件系统中的数据，比如HDFS。因此，所有通过容错数据生成的RDD也是容错的。然而，对于Spark Streaming来说，这却行不通，因为在大多数情况下，数据都是通过网络接收的（除了使用fileStream数据源）。要让Spark Streaming程序中，所有生成的RDD，都达到与普通Spark程序的RDD，相同的容错性，接收到的数据必须被复制到多个Worker节点上的Executor内存中，默认的复制因子是2。

基于上述理论，在出现失败的事件时，有两种数据需要被恢复：

1、数据接收到了，并且已经复制过——这种数据在一个Worker节点挂掉时，是可以继续存活的，因为在其他Worker节点上，还有它的一份副本。
2、数据接收到了，但是正在缓存中，等待复制的——因为还没有复制该数据，因此恢复它的唯一办法就是重新从数据源获取一份。

此外，还有两种失败是我们需要考虑的：

1、Worker节点的失败——任何一个运行了Executor的Worker节点的挂掉，都会导致该节点上所有在内存中的数据都丢失。如果有Receiver运行在该Worker节点上的Executor中，那么缓存的，待复制的数据，都会丢失。
2、Driver节点的失败——如果运行Spark Streaming应用程序的Driver节点失败了，那么显然SparkContext会丢失，那么该Application的所有Executor的数据都会丢失。


## 2、Spark Streaming容错语义的定义
流式计算系统的容错语义，通常是以一条记录能够被处理多少次来衡量的。有三种类型的语义可以提供：

1、最多一次：每条记录可能会被处理一次，或者根本就不会被处理。可能有数据丢失。
2、至少一次：每条记录会被处理一次或多次，这种语义比最多一次要更强，因为它确保零数据丢失。但是可能会导致记录被重复处理几次。
3、一次且仅一次：每条记录只会被处理一次——没有数据会丢失，并且没有数据会处理多次。这是最强的一种容错语义。


## 3、Spark Streaming的基础容错语义
在Spark Streaming中，处理数据都有三个步骤：

1、接收数据：使用Receiver或其他方式接收数据。
2、计算数据：使用DStream的transformation操作对数据进行计算和处理。
3、推送数据：最后计算出来的数据会被推送到外部系统，比如文件系统、数据库等。

如果应用程序要求必须有一次且仅一次的语义，那么上述三个步骤都必须提供一次且仅一次的语义。每条数据都得保证，只能接收一次、只能计算一次、只能推送一次。Spark Streaming中实心这些语义的步骤如下：

1、接收数据：不同的数据源提供不同的语义保障。
2、计算数据：所有接收到的数据一定只会被计算一次，这是基于RDD的基础语义所保障的。即使有失败，只要接收到的数据还是可访问的，最后一个计算出来的数据一定是相同的。
3、推送数据：output操作默认能确保至少一次的语义，因为它依赖于output操作的类型，以及底层系统的语义支持（比如是否有事务支持等），但是用户可以实现它们自己的事务机制来确保一次且仅一次的语义。


## 4、接收数据的容错语义

1、基于文件的数据源
如果所有的输入数据都在一个容错的文件系统中，比如HDFS，Spark Streaming一定可以从失败进行恢复，并且处理所有数据。这就提供了一次且仅一次的语义，意味着所有的数据只会处理一次。

2、基于Receiver的数据源

对于基于Receiver的数据源，容错语义依赖于失败的场景和Receiver类型。

可靠的Receiver：这种Receiver会在接收到了数据，并且将数据复制之后，对数据源执行确认操作。如果Receiver在数据接收和复制完成之前，就失败了，那么数据源对于缓存的数据会接收不到确认，此时，当Receiver重启之后，数据源会重新发送数据，没有数据会丢失。

不可靠的Receiver：这种Receiver不会发送确认操作，因此当Worker或者Driver节点失败的时候，可能会导致数据丢失。

不同的Receiver，提供了不同的语义。如果Worker节点失败了，那么使用的是可靠的Receiver的话，没有数据会丢失。使用的是不可靠的Receiver的话，接收到，但是还没复制的数据，可能会丢失。如果Driver节点失败的话，所有过去接收到的，和复制过缓存在内存中的数据，全部会丢失。

要避免这种过去接收的所有数据都丢失的问题，Spark从1.2版本开始，引入了预写日志机制，可以将Receiver接收到的数据保存到容错存储中。如果使用可靠的Receiver，并且还开启了预写日志机制，那么可以保证数据零丢失。这种情况下，会提供至少一次的保障。（Kafka是可以实现可靠Receiver的）

![](../../pic/2019-06-09-09-35-09.png)

从Spark 1.3版本开始，引入了新的Kafka Direct API，可以保证，所有从Kafka接收到的数据，都是一次且仅一次。基于该语义保障，如果自己再实现一次且仅一次语义的output操作，那么就可以获得整个Spark Streaming应用程序的一次且仅一次的语义。


## 5、输出数据的容错语义
output操作，比如foreachRDD，可以提供至少一次的语义。那意味着，当Worker节点失败时，转换后的数据可能会被写入外部系统一次或多次。对于写入文件系统来说，这还是可以接收的，因为会覆盖数据。但是要真正获得一次且仅一次的语义，有两个方法：

1、幂等更新：多次写操作，都是写相同的数据，例如saveAs系列方法，总是写入相同的数据。
2、事务更新：所有的操作都应该做成事务的，从而让写入操作执行一次且仅一次。给每个batch的数据都赋予一个唯一的标识，然后更新的时候判定，如果数据库中还没有该唯一标识，那么就更新，如果有唯一标识，那么就不更新。

dstream.foreachRDD { (rdd, time) =>
  rdd.foreachPartition { partitionIterator =>
    val partitionId = TaskContext.get.partitionId()
    val uniqueId = generateUniqueId(time.milliseconds, partitionId)
    // partitionId和foreachRDD传入的时间，可以构成一个唯一的标识
  }
}


## 6、Storm的容错语义
ok，就输出数据这一点来看，我真的不理解，网上，有些同学，特别的挺Spark Streaming，踩Storm。问题是，他们真的对这两种实时计算技术都很精通吗？都对它们所有的高级特性掌握的非常彻底吗？

Storm首先，它可以实现消息的高可靠性，就是说，它有一个机制，叫做Acker机制，可以保证，如果消息处理失败，那么就重新发送。保证了，至少一次的容错语义。但是光靠这个，还是不行，数据可能会重复。

Storm提供了非常非常完善的事务机制，可以实现一次且仅一次的事务机制。事务Topology、透明的事务Topology、非透明的事务Topology，可以应用各种各样的情况。对实现一次且仅一次的这种语义的支持，做的非常非常好。用事务机制，可以获得它内部提供的一个唯一的id，然后基于这个id，就可以实现，output操作，输出，推送数据的时候，先判断，该数据是否更新过，如果没有的话，就更新；如果更新过，就不要重复更新了。

所以，至少，在容错 / 事务机制方面，我觉得Spark Streaming还有很大的空间可以发展。特别是对于output操作的一次且仅一次的语义支持！





# 第107讲-Spark Streaming：架构原理深度剖析

![](../../pic/2019-06-09-09-38-13.png)

# 第108讲-Spark Streaming：StreamingContext初始化与Receiver启动原理剖析与源码分析

# 第109讲-Spark Streaming：数据接收原理剖析与源码分析

![](../../pic/2019-06-09-09-38-56.png)

# 第110讲-Spark Streaming：数据处理原理剖析与源码分析（block与batch关系透彻解析）

![](../../pic/2019-06-09-09-39-22.png)

# 第111讲-Spark Streaming：性能调优

## 1、数据接收并行度调优

通过网络接收数据时（比如Kafka、Flume），会将数据反序列化，并存储在Spark的内存中。如果数据接收称为系统的瓶颈，那么可以考虑并行化数据接收。每一个输入DStream都会在某个Worker的Executor上启动一个Receiver，该Receiver接收一个数据流。因此可以通过创建多个输入DStream，并且配置它们接收数据源不同的分区数据，达到接收多个数据流的效果。比如说，一个接收两个Kafka Topic的输入DStream，可以被拆分为两个输入DStream，每个分别接收一个topic的数据。这样就会创建两个Receiver，从而并行地接收数据，进而提升吞吐量。多个DStream可以使用union算子进行聚合，从而形成一个DStream。然后后续的transformation算子操作都针对该一个聚合后的DStream即可。

int numStreams = 5;
List<JavaPairDStream<String, String>> kafkaStreams = new ArrayList<JavaPairDStream<String, String>>(numStreams);
for (int i = 0; i < numStreams; i++) {
  kafkaStreams.add(KafkaUtils.createStream(...));
}
JavaPairDStream<String, String> unifiedStream = streamingContext.union(kafkaStreams.get(0), kafkaStreams.subList(1, kafkaStreams.size()));
unifiedStream.print();


数据接收并行度调优，除了创建更多输入DStream和Receiver以外，还可以考虑调节block interval。通过参数，spark.streaming.blockInterval，可以设置block interval，默认是200ms。对于大多数Receiver来说，在将接收到的数据保存到Spark的BlockManager之前，都会将数据切分为一个一个的block。而每个batch中的block数量，则决定了该batch对应的RDD的partition的数量，以及针对该RDD执行transformation操作时，创建的task的数量。每个batch对应的task数量是大约估计的，即batch interval / block interval。

例如说，batch interval为2s，block interval为200ms，会创建10个task。如果你认为每个batch的task数量太少，即低于每台机器的cpu core数量，那么就说明batch的task数量是不够的，因为所有的cpu资源无法完全被利用起来。要为batch增加block的数量，那么就减小block interval。然而，推荐的block interval最小值是50ms，如果低于这个数值，那么大量task的启动时间，可能会变成一个性能开销点。


除了上述说的两个提升数据接收并行度的方式，还有一种方法，就是显式地对输入数据流进行重分区。使用inputStream.repartition(<number of partitions>)即可。这样就可以将接收到的batch，分布到指定数量的机器上，然后再进行进一步的操作。


## 2、任务启动调优

如果每秒钟启动的task过于多，比如每秒钟启动50个，那么发送这些task去Worker节点上的Executor的性能开销，会比较大，而且此时基本就很难达到毫秒级的延迟了。使用下述操作可以减少这方面的性能开销：

1、Task序列化：使用Kryo序列化机制来序列化task，可以减小task的大小，从而减少发送这些task到各个Worker节点上的Executor的时间。
2、执行模式：在Standalone模式下运行Spark，可以达到更少的task启动时间。

上述方式，也许可以将每个batch的处理时间减少100毫秒。从而从秒级降到毫秒级。


## 3、数据处理并行度调优

如果在计算的任何stage中使用的并行task的数量没有足够多，那么集群资源是无法被充分利用的。举例来说，对于分布式的reduce操作，比如reduceByKey和reduceByKeyAndWindow，默认的并行task的数量是由spark.default.parallelism参数决定的。你可以在reduceByKey等操作中，传入第二个参数，手动指定该操作的并行度，也可以调节全局的spark.default.parallelism参数。


## 4、数据序列化调优

数据序列化造成的系统开销可以由序列化格式的优化来减小。在流式计算的场景下，有两种类型的数据需要序列化。

1、输入数据：默认情况下，接收到的输入数据，是存储在Executor的内存中的，使用的持久化级别是StorageLevel.MEMORY_AND_DISK_SER_2。这意味着，数据被序列化为字节从而减小GC开销，并且会复制以进行executor失败的容错。因此，数据首先会存储在内存中，然后在内存不足时会溢写到磁盘上，从而为流式计算来保存所有需要的数据。这里的序列化有明显的性能开销——Receiver必须反序列化从网络接收到的数据，然后再使用Spark的序列化格式序列化数据。

2、流式计算操作生成的持久化RDD：流式计算操作生成的持久化RDD，可能会持久化到内存中。例如，窗口操作默认就会将数据持久化在内存中，因为这些数据后面可能会在多个窗口中被使用，并被处理多次。然而，不像Spark Core的默认持久化级别，StorageLevel.MEMORY_ONLY，流式计算操作生成的RDD的默认持久化级别是StorageLevel.MEMORY_ONLY_SER ，默认就会减小GC开销。


在上述的场景中，使用Kryo序列化类库可以减小CPU和内存的性能开销。使用Kryo时，一定要考虑注册自定义的类，并且禁用对应引用的tracking（spark.kryo.referenceTracking）。

在一些特殊的场景中，比如需要为流式应用保持的数据总量并不是很多，也许可以将数据以非序列化的方式进行持久化，从而减少序列化和反序列化的CPU开销，而且又不会有太昂贵的GC开销。举例来说，如果你数秒的batch interval，并且没有使用window操作，那么你可以考虑通过显式地设置持久化级别，来禁止持久化时对数据进行序列化。这样就可以减少用于序列化和反序列化的CPU性能开销，并且不用承担太多的GC开销。


## 5、batch interval调优（最重要）

如果想让一个运行在集群上的Spark Streaming应用程序可以稳定，它就必须尽可能快地处理接收到的数据。换句话说，batch应该在生成之后，就尽可能快地处理掉。对于一个应用来说，这个是不是一个问题，可以通过观察Spark UI上的batch处理时间来定。batch处理时间必须小于batch interval时间。

基于流式计算的本质，batch interval对于，在固定集群资源条件下，应用能保持的数据接收速率，会有巨大的影响。例如，在WordCount例子中，对于一个特定的数据接收速率，应用业务可以保证每2秒打印一次单词计数，而不是每500ms。因此batch interval需要被设置得，让预期的数据接收速率可以在生产环境中保持住。

为你的应用计算正确的batch大小的比较好的方法，是在一个很保守的batch interval，比如5~10s，以很慢的数据接收速率进行测试。要检查应用是否跟得上这个数据速率，可以检查每个batch的处理时间的延迟，如果处理时间与batch interval基本吻合，那么应用就是稳定的。否则，如果batch调度的延迟持续增长，那么就意味应用无法跟得上这个速率，也就是不稳定的。因此你要想有一个稳定的配置，可以尝试提升数据处理的速度，或者增加batch interval。记住，由于临时性的数据增长导致的暂时的延迟增长，可以合理的，只要延迟情况可以在短时间内恢复即可。


## 6、内存调优

优化Spark应用的内存使用和GC行为，在Spark Core的调优中，已经讲过了。这里讲一下与Spark Streaming应用相关的调优参数。

Spark Streaming应用需要的集群内存资源，是由使用的transformation操作类型决定的。举例来说，如果想要使用一个窗口长度为10分钟的window操作，那么集群就必须有足够的内存来保存10分钟内的数据。如果想要使用updateStateByKey来维护许多key的state，那么你的内存资源就必须足够大。反过来说，如果想要做一个简单的map-filter-store操作，那么需要使用的内存就很少。

通常来说，通过Receiver接收到的数据，会使用StorageLevel.MEMORY_AND_DISK_SER_2持久化级别来进行存储，因此无法保存在内存中的数据会溢写到磁盘上。而溢写到磁盘上，是会降低应用的性能的。因此，通常是建议为应用提供它需要的足够的内存资源。建议在一个小规模的场景下测试内存的使用量，并进行评估。

内存调优的另外一个方面是垃圾回收。对于流式应用来说，如果要获得低延迟，肯定不想要有因为JVM垃圾回收导致的长时间延迟。有很多参数可以帮助降低内存使用和GC开销：

1、DStream的持久化：正如在“数据序列化调优”一节中提到的，输入数据和某些操作生产的中间RDD，默认持久化时都会序列化为字节。与非序列化的方式相比，这会降低内存和GC开销。使用Kryo序列化机制可以进一步减少内存使用和GC开销。进一步降低内存使用率，可以对数据进行压缩，由spark.rdd.compress参数控制（默认false）。

2、清理旧数据：默认情况下，所有输入数据和通过DStream transformation操作生成的持久化RDD，会自动被清理。Spark Streaming会决定何时清理这些数据，取决于transformation操作类型。例如，你在使用窗口长度为10分钟内的window操作，Spark会保持10分钟以内的数据，时间过了以后就会清理旧数据。但是在某些特殊场景下，比如Spark SQL和Spark Streaming整合使用时，在异步开启的线程中，使用Spark SQL针对batch RDD进行执行查询。那么就需要让Spark保存更长时间的数据，直到Spark SQL查询结束。可以使用streamingContext.remember()方法来实现。

3、CMS垃圾回收器：使用并行的mark-sweep垃圾回收机制，被推荐使用，用来保持GC低开销。虽然并行的GC会降低吞吐量，但是还是建议使用它，来减少batch的处理时间（降低处理过程中的gc开销）。如果要使用，那么要在driver端和executor端都开启。在spark-submit中使用--driver-java-options设置；使用spark.executor.extraJavaOptions参数设置。-XX:+UseConcMarkSweepGC。



# 第112讲-课程总结


1、Scala编程详解：熟练进行Scala编程、看懂Spark源码
2、课程环境搭建：从零开始搭建Hadoop、Spark、Hive、ZooKeeper、Kafka集群
3、Spark核心编程：熟练掌握Spark Core编程，掌握高级编程技术（基于排序的wordcount、二次排序、分组取topn），开发各种简单或复杂的离线批处理程序，案例实战
4、Spark内核源码深度剖析：深入理解Spark底层架构和原理，精读Spark内核的核心源码，能够对线上的故障报错进行排查，甚至通过源码追踪问题原因
5、Spark性能优化：掌握Spark常用性能优化技术，能够对Spark离线批处理程序进行性能优化
6、Spark SQL：熟练使用Spark SQL开发交互式查询程序，掌握最新特性（内置函数、开窗函数、UDAF函数），理解架构原理和性能优化技术，案例实战
7、Spark Streaming：熟练使用Spark Streaming开发各种实时计算应用，掌握高级技术（Kafka Direct API、updateStateByKey、transform、window、foreachRDD性能优化、与Spark SQL整合使用、持久化、checkpoint、容错和事务），理解工作原理，阅读核心源码，掌握性能优化技术，案例实战


掌握了上述内容之后，希望大家能够对重点的知识，反复看2~3遍视频。对于课程中所有代码编写，所有的案例实战，全部自己手敲一遍代码。希望大家对于源码分析相关，可以自己下载源码，自己根据课程讲解思路，看几遍源码。

客观的说，在100%掌握上述所有内容之后，在国内，完全可以达到Spark高级开发工程师的水平。去国内任何公司面试Spark相关内容，都可以hold住。并且真正进入公司，用Spark做项目时，用Spark根据业务需求开发应用和系统，都没问题。对于普通的线上报错故障，以及性能调优，都没有问题。

对于老师来说，讲课只能做到这里了，后面的学习都靠同学们了。后面，如果有机会，会尽快出一套Spark的大数据项目实战课程。让大家除了精通Spark技术之外，还能够积累Spark的实际企业项目实战经验。进一步提升含金量。

谢谢大家的支持。课程内容过多，难免有疏漏和错误。还希望大家多包涵，并一起交流和讨论。
