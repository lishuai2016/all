---
title: CMS收集过程和日志分析
categories: 
- jvm
tags:
---

CMS收集过程和日志分析

[参考](http://www.cnblogs.com/zhangxiaoguang/p/5792468.html)


# CMS的执行过程可以分为以下6个阶段
- 初始标记(STW)[CMS Initial Mark]
- 并发标记[CMS-concurrent-mark]
- 并发预清理[CMS-concurrent-preclean、CMS-concurrent-abortable-preclean]
- 重新标记(STW)[CMS Final Remark]
- 并发清理[CMS-concurrent-sweep]
- 重置[CMS-concurrent-reset]

让我们先简单的看下整个堆年轻代和年老代的垃圾收集器组合（以下配合java8完美支持，其他版本可能稍有不同），其中标红线的则是我们今天要着重讲的内容：
<div align="center"> <img src="/images/cms日志分析1.jpg"/> </div><br>


[ParNew and CMS组合]
"Concurrent Mark and Sweep" 是CMS的全称，官方给予的名称是：“Mostly Concurrent Mark and Sweep Garbage Collector”;
年轻代：采用 stop-the-world mark-copy 算法；
年老代：采用 Mostly Concurrent mark-sweep 算法；
设计目标：年老代收集的时候避免长时间的暂停；
能够达成该目标主要因为以下两个原因：
1  它不会花时间整理压缩年老代，而是维护了一个叫做 free-lists 的数据结构，该数据结构用来管理那些回收再利用的内存空间；
2  mark-sweep分为多个阶段，其中一大部分阶段GC的工作是和Application threads的工作同时进行的（当然，gc线程会和用户线程竞争CPU的时间），
默认的GC的工作线程为你服务器物理CPU核数的1/4；

备注：当你的服务器是多核同时你的目标是低延时，那该GC的搭配则是你的不二选择。


# GC日志初体验
首先对整个GC日志有一个大概的认知
2016-08-23T02:23:07.219-0200: 64.322: [GC (Allocation Failure) 64.322: [ParNew: 613404K->68068K(613440K), 0.1020465 secs] 10885349K->10880154K(12514816K), 0.1021309 secs] [Times: user=0.78 sys=0.01, real=0.11 secs]
2016-08-23T02:23:07.321-0200: 64.425: [GC (CMS Initial Mark) [1 CMS-initial-mark: 10812086K(11901376K)] 10887844K(12514816K), 0.0001997 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2016-08-23T02:23:07.321-0200: 64.425: [CMS-concurrent-mark-start]
2016-08-23T02:23:07.357-0200: 64.460: [CMS-concurrent-mark: 0.035/0.035 secs] [Times: user=0.07 sys=0.00, real=0.03 secs]
2016-08-23T02:23:07.357-0200: 64.460: [CMS-concurrent-preclean-start]
2016-08-23T02:23:07.373-0200: 64.476: [CMS-concurrent-preclean: 0.016/0.016 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
2016-08-23T02:23:07.373-0200: 64.476: [CMS-concurrent-abortable-preclean-start]
2016-08-23T02:23:08.446-0200: 65.550: [CMS-concurrent-abortable-preclean: 0.167/1.074 secs] [Times: user=0.20 sys=0.00, real=1.07 secs]
2016-08-23T02:23:08.447-0200: 65.550: [GC (CMS Final Remark) [YG occupancy: 387920 K (613440 K)]65.550: [Rescan (parallel) , 0.0085125 secs]65.559: [weak refs processing, 0.0000243 secs]65.559: [class unloading, 0.0013120 secs]65.560: [scrub symbol table, 0.0008345 secs]65.561: [scrub string table, 0.0001759 secs][1 CMS-remark: 10812086K(11901376K)] 11200006K(12514816K), 0.0110730 secs] [Times: user=0.06 sys=0.00, real=0.01 secs]
2016-08-23T02:23:08.458-0200: 65.561: [CMS-concurrent-sweep-start]
2016-08-23T02:23:08.485-0200: 65.588: [CMS-concurrent-sweep: 0.027/0.027 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
2016-08-23T02:23:08.485-0200: 65.589: [CMS-concurrent-reset-start]
2016-08-23T02:23:08.497-0200: 65.601: [CMS-concurrent-reset: 0.012/0.012 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]


# 1、Minor GC
2016-08-23T02:23:07.219-0200【1】: 64.3222【2】:[GC3【3】(Allocation Failure【4】) 64.322: [ParNew【5】: 613404K->68068K【6】(613440K)【7】,
 0.1020465 secs【8】] 10885349K->10880154K【9】(12514816K)【10】, 0.1021309 secs【11】][Times: user=0.78 sys=0.01, real=0.11 secs]【12】

【1】、2016-08-23T02:23:07.219-0200 – GC发生的时间；
【2】、64.322 – GC开始，相对JVM启动的相对时间，单位是秒；
【3】、GC – 区别MinorGC和FullGC的标识，这次代表的是MinorGC;
【4】、Allocation Failure – MinorGC的原因，在这个case里边，由于年轻代不满足申请的空间，因此触发了MinorGC;
【5】、ParNew – 收集器的名称，它预示了年轻代使用一个并行的 mark-copy stop-the-world 垃圾收集器；
【6】、613404K->68068K – 收集前后年轻代的使用情况；
【7】、(613440K) – 整个年轻代的容量；
【8】、0.1020465 secs – 这个解释用原滋原味的解释：Duration for the collection w/o final cleanup.
【9】、10885349K->10880154K – 收集前后整个堆的使用情况；
【10】、(12514816K) – 整个堆的容量；
【11】、0.1021309 secs – ParNew收集器标记和复制年轻代活着的对象所花费的时间（包括和老年代通信的开销、对象晋升到老年代时间、垃圾收集周期结束一些最后的清理对象等的花销）；
【12】、[Times: user=0.78 sys=0.01, real=0.11 secs] – GC事件在不同维度的耗时，具体的用英文解释起来更加合理:
    user – Total CPU time that was consumed by Garbage Collector threads during this collection
    sys – Time spent in OS calls or waiting for system event
    real – Clock time for which your application was stopped. With Parallel GC this number should be close to (user time + system time) 
    divided by the number of threads used by the Garbage Collector. In this particular case 8 threads were used. 
    Note that due to some activities not being parallelizable, it always exceeds the ratio by a certain amount.
我们来分析下对象晋升问题：
开始的时候：整个堆的大小是 10885349K，年轻代大小是613404K，这说明老年代大小是 10885349-613404=10271945K，
收集完成之后：整个堆的大小是 10880154K，年轻代大小是68068K，这说明老年代大小是 10880154-68068=10812086K，
老年代的大小增加了：10812086-10271945=608209K，也就是说 年轻代到年老代promot了608209K的数据；图形分析：
<div align="center"> <img src="/images/cms日志分析2.jpg"/> </div><br>


## 2、Full/Major GC 
2016-08-23T11:23:07.321-0200: 64.425: [GC (CMS Initial Mark)【1】 [1 CMS-initial-mark: 10812086K(11901376K)] 10887844K(12514816K), 0.0001997 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2016-08-23T11:23:07.321-0200: 64.425: [CMS-concurrent-mark-start]
2016-08-23T11:23:07.357-0200: 64.460: [CMS-concurrent-mark【2】: 0.035/0.035 secs] [Times: user=0.07 sys=0.00, real=0.03 secs]
2016-08-23T11:23:07.357-0200: 64.460: [CMS-concurrent-preclean-start]
2016-08-23T11:23:07.373-0200: 64.476: [CMS-concurrent-preclean【3】: 0.016/0.016 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
2016-08-23T11:23:07.373-0200: 64.476: [CMS-concurrent-abortable-preclean-start]
2016-08-23T11:23:08.446-0200: 65.550: [CMS-concurrent-abortable-preclean【4】: 0.167/1.074 secs] [Times: user=0.20 sys=0.00, real=1.07 secs]
2016-08-23T11:23:08.447-0200: 65.550: [GC (CMS Final Remark【5】)
[YG occupancy: 387920 K (613440 K)]65.550: [Rescan (parallel) , 0.0085125 secs]65.559: 
[weak refs processing, 0.0000243 secs]65.559: [class unloading, 0.0013120 secs]65.560: 
[scrub symbol table, 0.0008345 secs]65.561: [scrub string table, 0.0001759 secs][1 CMS-remark: 10812086K(11901376K)] 11200006K(12514816K), 0.0110730 secs] 
[Times: user=0.06 sys=0.00, real=0.01 secs]
2016-08-23T11:23:08.458-0200: 65.561: [CMS-concurrent-sweep-start]
2016-08-23T11:23:08.485-0200: 65.588: [CMS-concurrent-sweep【6】: 0.027/0.027 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
2016-08-23T11:23:08.485-0200: 65.589: [CMS-concurrent-reset-start]
2016-08-23T11:23:08.497-0200: 65.601: [CMS-concurrent-reset【7】: 0.012/0.012 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]

### Phase 1: Initial Mark
这是CMS中两次stop-the-world事件中的一次。它有两个目标：
- 标记GC Roots可达的老年代对象；
- 遍历新生代对象，标记可达的老年代对象；
该阶段进行可达性分析，标记GC ROOT能[直接关联到的对象]。注意是直接关联,[间接关联的对象]在下一阶段标记。

标记结果如下：
<div align="center"> <img src="/images/cms日志分析3.jpg"/> </div><br>

分析：
2016-08-23T11:23:07.321-0200: 64.42【1】: [GC (CMS Initial Mark【2】[1 CMS-initial-mark: 10812086K【3】(11901376K)【4】] 
10887844K【5】(12514816K)【6】, 0.0001997 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]【7】

【1】、016-08-23T11:23:07.321-0200: 64.42 – GC事件开始，包括时钟时间和相对JVM启动时候的相对时间，下边所有的阶段改时间的含义相同；
【2】、CMS Initial Mark – 收集阶段，开始收集所有的GC Roots和直接引用到的对象；
【3】、10812086K – 当前老年代使用情况；
【4】、(11901376K) – 老年代可用容量；
【5】、10887844K – 当前整个堆的使用情况；
【6】、(12514816K) – 整个堆的容量；
【7】、0.0001997 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] – 时间计量；

### Phase 2: Concurrent Mark
该阶段进行[GC ROOT TRACING]，在第一个阶段被暂停的线程重新开始运行。由前阶段标记过的对象出发，所有可到达的对象都在本阶段中标记

这个阶段会遍历整个老年代并且标记所有存活的对象，从“初始化标记”阶段找到的GC Roots开始。并发标记的特点是和应用程序线程同时运行。
并不是老年代的所有存活对象都会被标记，因为标记的同时应用程序会改变一些对象的引用等。
标记结果如下：
<div align="center"> <img src="/images/cms日志分析4.jpg"/> </div><br>

在上边的图中，一个引用的箭头已经远离了当前对象（current obj）

分析：
2016-08-23T11:23:07.321-0200: 64.425: [CMS-concurrent-mark-start]
2016-08-23T11:23:07.357-0200: 64.460: [CMS-concurrent-mark【1】: 035/0.035 secs【2】] [Times: user=0.07 sys=0.00, real=0.03 secs]【3】
【1】、CMS-concurrent-mark – 并发收集阶段，这个阶段会遍历整个年老代并且标记活着的对象；
【2】、035/0.035 secs – 展示该阶段持续的时间和时钟时间；
【3】、[Times: user=0.07 sys=0.00, real=0.03 secs] – 同上

### Phase 3: Concurrent Preclean
这个阶段又是一个并发阶段，和应用线程并行运行，不会中断他们。前一个阶段在并行运行的时候，一些对象的引用已经发生了变化，
当这些引用发生变化的时候，JVM会标记堆的这个区域为Dirty Card(包含被标记但是改变了的对象，被认为"dirty")，这就是 Card Marking。
如下图：
<div align="center"> <img src="/images/cms日志分析5.jpg"/> </div><br>

在pre-clean阶段，那些能够从dirty card对象到达的对象也会被标记，这个标记做完之后，dirty card标记就会被清除了，如下：
<div align="center"> <img src="/images/cms日志分析6.jpg"/> </div><br>

另外，一些必要的清扫工作也会做，还会做一些final remark阶段需要的准备工作；

分析
2016-08-23T11:23:07.357-0200: 64.460: [CMS-concurrent-preclean-start]
2016-08-23T11:23:07.373-0200: 64.476: [CMS-concurrent-preclean【1】: 0.016/0.016 secs【2】] [Times: user=0.02 sys=0.00, real=0.02 secs]【3】
【1】、CMS-concurrent-preclean – 这个阶段负责前一个阶段标记了又发生改变的对象标记；
【2】、0.016/0.016 secs – 展示该阶段持续的时间和时钟时间；
【3】、[Times: user=0.02 sys=0.00, real=0.02 secs] – 同上

### Phase 4: Concurrent Abortable Preclean
又一个并发阶段不会停止应用程序线程。这个阶段尝试着去承担STW的Final Remark阶段足够多的工作。这个阶段持续的时间依赖好多的因素，
由于这个阶段是重复的做相同的事情直到发生aboart的条件（比如：重复的次数、多少量的工作、持续的时间等等）之一才会停止。

2016-08-23T11:23:07.373-0200: 64.476: [CMS-concurrent-abortable-preclean-start]
2016-08-23T11:23:08.446-0200: 65.550: [CMS-concurrent-abortable-preclean【1】: 0.167/1.074 secs【2】] [Times: user=0.20 sys=0.00, real=1.07 secs]【3】
【1】、CMS-concurrent-abortable-preclean – 可终止的并发预清理；
【2】、0.167/1.074 secs – 展示该阶段持续的时间和时钟时间（It is interesting to note that the user time reported is a lot smaller than clock time. 
Usually we have seen that real time is less than user time, meaning that some work was done in parallel and so elapsed clock time is less than used CPU time. 
Here we have a little amount of work – for 0.167 seconds of CPU time, and garbage collector threads were doing a lot of waiting.
 Essentially, they were trying to stave off for as long as possible before having to do an STW pause. By default, this phase may last for up to 5 seconds）；
【3】、[Times: user=0.20 sys=0.00, real=1.07 secs] – 同上
这个阶段很大程度的影响着即将来临的Final Remark的停顿，有相当一部分重要的 configuration options 和 失败的模式；

### Phase 5: Final Remark
这个阶段是CMS中第二个并且是最后一个STW的阶段。该阶段的任务是完成标记整个年老代的所有的存活对象。由于之前的预处理是并发的，它可能跟不上应用程序改变的速度，
这个时候，STW是非常需要的来完成这个严酷考验的阶段。
通常CMS尽量运行Final Remark阶段在年轻代是足够干净的时候，目的是消除紧接着的连续的几个STW阶段。

分析：
2016-08-23T11:23:08.447-0200: 65.550【1】: [GC (CMS Final Remark【2】) [YG occupancy: 387920 K (613440 K)【3】]65.550: 
[Rescan (parallel) , 0.0085125 secs]【4】65.559: [weak refs processing, 0.0000243 secs]65.559【5】: 
[class unloading, 0.0013120 secs]65.560【6】: [scrub string table, 0.0001759 secs【7】][1 CMS-remark: 10812086K(11901376K)【8】] 
11200006K(12514816K) 【9】, 0.0110730 secs【10】] [[Times: user=0.06 sys=0.00, real=0.01 secs]【11】

【1】、2016-08-23T11:23:08.447-0200: 65.550 – 同上；
【2】、CMS Final Remark – 收集阶段，这个阶段会标记老年代全部的存活对象，包括那些在并发标记阶段更改的或者新创建的引用对象；
【3】、YG occupancy: 387920 K (613440 K) – 年轻代当前占用情况和容量；
【4】、[Rescan (parallel) , 0.0085125 secs] – 这个阶段在应用停止的阶段完成存活对象的标记工作；
【5】、weak refs processing, 0.0000243 secs]65.559 – 第一个子阶段，随着这个阶段的进行处理弱引用；
【6】、class unloading, 0.0013120 secs]65.560 – 第二个子阶段(that is unloading the unused classes, with the duration and timestamp of the phase);
【7】、scrub string table, 0.0001759 secs – 最后一个子阶段（that is cleaning up symbol and string tables which hold class-level metadata and internalized string respectively）
【8】、10812086K(11901376K) – 在这个阶段之后老年代占有的内存大小和老年代的容量；
【9】、11200006K(12514816K) – 在这个阶段之后整个堆的内存大小和整个堆的容量；
【10】、0.0110730 secs – 这个阶段的持续时间；
【11】、[Times: user=0.06 sys=0.00, real=0.01 secs] – 同上；
通过以上5个阶段的标记，老年代所有存活的对象已经被标记并且现在要通过Garbage Collector采用清扫的方式回收那些不能用的对象了。

### Phase 6: Concurrent Sweep
和应用线程同时进行，不需要STW。这个阶段的目的就是移除那些不用的对象，回收他们占用的空间并且为将来使用。
如图：
<div align="center"> <img src="/images/cms日志分析7.jpg"/> </div><br>

分析：
2016-08-23T11:23:08.458-0200: 65.561: [CMS-concurrent-sweep-start] 2016-08-23T11:23:08.485-0200: 65.588: 
[CMS-concurrent-sweep【1】: 0.027/0.027 secs【2】] [[Times: user=0.03 sys=0.00, real=0.03 secs] 【3】

【1】、CMS-concurrent-sweep – 这个阶段主要是清除那些没有标记的对象并且回收空间；
【2】、0.027/0.027 secs – 展示该阶段持续的时间和时钟时间；
【3】、[Times: user=0.03 sys=0.00, real=0.03 secs] – 同上

### Phase 7: Concurrent Reset
这个阶段并发执行，重新设置CMS算法内部的数据结构，准备下一个CMS生命周期的使用。

2016-08-23T11:23:08.485-0200: 65.589: [CMS-concurrent-reset-start] 2016-08-23T11:23:08.497-0200: 65.601:
[CMS-concurrent-reset【1】: 0.012/0.012 secs【2】] [[Times: user=0.01 sys=0.00, real=0.01 secs]【3】

【1】、CMS-concurrent-reset – 这个阶段重新设置CMS算法内部的数据结构，为下一个收集阶段做准备；
【2】、0.012/0.012 secs – 展示该阶段持续的时间和时钟时间；
【3】、[Times: user=0.01 sys=0.00, real=0.01 secs] – 同上
