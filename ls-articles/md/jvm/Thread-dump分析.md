---
title: Thread-dump分析
categories: 
- jvm
tags:
---


目录：
一、thread dump信息获取
二、 dump文件的构成和线程的分类
三、thread线程的原始状态和thread dump文件中线程状态的
四、Dump文件中的线程状态含义
五、线程dump文件中一个线程的各个部分
六、案例分析
七、附录
1、死锁测试代码
2、死锁的thread dump文件（在Windows下按Ctrl+pausebreak在控制台输出的）
3、一般java程序的线程信息
4、Thread-Tomcat容器dump分析


[总结]
BLOCKED---waiting for monitor entry（deadlock）
BLOCKED---waiting for monitor entry（热点同步代码块造成的阻塞）
WAITING---（in Object.wait()）（调用 object.wait()造成的）
TIMED_WAITING--- in Object.wait()（object.wait(1 * 60 * 1000)造成）
WAITING (parking)---waiting on condition（通过lock锁的条件变量condition.await()造成）
TIMED_WAITING (parking)---waiting on condition（通过lock锁的条件变量condition.await(5, TimeUnit.MINUTES)造成）
TIMED_WAITING---waiting on condition（Thread.sleep(1*60*1000)造成的）


# 前言 基本概念
在对Java内存泄漏进行分析的时候，需要对jvm运行期间的内存占用、线程执行等情况进行记录的dump文件，常用的主要有[thread dump]和[heap dump]
1、thread dump 主要记录JVM在某一时刻各个线程执行的情况，以栈的形式显示，是一个文本文件。通过对thread dump文件可以分析出程序的问题出现在什么地方，
从而定位具体的代码然后进行修正。thread dump需要结合占用系统资源的线程id进行分析才有意义。
2、heap dump 主要记录了在某一时刻JVM堆中对象使用的情况，即某个时刻JVM堆的快照，是一个二进制文件，主要用于分析哪些对象占用了太对的堆空间，
从而发现导致内存泄漏的对象。
上面两种dump文件都具有实时性，因此需要在服务器出现问题的时候生成，并且多生成几个文件，方便进行对比分析。下面我们先来说一下如何生成 thread dump。


# 一、thread dump信息获取
## 1、发送信号
* In Unix, use "kill -3 <pid>" where pid is the Process ID of the JVM.（kill 信号列表）
* In Windows, press CTRL+BREAK on the window where the JVM is running.

## 2、通过命令导出文本文件
jps -l 找出服务器上运行的Java进程
jstack -l pid  直接显示在屏幕上
jstack pid | tee -a jstack.log    导出堆栈文件

## 3、通过jdk工具jvisualvm，在界面上点击生成thread dump文件

# 二、 dump文件的构成和线程的分类
[JVM中的线程是和OS中的线程一一对应的]
简单来看分为以下三部分
- 最上面的是时间和jvm的信息
- 各个线程thread信息
- 最后面是heap堆的信息

操作系统负责调度所有的线程，因此在不同的平台上，Java线程的优先级有所不同。在JVM中除了应用线程，
还有其他的一些线程用于支持JVM的运行，这些线程可以被划分为以下几类：
1、Full thread dump identifier                                      这一部分是内容最开始的部分，展示了快照文件的生成时间和JVM的版本信息。

2、Java EE middleware, third party & custom application Threads     [这是整个文件的核心部分]，里面展示了JavaEE容器（如tomcat、resin等）、自己的程序中所使用的线程信息

3、HotSpot VM Thread         这一部分展示了[JVM内部线程的信息]，用于执行内部的原生操作,下面常见的集中内置线程

3.1 "Attach Listener"        该线程负责接收外部命令，执行该命令并把结果返回给调用者，此种类型的线程通常在桌面程序中出现

3.2 "Signal Dispatcher"      Attach Listener线程的职责是接收外部jvm命令，当命令接收成功后，会交给signal dispather 线程去进行分发到各个不同的模块处理命令，
                             并且返回处理结果。signal dispather线程也是在第一次接收外部jvm命令时，进行初始化工作。

3.3 "DestroyJavaVM"          执行main()的线程在执行完之后调用JNI中的 jni_DestroyJavaVM() 方法会唤起DestroyJavaVM 线程。在JBoss启动之后，也会唤起DestroyJavaVM线程，
                             处于等待状态，等待其它线程（java线程和native线程）退出时通知它卸载JVM。

3.4 "Service Thread"         用于启动服务的线程

3.5 "CompilerThread"         用来调用JITing，实时编译装卸CLASS。通常JVM会启动多个线程来处理这部分工作，线程名称后面的数字也会累加，比如CompilerThread1,
                             这些线程负责在运行时将字节码编译为本地代码；

3.6 "Reference Handler"      JVM在创建main线程后就创建Reference Handler线程，其优先级最高，为10，它主要用于处理引用对象本身（软引用、弱引用、虚引用）的垃圾回收问题 

3.7 "VM Thread"              [JVM中线程的母体]，根据HotSpot源码中关于vmThread.hpp里面的注释，它是一个单例的对象（最原始的线程）会产生或触发所有其他的线程，
                             这个单例的VM线程是会被其他线程所使用来做一些VM操作（如清扫垃圾等）。
                             在 VM Thread 的结构体里有一个VMOperationQueue列队，所有的VM线程操作(vm_operation)都会被保存到这个列队当中，VMThread 本身就是一个线程，
                             它的线程负责执行一个自轮询的loop函数(具体可以参考：VMThread.cpp里面的void VMThread::loop()) ，
                             该loop函数从VMOperationQueue列队中按照优先级取出当前需要执行的操作对象(VM_Operation)，
                             并且调用VM_Operation->evaluate函数去执行该操作类型本身的业务逻辑。
                             VM操作类型被定义在vm_operations.hpp文件内，列举几个：ThreadStop、ThreadDump、PrintThreads、GenCollectFull、GenCollectFullConcurrent、
                             CMS_Initial_Mark、CMS_Final_Remark….. 有兴趣的同学，可以自己去查看源文件。
                             负责JVM在安全点内的各种操作，这些操作（诸如自动内存管理、取消偏向锁、线程dump、线程挂起等等）
                             在执行过程中需要JVM处于这样一个状态——堆的内容不会被改变，这种状态在JVM里叫做安全点（safe-point）。

3.8 "Finalizer"     这个线程也是在main线程之后创建的，其优先级为10，主要用于在垃圾收集前，调用对象的finalize()方法；关于Finalizer线程的几点：
                  （1）只有当开始一轮垃圾收集时，才会开始调用finalize()方法；因此并不是所有对象的finalize()方法都会被执行；
                  （2）该线程也是daemon线程，因此如果虚拟机中没有其他非daemon线程，不管该线程有没有执行完finalize()方法，JVM也会退出；
                  （3）JVM在垃圾收集时会将失去引用的对象包装成Finalizer对象（Reference的实现），并放入ReferenceQueue，由Finalizer线程来处理；
                        最后将该Finalizer对象的引用置为null，由垃圾收集器来回收；
                  （4）JVM为什么要单独用一个线程来执行finalize()方法呢？如果JVM的垃圾收集线程自己来做，很有可能由于在finalize()方法中误操作导致GC线程停止或不可控，
                  这对GC线程来说是一种灾难。
                  
4、HotSpot GC Thread                                         [JVM中用于进行资源回收的线程]，包括以下几种类型的线程

4.1 "VM Periodic Task Thread"                                该线程是JVM周期性任务调度的线程，它由WatcherThread创建，是一个单例对象。该线程在JVM内使用得比较频繁，
                                                             比如：定期的内存监控、JVM运行状况监控。
                                                             可以使用jstat 命令查看GC的情况，比如查看某个进程没有存活必要的引用可以使用命令 jstat -gcutil <pid> 250 7 
                                                             参数中pid是进程id，后面的250和7表示每250毫秒打印一次，总共打印7次。
                                                             这对于防止因为应用代码中直接使用native库或者第三方的一些监控工具的内存泄漏有非常大的帮助。

4.2 "GC task thread#0 (ParallelGC)"                          垃圾回收线程，该线程会负责进行垃圾回收。通常JVM会启动多个线程来处理这个工作，线程名称中#后面的数字也会累加。

如果在JVM中增加了 -XX:+UseConcMarkSweepGC 参数将会启用CMS （Concurrent Mark-Sweep）GC Thread方式，以下是该模式下的线程类型：
"Gang worker#0 (Parallel GC Threads)"  
原来垃圾回收线程GC task thread#0 (ParallelGC) 被替换为 Gang worker#0 (Parallel GC Threads)。Gang worker 是JVM用于年轻代垃圾回收(minor gc)的线程。

"Concurrent Mark-Sweep GC Thread"  
并发标记清除垃圾回收器（就是通常所说的CMS GC）线程， 该线程主要针对于年老代垃圾回收

"Surrogate Locker Thread (Concurrent GC)"  
此线程主要配合CMS垃圾回收器来使用，是一个守护线程，主要负责处理GC过程中Java层的Reference（指软引用、弱引用等等）
与jvm 内部层面的对象状态同步。这里以 WeakHashMap 为例进行说明，首先是一个关键点：
(1)WeakHashMap和HashMap一样，内部有一个Entry[]数组;
(2)WeakHashMap的Entry比较特殊，它的继承体系结构为Entry->WeakReference->Reference;
(3)Reference 里面有一个全局锁对象：Lock，它也被称为pending_lock，注意：它是静态对象；
(4)Reference 里面有一个静态变量：pending；
(5)Reference 里面有一个静态内部类：ReferenceHandler的线程，它在static块里面被初始化并且启动，启动完成后处于wait状态，它在一个Lock同步锁模块中等待；
(6)WeakHashMap里面还实例化了一个ReferenceQueue列队
假设，WeakHashMap对象里面已经保存了很多对象的引用，JVM 在进行CMS GC的时候会创建一个ConcurrentMarkSweepThread（简称CMST）线程去进行GC。
ConcurrentMarkSweepThread线程被创建的同时会创建一个SurrogateLockerThread（简称SLT）线程并且启动它，SLT启动之后，处于等待阶段。
CMST开始GC时，会发一个消息给SLT让它去获取Java层Reference对象的全局锁：Lock。直到CMS GC完毕之后，JVM 会将WeakHashMap中所有被回收的对象所属的WeakReference
容器对象放入到Reference 的pending属性当中（每次GC完毕之后，pending属性基本上都不会为null了），然后通知SLT释放并且notify全局锁:Lock。
此时激活了ReferenceHandler线程的run方法，使其脱离wait状态，开始工作了。
ReferenceHandler这个线程会将pending中的所有WeakReference对象都移动到它们各自的列队当中，比如当前这个WeakReference属于某个WeakHashMap对象，
那么它就会被放入相应的ReferenceQueue列队里面（该列队是链表结构）。 当我们下次从WeakHashMap对象里面get、put数据或者调用size方法的时候，
WeakHashMap就会将ReferenceQueue列队中的WeakReference依依poll出来去和Entry[]数据做比较，如果发现相同的，则说明这个Entry所保存的对象已经被GC掉了，
那么将Entry[]内的Entry对象剔除掉。

5、JNI global references count
这一部分主要回收那些在native代码上被引用，但在java代码中却没有存活必要的引用，对于防止因为应用代码中直接使用native库或第三方的一些监控工具的内存泄漏有非常大的帮助。

<div align="center"> <img src="/images/thread1-1.png"/> </div><br>


6、其他
6.1、RMI开头的线程，负责JVM跟JMC客户端通信，吐出JVM内的运行信息
6.2、Monitor Ctrl-Break   在windows上按Ctrl+pausebreak键，在窗口输出thread dump信息


http-nio-8080-AsyncTimeout
http-nio-8080-Acceptor-0
http-nio-8080-ClientPoller-1
http-nio-8080-ClientPoller-0
http-nio-8080-exec-10
NioBlockingSelector.BlockPoller-1
container-0
ContainerBackgroundProcessor[StandardEngine[Tomcat]]




# 三、thread线程的原始状态和thread dump文件中线程状态的
## 1、thread线程的原始状态
NEW：还没有调用start()方法开始执行，在thread dump中不会出现
RUNNABLE：正常在执行的线程
BLOCKED：阻塞状态，等待获取锁进入同步方法或者同步代码块
WAITING：调用wait()，join()，LockSupport.park()方法
TIMED_WAITING：调用wait(long)，join(long)，LockSupport.parkNanos，LockSupport.parkUntil方法，在指定时间内等待并且没有超时
TERMINATED：正常执行完的线程，在thread dump中不会出现
在thread dump中可能出现的thread线程的原始状态有：[RUNNABLE]，[BLOCKED]，[WAITING]，[TIMED_WAITING]

## 2、thread dump文件中线程状态的种类
Runnable
sleeping
Waiting for monitor entry （重点关注：说明是synchronized锁）
Wait on condition（重点关注）
in Object.wait() （重点关注）
Deadlock（重点关注）
其中
thread的RUNNABLE对应---> Runnable
thread的BLOCKED对应---> Waiting for monitor entry 
thread的WAITING和TIMED_WAITING对应---> in Object.wait()和Wait on condition

# 四、Dump文件中的线程状态含义
## 1、Deadlock
死锁线程，一般指多个线程调用间，进入相互资源占用，导致一直等待无法释放的情况。
## 2、Runnable：
一般指该线程正在执行状态中，该线程占用了资源，正在处理某个请求，有可能正在传递SQL到数据库执行，有可能在对某个文件操作，有可能进行数据类型等转换。
## 3、Waiting on condition
等待资源，或等待某个条件的发生。具体原因需结合 stacktrace来分析。如果堆栈信息明确是应用代码，则证明该线程正在等待资源。
一般是大量读取某资源，且该资源采用了资源锁的情况下，线程进入等待状态，等待资源的读取。又或者，正在等待其他线程的执行等。
如果发现有大量的线程都在处在 Wait on condition，从线程 stack看，正等待网络读写，这可能是一个网络瓶颈的征兆。因为网络阻塞导致线程无法执行。
一种情况是网络非常忙，几乎消耗了所有的带宽，仍然有大量数据等待网络读写；
另一种情况也可能是网络空闲，但由于路由等问题，导致包无法正常的到达。
另外一种出现 Wait on condition的常见情况是该线程在 [sleep]，等待 sleep的时间到了时候，将被唤醒。
## 4、Waiting for monitor entry 和 in Object.wait()
Monitor是 Java中用以实现线程之间的互斥与协作的主要手段，它可以看成是对象或者 Class的锁。
每一个对象都有，也仅有一个 monitor。每个 Monitor在某个时刻，只能被一个线程拥有，该线程就是 “Active Thread”，
而其它线程都是 “Waiting Thread”，分别在两个队列 “ Entry Set”和 “Wait Set”里面等候。
在 “Entry Set”中等待的线程状态是 “Waiting for monitor entry”，而在 “Wait Set”中等待的线程状态是 “in Object.wait()”。
在 “Entry Set”中的是试图获取锁而阻塞的线程；而“Wait Set”则是，获取锁后调用wait()而进入阻塞的线程，
当其他线程调用notify()或者notifyall()唤醒等待该锁上的其他线程时，会把“Wait Set”队列中的
一个或者全部线程放入“Entry Set”中，然后在其中随机选择一个重新获取锁，然后执行。
<div align="center"> <img src="/images/thread1-0.png"/> </div><br>


# 五、线程dump文件中一个线程的各个部分
1. "Timer-0" daemon prio=10 tid=0xac190c00 nid=0xaef in Object.wait() [0xae77d000] 
2. java.lang.Thread.State: TIMED_WAITING (on object monitor) 
3. atjava.lang.Object.wait(Native Method) 
4. -waiting on <0xb3885f60> (a java.util.TaskQueue) ###继续wait 
5. atjava.util.TimerThread.mainLoop(Timer.java:509) 
6. -locked <0xb3885f60> (a java.util.TaskQueue) ###已经locked 
7. atjava.util.TimerThread.run(Timer.java:462)
* 线程名称：Timer-0 
* 线程类型：daemon 
* 优先级: 10，默认是5 ，数值越大优先级越高越先执行
* jvm线程id：tid=0xac190c00，jvm内部线程的唯一标识（通过java.lang.Thread.getId()获取，通常用自增方式实现。） 
* 对应系统线程id（NativeThread ID）：nid=0xaef，和top命令查看的线程pid对应，不过一个是10进制，一个是16进制。（通过命令：top -H -p pid，可以查看该进程的所有线程信息）
* 线程状态：in Object.wait(). 
* 起始栈地址：[0xae77d000] 
* Java thread statck trace：是上面2-7行的信息。到目前为止这是最重要的数据，Java stack trace提供了大部分信息来精确定位问题根源。（倒着看，入口在最下面一行）


# 六、案例分析
综合案例一：BLOCKED---waiting for monitor entry（deadlock）
综合案例二：BLOCKED---waiting for monitor entry（热点同步代码块造成的阻塞）
综合案例三：WAITING---（in Object.wait()）（调用 object.wait()造成的）
综合案例四：TIMED_WAITING--- in Object.wait()（object.wait(1 * 60 * 1000)造成）
综合案例五：WAITING (parking)---waiting on condition（通过lock锁的条件变量condition.await()造成）
综合案例六：TIMED_WAITING (parking)---waiting on condition（通过lock锁的条件变量condition.await(5, TimeUnit.MINUTES)造成）
综合案例七：TIMED_WAITING---waiting on condition（Thread.sleep(1*60*1000)造成的）

## 综合案例一：BLOCKED---waiting for monitor entry（deadlock）

```java
public class Test {
    public static void main(String[] args) throws Exception{
        System.out.println("主线程开始"+Thread.currentThread().getName());
        final LeftRightDeadLock lock = new LeftRightDeadLock();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    lock.leftRight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    lock.rightLeft();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        t2.start();
 
        t1.join();
        t2.join();
        System.out.println("主线程结束"+Thread.currentThread().getName());
    }
}
```

```java
public class LeftRightDeadLock {
    private final Object left = new Object();
    private final Object right = new Object();
 
    public void leftRight() throws Exception {
        synchronized (left) {
            System.out.println(Thread.currentThread().getName()+"获得left锁");
            Thread.sleep(1000);
            synchronized (right)
            {
                System.out.println("leftRight end!");
            }
        }
    }
 
    public void rightLeft() throws Exception {
        synchronized (right) {    System.out.println(Thread.currentThread().getName()+"获得right锁");
            Thread.sleep(1000);
            synchronized (left)
            {
                System.out.println("rightLeft end!");
            }
        }
    }
}
```
    thread dump信息：
    "Thread-1" prio=6 tid=0x000000000c019000 nid=0x2f38 waiting for monitor entry [0x000000000d14f000]
    java.lang.Thread.State: BLOCKED (on object monitor)
    at ls.lock.LeftRightDeadLock.rightLeft(LeftRightDeadLock.java:32)
    - waiting to lock <0x00000000d7d53bb8> (a java.lang.Object)
    - locked <0x00000000d7d53bc8> (a java.lang.Object)
    at ls.lock.Test$2.run(Test.java:26)
    
    "Thread-0" prio=6 tid=0x000000000c018000 nid=0x3de0 waiting for monitor entry [0x000000000cddf000]
    java.lang.Thread.State: BLOCKED (on object monitor)
    at ls.lock.LeftRightDeadLock.leftRight(LeftRightDeadLock.java:20)
    - waiting to lock <0x00000000d7d53bc8> (a java.lang.Object)
    - locked <0x00000000d7d53bb8> (a java.lang.Object)
    at ls.lock.Test$1.run(Test.java:16)
    
    Found one Java-level deadlock:
    =============================
    "Thread-1":
    waiting to lock monitor 0x000000000aa87f58 (object 0x00000000d7d53bb8, a java.lang.Object),
    which is held by "Thread-0"
    "Thread-0":
    waiting to lock monitor 0x000000000c01ad08 (object 0x00000000d7d53bc8, a java.lang.Object),
    which is held by "Thread-1"

## 综合案例二：BLOCKED---waiting for monitor entry（热点同步代码块造成的阻塞）

```java
public class BlockedState {
    private static Object object = new Object();
 
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                    long begin = System.currentTimeMillis();
                    long end = System.currentTimeMillis();
                    // 让线程运行1分钟,会一直持有object的监视器
                    while ((end - begin) <= 1 * 60 * 1000) {
                        end = System.currentTimeMillis();
                    }
                }
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
```


thread dump信息：
"t2" prio=6 tid=0x000000000c389800 nid=0x3ee4 waiting for monitor entry [0x000000000cc6f000]
java.lang.Thread.State: BLOCKED (on object monitor)
at ls.lock.BlockedState$1.run(BlockedState.java:19)
- waiting to lock <0x00000000d7d4c460> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c388800 nid=0x3d08 runnable [0x000000000cf0e000]
java.lang.Thread.State: RUNNABLE
at ls.lock.BlockedState$1.run(BlockedState.java:24)
- locked <0x00000000d7d4c460> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

[一个线程占用锁资源，其他线程没法使用]


## 综合案例三：WAITING---（in Object.wait()）（调用 object.wait()造成的）

```java
public class WaitingState {
    private static Object object = new Object();
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
```

thread dump信息：
"t2" prio=6 tid=0x000000000c379800 nid=0x3e28 in Object.wait() [0x000000000d06f000]
java.lang.Thread.State: WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Object.wait(Object.java:503)
at ls.lock.WaitingState$1.run(WaitingState.java:29)
- locked <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c375000 nid=0x3f54 in Object.wait() [0x000000000d18f000]
java.lang.Thread.State: WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Object.wait(Object.java:503)
at ls.lock.WaitingState$1.run(WaitingState.java:29)
- locked <0x00000000d7d4c4a0> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

说明：两个线程都是获取锁然后调用wait()释放锁等待被唤醒。

## 综合案例四：TIMED_WAITING--- in Object.wait()（object.wait(1 * 60 * 1000)造成）

```java
public class TimedWaitingState1 {
    private static Object object = new Object();
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                        try {
                            // 进入等待的同时,会进入释放监视器
                            object.wait(1 * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
 
                }
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
```
thread dump信息：
"t2" prio=6 tid=0x000000000c0e5000 nid=0x3788 in Object.wait() [0x000000000cf9f000]
java.lang.Thread.State: TIMED_WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c978> (a java.lang.Object)
at ls.lock.TimedWaitingState1$1.run(TimedWaitingState1.java:26)
- locked <0x00000000d7d4c978> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c0b9000 nid=0x31a8 in Object.wait() [0x000000000cddf000]
java.lang.Thread.State: TIMED_WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000000d7d4c978> (a java.lang.Object)
at ls.lock.TimedWaitingState1$1.run(TimedWaitingState1.java:26)
- locked <0x00000000d7d4c978> (a java.lang.Object)
at java.lang.Thread.run(Thread.java:744)

## 综合案例五：WAITING (parking)---waiting on condition（通过条件变量condition.await()造成）

```java
public class TimedWaitingState3 {
    // java的显示锁,类似java对象内置的监视器
    private static Lock lock = new ReentrantLock();
    // 锁关联的条件队列(类似于object.wait)
    private static Condition condition = lock.newCondition();
    public static void main(String[] args) {
        Runnable task = new Runnable() {
 
            @Override
            public void run() {
                // 加锁,进入临界区
                lock.lock();
                System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 解锁,退出临界区
                lock.unlock();
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
```
thread dump信息：
"t2" prio=6 tid=0x000000000c3ae800 nid=0x3f1c waiting on condition [0x000000000d03f000]
java.lang.Thread.State: WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ef90> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
at ls.lock.TimedWaitingState3$1.run(TimedWaitingState3.java:51)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c3ad800 nid=0x36a4 waiting on condition [0x000000000cdbe000]
java.lang.Thread.State: WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ef90> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
at ls.lock.TimedWaitingState3$1.run(TimedWaitingState3.java:51)
at java.lang.Thread.run(Thread.java:744)

## 综合案例六：TIMED_WAITING (parking)---waiting on condition（通过条件变量condition.await(5, TimeUnit.MINUTES)造成）

```java
public class TimedWaitingState {
    // java的显示锁,类似java对象内置的监视器
    private static Lock lock = new ReentrantLock();
    // 锁关联的条件队列(类似于object.wait)
    private static Condition condition = lock.newCondition();
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 加锁,进入临界区
                lock.lock();
                System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                try {
                    condition.await(5, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 解锁,退出临界区
                lock.unlock();
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
```
thread dump信息：
"t2" prio=6 tid=0x000000000c321000 nid=0x3bb4 waiting on condition [0x000000000d28f000]
java.lang.Thread.State: TIMED_WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ed18> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2176)
at ls.lock.TimedWaitingState$1.run(TimedWaitingState.java:31)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c320800 nid=0x3d64 waiting on condition [0x000000000d05e000]
java.lang.Thread.State: TIMED_WAITING (parking)
at sun.misc.Unsafe.park(Native Method)
- parking to wait for  <0x00000000d7d4ed18> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)
at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2176)
at ls.lock.TimedWaitingState$1.run(TimedWaitingState.java:31)
at java.lang.Thread.run(Thread.java:744)

## 综合案例七：TIMED_WAITING---waiting on condition（Thread.sleep(1*60*1000)造成的）

```java
public class TimedWaitingState2 {
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程：" + Thread.currentThread().getName()+"获取锁");
                try {
                    Thread.sleep(1*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "t1").start();
        new Thread(task, "t2").start();
    }
}
```
thread dump信息：
"t2" prio=6 tid=0x000000000c632000 nid=0x3f14 waiting on condition [0x000000000d20f000]
java.lang.Thread.State: TIMED_WAITING (sleeping)
at java.lang.Thread.sleep(Native Method)
at ls.lock.WaitOnCondition$1.run(WaitOnCondition.java:17)
at java.lang.Thread.run(Thread.java:744)

"t1" prio=6 tid=0x000000000c551000 nid=0xae8 waiting on condition [0x000000000d07e000]
java.lang.Thread.State: TIMED_WAITING (sleeping)
at java.lang.Thread.sleep(Native Method)
at ls.lock.WaitOnCondition$1.run(WaitOnCondition.java:17)
at java.lang.Thread.run(Thread.java:744)

# 七、附录
## 1、死锁测试代码
```java
public class LeftRightDeadLock
{
    private final Object left = new Object();
    private final Object right = new Object();
 
    public void leftRight() throws Exception
    {
        synchronized (left)
        {
            System.out.println(Thread.currentThread().getName()+"获得left锁");
            Thread.sleep(1000);
            synchronized (right)
            {
                System.out.println("leftRight end!");
            }
        }
    }
 
    public void rightLeft() throws Exception
    {
        synchronized (right)
        {    System.out.println(Thread.currentThread().getName()+"获得right锁");
            Thread.sleep(1000);
            synchronized (left)
            {
                System.out.println("rightLeft end!");
            }
        }
    }
}
public class Test {
    public static void main(String[] args) throws Exception{
        System.out.println("主线程开始"+Thread.currentThread().getName());
        final LeftRightDeadLock lock = new LeftRightDeadLock();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    lock.leftRight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    lock.rightLeft();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        t2.start();
 
        t1.join();
        t2.join();
        System.out.println("主线程结束"+Thread.currentThread().getName());
 
    }
}
```
程序运行输出：
主线程开始main
Thread-1获得right锁
Thread-0获得left锁

## 2、死锁的thread dump文件（在Windows下按Ctrl+pausebreak在控制台输出的）

    2018-07-30 16:24:06
    Full thread dump Java HotSpot(TM) 64-Bit Server VM (24.51-b03 mixed mode):
     
    "Thread-1" prio=6 tid=0x000000000c4ee000 nid=0x2ffc waiting for monitor entry [0x000000000d0ef000]
       java.lang.Thread.State: BLOCKED (on object monitor)
        at ls.lock.LeftRightDeadLock.rightLeft(LeftRightDeadLock.java:32)
        - waiting to lock <0x00000000d7d53ca8> (a java.lang.Object)
        - locked <0x00000000d7d53cb8> (a java.lang.Object)
        at ls.lock.Test$2.run(Test.java:25)
     
    "Thread-0" prio=6 tid=0x000000000c4ed800 nid=0x46f0 waiting for monitor entry [0x000000000d21f000]
       java.lang.Thread.State: BLOCKED (on object monitor)
        at ls.lock.LeftRightDeadLock.leftRight(LeftRightDeadLock.java:20)
        - waiting to lock <0x00000000d7d53cb8> (a java.lang.Object)
        - locked <0x00000000d7d53ca8> (a java.lang.Object)
        at ls.lock.Test$1.run(Test.java:15)
     
    "Service Thread" daemon prio=6 tid=0x000000000c3c9800 nid=0x31a0 runnable [0x0000000000000000]
       java.lang.Thread.State: RUNNABLE
     
    "C2 CompilerThread1" daemon prio=10 tid=0x000000000c3c8800 nid=0x430c waiting on condition [0x0000000000000000]
       java.lang.Thread.State: RUNNABLE
     
    "C2 CompilerThread0" daemon prio=10 tid=0x000000000c3c7800 nid=0x2a48 waiting on condition [0x0000000000000000]
       java.lang.Thread.State: RUNNABLE
     
    "Monitor Ctrl-Break" daemon prio=6 tid=0x000000000c3bf800 nid=0x48ac runnable [0x000000000c95f000]
       java.lang.Thread.State: RUNNABLE
        at java.net.SocketInputStream.socketRead0(Native Method)
        at java.net.SocketInputStream.read(SocketInputStream.java:152)
        at java.net.SocketInputStream.read(SocketInputStream.java:122)
        at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:283)
        at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:325)
        at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:177)
        - locked <0x00000000d7e12a50> (a java.io.InputStreamReader)
        at java.io.InputStreamReader.read(InputStreamReader.java:184)
        at java.io.BufferedReader.fill(BufferedReader.java:154)
        at java.io.BufferedReader.readLine(BufferedReader.java:317)
        - locked <0x00000000d7e12a50> (a java.io.InputStreamReader)
        at java.io.BufferedReader.readLine(BufferedReader.java:382)
        at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)
     
    "Attach Listener" daemon prio=10 tid=0x000000000ac3f800 nid=0x3e48 runnable [0x0000000000000000]
       java.lang.Thread.State: RUNNABLE
     
    "Signal Dispatcher" daemon prio=10 tid=0x000000000abda000 nid=0x3a20 waiting on condition [0x0000000000000000]
       java.lang.Thread.State: RUNNABLE
     
    "Finalizer" daemon prio=8 tid=0x000000000abc2800 nid=0x31f0 in Object.wait() [0x000000000bb8f000]
       java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000d7c05568> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
        - locked <0x00000000d7c05568> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:151)
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:189)
     
    "Reference Handler" daemon prio=10 tid=0x000000000abbf000 nid=0x2354 in Object.wait() [0x000000000beaf000]
       java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000d7c050f0> (a java.lang.ref.Reference$Lock)
        at java.lang.Object.wait(Object.java:503)
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:133)
        - locked <0x00000000d7c050f0> (a java.lang.ref.Reference$Lock)
     
    "main" prio=6 tid=0x00000000024e9000 nid=0x48dc in Object.wait() [0x00000000029cf000]
       java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000d7d53cc8> (a ls.lock.Test$1)
        at java.lang.Thread.join(Thread.java:1280)
        - locked <0x00000000d7d53cc8> (a ls.lock.Test$1)
        at java.lang.Thread.join(Thread.java:1354)
        at ls.lock.Test.main(Test.java:34)
     
    "VM Thread" prio=10 tid=0x000000000abbd800 nid=0x40e8 runnable 
     
    "GC task thread#0 (ParallelGC)" prio=6 tid=0x00000000024ff000 nid=0x40c0 runnable 
     
    "GC task thread#1 (ParallelGC)" prio=6 tid=0x0000000002501000 nid=0x4064 runnable 
     
    "GC task thread#2 (ParallelGC)" prio=6 tid=0x0000000002502800 nid=0x437c runnable 
     
    "GC task thread#3 (ParallelGC)" prio=6 tid=0x0000000002504800 nid=0x1270 runnable 
     
    "VM Periodic Task Thread" prio=10 tid=0x000000000c3cd800 nid=0x34d4 waiting on condition 
     
    JNI global references: 127
     
     
    Found one Java-level deadlock:
    =============================
    "Thread-1":
      waiting to lock monitor 0x000000000c4eff28 (object 0x00000000d7d53ca8, a java.lang.Object),
      which is held by "Thread-0"
    "Thread-0":
      waiting to lock monitor 0x000000000abc96e8 (object 0x00000000d7d53cb8, a java.lang.Object),
      which is held by "Thread-1"
     
    Java stack information for the threads listed above:
    ===================================================
    "Thread-1":
        at ls.lock.LeftRightDeadLock.rightLeft(LeftRightDeadLock.java:32)
        - waiting to lock <0x00000000d7d53ca8> (a java.lang.Object)
        - locked <0x00000000d7d53cb8> (a java.lang.Object)
        at ls.lock.Test$2.run(Test.java:25)
    "Thread-0":
        at ls.lock.LeftRightDeadLock.leftRight(LeftRightDeadLock.java:20)
        - waiting to lock <0x00000000d7d53cb8> (a java.lang.Object)
        - locked <0x00000000d7d53ca8> (a java.lang.Object)
        at ls.lock.Test$1.run(Test.java:15)
     
    Found 1 deadlock.
     
    Heap
     PSYoungGen      total 36352K, used 3751K [0x00000000d7c00000, 0x00000000da480000, 0x0000000100000000)
      eden space 31232K, 12% used [0x00000000d7c00000,0x00000000d7fa9ce0,0x00000000d9a80000)
      from space 5120K, 0% used [0x00000000d9f80000,0x00000000d9f80000,0x00000000da480000)
      to   space 5120K, 0% used [0x00000000d9a80000,0x00000000d9a80000,0x00000000d9f80000)
     ParOldGen       total 82432K, used 0K [0x0000000087400000, 0x000000008c480000, 0x00000000d7c00000)
      object space 82432K, 0% used [0x0000000087400000,0x0000000087400000,0x000000008c480000)
     PSPermGen       total 21504K, used 3201K [0x0000000082200000, 0x0000000083700000, 0x0000000087400000)
      object space 21504K, 14% used [0x0000000082200000,0x0000000082520540,0x0000000083700000)


## 3、一般java程序的线程信息
我们现在写一个简单的hello word程序，代码如下：
```java
public class GcExample {

    private static class E {
        public static final int[] a = new int[10*1024];
    }

    public static void main(String[] args) {
        System.out.println("hello");
        while (true) {
            new E();
        }
    }
}
```

然后使用jvisualvm(或者jmc（Java Mission Control）)attach到这个程序上，展现为如下的情况：
<div align="center"> <img src="/images/thread1-2.png"/> </div><br>

1、RMI开头的线程，负责JVM跟JMC客户端通信，吐出JVM内的运行信息；
2、Attach Listener和Single Dispatcher两个线程，属于信号处理线程，负责接收外部到当前JVM的attach信号，并建立通信用的文件socket；
3、Finalizer线程，用于处理Finalizer队列的线程，在Java中，如果一个对象重写了finalize()方法，那么JVM会为之创建一个对应的Finalizer对象，
所有的Finzlizer对象会构成一个列表，由Finalizer线程统一处理
4、Reference Handler，负责JVM中的引用处理
5、main，我们例子中的业务线程。

我想你现在也有这个疑问——跟上面说的那个分类对不上，有些线程没看到，是的，可能是由于JMC的实现机制，这些线程没有被展示出来，
我们再用jstack命令做一次线程dump，就可以得到如下内容：在windoes下的cmd窗口
jstack 10500 > lishuai.txt

线程信息：
2019-01-04 10:07:02
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.181-b13 mixed mode):

"Service Thread" #10 daemon prio=9 os_prio=0 tid=0x00000000599fc800 nid=0x28ec runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread2" #9 daemon prio=9 os_prio=2 tid=0x00000000599fc000 nid=0x2580 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #8 daemon prio=9 os_prio=2 tid=0x00000000599fb000 nid=0x2758 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #7 daemon prio=9 os_prio=2 tid=0x0000000059a5f000 nid=0x11a0 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Monitor Ctrl-Break" #6 daemon prio=5 os_prio=0 tid=0x0000000059a4c000 nid=0x2654 runnable [0x0000000058c8e000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
	at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
	at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
	- locked <0x00000000d82114e0> (a java.io.InputStreamReader)
	at java.io.InputStreamReader.read(InputStreamReader.java:184)
	at java.io.BufferedReader.fill(BufferedReader.java:161)
	at java.io.BufferedReader.readLine(BufferedReader.java:324)
	- locked <0x00000000d82114e0> (a java.io.InputStreamReader)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

"Attach Listener" #5 daemon prio=5 os_prio=2 tid=0x00000000591da800 nid=0x2124 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 tid=0x0000000057d53000 nid=0x2990 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=1 tid=0x0000000057d41800 nid=0x26ec in Object.wait() [0x00000000591af000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000000d7c08ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
	- locked <0x00000000d7c08ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)

"Reference Handler" #2 daemon prio=10 os_prio=2 tid=0x0000000057cf9800 nid=0x2ab8 in Object.wait() [0x0000000058f6f000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000000d7c06bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x00000000d7c06bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"main" #1 prio=5 os_prio=0 tid=0x00000000023fd800 nid=0x2984 runnable [0x000000000310f000]
   java.lang.Thread.State: RUNNABLE
	at com.ls.thread.GcExample.main(GcExample.java:17)

"VM Thread" os_prio=2 tid=0x0000000057cf2000 nid=0x288c runnable 

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x0000000002413800 nid=0x1c60 runnable 

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x0000000002415000 nid=0x568 runnable 

"GC task thread#2 (ParallelGC)" os_prio=0 tid=0x0000000002418800 nid=0x2524 runnable 

"GC task thread#3 (ParallelGC)" os_prio=0 tid=0x000000000241a000 nid=0x236c runnable 

"VM Periodic Task Thread" os_prio=2 tid=0x0000000059b7b800 nid=0xa9c waiting on condition 

JNI global references: 12



## 4、Thread-Tomcat容器dump分析

### 1、springboot内置Tomcat线程信息

ContainerBackgroundProcessor
container-0
http-nio-8080-exec-1
NioBlockingSelector.BlockPoller-1
http-nio-8080-ClientPoller-0
http-nio-8080-ClientPoller-1
http-nio-8080-Acceptor-0
http-nio-8080-AsyncTimeout



2019-01-04 13:26:45
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.181-b13 mixed mode):

"RMI TCP Connection(6)-10.12.128.193" #57 daemon prio=5 os_prio=0 tid=0x000000005c8fc800 nid=0x12dc runnable [0x000000005b26d000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at java.io.BufferedInputStream.fill(BufferedInputStream.java:246)
	at java.io.BufferedInputStream.read(BufferedInputStream.java:265)
	- locked <0x00000000d95bfb90> (a java.io.BufferedInputStream)
	at java.io.FilterInputStream.read(FilterInputStream.java:83)
	at sun.rmi.transport.tcp.TCPTransport.handleMessages(TCPTransport.java:555)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run0(TCPTransport.java:834)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.lambda$run$0(TCPTransport.java:688)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler$$Lambda$5/1748224055.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run(TCPTransport.java:687)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)

"JMX server connection timeout 56" #56 daemon prio=5 os_prio=0 tid=0x000000005c8fd000 nid=0x38b4 in Object.wait() [0x000000005fccf000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at com.sun.jmx.remote.internal.ServerCommunicatorAdmin$Timeout.run(ServerCommunicatorAdmin.java:168)
	- locked <0x00000000d880cf58> (a [I)
	at java.lang.Thread.run(Thread.java:748)

"DestroyJavaVM" #50 prio=5 os_prio=0 tid=0x000000005c90b000 nid=0x41f0 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"http-nio-8080-AsyncTimeout" #48 daemon prio=5 os_prio=0 tid=0x000000005c90a000 nid=0x42a0 waiting on condition [0x000000005f5ff000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.coyote.AbstractProtocol$AsyncTimeout.run(AbstractProtocol.java:1143)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-Acceptor-0" #47 daemon prio=5 os_prio=0 tid=0x000000005c909800 nid=0x3cc4 runnable [0x000000005f39e000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:422)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:250)
	- locked <0x00000000de902ec8> (a java.lang.Object)
	at org.apache.tomcat.util.net.NioEndpoint$Acceptor.run(NioEndpoint.java:455)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-ClientPoller-1" #46 daemon prio=5 os_prio=0 tid=0x000000005c908800 nid=0x3fac runnable [0x000000005f28f000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll0(Native Method)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll(WindowsSelectorImpl.java:296)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.access$400(WindowsSelectorImpl.java:278)
	at sun.nio.ch.WindowsSelectorImpl.doSelect(WindowsSelectorImpl.java:159)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000de920178> (a sun.nio.ch.Util$3)
	- locked <0x00000000de920168> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000de9035d8> (a sun.nio.ch.WindowsSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:798)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-ClientPoller-0" #45 daemon prio=5 os_prio=0 tid=0x000000005c908000 nid=0x201c runnable [0x000000005f08e000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll0(Native Method)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll(WindowsSelectorImpl.java:296)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.access$400(WindowsSelectorImpl.java:278)
	at sun.nio.ch.WindowsSelectorImpl.doSelect(WindowsSelectorImpl.java:159)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000de876268> (a sun.nio.ch.Util$3)
	- locked <0x00000000de876258> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000de876018> (a sun.nio.ch.WindowsSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:798)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-10" #44 daemon prio=5 os_prio=0 tid=0x000000005c907000 nid=0x3680 waiting on condition [0x000000005eaee000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-9" #43 daemon prio=5 os_prio=0 tid=0x000000005c906800 nid=0x3fc4 waiting on condition [0x000000005ee4e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-8" #42 daemon prio=5 os_prio=0 tid=0x000000005c905800 nid=0x3fb0 waiting on condition [0x000000005ec5f000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-7" #41 daemon prio=5 os_prio=0 tid=0x000000005c904800 nid=0x3fa0 waiting on condition [0x000000005e76e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-6" #40 daemon prio=5 os_prio=0 tid=0x000000005c904000 nid=0x43e8 waiting on condition [0x000000005e88e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-5" #39 daemon prio=5 os_prio=0 tid=0x000000005c903000 nid=0x3cd0 waiting on condition [0x000000005e5ee000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-4" #38 daemon prio=5 os_prio=0 tid=0x000000005c902800 nid=0x3ccc waiting on condition [0x000000005d28f000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-3" #37 daemon prio=5 os_prio=0 tid=0x000000005c901800 nid=0x4180 waiting on condition [0x000000005e49f000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-2" #36 daemon prio=5 os_prio=0 tid=0x000000005c901000 nid=0x2d30 waiting on condition [0x000000005e30e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8080-exec-1" #35 daemon prio=5 os_prio=0 tid=0x000000005c900000 nid=0x3fbc waiting on condition [0x000000005e1fe000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000de872998> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

"NioBlockingSelector.BlockPoller-1" #34 daemon prio=5 os_prio=0 tid=0x000000005c8ff800 nid=0x3cc8 runnable [0x000000005e0bf000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll0(Native Method)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll(WindowsSelectorImpl.java:296)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.access$400(WindowsSelectorImpl.java:278)
	at sun.nio.ch.WindowsSelectorImpl.doSelect(WindowsSelectorImpl.java:159)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000de89aa88> (a sun.nio.ch.Util$3)
	- locked <0x00000000de89aa78> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000de89a918> (a sun.nio.ch.WindowsSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller.run(NioBlockingSelector.java:298)

"container-0" #31 prio=5 os_prio=0 tid=0x000000005c8fe800 nid=0x3bd4 waiting on condition [0x000000005dcdf000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.catalina.core.StandardServer.await(StandardServer.java:427)
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer$1.run(TomcatWebServer.java:182)

"ContainerBackgroundProcessor[StandardEngine[Tomcat]]" #30 daemon prio=5 os_prio=0 tid=0x000000005c8fe000 nid=0x25e4 waiting on condition [0x000000005b79f000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.run(ContainerBase.java:1357)
	at java.lang.Thread.run(Thread.java:748)

"RMI TCP Connection(5)-10.12.128.193" #17 daemon prio=5 os_prio=0 tid=0x000000005a45a800 nid=0x4250 runnable [0x000000005b5ed000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at java.io.BufferedInputStream.fill(BufferedInputStream.java:246)
	at java.io.BufferedInputStream.read(BufferedInputStream.java:265)
	- locked <0x00000000d88034d0> (a java.io.BufferedInputStream)
	at java.io.FilterInputStream.read(FilterInputStream.java:83)
	at sun.rmi.transport.tcp.TCPTransport.handleMessages(TCPTransport.java:555)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run0(TCPTransport.java:834)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.lambda$run$0(TCPTransport.java:688)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler$$Lambda$5/1748224055.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run(TCPTransport.java:687)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)

"RMI Scheduler(0)" #16 daemon prio=5 os_prio=0 tid=0x000000005a456000 nid=0x41fc waiting on condition [0x000000005b3cf000]
   java.lang.Thread.State: TIMED_WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000008785d5e8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:215)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2078)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1093)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:809)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)

"RMI TCP Accept-0" #14 daemon prio=5 os_prio=0 tid=0x000000005a42c000 nid=0x41ec runnable [0x000000005af9f000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.accept0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketAccept(DualStackPlainSocketImpl.java:131)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
	at java.net.PlainSocketImpl.accept(PlainSocketImpl.java:199)
	- locked <0x000000008785d9a8> (a java.net.SocksSocketImpl)
	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
	at java.net.ServerSocket.accept(ServerSocket.java:513)
	at sun.management.jmxremote.LocalRMIServerSocketFactory$1.accept(LocalRMIServerSocketFactory.java:52)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.executeAcceptLoop(TCPTransport.java:405)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.run(TCPTransport.java:377)
	at java.lang.Thread.run(Thread.java:748)

"RMI TCP Accept-56204" #13 daemon prio=5 os_prio=0 tid=0x000000005a425800 nid=0x3898 runnable [0x000000005adfe000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.accept0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketAccept(DualStackPlainSocketImpl.java:131)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
	at java.net.PlainSocketImpl.accept(PlainSocketImpl.java:199)
	- locked <0x000000008785de68> (a java.net.SocksSocketImpl)
	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
	at java.net.ServerSocket.accept(ServerSocket.java:513)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.executeAcceptLoop(TCPTransport.java:405)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.run(TCPTransport.java:377)
	at java.lang.Thread.run(Thread.java:748)

"RMI TCP Accept-0" #12 daemon prio=5 os_prio=0 tid=0x000000005a41f800 nid=0x4204 runnable [0x000000005ac9e000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.accept0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketAccept(DualStackPlainSocketImpl.java:131)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
	at java.net.PlainSocketImpl.accept(PlainSocketImpl.java:199)
	- locked <0x0000000087851720> (a java.net.SocksSocketImpl)
	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
	at java.net.ServerSocket.accept(ServerSocket.java:513)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.executeAcceptLoop(TCPTransport.java:405)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.run(TCPTransport.java:377)
	at java.lang.Thread.run(Thread.java:748)

"Service Thread" #10 daemon prio=9 os_prio=0 tid=0x0000000059237000 nid=0x3aec runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread2" #9 daemon prio=9 os_prio=2 tid=0x0000000059203000 nid=0x4078 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #8 daemon prio=9 os_prio=2 tid=0x00000000591f9800 nid=0x3100 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #7 daemon prio=9 os_prio=2 tid=0x00000000591a1000 nid=0x1be4 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Monitor Ctrl-Break" #6 daemon prio=5 os_prio=0 tid=0x0000000059154000 nid=0x4098 runnable [0x000000005990f000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
	at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
	at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
	- locked <0x00000000879bb030> (a java.io.InputStreamReader)
	at java.io.InputStreamReader.read(InputStreamReader.java:184)
	at java.io.BufferedReader.fill(BufferedReader.java:161)
	at java.io.BufferedReader.readLine(BufferedReader.java:324)
	- locked <0x00000000879bb030> (a java.io.InputStreamReader)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

"Attach Listener" #5 daemon prio=5 os_prio=2 tid=0x0000000058f4a800 nid=0x4140 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 tid=0x0000000057aef000 nid=0x3c74 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=1 tid=0x0000000057acf000 nid=0x26e0 in Object.wait() [0x0000000058bdf000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000000877988e8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
	- locked <0x00000000877988e8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)

"Reference Handler" #2 daemon prio=10 os_prio=2 tid=0x0000000057a87800 nid=0x4134 in Object.wait() [0x0000000058f3f000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000000877cce60> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x00000000877cce60> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=2 tid=0x0000000057a80000 nid=0x3aac runnable 

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x0000000002420800 nid=0x3ca4 runnable 

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x0000000002422800 nid=0x3d88 runnable 

"GC task thread#2 (ParallelGC)" os_prio=0 tid=0x0000000002426000 nid=0x3cbc runnable 

"GC task thread#3 (ParallelGC)" os_prio=0 tid=0x0000000002427800 nid=0x3564 runnable 

"VM Periodic Task Thread" os_prio=2 tid=0x000000005a430000 nid=0x41d8 waiting on condition 

JNI global references: 933




### 2、Tomcat容器中部署的web程序

需要关注的：
http-bio-8008-exec-3
http-bio-8008-Acceptor-0
http-bio-8008-AsyncTimeout
ContainerBackgroundProcessor

Druid-ConnectionPool-Destroy-767849847
Druid-ConnectionPool-Create-767849847[成对出现]
Abandoned connection cleanup thread
JSF-Future-Checker-CB-1-T-1
Timer-0
DefaultQuartzScheduler_QuartzSchedulerThread[Quartz定时器任务线程]
DefaultQuartzScheduler_Worker
javamelody

GC Daemon
main    ：org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:455) [主线程]

**全部内容**
2019-01-04 20:43:14
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

JNI global references: 337








[参考](https://mp.weixin.qq.com/s?__biz=MzIwMzY1OTU1NQ==&mid=2247485264&idx=1&sn=e26328e751063eeaca741d02fdd7a0f8&chksm=96cd471ca1bace0af19553394810e6cde0aa69f32f53e986a0cbe7888f6066ae7b07914550c7&mpshare=1&scene=24&srcid=0104abix9tU0p6oxRhSG0xVt#rd)

http://www.importnew.com/23601.html
https://jameswxx.iteye.com/blog/1041173
https://juejin.im/post/5b31b510e51d4558a426f7e9
https://www.cnblogs.com/kongzhongqijing/articles/4152072.html
https://www.cnblogs.com/zhengyun_ustc/archive/2013/01/06/dumpanalysis.html
https://segmentfault.com/a/1190000000615690
https://blog.csdn.net/rachel_luo/article/details/8920596
https://www.cnblogs.com/lupeng2010/p/6145712.html
https://blog.csdn.net/l1394049664/article/details/81290910
https://blog.csdn.net/lmb55/article/details/79349680
https://www.jianshu.com/p/a4bcb36d3c5f
https://www.cnblogs.com/toSeeMyDream/p/7151635.html
https://www.javatang.com/archives/2017/10/19/51301886.html

