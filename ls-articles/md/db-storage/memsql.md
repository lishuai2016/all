# memsql简介
[memsql文档](https://docs.memsql.com)

MemSQL 数据库号称是世界上最快的分布式内存数据库（The World's Fastest In-Memory Database）！
它是由Eric Frenkiel（前Facebook员工）和Nikita Shamgunov（前微软SQL Server高级工程师）创建的一款基于内存的分布式关系数据库，
它通过将数据存储在内存中，并将SQL语句预编译为C++而获得极速的执行效率。它兼容MySQL，且速度要比MySQL快30倍，
能实现每秒150万次事务。

# 1、表类型

它包含reference tables（参照表）, sharded tables（分布表）, and temporary tables.这三种表结构。默认创建的是分布表

1、参照表（reference table)
数据分布在主aggregator和每个leaf节点。每个节点的数据都是完整的（没有分区）。参照表同过复制从主aggregator向每个leaf节点同步数据。
另外参照表的写只能在主aggregator进行。

2、分布表 （sharded table)
数据通过hash分片存储在每个leaf节点，每个leaf节点只有部分数据。默认通过主键进行分区。

一般可以把小数据量的维度表设置为参照表，每个节点拥有参照表的全部数据，和大数据量的的分布表进行关联查询；



# 2、存储格式

memsql支持两种数据存储形式，基于内存的行存储和基于磁盘的列存储，创建表的时候默认是基于内存的行存储，在创建的时候可以指定分区列，
一般选择区分度比较大的列，防止数据倾斜，在指定分区列时，默认的是随机策略，可能会存在数据倾斜问题。
存在主键时，默认使用主键进行分区，如果主键和分区列同时存在的时候，需要主键包含的列覆盖掉分区包含的列，便于数据的高效查找。
（https://docs.memsql.com/concepts/v6.7/rowstore/）

CREATE TABLE products (
     ProductId INT,
     Color VARCHAR(10),
     Price INT,
     dt DATETIME,
     KEY (Price),
     KEY (Color),
     PRIMARY KEY(ProductId),
     SHARD KEY (ProductId)
);

并且，行存储支持数据的持久化，基于内存定期快照和日志的形式保存内存中的数据，在服务器重启后用于恢复数据。



## 1、分区设置

可以在配置文件中设置，或者的创建数据库的时候指定分区数。

Data is sharded across the leaves into partitions. Each partition is a database on a leaf (named <dbname>_N) with a slice of each table’s data.
 By default, MemSQL will create one partition per CPU core on the leaves for maximum parallelism. 
 This number is configurable cluster-wide with the default-partitions-per-leaf variable, or as an optional parameter to CREATE DATABASE .

数据被分散到各个分区中存储的，在叶子节点每个分区是一个单独的数据库，存储的是表的一部分数据。

[查询或者插入数据的过程](https://docs.memsql.com/concepts/v6.7/distributed-sql/)

default_partitions_per_leaf                  | 16 

【分区默认是8的倍数】

【查询的时候，有筛选条件匹配的进行分区查询，然后才根据索引进行二级查询，不匹配分区的时候，进行全区扫描查询】

如果把两个join关联查询的表的分区字段设置为一样，这样在进行关联查询的时候，通过hash数据都是在同一个分区中，可以提高查询的效率。



# 3、几个概念：
1、Aggregators(汇聚器) MemSQL集群的一种节点，为访问MemSQL集群的网关，一个集群中可以有多个汇聚器，汇聚器主要负责向叶子节点发送DML请求、汇聚操作结果并返回给客户端

2、Master Aggregator(主汇聚器) 是一种特殊的汇聚器，除了一般汇聚器的功能，还负责监控集群、失效切换及DDL语句的操作。每一个MemSQL的集群只能有一个主汇聚器。当主汇聚器失效时，MemSQL的集群和DDL语句操作将被挂起，而一般的DML语句仍可以通过其他的汇聚器进行操作。如果主汇聚器的程序文件没有丢失，那么只要重启一下主汇聚器即可，主汇聚器将会自动重连。否则的话，可以在一个子汇聚器上执行命令来将子汇聚器提升为主汇聚器。

3、Leaf Node(叶子节点) 在MemSQL集群中，叶子节点的作用是存储和计算。负责在集群中存储数据的切片。为了优化性能，MemSQL会围绕叶子节点自动分布数据到分区。每一个叶子节点由若干个分区组成。每一个分区其实就是一个database。

4、Partitions(分区)  一个叶子节点由多个分区组成。每一个分区其实就是一个database。比如说，如有你创建了一个database名为test，那么当你在一个叶子节点执行SHOW DATABASES 时，会看到数据库名为类似test_5，表明这是分区5。通过SHOW DATABASES EXTENDED命令输出的State列，可以查看分区是Master或者slave分区或者正处于其他状态的分区。Master分区是数据库真正执行操作的分区，而slave分区则是通过网络和sql操作将Master上的数据复制到slave上。另外，slave分区是只读的。

 

# 4、设计原则

[参考](https://docs.memsql.com/?utm_source=ops&utm_medium=link&utm_campaign=ref)

MemSQL分布式系统的设计遵循如下的原则：

1、性能优先 　　
MemSQL是世界上最快的single-box数据库，分布式部署在多台服务器上时更是能让性能有线性的提升。MemSQL集群能够处理每秒数十亿行数据，而硬件只需要普通的商用硬件即可。

2、将集群分为汇聚层和叶子层同时尽可能多的将工作交由叶子层处理 　　
这允许你在线增加额外的叶子节点到集群来提升集群容量和查询性能

3、无单点失败风险　　
通过运行冗余的集群，可以确保每一个数据分区都有一个热备份。当叶子节点失效时，MemSQL将自动切换到salve分区。汇聚层同样也有类似的技术来规避单节点风险。

4、强大但又简单的集群管理   
分布式系统提供了REBALANCE PARTITIONS功能来恢复分区数据

5、没有隐式的数据移动  


# 5、memsql命令

- 1、SHOW AGGREGATORS;   查看聚合节点信息

- 2、SHOW LEAVES;   查看叶子节点信息

- 3、SHOW PARTITIONS on cost;   查看数据库上的所有的分区

- 4、SHOW DATABASES EXTENDED; 
通过SHOW DATABASES EXTENDED命令输出的State列，可以查看分区是Master或者slave分区或者正处于其他状态的分区。
Master分区是数据库真正执行操作的分区，而slave分区则是通过网络和sql操作将Master上的数据复制到slave上。
另外，slave分区是只读的。每一个叶子节点由若干个分区组成。每一个分区其实就是一个database。

- 5、SHOW REPLICATION STATUS \G； 查看数据库的主从节点列表

- 6、show table status \G   查看表的状态



# 6、文档翻译

[参考](https://docs.memsql.com/introduction/latest/how-memsql-works/)
MemSQL是一个可以大规模处理事务和实时数据分析关系数据库。使用标准的SQL驱动和句法，适用于广泛的驱动和应用生态。


1、How MemSQL Works

MemSQL可以在云实例和标准工业级硬件上进行大规模的水平扩展，同时可以保证高吞吐量。
MemSQL具有很好的兼容性，能很好的和当下数据处理生态平台很好的整合，使得你可以很方便的把它整合到你的系统中去。
它的特点是内存行存储和磁盘列存储，两者都支持高并发操作和数据分析。
另外的一个特点是数据提取，MemSQL Pipelines以高吞吐量将大量数据以“精确一次”的语义传输到数据库中

1.1、数据的提取
MemSQL可以加载各种来源连续和离散的数据。比较常见的是文件、kafka集群、S3, HDFS，或者其他数据库中的数据。
MemSQL使用并行加载来提取数据流以最大化吞吐量。


MemSQL Pipelines 是一个内建非常好用的功能，用来提取、转化外部的各种数据，它在数据需要实时提取


1.2、分布式架构
MemSQL的分布式架构被设计为直接的、简单的并且快速的。这里概述了MemSQL集群，包括各式组件的交互。
同时介绍了当你执行一个查询或者管理操作的时候，MemSQL环境发生了什么。

 
# 参考
https://www.cnblogs.com/justfortaste/p/3532158.html

https://searchdatabase.techtarget.com.cn/7-20914/

http://mini.eastday.com/mobile/180320163047722.html

https://blog.csdn.net/luckyzhou_/article/details/71124738

https://www.cnblogs.com/caozhangni/p/5051920.html
