---
title: JVM概述
categories: 
- jvm
tags:
---

[参考](https://www.toutiao.com/i6637078787345351172/)
[参考](https://blog.csdn.net/l1394049664/article/details/81486470)


# 一：虚拟机内存图解
根据《Java虚拟机规范》的规定，运行时数据区通常包括这几个部分：
- 程序计数器(Program Counter Register)
- Java栈(VM Stack)
- 本地方法栈(Native Method Stack)
- 方法区(Method Area)
- 堆(Heap)

备注：方法区是虚拟机规范中对运行时数据区划分的一个内存区域，不同的虚拟机厂商可以有不同的实现，
而HotSpot虚拟机以永久代来实现方法区，所以方法区是一个规范，而永久代则是其中的一种实现方式。
<div align="center"> <img src="/images/jvm.jpg"/> </div><br>
<div align="center"> <img src="/images/jvm0.png"/> </div><br>

JAVA程序运行与虚拟机之上，运行时需要内存空间。虚拟机执行JAVA程序的过程中会把它管理的内存划分为不同的数据区域方便管理。
虚拟机管理内存数据区域划分如下图：
<div align="center"> <img src="/images/jvm1.jpg"/> </div><br>

数据区域分类：
方法区： (Method Area)
虚拟机栈 ： (VM Stack)
本地方法栈 ： (Native Method Stack)
堆： (Heap)
程序计数器： (Program Counter Register)
直接内存 ： (Direct Memory)

说明：
## 1. 程序计数器
行号指示器，字节码指令的分支、循环、跳转、异常处理、线程恢复(CPU切换)，每条线程都需要一个独立的计数器，线程私有内存互不影响,
该区域不会发生内存溢出异常。
程序计数器（Program Counter Register），也有称作为PC寄存器。在汇编语言中，程序计数器是指CPU中的寄存器，
它保存的是程序当前执行的指令的地址（也可以说保存下一条指令的所在存储单元的地址），当CPU需要执行指令时，
需要从程序计数器中得到当前需要执行的指令所在存储单元的地址，然后根据得到的地址获取到指令，在得到指令之后，
程序计数器便自动加1或者根据转移指针得到下一条指令的地址，如此循环，直至执行完所有的指令。
虽然JVM中的程序计数器并不像汇编语言中的程序计数器一样是物理概念上的CPU寄存器，
但是JVM中的程序计数器的功能跟汇编语言中的程序计数器的功能在逻辑上是等同的，也就是说是用来指示 执行哪条指令的。
由于在JVM中，多线程是通过线程轮流切换来获得CPU执行时间的，因此，在任一具体时刻，一个CPU的内核只会执行一条线程中的指令，
因此，为了能够使得每个线程都在线程切换后能够恢复在切换之前的程序执行位置，每个线程都需要有自己独立的程序计数器，并且不能互相被干扰，
否则就会影响到程序的正常执行次序。因此，可以这么说，程序计数器是每个线程所私有的。

在JVM规范中规定，如果线程执行的是非native方法，则程序计数器中保存的是当前需要执行的指令的地址；如果线程执行的是native方法，
则程序计数器中的值是undefined。由于程序计数器中存储的数据所占空间的大小不会随程序的执行而发生改变，因此，
对于程序计数器是不会发生内存溢出现象(OutOfMemory)的。


## 2. 虚拟机栈
是线程私有的，声明周期与线程相同，虚拟机栈是Java方法执行的内存模型，每个方法被执行时都会创建一个栈帧，即方法运行期间的基础数据结构，
栈帧用于存储：局部变量表、操作数栈、动态链接、方法出口等，每个方法执行中都对应虚拟机栈帧从入栈到处栈的过程。
是一种数据结构，是虚拟机中的局部变量表，对应物理层之上的程序数据模型。
局部变量表，是一种程序运行数据模型，存放了编译期可知的各种数据类型例如：
Boolean、byte、char、short、int、float、long、double、对象引用类型(对象内存地址变量，指针或句柄)，
程序运行时，根据局部变量表分配栈帧空间大小，在运行中，大小是不变的。
异常类型：
stackOverFlowError 线程请求栈深度大于虚拟机允许深度 
OutOfMemory 内存空间耗尽无法进行扩展。

Java栈也称作虚拟机栈（Java Vitual Machine Stack），也就是我们常常所说的栈。事实上，Java栈是Java方法执行的内存模型。
Java栈中存放的是一个个的栈帧，每个栈帧对应一个被调用的方法，在栈帧中包括局部变量表(Local Variables)、操作数栈(Operand Stack)、
指向当前方法所属的类的运行时常量池（运行时常量池的概念在方法区部分会谈到）的引用(Reference to runtime constant pool)、
方法返回地址(Return Address)和一些额外的附加信息。当线程执行一个方法时，就会随之创建一个对应的栈帧，并将建立的栈帧压栈。
当方法执行完毕之后，便会将栈帧出栈。因此可知，线程当前执行的方法所对应的栈帧必定位于Java栈的顶部。
讲到这里，大家就应该会明白为什么在使用递归方法的时候容易导致栈内存溢出的现象了以及为什么栈区的空间不用程序员去管理了
（当然在Java中，程序员基本不用关系到内存分配和释放的事情，因为Java有自己的垃圾回收机制），这部分空间的分配和释放都是由系统自动实施的。
对于所有的程序设计语言来说，栈这部分空间对程序员来说是不透明的。下图表示了一个Java栈的模型：
<div align="center"> <img src="/images/jvm1-1.jpg"/> </div><br>

### 1）、局部变量表
就是用来存储方法中的局部变量（包括在方法中声明的非静态变量以及函数形参）。对于基本数据类型的变量，则直接存储它的值，
对于引用类型的变量，则存的是指向对象的引用。局部变量表的大小在编译器就可以确定其大小了，因此在程序执行期间局部变量表的大小是不会改变的。

### 2）、操作数栈
想必学过数据结构中的栈的朋友想必对表达式求值问题不会陌生，栈最典型的一个应用就是用来对表达式求值。
想想一个线程执行方法的过程中，实际上就是不断执行语句的过程，而归根到底就是进行计算的过程。
因此可以这么说，程序中的所有计算过程都是在借助于操作数栈来完成的。

### 3）、指向运行时常量池的引用
因为在方法执行的过程中有可能需要用到类中的常量，所以必须要有一个引用指向运行时常量。

### 4）、方法返回地址
当一个方法执行完毕之后，要返回之前调用它的地方，因此在栈帧中必须保存一个方法返回地址。
由于每个线程正在执行的方法可能不同，因此每个线程都会有一个自己的Java栈，互不干扰。


## 3. 本地方法栈
与虚拟机栈类似，虚拟机栈为Java程序服务，本地方法栈支持虚拟机的运行服务，具体实现由虚拟机厂商决定，
也会抛出 stackOverFlowError、OutOfMemory异常。

本地方法栈与Java栈的作用和原理非常相似。区别只不过是Java栈是为执行Java方法服务的，
而本地方法栈则是为执行本地方法（Native Method）服务的。在JVM规范中，并没有对本地方法栈的具体实现方法以及数据结构作强制规定，
虚拟机可以自由实现它。在HotSopt虚拟机中直接就把本地方法栈和Java栈合二为一。

## 4. 堆
是虚拟机管理内存中最大的一部分，被所有线程共享，用于存放对象实例(对象、数组)，物理上不连续的内存空间，
由于GC收集器，分代收集，所以划分为：新生代 Eden、From SurVivor空间、To SurVivor空间，allot buffer(分配空间)，
可能会划分出多个线程私有的缓冲区，老年代。

1.7构成
<div align="center"> <img src="/images/jvm1-2.jpg"/> </div><br>


1.8的构成
<div align="center"> <img src="/images/jvm1-2.png"/> </div><br>

（1） 堆是JVM中所有线程共享的，因此在其上进行对象内存的分配均需要进行加锁，这也导致了new对象的开销是比较大的
（2） Sun Hotspot JVM为了提升对象内存分配的效率，对于所创建的线程都会分配一块独立的空间TLAB（Thread Local Allocation Buffer），其大小由JVM根据运行的情况计算而得，在TLAB上分配对象时不需要加锁，因此JVM在给线程的对象分配内存时会尽量的在TLAB上分配，在这种情况下JVM中分配对象内存的性能和C基本是一样高效的，但如果对象过大的话则仍然是直接使用堆空间分配
（3） TLAB仅作用于新生代的Eden Space，因此在编写Java程序时，通常多个小的对象比大的对象分配起来更加高效。
（4） 所有新创建的Object 都将会存储在新生代Yong Generation中。如果Young Generation的数据在一次或多次GC后存活下来，那么将被转移到OldGeneration。新的Object总是创建在Eden Space。

堆空间内存分配（默认情况下）
老年代 ： 三分之二的堆空间
年轻代 ： 三分之一的堆空间 
eden区： 8/10 的年轻代空间
survivor0 : 1/10 的年轻代空间
survivor1 : 1/10 的年轻代空间
命令行上执行如下命令，查看所有默认的jvm参数
java -XX:+PrintFlagsFinal -version

-XX:InitialSurvivorRatio    新生代Eden/Survivor空间的初始比例
-XX:Newratio    Old区 和 Yong区 的内存比例

一道推算题:默认参数下，如果仅给出eden区40M，求堆空间总大小
根据比例可以推算出，两个survivor区各5M，年轻代50M。老年代是年轻代的两倍，即100M。那么堆总大小就是150M。


## 5. 方法区
<div align="center"> <img src="/images/jvm1-3.png"/> </div><br>

与堆一样属于线程共享的内存区域，用于存储虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码（动态加载OSGI）等数据。
理论上属于java虚拟机的一部分，为了区分开来叫做 Non-Heap非堆。
这个区域可以选择不进行垃圾回收，该区域回收目的主要是常量池的回收，及类型的卸载class,内存区不足时会抛出OutOfMemory异常
运行时常量池：
方法区的一部分，Class的版本、字段、接口、方法等，及编译期生成的各种字面量、符号引用，编译类加载后存放在该区域。会抛出OutOfMemory异常。

根据 JVM8 规范，JVM 运行时内存共分为虚拟机栈、堆、[元空间]、程序计数器、本地方法栈五个部分。
还有一部分内存叫直接内存，属于操作系统的本地内存，也是可以直接操作的.

元空间(Metaspace）
元空间的本质和永久代类似，都是对JVM规范中方法区的实现。不过元空间与永久代之间最大的区别在于：[元空间并不在虚拟机中，而是使用本地内存]。


## 6. 直接内存
直接内存不属于虚拟内存区域，是一种基于通道与缓冲区的IO方式，可以使用本地函数直接分配堆外内存，在堆中存储引用的外部内存地址，
通过引用完成对直接引用内存的操作，1.4之后提供的NIO显著提高效率，避免了堆内存与Native内存的来回复制操作，不受虚拟机内存控制，
会抛出OUtOfMemory异常。

# 二：对象访问内部实现过程
对象访问 涉及到对象的地址变更状态变更，内存地址移动，变量、接口、实现类、方法、父类型等。
## 1、 句柄方式 (访问)
<div align="center"> <img src="/images/jvm2.png"/> </div><br>

## 2、指针方式 (访问)
<div align="center"> <img src="/images/jvm3.png"/> </div><br>

优缺点：
句柄访问方式：reference中存储的是稳定的地址，对象变更时只会改变句柄实例数据指针，引用本身不需要修改
指针访问方式：优点速度快，节省了指针定位时间开销
目前Java默认使用的HotSpot虚拟机采用的便是是第二种方式进行对象访问的。

# 三：内存区域控制参数及对应溢出异常
开发过程中，或程序运行过程中每次遇到OutOfMemory异常或GC异常或StackOverflowError异常我们都是一堆参数乱配，都把值调大，
只是大体知道是跟jvm内存分配有关，具体应该怎么调，对应的异常应该调整那些参数，或者换句话说，
jvm内存分配区域中都分别对应那些参数大多数情况下都是不知道的，只是把相关的参数跳上去，预期结果都是应该起作用，
到底能不能起作用，自己心里也没底。下面就来说一下jvm堆、栈、方法区等内存区域对应的参数，及每个区域可能抛出的异常类型，发生异常的场景分析。

## 3.1、参数类型
- 堆空间参数
- 栈空间参数
- 方法区空间参数
- 本机直接内存参数

## 3.2、异常类型
- OutOfMemory异常
- StackOverflowError异常

## 3.3、辅助参数说明
1.-XX:+HeapDumpOnOutOfMemoryError 打印堆内存异常时打印出快照信息
2.-XX:+HeapDumpPath 快照输出路径
3.-Xmn指定eden区的大小 -XX:SurvirorRation来调整幸存区的大小
4.-XX:PretenureSizeThreshold设置进入老年代的阀值

## 3.4、参数说明、对应场景的异常
### 1.堆内存参数
-Xms：堆最小值（新生代和老年代之和）
-Xmx：堆最大值（新生代和老年代之和）
当最小值=最大值时，这时堆内存是不可扩展的。
例：-Xms80M -Xmx80M
通常将-Xmx和-Xms设置为一样的大小来减少gc的次数，堆内存不足时抛出OutOfMemoryError异常。

### 2.栈内存参数
-Xss
例：-Xss128k
单线程下无论栈帧太大还是栈容量太小，及引用深度超过虚拟机允许深度都会抛出StackOverflowError每个方法压入栈的帧大小是不一致的。
多线程下当每个线程分配栈帧太大内存不能够扩展时抛出OutOfMemoryError异常线程栈帧越大，可创建的线程越少。

### 3.方法区参数
-XX:PermSize方法区内存最小值
-XX:MaxPermSize 方法区内存最大值
各个线程共享的内存区域，主要用来存储类的元数据、常量、静态变量、即时编译器编译后的代码等数据
例：-XX:PermSize=20M -XX:MaxPermSize=20M
异常类型 OutOfMemoryError :
原因：常量过多，或代理反射等使用频繁

### 4.本机直接内存参数
-XX:MaxDirectMemorySize
例：-XX:MaxDirectMemorySize=10M
不足时抛出OutOfMemory异常

# 四：垃圾收集算法
经典的垃圾回收算法以下几种

## 4.1、标记--清除算法(Mark-Sweep)

回收前状态：
<div align="center"> <img src="/images/jvm4jpg"/> </div><br>

回收后;
<div align="center"> <img src="/images/jvm5.jpg"/> </div><br>


优缺点：
算法执行分为两个阶段标记与清除，所有的回收算法，基本都基于标记回收算法做了深度优化
缺点：效率问题，内存空间碎片（不连续的空间）

## 4.2、复制算法(Copying)

回收前状态：
Eden内存空间 [8]
<div align="center"> <img src="/images/jvm6.jpg"/> </div><br>

Survivor1空间（From空间）1
<div align="center"> <img src="/images/jvm7.jpg"/> </div><br>

Survivor2空间(To空间) 1
<div align="center"> <img src="/images/jvm8.jpg"/> </div><br>

Eden内存空间与Survivor空间 8:1
<div align="center"> <img src="/images/jvm9.jpg"/> </div><br>

回收后状态：
Eden内存空间 [8]
<div align="center"> <img src="/images/jvm10.jpg"/> </div><br>

Survivor1空间（From空间）1
<div align="center"> <img src="/images/jvm11.jpg"/> </div><br>

Eden内存空间与Survivor空间 8:1
<div align="center"> <img src="/images/jvm12.jpg"/> </div><br>


优缺点：
比较标记清除算法，避免了回收造成的内存碎片问题，
缺点：以局部的内存空间牺牲为代价，不过空间的浪费比较小，默认8:1的比例1是浪费的。复制也有一定的效率与空间成本

## 4.3、标记整理算法(Mark-Compact)

回收前状态：
<div align="center"> <img src="/images/jvm13.jpg"/> </div><br>

回收后状态：
<div align="center"> <img src="/images/jvm14.jpg"/> </div><br>

优缺点：
避免了，空间的浪费，与内存碎片问题。
缺点：整理时复制有效率成本。

## 4.4、分代收集不是一种算法，而是根据对象的生命周期来选择合适的垃圾回收算法
把Java堆分为新生代和老年代，这样就可以根据各个年代的特点采用最适当的收集算法。在新生代中，每次垃圾收集时都发现有大批对象死去，
只有少量存活，那就选用复制算法，只需要付出少量存活对象的复制成本就可以完成收集。而老年代中因为对象存活率高、没有额外空间对它进行分配担保，
就必须使用“标记-清理”或“标记-整理”算法来进行回收。

# 五：垃圾收集器
## 5.1、七种垃圾收集器
(1) Serial（串行GC）-XX:+UseSerialGC
(2) ParNew（并行GC）-XX:+UseParNewGC
(3) Parallel Scavenge（并行回收GC）
(4) Serial Old（MSC）（串行GC）-XX:+UseSerialGC
(5) CMS（并发GC）-XX:+UseConcMarkSweepGC
(6) Parallel Old（并行GC）-XX:+UseParallelOldGC
(7) G1（JDK1.7update14才可以正式商用）
<div align="center"> <img src="/images/jvm15.png"/> </div><br>

1~3用于年轻代垃圾回收：年轻代的垃圾回收称为minor GC
4~6用于年老代垃圾回收（当然也可以用于方法区的回收）：年老代的垃圾回收称为full GC
G1独立完成"分代垃圾回收"

注意：[并行与并发]
并行：多条垃圾回收线程同时操作
并发：垃圾回收线程与用户线程一起操作

## 5.2、常用五种组合
- Serial/Serial Old
- ParNew/Serial Old：与上边相比，只是比年轻代多了多线程垃圾回收而已
- ParNew/CMS：当下比较高效的组合
- Parallel Scavenge/Parallel Old：自动管理的组合
- G1：最先进的收集器，但是需要JDK1.7update14以上

### (1)、Serial/Serial Old
年轻代Serial收集器采用单个GC线程实现"[复制]"算法（包括扫描、复制）
年老代Serial Old收集器采用单个GC线程实现"[标记-整理]"算法
Serial与Serial Old都会暂停所有用户线程（即STW）

说明：
STW（stop the world）：编译代码时为每一个方法注入safepoint（方法中循环结束的点、方法执行结束的点），
在暂停应用时，需要等待所有的用户线程进入safepoint，之后暂停所有线程，然后进行垃圾回收。

适用场合：
CPU核数<2，物理内存<2G的机器（简单来讲，单CPU，新生代空间较小且对STW时间要求不高的情况下使用）
[-XX:UseSerialGC：强制使用该GC组合]
-XX:PrintGCApplicationStoppedTime：查看STW时间

### （2）、ParNew/Serial Old：

ParNew除了采用多GC线程来实现[复制算法]以外，其他都与Serial一样，但是此组合中的Serial Old又是一个单GC线程，
所以该组合是一个比较尴尬的组合，在单CPU情况下没有Serial/Serial Old速度快（因为ParNew多线程需要切换），
在多CPU情况下又没有之后的三种组合快（因为Serial Old是单GC线程），所以使用其实不多。
-XX:ParallelGCThreads：指定ParNew GC线程的数量，默认与CPU核数相同，该参数在于CMS GC组合时，也可能会用到

### （3）、Parallel Scavenge/Parallel Old：

特点：
年轻代Parallel Scavenge收集器采用多个GC线程实现"[复制]"算法（包括扫描、复制）; Old收集器采用多个GC线程实现"[标记-整理]"算法；
ParallelScavenge与Parallel Old都会暂停所有用户线程（即STW）

说明：
吞吐量：CPU运行代码时间/(CPU运行代码时间+GC时间)CMS
主要注重STW的缩短（该时间越短，用户体验越好，所以主要用于处理很多的交互任务的情况）
Parallel Scavenge/Parallel Old主要注重吞吐量（吞吐量越大，说明CPU利用率越高，所以主要用于处理很多的CPU计算任务而用户交互任务较少的情况）

参数设置：
[-XX:+UseParallelOldGC：使用该GC组合]
-XX:GCTimeRatio：直接设置吞吐量大小，假设设为19，则允许的最大GC时间占总时间的1/(1+19)，默认值为99，即1/(1+99)
-XX:MaxGCPauseMillis：最大GC停顿时间，该参数并非越小越好
-XX:+UseAdaptiveSizePolicy：开启该参数，-Xmn/-XX:SurvivorRatio/-XX:PretenureSizeThreshold这些参数就不起作用了，
虚拟机会自动收集监控信息，动态调整这些参数以提供最合适的的停顿时间或者最大的吞吐量（GC自适应调节策略），
而我们需要设置的就是-Xmx，-XX:+UseParallelOldGC或-XX:GCTimeRatio两个参数就好（当然-Xms也指定上与-Xmx相同就好）

注意：
-XX:GCTimeRatio和-XX:MaxGCPauseMillis设置一个就好
不开启-XX:+UseAdaptiveSizePolicy，-Xmn/-XX:SurvivorRatio/-XX:PretenureSizeThreshold这些参数依旧可以配置，以resin服务器为例
<jvm-arg>-Xms2048m</jvm-arg> 
<jvm-arg>-Xmx2048m</jvm-arg> 
<jvm-arg>-Xmn512m</jvm-arg> 
<jvm-arg>-Xss1m</jvm-arg> 
<jvm-arg>-XX:PermSize=256M</jvm-arg> 
<jvm-arg>-XX:MaxPermSize=256M</jvm-arg> 
<jvm-arg>-XX:SurvivorRatio=8</jvm-arg> 
<jvm-arg>-XX:MaxTenuringThreshold=15</jvm-arg> 
<jvm-arg>-XX:+UseParallelOldGC</jvm-arg> 
<jvm-arg>-XX:GCTimeRatio=19</jvm-arg> 
<jvm-arg>-XX:+PrintGCDetails</jvm-arg> 
<jvm-arg>-XX:+PrintGCTimeStamps</jvm-arg> 

适用场合：
很多的CPU计算任务而用户交互任务较少的情况不想自己去过多的关注GC参数，想让虚拟机自己进行调优工作

# 6、调优方法

## 6.1、新对象预留新生代
由于fullGC(老年代)的成本远比minorGC（新生代和老年代）的成本大，所以给应用分配一个合理的新生代空间，尽量将对象分配到新生代减小fullGC的频率

## 6.2、大对象进入老年代
将大对象直接分配到老年代，保持新生代对象的结构的完整性，以提高GC效率， 以通过[-XX:PretenureSizeThreshold]设置进入老年代的阀值

## 6.3、稳定与震荡的堆大小
稳定的堆大小是对垃圾回收有利的，方法将-Xms和-Xmx的大小一致

## 4.4、 吞吐量优先
尽可能减少系统执行垃圾回收的总时间，故采用并行垃圾回收器
-XX:+UseParallelGC或使用-XX:+UseParallelOldGC

## 4.5、 降低停顿
使用CMS回收器,同时减少fullGC的次数


# 7、获取gc信息的方法
-verbose:gc或者-XX:+PrintGC 获取gc信息
-XX:+PrintGCDetails 获取更加详细的gc信息
-XX:+PrintGCTimeStamps 获取GC的频率和间隔
-XX:+PrintHeapAtGC 获取堆的使用情况
-Xloggc:D:gc.log 指定日志情况的保存路径

# 8、jvm调优实战-tomcat启动加速
在tomcat的bin/catalina.bat文件的开头添加相关的配置

# 9：监控工具
监控工具：一般问题定位，性能调优都会使用到。
(一)、jps
Jps是参照Unix系统的取名规则命名的，而他的功能和ps的功能类似，
可以列举正在运行的虚拟机进程并显示虚拟机执行的主类以及这些进程的唯一ID（ＬＶＭＩＤ，对应本机来说和PID相同），他的用法如下：
Jps [option] [hostid]

jps -q 只输出LVMID
jps -m 输出JVM启动时传给主类的方法
jps -l 输出主类的全名，如果是Jar则输出jar的路径
jps -v 输出JVM的启动参数

备注：推荐[jps -mlvV]

(二)、jstat
jstat主要用于监控虚拟机的各种运行状态信息，如类的装载、内存、垃圾回收、JIT编译器等，
在没有GUI的服务器上，这款工具是首选的一款监控工具。其用法如下：
jstat [option vmid [interval [s|ms] [vount] ] ]

jstat 监控内容 线程号 刷新时间间隔 次数

jstat –gc 20445 1 20 :监视Java堆，包含eden、2个survivor区、old区和永久带区域的容量、已用空间、GC时间合计等信息
jstat –gcutil 20445 1 20:监视内容与-gc相同，但输出主要关注已使用空间占总空间的百分比
jstat –class 20445 1 20:监视类的装载、卸载数量以及类的装载总空间和耗费时间等
.......-gccapcity......:监视内容与-gc相同，但输出主要关注Java区域用到的最大和最小空间
.......-gccause........:与-gcutil输出信息相同，额外输出导致上次GC产生的原因
.......-gcnew..........:监控新生代的GC情况
.......-gcnewcapacity..:与-gcnew监控信息相同，输出主要关注使用到的最大和最小空间
.......-gcold..........:监控老生代的GC情况
.......-gcoldcapacity..:与-gcold监控信息相同，输出主要关注使用到的最大和最小空间
.......-gcpermcapacity.:输出永久带用到的最大和最小空间
.......-compiler.......:输出JIT编译器编译过的方法、耗时信息
.......-printcompilation:输出已经被JIT编译的方法

(三)、jinfo
jinfo的作用是实时查看虚拟机的各项参数信息jps –v可以查看虚拟机在启动时被显式指定的参数信息，
但是如果你想知道默认的一些参数信息呢？除了去查询对应的资料以外，jinfo就显得很重要了。jinfo的用法如下：
Jinfo [option] pid

(四)、jmap
map用于生成堆快照（heapdump）。当然我们有很多方法可以取到对应的dump信息，如我们通过JVM启动时加入启动参数 
–XX:HeapDumpOnOutOfMemoryError参数，可以让JVM在出现内存溢出错误的时候自动生成dump文件，
亦可以通过-XX:HeapDumpOnCtrlBreak参数，在运行时使用ctrl+break按键生成dump文件，当然我们也可以使用kill -3 pid的方式去控制JVM生成dump文件。
Jmap的作用不仅仅是为了获取dump文件，还可以用于查询finalize执行队列、Java堆和永久带的详细信息，如空间使用率、垃圾回收器等。其运行格式如下：

Jmap [option] vmip
监控堆栈信息主要用来定位问题的原因，生成堆栈快照
.......-dump......:生成对应的dump信息，用法为-dump:[live,]format=b,file={fileName}
.......-finalizerinfo......:显示在F-Queue中等待的Finalizer方法的对象（只在linux下生效）
.......-heap......：显示堆的详细信息、垃圾回收器信息、参数配置、分代详情等
.......-histo......：显示堆栈中的对象的统计信息，包含类、实例数量和合计容量
.......-permstat......：以ClassLoder为统计口径显示永久带的内存状态
.......-F......：虚拟机对-dump无响应时可使用这个选项强制生成dump快照
例子：jmap -dump:format=b,file=yhj.dump 20445

(五)、jstack
Jstack用于JVM当前时刻的线程快照，又称threaddump文件，它是JVM当前每一条线程正在执行的堆栈信息的集合。
生成线程快照的主要目的是为了定位线程出现长时间停顿的原因，如线程死锁、死循环、请求外部时长过长导致线程停顿的原因。
通过jstack我们就可以知道哪些进程在后台做些什么？在等待什么资源等！其运行格式如下：

Jstack [option] vmid

-F 当正常输出的请求不响应时强制输出线程堆栈
-l 除堆栈信息外，显示关于锁的附加信息
-m 显示native方法的堆栈信息

(六)、jconsole
在JDK的bin目录下,监控内存,thread,堆栈等

(七)、jprofile
类似于jconsole,比jconsole监控信息更全面，内存，线程，包,cup 类，堆栈，等等


