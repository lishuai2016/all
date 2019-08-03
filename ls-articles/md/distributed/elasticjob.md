---
title: Elastic-Job
categories: 
- 分布式
tags:
---

# 概述
Elastic-Job是一个分布式调度解决方案，由两个相互独立的子项目Elastic-Job-Lite和Elastic-Job-Cloud组成。
Elastic-Job-Lite定位为轻量级无中心化解决方案，使用jar包的形式提供分布式任务的协调服务。
Elastic-Job-Cloud使用Mesos + Docker的解决方案，额外提供资源治理、应用分发以及进程隔离等服务。

Elastic-Job is a distributed scheduled job framework, based on Quartz and Zookeeper.
Elastic-Job是一个分布式调度框架，基于Quartz and Zookeeper

**标签**
- distributed
- scheduled-jobs
- cron
- job
- job-management
- sharding
- quartz
- zookeeper
- registry

# 功能列表

## Elastic-Job-Lite
- 分布式调度协调
- 弹性扩容缩容
- 失效转移
- 错过执行作业重触发
- 作业分片一致性，保证同一分片在分布式环境中仅一个执行实例
- 自诊断并修复分布式不稳定造成的问题
- 支持并行调度
- 支持作业生命周期操作
- 丰富的作业类型
- Spring整合以及命名空间提供
- 运维平台

## Elastic-Job-Cloud
- 应用自动分发
- 基于Fenzo的弹性资源分配
- 分布式调度协调
- 弹性扩容缩容
- 失效转移
- 错过执行作业重触发
- 作业分片一致性，保证同一分片在分布式环境中仅一个执行实例
- 支持并行调度
- 支持作业生命周期操作
- 丰富的作业类型
- Spring整合
- 运维平台
- 基于Docker的进程隔离(TBD)

# elastic-job的原理简介
简单来说，就是根据规则，在多节点部署环境下执行定时任务时，可以借助zookeeper来实现把一个大的定时任务拆分成多个子任务[分片]，在各个节点执行。来达到合理利用集群资源提高效率

## 解决的问题
elastic-job是当当开源的一款非常好用的作业框架，在这之前，我们开发定时任务一般都是使用quartz或者spring-task（ScheduledExecutorService），无论是使用quartz还是spring-task，我们都会至少遇到两个痛点：
1.不敢轻易跟着应用服务多节点部署，可能会重复多次执行而引发系统逻辑的错误。
2.quartz的集群仅仅只是用来HA，节点数量的增加并不能给我们的每次执行效率带来提升，即不能实现水平扩展。

## elastic-job-lite原理
举个典型的job场景，比如余额宝里的昨日收益，系统需要job在每天某个时间点开始，给所有余额宝用户计算收益。如果用户数量不多，我们可以轻易使用quartz来完成，我们让计息job在某个时间点开始执行，循环遍历所有用户计算利息，这没问题。可是，如果用户体量特别大，
我们可能会面临着在第二天之前处理不完这么多用户。另外，我们部署job的时候也得注意，我们可能会把job直接放在我们的webapp里，
webapp通常是多节点部署的，这样，我们的job也就是多节点，多个job同时执行，很容易造成重复执行，比如用户重复计息，为了避免这种情况，
我们可能会对job的执行加锁，保证始终只有一个节点能执行，或者干脆让job从webapp里剥离出来，独自部署一个节点。
elastic-job就可以帮助我们解决上面的问题，elastic底层的任务调度还是使用的quartz，通过zookeeper来动态给job节点分片。
我们来看：
### 很大体量的用户需要在特定的时间段内计息完成
我们肯定是希望我们的任务可以通过集群达到水平扩展，集群里的每个节点都处理部分用户，不管用户数量有多庞大，我们只要增加机器就可以了，
比如单台机器特定时间能处理n个用户，2台机器处理2n个用户，3台3n，4台4n...，再多的用户也不怕了。
使用elastic-job开发的作业都是zookeeper的客户端，比如我希望3台机器跑job，我们将任务分成3片，框架通过zk的协调，
最终会让3台机器分别分配到0,1,2的任务片，比如server0-->0，server1-->1，server2-->2，当server0执行时，
可以只查询id%3==0的用户，server1执行时，只查询id%3==1的用户，server2执行时，只查询id%3==2的用户。
### 任务部署多节点引发重复执行
在上面的基础上，我们再增加server3，此时，server3分不到任务分片，因为只有3片，已经分完了。没有分到任务分片的作业程序将不执行。
如果此时server2挂了，那么server2的分片项会分配给server3，server3有了分片，就会替代server2执行。
如果此时server3也挂了，只剩下server0和server1了，框架也会自动把server3的分片随机分配给server0或者server1，可能会这样，server0-->0，server1-->1,2。
这种特性称之为弹性扩容，即elastic-job名称的由来。



# 在springboot下的简单使用
## 配置文件application.properties
```
server.port=${random.int[10000,19999]}
regCenter.serverList = localhost:2181
regCenter.namespace = elastic-job-lite-springboot
# 每隔5秒钟执行一次
stockJob.cron = 0/5 * * * * ?
stockJob.shardingTotalCount = 4
# 任务执行四次
stockJob.shardingItemParameters = 0=0,1=1,2=0,3=1
```

## zookeeper配置
```java
@Configuration
public class ElasticRegCenterConfig {
    /**
     * 配置zookeeper
     * @param serverList 注册中心
     * @param namespace 应用的命名空间
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(
            @Value("${regCenter.serverList}") final String serverList,
            @Value("${regCenter.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }
}
```

## elastic-job配置
```java
@Configuration
public class ElasticJobConfig {
    @Autowired
    private ZookeeperRegistryCenter regCenter;
    /**
     * 配置任务监听器
     * @return
     */
    @Bean
    public ElasticJobListener elasticJobListener() {
        return new MyElasticJobListener();
    }
    /**
     * 配置任务详细信息
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     * 1、JobCoreConfiguration 定义作业核心配置
     * 2、SimpleJobConfiguration 定义SIMPLE类型配置
     * 3、LiteJobConfiguration  定义Lite作业根配置
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters).build()
                , jobClass.getCanonicalName())
        ).overwrite(true).build();
    }
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final SimpleJobDemo simpleJob,
                                           @Value("${stockJob.cron}") final String cron,
                                           @Value("${stockJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${stockJob.shardingItemParameters}") final String shardingItemParameters) {
        MyElasticJobListener elasticJobListener = new MyElasticJobListener();
        return new SpringJobScheduler(simpleJob, regCenter,
                getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters),
                elasticJobListener);
    }
}

```
## 在任务的执行过程添加监听器，类似拦器的功能
```java
public class MyElasticJobListener implements ElasticJobListener {
    private static final Logger logger = LoggerFactory.getLogger(MyElasticJobListener.class);

    private long beginTime = 0;
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        beginTime = System.currentTimeMillis();

        logger.info("===>{} JOB BEGIN TIME: {} <===",shardingContexts.getJobName(), beginTime);
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        long endTime = System.currentTimeMillis();
        logger.info("===>{} JOB END TIME: {},TOTAL CAST: {} <===",shardingContexts.getJobName(), endTime, endTime - beginTime);
    }
}

```

## 执行的任务逻辑
```java
@Component
public class SimpleJobDemo implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(String.format("------Thread ID: %s, 任務總片數: %s, " +
                        "当前分片項: %s.当前參數: %s," +
                        "当前任務名稱: %s.当前任務參數: %s"
                ,
                Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()

        ));
    }
}

```
## 输出
```
2019-01-07 23:44:55.013  INFO 2196 --- [obDemo_Worker-1] c.l.e.job.listener.MyElasticJobListener  : ===>com.ls.elastic.job.task.SimpleJobDemo JOB BEGIN TIME: 1546875895013 <===
------Thread ID: 45, 任務總片數: 4, 当前分片項: 0.当前參數: 0,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
------Thread ID: 46, 任務總片數: 4, 当前分片項: 1.当前參數: 1,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
------Thread ID: 46, 任務總片數: 4, 当前分片項: 2.当前參數: 0,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
------Thread ID: 47, 任務總片數: 4, 当前分片項: 3.当前參數: 1,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
2019-01-07 23:44:55.416  INFO 2196 --- [obDemo_Worker-1] c.l.e.job.listener.MyElasticJobListener  : ===>com.ls.elastic.job.task.SimpleJobDemo JOB END TIME: 1546875895416,TOTAL CAST: 403 <===
2019-01-07 23:45:00.015  INFO 2196 --- [obDemo_Worker-1] c.l.e.job.listener.MyElasticJobListener  : ===>com.ls.elastic.job.task.SimpleJobDemo JOB BEGIN TIME: 1546875900015 <===
------Thread ID: 51, 任務總片數: 4, 当前分片項: 0.当前參數: 0,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
------Thread ID: 51, 任務總片數: 4, 当前分片項: 1.当前參數: 1,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
------Thread ID: 51, 任務總片數: 4, 当前分片項: 2.当前參數: 0,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
------Thread ID: 51, 任務總片數: 4, 当前分片項: 3.当前參數: 1,当前任務名稱: com.ls.elastic.job.task.SimpleJobDemo.当前任務參數: 
2019-01-07 23:45:00.367  INFO 2196 --- [obDemo_Worker-1] c.l.e.job.listener.MyElasticJobListener  : ===>com.ls.elastic.job.task.SimpleJobDemo JOB END TIME: 1546875900367,TOTAL CAST: 352 <===

Process finished with exit code 1
```


# 监控elastic-job-lite-console使用
http://localhost:8899/  
**功能列表**
- 登录安全控制
- 注册中心、事件追踪数据源管理
- 快捷修改作业设置
- 作业和服务器维度状态查看
- 操作作业禁用\启用、停止和删除等生命周期
- 事件追踪查询

## 启动服务
1、可以通过下面的链接下载压缩包，
https://github.com/miguangying/elastic-job-lite-console#elastic-job-lite-console
2、通过源码构建[https://gitee.com/elasticjob/elastic-job](https://gitee.com/elasticjob/elastic-job) 
解压缩elastic-job-lite-console-${version}.tar.gz并执行bin\start.sh。打开浏览器访问http://localhost:8899/即可访问控制台。
8899为默认端口号，可通过启动脚本输入-p自定义端口号。
elastic-job-lite-console-${version}.tar.gz可通过mvn install编译获取。
3、下载源码，导入idea，直接在idea中执行elastic-job-lite-console模块的主类ConsoleBootstrap

## 登录
提供两种账户，管理员及访客，管理员拥有全部操作权限，访客仅拥有察看权限。默认管理员用户名和密码是root/root，
访客用户名和密码是guest/guest，可通过conf\auth.properties修改管理员及访客用户名及密码。

## 配置zookeeper信息
elastic-job的运维平台设计理念是：
1）运维平台和elastic-job-lite并无直接关系，是通过读取作业注册中心数据展现作业状态，或更新注册中心数据修改全局配置。
2）控制台只能控制作业本身是否运行，但不能控制作业进程的启动，因为控制台和作业本身服务器是完全分离的，控制台并不能控制作业服务器。

所以运维平台配置zookeeper的信息才是关键，只有连通了zookeeper才能对定时任务进行操作，操作步骤：
1）左边菜单点击【全局配置】选中【注册中心配置】，然后出现一个已配置列表，在列表的分页显示下方有个【添加】按钮进行添加
2）添加字段说明
- 注册中心名称：自定义，用于当前列表显示，便于区分
- 注册中心地址：zookeeper的地址，需要连接哪个就填写哪个 【IP:端口】
- 命名空间：任务创建ZookeeperRegistryCenter的时候填写namespace，要对应上，才能看到对应下的任务
- 登录凭证：可不填，默认zookeeper不需要填写，除非设置了zookeeper相关信息
3）填写完之后，点击右下角【提交】按钮，即完成
4）列表中出现新增的注册配置，然后点击该配置最后的操作项，【连接】，则连接上zookeeper
![](/images/elastic-job-0.png)
![](/images/elastic-job-1.png)


## 作业操作
1）作业维度
查看当前挂在zookeeper的命名空间下的所有任务，提供删除，编辑，触发，失效等一系列功能
2） 服务器维度
查看当前连着zookeeper的服务器，提供删除，失效，终止等一些列功能
点击【详情】可以看到该服务器下的正在运行的定时任务,当点击【终止】时，定时任务将会被停止
![](/images/elastic-job-2.png)
![](/images/elastic-job-3.png)








# 目录结构说明
elastic-job
    ├──elastic-job-lite                                 lite父模块，不应直接使用
    ├      ├──elastic-job-lite-core                     Java支持模块，可直接使用
    ├      ├──elastic-job-lite-spring                   Spring命名空间支持模块，可直接使用
    ├      ├──elastic-job-lite-lifecyle                 lite作业相关操作模块，不可直接使用
    ├      ├──elastic-job-lite-console                  lite界面模块，可直接使用
    ├──elastic-job-example                              使用示例
    ├      ├──elastic-job-example-embed-zk              供示例使用的内嵌ZK模块
    ├      ├──elastic-job-example-jobs                  作业示例
    ├      ├──elastic-job-example-lite-java             基于Java的使用示例
    ├      ├──elastic-job-example-lite-spring           基于Spring的使用示例
    ├      ├──elastic-job-example-lite-springboot       基于SpringBoot的使用示例
    ├──elastic-job-doc                                  markdown生成文档的项目，使用方无需关注
    ├      ├──elastic-job-lite-doc                      lite相关文档


# 实现原理
## 弹性分布式实现
- 第一台服务器上线触发主服务器选举。主服务器一旦下线，则重新触发选举，选举过程中阻塞，只有主服务器选举完成，才会执行其他任务。

- 某作业服务器上线时会自动将服务器信息注册到注册中心，下线时会自动更新服务器状态。

- 主节点选举，服务器上下线，分片总数变更均更新重新分片标记。

- 定时任务触发时，如需重新分片，则通过主服务器分片，分片过程中阻塞，分片结束后才可执行任务。如分片过程中主服务器下线，则先选举主服务器，再分片。

- 通过上一项说明可知，为了维持作业运行时的稳定性，运行过程中只会标记分片状态，不会重新分片。分片仅可能发生在下次任务触发前。

- 每次分片都会按服务器IP排序，保证分片结果不会产生较大波动。

- 实现失效转移功能，在某台服务器执行完毕后主动抓取未分配的分片，并且在某台服务器下线后主动寻找可用的服务器执行任务。


## zookeeper中的数据存储结构
zookeeper可视化工具ZooInspector
java -jar zookeeper-dev-ZooInspector.jar
![](/images/elastic-zk-0.png)
![](/images/elastic-zk-1.png)

注册中心在定义的命名空间下，创建作业名称节点，用于区分不同作业，所以作业一旦创建则不能修改作业名称，如果修改名称将视为新的作业。作业名称节点下又包含4个数据子节点，分别是config, instances, sharding, servers和leader。

### config节点
作业配置信息，以JSON格式存储

### instances节点
作业运行实例信息，子节点是当前作业运行实例的主键。作业运行实例主键由作业运行服务器的IP地址和PID构成。作业运行实例主键均为临时节点，当作业实例上线时注册，下线时自动清理。注册中心监控这些节点的变化来协调分布式作业的分片以及高可用。 可在作业运行实例节点写入TRIGGER表示该实例立即执行一次。

### sharding节点
作业分片信息，子节点是分片项序号，从零开始，至分片总数减一。分片项序号的子节点存储详细信息。每个分片项下的子节点用于控制和记录分片运行状态。节点详细信息说明：

子节点名	临时节点	描述
instance	否	执行该分片项的作业运行实例主键
running	是	分片项正在运行的状态
仅配置monitorExecution时有效
failover	是	如果该分片项被失效转移分配给其他作业服务器，则此节点值记录执行此分片的作业服务器IP
misfire	否	是否开启错过任务重新执行
disabled	否	是否禁用此分片项

### servers节点
作业服务器信息，子节点是作业服务器的IP地址。可在IP地址节点写入DISABLED表示该服务器禁用。 在新的cloud native架构下，servers节点大幅弱化，仅包含控制服务器是否可以禁用这一功能。为了更加纯粹的实现job核心，servers功能未来可能删除，控制服务器是否禁用的能力应该下放至自动化部署系统。

### leader节点
作业服务器主节点信息，分为election，sharding和failover三个子节点。分别用于主节点选举，分片和失效转移处理。

leader节点是内部使用的节点，如果对作业框架原理不感兴趣，可不关注此节点。

子节点名	临时节点	描述
election\instance	是	主节点服务器IP地址
一旦该节点被删除将会触发重新选举
重新选举的过程中一切主节点相关的操作都将阻塞
election\latch	否	主节点选举的分布式锁
为curator的分布式锁使用
sharding\necessary	否	是否需要重新分片的标记
如果分片总数变化，或作业服务器节点上下线或启用/禁用，以及主节点选举，会触发设置重分片标记
作业在下次执行时使用主节点重新分片，且中间不会被打断
作业执行时不会触发分片
sharding\processing	是	主节点在分片时持有的节点
如果有此节点，所有的作业执行都将阻塞，直至分片结束
主节点分片结束或主节点崩溃会删除此临时节点
failover\items\分片项	否	一旦有作业崩溃，则会向此节点记录
当有空闲作业服务器时，会从此节点抓取需失效转移的作业项
failover\items\latch	否	分配失效转移分片项时占用的分布式锁
为curator的分布式锁使用



# 流程图
## 作业启动
![](/images/elastic-流程图启动.jpg)


## 作业执行
![](/images/elastic-流程图作业执行.jpg)





# 参考文档
elastic-job-lite的官方文档
http://elasticjob.io/docs/elastic-job-lite/00-overview/intro/

https://github.com/elasticjob
https://github.com/elasticjob/elastic-job-lite
https://gitee.com/elasticjob/elastic-job
https://github.com/liuzhongkai/spider

https://blog.csdn.net/fanfan_v5/article/details/61310045
https://blog.csdn.net/LOVELONG8808/article/details/80393290


