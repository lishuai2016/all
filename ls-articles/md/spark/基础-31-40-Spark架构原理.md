
<!-- TOC -->

- [第31讲-Spark核心编程：Spark架构原理](#第31讲-spark核心编程spark架构原理)
- [第32讲-Spark核心编程：创建RDD（集合、本地文件、HDFS文件）](#第32讲-spark核心编程创建rdd集合本地文件hdfs文件)
    - [1、 创建RDD方式有哪些](#1-创建rdd方式有哪些)
    - [2、并行化集合创建RDD](#2并行化集合创建rdd)
    - [3、 使用本地文件和HDFS创建RDD](#3-使用本地文件和hdfs创建rdd)
- [第33讲-Spark核心编程：操作RDD（transformation和action案例实战）](#第33讲-spark核心编程操作rddtransformation和action案例实战)
    - [1、 transformation和action介绍](#1-transformation和action介绍)
    - [2、案例：统计文件字数](#2案例统计文件字数)
    - [3、案例：统计文件每行出现的次数](#3案例统计文件每行出现的次数)
    - [4、常用transformation和action介绍](#4常用transformation和action介绍)
- [第34讲-Spark核心编程：transformation操作开发实战](#第34讲-spark核心编程transformation操作开发实战)
- [第35讲-Spark核心编程：action操作开发实战](#第35讲-spark核心编程action操作开发实战)
- [第36讲-Spark核心编程：RDD持久化详解](#第36讲-spark核心编程rdd持久化详解)
    - [1、RDD持久化原理](#1rdd持久化原理)
    - [2、 RDD持久化实战](#2-rdd持久化实战)
    - [3、RDD持久化策略](#3rdd持久化策略)
    - [4、如何选择RDD持久化策略？](#4如何选择rdd持久化策略)
- [第37讲-Spark核心编程：共享变量（Broadcast Variable和Accumulator）](#第37讲-spark核心编程共享变量broadcast-variable和accumulator)
    - [1、 共享变量工作原理](#1-共享变量工作原理)
    - [2、 Broadcast Variable](#2-broadcast-variable)
    - [3、 Accumulator](#3-accumulator)
- [第38讲-Spark核心编程：高级编程之基于排序机制的wordcount程序](#第38讲-spark核心编程高级编程之基于排序机制的wordcount程序)
- [第39讲-Spark核心编程：高级编程之二次排序](#第39讲-spark核心编程高级编程之二次排序)
- [第40讲-Spark核心编程：高级编程之topn](#第40讲-spark核心编程高级编程之topn)

<!-- /TOC -->

# 第31讲-Spark核心编程：Spark架构原理


![](../../pic/2019-06-08-12-10-53.png)

![](../../pic/2019-06-08-14-28-19.png)


spark集群这个有主节点和从节点，主节点就是master，从节点就是worker。

- master负责资源调度，监控等；

- worker：一个是存储RDD的partition，还有就是启动其他进程和线程对RDD的partition进行计算。（worker进程启动executor进行，executor启动多个task线程，执行我们编写的map、flatmap、reduce等）

任务的执行过程按照上面图中的顺时针方向。


# 第32讲-Spark核心编程：创建RDD（集合、本地文件、HDFS文件）

[创建RDD其实就是数据的输入]

## 1、 创建RDD方式有哪些

进行Spark核心编程时，首先要做的第一件事，就是创建一个初始的RDD。该RDD中，通常就代表和包含了Spark应用程序的输入源数据。然后在创建了初始的RDD之后，才可以通过Spark Core提供的transformation算子，对该RDD进行转换，来获取其他的RDD。

Spark Core提供了三种创建RDD的方式，包括：使用程序中的集合创建RDD；使用本地文件创建RDD；使用HDFS文件创建RDD。

个人经验认为：

- 1、使用程序中的集合创建RDD，主要用于进行测试，可以在实际部署到集群运行之前，自己使用集合构造测试数据，来测试后面的spark应用的流程。

- 2、使用本地文件创建RDD，主要用于临时性地处理一些存储了大量数据的文件。

- 3、使用HDFS文件创建RDD，应该是最常用的生产环境处理方式，主要可以针对HDFS上存储的大数据，进行离线批处理操作。


备注：使用spark-core来替代MapReduce

## 2、并行化集合创建RDD

如果要通过并行化集合来创建RDD，需要针对程序中的集合，调用SparkContext的parallelize()方法。Spark会将集合中的数据拷贝到集群上去，形成一个分布式的数据集合，也就是一个RDD。相当于是，集合中的部分数据会到一个节点上，而另一部分数据会到其他节点上。然后就可以用并行的方式来操作这个分布式数据集合，即RDD。

// 案例：1到10累加求和

val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

val rdd = sc.parallelize(arr)

val sum = rdd.reduce(_ + _)


调用parallelize()时，有一个重要的参数可以指定，就是要将集合切分成多少个partition。Spark会为每一个partition运行一个task来进行处理。Spark官方的建议是，为集群中的每个CPU创建2~4个partition。Spark默认会根据集群的情况来设置partition的数量。但是也可以在调用parallelize()方法时，传入第二个参数，来设置RDD的partition数量。比如parallelize(arr, 10)

> 案例代码

```java
package ls.spark.zhonghuashishan.core;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

/**
 * 并行化集合创建RDD
 * 案例：累加1到10
 * @author Administrator
 *
 */
public class ParallelizeCollection {

	public static void main(String[] args) {
		// 创建SparkConf
		SparkConf conf = new SparkConf()
				.setAppName("ParallelizeCollection")
				.setMaster("local");

		// 创建JavaSparkContext
		JavaSparkContext sc = new JavaSparkContext(conf);

		// 要通过并行化集合的方式创建RDD，那么就调用SparkContext以及其子类，的parallelize()方法
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		JavaRDD<Integer> numberRDD = sc.parallelize(numbers);

		// 执行reduce算子操作
		// 相当于，先进行1 + 2 = 3；然后再用3 + 3 = 6；然后再用6 + 4 = 10。。。以此类推
		int sum = numberRDD.reduce(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer num1, Integer num2) throws Exception {
				return num1 + num2;
			}

		});

		// 输出累加的和
		System.out.println("1到10的累加和：" + sum);

		// 关闭JavaSparkContext
		sc.close();
	}

}


```


## 3、 使用本地文件和HDFS创建RDD



Spark是支持使用任何Hadoop支持的存储系统上的文件创建RDD的，比如说HDFS、Cassandra、HBase以及本地文件。通过调用SparkContext的textFile()方法，可以针对本地文件或HDFS文件创建RDD。

有几个事项是需要注意的：

- 1、如果是针对本地文件的话，如果是在windows上本地测试，windows上有一份文件即可；如果是在spark集群上针对linux本地文件，那么需要将文件拷贝到所有worker节点上。

- 2、Spark的textFile()方法支持针对目录、压缩文件以及通配符进行RDD创建。

- 3、Spark默认会为hdfs文件的每一个block创建一个partition，但是也可以通过textFile()的第二个参数手动设置分区数量，只能比block数量多，不能比block数量少。

// 案例：文件字数统计
val rdd = sc.textFile("data.txt")
val wordCount = rdd.map(line => line.length).reduce(_ + _)



Spark的textFile()除了可以针对上述几种普通的文件创建RDD之外，还有一些特列的方法来创建RDD：

- 1、SparkContext.wholeTextFiles()方法，可以针对一个目录中的大量小文件，返回<filename, fileContent>组成的pair，作为一个PairRDD，而不是普通的RDD。普通的textFile()返回的RDD中，每个元素就是文件中的一行文本。

- 2、SparkContext.sequenceFile[K, V]()方法，可以针对SequenceFile创建RDD，K和V泛型类型就是SequenceFile的key和value的类型。K和V要求必须是Hadoop的序列化类型，比如IntWritable、Text等。

- 3、SparkContext.hadoopRDD()方法，对于Hadoop的自定义输入类型，可以创建RDD。该方法接收JobConf、InputFormatClass、Key和Value的Class。

- 4、SparkContext.objectFile()方法，可以针对之前调用RDD.saveAsObjectFile()创建的对象序列化的文件，反序列化文件中的数据，并创建一个RDD。

> 案例代码

>> 本地文件

```java
package ls.spark.zhonghuashishan.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

/**
 * 使用本地文件创建RDD
 * 案例：统计文本文件字数（字符的个数）
 * @author Administrator
 *
 */
public class LocalFile {

	public static void main(String[] args) {
		// 创建SparkConf
		SparkConf conf = new SparkConf()
				.setAppName("LocalFile")
				.setMaster("local");
		// 创建JavaSparkContext
		JavaSparkContext sc = new JavaSparkContext(conf);

		// 使用SparkContext以及其子类的textFile()方法，针对本地文件创建RDD
		//JavaRDD<String> lines = sc.textFile("C://Users//Administrator//Desktop//spark.txt");
		JavaRDD<String> lines = sc.textFile("spark.txt");

		// 统计文本文件内的字数
		JavaRDD<Integer> lineLength = lines.map(new Function<String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(String v1) throws Exception {  //每一行的字数
				return v1.length();
			}

		});

		int count = lineLength.reduce(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}

		});

		System.out.println("文件总字数是：" + count);

		// 关闭JavaSparkContext
		sc.close();
	}

}


```

>> hdfs文件

```java
package ls.spark.zhonghuashishan.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

/**
 * 使用HDFS文件创建RDD
 * 案例：统计文本文件字数
 * @author Administrator
 *
 */
public class HDFSFile {

	public static void main(String[] args) {
		// 创建SparkConf
		// 修改：去除setMaster()设置，修改setAppName()
		SparkConf conf = new SparkConf()
				.setAppName("HDFSFile");
		// 创建JavaSparkContext
		JavaSparkContext sc = new JavaSparkContext(conf);

		// 使用SparkContext以及其子类的textFile()方法，针对HDFS文件创建RDD
		// 只要把textFile()内的路径修改为hdfs文件路径即可
		JavaRDD<String> lines = sc.textFile("hdfs://spark1:9000/spark.txt");

		// 统计文本文件内的字数
		JavaRDD<Integer> lineLength = lines.map(new Function<String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(String v1) throws Exception {
				return v1.length();
			}

		});

		int count = lineLength.reduce(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}

		});

		System.out.println("文件总字数是：" + count);

		// 关闭JavaSparkContext
		sc.close();
	}

}

```




# 第33讲-Spark核心编程：操作RDD（transformation和action案例实战）

> 数据

```
hello you
hello me
hello world
hello you
hello you
```


![](../../pic/2019-06-08-18-07-52.png)


## 1、 transformation和action介绍



Spark支持两种RDD操作：transformation和action。transformation操作会针对已有的RDD创建一个新的RDD；而action则主要是对RDD进行最后的操作，比如遍历、reduce、保存到文件等，并可以返回结果给Driver程序。

例如，map就是一种transformation操作，它用于将已有RDD的每个元素传入一个自定义的函数，并获取一个新的元素，然后将所有的新元素组成一个新的RDD。而reduce就是一种action操作，它用于对RDD中的所有元素进行聚合操作，并获取一个最终的结果，然后返回给Driver程序。

transformation的特点就是lazy特性。lazy特性指的是，如果一个spark应用中只定义了transformation操作，那么即使你执行该应用，这些操作也不会执行。也就是说，transformation是不会触发spark程序的执行的，它们只是记录了对RDD所做的操作，但是不会自发的执行。只有当transformation之后，接着执行了一个action操作，那么所有的transformation才会执行。Spark通过这种lazy特性，来进行底层的spark应用执行的优化，避免产生过多中间结果。

action操作执行，会触发一个spark job的运行，从而触发这个action之前所有的transformation的执行。这是action的特性。



## 2、案例：统计文件字数



这里通过一个之前学习过的案例，统计文件字数，来讲解transformation和action。

// 这里通过textFile()方法，针对外部文件创建了一个RDD，lines，但是实际上，程序执行到这里为止，spark.txt文件的数据是不会加载到内存中的。lines，只是代表了一个指向spark.txt文件的引用。
val lines = sc.textFile("spark.txt")

// 这里对lines RDD进行了map算子，获取了一个转换后的lineLengths RDD。但是这里连数据都没有，当然也不会做任何操作。lineLengths RDD也只是一个概念上的东西而已。
val lineLengths = lines.map(line => line.length)

// 之列，执行了一个action操作，reduce。此时就会触发之前所有transformation操作的执行，Spark会将操作拆分成多个task到多个机器上并行执行，每个task会在本地执行map操作，并且进行本地的reduce聚合。最后会进行一个全局的reduce聚合，然后将结果返回给Driver程序。
val totalLength = lineLengths.reduce(_ + _)


## 3、案例：统计文件每行出现的次数

Spark有些特殊的算子，也就是特殊的transformation操作。比如groupByKey、sortByKey、reduceByKey等，其实只是针对特殊的RDD的。即包含key-value对的RDD。而这种RDD中的元素，实际上是scala中的一种类型，即Tuple2，也就是包含两个值的Tuple。

在scala中，需要手动导入Spark的相关隐式转换，import org.apache.spark.SparkContext._。然后，对应包含Tuple2的RDD，会自动隐式转换为PairRDDFunction，并提供reduceByKey等方法。

val lines = sc.textFile("hello.txt")
val linePairs = lines.map(line => (line, 1))
val lineCounts = linePairs.reduceByKey(_ + _)
lineCounts.foreach(lineCount => println(lineCount._1 + " appears " + llineCount._2 + " times."))


## 4、常用transformation和action介绍

![](../../pic/2019-06-08-18-11-24.png)

![](../../pic/2019-06-08-18-11-13.png)




# 第34讲-Spark核心编程：transformation操作开发实战

- 1、map：将集合中每个元素乘以2
- 2、filter：过滤出集合中的偶数
- 3、flatMap：将行拆分为单词
- 4、groupByKey：将每个班级的成绩进行分组
- 5、reduceByKey：统计每个班级的总分
- 6、sortByKey：将学生分数进行排序
- 7、join：打印每个学生的成绩
- 8、cogroup：打印每个学生的成绩


# 第35讲-Spark核心编程：action操作开发实战

- 1、reduce：
- 2、collect：
- 3、count：
- 4、take：
- 5、saveAsTextFile：
- 6、countByKey：
- 7、foreach：



# 第36讲-Spark核心编程：RDD持久化详解

> RDD持久化的工作原理

![](../../pic/2019-06-08-18-23-08.png)

> 不使用RDD持久化的问题的原理

![](../../pic/2019-06-08-18-23-28.png)


## 1、RDD持久化原理

Spark非常重要的一个功能特性就是可以将RDD持久化在内存中。当对RDD执行持久化操作时，每个节点都会将自己操作的RDD的partition持久化到内存中，并且在之后对该RDD的反复使用中，直接使用内存缓存的partition。这样的话，对于针对一个RDD反复执行多个操作的场景，就只要对RDD计算一次即可，后面直接使用该RDD，而不需要反复计算多次该RDD。

巧妙使用RDD持久化，甚至在某些场景下，可以将spark应用程序的性能提升10倍。对于迭代式算法和快速交互式应用来说，RDD持久化，是非常重要的。

要持久化一个RDD，只要调用其cache()或者persist()方法即可。在该RDD第一次被计算出来时，就会直接缓存在每个节点中。而且Spark的持久化机制还是自动容错的，如果持久化的RDD的任何partition丢失了，那么Spark会自动通过其源RDD，使用transformation操作重新计算该partition。

cache()和persist()的区别在于，cache()是persist()的一种简化方式，cache()的底层就是调用的persist()的无参版本，同时就是调用persist(MEMORY_ONLY)，将数据持久化到内存中。如果需要从内存中清楚缓存，那么可以使用unpersist()方法。

Spark自己也会在shuffle操作时，进行数据的持久化，比如写入磁盘，主要是为了在节点失败时，避免需要重新计算整个过程。


## 2、 RDD持久化实战
实际编码体验RDD持久化的使用，以及其效果。


## 3、RDD持久化策略
RDD持久化是可以手动选择不同的策略的。比如可以将RDD持久化在内存中、持久化到磁盘上、使用序列化的方式持久化，多持久化的数据进行多路复用。只要在调用persist()时传入对应的StorageLevel即可。

![](../../pic/2019-06-08-18-21-14.png)

![](../../pic/2019-06-08-18-21-46.png)


## 4、如何选择RDD持久化策略？

Spark提供的多种持久化级别，主要是为了在CPU和内存消耗之间进行取舍。下面是一些通用的持久化级别的选择建议：

- 1、优先使用MEMORY_ONLY，如果可以缓存所有数据的话，那么就使用这种策略。因为纯内存速度最快，而且没有序列化，不需要消耗CPU进行反序列化操作。

- 2、如果MEMORY_ONLY策略，无法存储的下所有数据的话，那么使用MEMORY_ONLY_SER，将数据进行序列化进行存储，纯内存操作还是非常快，只是要消耗CPU进行反序列化。

- 3、如果需要进行快速的失败恢复，那么就选择带后缀为_2的策略，进行数据的备份，这样在失败时，就不需要重新计算了。

- 4、能不使用DISK相关的策略，就不用使用，有的时候，从磁盘读取数据，还不如重新计算一次。










# 第37讲-Spark核心编程：共享变量（Broadcast Variable和Accumulator）

> 共享变量的工作原理

![](../../pic/2019-06-08-18-24-24.png)

## 1、 共享变量工作原理

Spark一个非常重要的特性就是共享变量。

默认情况下，如果在一个算子的函数中使用到了某个外部的变量，那么这个变量的值会被拷贝到每个task中。此时每个task只能操作自己的那份变量副本。如果多个task想要共享某个变量，那么这种方式是做不到的。

Spark为此提供了两种共享变量，一种是Broadcast Variable（广播变量），另一种是Accumulator（累加变量）。Broadcast Variable会将使用到的变量，仅仅为每个节点拷贝一份，更大的用处是优化性能，减少网络传输以及内存消耗。Accumulator则可以让多个task共同操作一份变量，主要可以进行累加操作



## 2、 Broadcast Variable

Spark提供的Broadcast Variable，是只读的。并且在每个节点上只会有一份副本，而不会为每个task都拷贝一份副本。因此其最大作用，就是减少变量到各个节点的网络传输消耗，以及在各个节点上的内存消耗。此外，spark自己内部也使用了高效的广播算法来减少网络消耗。

可以通过调用SparkContext的broadcast()方法，来针对某个变量创建广播变量。然后在算子的函数内，使用到广播变量时，每个节点只会拷贝一份副本了。每个节点可以使用广播变量的value()方法获取值。记住，广播变量，是只读的。

val factor = 3
val factorBroadcast = sc.broadcast(factor)

val arr = Array(1, 2, 3, 4, 5)
val rdd = sc.parallelize(arr)
val multipleRdd = rdd.map(num => num * factorBroadcast.value())

multipleRdd.foreach(num => println(num))


## 3、 Accumulator

Spark提供的Accumulator，主要用于多个节点对一个变量进行共享性的操作。Accumulator只提供了累加的功能。但是确给我们提供了多个task对一个变量并行操作的功能。但是task只能对Accumulator进行累加操作，不能读取它的值。只有Driver程序可以读取Accumulator的值。

val sumAccumulator = sc.accumulator(0)

val arr = Array(1, 2, 3, 4, 5)
val rdd = sc.parallelize(arr)
rdd.foreach(num => sumAccumulator += num)

println(sumAccumulator.value)





# 第38讲-Spark核心编程：高级编程之基于排序机制的wordcount程序

> 案例需求

- 1、对文本文件内的每个单词都统计出其出现的次数。
- 2、按照每个单词出现次数的数量，降序排序。



# 第39讲-Spark核心编程：高级编程之二次排序

> 案例需求

- 1、按照文件中的第一列排序。
- 2、如果第一列相同，则按照第二列排序。


# 第40讲-Spark核心编程：高级编程之topn

> 案例需求

- 1、对文本文件内的数字，取最大的前3个。
- 2、对每个班级内的学生成绩，取出前3名。（分组取topn）
- 3、课后作用：用Scala来实现分组取topn。


