# cms垃圾收集器

[图解 CMS 垃圾回收机制，你值得拥有](http://www.importnew.com/27822.html)


# CMS的执行过程可以分为以下6个阶段
- 初始标记(STW)[CMS Initial Mark]
- 并发标记[CMS-concurrent-mark]
- 并发预清理[CMS-concurrent-preclean、CMS-concurrent-abortable-preclean]
- 重新标记(STW)[CMS Final Remark]
- 并发清理[CMS-concurrent-sweep]
- 重置[CMS-concurrent-reset]

# 1、什么是CMS？
CMS全称 Concurrent Mark Sweep，是一款并发的、使用标记-清除算法的垃圾回收器，
如果老年代使用CMS垃圾回收器，需要添加虚拟机参数”-XX:+UseConcMarkSweepGC”。

# 2、使用场景：
GC过程短暂停，适合对时延要求较高的服务，用户线程不允许长时间的停顿。
缺点：服务长时间运行，造成严重的内存碎片化。另外，算法实现比较复杂（如果也算缺点的话）

# 3、实现机制
根据GC的触发机制分为：[周期性Old GC（被动）]和[主动Old GC]

## 3.1、周期性Old GC
周期性Old GC，执行的逻辑也叫Background Collect，对老年代进行回收，在GC日志中比较常见，
由后台线程ConcurrentMarkSweepThread循环判断（默认2s）是否需要触发。
<div align="center"> <img src="/images/cms1.png"/> </div><br>

### （1）触发条件
1、如果没有设置-XX:+UseCMSInitiatingOccupancyOnly，虚拟机会根据收集的数据决定是否触发（建议线上环境带上这个参数，不然会加大问题排查的难度）。
2、老年代使用率达到阈值 CMSInitiatingOccupancyFraction，默认92%。
3、永久代的使用率达到阈值 CMSInitiatingPermOccupancyFraction，默认92%，前提是开启 CMSClassUnloadingEnabled。
4、新生代的晋升担保失败。
[晋升担保失败]
老年代是否有足够的空间来容纳全部的新生代对象或历史平均晋升到老年代的对象，如果不够的话，就提早进行一次老年代的回收，防止下次进行YGC的时候发生晋升失败。

### （2）周期性Old GC过程
当条件满足时，采用“标记-清理”算法对老年代进行回收，过程可以说很简单，标记出存活对象，清理掉垃圾对象，
但是为了实现整个过程的低延迟，实际算法远远没这么简单，整个过程分为如下几个部分：
<div align="center"> <img src="/images/cms2.png"/> </div><br>

对象在标记过程中，根据标记情况，分成三类：
白色对象，表示自身未被标记；
灰色对象，表示自身被标记，但内部引用未被处理；
黑色对象，表示自身被标记，内部引用都被处理；
<div align="center"> <img src="/images/cms3.png"/> </div><br>

假设发生Background Collect时，Java堆的对象分布如下：
<div align="center"> <img src="/images/cms4.png"/> </div><br>

1、InitialMarking（初始化标记，整个过程STW）
该阶段单线程执行，主要分分为两步：
- 标记GC Roots可达的老年代对象；
- 遍历新生代对象，标记可达的老年代对象；
该过程结束后，对象分布如下：
<div align="center"> <img src="/images/cms5.png"/> </div><br>


2、Marking（并发标记）
该阶段GC线程和应用线程并发执行，遍历InitialMarking阶段标记出来的存活对象，然后继续递归标记这些对象可达的对象。
因为该阶段并发执行的，在运行期间可能发生新生代的对象晋升到老年代、或者是直接在老年代分配对象、或者更新老年代对象的引用关系等等，
对于这些对象，都是需要进行重新标记的，否则有些对象就会被遗漏，发生漏标的情况。
为了提高重新标记的效率，该阶段会把上述对象所在的Card标识为Dirty，后续只需扫描这些Dirty Card的对象，避免扫描整个老年代。
<div align="center"> <img src="/images/cms6.png"/> </div><br>


3-1、Precleaning（预清理）
通过参数CMSPrecleaningEnabled选择关闭该阶段，默认启用，主要做两件事情：
- 处理新生代已经发现的引用，比如在并发阶段，在Eden区中分配了一个A对象，A对象引用了一个老年代对象B（这个B之前没有被标记），在这个阶段就会标记对象B为活跃对象。
- 在并发标记阶段，如果老年代中有对象内部引用发生变化，会把所在的Card标记为Dirty（其实这里并非使用CardTable，而是一个类似的数据结构，叫ModUnionTalble），通过扫描这些Table，重新标记那些在并发标记阶段引用被更新的对象（晋升到老年代的对象、原本就在老年代的对象）

3-2、AbortablePreclean（可中断的预清理）
该阶段发生的前提是，新生代Eden区的内存使用量大于参数CMSScheduleRemarkEdenSizeThreshold 默认是2M，如果新生代的对象太少，就没有必要执行该阶段，直接执行重新标记阶段。

[为什么需要这个阶段，存在的价值是什么？]
因为CMS GC的终极目标是降低垃圾回收时的暂停时间，所以在该阶段要尽最大的努力去处理那些在并发阶段被应用线程更新的老年代对象，
这样在暂停的重新标记阶段就可以少处理一些，暂停时间也会相应的降低。

在该阶段，主要循环的做两件事：
- 处理 From 和 To 区的对象，标记可达的老年代对象
- 和上一个阶段一样，扫描处理Dirty Card中的对象
当然了，这个逻辑不会一直循环下去，打断这个循环的条件有三个：
(1)可以设置最多循环的次数 CMSMaxAbortablePrecleanLoops，默认是0，意思没有循环次数的限制。
(2)如果执行这个逻辑的时间达到了阈值CMSMaxAbortablePrecleanTime，默认是5s，会退出循环。
(3)如果新生代Eden区的内存使用率达到了阈值CMSScheduleRemarkEdenPenetration，默认50%，会退出循环。
（这个条件能够成立的前提是，在进行Precleaning时，Eden区的使用率小于十分之一）


如果在循环退出之前，发生了一次YGC，对于后面的Remark阶段来说，大大减轻了扫描年轻代的负担，但是发生YGC并非人为控制，所以只能祈祷这5s内可以来一次YGC。

    1678.150: [CMS-concurrent-preclean-start]
    1678.186: [CMS-concurrent-preclean: 0.044/0.055 secs]
    1678.186: [CMS-concurrent-abortable-preclean-start]
    1678.365: [GC 1678.465: [ParNew: 2080530K->1464K(2044544K), 0.0127340 secs] 1389293K->306572K(2093120K), 0.0167509 secs]
    1680.093: [CMS-concurrent-abortable-preclean: 1.052/1.907 secs]  

在上面GC日志中，1678.186启动了AbortablePreclean阶段，在随后不到2s就发生了一次YGC。

4、FinalMarking（并发重新标记，STW过程）
该阶段并发执行，在之前的并行阶段（GC线程和应用线程同时执行，好比你妈在打扫房间，你还在扔纸屑），可能产生新的引用关系如下：
- 老年代的新对象被GC Roots引用
- 老年代的未标记对象被新生代对象引用
- 老年代已标记的对象增加新引用指向老年代其它对象
- 新生代对象指向老年代引用被删除
- 也许还有其它情况..
上述对象中可能有一些已经在Precleaning阶段和AbortablePreclean阶段被处理过，但总存在没来得及处理的，所以还有进行如下的处理：
- 遍历新生代对象，重新标记
- 根据GC Roots，重新标记
- 遍历老年代的Dirty Card，重新标记，这里的Dirty Card大部分已经在clean阶段处理过

在第一步骤中，需要遍历新生代的全部对象，如果新生代的使用率很高，需要遍历处理的对象也很多，这对于这个阶段的总耗时来说，是个灾难
（因为可能大量的对象是暂时存活的，而且这些对象也可能引用大量的老年代对象，造成很多应该回收的老年代对象而没有被回收，遍历递归的次数也增加不少），
如果在AbortablePreclean阶段中能够恰好的发生一次YGC，这样就可以避免扫描无效的对象。

如果在AbortablePreclean阶段没来得及执行一次YGC，怎么办？
CMS算法中提供了一个参数：CMSScavengeBeforeRemark，默认并没有开启，如果开启该参数，在执行该阶段之前，会强制触发一次YGC，
可以减少新生代对象的遍历时间，回收的也更彻底一点。
不过，这种参数有利有弊，利是降低了Remark阶段的停顿时间，弊的是在新生代对象很少的情况下也多了一次YGC，
最可怜的是在AbortablePreclean阶段已经发生了一次YGC，然后在该阶段又傻傻的触发一次。
所以利弊需要把握。

## 3.2、主动Old GC
这个主动Old GC的过程，触发条件比较苛刻：
- YGC过程发生[Promotion Failed]，进而对老年代进行回收
- 比如执行了System.gc()，前提是没有参数ExplicitGCInvokesConcurrent
- 其它情况…
如果触发了主动Old GC，这时周期性Old GC正在执行，那么会夺过周期性Old GC的执行权（同一个时刻只能有一种在Old GC在运行），
并记录 concurrent mode failure 或者 concurrent mode interrupted。

主动GC开始时，需要判断本次GC是否要对老年代的空间进行Compact（因为长时间的周期性GC会造成大量的碎片空间），判断逻辑实现如下：

*should_compact =
    UseCMSCompactAtFullCollection &&
    ((_full_gcs_since_conc_gc >= CMSFullGCsBeforeCompaction) ||
     GCCause::is_user_requested_gc(gch->gc_cause()) ||
     gch->incremental_collection_will_fail(true /* consult_young */));
     
在三种情况下会进行压缩：
- 其中参数UseCMSCompactAtFullCollection(默认true)和 CMSFullGCsBeforeCompaction(默认0)，所以默认每次的主动GC都会对老年代的内存空间进行压缩，就是把对象移动到内存的最左边。
- 当然了，比如执行了System.gc()，前提是没有参数ExplicitGCInvokesConcurrent，也会进行压缩。
- 如果新生代的晋升担保会失败。
带压缩动作的算法，称为MSC，标记-清理-压缩，采用单线程，全暂停的方式进行垃圾收集，暂停时间很长很长…

那不带压缩动作的算法是什么样的呢？
不带压缩动作的执行逻辑叫Foreground Collect，整个过程相对周期性Old GC来说，少了Precleaning和AbortablePreclean两个阶段，其它过程都差不多。
如果执行System.gc()，而且添加了参数ExplicitGCInvokesConcurrent，这时并不属于主动GC，它会推进周期性Old GC的进行，比如刚刚执行过一次，并不会等2s后检查条件，
而是立马启动周期性Old GC。



