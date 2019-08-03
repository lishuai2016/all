# 1、运行程序
```java
public class HeapOOMError {
    public static void main(String[] args) {
        System.out.println();
        List<byte[]> list = new ArrayList<>();
        int i = 0;
        while (true) {
            list.add(new  byte[5*1024*1024]);
            System.out.println("count:"+(++i));
        }
    }
}
```

# 2、jvm参数
-Xms20m -Xmx20m -XX:+PrintGCDetails -XX:+PrintHeapAtGC

D:\soft-install\jdk8\bin\java -Xms20m -Xmx20m -XX:+PrintGCDetails -XX:+PrintHeapAtGC "-javaagent:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\lib\idea_rt.jar=53726:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\bin" -Dfile.encoding=UTF-8 -classpath D:\soft-install\jdk8\jre\lib\charsets.jar;D:\soft-install\jdk8\jre\lib\deploy.jar;D:\soft-install\jdk8\jre\lib\ext\access-bridge-64.jar;D:\soft-install\jdk8\jre\lib\ext\cldrdata.jar;D:\soft-install\jdk8\jre\lib\ext\dnsns.jar;D:\soft-install\jdk8\jre\lib\ext\jaccess.jar;D:\soft-install\jdk8\jre\lib\ext\jfxrt.jar;D:\soft-install\jdk8\jre\lib\ext\localedata.jar;D:\soft-install\jdk8\jre\lib\ext\nashorn.jar;D:\soft-install\jdk8\jre\lib\ext\sunec.jar;D:\soft-install\jdk8\jre\lib\ext\sunjce_provider.jar;D:\soft-install\jdk8\jre\lib\ext\sunmscapi.jar;D:\soft-install\jdk8\jre\lib\ext\sunpkcs11.jar;D:\soft-install\jdk8\jre\lib\ext\zipfs.jar;D:\soft-install\jdk8\jre\lib\javaws.jar;D:\soft-install\jdk8\jre\lib\jce.jar;D:\soft-install\jdk8\jre\lib\jfr.jar;D:\soft-install\jdk8\jre\lib\jfxswt.jar;D:\soft-install\jdk8\jre\lib\jsse.jar;D:\soft-install\jdk8\jre\lib\management-agent.jar;D:\soft-install\jdk8\jre\lib\plugin.jar;D:\soft-install\jdk8\jre\lib\resources.jar;D:\soft-install\jdk8\jre\lib\rt.jar;D:\IdeaProjects\lishuai-notes\ls-springboot2\springboot-32-jvm\target\classes;D:\maven-respo\org\springframework\boot\spring-boot-starter\2.0.4.RELEASE\spring-boot-starter-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot\2.0.4.RELEASE\spring-boot-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\spring-context\5.0.8.RELEASE\spring-context-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-aop\5.0.8.RELEASE\spring-aop-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-beans\5.0.8.RELEASE\spring-beans-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-expression\5.0.8.RELEASE\spring-expression-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-autoconfigure\2.0.4.RELEASE\spring-boot-autoconfigure-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-logging\2.0.4.RELEASE\spring-boot-starter-logging-2.0.4.RELEASE.jar;D:\maven-respo\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;D:\maven-respo\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;D:\maven-respo\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;D:\maven-respo\org\apache\logging\log4j\log4j-to-slf4j\2.10.0\log4j-to-slf4j-2.10.0.jar;D:\maven-respo\org\apache\logging\log4j\log4j-api\2.10.0\log4j-api-2.10.0.jar;D:\maven-respo\org\slf4j\jul-to-slf4j\1.7.25\jul-to-slf4j-1.7.25.jar;D:\maven-respo\javax\annotation\javax.annotation-api\1.3.2\javax.annotation-api-1.3.2.jar;D:\maven-respo\org\springframework\spring-core\5.0.8.RELEASE\spring-core-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-jcl\5.0.8.RELEASE\spring-jcl-5.0.8.RELEASE.jar;D:\maven-respo\org\yaml\snakeyaml\1.19\snakeyaml-1.19.jar com.ls.jvm.HeapOOMError

count:1
count:2
{Heap before GC invocations=1 (full 0):
 PSYoungGen      total 6144K, used 2295K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 40% used [0x00000000ff980000,0x00000000ffbbdd40,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
  to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 ParOldGen       total 13824K, used 10240K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 74% used [0x00000000fec00000,0x00000000ff600020,0x00000000ff980000)
 Metaspace       used 3443K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
[GC (Allocation Failure) [PSYoungGen: 2295K->504K(6144K)] 12535K->10997K(19968K), 0.0041737 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap after GC invocations=1 (full 0):
 PSYoungGen      total 6144K, used 504K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 0% used [0x00000000ff980000,0x00000000ff980000,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff00000,0x00000000fff7e030,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 13824K, used 10493K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff63f6d0,0x00000000ff980000)
 Metaspace       used 3443K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
}
count:3
{Heap before GC invocations=2 (full 0):
 PSYoungGen      total 6144K, used 5736K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 92% used [0x00000000ff980000,0x00000000ffe9c2e0,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff00000,0x00000000fff7e030,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 13824K, used 10493K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff63f6d0,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
[GC (Allocation Failure) --[PSYoungGen: 5736K->5736K(6144K)] 16230K->16270K(19968K), 0.1148308 secs] [Times: user=0.05 sys=0.00, real=0.12 secs] 
Heap after GC invocations=2 (full 0):
 PSYoungGen      total 6144K, used 5736K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 92% used [0x00000000ff980000,0x00000000ffe9c2e0,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff00000,0x00000000fff7e030,0x00000000fff80000)
  to   space 512K, 95% used [0x00000000fff80000,0x00000000ffffa020,0x0000000100000000)
 ParOldGen       total 13824K, used 10533K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 76% used [0x00000000fec00000,0x00000000ff6496d0,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
}
{Heap before GC invocations=3 (full 1):
 PSYoungGen      total 6144K, used 5736K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 92% used [0x00000000ff980000,0x00000000ffe9c2e0,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff00000,0x00000000fff7e030,0x00000000fff80000)
  to   space 512K, 95% used [0x00000000fff80000,0x00000000ffffa020,0x0000000100000000)
 ParOldGen       total 13824K, used 10533K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 76% used [0x00000000fec00000,0x00000000ff6496d0,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
[Full GC (Ergonomics) [PSYoungGen: 5736K->5585K(6144K)] [ParOldGen: 10533K->10472K(13824K)] 16270K->16057K(19968K), [Metaspace: 3444K->3444K(1056768K)], 0.0051180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap after GC invocations=3 (full 1):
 PSYoungGen      total 6144K, used 5585K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 99% used [0x00000000ff980000,0x00000000ffef44c0,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 13824K, used 10472K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff63a210,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
}
{Heap before GC invocations=4 (full 1):
 PSYoungGen      total 6144K, used 5585K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 99% used [0x00000000ff980000,0x00000000ffef44c0,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 13824K, used 10472K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff63a210,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
[GC (Allocation Failure) --[PSYoungGen: 5585K->5585K(6144K)] 16057K->16073K(19968K), 0.0005845 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap after GC invocations=4 (full 1):
 PSYoungGen      total 6144K, used 5585K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 99% used [0x00000000ff980000,0x00000000ffef44c0,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
  to   space 512K, 98% used [0x00000000fff80000,0x00000000ffffe030,0x0000000100000000)
 ParOldGen       total 13824K, used 10488K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff63e210,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
}
{Heap before GC invocations=5 (full 2):
 PSYoungGen      total 6144K, used 5585K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 99% used [0x00000000ff980000,0x00000000ffef44c0,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
  to   space 512K, 98% used [0x00000000fff80000,0x00000000ffffe030,0x0000000100000000)
 ParOldGen       total 13824K, used 10488K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff63e210,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
[Full GC (Allocation Failure) [PSYoungGen: 5585K->5572K(6144K)] [ParOldGen: 10488K->10466K(13824K)] 16073K->16039K(19968K), [Metaspace: 3444K->3444K(1056768K)], 0.0041533 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap after GC invocations=5 (full 2):
 PSYoungGen      total 6144K, used 5572K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 98% used [0x00000000ff980000,0x00000000ffef12d8,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 13824K, used 10466K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff638ba0,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
}
{Heap before GC invocations=6 (full 2):
 PSYoungGen      total 6144K, used 5632K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 100% used [0x00000000ff980000,0x00000000fff00000,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 13824K, used 10466K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff638ba0,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
[GC (Allocation Failure) Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
[PSYoungGen: 5632K->504K(6144K)] 16098K->10970K(19968K), 0.0129345 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
	at com.ls.jvm.HeapOOMError.main(HeapOOMError.java:17)
Heap after GC invocations=6 (full 2):
 PSYoungGen      total 6144K, used 504K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 0% used [0x00000000ff980000,0x00000000ff980000,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff80000,0x00000000ffffe030,0x0000000100000000)
  to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 ParOldGen       total 13824K, used 10466K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff638ba0,0x00000000ff980000)
 Metaspace       used 3444K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 375K, capacity 388K, committed 512K, reserved 1048576K
}
Heap
 PSYoungGen      total 6144K, used 802K [0x00000000ff980000, 0x0000000100000000, 0x0000000100000000)
  eden space 5632K, 5% used [0x00000000ff980000,0x00000000ff9cab88,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff80000,0x00000000ffffe030,0x0000000100000000)
  to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 ParOldGen       total 13824K, used 10466K [0x00000000fec00000, 0x00000000ff980000, 0x00000000ff980000)
  object space 13824K, 75% used [0x00000000fec00000,0x00000000ff638ba0,0x00000000ff980000)
 Metaspace       used 3475K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 379K, capacity 388K, committed 512K, reserved 1048576K

Process finished with exit code 1
