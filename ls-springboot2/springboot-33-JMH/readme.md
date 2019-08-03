#1、JMH
即[Java Microbenchmark Harness]，这是专门用于进行代码的微基准测试的一套工具API。

JMH 由 OpenJDK/Oracle 里面那群开发了 Java 编译器的大牛们所开发 。何谓 Micro Benchmark 呢？ 
简单地说就是在 method 层面上的 benchmark，精度可以精确到[微秒级]。

Java的基准测试需要注意的几个点：
- 测试前需要预热。
- 防止无用代码进入测试方法中。
- 并发测试。
- 测试结果呈现。

比较典型的使用场景：
当你已经找出了热点函数，而需要对热点函数进行进一步的优化时，就可以使用 JMH 对优化的效果进行定量的分析。
想定量地知道某个函数需要执行多长时间，以及执行时间和输入 n 的相关性
一个函数有两种不同实现（例如JSON序列化/反序列化有Jackson和Gson实现），不知道哪种实现性能更好
尽管 JMH 是一个相当不错的 Micro Benchmark Framework，但很无奈的是网上能够找到的文档比较少，
而官方也没有提供比较详细的文档，对使用造成了一定的障碍。 但是有个好消息是官方的 Code Sample 写得非常浅显易懂，
 推荐在需要详细了解 JMH 的用法时可以通读一遍——本文则会介绍 JMH 最典型的用法和部分常用选项。

第一个例子
添加maven依赖
如果使用maven项目，只需要添加如下依赖：

    <!-- https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core -->
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-core</artifactId>
            <version>1.19</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-generator-annprocess -->
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-generator-annprocess</artifactId>
            <version>1.19</version>
            <scope>provided</scope>
        </dependency>
编写性能测试
接下来我写一个比较字符串连接操作的时候，直接使用字符串相加和使用StringBuilder的append方式的性能比较测试：

 ```java
/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-23 
 * 比较字符串直接相加和StringBuilder的效率
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(8)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringBuilderBenchmark {


public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
           .include(StringBuilderBenchmark.class.getSimpleName())
           .output("d:/Benchmark.log")
           .build();
   new Runner(options).run();
}

@Benchmark
public void testStringAdd() {
String a = "";
for (int i = 0; i < 10; i++) {
          a += i;
      }
      print(a);
   }

   @Benchmark
   public void testStringBuilderAdd() {
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < 10; i++) {
           sb.append(i);
       }
       print(sb.toString());
   }

  private void print(String a) {
   }
}

```
这里其实也比较简单，new个Options，然后传入要运行哪个测试，选择基准测试报告输出文件地址，然后通过Runner的run方法就可以跑起来了。

报告结果
我们跑一下这个基准测试，完成后打开E:/Benchmark.log，结果如下：

  # JMH version: 1.19
  # VM version: JDK 1.8.0_181, VM 25.181-b13
  # VM invoker: D:\soft-install\jdk8\jre\bin\java.exe
  # VM options: -javaagent:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\lib\idea_rt.jar=53357:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\bin -Dfile.encoding=UTF-8
  # Warmup: 3 iterations, 1 s each
  # Measurement: 10 iterations, 5 s each
  # Timeout: 10 min per iteration
  # Threads: 8 threads, will synchronize iterations
  # Benchmark mode: Throughput, ops/time
  # Benchmark: ls.com.jmh.StringBuilderBenchmark.testStringAdd
  
  # Run progress: 0.00% complete, ETA 00:03:32
  # Fork: 1 of 2
  # Warmup Iteration   1: 7940.843 ops/ms
  # Warmup Iteration   2: 11229.630 ops/ms
  # Warmup Iteration   3: 11122.507 ops/ms
  Iteration   1: 10919.994 ops/ms
  Iteration   2: 11118.757 ops/ms
  Iteration   3: 10790.044 ops/ms
  Iteration   4: 10983.230 ops/ms
  Iteration   5: 10572.673 ops/ms
  Iteration   6: 10544.898 ops/ms
  Iteration   7: 11092.313 ops/ms
  Iteration   8: 10878.773 ops/ms
  Iteration   9: 10182.366 ops/ms
  Iteration  10: 10804.186 ops/ms
  
  # Run progress: 25.00% complete, ETA 00:02:56
  # Fork: 2 of 2
  # Warmup Iteration   1: 9965.846 ops/ms
  # Warmup Iteration   2: 10673.393 ops/ms
  # Warmup Iteration   3: 9715.511 ops/ms
  Iteration   1: 10624.889 ops/ms
  Iteration   2: 10194.215 ops/ms
  Iteration   3: 10398.239 ops/ms
  Iteration   4: 10597.658 ops/ms
  Iteration   5: 9043.354 ops/ms
  Iteration   6: 9903.444 ops/ms
  Iteration   7: 10668.711 ops/ms
  Iteration   8: 10285.016 ops/ms
  Iteration   9: 11236.953 ops/ms
  Iteration  10: 10981.301 ops/ms
  
  
  Result "ls.com.jmh.StringBuilderBenchmark.testStringAdd":
    10591.051 ±(99.9%) 439.277 ops/ms [Average]
    (min, avg, max) = (9043.354, 10591.051, 11236.953), stdev = 505.872
    CI (99.9%): [10151.773, 11030.328] (assumes normal distribution)
  
  
  # JMH version: 1.19
  # VM version: JDK 1.8.0_181, VM 25.181-b13
  # VM invoker: D:\soft-install\jdk8\jre\bin\java.exe
  # VM options: -javaagent:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\lib\idea_rt.jar=53357:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\bin -Dfile.encoding=UTF-8
  # Warmup: 3 iterations, 1 s each
  # Measurement: 10 iterations, 5 s each
  # Timeout: 10 min per iteration
  # Threads: 8 threads, will synchronize iterations
  # Benchmark mode: Throughput, ops/time
  # Benchmark: ls.com.jmh.StringBuilderBenchmark.testStringBuilderAdd
  
  # Run progress: 50.00% complete, ETA 00:01:55
  # Fork: 1 of 2
  # Warmup Iteration   1: 43899.894 ops/ms
  # Warmup Iteration   2: 50684.364 ops/ms
  # Warmup Iteration   3: 24093.176 ops/ms
  Iteration   1: 24529.140 ops/ms
  Iteration   2: 24421.703 ops/ms
  Iteration   3: 24352.133 ops/ms
  Iteration   4: 24969.391 ops/ms
  Iteration   5: 24842.822 ops/ms
  Iteration   6: 24417.623 ops/ms
  Iteration   7: 24176.709 ops/ms
  Iteration   8: 23984.493 ops/ms
  Iteration   9: 24144.457 ops/ms
  Iteration  10: 24148.706 ops/ms
  
  # Run progress: 75.00% complete, ETA 00:00:57
  # Fork: 2 of 2
  # Warmup Iteration   1: 42659.776 ops/ms
  # Warmup Iteration   2: 31502.940 ops/ms
  # Warmup Iteration   3: 24269.930 ops/ms
  Iteration   1: 24806.190 ops/ms
  Iteration   2: 24853.469 ops/ms
  Iteration   3: 24818.508 ops/ms
  Iteration   4: 24862.524 ops/ms
  Iteration   5: 23625.728 ops/ms
  Iteration   6: 23874.826 ops/ms
  Iteration   7: 24518.567 ops/ms
  Iteration   8: 23993.098 ops/ms
  Iteration   9: 23585.864 ops/ms
  Iteration  10: 23335.521 ops/ms
  
  
  Result "ls.com.jmh.StringBuilderBenchmark.testStringBuilderAdd":
    24313.074 ±(99.9%) 415.516 ops/ms [Average]
    (min, avg, max) = (23335.521, 24313.074, 24969.391), stdev = 478.509
    CI (99.9%): [23897.558, 24728.590] (assumes normal distribution)
  
  
  # Run complete. Total time: 00:03:50
  
  Benchmark                                     Mode  Cnt      Score     Error   Units
  StringBuilderBenchmark.testStringAdd         thrpt   20  10591.051 ± 439.277  ops/ms
  StringBuilderBenchmark.testStringBuilderAdd  thrpt   20  24313.074 ± 415.516  ops/ms

  
仔细看，三大部分，第一部分是字符串用加号连接执行的结果，
第二部分是StringBuilder执行的结果，
第三部分就是两个的简单结果比较。这里注意我们forks传的2，所以每个测试有两个fork结果。

前两部分是一样的，简单说下。首先会写出每部分的一些参数设置，然后是预热迭代执行（Warmup Iteration）， 
然后是正常的迭代执行（Iteration），最后是结果（Result）。这些看看就好，我们最关注的就是第三部分， 
其实也就是最终的结论。千万别看歪了，他输出的也确实很不爽，error那列其实没有内容，
score的结果是xxx ± xxx，单位是每毫秒多少个操作。可以看到，StringBuilder的速度还确实是要比String进行文字叠加的效率好太多。

# 2、注解介绍
好了，当你对JMH有了一个基本认识后，现在来详细解释一下前面代码中的各个注解含义。

## 2.1、@BenchmarkMode（Benchmark基准的意思）
基准测试类型。这里选择的是Throughput也就是吞吐量。根据源码点进去，每种类型后面都有对应的解释，比较好理解，
吞吐量会得到单位时间内可以进行的操作数。

Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”。
AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，
用于测试冷启动时的性能。
All(“all”, “All benchmark modes”);

## 2.2、@Warmup
上面我们提到了，进行基准测试前需要进行预热。一般我们前几次进行程序测试的时候都会比较慢， 所以要让程序进行几轮预热，保证测试的准确性。
其中的参数iterations也就非常好理解了，就是预热轮数。
为什么需要预热？因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译成为机器码从而提高执行速度。
所以为了让 benchmark 的结果更加接近真实情况就需要进行预热。

## 2.3、@Measurement
度量，其实就是一些基本的测试参数。
iterations 进行测试的轮次
time 每轮进行的时长
timeUnit 时长单位
都是一些基本的参数，可以根据具体情况调整。一般比较重的东西可以进行大量的测试，放到服务器上运行。

## 2.4、@Threads
每个进程中的测试线程，这个非常好理解，根据具体情况选择，一般为cpu乘以2。

## 2.5、@Fork
进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。

## 2.6、@OutputTimeUnit
这个比较简单了，基准测试结果的时间类型。一般选择秒、毫秒、微秒。

## @Benchmark
方法级注解，表示该方法是需要进行 benchmark 的对象，用法和 JUnit 的 @Test 类似。

## @Param
属性级注解，@Param 可以用来指定某项参数的多种情况。特别适合用来测试一个函数在不同的参数输入的情况下的性能。

## @Setup
方法级注解，这个注解的作用就是我们需要在测试之前进行一些准备工作，比如对一些数据的初始化之类的。

## @TearDown
方法级注解，这个注解的作用就是我们需要在测试之后进行一些结束工作，比如关闭线程池，数据库连接等的，主要用于资源的回收等。

## @State
当使用@Setup参数的时候，必须在类上加这个参数，不然会提示无法运行。

State 用于声明某个类是一个“状态”，然后接受一个 Scope 参数用来表示该状态的共享范围。 
因为很多 benchmark 会需要一些表示状态的类，JMH 允许你把这些类以依赖注入的方式注入到 benchmark 函数里。Scope 主要分为三种。
Thread: 该状态为每个线程独享。
Group: 该状态为同一个组里面所有线程共享。
Benchmark: 该状态在所有线程间共享。

关于State的用法，官方的 code sample 里有比较好的例子。

第二个例子
再来看一个更常规一点性能测试的例子，
计算 1 ~ n 之和，比较串行算法和并行算法的效率，看 n 在大约多少时并行算法开始超越串行算法

首先定义一个表示这两种实现的接口：

 1/**
 2 * Calculator
 3 *
 4 * @author XiongNeng
 5 * @version 1.0
 6 * @since 2018/1/7
 7 */
 8public interface Calculator {
 9    /**
10     * calculate sum of an integer array
11     *
12     * @param numbers
13     * @return
14     */
15    public long sum(int[] numbers);
16
17    /**
18     * shutdown pool or reclaim any related resources
19     */
20    public void shutdown();
21}
具体的两种实现代码我就不贴了，主要说明一下串行算法和并行算法实现原理：

串行算法：使用 for-loop 来计算 n 个正整数之和。

并行算法：将所需要计算的 n 个正整数分成 m 份，交给 m 个线程分别计算出和以后，再把它们的结果相加。

进行 benchmark 的代码如下：

 1/**
 2 * 自然数求和的串行和并行算法性能测试
 3 *
 4 * @author XiongNeng
 5 * @version 1.0
 6 * @since 2018/1/7
 7 */
 8@BenchmarkMode(Mode.AverageTime)
 9@OutputTimeUnit(TimeUnit.MICROSECONDS)
10@State(Scope.Benchmark)
11public class SecondBenchmark {
12    @Param({"10000", "100000", "1000000"})
13    private int length;
14
15    private int[] numbers;
16    private Calculator singleThreadCalc;
17    private Calculator multiThreadCalc;
18
19    public static void main(String[] args) throws Exception {
20        Options opt = new OptionsBuilder()
21                .include(SecondBenchmark.class.getSimpleName())
22                .forks(1)
23                .warmupIterations(5)
24                .measurementIterations(2)
25                .build();
26        Collection<RunResult> results =  new Runner(opt).run();
27        ResultExporter.exportResult("单线程与多线程求和性能", results, "length", "微秒");
28    }
29
30    @Benchmark
31    public long singleThreadBench() {
32        return singleThreadCalc.sum(numbers);
33    }
34
35    @Benchmark
36    public long multiThreadBench() {
37        return multiThreadCalc.sum(numbers);
38    }
39
40    @Setup
41    public void prepare() {
42        numbers = IntStream.rangeClosed(1, length).toArray();
43        singleThreadCalc = new SinglethreadCalculator();
44        multiThreadCalc = new MultithreadCalculator(Runtime.getRuntime().availableProcessors());
45    }
46
47    @TearDown
48    public void shutdown() {
49        singleThreadCalc.shutdown();
50        multiThreadCalc.shutdown();
51    }
52}
我在自己的笔记本电脑上跑下来的结果，总数在10000时并行算法不如串行算法， 总数达到100000时并行算法开始和串行算法接近，
总数达到1000000时并行算法所耗时间约是串行算法的一半左右。