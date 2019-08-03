package com.ls.lishuai.jvm;

import java.util.Vector;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 16:27
 *

https://zhuanlan.zhihu.com/p/36597907

（1）当参数设置为如下时：（设置新生代为1M，很小）
-Xmx20m -Xms20m -Xmn1m -XX:+PrintGCDetails

Heap
PSYoungGen      total 512K, used 0K [0x00000000fff00000, 0x0000000100000000, 0x0000000100000000)
eden space 0K, -2147483648% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff00000)
from space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
ParOldGen       total 19456K, used 11848K [0x00000000fec00000, 0x00000000fff00000, 0x00000000fff00000)
object space 19456K, 60% used [0x00000000fec00000,0x00000000ff792058,0x00000000fff00000)
PSPermGen       total 21504K, used 3200K [0x00000000f9a00000, 0x00000000faf00000, 0x00000000fec00000)
object space 21504K, 14% used [0x00000000f9a00000,0x00000000f9d201c8,0x00000000faf00000)

总结：没有触发GC 由于新生代的内存比较小，所以全部分配在老年代。


（2）当参数设置为如下时：（设置新生代为15M，足够大）

-Xmx20m -Xms20m -Xmn15m -XX:+PrintGCDetails

运行效果：
Heap
PSYoungGen      total 13824K, used 12273K [0x00000000ff100000, 0x0000000100000000, 0x0000000100000000)
eden space 12288K, 99% used [0x00000000ff100000,0x00000000ffcfc498,0x00000000ffd00000)
from space 1536K, 0% used [0x00000000ffe80000,0x00000000ffe80000,0x0000000100000000)
to   space 1536K, 0% used [0x00000000ffd00000,0x00000000ffd00000,0x00000000ffe80000)
ParOldGen       total 5120K, used 0K [0x00000000fea00000, 0x00000000fef00000, 0x00000000ff100000)
object space 5120K, 0% used [0x00000000fea00000,0x00000000fea00000,0x00000000fef00000)
PSPermGen       total 21504K, used 3164K [0x00000000f9800000, 0x00000000fad00000, 0x00000000fea00000)
object space 21504K, 14% used [0x00000000f9800000,0x00000000f9b17038,0x00000000fad00000)

总结：没有触发GC、全部分配在eden(99% used)、老年代没有使用（0% used）

（3）当参数设置为如下时：（设置新生代为7M，不大不小）SurvivorRatio默认值是8 一个s区占十分之一

-Xmx20m -Xms20m –Xmn7m -XX:+PrintGCDetails
运行效果：
[GC [PSYoungGen: 5558K->504K(6656K)] 5558K->1640K(19968K), 0.0037924 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC [PSYoungGen: 5822K->504K(6656K)] 6958K->2688K(19968K), 0.0132513 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
Heap
PSYoungGen      total 6656K, used 1775K [0x00000000ff900000, 0x0000000100000000, 0x0000000100000000)
eden space 6144K, 20% used [0x00000000ff900000,0x00000000ffa3ddb0,0x00000000fff00000)
from space 512K, 98% used [0x00000000fff80000,0x00000000ffffe010,0x0000000100000000)
to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
ParOldGen       total 13312K, used 2184K [0x00000000fec00000, 0x00000000ff900000, 0x00000000ff900000)
object space 13312K, 16% used [0x00000000fec00000,0x00000000fee22020,0x00000000ff900000)
PSPermGen       total 21504K, used 2948K [0x00000000f9a00000, 0x00000000faf00000, 0x00000000fec00000)
object space 21504K, 13% used [0x00000000f9a00000,0x00000000f9ce1018,0x00000000faf00000)


总结：进行了2次新生代GC　s0 s1 太小，需要老年代担保

（4）当参数设置为如下时：（设置新生代为7M，不大不小；同时，增加幸存代大小）
-Xmx20m -Xms20m -Xmn7m -XX:SurvivorRatio=2 -XX:+PrintGCDetails
运行效果：

[GC [PSYoungGen: 3642K->1528K(5632K)] 3642K->1640K(18944K), 0.0053814 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC [PSYoungGen: 4654K->1512K(5632K)] 4766K->1672K(18944K), 0.0045486 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC [PSYoungGen: 4744K->1528K(5632K)] 4904K->1704K(18944K), 0.0011149 secs] [Times: user=0.05 sys=0.00, real=0.00 secs]
Heap
PSYoungGen      total 5632K, used 3751K [0x00000000ff900000, 0x0000000100000000, 0x0000000100000000)
eden space 4096K, 54% used [0x00000000ff900000,0x00000000ffb2bcc8,0x00000000ffd00000)
from space 1536K, 99% used [0x00000000ffd00000,0x00000000ffe7e020,0x00000000ffe80000)
to   space 1536K, 0% used [0x00000000ffe80000,0x00000000ffe80000,0x0000000100000000)
ParOldGen       total 13312K, used 176K [0x00000000fec00000, 0x00000000ff900000, 0x00000000ff900000)
object space 13312K, 1% used [0x00000000fec00000,0x00000000fec2c000,0x00000000ff900000)
PSPermGen       total 21504K, used 3071K [0x00000000f9a00000, 0x00000000faf00000, 0x00000000fec00000)
object space 21504K, 14% used [0x00000000f9a00000,0x00000000f9cfff80,0x00000000faf00000)

总结：进行了至少3次新生代GC s0 s1 增大

5）当参数设置为如下时：

-Xmx20m -Xms20m -XX:NewRatio=1 -XX:SurvivorRatio=2 -XX:+PrintGCDetails

[GC [PSYoungGen: 4548K->1688K(7680K)] 4548K->1688K(17920K), 0.0015256 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC [PSYoungGen: 5952K->1688K(7680K)] 5952K->1688K(17920K), 0.0013248 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
PSYoungGen      total 7680K, used 5009K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
eden space 5120K, 64% used [0x00000000ff600000,0x00000000ff93e788,0x00000000ffb00000)
from space 2560K, 65% used [0x00000000ffd80000,0x00000000fff26020,0x0000000100000000)
to   space 2560K, 0% used [0x00000000ffb00000,0x00000000ffb00000,0x00000000ffd80000)
ParOldGen       total 10240K, used 0K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
object space 10240K, 0% used [0x00000000fec00000,0x00000000fec00000,0x00000000ff600000)
PSPermGen       total 21504K, used 2906K [0x00000000f9a00000, 0x00000000faf00000, 0x00000000fec00000)
object space 21504K, 13% used [0x00000000f9a00000,0x00000000f9cd6bf0,0x00000000faf00000)

（6）当参数设置为如下时： 和上面的（5）相比，适当减小幸存代大小，这样的话，能够减少GC的次数  （通过增大E区的大小）

-Xmx20m -Xms20m -XX:NewRatio=1 -XX:SurvivorRatio=3 -XX:+PrintGCDetails


[GC [PSYoungGen: 5579K->1720K(8192K)] 5579K->1720K(18432K), 0.0083712 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
[GC [PSYoungGen: 6977K->1656K(8192K)] 6977K->1656K(18432K), 0.0042386 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
PSYoungGen      total 8192K, used 2905K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
eden space 6144K, 20% used [0x00000000ff600000,0x00000000ff738788,0x00000000ffc00000)
from space 2048K, 80% used [0x00000000ffe00000,0x00000000fff9e040,0x0000000100000000)
to   space 2048K, 0% used [0x00000000ffc00000,0x00000000ffc00000,0x00000000ffe00000)
ParOldGen       total 10240K, used 0K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
object space 10240K, 0% used [0x00000000fec00000,0x00000000fec00000,0x00000000ff600000)
PSPermGen       total 21504K, used 3067K [0x00000000f9a00000, 0x00000000faf00000, 0x00000000fec00000)
object space 21504K, 14% used [0x00000000f9a00000,0x00000000f9cfec68,0x00000000faf00000)











-XX:+HeapDumpOnOutOfMemoryError、-XX:+HeapDumpPath
-XX:+HeapDumpOnOutOfMemoryError
　OOM时导出堆到文件根据这个文件，我们可以看到系统dump时发生了什么。
-XX:+HeapDumpPath
　　导出OOM的路径
例如我们设置如下的参数：
-Xmx20m -Xms5m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=d:/a.dump
上方意思是说，现在给堆内存最多分配20M的空间。如果发生了OOM异常，那就把dump信息导出到d:/a.dump文件中。

堆的分配参数总结：

根据实际事情调整新生代和幸存代的大小
官方推荐新生代占堆的3/8
幸存代占新生代的1/10
在OOM时，记得Dump出堆，确保可以排查现场问题


 *
 */
public class JavaTest {
    public static void main(String[] args) {
//        byte[] b = null;
//        for (int i = 0; i < 10; i++)
//            b = new byte[1 * 1024 * 1024];
        Vector v = new Vector();
        for (int i = 0; i < 25; i++)
            v.add(new byte[1 * 1024 * 1024]);

    }

}
