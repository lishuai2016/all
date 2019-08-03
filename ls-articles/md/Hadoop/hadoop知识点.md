---
title: Hadoop知识点整理
categories: 
- hadoop
tags:
---

#Hadoop知识点整理





hadoop开发常见问题


资料
http://www.raincent.com/content-85-8162-2.html

hadoop版本问题
当前Hadoop版本比较混乱，让很多用户不知所措。实际上，当前Hadoop只有两个版本：Hadoop 1.0和Hadoop 2.0，其中，Hadoop 1.0由一个分布式文件系统HDFS和一个离线计算框架MapReduce组成，而Hadoop 2.0则包含一个支持NameNode横向扩展的HDFS，一个资源管理系统YARN和一个运行在YARN上的离线计算框架MapReduce。相比于Hadoop 1.0，Hadoop 2.0功能更加强大，且具有更好的扩展性、性能，并支持多种计算框架。




hadoop的集群是基于master/slave模式，namenode和jobtracker属于master，datanode和tasktracker属于slave，master只有一个，而slave有多个

SecondaryNameNode内存需求和NameNode在一个数量级上，所以通常secondary NameNode（运行在单独的物理机器上）和NameNode运行在不同的机器上。
JobTracker和TaskTracker
JobTracker  对应于 NameNode
TaskTracker 对应于 DataNode
DataNode 和NameNode 是针对数据存放来而言的
JobTracker和TaskTracker是对于MapReduce执行而言的

mapreduce中几个主要概念，mapreduce整体上可以分为这么几条执行线索：
jobclient，JobTracker与TaskTracker。
1、JobClient会在用户端通过JobClient类将应用已经配置参数打包成jar文件存储到hdfs，
并把路径提交到Jobtracker,然后由JobTracker创建每一个Task（即MapTask和ReduceTask）
并将它们分发到各个TaskTracker服务中去执行
2、JobTracker是一个master服务，软件启动之后JobTracker接收Job，负责调度Job的每一个子任务task运行于TaskTracker上，
并监控它们，如果发现有失败的task就重新运行它。一般情况应该把JobTracker部署在单独的机器上。
3、TaskTracker是运行在多个节点上的slaver服务。TaskTracker主动与JobTracker通信，接收作业，并负责直接执行每一个任务。
TaskTracker都需要运行在HDFS的DataNode上


1、在mr环节中，那些环节需要优化，如何优化，请详细说明。
    1.1、 setNumReduceTasks  适当的设置reduce的数量，如果数据量比较大，那么可以增加reduce的数量
    1.2、适当的时候使用 combine 函数，减少网络传输数据量
    1.3、压缩map和reduce的输出数据
   1.4、使用SequenceFile二进制文件。
   1.5、通过application 的ui页面观察job的运行参数
  1.6、太多小文件，造成map任务过多的问题，应该可以先合并小文件，或者有一个特定的map作为处理小文件的输入
   1.7、map端效率低原因分析
源文件的大小远小于HDFS的块的大小。这意味着任务的开启和停止要耗费更多的时间，就没有足够的时间来读取并处理输入数据。
源文件无法分块。这导致需要通过网络IO从其他节点读取文件块。
一个节点的本地磁盘或磁盘控制器运行在降级模式中，读取写入性能都很差。这会影响某个节点，而不是全部节点。
源文件不来自于HDFS。则可能是Hadoop节点和数据源之间的延迟导致了性能低下。
Map任务从其他数据节点读取数据。可以从JobTracker的map任务细节信息和任务运行尝试中找到输入块的位置。
如果输入块的位置不是任务执行的节点，那就不是本地数据了。






Q1.列举出hadoop中定义的最常用的InputFormats。哪个是默认的？
Ans: hadoop中定义的最常见的InputFormats有TextInputFormat，KeyValueInputFormat，SequenceFileInputFormat。默认的InputFormats是TextInputFormats。

Q2. TextInputFormat和KeyValueInputFormat类之间的不同之处在于哪里？
Ans: TextInputFormat读取文本文件中的所有行，提供了行的偏移作为Mapper的键，实际的行作为mapper的值。
KeyValueInputFormat读取文本文件，解析所有行，首个空格前的字符是mapper的key，行的其余部分则是mapper的值。

Q3. hadoop中的InputSplit是什么？
Ans: 当一个hadoop job 运行时，它将输入文件拆分成块，分配每个块给各个mapper处理。这称为Input Split。

Q4. hadoop框架中文件拆分是如何被触发的？
Ans: 通过运行输入格式类中的getInputSplit()方法。

Q5. 考虑一种情况：Map/Reduce系统中，HDFS块大小事64MB，输入格式FileInputFormat，有三个文件64K，65MB，127MB，那么有hadoop框架会将输入划分成多少？
Ans:hadoop将会做5个拆分，64K文件拆分1个，65MB文件拆分2个，127MB文件拆分2个。

Q6. hadoop中的RecordReader的目的是什么？
Ans: InputSplit已经定义了部分工作，但没有描述如何访问它。RecordReader类实际从它的源加载数据，并转换成适合Mapper读取。RecordReader示例由InputFormat定义。

Q7. 如果hadoop中没有定义定制分区，那么如何在输出到reducer前执行数据分区？
Ans: 默认的分区器为各个键计算一个哈希值，并分配给基于这个结果的分区。

Q8. 什么是Combiner？
Ans: Combiner是一个最小删减的过程，处理由mapper产生的数据。Combiner将会接收某个节点上由mapper实例发送的所有数据。Combiner的输出被送到Reducer，
代替Mapper的输出。



4、简要描述你知道的数据挖掘算法和使用场景
（一）基于分类模型的案例
    （ 1）垃圾邮件的判别    通常会采用朴素贝叶斯的方法进行判别
    （2）医学上的肿瘤判断   通过分类模型识别 
（二）基于预测模型的案例  
         （1）红酒品质的判断  分类回归树模型进行预测和判断红酒的品质  
         （ 2）搜索引擎的搜索量和股价波动
（三）基于关联分析的案例：沃尔玛的啤酒尿布
（四）基于聚类分析的案例：零售客户细分
（五）基于异常值分析的案例：支付中的交易欺诈侦测
（六）基于协同过滤的案例：电商猜你喜欢和推荐引擎
（七）基于社会网络分析的案例：电信中的种子客户
（八）基于文本分析的案例
（1）字符识别：扫描王APP
（2）文学著作与统计：红楼梦归属
5、列举你知道的常用的hadoop管理和监控的命令、比如hdfs dfs -mkdir /usr
       -ls  -cat   -text    -cp   -put  -chmod   -chown
       -du      -get   -copyFromLocal   -copyToLocal 
      -mv   -rm   - tail   -chgrp















HDFS数据写入实现机制
3. HDFS的存储机制？
写入HDFS过程：
Client调用DistributedFileSystem对象的create方法，创建一个文件输出流（FSDataOutputStream）对象，通过DistributedFileSystem对象与Hadoop集群的NameNode进行一次RPC远程调用，在HDFS的Namespace中创建一个文件条目（Entry），该条目没有任何的Block，通过FSDataOutputStream对象，向DataNode写入数据，数据首先被写入FSDataOutputStream对象内部的Buffer中，然后数据被分割成一个个Packet数据包，以Packet最小单位，基于Socket连接发送到按特定算法选择的HDFS集群中一组DataNode（正常是3个，可能大于等于1）中的一个节点上，在这组DataNode组成的 Pipeline上依次传输 Packet， 这组 DataNode 组成的 Pipeline反方向上，发送 ack，最终由 Pipeline 中第一个 DataNode 节点将 Pipeline ack发送给Client，完成向文件写入数据，Client 在文件输出流（FSDataOutputStream）对象上调用close方法，关闭流.调用DistributedFileSystem对象的complete 方法，通知 NameNode 文件写入成功.
读取文件过程：
使用HDFS提供的客户端开发库Client，向远程的Namenode发起RPC请求；Namenode会视情况返回文件的部分或全部block列表，对于每个block，Namenode都会返回有该block拷贝的DataNode地址；客户端开发库Client会选取离客户端最接近的DataNode来读取block；如果客户端本身就是DataNode,那么将从本地直接获取数据.读取完当前block的数据后，关闭与当前的DataNode连接，并为读取下一个block寻找最佳的DataNode；当读完列表的block后，且文件读取还没有结束，客户端开发库会继续向Namenode获取下一批的block列表。读取完一个block都会进行 checksum 验证，如果读取 datanode 时出现错误，客户端会通知 Namenode，然后再从下一个拥有该 block 拷贝的 datanode 继续读。



.Hadoop 中通过拆分任务到多个节点运行来实现并行计
算，但某些节点运行较慢会拖慢整个任务的运行，Hadoop
采用何种机制应对这个情况？
答: 推测执行机制是 Hadoop 对“拖后腿”的任务的一种优化机制，当一个作业的某些任务
运行速度明显慢于同作业的其他任务时，Hadoop 会在另一个节点 上为“慢任务”启动一
个备份任务，这样两个任务同时处理一份数据，而 Hadoop 最终会将优先完成的那个任务
的结果作为最终结果，并将另一个任务杀掉。


15 请描述mapReduce中shuffle阶段的工作流程，如何优化shuffle阶段
 
 
 
分区，排序，溢写，拷贝到对应reduce机器上，增加combiner，压缩溢写的文件。
16 请描述mapReduce 中combiner 的作用是什么，一般使用情景，哪些情况不需要？
在MR作业中的Map阶段会输出结果数据到磁盘中。
Combiner只应该适用于那种Reduce的输入（key：value与输出（key：value）类型完全一致，且不影响最终结果的场景。比如累加，最大值等，也可以用于过滤数据，在 map端将无效的数据过滤掉。
在这些需求场景下，输出的数据是可以根据key值来作合并的，合并的目的是减少输出的数据量，减少IO的读写，减少网络传输,以提高MR的作业效率。
 
1.combiner的作用就是在map端对输出先做一次合并,以减少传输到reducer的数据量.
2.combiner最基本是实现本地key的归并,具有类似本地reduce,那么所有的结果都是reduce完成,效率会相对降低。
3.使用combiner,先完成的map会在本地聚合,提升速度.



 
3. 简述Hadoop的几个默认端口及其含义
 
dfs.namenode.http-address:50070
dfs.datanode.address:50010
fs.defaultFS:8020
yarn.resourcemanager.webapp.address:8088


5. 谈谈你对MapReduce的优化建议。
 
一.mapper调优
mapper调优主要就一个目标：减少输出量。
我们可以通过增加combine阶段以及对输出进行压缩设置进行mapper调优。
1>combine合并：
    实现自定义combine要求继承reducer类。比较适合map的输出是数值型的，方便进行统计。
2>压缩设置：
    在提交job的时候分别设置启动压缩和指定压缩方式。
 
二.reducer调优
 reducer调优主要是通过参数调优和设置reducer的个数来完成。
 reducer个数调优：
  要求：一个reducer和多个reducer的执行结果一致，不能因为多个reducer导致执行结果异常。
规则：一般要求在hadoop集群中的执行mr程序，map执行完成100%后，尽量早的看到reducer执行到33%，可以通过命令hadoop job -status job_id或者web页面来查看。
   原因：map的执行process数是通过inputformat返回recordread来定义的；而reducer是有三部分构成的，分别为读取mapper输出数据、合并所有输出数据以及reduce处理，其中第一步要依赖map的执行，所以在数据量比较大的情况下，一个reducer无法满足性能要求的情况下，我们可以通过调高reducer的个数来解决该问题。
优点：充分利用集群的优势。
缺点：有些mr程序没法利用多reducer的优点，比如获取top n的mr程序。


5.Mapreduce的工作原理，请举例子说明mapreduce是怎么运行的？
 
离线计算框架，过程分为split map shuffle reduce四个过程 。
架构节点有：Jobtracker TaskTracker 。
Split将文件分割，传输到mapper，mapper接收KV形式的数据，经过处理，再传到shuffle过程。
Shuffle先进行HashPartition或者自定义的partition，会有数据倾斜和reduce的负载均衡问题；再进行排序，默认按字典排序；为减少mapper输出数据，再根据key进行合并，相同key的数据value会被合并；最后分组形成（key,value{}）形式的数据，输出到下一阶段 。
Reduce输入的数据就变成了，key+迭代器形式的数据，再进行处理。


2.两个类TextInputFormat和KeyValueInputFormat的区别是什么？
 
相同点：
TextInputformat和KeyValueTextInputFormat都继承了FileInputFormat类，都是每一行作为一个记录；
区别：
TextInputformat将每一行在文件中的起始偏移量作为 key，每一行的内容作为value。默认以\n或回车键作为一行记录。
KeyValueTextInputFormat 适合处理输入数据的每一行是两列，并用 tab 分离的形式

 




4.Hadoop 框架中文件拆分是怎么被调用的？
 
InputFormat 是 MapReduce 中一个很常用的概念是文件拆分必须实现的一个接口，包含了两个方法：
public interface InputFormat<K, V> {
InputSplit[] getSplits(JobConf job, int numSplits) throws IOException;
RecordReader<K, V> createRecordReader(InputSplit split, TaskAttemptContext
context) throws IOException;
}
这两个方法分别完成以下工作：
方法 getSplits 将输入数据切分成 splits，splits 的个数即为 map tasks 的个数，
splits 的大小默认为块大小，即 64M
方法 getRecordReader 将每个 split 解析成 records, 再依次将 record 解析成
<K,V>对，也就是说 InputFormat 完成以下工作：InputFile --> splits --> <K,V>
系 统 常 用 的 InputFormat 又 有 哪 些 呢 ？ TextFileInputFormat ，
KeyValueTextFileInputFormat，SequenceFileInputFormat<key,value>,
NLineInputFormat 其中 Text InputFormat 便是最常用的，它的 <K,V>就代表 <行偏移,
该行内容>
然而系统所提供的这几种固定的将 InputFile 转换为 <K,V>的方式有时候并不能满足我
们的需求：
此时需要我们自定义 InputFormat ，从而使 Hadoop 框架按照我们预设的方式来将
InputFile 解析为<K,V>
在领会自定义 InputFormat 之前，需要弄懂一下几个抽象类、接口及其之间的关系：
InputFormat(interface), FileInputFormat(abstract class), TextInputFormat(class),
RecordReader (interface), Line RecordReader(class)的关系FileInputFormat implements InputFormat
TextInputFormat extends FileInputFormat
TextInputFormat.get RecordReader calls Line RecordReader
Line RecordReader implements RecordReader
对于 InputFormat 接口，上面已经有详细的描述
再看看 FileInputFormat，它实现了 InputFormat 接口中的 getSplits 方法，而将
getRecordReader 与 isSplitable 留给具体类(如 TextInputFormat )实现， isSplitable 方
法通常不用修改，所以只需要在自定义的 InputFormat 中实现
getRecordReader 方 法 即 可 ， 而 该 方 法 的 核 心 是 调 用 Line RecordReader( 即 由
LineRecorderReader 类来实现 " 将每个 s plit 解析成 records, 再依次将 record 解析成
<K,V>对" )，该方法实现了接口 RecordReader
public interface RecordReader<K, V> {
boolean next(K key, V value) throws IOException;
K createKey();
V createValue();
long getPos() throws IOException;
public void close() throws IOException;
float getProgress() throws IOException;
}
定义一个 InputFormat 的核心是定义一个类似于LineRecordReader的RecordReader



6.Hadoop 中 RecordReader 的作用是什么?
 
Hadoop 的 MapReduce 框架来处理数据，主要是面向海量大数据，对于这类数据，Hadoop 能够使其真正发挥其能力。对于海量小文件，不是说不能使用 Hadoop 来处理，只不过直接进行处理效率不会高，实际应用中，我们在使用 Hadoop 进行计算的时候，需要考虑将小数据转换成大数据，比如通过合并压缩等方法，我们通过自定义 InputFormat和RecordReader来实现对海量小文件的并行处理。


8.如果没有定义 partitioner，那数据在被送达 reducer 前是如何被分区的？
如果没有自定义的 partitioning，则默认的 partition 算法，即根据每一条数据的 key
的 hashcode 值摸运算（%）reduce 的数量，得到的数字就是“分区号“。

 

7.Map 阶段结束后， Hadoop 框架会处理： Partitioning，Shuffle 和 Sort，在这个阶段都发生了什么？
 

map task上的洗牌（shuffle）结束，此时 reducer task 上的洗牌开始，抓取 fetch 所
属于自己分区的数据，同时将这些分区的数据进行排序sort（默认的排序是根据每一条数据的键的字典
排序），进而将数据进行合并merge，即根据key相同的，将其 value 组成一个集合，最后输出结果。



1.3Hadoop集群有几种角色的节点，每个节点对应的进程有哪些？
 
Hadoop 集群从三个角度划分为两个角色。 Hadoop 集群包括 hdfs 作为文件存储系统，mapreduce 作为分布式计算框架。
(1)最基本的划分为主节点和从节点
(2)在 hdfs 中，Namenode 作为主节点，接收客户端的读写服务。 存储文件的的元数据如文件名，文件目录结构，文件属性(生成时间，副本数，文件权限)，以及 block所在的datanode 等等，namenode 的元数据信息会在启动后加载到内存，metadata存储到磁盘文件名为” fsimage” ,block 的信息不会保存到 fsimage,edits 记录对metadata 的操作日志datanode 作为从节点。 Datanode 的作用是存储数据即 block，启动 datanode 线程的时候会向 namenode 汇报 block 信息，通过向 namenode 发送心跳保持与其联系，如果 namenode10 分钟没有收到 datanode 的心跳，则认为 datanode 挂了，并复制其上的 block 到其他 datanode。
(3)在 mapreduce 中的主节点为 jobTracker 和 tasktracker。
JobTracker 作为主节点，负责调度分配每一个子任务 task 运行于 taskTracker上，如果发现有失败的task 就重新分配到其他节点，每个 Hadoop 集群一般有一个JobTaskTracker，运行在 master 节点上。
TaskTracker主动与JobTracker通信，接收作业，并负责直接执行每一个任务，为了减少网络带宽TaskTracker 最好运行在 datanode 上



1.7mapreduce 的优化方法有哪些?
 
Mapreduce 程序效率的瓶颈在于两点：
一计算机性能
二 I/O 操作优化
优化分为时间和空间两种常见的优化策略如下：
1,输入的文件尽量使用大文件，众多的小文件会导致map的数量众多，每个map任务都会造成一些性能的损失，如果输入的是小的文件可以在进行mapreduce处理之前整合成为大文件，或者直接采用ConbinFileInputFormat来作为输入方式，此时，hadoop 会考虑节点和集群的位置信息，决定将哪些文件打包到一个单元中。
2,合理的分配 map 和 reduce 的任务的数量.
3,压缩中间数据，减少 I/O.
4,在 map 之后先进行 combine 处理，减少 I/O.



1.10 列举你了解的海量数据的处理方法及适用范围，如果有相关使用经验，可简要说明。
 
mapreduce 分布式计算 mapreduce 的思想就是分而治之
倒排索引:一种索引方法，用来存储在全文搜索下某个单词在一个文档或者一组文档中的存储位置的映射，在倒排索引中单词指向了包含单词的文档。
消息队列:大量的数据写入首先存入消息队列进行缓冲，再把消息队列作为数据来源进行数据读取。
数据库读写分离:向一台数据库写入数据，另外的多台数据库从这台数据库中进行读取。


2. 运行一个 hadoop 任务的流程是什么样的。
1、 导入数据对需分析的数据进行分片，片的大小默认与 datanode 块大小相同。
2、 每个数据片由一个 mapper 进行分析，mapper 按照需求将数据拆分为一个个 keyvalue 格式的数据。
3、 每个 key-value 数据调用一次 map 方法，对数据进行相应的处理后输出。
4、 将输出的数据复制到对应的分区，默认一个键一个区，相同键放在同一个区中。
5、 将输出的数据进行合并为 key-Iterable 格式。
6、 每个分区有一个 reduce，每个 reduce 将同一个分区的数据进行合并处理为自己所需的数据格式。
7、 将数据输出至 hdfs。

 


4. 什么样的计算不能用 mr 来提速，举 5 个例子。
 
 
1、 数据量很小。
2、 繁杂的小文件。
3、 索引是更好的存取机制的时候。
4、 事务处理。
5、 只有一台机器的时候。



5. 一个 mr 作业跑的比较慢，如何来优化。至少给出 6 个方案。
 
mr跑的慢可能有很多原因，如：数据倾斜、map和reduce数设置不合理、reduce等待过久、小文件过多、spill 次数过多、 merge 次数过多等。
1、解决数据倾斜：数据倾斜可能是partition不合理，导致部分partition中的数据过多，部分过少。可通过分析数据，自定义分区器解决。
2、合理设置map和reduce数：两个都不能设置太少，也不能设置太多。太少，会导致task等待，延长处理时间；太多，会导致 map、 reduce 任务间竞争资源，造成处理超时等错误。
3、设置map、reduce共存：调整slowstart.completedmaps参数，使map运行到一定程度后，reduce也开始运行，减少 reduce 的等待时间。
4、合并小文件：在执行mr任务前将小文件进行合并，大量的小文件会产生大量的map任务，增大map任务装载次数，而任务的装载比较耗时，从而导致 mr 运行较慢。
5、减少spill次数：通过调整io.sort.mb及sort.spill.percent参数值，增大触发spill的内存上限，减少spill 次数，从而减少磁盘 IO。
6、减少merge次数：通过调整io.sort.factor参数，增大merge的文件数目，减少merge的次数，从而缩短mr处理时间。


6. Hadoop会有哪些重大故障，如何应对？至少给出 5个。
 
 
1、 namenode 单点故障：通过 zookeeper 搭建 HA 高可用，可自动切换 namenode。
2、ResourceManager单点故障：可通过配置YARN的HA，并在配置的namenode上手动启动ResourceManager作为Slave，在 Master 故障后，Slave 会自动切换为Master。
3、reduce阶段内存溢出：是由于单个reduce任务处理的数据量过多，通过增大reducetasks数目、优化partition 规则使数据分布均匀进行解决。
4、datanode内存溢出：是由于创建的线程过多，通过调整linux的maxuserprocesses参数，增大可用线程数进行解决。
5、 集群间时间不同步导致运行异常：通过配置内网时间同步服务器进行解决。



8. 你认为 hadoop 有哪些设计不合理的地方。
 
 
1、 不支持文件的并发写入和对文件内容的随机修改。
2、 不支持低延迟、高吞吐的数据访问。
3、 存取大量小文件，会占用 namenode 大量内存，小文件的寻道时间超过读取时间。
4、 hadoop 环境搭建比较复杂。
5、 数据无法实时处理。
6、 mapreduce 的 shuffle 阶段 IO 太多。
7、 编写 mapreduce 难度较高，实现复杂逻辑时，代码量太大。


如果nameNode意外终止，SecondaryNameNode会帮助恢复而不是替代。
4.lucene是支持随机读写的，而HDFS只是支持随机读，但是HBase可以来补救。HBase提供随机读写，来解决Hadoop不能处理的问题，
15.namenode不需要从磁盘中读取metadata，所有数据都在内存中，硬盘上只是序列化的结果，只有每次namenode启动时才会读取。

.0请写出以下的shell命令 
（1）杀死一个job
（2）删除hdfs上的 /tmp/aaa目录
（3）加入一个新的存储节点和删除一个节点需要执行的命令
答：（1）hadoop job –list   得到job的id，然后执      行 hadoop job  -kill  jobId就可以杀死一个指定jobId的job工作了。
（2）hadoopfs  -rmr /tmp/aaa
(3)  增加一个新的节点在新的几点上执行
            Hadoop  daemon.sh start  datanode
                     Hadooop daemon.sh  start  tasktracker/nodemanager
 
下线时，要在conf目录下的excludes文件中列出要下线的datanode机器主机名
              然后在主节点中执行  hadoop  dfsadmin  -refreshnodes  à下线一个datanode
删除一个节点的时候，只需要在主节点执行
 hadoop mradmin -refreshnodes  ---à下线一个tasktracker/nodemanager



这个有问题？？？
6.0      当前日志采样格式为
           a , b , c , d
           b , b , f , e
           a , a , c , f        
请你用最熟悉的语言编写mapreduce，计算第四列每个元素出现的个数
 
答：
public classWordCount1 {
       public static final String INPUT_PATH ="hdfs://hadoop0:9000/in";
       public static final String OUT_PATH ="hdfs://hadoop0:9000/out";
       public static void main(String[] args)throws Exception {
              Configuration conf = newConfiguration();
              FileSystem fileSystem =FileSystem.get(conf);
              if(fileSystem.exists(newPath(OUT_PATH))){}
              fileSystem.delete(newPath(OUT_PATH),true);
              Job job = newJob(conf,WordCount1.class.getSimpleName());
              //1.0读取文件，解析成key,value对
              FileInputFormat.setInputPaths(job,newPath(INPUT_PATH));
              //2.0写上自己的逻辑，对输入的可以，value进行处理，转换成新的key,value对进行输出
              job.setMapperClass(MyMapper.class);
              job.setMapOutputKeyClass(Text.class);
              job.setMapOutputValueClass(LongWritable.class);
              //3.0对输出后的数据进行分区
              //4.0对分区后的数据进行排序，分组，相同key的value放到一个集合中
              //5.0对分组后的数据进行规约
              //6.0对通过网络将map输出的数据拷贝到reduce节点
              //7.0 写上自己的reduce函数逻辑，对map输出的数据进行处理
              job.setReducerClass(MyReducer.class);
              job.setOutputKeyClass(Text.class);
              job.setOutputValueClass(LongWritable.class);
              FileOutputFormat.setOutputPath(job,new Path(OUT_PATH));
              job.waitForCompletion(true);
       }
       static class MyMapper extendsMapper<LongWritable, Text, Text, LongWritable>{
              @Override
              protected void map(LongWritablek1, Text v1,
                            org.apache.hadoop.mapreduce.Mapper.Contextcontext)
                            throws IOException,InterruptedException {
                     String[] split =v1.toString().split("\t");
                     for(String words :split){
                            context.write(split[3],1);
                     }
              }
       }
       static class MyReducer extends Reducer<Text,LongWritable, Text, LongWritable>{
             
              protected void reduce(Text k2,Iterable<LongWritable> v2,
                            org.apache.hadoop.mapreduce.Reducer.Contextcontext)
                            throws IOException,InterruptedException {
                     Long count = 0L;
                     for(LongWritable time :v2){
                            count += time.get();
                     }
                     context.write(v2, newLongWritable(count));
              }
       }
}

9.0 请简述hadoop怎样实现二级排序（就是对key和value双排序）
    第一种方法是，Reducer将给定key的所有值都缓存起来，然后对它们再做一个Reducer内排序。但是，由于Reducer需要保存给定key的所有值，可能会导致出现内存耗尽的错误。
第二种方法是，将值的一部分或整个值加入原始key，生成一个组合key。这两种方法各有优势，第一种方法编写简单，但并发度小，数据量大的情况下速度慢(有内存耗尽的危险)，
第二种方法则是将排序的任务交给MapReduce框架shuffle，更符合Hadoop/Reduce的设计思想。这篇文章里选择的是第二种。我们将编写一个Partitioner，确保拥有相同key(原始key，不包括添加的部分)的所有数据被发往同一个Reducer，还将编写一个Comparator，以便数据到达Reducer后即按原始key分组。


10.简述hadoop实现jion的几种方法
Map side join----大小表join的场景，可以借助distributed cache
Reduce side join


12.0 请简述mapreduce中的combine和partition的作用
答：combiner是发生在map的最后一个阶段，其原理也是一个小型的reducer，主要作用是减少输出到reduce的数据量，缓解网络传输瓶颈，提高reducer的执行效率。
partition的主要作用将map阶段产生的所有kv对分配给不同的reducer task处理，可以将reduce阶段的处理负载进行分摊


hive内部表和外部表的区别
Hive 向内部表导入数据时，会将数据移动到数据仓库指向的路径；若是外部表，数据的具体存放目录由用户建表时指定
在删除表的时候，内部表的元数据和数据会被一起删除， 
而外部表只删除元数据，不删除数据。
这样外部表相对来说更加安全些，数据组织也更加灵活，方便共享源数据。


14. Hbase的rowKey怎么创建比较好？列簇怎么创建比较好？
答：
rowKey最好要创建有规则的rowKey，即最好是有序的。
经常需要批量读取的数据应该让他们的rowkey连续；
将经常需要作为条件查询的关键词组织到rowkey中；
 
列族的创建：
按照业务特点，把数据归类，不同类别的放在不同列族


15. 用mapreduce怎么处理数据倾斜问题
本质：让各分区的数据分布均匀
可以根据业务特点，设置合适的partition策略
如果事先根本不知道数据的分布规律，利用随机抽样器抽样后生成partition策略再处理

17. hbase内部机制是什么
答：
Hbase是一个能适应联机业务的数据库系统
物理存储：hbase的持久化数据是存放在hdfs上
存储管理：一个表是划分为很多region的，这些region分布式地存放在很多regionserver上
Region内部还可以划分为store，store内部有memstore和storefile
版本管理：hbase中的数据更新本质上是不断追加新的版本，通过compact操作来做版本间的文件合并
Region的split
集群管理：zookeeper  + hmaster（职责）  + hregionserver（职责

19 hadoop中常用的数据压缩算法
答：
Lzo
Gzip
Default
Snapyy
如果要对数据进行压缩，最好是将原始数据转为SequenceFile  或者 Parquet File（spark）
 


32.三个datanode中当有一个datanode出现错误时会怎样？
答：
Namenode会通过心跳机制感知到datanode下线
会将这个datanode上的block块在集群中重新复制一份，恢复文件的副本数量
会引发运维团队快速响应，派出同事对下线datanode进行检测和修复，然后重新上线

35.MapReduce优化经验
答：(1.)设置合理的map和reduce的个数。合理设置blocksize
(2.)避免出现数据倾斜
(3.combine函数
(4.对数据进行压缩
(5.小文件处理优化：事先合并成大文件，combineTextInputformat，在hdfs上用mapreduce将小文件合并成SequenceFile大文件（key:文件名，value：文件内容）
(6.参数优化


36.请列举出曾经修改过的/etc/下面的文件，并说明修改要解决什么问题？
答：/etc/profile这个文件，主要是用来配置环境变量。让hadoop命令可以在任意目录下面执行。
/ect/sudoers
/etc/hosts
/etc/sysconfig/network
/etc/inittab


39.mapreduce的大致流程
答：主要分为八个步骤
1/对文件进行切片规划
2/启动相应数量的maptask进程
3/调用FileInputFormat中的RecordReader，读一行数据并封装为k1v1
4/调用自定义的map函数，并将k1v1传给map
5/收集map的输出，进行分区和排序
6/reduce task任务启动，并从map端拉取数据
7/reduce task调用自定义的reduce函数进行处理
8/调用outputformat的recordwriter将结果数据输出

52.如何确认hadoop集群的健康状况
答：有完善的集群监控体系（ganglia，nagios）
Hdfs dfsadmin –report
Hdfs haadmin  –getServiceState  nn1

54.hive如何调优
答：hive最终都会转化为mapreduce的job来运行，要想hive调优，实际上就是mapreduce调优，可以有下面几个方面的调优。解决收据倾斜问题，减少job数量，设置合理的map和reduce个数，对小文件进行合并，优化时把握整体，单个task最优不如整体最优。按照一定规则分区。

58.HBase宕机如何处理
答：宕机分为HMaster宕机和HRegisoner宕机，如果是HRegisoner宕机，HMaster会将其所管理的region重新分布到其他活动的RegionServer上，由于数据和日志都持久在HDFS中，该操作不会导致数据丢失。所以数据的一致性和安全性是有保障的。
如果是HMaster宕机，HMaster没有单点问题，HBase中可以启动多个HMaster，通过Zookeeper的Master Election机制保证总有一个Master运行。即ZooKeeper会保证总会有一个HMaster在对外提供服务。

63. 谈谈 hadoop1 和 hadoop2 的区别
答：
hadoop1的主要结构是由HDFS和mapreduce组成的，HDFS主要是用来存储数据，mapreduce主要是用来计算的，那么HDFS的数据是由namenode来存储元数据信息，datanode来存储数据的。Jobtracker接收用户的操作请求之后去分配资源执行task任务。
在hadoop2中，首先避免了namenode单点故障的问题，使用两个namenode来组成namenode  feduration的机构，两个namenode使用相同的命名空间，一个是standby状态，一个是active状态。用户访问的时候，访问standby状态，并且，使用journalnode来存储数据的原信息，一个namenode负责读取journalnode中的数据，一个namenode负责写入journalnode中的数据，这个平台组成了hadoop的HA就是high  availableAbility高可靠。
然后在hadoop2中没有了jobtracker的概念了，统一的使用yarn平台来管理和调度资源，yarn平台是由resourceManager和NodeManager来共同组成的，ResourceManager来接收用户的操作请求之后，去NodeManager上面启动一个主线程负责资源分配的工作，然后分配好了资源之后告知ResourceManager，然后ResourceManager去对应的机器上面执行task任务。

67. 文件大小默认为 64M，改为 128M 有啥影响？
答：更改文件的block块大小，需要根据我们的实际生产中来更改block的大小，如果block定义的太小，大的文件都会被切分成太多的小文件，减慢用户上传效率，如果block定义的太大，那么太多的小文件可能都会存到一个block块中，虽然不浪费硬盘资源，可是还是会增加namenode的管理内存压力。


1、  在hadoop中定义的主要公用InputFormat中，哪个是默认值？ FileInputFormat
 
2、  两个类TextInputFormat和KeyValueInputFormat的区别是什么？
       答：TextInputFormat主要是用来格式化输入的文本文件的，KeyValueInputFormat则主要是用来指定输入输出的key,value类型的
 
3、  在一个运行的hadoop任务中，什么是InputSplit？
       InputSplit是InputFormat中的一个方法，主要是用来切割输入文件的，将输入文件切分成多个小文件，
       然后每个小文件对应一个map任务
      
4、  Hadoop框架中文件拆分是怎么调用的？
       InputFormat  --> TextInputFormat  -->RecordReader  --> LineRecordReader  --> LineReader

下列哪项通常是集群的最主要瓶颈：
由于大数据面临海量数据，读写数据都需要io，然后还要冗余数据，hadoop一般备3份数据，所以IO就会打折扣。


3.2 Block Size 是不可以修改的。（错误 ）
分析：它是可以被修改的 Hadoop 的基础配置文件是 hadoop-default.xml，默认建立一
个 Job 的时候会建立 Job 的 Config，Config 首先读入 hadoop-default.xml 的配置，然
后再读入 hadoop-site.xml 的配置（这个文件初始的时候配置为空），hadoop-site.xml 中主要配置需要覆盖的 hadoop-default.xml 的系统级配置。


3.1 Ganglia 不仅可以进行监控，也可以进行告警。（ 正确）
分析：此题的目的是考 Ganglia 的了解。严格意义上来讲是正确。ganglia 作为一款最常用的 Linux 环境中的监控软件，它擅长的的是从节点中按照用户的需求以较低的代价采集数据。
但是 ganglia 在预警以及发生事件后通知用户上并不擅长。最新的 ganglia 已经有了部分这方面的功能。但是更擅长做警告的还有 Nagios。Nagios，就是一款精于预警、通知的软件。通过将 Ganglia 和 Nagios 组合起来，把 Ganglia 采集的数据作为 Nagios 的数据源，然后利用 Nagios 来发送预警通知，可以完美的实现一整套监控管理的系统。

3.3 Nagios 不可以监控 Hadoop 集群，因为它不提供 Hadoop 支持。（错误 ）
分析：Nagios 是集群监控工具，而且是云计算三大利器之一






5.Redis,传统数据库,hbase,hive 每个之间的区别(问的非常细)
Redis是缓存，围绕着内存和缓存说
Hbase是列式数据库，存在hdfs上，围绕着数据量来说
Hive是数据仓库，是用来分析数据的，不是增删改查数据的。


3.Mapreduce 一些流程,经过哪些步骤
Map—combiner—partition—sort—copy—sort—grouping—reduce
4.说下对hadoop 的一些理解,包括哪些组件
详谈hadoop的应用，包括的组件分为三类，分别说明hdfs，yarn，mapreduce

1.一些传统的hadoop 问题,mapreduce 他就问shuffle 阶段,你怎么理解的
Shuffle意义在于将不同map处理后的数据进行合理分配，让reduce处理，从而产生了排序、分区。
2.Mapreduce 的 map 数量 和 reduce 数量 怎么确定 ,怎么配置
Map无法配置，reduce随便配置






Apache Hadoop 是一个开源软件框架，可安装在一个商用机器集群中，使机器可彼此通信并协同工作，以高度分布式的方式共同存储和处理大量数据。最初，Hadoop 包含以下两个主要组件：Hadoop Distributed File System (HDFS) 和一个分布式计算引擎，该引擎支持以 MapReduce 作业的形式实现和运行程序。
MapReduce 是 Google 推广的一个简单的编程模型，它对以高度并行和可扩展的方式处理大数据集很有用。MapReduce 的灵感来源于函数式编程，用户可将他们的计算表达为 map 和 reduce 函数，将数据作为键值对来处理。Hadoop 提供了一个高级 API 来在各种语言中实现自定义的 map 和 reduce 函数。
Hadoop 还提供了软件基础架构，以一系列 map 和 reduce 任务的形式运行 MapReduce 作业。Map 任务 在输入数据的子集上调用 map 函数。在完成这些调用后，reduce 任务 开始在 map 函数所生成的中间数据上调用 reduce 任务，生成最终的输出。 map 和 reduce 任务彼此单独运行，这支持并行和容错的计算。
最重要的是，Hadoop 基础架构负责处理分布式处理的所有复杂方面：并行化、调度、资源管理、机器间通信、软件和硬件故障处理，等等。得益于这种干净的抽象，实现处理数百（或者甚至数千）个机器上的数 TB 数据的分布式应用程序从未像现在这么容易过，甚至对于之前没有使用分布式系统的经验的开发人员也是如此。
MR架构
map reduce 过程图
将任务分割为 Map 端和 reduce 端。
JobClient JobTracker TaskTracker
MR 架构

	1. JobClient 向 JobTracker 请求一个新的 jobID
	2. 检查作业输出说明
	3. 计算作业输出划分split
	4. 将运行作业所需要的资源（作业的jar文件、配置文件、计算所得的输入划分）复制到一个以作业ID命名的目录中JobTracker的文件系统。
	5. 通过调用JobTracker的submitJob()方法，告诉JobTracker作业准备执行
	6. JobTracker接收到submitJob()方法调用后，把此调用放到一个内部队列中，交由作业调度器进行调度，并对其进行初始化
	7. 创建运行任务列表，作业调度去首先从共享文件系统中获取JobClient已经计算好的输入划分信息（图中step6），然后为每个划分创建一个Map任务（一个split对应一个map，有多少split就有多少map）。
	8. TaskTracker执行一个简单的循环，定期发送心跳（heartbeat）调用JobTracker

shuffle combine
整体的Shuffle过程包含以下几个部分：Map端Shuffle、Sort阶段、Reduce端Shuffle。即是说：Shuffle 过程横跨 map 和 reduce 两端，中间包含 sort 阶段，就是数据从 map task 输出到reduce task输入的这段过程。
sort、combine 是在 map 端的，combine 是提前的 reduce ，需要自己设置。
Hadoop 集群中，大部分 map task 与 reduce task 的执行是在不同的节点上。当然很多情况下 Reduce 执行时需要跨节点去拉取其它节点上的map task结果。如果集群正在运行的 job 有很多，那么 task 的正常执行对集群内部的网络资源消耗会很严重。而对于必要的网络资源消耗，最终的目的就是最大化地减少不必要的消耗。还有在节点内，相比于内存，磁盘 IO 对 job 完成时间的影响也是可观的。从最基本的要求来说，对于 MapReduce 的 job 性能调优的 Shuffle 过程，目标期望可以有：

	* 完整地从map task端拉取数据到reduce 端。
	* 在跨节点拉取数据时，尽可能地减少对带宽的不必要消耗。
	* 减少磁盘IO对task执行的影响。

总体来讲这段Shuffle过程，能优化的地方主要在于减少拉取数据的量及尽量使用内存而不是磁盘。
Map Shuffle
map shuffle

	1. 输入
在map task 执行时，其输入来源 HDFS的 block ，map task 只读取split 。Split 与 block 的对应关系可能是多对一，默认为一对一。
	2. 切分
决定于当前的 mapper的 part交给哪个 reduce的方法是：mapreduce 提供的Partitioner接口，对key 进行 hash 后，再以 reducetask 数量取模，然后到指定的 job 上。
然后将数据写入内存缓冲区中，缓冲区的作用是批量收集map结果，减少磁盘IO的影响。key/value对以及 Partition 的结果都会被写入缓冲区。写入之前，key 与value 值都会被序列化成字节数组。
	3. 溢写
由于内存缓冲区的大小限制（默认100MB），当map task输出结果很多时就可能发生内存溢出，所以需要在一定条件下将缓冲区的数据临时写入磁盘，然后重新利用这块缓冲区。这个从内存往磁盘写数据的过程被称为Spill，中文可译为溢写。
这个溢写是由另外单独线程来完成，不影响往缓冲区写map结果的线程。
整个缓冲区有个溢写的比例spill.percent。这个比例默认是0.8，

Combiner 将有相同key的 key/value 对加起来，减少溢写spill到磁盘的数据量。Combiner的适用场景：由于Combiner的输出是Reducer的输入，Combiner绝不能改变最终的计算结果。故大多数情况下，combiner适用于输入输出的key/value类型完全一致，且不影响最终结果的场景（比如累加、最大值等……）。

	1. Merge
map 很大时，每次溢写会产生一个 spill_file，这样会有多个 spill_file，而最终的输出只有一个文件，在最终输出之前会对多个中间过程多次产生的溢写文件 spill_file 进行合并，此过程就是 merge。

merge 就是把相同 key 的结果加起来。（当然，如果设置过combiner，也会使用combiner来合并相同的key）
Reduce Shuffle
reduce shuffle
在 reduce task 之前，不断拉取当前 job 里每个 maptask 的最终结果，然后对从不同地方拉取过来的数据不断地做 merge ，也最终形成一个文件作为 reduce task 的输入文件。

	1. copy
Reduce进程启动一些数据copy线程(Fetcher)，通过HTTP方式请求map task所在的TaskTracker获取map task的输出文件。因为maptask早已结束，这些文件就归TaskTracker管理在本地磁盘中。
	2. merge
Copy 过来的数据会先放入内存缓冲区中，这里的缓冲区大小要比 map 端的更为灵活，它基于 JVM 的 heap size 设置，因为 Shuffle 阶段 Reducer 不运行，所以应该把绝大部分的内存都给 Shuffle 用。这里需要强调的是，merge 有三种形式：1)内存到内存 2)内存到磁盘 3)磁盘到磁盘。默认情况下第一种形式不启用，让人比较困惑，是吧。当内存中的数据量到达一定阈值，就启动内存到磁盘的 merge 。与 map 端类似，这也是溢写的过程，这个过程中如果你设置有Combiner，也是会启用的，然后在磁盘中生成了众多的溢写文件。第二种merge方式一直在运行，直到没有 map 端的数据时才结束，然后启动第三种磁盘到磁盘的 merge 方式生成最终的那个文件。
	3. reducer的输入
merge 的最后会生成一个文件，大多数情况下存在于磁盘中，但是需要将其放入内存中。当reducer 输入文件已定，整个 Shuffle 阶段才算结束。然后就是 Reducer 执行，把结果放到 HDFS 上。

YARN
YARN（Yet Another Resource Negotiator）,下一代MapReduce框架的名称，为了容易记忆，一般称为MRv2（MapReduce version 2）。该框架已经不再是一个传统的MapReduce框架，甚至与MapReduce无关，她是一个通用的运行时框架，用户可以编写自己的计算框架，在该运行环境中运行。用于自己编写的框架作为客户端的一个lib，在运用提交作业时打包即可。
why YARN instead of MRMR 的缺点
经典 MapReduce 的最严重的限制主要关系到可伸缩性、资源利用和对与 MapReduce 不同的工作负载的支持。在 MapReduce 框架中，作业执行受两种类型的进程控制：

	* 一个称为 JobTracker 的主要进程，它协调在集群上运行的所有作业，分配要在 TaskTracker 上运行的 map 和 reduce 任务。
	* 许多称为 TaskTracker 的下级进程，它们运行分配的任务并定期向 JobTracker 报告进度。

大型的 Hadoop 集群显现出了由单个 JobTracker 导致的可伸缩性瓶颈。
此外，较小和较大的 Hadoop 集群都从未最高效地使用他们的计算资源。在 Hadoop MapReduce 中，每个从属节点上的计算资源由集群管理员分解为固定数量的 map 和 reduce slot，这些 slot 不可替代。设定 map slot 和 reduce slot 的数量后，节点在任何时刻都不能运行比 map slot 更多的 map 任务，即使没有 reduce 任务在运行。这影响了集群的利用率，因为在所有 map slot 都被使用（而且我们还需要更多）时，我们无法使用任何 reduce slot，即使它们可用，反之亦然。
Hadoop 设计为仅运行 MapReduce 作业。随着替代性的编程模型（比如 Apache Giraph 所提供的图形处理）的到来，除 MapReduce 外，越来越需要为可通过高效的、公平的方式在同一个集群上运行并共享资源的其他编程模型提供支持。
原MapReduce框架的不足
	* JobTracker是集群事务的集中处理点，存在单点故障
	* JobTracker需要完成的任务太多，既要维护job的状态又要维护job的task的状态，造成过多的资源消耗
	* 在taskTracker端，用map/reduce task作为资源的表示过于简单，没有考虑到CPU、内存等资源情况，当把两个需要消耗大内存的task调度到一起，很容易出现OOM
	* 把资源强制划分为map/reduce slot,当只有map task时，reduce slot不能用；当只有reduce task时，map slot不能用，容易造成资源利用不足。

解决可伸缩性问题
在 Hadoop MapReduce 中，JobTracker 具有两种不同的职责：

	* 管理集群中的计算资源，这涉及到维护活动节点列表、可用和占用的 map 和 reduce slots 列表，以及依据所选的调度策略将可用 slots 分配给合适的作业和任务
	* 协调在集群上运行的所有任务，这涉及到指导 TaskTracker 启动 map 和 reduce 任务，监视任务的执行，重新启动失败的任务，推测性地运行缓慢的任务，计算作业计数器值的总和，等等

为单个进程安排大量职责会导致重大的可伸缩性问题，尤其是在较大的集群上，JobTracker 必须不断跟踪数千个 TaskTracker、数百个作业，以及数万个 map 和 reduce 任务。相反，TaskTracker 通常近运行十来个任务，这些任务由勤勉的 JobTracker 分配给它们。
为了解决可伸缩性问题，一个简单而又绝妙的想法应运而生：我们减少了单个 JobTracker 的职责，将部分职责委派给 TaskTracker，因为集群中有许多 TaskTracker。在新设计中，这个概念通过将 JobTracker 的双重职责（集群资源管理和任务协调）分开为两种不同类型的进程来反映。
YARN 的优点
	1. 更快地MapReduce计算
	2. 对多框架支持
	3. 框架升级更容易

YARN

	* ResourceManager 代替集群管理器
	* ApplicationMaster 代替一个专用且短暂的 JobTracker
	* NodeManager 代替 TaskTracker
	* 一个分布式应用程序代替一个 MapReduce 作业

一个全局 ResourceManager 以主要后台进程的形式运行，它通常在专用机器上运行，在各种竞争的应用程序之间仲裁可用的集群资源。
在用户提交一个应用程序时，一个称为 ApplicationMaster 的轻量型进程实例会启动来协调应用程序内的所有任务的执行。这包括监视任务，重新启动失败的任务，推测性地运行缓慢的任务，以及计算应用程序计数器值的总和。有趣的是，ApplicationMaster 可在容器内运行任何类型的任务。
NodeManager 是 TaskTracker 的一种更加普通和高效的版本。没有固定数量的 map 和 reduce slots，NodeManager 拥有许多动态创建的资源容器。


作者：HarperKoo
链接：https://www.jianshu.com/p/c97ff0ab5f49
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。












