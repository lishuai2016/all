
<!-- TOC -->

- [23_怎么保证redis是高并发以及高可用的？](#23_怎么保证redis是高并发以及高可用的)
    - [01_面试题以及解答思路介绍](#01_面试题以及解答思路介绍)
    - [02_redis如何通过读写分离来承载读请求QPS超过10万+？](#02_redis如何通过读写分离来承载读请求qps超过10万)
    - [03_redis replication以及master持久化对主从架构的安全意义](#03_redis-replication以及master持久化对主从架构的安全意义)
    - [04_redis主从复制原理、断点续传、无磁盘化复制、过期key处理](#04_redis主从复制原理断点续传无磁盘化复制过期key处理)
    - [05_redis replication的完整流运行程和原理的再次深入剖析](#05_redis-replication的完整流运行程和原理的再次深入剖析)
    - [06_redis主从架构下如何才能做到百分之99.99的高可用性？](#06_redis主从架构下如何才能做到百分之9999的高可用性)
    - [07_redis哨兵架构的相关基础知识的讲解](#07_redis哨兵架构的相关基础知识的讲解)
    - [08_redis哨兵主备切换的数据丢失问题：异步复制、集群脑裂](#08_redis哨兵主备切换的数据丢失问题异步复制集群脑裂)
    - [09_redis哨兵的多个核心底层原理的深入解析（包含slave选举算法）](#09_redis哨兵的多个核心底层原理的深入解析包含slave选举算法)

<!-- /TOC -->

# 23_怎么保证redis是高并发以及高可用的？

高并发：读写分离实现

高可用：哨兵机制，主备切换


## 01_面试题以及解答思路介绍

> 1、面试题

如何保证Redis的高并发和高可用？redis的主从复制原理能介绍一下么？redis的哨兵原理能介绍一下么？

> 2、面试官心里分析

其实问这个问题，主要是考考你，redis单机能承载多高并发？如果单机扛不住如何扩容抗更多的并发？redis会不会挂？既然redis会挂那怎么保证redis是高可用的？

其实针对的都是项目中你肯定要考虑的一些问题，如果你没考虑过，那确实你对生产系统中的问题思考太少。

> 3、面试题剖析

就是如果你用redis缓存技术的话，肯定要考虑如何用redis来加多台机器，保证redis是高并发的，还有就是如何让Redis保证自己不是挂掉以后就直接死掉了，redis高可用

我这里会选用我之前讲解过这一块内容，redis高并发、高可用、缓存一致性

> 4、总结[非集群模式]

redis高并发：主从架构，一主多从，一般来说，很多项目其实就足够了，单主用来写入数据，单机几万QPS，多从用来查询数据，多个从实例可以提供每秒10万的QPS。

redis高并发的同时，还需要容纳大量的数据：一主多从，每个实例都容纳了完整的数据，比如redis主就10G的内存量，其实你就最对只能容纳10g的数据量。如果你的缓存要容纳的数据量很大，达到了几十g，甚至几百g，或者是几t，那你就需要redis集群，而且用redis集群之后，可以提供可能每秒几十万的读写并发。

redis高可用：如果你做主从架构部署，其实就是加上哨兵就可以了，就可以实现，任何一个实例宕机，自动会进行主备切换。


## 02_redis如何通过读写分离来承载读请求QPS超过10万+？


![redis主从实现读写分离支撑10万+的高并发](../../pic/2019-06-15-23-57-46.png)


> 1、redis高并发跟整个系统的高并发之间的关系

redis，你要搞高并发的话，不可避免，要把底层的缓存搞得很好

mysql，高并发，做到了，那么也是通过一系列复杂的分库分表，订单系统，事务要求的，QPS到几万，比较高了

要做一些电商的商品详情页，真正的超高并发，QPS上十万，甚至是百万，一秒钟百万的请求量

光是redis是不够的，但是redis是整个大型的缓存架构中，支撑高并发的架构里面，非常重要的一个环节

首先，你的底层的缓存中间件，缓存系统，必须能够支撑的起我们说的那种高并发，
其次，再经过良好的整体的缓存架构的设计（多级缓存架构、热点缓存），支撑真正的上十万，甚至上百万的高并发

> 2、redis不能支撑高并发的瓶颈在哪里？

单机

![redis单机的瓶颈](../../pic/2019-06-15-23-57-20.png)

> 3、如果redis要支撑超过10万+的并发，那应该怎么做？

单机的redis几乎不太可能说QPS超过10万+，除非一些特殊情况，比如你的机器性能特别好，配置特别高，物理机，维护做的特别好，而且你的整体的操作不是太复杂

单机在几万

读写分离，一般来说，对缓存，一般都是用来支撑读高并发的，写的请求是比较少的，可能写请求也就一秒钟几千，一两千

大量的请求都是读，一秒钟二十万次读

读写分离

主从架构 -> 读写分离 -> 支撑10万+读QPS的架构

> 4、接下来要讲解的一个topic

redis replication

redis主从架构 -> 读写分离架构 -> 可支持水平扩展的读高并发架构


## 03_redis replication以及master持久化对主从架构的安全意义

[从主节点同步数据到从节点]

> 课程大纲

- 1、图解redis replication基本原理

- 2、redis replication的核心机制

- 3、master持久化对于主从架构的安全保障的意义


redis replication -> 主从架构 -> 读写分离 -> 水平扩容支撑读高并发

redis replication的最最基本的原理，铺垫



>> 1、图解redis replication基本原理

![](../../pic/2019-06-16-00-15-55.png)

>> 2、redis replication的核心机制

- （1）redis采用异步方式复制数据到slave节点，不过redis 2.8开始，slave node会周期性地确认自己每次复制的数据量

- （2）一个master node是可以配置多个slave node的

- （3）slave node也可以连接其他的slave node

- （4）slave node做复制的时候，是不会block master node的正常工作的

- （5）slave node在做复制的时候，也不会block对自己的查询操作，它会用旧的数据集来提供服务; 但是复制完成的时候，需要删除旧数据集，加载新数据集，这个时候就会暂停对外服务了

- （6）slave node主要用来进行横向扩容，做读写分离，扩容的slave node可以提高读的吞吐量

slave节点，高可用性，有很大的关系



>> 3、master持久化对于主从架构的安全保障的意义

如果采用了主从架构，那么建议必须开启master node的持久化！

不建议用slave node作为master node的数据热备，因为那样的话，如果你关掉master的持久化，可能在master宕机重启的时候数据是空的，然后可能一经过复制，salve node数据也丢了

master -> RDB和AOF都关闭了 -> 全部在内存中

master宕机，重启，是没有本地数据可以恢复的，然后就会直接认为自己IDE数据是空的

master就会将空的数据集同步到slave上去，所有slave的数据全部清空

100%的数据丢失

[master节点，必须要使用持久化机制]

第二个，master的各种备份方案，要不要做，万一说本地的所有文件丢失了; 从备份中挑选一份rdb去恢复master; 这样才能确保master启动的时候，是有数据的

即使采用了后续讲解的高可用机制，slave node可以自动接管master node，但是也可能sentinal还没有检测到master failure，master node就自动重启了，还是可能导致上面的所有slave node数据清空故障



## 04_redis主从复制原理、断点续传、无磁盘化复制、过期key处理

![redis replica最最基本的原理](../../pic/2019-06-16-00-01-56.png)

![redis主从复制的原理](../../pic/2019-06-16-00-02-20.png)


> 课程大纲

>> 1、主从架构的核心原理

当启动一个slave node的时候，它会发送一个PSYNC命令给master node

如果这是slave node重新连接master node，那么master node仅仅会复制给slave部分缺少的数据; 否则如果是slave node第一次连接master node，那么会触发一次full resynchronization

开始full resynchronization的时候，master会启动一个后台线程，开始生成一份RDB快照文件，同时还会将从客户端收到的所有写命令缓存在内存中。RDB文件生成完毕之后，master会将这个RDB发送给slave，slave会先写入本地磁盘，然后再从本地磁盘加载到内存中。然后master会将内存中缓存的写命令发送给slave，slave也会同步这些数据。

slave node如果跟master node有网络故障，断开了连接，会自动重连。master如果发现有多个slave node都来重新连接，仅仅会启动一个rdb save操作，用一份数据服务所有slave node。

>> 2、主从复制的断点续传

从redis 2.8开始，就支持主从复制的断点续传，如果主从复制过程中，网络连接断掉了，那么可以接着上次复制的地方，继续复制下去，而不是从头开始复制一份

master node会在内存中常见一个backlog，master和slave都会保存一个replica offset还有一个master id，offset就是保存在backlog中的。如果master和slave网络连接断掉了，slave会让master从上次的replica offset开始继续复制

但是如果没有找到对应的offset，那么就会执行一次resynchronization

>> 3、无磁盘化复制

master在内存中直接创建rdb，然后发送给slave，不会在自己本地落地磁盘了

repl-diskless-sync

repl-diskless-sync-delay，等待一定时长再开始复制，因为要等更多slave重新连接过来

>> 4、过期key处理

slave不会过期key，只会等待master过期key。如果master过期了一个key，或者通过LRU淘汰了一个key，那么会模拟一条del命令发送给slave。


## 05_redis replication的完整流运行程和原理的再次深入剖析



![复制的完整的基本流程](../../pic/2019-06-16-00-05-44.png)


> 1、复制的完整流程

- （1）slave node启动，仅仅保存master node的信息，包括master node的host和ip，但是复制流程没开始,master host和ip是从哪儿来的，redis.conf里面的slaveof配置的

- （2）slave node内部有个定时任务，每秒检查是否有新的master node要连接和复制，如果发现，就跟master node建立socket网络连接

- （3）slave node发送ping命令给master node

- （4）口令认证，如果master设置了requirepass，那么salve node必须发送masterauth的口令过去进行认证

- （5）master node第一次执行全量复制，将所有数据发给slave node

- （6）master node后续持续将写命令，异步复制给slave node

> 2、数据同步相关的核心机制

指的就是第一次slave连接msater的时候，执行的全量复制，那个过程里面你的一些细节的机制

>> （1）master和slave都会维护一个offset

master会在自身不断累加offset，slave也会在自身不断累加offset

slave每秒都会上报自己的offset给master，同时master也会保存每个slave的offset

这个倒不是说特定就用在全量复制的，主要是master和slave都要知道各自的数据的offset，才能知道互相之间的数据不一致的情况

>> （2）backlog[存放的是什么数据？]

master node有一个backlog，默认是1MB大小

master node给slave node复制数据时，也会将数据在backlog中同步写一份

backlog主要是用来做全量复制中断候的增量复制的

>> （3）master run id

![](../../pic/2019-06-16-09-52-19.png)

![maste run id的作用](../../pic/2019-06-16-00-05-23.png)

info server，可以看到master run id

如果根据host+ip定位master node，是不靠谱的，如果master node重启或者数据出现了变化，那么slave node应该根据不同的run id区分，run id不同就做全量复制

如果需要不更改run id重启redis，可以使用redis-cli debug reload命令

>> （4）psync

从节点使用psync从master node进行复制，psync runid offset

master node会根据自身的情况返回响应信息，可能是FULLRESYNC runid offset触发全量复制，可能是CONTINUE触发增量复制

> 3、全量复制

- （1）master执行bgsave，在本地生成一份rdb快照文件

- （2）master node将rdb快照文件发送给salve node，如果rdb复制时间超过60秒（repl-timeout），那么slave node就会认为复制失败，可以适当调节大这个参数

- （3）对于千兆网卡的机器，一般每秒传输100MB，6G文件，很可能超过60s

- （4）master node在生成rdb时，会将所有新的写命令缓存在内存中，在salve node保存了rdb之后，再将新的写命令复制给salve node

- （5）client-output-buffer-limit slave 256MB 64MB 60，如果在复制期间，内存缓冲区持续消耗超过64MB，或者一次性超过256MB，那么停止复制，复制失败(复制期间写并发太高)

- （6）slave node接收到rdb之后，清空自己的旧数据，然后重新加载rdb到自己的内存中，同时基于旧的数据版本对外提供服务

- （7）如果slave node开启了AOF，那么会立即执行BGREWRITEAOF，重写AOF

rdb生成、rdb通过网络拷贝、slave旧数据的清理、slave aof rewrite，很耗费时间

如果复制的数据量在4G~6G之间，那么很可能全量复制时间消耗到1分半到2分钟

> 4、增量复制

- （1）如果全量复制过程中，master-slave网络连接断掉，那么salve重新连接master时，会触发增量复制

- （2）master直接从自己的backlog中获取部分丢失的数据，发送给slave node，默认backlog就是1MB

- （3）msater就是根据slave发送的psync中的offset来从backlog中获取数据的

> 5、heartbeat

主从节点互相都会发送heartbeat信息

master默认每隔10秒发送一次heartbeat，salve node每隔1秒发送一个heartbeat

> 6、异步复制

master每次接收到写命令之后，现在内部写入数据，然后异步发送给slave node


## 06_redis主从架构下如何才能做到百分之99.99的高可用性？

![redis的不可用](../../pic/2019-06-16-00-06-23.png)

![redis基于哨兵的高可用性](../../pic/2019-06-16-00-06-41.png)

![什么是99.99%高可用性](../../pic/2019-06-16-00-07-05.png)

![系统处于不可用是什么意思](../../pic/2019-06-16-00-07-29.png)


> 1、什么是99.99%高可用？

架构上，高可用性，99.99%的高可用性

讲的学术，99.99%，公式，系统可用的时间 / 系统故障的时间，365天，在365天 * 99.99%的时间内，你的系统都是可以哗哗对外提供服务的，那就是高可用性，99.99%

系统可用的时间 / 总的时间 = 高可用性，然后会对各种时间的概念，说一大堆解释

> 2、redis不可用是什么？单实例不可用？主从架构不可用？不可用的后果是什么？

> 3、redis怎么才能做到高可用？




## 07_redis哨兵架构的相关基础知识的讲解


> 1、哨兵的介绍

sentinal，中文名是哨兵

哨兵是redis集群架构中非常重要的一个组件，主要功能如下

- （1）集群监控，负责监控redis master和slave进程是否正常工作
- （2）消息通知，如果某个redis实例有故障，那么哨兵负责发送消息作为报警通知给管理员
- （3）故障转移，如果master node挂掉了，会自动转移到slave node上
- （4）配置中心，如果故障转移发生了，通知client客户端新的master地址

哨兵本身也是分布式的，作为一个哨兵集群去运行，互相协同工作

- （1）故障转移时，判断一个master node是宕机了，需要大部分的哨兵都同意才行，涉及到了分布式选举的问题

- （2）即使部分哨兵节点挂掉了，哨兵集群还是能正常工作的，因为如果一个作为高可用机制重要组成部分的故障转移系统本身是单点的，那就很坑爹了

目前采用的是sentinal 2版本，sentinal 2相对于sentinal 1来说，重写了很多代码，主要是让故障转移的机制和算法变得更加健壮和简单

> 2、哨兵的核心知识

- （1）哨兵至少需要3个实例，来保证自己的健壮性
- （2）哨兵 + redis主从的部署架构，是不会保证数据零丢失的，只能保证redis集群的高可用性[有可能丢失数据，需要其他机制保障]
- （3）对于哨兵 + redis主从这种复杂的部署架构，尽量在测试环境和生产环境，都进行充足的测试和演练

> 3、为什么redis哨兵集群只有2个节点无法正常工作？

[哨兵会部署在redis集群中还是单独部署一个集群？？？]

哨兵集群必须部署2个以上节点

如果哨兵集群仅仅部署了个2个哨兵实例，quorum=1

+----+         +----+
| M1 |---------| R1 |
| S1 |         | S2 |
+----+         +----+

Configuration: quorum = 1

master宕机，s1和s2中只要有1个哨兵认为master宕机就可以还行切换，同时s1和s2中会选举出一个哨兵来执行故障转移

同时这个时候，需要majority，也就是大多数哨兵都是运行的，2个哨兵的majority就是2（2的majority=2，3的majority=2，5的majority=3，4的majority=2），2个哨兵都运行着，就可以允许执行故障转移

但是如果整个M1和S1运行的机器宕机了，那么哨兵只有1个了，此时就没有majority来允许执行故障转移，虽然另外一台机器还有一个R1，但是故障转移不会执行

> 4、经典的3节点哨兵集群

       +----+
       | M1 |
       | S1 |
       +----+
          |
+----+    |    +----+
| R2 |----+----| R3 |
| S2 |         | S3 |
+----+         +----+

备注：上面的M代表redis的master，R代表redis的slave


Configuration: quorum = 2，majority

如果M1所在机器宕机了，那么三个哨兵还剩下2个，S2和S3可以一致认为master宕机，然后选举出一个来执行故障转移

同时3个哨兵的majority是2，所以还剩下的2个哨兵运行着，就可以允许执行故障转移



## 08_redis哨兵主备切换的数据丢失问题：异步复制、集群脑裂

问题：

- 1、脑裂的问题如何恢复正常的？两个master会共存多久？自动的还是手工的？

> 课程大纲

- 1、两种数据丢失的情况
- 2、解决异步复制和脑裂导致的数据丢失

------------------------------------------------------------------

> 1、两种数据丢失的情况

主备切换的过程，可能会导致数据丢失

>> （1）异步复制导致的数据丢失

因为master -> slave的复制是异步的，所以可能有部分数据还没复制到slave，master就宕机了，此时这些部分数据就丢失了


![异步复制导致的数据丢失问题](../../pic/2019-06-16-00-08-52.png)

>> （2）脑裂导致的数据丢失

![集群脑裂导致的数据丢失问题](../../pic/2019-06-16-00-08-12.png)

脑裂，也就是说，某个master所在机器突然脱离了正常的网络，跟其他slave机器不能连接，但是实际上master还运行着

此时哨兵可能就会认为master宕机了，然后开启选举，将其他slave切换成了master

这个时候，集群里就会有两个master，也就是所谓的脑裂

此时虽然某个slave被切换成了master，但是可能client还没来得及切换到新的master，还继续写向旧master的数据可能也丢失了

因此旧master再次恢复的时候，会被作为一个slave挂到新的master上去，自己的数据会清空，重新从新的master复制数据

------------------------------------------------------------------

> 2、解决异步复制和脑裂导致的数据丢失

min-slaves-to-write 1

min-slaves-max-lag 10

参数含义：要求至少有1个slave，数据复制和同步的延迟不能超过10秒

如果说一旦所有的slave，数据复制和同步的延迟都超过了10秒钟，那么这个时候，master就不会再接收任何请求了

上面两个配置可以减少异步复制和脑裂导致的数据丢失

>> （1）减少异步复制的数据丢失

![异步复制导致数据丢失如何降低损失](../../pic/2019-06-16-00-09-12.png)


有了min-slaves-max-lag这个配置，就可以确保说，一旦slave复制数据和ack延时太长，就认为可能master宕机后损失的数据太多了，那么就拒绝写请求，这样可以把master宕机时由于部分数据未同步到slave导致的数据丢失降低的可控范围内

>> （2）减少脑裂的数据丢失


![脑裂导致数据丢失的问题如何降低损失](../../pic/2019-06-16-00-08-32.png)

如果一个master出现了脑裂，跟其他slave丢了连接，那么上面两个配置可以确保说，如果不能继续给指定数量的slave发送数据，而且slave超过10秒没有给自己ack消息，那么就直接拒绝客户端的写请求

这样脑裂后的旧master就不会接受client的新数据，也就避免了数据丢失

上面的配置就确保了，如果跟任何一个slave丢了连接，在10秒后发现没有slave给自己ack，那么就拒绝新的写请求

因此在脑裂场景下，最多就丢失10秒的数据



## 09_redis哨兵的多个核心底层原理的深入解析（包含slave选举算法）


> 1、sdown和odown转换机制

sdown和odown两种失败状态

sdown是主观宕机，就一个哨兵如果自己觉得一个master宕机了，那么就是主观宕机

odown是客观宕机，如果quorum数量的哨兵都觉得一个master宕机了，那么就是客观宕机

sdown达成的条件很简单，如果一个哨兵ping一个master，超过了is-master-down-after-milliseconds指定的毫秒数之后，就主观认为master宕机

sdown到odown转换的条件很简单，如果一个哨兵在指定时间内，收到了quorum指定数量的其他哨兵也认为那个master是sdown了，那么就认为是odown了，客观认为master宕机

> 2、哨兵集群的自动发现机制

哨兵互相之间的发现，是通过redis的pub/sub系统实现的，每个哨兵都会往__sentinel__:hello这个channel里发送一个消息，这时候所有其他哨兵都可以消费到这个消息，并感知到其他的哨兵的存在

每隔两秒钟，每个哨兵都会往自己监控的某个master+slaves对应的__sentinel__:hello channel里发送一个消息，内容是自己的host、ip和runid还有对这个master的监控配置

每个哨兵也会去监听自己监控的每个master+slaves对应的__sentinel__:hello channel，然后去感知到同样在监听这个master+slaves的其他哨兵的存在

每个哨兵还会跟其他哨兵交换对master的监控配置，互相进行监控配置的同步

> 3、slave配置的自动纠正

哨兵会负责自动纠正slave的一些配置，比如slave如果要成为潜在的master候选人，哨兵会确保slave在复制现有master的数据; 如果slave连接到了一个错误的master上，比如故障转移之后，那么哨兵会确保它们连接到正确的master上

> 4、slave->master选举算法

如果一个master被认为odown了，而且majority哨兵都允许了主备切换，那么某个哨兵就会执行主备切换操作，此时首先要选举一个slave来

会考虑slave的一些信息

- （1）跟master断开连接的时长
- （2）slave优先级
- （3）复制offset
- （4）run id

如果一个slave跟master断开连接已经超过了down-after-milliseconds的10倍，外加master宕机的时长，那么slave就被认为不适合选举为master

(down-after-milliseconds * 10) + milliseconds_since_master_is_in_SDOWN_state

接下来会对slave进行排序

- （1）按照slave优先级进行排序，slave priority越低，优先级就越高
- （2）如果slave priority相同，那么看replica offset，哪个slave复制了越多的数据，offset越靠后，优先级就越高
- （3）如果上面两个条件都相同，那么选择一个run id比较小的那个slave

> 5、quorum和majority

每次一个哨兵要做主备切换，首先需要quorum数量的哨兵认为odown，然后选举出一个哨兵来做切换，这个哨兵还得得到majority哨兵的授权，才能正式执行切换

如果quorum < majority，比如5个哨兵，majority就是3，quorum设置为2，那么就3个哨兵授权就可以执行切换

但是如果quorum >= majority，那么必须quorum数量的哨兵都授权，比如5个哨兵，quorum是5，那么必须5个哨兵都同意授权，才能执行切换

> 6、configuration epoch

哨兵会对一套redis master+slave进行监控，有相应的监控的配置

执行切换的那个哨兵，会从要切换到的新master（salve->master）那里得到一个configuration epoch，这就是一个version号，每次切换的version号都必须是唯一的

如果第一个选举出的哨兵切换失败了，那么其他哨兵，会等待failover-timeout时间，然后接替继续执行切换，此时会重新获取一个新的configuration epoch，作为新的version号

> 7、configuraiton传播

哨兵完成切换之后，会在自己本地更新生成最新的master配置，然后同步给其他的哨兵，就是通过之前说的pub/sub消息机制

这里之前的version号就很重要了，因为各种消息都是通过一个channel去发布和监听的，所以一个哨兵完成一次新的切换之后，新的master配置是跟着新的version号的

其他的哨兵都是根据版本号的大小来更新自己的master配置的