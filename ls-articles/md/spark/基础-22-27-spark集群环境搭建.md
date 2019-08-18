
Hadoop2.7+spark-2.1.0-bin-hadoop2.7+scala-2.12.8+jdk1.8


# 1、搭建Hadoop的分布式集群

- [Hadoop2.7集群环境搭建](https://blog.csdn.net/l1394049664/article/details/82505748)

# 2、安装Scala【版本2.12.8】

- [scala包下载](https://www.scala-lang.org/download/)


解压 tar -zxvf scala-2.12.8.tgz

vim /etc/profile

export SCALA_HOME=/export/scala/scala-2.12.8
export PATH=$PATH:$SCALA_HOME/bin


:wq #保存并退出

执行配置文件

source /etc/profile

验证scala

scala -version

# 3、安装spark

- [spark包下载](https://archive.apache.org/dist/spark/spark-2.1.0/)

1.下载文件

wget -O "spark-2.1.0-bin-hadoop2.7.tgz" "http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.7.tgz"


2.解压并移动至相应的文件夹

tar -xvf spark-2.1.0-bin-hadoop2.7.tgz

mv spark-2.1.0-bin-hadoop2.7 /export

3.修改相应的配置文件

（1）/etc/profie

```
#Spark enviroment
export SPARK_HOME=/opt/spark-2.1.0-bin-hadoop2.7/
export PATH="$SPARK_HOME/bin:$PATH"
```

（2）$SPARK_HOME/conf/spark-env.sh

cp spark-env.sh.template spark-env.sh

```
#配置内容如下：
export SCALA_HOME=/export/scala/scala-2.12.8
export JAVA_HOME=/export/java/jdk1.8/jdk1.8.0_201
export SPARK_MASTER_IP=node1
export SPARK_WORKER_MEMORY=1g
# 注意下面的配置不用配置为Hadoop的安装目录而是配置文件的目录，否则会报错io文件找不到
export HADOOP_CONF_DIR=/export/hadoop-2.7.3/etc/hadoop

```


（3）$SPARK_HOME/conf/slaves

cp slaves.template slaves

配置内容如下

```
master
worker1
worker2
```

(4) WorkerN节点：

将配置好的spark文件复制到workerN节点

scp spark-2.1.0-bin-hadoop2.7 root@workerN:/export

修改/etc/profile，增加spark相关的配置，如MASTER节点一样

(5)、启动

/export/spark-2.1.0-bin-hadoop2.7/sbin/start-all.sh

/export/spark-2.1.0-bin-hadoop2.7/sbin/stop-all.sh


（6）、访问spark可视化页面

http://192.168.254.101:8080


（7）、执行spark自带的例子

> local模式

```
./bin/spark-submit  --class  org.apache.spark.examples.SparkPi  --master local   examples/jars/spark-examples_2.11-2.1.0.jar
```


> standalone模式

```
./bin/spark-submit  --class  org.apache.spark.examples.SparkPi  --master spark://node1:7077   examples/jars/spark-examples_2.11-2.1.0.jar
```

备注：运行的结果会出现在spark的可视化界面

> yarn模式


```
./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn --deploy-mode cluster --driver-memory 1g --executor-memory 1g  --executor-cores 1 --num-executors 3 /export/spark-2.1.0-bin-hadoop2.7/examples/jars/spark-examples_2.11-2.1.0.jar 10

```



备注：运行的结果不会出现在spark的可视化界面，而是会出现Hadoop的MapReduce任务列表中。




# 问题


> 问题1：Spark/Yarn: File does not exist on HDFS

错误日志

Diagnostics: File file:/tmp/spark-040dd48f-1880-4ee8-b072-9cd02790a302/__spark_libs__7158246219479355925.zip does not exist
java.io.FileNotFoundException: File file:/tmp/spark-040dd48f-1880-4ee8-b072-9cd02790a302/__spark_libs__7158246219479355925.zip does not exist

原因:spark指向Hadoop的配置文件路径不对

- [解决方案见](https://stackoverflow.com/questions/44231261/spark-yarn-file-does-not-exist-on-hdfs)




# 参考

- [Hadoop2.7.3+Spark2.1.0 完全分布式环境 搭建全过程](https://www.cnblogs.com/purstar/p/6293605.html)

- [Linux安装Spark集群(CentOS7+Spark2.1.1+Hadoop2.8.0)](https://blog.csdn.net/pucao_cug/article/details/72353701)

- [各模式下运行spark自带实例SparkPi](https://blog.csdn.net/yt_sports/article/details/50424522?utm_source=copy)