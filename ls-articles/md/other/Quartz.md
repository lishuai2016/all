---
title: Quartz
categories: 
- Quartz
tags:
---


[参考](https://yq.aliyun.com/articles/29122)

https://blog.csdn.net/beliefer/article/details/51578546
https://www.cnblogs.com/nick-huang/p/4861612.html

目录：
Quartz任务调度(3)存储与持久化操作配置详细解析
一、内存存储RAMJobStore（默认org.quartz.simple.RAMJobStore）
二、持久性JobStore[]
三、持久化配置步骤
1. 配置数据库
2. 使用JobStoreTX
3、测试
4、扩展测试
5、恢复异常中断的任务
四、springboot+quartz+jpa的配置文件



# 一、内存存储RAMJobStore
Quartz默认使用RAMJobStore，它的优点是速度。因为所有的 Scheduler 信息都保存在计算机内存中，访问这些数据随着电脑而变快。
而无须访问数据库或IO等操作，但它的缺点是将 Job 和 Trigger 信息存储在内存中的。因而我们每次重启程序，Scheduler 的状态，包括 Job 和 Trigger 信息都丢失了。 
Quartz 的内存 Job 存储的能力是由一个叫做 org.quartz.simple.RAMJobStore 类提供。
在我们的quartz-2.x.x.jar包下的org.quartz包下即存储了我们的默认配置quartz.properties。打开这个配置文件，我们会看到如下信息

    # Default Properties file for use by StdSchedulerFactory
    # to create a Quartz Scheduler Instance, if a different
    # properties file is not explicitly specified.
    #
    
    org.quartz.scheduler.instanceName: DefaultQuartzScheduler
    org.quartz.scheduler.rmi.export: false
    org.quartz.scheduler.rmi.proxy: false
    org.quartz.scheduler.wrapJobExecutionInUserTransaction: false
    
    org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
    org.quartz.threadPool.threadCount: 10
    org.quartz.threadPool.threadPriority: 5
    org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread: true
    
    org.quartz.jobStore.misfireThreshold: 60000
    org.quartz.jobStore.class: org.quartz.simpl.RAMJobStore #这里默认使用RAMJobStore
    
# 二、持久性JobStore
Quartz 提供了两种类型的持久性 JobStore，为[JobStoreTX和JobStoreCMT]，其中： 
1. JobStoreTX为独立环境中的持久性存储，它设计为用于独立环境中。这里的 “独立”，我们是指这样一个环境，在其中不存在与应用容器的事物集成。
这里并不意味着你不能在一个容器中使用 JobStoreTX，只不过，它不是设计来让它的事务受容器管理。区别就在于 Quartz 的事物是否要参与到容器的事物中去。 
2. JobStoreCMT 为程序容器中的持久性存储，它设计为当你想要程序容器来为你的 JobStore 管理事物时，并且那些事物要参与到容器管理的事物边界时使用。
它的名字明显是来源于容器管理的事物(Container Managed Transactions (CMT))。

# 三、持久化配置步骤
要将JobDetail等信息持久化我们的数据库中，我们可按一下步骤操作：

## 1. 配置数据库
在 /docs/dbTables 目录下存放了几乎所有数据库的的SQL脚本，这里的 是解压 Quartz 分发包后的目录。我们使用常用mysql数据库，下面是示例sql脚本代码

#
# Quartz seems to work best with the driver mm.mysql-2.0.7-bin.jar
#
# PLEASE consider using mysql with innodb tables to avoid locking issues
#
# In your Quartz properties file, you'll need to set 
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    JOB_NAME  VARCHAR(100) NOT NULL,
    JOB_GROUP VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(100) NULL,
    JOB_CLASS_NAME   VARCHAR(100) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    JOB_NAME  VARCHAR(100) NOT NULL,
    JOB_GROUP VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(100) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(100) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    CRON_EXPRESSION VARCHAR(100) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (          
    SCHED_NAME VARCHAR(80) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    STR_PROP_1 VARCHAR(120) NULL,
    STR_PROP_2 VARCHAR(120) NULL,
    STR_PROP_3 VARCHAR(120) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    CALENDAR_NAME  VARCHAR(100) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP  VARCHAR(100) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    INSTANCE_NAME VARCHAR(100) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(100) NULL,
    JOB_GROUP VARCHAR(100) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    INSTANCE_NAME VARCHAR(100) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(80) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

commit;

其中各表的含义如下所示：
表名	                        描述
QRTZ_CALENDARS	                以 Blob 类型存储 Quartz 的 Calendar 信息
QRTZ_CRON_TRIGGERS	            存储 Cron Trigger，包括 Cron 表达式和时区信息[重要]
QRTZ_FIRED_TRIGGERS	            存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息[记录本次触发和下次触发的时间]
QRTZ_PAUSED_TRIGGER_GRPS	    存储已暂停的 Trigger 组的信息
QRTZ_SCHEDULER_STATE	        存储少量的有关 Scheduler 的状态信息，和别的 Scheduler 实例(假如是用于一个集群中)
QRTZ_LOCKS	                    存储程序的非观锁的信息(假如使用了悲观锁)
QRTZ_JOB_DETAILS	            存储每一个已配置的 Job 的详细信息[重要]
QRTZ_JOB_LISTENERS	            存储有关已配置的 JobListener 的信息
QRTZ_SIMPLE_TRIGGERS	        存储简单的 Trigger，包括重复次数，间隔，以及已触的次数
QRTZ_BLOG_TRIGGERS      	    作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
QRTZ_TRIGGER_LISTENERS	        存储已配置的 TriggerListener 的信息
QRTZ_TRIGGERS	                存储已配置的 Trigger 的信息[重要]

## 2. 使用JobStoreTX
首先，我们需要在我们的属性文件中表明使用JobStoreTX： 
org.quartz.jobStore.class = org.quartz.ompl.jdbcjobstore.JobStoreTX
然后我们需要配置能理解不同数据库系统中某一特定方言的驱动代理：
数据库平台	                Quartz 代理类
Cloudscape/Derby	        org.quartz.impl.jdbcjobstore.CloudscapeDelegate
DB2 (version 6.x)	        org.quartz.impl.jdbcjobstore.DB2v6Delegate
DB2 (version 7.x)	        org.quartz.impl.jdbcjobstore.DB2v7Delegate
DB2 (version 8.x)	        org.quartz.impl.jdbcjobstore.DB2v8Delegate
HSQLDB	                    org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
MS SQL Server	            org.quartz.impl.jdbcjobstore.MSSQLDelegate
Pointbase	                org.quartz.impl.jdbcjobstore.PointbaseDelegate
PostgreSQL	                org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
(WebLogic JDBC Driver)	    org.quartz.impl.jdbcjobstore.WebLogicDelegate
(WebLogic 8.1 with Oracle)	org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate
Oracle	                    org.quartz.impl.jdbcjobstore.oracle.OracleDelegate
如果我们的数据库平台没在上面列出，那么最好的选择就是，直接使用标准的 JDBC 代理 [org.quartz.impl.jdbcjobstore.StdDriverDelegate] 就能正常的工作。

以下是一些相关常用的配置属性及其说明：
属性	                                            默认值	    描述
org.quartz.jobStore.dataSource	                    无	        用于 quartz.properties 中数据源的名称
org.quartz.jobStore.tablePrefix	                    QRTZ_	    指定用于 Scheduler 的一套数据库表名的前缀。假如有不同的前缀，Scheduler 就能在同一数据库中使用不同的表。
org.quartz.jobStore.userProperties	                False	    “use properties” 标记指示着持久性 JobStore 所有在 JobDataMap 中的值都是字符串，因此能以 名-值 对的形式存储，而不用让更复杂的对象以序列化的形式存入 BLOB 列中。这样会更方便，因为让你避免了发生于序列化你的非字符串的类到 BLOB 时的有关类版本的问题。
org.quartz.jobStore.misfireThreshold	            60000	    在 Trigger 被认为是错过触发之前，Scheduler 还容许 Trigger 通过它的下次触发时间的毫秒数。默认值(假如你未在配置中存在这一属性条目) 是 60000(60 秒)。这个不仅限于 JDBC-JobStore；它也可作为 RAMJobStore 的参数
org.quartz.jobStore.isClustered	                    False	    设置为 true 打开集群特性。如果你有多个 Quartz 实例在用同一套数据库时，这个属性就必须设置为 true。
org.quartz.jobStore.clusterCheckinInterval	        15000	    设置一个频度(毫秒)，用于实例报告给集群中的其他实例。这会影响到侦测失败实例的敏捷度。它只用于设置了 isClustered 为 true 的时候。
org.quartz.jobStore.maxMisfiresToHandleAtATime	    20	        这是 JobStore 能处理的错过触发的 Trigger 的最大数量。处理太多(超过两打) 很快会导致数据库表被锁定够长的时间，这样就妨碍了触发别的(还未错过触发) trigger 执行的性能。
org.quartz.jobStore.dontSetAutoCommitFalse	        False	    设置这个参数为 true 会告诉 Quartz 从数据源获取的连接后不要调用它的 setAutoCommit(false) 方法。这在少些情况下是有帮助的，比如假如你有这样一个驱动，它会抱怨本来就是关闭的又来调用这个方法。这个属性默认值是 false，因为大多数的驱动都要求调用 setAutoCommit(false)。
org.quartz.jobStore.selectWithLockSQL	            SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE	这必须是一个从 LOCKS 表查询一行并对这行记录加锁的 SQL 语句。假如未设置，默认值就是 SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE，这能在大部分数据库上工作。{0} 会在运行期间被前面你配置的 TABLE_PREFIX 所替换。
org.quartz.jobStore.txIsolationLevelSerializable	False	    值为 true 时告知 Quartz(当使用 JobStoreTX 或 CMT) 调用 JDBC 连接的 setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE) 方法。这有助于阻止某些数据库在高负载和长时间事物时锁的超时。



我们还需要配置Datasource 属性
属性	                                    必须	说明
org.quartz.dataSource.NAME.driver	        是	    JDBC 驱动类的全限名
org.quartz.dataSource.NAME.URL	            是	    连接到你的数据库的 URL(主机，端口等)
org.quartz.dataSource.NAME.user	            否	    用于连接你的数据库的用户名
org.quartz.dataSource.NAME.password	        否	    用于连接你的数据库的密码
org.quartz.dataSource.NAME.maxConnections	否	    DataSource 在连接接中创建的最大连接数
org.quartz.dataSource.NAME.validationQuary	否	    一个可选的 SQL 查询字串，DataSource 用它来侦测并替换失败/断开的连接。例如，Oracle 用户可选用 select table_name from user_tables，这个查询应当永远不会失败，除非直的就是连接不上了。


下面是我们的一个quartz.properties属性文件配置实例：
org.quartz.scheduler.instanceName = MyScheduler
org.quartz.threadPool.threadCount = 3
org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
org.quartz.jobStore.tablePrefix = QRTZ_
org.quartz.jobStore.dataSource = myDS

org.quartz.dataSource.myDS.driver = com.mysql.jdbc.Driver
org.quartz.dataSource.myDS.URL = jdbc:mysql://localhost:3306/quartz?characterEncoding=utf-8
org.quartz.dataSource.myDS.user = root
org.quartz.dataSource.myDS.password = root
org.quartz.dataSource.myDS.maxConnections =5
配置好quartz.properties属性文件后，我们只要**将它放在类路径下，然后运行我们的程序，即可覆盖在quartz.jar包中默认的配置文件

## 3. 测试
编写我们的测试文件，我们的测试环境是在quartz-2.2.2版本下进行的。

    public class pickNewsJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("在"+sdf.format(new Date())+"扒取新闻");
    }

    public static void main(String args[]) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(pickNewsJob.class)
                .withIdentity("job1", "jgroup1").build();
        SimpleTrigger simpleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("trigger1")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(10, 2))
                .startNow()
                .build();

        //创建scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }
}
执行测试方法，能看到控制台打印如下日志信息，关注红色部分，更注意其中的粗体部分，是我们quartz调用数据库的一些信息：

INFO : org.quartz.core.SchedulerSignalerImpl - Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl 
INFO : org.quartz.core.QuartzScheduler - Quartz Scheduler v.2.2.2 created. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Using thread monitor-based data access locking (synchronization). 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - JobStoreTX initialized. 
INFO : org.quartz.core.QuartzScheduler - Scheduler meta-data: Quartz Scheduler (v2.2.2) ‘MyScheduler’ with instanceId ‘NON_CLUSTERED’ 
Scheduler class: ‘org.quartz.core.QuartzScheduler’ - running locally. 
NOT STARTED. 
Currently in standby mode. 
Number of jobs executed: 0 
Using thread pool ‘org.quartz.simpl.SimpleThreadPool’ - with 3 threads. 
Using job-store ‘org.quartz.impl.jdbcjobstore.JobStoreTX’ - which supports persistence. and is not clustered. 

INFO : org.quartz.impl.StdSchedulerFactory - Quartz scheduler ‘MyScheduler’ initialized from default resource file in Quartz package: ‘quartz.properties’ 
INFO : org.quartz.impl.StdSchedulerFactory - Quartz scheduler version: 2.2.2 
INFO : com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource - Initializing c3p0 pool… com.mchange.v2.c3p0.ComboPooledDataSource [ acquireIncrement -> 3, acquireRetryAttempts -> 30, acquireRetryDelay -> 1000, autoCommitOnClose -> false, automaticTestTable -> null, breakAfterAcquireFailure -> false, checkoutTimeout -> 0, connectionCustomizerClassName -> null, connectionTesterClassName -> com.mchange.v2.c3p0.impl.DefaultConnectionTester, dataSourceName -> z8kfsx9f1dp34iubvoy4d|7662953a, debugUnreturnedConnectionStackTraces -> false, description -> null, driverClass -> com.mysql.jdbc.Driver, factoryClassLocation -> null, forceIgnoreUnresolvedTransactions -> false, identityToken -> z8kfsx9f1dp34iubvoy4d|7662953a, idleConnectionTestPeriod -> 0, initialPoolSize -> 3, jdbcUrl -> jdbc:mysql://localhost:3306/quartz?characterEncoding=utf-8, lastAcquisitionFailureDefaultUser -> null, maxAdministrativeTaskTime -> 0, maxConnectionAge -> 0, maxIdleTime -> 0, maxIdleTimeExcessConnections -> 0, maxPoolSize -> 5, maxStatements -> 0, maxStatementsPerConnection -> 120, minPoolSize -> 1, numHelperThreads -> 3, numThreadsAwaitingCheckoutDefaultUser -> 0, preferredTestQuery -> null, properties -> {user=******, password=******}, propertyCycle -> 0, testConnectionOnCheckin -> false, testConnectionOnCheckout -> false, unreturnedConnectionTimeout -> 0, usesTraditionalReflectiveProxies -> false ] 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Freed 0 triggers from ‘acquired’ / ‘blocked’ state. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Recovering 0 jobs that were in-progress at the time of the last shut-down.这里代表在我们任务开始时，先从数据库查询旧记录，这些旧记录是之前由于程序中断等原因未能正常执行的，于是先Recovery回来并执行 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Recovery complete. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Removed 0 ‘complete’ triggers. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Removed 0 stale fired job entries. 

INFO : org.quartz.core.QuartzScheduler - Scheduler MyScheduler_$_NON_CLUSTERED started. 
在21:28:12扒取新闻 
在21:28:13扒取新闻 
在21:28:15扒取新闻 
在21:28:17扒取新闻 
….

## 4. 拓展测试
我们再次运行测试方法，然后马上中断程序，查询我们数据库，会看到如下内容： 
SELECT * FROM QRTZ_SIMPLE_TRIGGERS; 
+————-+————–+—————+————–+—————–+—————–+ 
| SCHED_NAME | TRIGGER_NAME | TRIGGER_GROUP | REPEAT_COUNT | REPEAT_INTERVAL | TIMES_TRIGGERED | 
+————-+————–+—————+————–+—————–+—————–+ 
| MyScheduler | trigger1 | DEFAULT | 9 | 2000 | 1 | 
+————-+————–+—————+————–+—————–+—————–+ 
1 row in set (0.00 sec)

然后我们再运行程序，发现报错了。 
org.quartz.ObjectAlreadyExistsException: Unable to store Job : ‘jgroup1.job1’, because one already exists with this identification. 
一般的，在我们的任务调度前，会先将相关的任务持久化到数据库中，然后调用完在删除记录，这里在程序开始试图将任务信息持久化到数据库时，
显然和（因为我们之前中断操作导致）数据库中存在的记录起了冲突。

## 5. 恢复异常中断的任务
这个时候，我们可以选择修改我们的job名和组名和triiger名，然后再运行我们的程序。查看控制台打印的信息部分展示如下：

INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Freed 1 triggers from ‘acquired’ / ‘blocked’ state. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Handling 1 trigger(s) that missed their scheduled fire-time.这里我们开始处理上一次异常未完成的存储在数据库中的任务记录 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Recovering 0 jobs that were in-progress at the time of the last shut-down. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Recovery complete. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Removed 0 ‘complete’ triggers. 
INFO : org.quartz.impl.jdbcjobstore.JobStoreTX - Removed 1 stale fired job entries. 
INFO : org.quartz.core.QuartzScheduler - Scheduler MyScheduler_$_NON_CLUSTERED started. 
在21:42:13扒取新闻 
在21:42:13扒取新闻 
在21:42:14扒取新闻 
在21:42:15扒取新闻 
在21:42:16扒取新闻 
在21:42:17扒取新闻 
在21:42:18扒取新闻 
在21:42:19扒取新闻 
在21:42:20扒取新闻 
在21:42:21扒取新闻 
在21:42:22扒取新闻 
在21:42:23扒取新闻 
在21:42:24扒取新闻 
在21:42:25扒取新闻 
在21:42:26扒取新闻 
在21:42:27扒取新闻 
在21:42:28扒取新闻 
在21:42:29扒取新闻 
在21:42:30扒取新闻 

我们会发现，“扒取新闻”一句的信息打印次数超过十次，但我们在任务调度中设置了打印十次，说明它恢复了上次的任务调度。 
而如果我们不想执行新的任务，只想纯粹地恢复之前异常中断任务，我们可以采用如下方法：

SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();
    // ①获取调度器中所有的触发器组
    List<String> triggerGroups = scheduler.getTriggerGroupNames();
    // ②重新恢复在tgroup1组中，名为trigger1触发器的运行
    for (int i = 0; i < triggerGroups.size(); i++) {//这里使用了两次遍历，针对每一组触发器里的每一个触发器名，和每一个触发组名进行逐次匹配
        List<String> triggers = scheduler.getTriggerGroupNames();
        for (int j = 0; j < triggers.size(); j++) {
            Trigger tg = scheduler.getTrigger(new TriggerKey(triggers
                    .get(j), triggerGroups.get(i)));
            // ②-1:根据名称判断
            if (tg instanceof SimpleTrigger
                    && tg.getDescription().equals("jgroup1.DEFAULT")) {//由于我们之前测试没有设置触发器所在组，所以默认为DEFAULT
                // ②-1:恢复运行
                scheduler.resumeJob(new JobKey(triggers.get(j),
                        triggerGroups.get(i)));
            }
        }
    }
    scheduler.start();
}


# 四、springboot+quartz+jpa搭建定时任务执行框架
## 1、SpringBoot 2.0.1、quartz 2.3.0、jpa的配置文件

    #注意中文乱码
    spring.datasource.url=jdbc:mysql://localhost:3306/quartz?characterEncoding=utf-8&useSSL=false
    spring.datasource.username=root
    spring.datasource.password=
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
    # Specify the DBMS
    spring.jpa.database = MYSQL
    # Show or not log for each sql query
    spring.jpa.show-sql = true
    # DDL mode. This is actually a shortcut for the "hibernate.hbm2ddl.auto" property. Default to "create-drop" when using an embedded database, "none" otherwise.
    spring.jpa.hibernate.ddl-auto = update
    # Hibernate 4 naming strategy fully qualified name. Not supported with Hibernate 5.
    #spring.jpa.hibernate.naming.strategy = org.hibernate.cfg.ImprovedNamingStrategy
    # stripped before adding them to the entity manager)
    #spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
    # 新特性
    spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
    spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    
    # quartz任务配置
    spring.quartz.job-store-type=jdbc
    spring.quartz.properties.org.quartz.scheduler.instanceName=clusteredScheduler
    spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO
    spring.quartz.properties.org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
    spring.quartz.properties.org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
    spring.quartz.properties.org.quartz.jobStore.tablePrefix=QRTZ_
    spring.quartz.properties.org.quartz.jobStore.isClustered=true
    spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval=10000
    spring.quartz.properties.org.quartz.jobStore.useProperties=false
    spring.quartz.properties.org.quartz.threadPool.class=org.quartz.simpl.SimpleThreadPool
    spring.quartz.properties.org.quartz.threadPool.threadCount=10
    spring.quartz.properties.org.quartz.threadPool.threadPriority=5
    spring.quartz.properties.org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread=true
    
    # 打开集群配置
    spring.quartz.properties.org.quartz.jobStore.isClustered:true
    # 设置集群检查间隔20s
    spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval = 2000  
    
## 2、在项目初始化的时候，通过spring的ApplicationRunner启动一个定时任务去执行

```java
/**
 * 初始化一个测试Demo任务
 * 创建者 科帮网
 * 创建时间	2018年4月3日
 * 备注：
 * JobDetail对应数据库表qrtz_job_details
 *Trigger对应的数据库表qrtz_triggers
 * scheduler.scheduleJob(job, trigger) 会创建一个表
 */
@Component
public class TaskRunner implements ApplicationRunner{
    
	private final static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);
	
	@Autowired
    private IJobService jobService;
	@Autowired
    private Scheduler scheduler;
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void run(ApplicationArguments var) throws Exception{
    	Long count = jobService.listQuartzEntity(null);
    	if(count==0){
    		LOGGER.info("初始化测试任务");
    		QuartzEntity quartz = new QuartzEntity();
    		quartz.setJobName("test01");
    		quartz.setJobGroup("test");
    		quartz.setDescription("测试任务");
    		quartz.setJobClassName("com.itstyle.quartz.job.ChickenJob");
    		quartz.setCronExpression("0/20 * * * * ?");//每隔二十秒执行一次
   	        Class cls = Class.forName(quartz.getJobClassName()) ;
   	        cls.newInstance();
   	        //构建job信息
   	        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
   	        		quartz.getJobGroup())
   	        		.withDescription(quartz.getDescription()).build();
   	        //添加JobDataMap数据（日志输出，没有存在表中）
   	        job.getJobDataMap().put("itstyle", "科帮网欢迎你");        
   	        job.getJobDataMap().put("blog", "https://blog.52itstyle.com");        
		   	job.getJobDataMap().put("data", new String[]{"张三","李四"});  
   	        // 触发时间点
   	        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
   	        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
   	                .startNow().withSchedule(cronScheduleBuilder).build();	
   	        //交由Scheduler安排触发
   	        scheduler.scheduleJob(job, trigger);
    	}
    }

}
```

```java
import java.io.Serializable;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Job 的实例要到该执行它们的时候才会实例化出来。每次 Job 被执行，一个新的 Job 实例会被创建。
 * 其中暗含的意思就是你的 Job 不必担心线程安全性，因为同一时刻仅有一个线程去执行给定 Job 类的实例，甚至是并发执行同一 Job 也是如此。
 * @DisallowConcurrentExecution 保证上一个任务执行完后，再去执行下一个任务
 */
@DisallowConcurrentExecution
public class ChickenJob implements  Job,Serializable {
    
	private static final Logger logger = LoggerFactory.getLogger(ChickenJob.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		 System.out.println("大吉大利、今晚吃鸡-01-测试集群模式");
		
		 System.out.println("Hello!  NewJob is executing."+new Date() );  
		 //取得job详情  
         JobDetail jobDetail = context.getJobDetail();     
         // 取得job名称  
         String jobName = jobDetail.getClass().getName();  
         logger.info("Name: " + jobDetail.getClass().getSimpleName());     
         //取得job的类  
         logger.info("Job Class: " + jobDetail.getJobClass());     
         //取得job开始时间  
         logger.info(jobName + " fired at " + context.getFireTime());     
         //取得job下次触发时间  
         logger.info("Next fire time " + context.getNextFireTime());  
         
         JobDataMap dataMap =  jobDetail.getJobDataMap();
         
         logger.info("itstyle: " + dataMap.getString("itstyle")); 
         logger.info("blog: " + dataMap.getString("blog"));
         String[] array = (String[]) dataMap.get("data");
         for(String str:array){
        	 logger.info("data: " + str);
         }
         
	}
}

```

日志输出
大吉大利、今晚吃鸡-01-测试集群模式
Hello!  NewJob is executing.Thu Jan 03 20:08:00 CST 2019
2019-01-03 20:08:00.009  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : Name: JobDetailImpl
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : Job Class: class com.itstyle.quartz.job.ChickenJob
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : org.quartz.impl.JobDetailImpl fired at Thu Jan 03 20:08:00 CST 2019
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : Next fire time Thu Jan 03 20:08:20 CST 2019
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : itstyle: 科帮网欢迎你
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : blog: https://blog.52itstyle.com
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : data: 张三
2019-01-03 20:08:00.010  INFO 10096 --- [eduler_Worker-1] com.itstyle.quartz.job.ChickenJob        : data: 李四


## 3、controller通过接口来添加任务、修改、触发、暂停任务

备注：核心点事[JobKey]对象

[触发任务立即执行]
JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());//
scheduler.triggerJob(key);
[暂停任务]
JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
scheduler.pauseJob(key);
[恢复任务]
JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
scheduler.resumeJob(key);
[删除任务步骤]
TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());  
 // 停止触发器  
scheduler.pauseTrigger(triggerKey);  
// 移除触发器  
scheduler.unscheduleJob(triggerKey);  
// 删除任务  
scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));  

```java
package com.itstyle.quartz.web;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itstyle.quartz.entity.QuartzEntity;
import com.itstyle.quartz.entity.Result;
import com.itstyle.quartz.service.IJobService;
@RestController
@RequestMapping("/job")
public class JobController {
	private final static Logger LOGGER = LoggerFactory.getLogger(JobController.class);
	

	@Autowired
    private Scheduler scheduler;
    @Autowired
    private IJobService jobService;
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/add")
	public Result save(QuartzEntity quartz){
		LOGGER.info("新增任务");
		try {
	        //如果是修改  展示旧的 任务
	        if(quartz.getOldJobGroup()!=null){
	        	JobKey key = new JobKey(quartz.getOldJobName(),quartz.getOldJobGroup());
	        	scheduler.deleteJob(key);
	        }
	        Class cls = Class.forName(quartz.getJobClassName()) ;
	        cls.newInstance();
	        //构建job信息
	        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
	        		quartz.getJobGroup())
	        		.withDescription(quartz.getDescription()).build();
	        // 触发时间点
	        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
	        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
	                .startNow().withSchedule(cronScheduleBuilder).build();	
	        //交由Scheduler安排触发
	        scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
		return Result.ok();
	}
	@PostMapping("/list")
	public Result list(QuartzEntity quartz,Integer pageNo,Integer pageSize){
		LOGGER.info("任务列表");
		List<QuartzEntity> list = jobService.listQuartzEntity(quartz, pageNo, pageSize);
		return Result.ok(list);
	}
	@PostMapping("/trigger")
	public  Result trigger(QuartzEntity quartz,HttpServletResponse response) {
		LOGGER.info("触发任务");
		try {
		     JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());//
		     scheduler.triggerJob(key);
		} catch (SchedulerException e) {
			 e.printStackTrace();
			 return Result.error();
		}
		return Result.ok();
	}
	@PostMapping("/pause")
	public  Result pause(QuartzEntity quartz,HttpServletResponse response) {
		LOGGER.info("停止任务");
		try {
		     JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
		     scheduler.pauseJob(key);
		} catch (SchedulerException e) {
			 e.printStackTrace();
			 return Result.error();
		}
		return Result.ok();
	}
	@PostMapping("/resume")
	public  Result resume(QuartzEntity quartz,HttpServletResponse response) {
		LOGGER.info("恢复任务");
		try {
		     JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
		     scheduler.resumeJob(key);
		} catch (SchedulerException e) {
			 e.printStackTrace();
			 return Result.error();
		}
		return Result.ok();
	}
	@PostMapping("/remove")
	public  Result remove(QuartzEntity quartz,HttpServletResponse response) {
		LOGGER.info("移除任务");
		try {  
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());  
            // 停止触发器  
            scheduler.pauseTrigger(triggerKey);  
            // 移除触发器  
            scheduler.unscheduleJob(triggerKey);  
            // 删除任务  
            scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));  
            System.out.println("removeJob:"+JobKey.jobKey(quartz.getJobName()));  
        } catch (Exception e) {  
        	e.printStackTrace();
            return Result.error();
        }  
		return Result.ok();
	}
}

```
