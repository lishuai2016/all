# Java线上问题排查工具单
<!-- TOC -->

- [Java线上问题排查工具单](#java线上问题排查工具单)
- [1、jps [jps -mlvV]](#1jps-jps--mlvv)
- [2、jstack](#2jstack)
- [3、jinfo](#3jinfo)
- [4、jmap](#4jmap)
    - [4.1、查看堆的情况](#41查看堆的情况)
    - [4.2、dump](#42dump)
    - [4.3、看看堆都被谁占了? 再配合zprofiler和btrace，排查问题简直是如虎添翼](#43看看堆都被谁占了-再配合zprofiler和btrace排查问题简直是如虎添翼)
    - [5、jstat](#5jstat)
        - [5.1、jstat -gcutil 413502 1000](#51jstat--gcutil-413502-1000)
        - [5.2、类加载统计：](#52类加载统计)
        - [5.3、编译统计](#53编译统计)
        - [5.5、堆内存统计](#55堆内存统计)
        - [新生代垃圾回收统计](#新生代垃圾回收统计)
        - [新生代内存统计](#新生代内存统计)
        - [老年代垃圾回收统计](#老年代垃圾回收统计)
        - [老年代内存统计](#老年代内存统计)
        - [元数据空间统计(jdk1.8)](#元数据空间统计jdk18)
    - [6、jdb](#6jdb)
    - [7、CHLSDB](#7chlsdb)
- [参考文章](#参考文章)

<!-- /TOC -->


总结： 
- 1、jps -mlvV pid 列出本机所有的jvm实例[重要]
- 2、jinfo pid 列出运行中的Java程序的运行环境参数
- 3、jstack pid 打印Java线程的堆栈，跟踪那些线程被阻塞或正等待[重要]

- 4、jmap 18544 物理内存使用情况
- 5、jmap -histo 18544 打印每个class的实例数目，内存占用，类全名信息
- 6、jmap -histo:live 18544  查看堆中存活的对象实例[重要]
- 7、jamp -dump:file=jamp.heapdump 18544   导出进程heapdump文件
- 8、jmap -heap 18544   输出Java进程的堆内存信息，包括永久代、年轻代、老年代[重要]

- 9、jstat -gcutil 18544  查看jvm的gc情况占比[重要]
- 10、jstat -gc 18544
- 11、jstat -compiler 18544 显示jvm实时编译的情况
- 12、jstat -class 18544 类加载统计[重要]

- 13、java  -XX:+PrintFlagsFinal -version 2>&1 | grep MaxHeapSize  Java查看jvm的最大堆大小
    
CPU占用率分析
- ps -mp 18544 -o THREAD,tid,time | more -10  根据pid得到该进程的线程列表
- jstack 3741 | grep 18f3 -A 30 | more -30  打印线程的堆栈信息
- jstack 18544 |tee -a jstack.log    导出堆栈文件

备注：大小单位是KB

# 1、jps [jps -mlvV]
```mysql
413502 org.apache.catalina.startup.Bootstrap -config /export/Domains/pre.ls.com/server1/conf/server.xml start 
-Djava.util.logging.config.file=/export/Domains/pre.ls.com/server1/conf/logging.properties 
-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager 
-Djava.library.path=/usr/local/lib 
-Xms1024m 
-Xmx1024m 
-XX:MaxPermSize=256m 
-Djava.awt.headless=true 
-Dsun.net.client.defaultConnectTimeout=60000 
-Dsun.net.client.defaultReadTimeout=60000 
-Djmagick.systemclassloader=no 
-Dnetworkaddress.cache.ttl=300 
-Dsun.net.inetaddr.ttl=300 
-Djava.endorsed.dirs=/ls/tomcat6.0.33/endorsed 
-Dcatalina.base=/export/Domains/pre.ls.com/server1 
-Dcatalina.home=/ls/tomcat6.0.33 
-Djava.io.tmpdir=/export/Domains/pre.ls.com/server1/temp
```


# 2、jstack
jstack 2815

native+java栈:
jstack -m 2815

jstack 413502 > 1.txt  输出到文件中
```mysql
2018-12-26 09:37:25
Full thread dump Java HotSpot(TM) 64-Bit Server VM (24.71-b01 mixed mode):

"Attach Listener" daemon prio=10 tid=0x00007fdbe4001000 nid=0x76af8 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"http-bio-8008-exec-7" daemon prio=10 tid=0x00007fdb70009800 nid=0x7699c waiting on condition [0x00007fdb7b8f7000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-exec-6" daemon prio=10 tid=0x00007fdb70008000 nid=0x7699b waiting on condition [0x00007fdb7b9f8000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-exec-5" daemon prio=10 tid=0x00007fdb70006800 nid=0x69be2 waiting on condition [0x00007fdb7baf9000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-exec-4" daemon prio=10 tid=0x00007fdb70004800 nid=0x69bdf waiting on condition [0x00007fdb7bbfa000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-exec-3" daemon prio=10 tid=0x00007fdb70003000 nid=0x69115 waiting on condition [0x00007fdb7bcfb000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-exec-2" daemon prio=10 tid=0x00007fdb70002000 nid=0x6509f waiting on condition [0x00007fdbe9f58000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-exec-1" daemon prio=10 tid=0x00007fdb70001800 nid=0x65052 waiting on condition [0x00007fdc041a1000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69d6090> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:104)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:32)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-AsyncTimeout" daemon prio=10 tid=0x00007fdcb8287000 nid=0x64f8d waiting on condition [0x00007fdb7bdfc000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.tomcat.util.net.JIoEndpoint$AsyncTimeout.run(JIoEndpoint.java:152)
	at java.lang.Thread.run(Thread.java:745)

"http-bio-8008-Acceptor-0" daemon prio=10 tid=0x00007fdcb8286000 nid=0x64f8c runnable [0x00007fdb7befd000]
   java.lang.Thread.State: RUNNABLE
	at java.net.PlainSocketImpl.socketAccept(Native Method)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:398)
	at java.net.ServerSocket.implAccept(ServerSocket.java:530)
	at java.net.ServerSocket.accept(ServerSocket.java:498)
	at org.apache.tomcat.util.net.DefaultServerSocketFactory.acceptSocket(DefaultServerSocketFactory.java:60)
	at org.apache.tomcat.util.net.JIoEndpoint$Acceptor.run(JIoEndpoint.java:222)
	at java.lang.Thread.run(Thread.java:745)

"ContainerBackgroundProcessor[StandardEngine[Catalina]]" daemon prio=10 tid=0x00007fdcb8281800 nid=0x64f8b waiting on condition [0x00007fdbe9e57000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.run(ContainerBase.java:1513)
	at java.lang.Thread.run(Thread.java:745)

"Druid-ConnectionPool-Destroy-767849847" daemon prio=10 tid=0x00007fdbc4aac000 nid=0x64f8a waiting on condition [0x00007fdb7bffe000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at com.alibaba.druid.pool.DruidDataSource$DestroyConnectionThread.run(DruidDataSource.java:1942)

"Druid-ConnectionPool-Create-767849847" daemon prio=10 tid=0x00007fdbc4ab5000 nid=0x64f89 waiting on condition [0x00007fdbe81c1000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c698c858> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at com.alibaba.druid.pool.DruidDataSource$CreateConnectionThread.run(DruidDataSource.java:1868)

"Druid-ConnectionPool-Destroy-1979657755" daemon prio=10 tid=0x00007fdbc48f9000 nid=0x64f88 waiting on condition [0x00007fdbe82c2000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at com.alibaba.druid.pool.DruidDataSource$DestroyConnectionThread.run(DruidDataSource.java:1942)

"Druid-ConnectionPool-Create-1979657755" daemon prio=10 tid=0x00007fdbc48fc000 nid=0x64f87 waiting on condition [0x00007fdbe83c3000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c69f6cd0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
	at com.alibaba.druid.pool.DruidDataSource$CreateConnectionThread.run(DruidDataSource.java:1868)

"Abandoned connection cleanup thread" daemon prio=10 tid=0x00007fdbc4812800 nid=0x64f86 in Object.wait() [0x00007fdbe84c4000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
	- locked <0x00000000c6c057e0> (a java.lang.ref.ReferenceQueue$Lock)
	at com.mysql.jdbc.AbandonedConnectionCleanupThread.run(AbandonedConnectionCleanupThread.java:64)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)

"JSF-Future-Checker-CB-1-T-1" daemon prio=10 tid=0x00007fdbc4789000 nid=0x64f85 waiting on condition [0x00007fdbe85c5000]
   java.lang.Thread.State: TIMED_WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c6c0a578> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2082)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1090)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:807)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)

"JSF-Future-Checker-0-T-1" daemon prio=10 tid=0x00007fdbc4785800 nid=0x64f84 waiting on condition [0x00007fdbe86c6000]
   java.lang.Thread.State: TIMED_WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c6c0ac38> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2082)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1090)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:807)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)

"Timer-0" daemon prio=10 tid=0x00007fdbc447e800 nid=0x64f83 in Object.wait() [0x00007fdbe90d1000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at java.util.TimerThread.mainLoop(Timer.java:552)
	- locked <0x00000000c62b97c8> (a java.util.TaskQueue)
	at java.util.TimerThread.run(Timer.java:505)

"DefaultQuartzScheduler_QuartzSchedulerThread" prio=10 tid=0x00007fdbc4478800 nid=0x64f82 in Object.wait() [0x00007fdbe91d2000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.core.QuartzSchedulerThread.run(QuartzSchedulerThread.java:253)
	- locked <0x00000000c62b1868> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-10" prio=10 tid=0x00007fdbc4474800 nid=0x64f81 in Object.wait() [0x00007fdbe92d3000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62b0240> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-9" prio=10 tid=0x00007fdbc4472800 nid=0x64f80 in Object.wait() [0x00007fdbe93d4000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62b0060> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-8" prio=10 tid=0x00007fdbc4464000 nid=0x64f7f in Object.wait() [0x00007fdbe94d5000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62afe80> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-7" prio=10 tid=0x00007fdbc4462000 nid=0x64f7e in Object.wait() [0x00007fdbe95d6000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62afca0> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-6" prio=10 tid=0x00007fdbc4460000 nid=0x64f7d in Object.wait() [0x00007fdbe96d7000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62afac0> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-5" prio=10 tid=0x00007fdbc445e000 nid=0x64f7c in Object.wait() [0x00007fdbe97d8000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62a78b8> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-4" prio=10 tid=0x00007fdbc445c800 nid=0x64f7b in Object.wait() [0x00007fdbe98d9000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62a76d8> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-3" prio=10 tid=0x00007fdbc445b000 nid=0x64f7a in Object.wait() [0x00007fdbe99da000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62a74f8> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-2" prio=10 tid=0x00007fdbc445a000 nid=0x64f79 in Object.wait() [0x00007fdbe9adb000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62a7318> (a java.lang.Object)

"DefaultQuartzScheduler_Worker-1" prio=10 tid=0x00007fdbc4454000 nid=0x64f78 in Object.wait() [0x00007fdbe9bdc000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:568)
	- locked <0x00000000c62a7138> (a java.lang.Object)

"javamelody" daemon prio=10 tid=0x00007fdbc442a800 nid=0x64f77 in Object.wait() [0x00007fdbe9cdd000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at java.util.TimerThread.mainLoop(Timer.java:552)
	- locked <0x00000000c606d790> (a java.util.TaskQueue)
	at java.util.TimerThread.run(Timer.java:505)

"GC Daemon" daemon prio=10 tid=0x00007fdcb856d800 nid=0x64f74 in Object.wait() [0x00007fdc044e9000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000000c5649a78> (a sun.misc.GC$LatencyLock)
	at sun.misc.GC$Daemon.run(GC.java:117)
	- locked <0x00000000c5649a78> (a sun.misc.GC$LatencyLock)

"Service Thread" daemon prio=10 tid=0x00007fdcb80f4800 nid=0x64f72 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" daemon prio=10 tid=0x00007fdcb80f2000 nid=0x64f71 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" daemon prio=10 tid=0x00007fdcb80ef000 nid=0x64f70 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" daemon prio=10 tid=0x00007fdcb80ed000 nid=0x64f6f runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" daemon prio=10 tid=0x00007fdcb80c4000 nid=0x64f6e in Object.wait() [0x00007fdc055d4000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
	- locked <0x00000000c553c6a0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:151)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" daemon prio=10 tid=0x00007fdcb80c2000 nid=0x64f6d in Object.wait() [0x00007fdc056d5000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at java.lang.Object.wait(Object.java:503)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:133)
	- locked <0x00000000c553c230> (a java.lang.ref.Reference$Lock)

"main" prio=10 tid=0x00007fdcb8009800 nid=0x64f40 runnable [0x00007fdcc17f0000]
   java.lang.Thread.State: RUNNABLE
	at java.net.PlainSocketImpl.socketAccept(Native Method)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:398)
	at java.net.ServerSocket.implAccept(ServerSocket.java:530)
	at java.net.ServerSocket.accept(ServerSocket.java:498)
	at org.apache.catalina.core.StandardServer.await(StandardServer.java:453)
	at org.apache.catalina.startup.Catalina.await(Catalina.java:777)
	at org.apache.catalina.startup.Catalina.start(Catalina.java:723)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at org.apache.catalina.startup.Bootstrap.start(Bootstrap.java:321)
	at org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:455)

"VM Thread" prio=10 tid=0x00007fdcb80bd800 nid=0x64f6c runnable 

"GC task thread#0 (ParallelGC)" prio=10 tid=0x00007fdcb801f800 nid=0x64f41 runnable 

"GC task thread#1 (ParallelGC)" prio=10 tid=0x00007fdcb8021000 nid=0x64f42 runnable 

"GC task thread#2 (ParallelGC)" prio=10 tid=0x00007fdcb8023000 nid=0x64f43 runnable 

"GC task thread#3 (ParallelGC)" prio=10 tid=0x00007fdcb8025000 nid=0x64f44 runnable 

"GC task thread#4 (ParallelGC)" prio=10 tid=0x00007fdcb8026800 nid=0x64f45 runnable 

"GC task thread#5 (ParallelGC)" prio=10 tid=0x00007fdcb8028800 nid=0x64f46 runnable 

"GC task thread#6 (ParallelGC)" prio=10 tid=0x00007fdcb802a800 nid=0x64f47 runnable 

"GC task thread#7 (ParallelGC)" prio=10 tid=0x00007fdcb802c800 nid=0x64f48 runnable 

"GC task thread#8 (ParallelGC)" prio=10 tid=0x00007fdcb802e000 nid=0x64f49 runnable 

"GC task thread#9 (ParallelGC)" prio=10 tid=0x00007fdcb8030000 nid=0x64f4a runnable 

"GC task thread#10 (ParallelGC)" prio=10 tid=0x00007fdcb8032000 nid=0x64f4b runnable 

"GC task thread#11 (ParallelGC)" prio=10 tid=0x00007fdcb8033800 nid=0x64f4c runnable 

"GC task thread#12 (ParallelGC)" prio=10 tid=0x00007fdcb8035800 nid=0x64f4d runnable 

"GC task thread#13 (ParallelGC)" prio=10 tid=0x00007fdcb8037800 nid=0x64f4e runnable 

"GC task thread#14 (ParallelGC)" prio=10 tid=0x00007fdcb8039800 nid=0x64f4f runnable 

"GC task thread#15 (ParallelGC)" prio=10 tid=0x00007fdcb803b000 nid=0x64f50 runnable 

"GC task thread#16 (ParallelGC)" prio=10 tid=0x00007fdcb803d000 nid=0x64f51 runnable 

"GC task thread#17 (ParallelGC)" prio=10 tid=0x00007fdcb803f000 nid=0x64f52 runnable 

"GC task thread#18 (ParallelGC)" prio=10 tid=0x00007fdcb8040800 nid=0x64f53 runnable 

"GC task thread#19 (ParallelGC)" prio=10 tid=0x00007fdcb8042800 nid=0x64f54 runnable 

"GC task thread#20 (ParallelGC)" prio=10 tid=0x00007fdcb8044800 nid=0x64f55 runnable 

"GC task thread#21 (ParallelGC)" prio=10 tid=0x00007fdcb8046800 nid=0x64f56 runnable 

"GC task thread#22 (ParallelGC)" prio=10 tid=0x00007fdcb8048000 nid=0x64f57 runnable 

"GC task thread#23 (ParallelGC)" prio=10 tid=0x00007fdcb804a000 nid=0x64f58 runnable 

"GC task thread#24 (ParallelGC)" prio=10 tid=0x00007fdcb804c000 nid=0x64f59 runnable 

"GC task thread#25 (ParallelGC)" prio=10 tid=0x00007fdcb804d800 nid=0x64f5a runnable 

"GC task thread#26 (ParallelGC)" prio=10 tid=0x00007fdcb804f800 nid=0x64f5b runnable 

"GC task thread#27 (ParallelGC)" prio=10 tid=0x00007fdcb8051800 nid=0x64f5c runnable 

"GC task thread#28 (ParallelGC)" prio=10 tid=0x00007fdcb8053800 nid=0x64f5d runnable 

"GC task thread#29 (ParallelGC)" prio=10 tid=0x00007fdcb8055000 nid=0x64f5e runnable 

"GC task thread#30 (ParallelGC)" prio=10 tid=0x00007fdcb8057000 nid=0x64f5f runnable 

"GC task thread#31 (ParallelGC)" prio=10 tid=0x00007fdcb8059000 nid=0x64f60 runnable 

"GC task thread#32 (ParallelGC)" prio=10 tid=0x00007fdcb805a800 nid=0x64f61 runnable 

"GC task thread#33 (ParallelGC)" prio=10 tid=0x00007fdcb805c800 nid=0x64f62 runnable 

"GC task thread#34 (ParallelGC)" prio=10 tid=0x00007fdcb805e800 nid=0x64f63 runnable 

"GC task thread#35 (ParallelGC)" prio=10 tid=0x00007fdcb8060000 nid=0x64f64 runnable 

"GC task thread#36 (ParallelGC)" prio=10 tid=0x00007fdcb8062000 nid=0x64f65 runnable 

"GC task thread#37 (ParallelGC)" prio=10 tid=0x00007fdcb8064000 nid=0x64f66 runnable 

"GC task thread#38 (ParallelGC)" prio=10 tid=0x00007fdcb8066000 nid=0x64f67 runnable 

"GC task thread#39 (ParallelGC)" prio=10 tid=0x00007fdcb8067800 nid=0x64f68 runnable 

"GC task thread#40 (ParallelGC)" prio=10 tid=0x00007fdcb8069800 nid=0x64f69 runnable 

"GC task thread#41 (ParallelGC)" prio=10 tid=0x00007fdcb806b800 nid=0x64f6a runnable 

"GC task thread#42 (ParallelGC)" prio=10 tid=0x00007fdcb806d000 nid=0x64f6b runnable 

"VM Periodic Task Thread" prio=10 tid=0x00007fdcb80ff000 nid=0x64f73 waiting on condition 

JNI global references: 318
```



# 3、jinfo

可看系统启动的参数，如下
jinfo -flags 413502

```mysql
Attaching to process ID 413502, please wait...
Exception in thread "main" java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: java.lang.InternalError: void* type hasn't been seen when parsing int*
	at sun.jvm.hotspot.HotSpotTypeDataBase.recursiveCreateBasicPointerType(HotSpotTypeDataBase.java:721)
	at sun.jvm.hotspot.HotSpotTypeDataBase.lookupType(HotSpotTypeDataBase.java:134)
	at sun.jvm.hotspot.HotSpotTypeDataBase.lookupOrCreateClass(HotSpotTypeDataBase.java:631)
	at sun.jvm.hotspot.HotSpotTypeDataBase.createType(HotSpotTypeDataBase.java:751)
	at sun.jvm.hotspot.HotSpotTypeDataBase.readVMTypes(HotSpotTypeDataBase.java:195)
	at sun.jvm.hotspot.HotSpotTypeDataBase.<init>(HotSpotTypeDataBase.java:89)
	at sun.jvm.hotspot.HotSpotAgent.setupVM(HotSpotAgent.java:395)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:305)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.HeapSummary.main(HeapSummary.java:49)
	... 6 more
```


# 4、jmap
两个用途
## 4.1、查看堆的情况
```mysql
jmap -heap 413502

Attaching to process ID 413502, please wait...
Exception in thread "main" java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: java.lang.InternalError: void* type hasn't been seen when parsing int*
	at sun.jvm.hotspot.HotSpotTypeDataBase.recursiveCreateBasicPointerType(HotSpotTypeDataBase.java:721)
	at sun.jvm.hotspot.HotSpotTypeDataBase.lookupType(HotSpotTypeDataBase.java:134)
	at sun.jvm.hotspot.HotSpotTypeDataBase.lookupOrCreateClass(HotSpotTypeDataBase.java:631)
	at sun.jvm.hotspot.HotSpotTypeDataBase.createType(HotSpotTypeDataBase.java:751)
	at sun.jvm.hotspot.HotSpotTypeDataBase.readVMTypes(HotSpotTypeDataBase.java:195)
	at sun.jvm.hotspot.HotSpotTypeDataBase.<init>(HotSpotTypeDataBase.java:89)
	at sun.jvm.hotspot.HotSpotAgent.setupVM(HotSpotAgent.java:395)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:305)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.HeapSummary.main(HeapSummary.java:49)
	... 6 more
```
出现上面这种报错，说明你执行命令的服务器的jdk版本和运行pid服务的jdk版本不一致，使用jps -mlvV 找到对应jdk版本，
然后使用jmap的全路径访问即可
```mysql
jmap 243
Attaching to process ID 243, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 24.71-b01
0x0000000000400000	7K	/home/ls/jdk1.7.0_71/bin/java
0x00007f7e313e2000	108K	/lib64/libresolv-2.12.so
0x00007f7e315fc000	26K	/lib64/libnss_dns-2.12.so
0x00007f7e31e8a000	88K	/lib64/libgcc_s-4.4.7-20120601.so.1
0x00007f7e320a0000	250K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libsunec.so
0x00007f7e445cb000	44K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libmanagement.so
0x00007f7e447d3000	89K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libnio.so
0x00007f7e479e5000	112K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libnet.so
0x00007f7eaa0e8000	120K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libzip.so
0x00007f7eaa303000	64K	/lib64/libnss_files-2.12.so
0x00007f7eaa511000	48K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libinstrument.so
0x00007f7eaa71b000	214K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libjava.so
0x00007f7eaa946000	63K	/home/ls/jdk1.7.0_71/jre/lib/amd64/libverify.so
0x00007f7eaab54000	42K	/lib64/librt-2.12.so
0x00007f7eaad5c000	582K	/lib64/libm-2.12.so
0x00007f7eaafe0000	14862K	/home/ls/jdk1.7.0_71/jre/lib/amd64/server/libjvm.so
0x00007f7eabe56000	1876K	/lib64/libc-2.12.so
0x00007f7eac1ea000	19K	/lib64/libdl-2.12.so
0x00007f7eac3ee000	103K	/home/ls/jdk1.7.0_71/lib/amd64/jli/libjli.so
0x00007f7eac605000	139K	/lib64/libpthread-2.12.so
0x00007f7eac822000	150K	/lib64/ld-2.12.so

```


## 4.2、dump
jmap -dump:live,format=b,file=/tmp/heap2.bin 2815
或者
jmap -dump:format=b,file=./heap3.bin 413502
file后面的是自定义的文件名，最后的数字是进程的pid。如果添加了live，只会打印活跃的对象。

jmap -dump:format=b,file=./heap3.heap 413502

## 4.3、看看堆都被谁占了? 再配合zprofiler和btrace，排查问题简直是如虎添翼

jmap -histo 413502 | head -10
```mysql
num     #instances         #bytes  class name
----------------------------------------------
   1:       2164173      145734912  [B
   2:       1214648       76291544  [C
   3:        723970       28958800  org.jrobin.core.RrdInt
   4:        556900       26731200  org.jrobin.core.RrdDouble
   5:        844816       20275584  java.lang.String
   6:        334140       16038720  org.jrobin.core.RrdLong
   7:         26831       14111008  [I
```


## 5、jstat
jstat参数众多，但是使用一个就够了,[总结垃圾回收统计,jstat -gcutil pid]

### 5.1、jstat -gcutil 413502 1000 

```mysql

Warning: Unresolved Symbol: sun.gc.metaspace.capacity substituted NaN
Warning: Unresolved Symbol: sun.gc.metaspace.used substituted NaN
Warning: Unresolved Symbol: sun.gc.metaspace.capacity substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.capacity substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.used substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.capacity substituted NaN
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 97.69   0.00   2.00  18.14      -      -    118   13.802     0    0.000   13.802
 
 S0：幸存1区当前使用比例
 S1：幸存2区当前使用比例
 E：伊甸园区使用比例
 O：老年代使用比例
 M：元数据区使用比例
 CCS：压缩使用比例
 YGC：年轻代垃圾回收次数
 FGC：老年代垃圾回收次数
 FGCT：老年代垃圾回收消耗时间
 GCT：垃圾回收消耗总时间
```



### 5.2、类加载统计：
jstat -class 413502

Loaded  Bytes  Unloaded  Bytes     Time   
  7175 14875.9        0     0.0       3.26
  
Loaded:加载class的数量
Bytes：所占用空间大小
Unloaded：未加载数量
Bytes:未加载占用空间
Time：时间

### 5.3、编译统计
jstat -compiler 413502
```mysql
Compiled Failed Invalid   Time   FailedType FailedMethod
    1375      1       0    13.70          1 org/apache/tomcat/util/IntrospectionUtils setProperty
    
    Compiled：编译数量。
    Failed：失败数量
    Invalid：不可用数量
    Time：时间
    FailedType：失败类型
    FailedMethod：失败的方法
```



 ### 5.4、垃圾回收统计
jstat -gc 413502 1000 
```mysql
Warning: Unresolved Symbol: sun.gc.metaspace.capacity substituted NaN
Warning: Unresolved Symbol: sun.gc.metaspace.used substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.capacity substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.used substituted NaN
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  4631.3   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802
58880.0 58880.0 57520.0  0.0   231936.0  9212.5   699392.0   126898.7    -      -      -      -       118   13.802   0      0.000   13.802

0C：第一个幸存区的大小
S1C：第二个幸存区的大小
S0U：第一个幸存区的使用大小
S1U：第二个幸存区的使用大小
EC：伊甸园区的大小
EU：伊甸园区的使用大小
OC：老年代大小
OU：老年代使用大小
MC：方法区大小
MU：方法区使用大小
CCSC:压缩类空间大小
CCSU:压缩类空间使用大小
YGC：年轻代垃圾回收次数
YGCT：年轻代垃圾回收消耗时间
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```

### 5.5、堆内存统计
```mysql
jstat -gccapacity 413502  
Warning: Unresolved Symbol: sun.gc.metaspace.minCapacity substituted NaN
Warning: Unresolved Symbol: sun.gc.metaspace.maxCapacity substituted NaN
Warning: Unresolved Symbol: sun.gc.metaspace.capacity substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.minCapacity substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.maxCapacity substituted NaN
Warning: Unresolved Symbol: sun.gc.compressedclassspace.capacity substituted NaN
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC 
349696.0 349696.0 349696.0 58880.0 58880.0 231936.0   699392.0   699392.0   699392.0   699392.0        -        -        -        -        -        -    118     0

NGCMN：新生代最小容量
NGCMX：新生代最大容量
NGC：当前新生代容量
S0C：第一个幸存区大小
S1C：第二个幸存区的大小
EC：伊甸园区的大小
OGCMN：老年代最小容量
OGCMX：老年代最大容量
OGC：当前老年代大小
OC:当前老年代大小
MCMN:最小元数据容量
MCMX：最大元数据容量
MC：当前元数据空间大小
CCSMN：最小压缩类空间大小
CCSMX：最大压缩类空间大小
CCSC：当前压缩类空间大小
YGC：年轻代gc次数
FGC：老年代GC次数
```

### 新生代垃圾回收统计
```mysql
jstat -gcnew 413502  

S0C：第一个幸存区大小
S1C：第二个幸存区的大小
S0U：第一个幸存区的使用大小
S1U：第二个幸存区的使用大小
TT:对象在新生代存活的次数
MTT:对象在新生代存活的最大次数
DSS:期望的幸存区大小
EC：伊甸园区的大小
EU：伊甸园区的使用大小
YGC：年轻代垃圾回收次数
YGCT：年轻代垃圾回收消耗时间
```

### 新生代内存统计
```mysql
jstat -gcnewcapacity 413502  


NGCMN：新生代最小容量
NGCMX：新生代最大容量
NGC：当前新生代容量
S0CMX：最大幸存1区大小
S0C：当前幸存1区大小
S1CMX：最大幸存2区大小
S1C：当前幸存2区大小
ECMX：最大伊甸园区大小
EC：当前伊甸园区大小
YGC：年轻代垃圾回收次数
FGC：老年代回收次数
```


### 老年代垃圾回收统计
```mysql
jstat -gcold 413502  

MC：方法区大小
MU：方法区使用大小
CCSC:压缩类空间大小
CCSU:压缩类空间使用大小
OC：老年代大小
OU：老年代使用大小
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```


### 老年代内存统计
```mysql
jstat -gcoldcapacity 413502  

OGCMN：老年代最小容量
OGCMX：老年代最大容量
OGC：当前老年代大小
OC：老年代大小
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```


### 元数据空间统计(jdk1.8)
```mysql
jstat -gcmetacapacity 413502  

MCMN: 最小元数据容量
MCMX：最大元数据容量
MC：当前元数据空间大小
CCSMN：最小压缩类空间大小
CCSMX：最大压缩类空间大小
CCSC：当前压缩类空间大小
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```

## 6、jdb
时至今日，jdb也是经常使用的。 
jdb可以用来预发debug,假设你预发的java_home是/opt/taobao/java/，远程调试端口是8000.那么
jdb -attach 8000

screenshot.png
出现以上代表jdb启动成功。后续可以进行设置断点进行调试。
具体参数可见oracle官方说明http://docs.oracle.com/javase/7/docs/technotes/tools/windows/jdb.html

##7、CHLSDB
CHLSDB感觉很多情况下可以看到更好玩的东西，不详细叙述了。 查询资料听说jstack和jmap等工具就是基于它的。

sudo -u admin /opt/taobao/java/bin/java -classpath /opt/taobao/java/lib/sa-jdi.jar sun.jvm.hotspot.CLHSDB
更详细的可见R大此贴
http://rednaxelafx.iteye.com/blog/1847971


# 参考文章
[jstat命令查看jvm的GC情况 （以Linux为例）](https://www.cnblogs.com/yjd_hycf_space/p/7755633.html)


