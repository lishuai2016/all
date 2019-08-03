
# 1、查看dashboard
输入dashboard，按enter/回车，会展示当前进程的信息，按ctrl+c可以中断执行。
$ dashboard 
ID            NAME                                     GROUP                       PRIORITY     STATE         %CPU          TIME          INTERRUPTED  DAEMON        
54            AsyncAppender-Worker-arthas-cache.result system                      5            WAITING       0             0:0           false        true          
5             Attach Listener                          system                      5            RUNNABLE      0             0:0           false        true          
27            ContainerBackgroundProcessor[StandardEng main                        5            TIMED_WAITING 0             0:0           false        true          
46            DestroyJavaVM                            main                        5            RUNNABLE      0             0:2           false        false         
3             Finalizer                                system                      8            WAITING       0             0:0           false        true          
6             Monitor Ctrl-Break                       main                        5            RUNNABLE      0             0:0           false        true          
30            NioBlockingSelector.BlockPoller-1        main                        5            RUNNABLE      0             0:0           false        true          
16            RMI Scheduler(0)                         system                      5            TIMED_WAITING 0             0:0           false        true          
12            RMI TCP Accept-0                         system                      5            RUNNABLE      0             0:0           false        true          
14            RMI TCP Accept-0                         system                      5            RUNNABLE      0             4:52          false        true          
13            RMI TCP Accept-52238                     system                      5            RUNNABLE      0             0:0           false        true          
86            RMI TCP Connection(idle)                 RMI Runtime                 5            TIMED_WAITING 0             0:0           false        true          
2             Reference Handler                        system                      10           WAITING       0             0:0           false        true          
4             Signal Dispatcher                        system                      9            RUNNABLE      0             0:0           false        true          
91            Timer-for-arthas-dashboard-ffb74636-9f37 system                      10           RUNNABLE      0             0:0           false        true          
Memory [内存信息]                             used       total       max         usage       GC[GC信息]                                                                                
heap                                298M       384M        1717M       17.39%      gc.ps_scavenge.count                     12                                       
ps_eden_space                       248M       299M        612M        40.67%      gc.ps_scavenge.time(ms)                  348                                      
ps_survivor_space                   11M        12M         12M         93.07%      gc.ps_marksweep.count                    2                                        
ps_old_gen                          38M        73M         1288M       3.00%       gc.ps_marksweep.time(ms)                 249                                      
nonheap                             56M        63M         -1          88.30%                                                                                        
code_cache                          6M         11M         240M        2.51%                                                                                         
metaspace                           44M        46M         -1          95.79%                                                                                        
Runtime  [运行环境]                                                                                                                                                            
os.name                                   Windows 7                                                                                                                  
os.version                                6.1                                                                                                                        
java.version                              1.8.0_181                                                                                                                  
java.home                                 D:\soft-install\jdk8\jre                                                                                                   
systemload.average                        -1.00                                                                                                                      
processors                                4                                                                                                                          
uptime                                    23409s     

当前系统的实时数据面板，按 ctrl+c 退出。
当运行在Ali-tomcat时，会显示当前tomcat的实时信息，如HTTP请求的qps, rt, 错误数, 线程池信息等等
数据说明
ID: Java级别的线程ID，注意这个ID不能跟jstack中的nativeID一一对应
NAME: 线程名
GROUP: 线程组名
PRIORITY: 线程优先级, 1~10之间的数字，越大表示优先级越高
STATE: 线程的状态
CPU%: 线程消耗的cpu占比，采样100ms，将所有线程在这100ms内的cpu使用量求和，再算出每个线程的cpu使用占比。
TIME: 线程运行总时间，数据格式为分：秒
INTERRUPTED: 线程当前的中断位状态
DAEMON: 是否是daemon线程


# 2、thread查看线程 查看当前线程信息，查看线程的堆栈

通过thread命令来获取到arthas-demo进程的Main Class
thread -n -1 | grep 'main('


thread                                                                                                                                                             
Threads Total: 42, NEW: 0, RUNNABLE: 21, BLOCKED: 0, WAITING: 14, TIMED_WAITING: 7, TERMINATED: 0                                                                    
ID            NAME                                     GROUP                       PRIORITY     STATE         %CPU          TIME          INTERRUPTED  DAEMON        
14            RMI TCP Accept-0                         system                      5            RUNNABLE      50            5:29          false        true          
92            as-command-execute-daemon                system                      10           RUNNABLE      50            0:0           false        true          
54            AsyncAppender-Worker-arthas-cache.result system                      5            WAITING       0             0:0           false        true          
5             Attach Listener                          system                      5            RUNNABLE      0             0:0           false        true          
27            ContainerBackgroundProcessor[StandardEng main                        5            TIMED_WAITING 0             0:0           false        true          
46            DestroyJavaVM                            main                        5            RUNNABLE      0             0:2           false        false         
3             Finalizer                                system                      8            WAITING       0             0:0           false        true          
6             Monitor Ctrl-Break                       main                        5            RUNNABLE      0             0:0           false        true          
30            NioBlockingSelector.BlockPoller-1        main                        5            RUNNABLE      0             0:0           false        true          
16            RMI Scheduler(0)                         system                      5            TIMED_WAITING 0             0:0           false        true          
12            RMI TCP Accept-0                         system                      5            RUNNABLE      0             0:0           false        true          
13            RMI TCP Accept-52238                     system                      5            RUNNABLE      0             0:0           false        true          
86            RMI TCP Connection(idle)                 RMI Runtime                 5            TIMED_WAITING 0             0:0           false        true          
2             Reference Handler                        system                      10           WAITING       0             0:0           false        true          
4             Signal Dispatcher                        system                      9            RUNNABLE      0             0:0           false        true          
28            container-0                              main                        5            TIMED_WAITING 0             0:0           false        false         
43            http-nio-80-Acceptor-0                   main                        5            RUNNABLE      0             0:0           false        true          
44            http-nio-80-AsyncTimeout                 main                        5            TIMED_WAITING 0             0:0           false        true          
41            http-nio-80-ClientPoller-0               main                        5            RUNNABLE      0             0:0           false        true          
42            http-nio-80-ClientPoller-1               main                        5            RUNNABLE      0             0:0           false        true          
31            http-nio-80-exec-1                       main                        5            WAITING       0             0:0           false        true          
40            http-nio-80-exec-10                      main                        5            WAITING       0             0:0           false        true          
71            http-nio-80-exec-11                      main                        5            WAITING       0             0:0           false        true          
32            http-nio-80-exec-2                       main                        5            WAITING       0             0:0           false        true          
33            http-nio-80-exec-3                       main                        5            WAITING       0             0:0           false        true          
34            http-nio-80-exec-4                       main                        5            WAITING       0             0:0           false        true          
36            http-nio-80-exec-6                       main                        5            WAITING       0             0:0           false        true          
37            http-nio-80-exec-7                       main                        5            WAITING       0             0:0           false        true          
38            http-nio-80-exec-8                       main                        5            WAITING       0             0:0           false        true          
39            http-nio-80-exec-9                       main                        5            WAITING       0             0:0           false        true          
56            job-timeout                              system                      5            TIMED_WAITING 0             0:0           false        true          
57            nioEventLoopGroup-2-1                    system                      10           RUNNABLE      0             0:0           false        false         
58            nioEventLoopGroup-3-1                    system                      10           RUNNABLE      0             0:0           false        false         
61            nioEventLoopGroup-3-2                    system                      10           RUNNABLE      0             0:0           false        false         
62            nioEventLoopGroup-3-3                    system                      10           RUNNABLE      0             0:0           false        false         
63            nioEventLoopGroup-3-4                    system                      10           RUNNABLE      0             0:0           false        false         
64            nioEventLoopGroup-3-5                    system                      10           RUNNABLE      0             0:0           false        false         
65            nioEventLoopGroup-3-6                    system                      10           RUNNABLE      0             0:0           false        false         
78            nioEventLoopGroup-3-7                    system                      10           RUNNABLE      0             0:0           false        false         
83            nioEventLoopGroup-3-8                    system                      10           RUNNABLE      0             0:0           false        false         
59            pool-1-thread-1                          system                      5            TIMED_WAITING 0             0:0           false        false         
60            pool-2-thread-1                          system                      5            WAITING       0             0:0           false        false         
Affect(row-cnt:0) cost in 115 ms. 


支持一键展示当前最忙的前N个线程并打印堆栈：[thread -n 3]
当没有参数时，显示所有线程的信息[thread]
thread id， 显示指定线程的运行堆栈[thread id]
thread -b, 找出当前阻塞其他线程的线程[thread -b]
有时候我们发现应用卡住了， 通常是由于某个线程拿住了某个锁， 并且其他线程都在等待这把锁造成的。 
为了排查这类问题， arthas提供了thread -b， 一键找出那个罪魁祸首。
注意， 目前只支持找出synchronized关键字阻塞住的线程， 如果是java.util.concurrent.Lock， 目前还不支持。
thread -i, 指定采样时间间隔[hread -n 3 -i 1000]

#  3、jvm 查看当前JVM信息

$ jvm                                                                                                                                                                
 RUNTIME                                                                                                                                                             
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 MACHINE-NAME                                   12244@ZB-PF115ML1                                                                                                    
 JVM-START-TIME                                 2018-12-30 21:49:59                                                                                                  
 MANAGEMENT-SPEC-VERSION                        1.2                                                                                                                  
 SPEC-NAME                                      Java Virtual Machine Specification                                                                                   
 SPEC-VENDOR                                    Oracle Corporation                                                                                                   
 SPEC-VERSION                                   1.8                                                                                                                  
 VM-NAME                                        Java HotSpot(TM) 64-Bit Server VM                                                                                    
 VM-VENDOR                                      Oracle Corporation                                                                                                   
 VM-VERSION                                     25.181-b13                                                                                                           
 INPUT-ARGUMENTS                                -javaagent:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\lib\idea_rt.jar=61389:D:\idea-2017-new\IntelliJ IDEA 2017.2.2\bin 
                                                -Dfile.encoding=UTF-8                                                                                                
                                                                                                                                                                     
 CLASS-PATH                                     D:\soft-install\jdk8\jre\lib\charsets.jar;D:\soft-install\jdk8\jre\lib\deploy.jar;D:\soft-install\jdk8\jre\lib\ext\a 
                                                ccess-bridge-64.jar;D:\soft-install\jdk8\jre\lib\ext\cldrdata.jar;D:\soft-install\jdk8\jre\lib\ext\dnsns.jar;D:\soft 
                                                -install\jdk8\jre\lib\ext\jaccess.jar;D:\soft-install\jdk8\jre\lib\ext\jfxrt.jar;D:\soft-install\jdk8\jre\lib\ext\lo 
                                                caledata.jar;D:\soft-install\jdk8\jre\lib\ext\nashorn.jar;D:\soft-install\jdk8\jre\lib\ext\sunec.jar;D:\soft-install 
                                                \jdk8\jre\lib\ext\sunjce_provider.jar;D:\soft-install\jdk8\jre\lib\ext\sunmscapi.jar;D:\soft-install\jdk8\jre\lib\ex 
                                                t\sunpkcs11.jar;D:\soft-install\jdk8\jre\lib\ext\zipfs.jar;D:\soft-install\jdk8\jre\lib\javaws.jar;D:\soft-install\j 
                                                dk8\jre\lib\jce.jar;D:\soft-install\jdk8\jre\lib\jfr.jar;D:\soft-install\jdk8\jre\lib\jfxswt.jar;D:\soft-install\jdk 
                                                8\jre\lib\jsse.jar;D:\soft-install\jdk8\jre\lib\management-agent.jar;D:\soft-install\jdk8\jre\lib\plugin.jar;D:\soft 
                                                -install\jdk8\jre\lib\resources.jar;D:\soft-install\jdk8\jre\lib\rt.jar;D:\IdeaProjects\lishuai-notes\ls-springboot2 
                                                \springboot-01-quickstart\target\classes;D:\maven-respo\org\springframework\boot\spring-boot-starter-web\2.0.4.RELEA 
                                                SE\spring-boot-starter-web-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter\2.0.4.RELEA 
                                                SE\spring-boot-starter-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot\2.0.4.RELEASE\spring-bo 
                                                ot-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-autoconfigure\2.0.4.RELEASE\spring-boot-aut 
                                                oconfigure-2.0.4.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-logging\2.0.4.RELEASE\sprin 
                                                g-boot-starter-logging-2.0.4.RELEASE.jar;D:\maven-respo\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.j 
                                                ar;D:\maven-respo\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;D:\maven-respo\org\apache\logging\log4j\l 
                                                og4j-to-slf4j\2.10.0\log4j-to-slf4j-2.10.0.jar;D:\maven-respo\org\apache\logging\log4j\log4j-api\2.10.0\log4j-api-2. 
                                                10.0.jar;D:\maven-respo\org\slf4j\jul-to-slf4j\1.7.25\jul-to-slf4j-1.7.25.jar;D:\maven-respo\javax\annotation\javax. 
                                                annotation-api\1.3.2\javax.annotation-api-1.3.2.jar;D:\maven-respo\org\yaml\snakeyaml\1.19\snakeyaml-1.19.jar;D:\mav 
                                                en-respo\org\springframework\boot\spring-boot-starter-json\2.0.4.RELEASE\spring-boot-starter-json-2.0.4.RELEASE.jar; 
                                                D:\maven-respo\com\fasterxml\jackson\core\jackson-databind\2.9.6\jackson-databind-2.9.6.jar;D:\maven-respo\com\faste 
                                                rxml\jackson\core\jackson-annotations\2.9.0\jackson-annotations-2.9.0.jar;D:\maven-respo\com\fasterxml\jackson\core\ 
                                                jackson-core\2.9.6\jackson-core-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.9.6\ 
                                                jackson-datatype-jdk8-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.9.6\jackson- 
                                                datatype-jsr310-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\module\jackson-module-parameter-names\2.9.6\jackson-m 
                                                odule-parameter-names-2.9.6.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-tomcat\2.0.4.RELEASE\spr 
                                                ing-boot-starter-tomcat-2.0.4.RELEASE.jar;D:\maven-respo\org\apache\tomcat\embed\tomcat-embed-core\8.5.32\tomcat-emb 
                                                ed-core-8.5.32.jar;D:\maven-respo\org\apache\tomcat\embed\tomcat-embed-el\8.5.32\tomcat-embed-el-8.5.32.jar;D:\maven 
                                                -respo\org\apache\tomcat\embed\tomcat-embed-websocket\8.5.32\tomcat-embed-websocket-8.5.32.jar;D:\maven-respo\org\hi 
                                                bernate\validator\hibernate-validator\6.0.11.Final\hibernate-validator-6.0.11.Final.jar;D:\maven-respo\javax\validat 
                                                ion\validation-api\2.0.1.Final\validation-api-2.0.1.Final.jar;D:\maven-respo\org\jboss\logging\jboss-logging\3.3.2.F 
                                                inal\jboss-logging-3.3.2.Final.jar;D:\maven-respo\com\fasterxml\classmate\1.3.4\classmate-1.3.4.jar;D:\maven-respo\o 
                                                rg\springframework\spring-web\5.0.8.RELEASE\spring-web-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-b 
                                                eans\5.0.8.RELEASE\spring-beans-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-webmvc\5.0.8.RELEASE\spr 
                                                ing-webmvc-5.0.8.RELEASE.jar;D:\maven-respo\org\springframework\spring-aop\5.0.8.RELEASE\spring-aop-5.0.8.RELEASE.ja 
                                                r;D:\maven-respo\org\springframework\spring-context\5.0.8.RELEASE\spring-context-5.0.8.RELEASE.jar;D:\maven-respo\or 
                                                g\springframework\spring-expression\5.0.8.RELEASE\spring-expression-5.0.8.RELEASE.jar;D:\maven-respo\org\slf4j\slf4j 
                                                -api\1.7.25\slf4j-api-1.7.25.jar;D:\maven-respo\org\springframework\spring-core\5.0.8.RELEASE\spring-core-5.0.8.RELE 
                                                ASE.jar;D:\maven-respo\org\springframework\spring-jcl\5.0.8.RELEASE\spring-jcl-5.0.8.RELEASE.jar;D:\idea-2017-new\In 
                                                telliJ IDEA 2017.2.2\lib\idea_rt.jar                                                                                 
 BOOT-CLASS-PATH                                D:\soft-install\jdk8\jre\lib\resources.jar;D:\soft-install\jdk8\jre\lib\rt.jar;D:\soft-install\jdk8\jre\lib\sunrsasi 
                                                gn.jar;D:\soft-install\jdk8\jre\lib\jsse.jar;D:\soft-install\jdk8\jre\lib\jce.jar;D:\soft-install\jdk8\jre\lib\chars 
                                                ets.jar;D:\soft-install\jdk8\jre\lib\jfr.jar;D:\soft-install\jdk8\jre\classes                                        
 LIBRARY-PATH                                   D:\soft-install\jdk8\bin;C:\Windows\Sun\Java\bin;C:\Windows\system32;C:\Windows;C:\Program Files (x86)\Common Files\ 
                                                Oracle\Java\javapath;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\Windows\system 
                                                32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Program Files\Intel\WiFi\bin\; 
                                                C:\Program Files\Common Files\Intel\WirelessCommon\;C:\Program Files\Lenovo\Touch Fingerprint Software\;C:\Program F 
                                                iles (x86)\Intel\UCRT\;C:\Program Files\Intel\UCRT\;C:\Program Files (x86)\Intel\Intel(R) Management Engine Componen 
                                                ts\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Manage 
                                                ment Engine Components\IPT;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;D:\soft-install\Anaconda 
                                                3;D:\soft-install\Anaconda3\Scripts;D:\soft-install\Anaconda3\Library\bin;D:\anzhuangbao\maven\apache-maven-2.2.1-bi 
                                                n\apache-maven-2.2.1\bin;D:\soft-install\jdk8\bin;D:\soft-install\jdk8\jre\bin;D:\soft-install\Lenovo Fingerprint Re 
                                                ader\;D:\soft-install\Lenovo Fingerprint Reader\x86\;C:\Program Files\MySQL\MySQL Server 5.5\bin;D:\soft-install\MyS 
                                                QL\MySQL Server 5.5\bin;D:\soft-install\git2.16\Git\cmd;D:\soft-install\TortoiseGit\bin;D:\soft-install\nodejs\;C:\P 
                                                rogram Files\Intel\WiFi\bin\;C:\Program Files\Common Files\Intel\WirelessCommon\;C:\Users\lishuai29\AppData\Roaming\ 
                                                npm;.                                                                                                                
                                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 CLASS-LOADING                                                                                                                                                       
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 LOADED-CLASS-COUNT                             4170                                                                                                                 
 TOTAL-LOADED-CLASS-COUNT                       4170                                                                                                                 
 UNLOADED-CLASS-COUNT                           0                                                                                                                    
 IS-VERBOSE                                     false                                                                                                                
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 COMPILATION                                                                                                                                                         
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 NAME                                           HotSpot 64-Bit Tiered Compilers                                                                                      
 TOTAL-COMPILE-TIME                             5942(ms)                                                                                                             
                                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 GARBAGE-COLLECTORS                                                                                                                                                  
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 PS Scavenge                                    6/83(ms)                                                                                                             
 [count/time]                                                                                                                                                        
 PS MarkSweep                                   1/115(ms)                                                                                                            
 [count/time]                                                                                                                                                        
                                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 MEMORY-MANAGERS                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 CodeCacheManager                               Code Cache                                                                                                           
                                                                                                                                                                     
 Metaspace Manager                              Metaspace                                                                                                            
                                                Compressed Class Space                                                                                               
                                                                                                                                                                     
 PS Scavenge                                    PS Eden Space                                                                                                        
 PS Survivor Space                                                                                                    
                                                                                                                                                                     
 PS MarkSweep                                   PS Eden Space                                                                                                        
                                                PS Survivor Space                                                                                                    
                                                PS Old Gen                                                                                                           
                                                                                                                                                                     
                                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 MEMORY                                                                                                                                                              
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 HEAP-MEMORY-USAGE                              219152384(209.00 MiB)/127926272(122.00 MiB)/1800929280(1.68 GiB)/97332208(92.82 MiB)                                 
 [committed/init/max/used]                                                                                                                                           
 NO-HEAP-MEMORY-USAGE                           37978112(36.22 MiB)/2555904(2.44 MiB)/-1(-1 B)/37089232(35.37 MiB)                                                   
 [committed/init/max/used]                                                                                                                                           
 PENDING-FINALIZE-COUNT                         0                                                                                                                    
                                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 OPERATING-SYSTEM                                                                                                                                                    
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 OS                                             Windows 7                                                                                                            
 ARCH                                           amd64                                                                                                                
 PROCESSORS-COUNT                               4                                                                                                                    
 LOAD-AVERAGE                                   -1.0                                                                                                                 
 VERSION                                        6.1                                                                               
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 THREAD                                                                                                                                                              
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 COUNT                                          18                                                                                                                   
 DAEMON-COUNT                                   8                                                                                                                    
 PEAK-COUNT                                     19                                                                                                                   
 STARTED-COUNT                                  27                                                                                                                   
 DEADLOCK-COUNT                                 0                                                                                                                    
                                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 FILE-DESCRIPTOR                                                                                                                                                     
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 MAX-FILE-DESCRIPTOR-COUNT                      -1                                                                                                                   
 OPEN-FILE-DESCRIPTOR-COUNT                     -1                                                                                                                   
Affect(row-cnt:0) cost in 76 ms.  


THREAD相关
- COUNT: JVM当前活跃的线程数
- DAEMON-COUNT: JVM当前活跃的守护线程数
- PEAK-COUNT: 从JVM启动开始曾经活着的最大线程数
- STARTED-COUNT: 从JVM启动开始总共启动过的线程次数
- DEADLOCK-COUNT: JVM当前死锁的线程数
文件描述符相关
- MAX-FILE-DESCRIPTOR-COUNT：JVM进程最大可以打开的文件描述符数
- OPEN-FILE-DESCRIPTOR-COUNT：JVM当前打开的文件描述符数


# 4、sysprop  查看当前JVM的系统属性(System Property)
使用参考
 USAGE:
   sysprop [-h] [property-name] [property-value]

 SUMMARY:
   Display, and change all the system properties.

 EXAMPLES:
 sysprop
 sysprop file.encoding
 sysprop production.mode true

 WIKI:
   https://alibaba.github.io/arthas/sysprop

 OPTIONS:
 -h, --help                                  this help
 <property-name>                             property name
 <property-value>                            property value
查看所有属性
$ sysprop
 KEY                                                  VALUE
-------------------------------------------------------------------------------------------------------------------------------------
 java.runtime.name                                    Java(TM) SE Runtime Environment
 sun.boot.library.path                                /Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre/lib
 java.vm.version                                      25.51-b03
 user.country.format                                  CN
 gopherProxySet                                       false
 java.vm.vendor                                       Oracle Corporation
 java.vendor.url                                      http://java.oracle.com/
 path.separator                                       :
 java.vm.name                                         Java HotSpot(TM) 64-Bit Server VM
 file.encoding.pkg                                    sun.io
 user.country                                         US
 sun.java.launcher                                    SUN_STANDARD
 sun.os.patch.level                                   unknown
 java.vm.specification.name                           Java Virtual Machine Specification
 user.dir                                             /private/var/tmp
 java.runtime.version                                 1.8.0_51-b16
 java.awt.graphicsenv                                 sun.awt.CGraphicsEnvironment
 java.endorsed.dirs                                   /Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre/lib/endors
                                                      ed
 os.arch                                              x86_64
 java.io.tmpdir                                       /var/folders/2c/tbxwzs4s4sbcvh7frbcc7n000000gn/T/
 line.separator

 java.vm.specification.vendor                         Oracle Corporation
 os.name                                              Mac OS X
 sun.jnu.encoding                                     UTF-8
 java.library.path                                    /Users/wangtao/Library/Java/Extensions:/Library/Java/Extensions:/Network/Libra
                                                      ry/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
 sun.nio.ch.bugLevel
 java.specification.name                              Java Platform API Specification
 java.class.version                                   52.0
 sun.management.compiler                              HotSpot 64-Bit Tiered Compilers
 os.version                                           10.12.6
 user.home                                            /Users/wangtao
 user.timezone                                        Asia/Shanghai
 java.awt.printerjob                                  sun.lwawt.macosx.CPrinterJob
 file.encoding                                        UTF-8
 java.specification.version                           1.8
 user.name                                            wangtao
 java.class.path                                      .
 java.vm.specification.version                        1.8
 sun.arch.data.model                                  64
 java.home                                            /Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre
 sun.java.command                                     Test
 java.specification.vendor                            Oracle Corporation
 user.language                                        en
 awt.toolkit                                          sun.lwawt.macosx.LWCToolkit
 java.vm.info                                         mixed mode
 java.version                                         1.8.0_51
 java.ext.dirs                                        /Users/wangtao/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.
                                                      8.0_51.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library
                                                      /Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
 sun.boot.class.path                                  /Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre/lib/resour
                                                      ces.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre/li
                                                      b/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre/l
                                                      ib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/H
                                                      ome/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Content
                                                      s/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Conte
                                                      nts/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jd
                                                      k/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_51.
                                                      jdk/Contents/Home/jre/classes
 java.vendor                                          Oracle Corporation
 file.separator                                       /
 java.vendor.url.bug                                  http://bugreport.sun.com/bugreport/
 sun.cpu.endian                                       little
 sun.io.unicode.encoding                              UnicodeBig
 sun.cpu.isalist


查看单个属性
支持通过TAB键自动补全
$ sysprop java.version
java.version=1.8.0_51

修改单个属性
$ sysprop user.country
user.country=US
$ sysprop user.country CN
Successfully changed the system property.
user.country=CN

# 5、sysenv  查看当前JVM的环境属性(System Environment Variables)
使用参考
 USAGE:
   sysenv [-h] [env-name]

 SUMMARY:
   Display the system env.

 EXAMPLES:
   sysenv
   sysenv USER

 WIKI:
   https://alibaba.github.io/arthas/sysenv

 OPTIONS:
 -h, --help                                                 this help
 <env-name>                                                 env name
查看所有环境变量
$ sysenv
 KEY                      VALUE
----------------------------------------------------------------------------------------------------------------------------
 PATH                     /Users/admin/.sdkman/candidates/visualvm/current/bin:/Users/admin/.sdkman/candidates/ja
                          va/current/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Applications/Wireshark.app/Contents/
                          MacOS
 SDKMAN_VERSION           5.7.3+337
 JAVA_HOME                /Users/admin/.sdkman/candidates/java/current
 JAVA_MAIN_CLASS_65244    demo.MathGame
 TERM                     xterm-256color
 LANG                     zh_CN.UTF-8
 AUTOJUMP_SOURCED         1
 COLORTERM                truecolor
 LOGNAME                  admin
 XPC_SERVICE_NAME         0
 PWD                      /Users/admin/code/ali/arthas/demo
 TERM_PROGRAM_VERSION     3.2.5
 _                        /Users/admin/.sdkman/candidates/java/current/bin/java
 SHELL                    /bin/bash
 TERM_PROGRAM             iTerm.app
 SDKMAN_PLATFORM          Darwin
 USER                     admin
 ITERM_PROFILE            Default
 TMPDIR                   /var/folders/0r/k561bkk917gg972stqclbz9h0000gn/T/
 XPC_FLAGS                0x0
 TERM_SESSION_ID          w0t4p0:60BC264D-9649-42AC-A7E4-AF85B69F93F8
 __CF_USER_TEXT_ENCODING  0x1F5:0x19:0x34
 Apple_PubSub_Socket_Ren  /private/tmp/com.apple.launchd.DwmmjSQsll/Render
 der
 COLORFGBG                7;0
 HOME                     /Users/admin
 SHLVL                    1
 AUTOJUMP_ERROR_PATH      /Users/admin/Library/autojump/errors.log


查看单个环境变量
支持通过TAB键自动补全
$ sysenv USER
USER=admin






# 6、sc [Search-Class]  查看JVM已加载的类信息
“Search-Class” 的简写，这个命令能搜索出所有已经加载到 JVM 中的 Class 信息，这个命令支持的参数有 [d]、[E]、[f] 和 [x:]。

参数说明
参数说明
参数名称	参数说明
class-pattern	类名表达式匹配
method-pattern	方法名表达式匹配
[d]	输出当前类的详细信息，包括这个类所加载的原始文件来源、类的声明、加载的ClassLoader等详细信息。
如果一个类被多个ClassLoader所加载，则会出现多次
[E]	开启正则表达式匹配，默认为通配符匹配
[f]	输出当前类的成员变量信息（需要配合参数-d一起使用）
[x:]	指定输出静态变量时属性的遍历深度，默认为 0，即直接使用 toString 输出
class-pattern支持全限定名，如com.taobao.test.AAA，也支持com/taobao/test/AAA这样的格式，这样，我们从异常堆栈里面把类名拷贝过来的时候，不需要在手动把/替换为.啦。
sc 默认开启了子类匹配功能，也就是说所有当前类的子类也会被搜索出来，想要精确的匹配，请打开options disable-sub-class true开关
使用参考
模糊搜索

$ sc demo.*
demo.MathGame
Affect(row-cnt:1) cost in 55 ms.
打印类的详细信息

$ sc -d demo.MathGame
class-info        demo.MathGame
code-source       /private/tmp/arthas-demo.jar
name              demo.MathGame
isInterface       false
isAnnotation      false
isEnum            false
isAnonymousClass  false
isArray           false
isLocalClass      false
isMemberClass     false
isPrimitive       false
isSynthetic       false
simple-name       MathGame
modifier          public
annotation
interfaces
super-class       +-java.lang.Object
class-loader      +-sun.misc.Launcher$AppClassLoader@3d4eac69
                    +-sun.misc.Launcher$ExtClassLoader@66350f69
classLoaderHash   3d4eac69
 
Affect(row-cnt:1) cost in 875 ms.
打印出类的Field信息

$ sc -d -f demo.MathGame
class-info        demo.MathGame
code-source       /private/tmp/arthas-demo.jar
name              demo.MathGame
isInterface       false
isAnnotation      false
isEnum            false
isAnonymousClass  false
isArray           false
isLocalClass      false
isMemberClass     false
isPrimitive       false
isSynthetic       false
simple-name       MathGame
modifier          public
annotation
interfaces
super-class       +-java.lang.Object
class-loader      +-sun.misc.Launcher$AppClassLoader@3d4eac69
                    +-sun.misc.Launcher$ExtClassLoader@66350f69
classLoaderHash   3d4eac69
fields            modifierprivate,static
                  type    java.util.Random
                  name    random
                  value   java.util.Random@522b4
                          08a
 
                  modifierprivate
                  type    int
                  name    illegalArgumentCount
 
Affect(row-cnt:1) cost in 19 ms.


# 7、sm   [Search-Method]  查看已加载类的方法信息
“Search-Method” 的简写，这个命令能搜索出所有已经加载了 Class 信息的方法信息。

sm 命令只能看到由当前类所声明 (declaring) 的方法，父类则无法看到。

参数说明
参数名称	参数说明
class-pattern	类名表达式匹配
method-pattern	方法名表达式匹配
[d]	展示每个方法的详细信息
[E]	开启正则表达式匹配，默认为通配符匹配
使用参考
$ sm java.lang.String
java.lang.String-><init>
java.lang.String->equals
java.lang.String->toString
java.lang.String->hashCode
java.lang.String->compareTo
java.lang.String->indexOf
java.lang.String->valueOf
java.lang.String->checkBounds
java.lang.String->length
java.lang.String->isEmpty
java.lang.String->charAt
java.lang.String->codePointAt
java.lang.String->codePointBefore
java.lang.String->codePointCount
java.lang.String->offsetByCodePoints
java.lang.String->getChars
java.lang.String->getBytes
java.lang.String->contentEquals
java.lang.String->nonSyncContentEquals
java.lang.String->equalsIgnoreCase
java.lang.String->compareToIgnoreCase
java.lang.String->regionMatches
java.lang.String->startsWith
java.lang.String->endsWith
java.lang.String->indexOfSupplementary
java.lang.String->lastIndexOf
java.lang.String->lastIndexOfSupplementary
java.lang.String->substring
java.lang.String->subSequence
java.lang.String->concat
java.lang.String->replace
java.lang.String->matches
java.lang.String->contains
java.lang.String->replaceFirst
java.lang.String->replaceAll
java.lang.String->split
java.lang.String->join
java.lang.String->toLowerCase
java.lang.String->toUpperCase
java.lang.String->trim
java.lang.String->toCharArray
java.lang.String->format
java.lang.String->copyValueOf
java.lang.String->intern
Affect(row-cnt:44) cost in 1342 ms.
$ sm -d java.lang.String toString
 declaring-class  java.lang.String
 method-name      toString
 modifier         public
 annotation
 parameters
 return           java.lang.String
 exceptions
 
Affect(row-cnt:1) cost in 3 ms.


# 8、dump  已加载类的 bytecode 到特定目录[生成.class文件]
参数说明
参数名称	参数说明
class-pattern	类名表达式匹配
[c:]	类所属 ClassLoader 的 hashcode
[E]	开启正则表达式匹配，默认为通配符匹配
使用参考
$ dump java.lang.String
 HASHCODE  CLASSLOADER  LOCATION
 null                   /Users/admin/logs/arthas/classdump/java/lang/String.class
Affect(row-cnt:1) cost in 119 ms.
$ dump demo.*
 HASHCODE  CLASSLOADER                                    LOCATION
 3d4eac69  +-sun.misc.Launcher$AppClassLoader@3d4eac69    /Users/admin/logs/arthas/classdump/sun.misc.Launcher$AppClassLoader-3d4eac69/demo/MathGame.class
             +-sun.misc.Launcher$ExtClassLoader@66350f69
Affect(row-cnt:1) cost in 39 ms.

# 9、stack  输出当前方法被调用的调用路径
很多时候我们都知道一个方法被执行，但这个方法被执行的路径非常多，或者你根本就不知道这个方法是从那里被执行了，此时你需要的是 stack 命令。

参数说明
参数名称	参数说明
class-pattern	类名表达式匹配
method-pattern	方法名表达式匹配
condition-express	条件表达式
[E]	开启正则表达式匹配，默认为通配符匹配
[n:]	执行次数限制


使用例子
启动 Demo
启动快速入门里的arthas-demo。

stack
$ stack demo.MathGame primeFactors
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 36 ms.
ts=2018-12-04 01:32:19;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)
据条件表达式来过滤
$ stack demo.MathGame primeFactors 'params[0]<0' -n 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 30 ms.
ts=2018-12-04 01:34:27;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)
 
ts=2018-12-04 01:34:30;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)
 
Command execution times exceed limit: 2, so command will exit. You can set it with -n option.
据执行时间来过滤
$ stack demo.MathGame primeFactors '#cost>5'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 35 ms.
ts=2018-12-04 01:35:58;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)


$ stack  com.ls.controller.HelloController hello                                                                                                                     
Press Ctrl+C to abort.                                                                                                                                               
Affect(class-cnt:1 , method-cnt:1) cost in 28 ms.                                                                                                                    
ts=2018-12-31 00:12:46;thread_name=http-nio-80-exec-6;id=24;is_daemon=true;priority=5;TCCL=org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoad
er@67cb033d                                                                                                                                                          
    @com.ls.controller.HelloController.hello()                                                                                                                       
        at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java:-2)                                                                            
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)                                                                             
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)                                                                     
        at java.lang.reflect.Method.invoke(Method.java:498)                                                                                                          
        at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:209)                                                   
        at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:136)                                           
        at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:102)               
        at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:877)             
        at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:783)                  
        at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)                                      
        at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:991)                                                                  
        at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:925)                                                                   
        at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:974)                                                                
        at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:866)                                                                         
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:635)                                                                                              
        at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:851)                                                                       
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)                                                                                              
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)                                                         
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)                                                                 
        at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)                                                                                    
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)                                                         
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)                                                                 
        at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)                                                        
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)                                                               
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)                                                         



# 10、classloader  查看classloader的继承树，urls，类加载信息
classloader 命令将 JVM 中所有的classloader的信息统计出来，并可以展示继承树，urls等。

可以让指定的classloader去getResources，打印出所有查找到的resources的url。对于ResourceNotFoundException比较有用。

参数说明
参数名称	参数说明
[l]	按类加载实例进行统计
[t]	打印所有ClassLoader的继承树
[a]	列出所有ClassLoader加载的类，请谨慎使用
[c:]	ClassLoader的hashcode
[c: r:]	用ClassLoader去查找resource
[c: load:]	用ClassLoader去加载指定的类
使用参考
按类加载类型查看统计信息
$ classloader
 name                                       numberOfInstances  loadedCountTotal
 com.taobao.arthas.agent.ArthasClassloader  1                  2115
 BootstrapClassLoader                       1                  1861
 sun.reflect.DelegatingClassLoader          5                  5
 sun.misc.Launcher$AppClassLoader           1                  4
 sun.misc.Launcher$ExtClassLoader           1                  1
Affect(row-cnt:5) cost in 3 ms.
按类加载实例查看统计信息
[classloader -l]
$ classloader -l
 name                                                loadedCount  hash      parent
 BootstrapClassLoader                                1861         null      null
 com.taobao.arthas.agent.ArthasClassloader@68b31f0a  2115         68b31f0a  sun.misc.Launcher$ExtClassLoader@66350f69
 sun.misc.Launcher$AppClassLoader@3d4eac69           4            3d4eac69  sun.misc.Launcher$ExtClassLoader@66350f69
 sun.misc.Launcher$ExtClassLoader@66350f69           1            66350f69  null
Affect(row-cnt:4) cost in 2 ms.
查看ClassLoader的继承树
[classloader -t]
$ classloader -t
+-BootstrapClassLoader
+-sun.misc.Launcher$ExtClassLoader@66350f69
  +-com.taobao.arthas.agent.ArthasClassloader@68b31f0a
  +-sun.misc.Launcher$AppClassLoader@3d4eac69
Affect(row-cnt:4) cost in 3 ms.
查看URLClassLoader实际的urls
[classloader -c 3d4eac69]
$ classloader -c 3d4eac69
file:/private/tmp/arthas-demo.jar
file:/Users/hengyunabc/.arthas/lib/3.0.5/arthas/arthas-agent.jar
Affect(row-cnt:9) cost in 3 ms.

使用ClassLoader去查找resource
[classloader -c 3d4eac69  -r META-INF/MANIFEST.MF]
$ classloader -c 3d4eac69  -r META-INF/MANIFEST.MF
 jar:file:/System/Library/Java/Extensions/MRJToolkit.jar!/META-INF/MANIFEST.MF
 jar:file:/private/tmp/arthas-demo.jar!/META-INF/MANIFEST.MF
 jar:file:/Users/hengyunabc/.arthas/lib/3.0.5/arthas/arthas-agent.jar!/META-INF/MANIFEST.MF


也可以尝试查找类的class文件：
[ classloader -c 1b6d3586 -r java/lang/String.class]
$ classloader -c 1b6d3586 -r java/lang/String.class
 jar:file:/Library/Java/JavaVirtualMachines/jdk1.8.0_60.jdk/Contents/Home/jre/lib/rt.jar!/java/lang/String.class


使用ClassLoader去加载类
[classloader -c 3d4eac69 --load demo.MathGame]
$ classloader -c 3d4eac69 --load demo.MathGame
load class success.
 class-info        demo.MathGame
 code-source       /private/tmp/arthas-demo.jar
 name              demo.MathGame
 isInterface       false
 isAnnotation      false
 isEnum            false
 isAnonymousClass  false
 isArray           false
 isLocalClass      false
 isMemberClass     false
 isPrimitive       false
 isSynthetic       false
 simple-name       MathGame
 modifier          public
 annotation
 interfaces
 super-class       +-java.lang.Object
 class-loader      +-sun.misc.Launcher$AppClassLoader@3d4eac69
                     +-sun.misc.Launcher$ExtClassLoader@66350f69
 classLoaderHash   3d4eac69



$ classloader                                                                                                                                                        
 name                                                numberOfInstances  loadedCountTotal                                                                             
 sun.misc.Launcher$AppClassLoader                    1                  3983                                                                                         
 BootstrapClassLoader                                1                  3106                                                                                         
 com.taobao.arthas.agent.ArthasClassloader           1                  1406                                                                                         
 sun.reflect.DelegatingClassLoader                   93                 93                                                                                           
 sun.misc.Launcher$ExtClassLoader                    1                  29                                                                                           
 javax.management.remote.rmi.NoCallStackClassLoader  2                  2                                                                                            
 sun.reflect.misc.MethodUtil                         1                  1                                                                                            
Affect(row-cnt:7) cost in 8 ms.                                                                                                                                      


$ classloader -l                                                                                                                                                     
 name                                                         loadedCount  hash      parent                                                                          
 BootstrapClassLoader                                         3106         null      null                                                                            
 com.taobao.arthas.agent.ArthasClassloader@4f018ebb           1409         4f018ebb  sun.misc.Launcher$ExtClassLoader@ee7d9f1                                        
 javax.management.remote.rmi.NoCallStackClassLoader@3d82c5f3  1            3d82c5f3  null                                                                            
 javax.management.remote.rmi.NoCallStackClassLoader@3b95a09c  1            3b95a09c  null                                                                            
 sun.misc.Launcher$AppClassLoader@18b4aac2                    3983         18b4aac2  sun.misc.Launcher$ExtClassLoader@ee7d9f1                                        
 sun.misc.Launcher$ExtClassLoader@ee7d9f1                     29           ee7d9f1   null                                                                            
 sun.reflect.misc.MethodUtil@329dcccd                         1            329dcccd  sun.misc.Launcher$AppClassLoader@18b4aac2                                       
Affect(row-cnt:7) cost in 22 ms.   

$ classloader -t                                                                                                                                                     
+-BootstrapClassLoader                                                                                                                                               
+-javax.management.remote.rmi.NoCallStackClassLoader@3d82c5f3                                                                                                        
+-javax.management.remote.rmi.NoCallStackClassLoader@3b95a09c                                                                                                        
+-sun.misc.Launcher$ExtClassLoader@ee7d9f1                                                                                                                           
  +-com.taobao.arthas.agent.ArthasClassloader@4f018ebb                                                                                                               
  +-sun.misc.Launcher$AppClassLoader@18b4aac2                                                                                                                        
    +-sun.reflect.misc.MethodUtil@329dcccd 

$ classloader -c 4f018ebb                                                                                                                                            
file:/D:/anzhuangbao/arthas-packaging-3.0.5-bin/arthas-core.jar                                                                                                      
                                                                                                                                                                     
Affect(row-cnt:99) cost in 10 ms.    


# 11、monitor方法执行监控
方法执行监控
对匹配 class-pattern／method-pattern的类、方法的调用进行监控。

monitor 命令是一个非实时返回命令.

实时返回命令是输入之后立即返回，而非实时返回的命令，则是不断的等待目标 Java 进程返回信息，直到用户输入 Ctrl+C 为止。

服务端是以任务的形式在后台跑任务，植入的代码随着任务的中止而不会被执行，所以任务关闭后，不会对原有性能产生太大影响，而且原则上，任何Arthas命令不会引起原有业务逻辑的改变。

监控的维度说明
监控项	说明
timestamp	时间戳
class	Java类
method	方法（构造方法、普通方法）
total	调用次数
success	成功次数
fail	失败次数
rt	平均RT
fail-rate	失败率
参数说明
方法拥有一个命名参数 [c:]，意思是统计周期（cycle of output），拥有一个整型的参数值

参数名称	参数说明
class-pattern	类名表达式匹配
method-pattern	方法名表达式匹配
[E]	开启正则表达式匹配，默认为通配符匹配
[c:]	统计周期，默认值为120秒
使用参考
[monitor -c 5 demo.MathGame primeFactors]
$ monitor -c 5 demo.MathGame primeFactors
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 94 ms.
 timestamp            class          method        total  success  fail  avg-rt(ms)  fail-rate
-----------------------------------------------------------------------------------------------
 2018-12-03 19:06:38  demo.MathGame  primeFactors  5      1        4     1.15        80.00%
 
 timestamp            class          method        total  success  fail  avg-rt(ms)  fail-rate
-----------------------------------------------------------------------------------------------
 2018-12-03 19:06:43  demo.MathGame  primeFactors  5      3        2     42.29       40.00%
 
 timestamp            class          method        total  success  fail  avg-rt(ms)  fail-rate
-----------------------------------------------------------------------------------------------
 2018-12-03 19:06:48  demo.MathGame  primeFactors  5      3        2     67.92       40.00%
 
 timestamp            class          method        total  success  fail  avg-rt(ms)  fail-rate
-----------------------------------------------------------------------------------------------
 2018-12-03 19:06:53  demo.MathGame  primeFactors  5      2        3     0.25        60.00%
 
 timestamp            class          method        total  success  fail  avg-rt(ms)  fail-rate
-----------------------------------------------------------------------------------------------
 2018-12-03 19:06:58  demo.MathGame  primeFactors  1      1        0     0.45        0.00%
 
 timestamp            class          method        total  success  fail  avg-rt(ms)  fail-rate
-----------------------------------------------------------------------------------------------
 2018-12-03 19:07:03  demo.MathGame  primeFactors  2      2        0     3182.72     0.00%


# 12、watch方法执行数据观测
让你能方便的观察到指定方法的调用情况。能观察到的范围为：返回值、抛出异常、入参，通过编写 OGNL 表达式进行对应变量的查看。

参数说明
watch 的参数比较多，主要是因为它能在 4 个不同的场景观察对象

参数名称	参数说明
class-pattern	类名表达式匹配
method-pattern	方法名表达式匹配
express	观察表达式
condition-express	条件表达式
[b]	在方法调用之前观察
[e]	在方法异常之后观察
[s]	在方法返回之后观察
[f]	在方法结束之后(正常返回和异常返回)观察
[E]	开启正则表达式匹配，默认为通配符匹配
[x:]	指定输出结果的属性遍历深度，默认为 1


watch 命令定义了4个观察事件点，即 -b 方法调用前，-e 方法异常后，-s 方法返回后，-f 方法结束后
4个观察事件点 -b、-e、-s 默认关闭，-f 默认打开，当指定观察点被打开后，在相应事件点会对观察表达式进行求值并输出
这里要注意方法入参和方法出参的区别，有可能在中间被修改导致前后不一致，除了 -b 事件点 params 代表方法入参外，其余事件都代表方法出参
当使用 -b 时，由于观察事件点是在方法调用前，此时返回值或异常均不存在
使用参考
启动 Demo
启动快速入门里的arthas-demo。

观察方法出参和返回值
$ watch demo.MathGame primeFactors "{params,returnObj}" -x 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 44 ms.
ts=2018-12-03 19:16:51; [cost=1.280502ms] result=@ArrayList[
    @Object[][
        @Integer[535629513],
    ],
    @ArrayList[
        @Integer[3],
        @Integer[19],
        @Integer[191],
        @Integer[49199],
    ],
]
观察方法入参
$ watch demo.MathGame primeFactors "{params,returnObj}" -x 2 -b
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 50 ms.
ts=2018-12-03 19:23:23; [cost=0.0353ms] result=@ArrayList[
    @Object[][
        @Integer[-1077465243],
    ],
    null,
]
对比前一个例子，返回值为空（事件点为方法执行前，因此获取不到返回值）
同时观察方法调用前和方法返回后
$ watch demo.MathGame primeFactors "{params,target,returnObj}" -x 2 -b -s -n 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 46 ms.
ts=2018-12-03 19:29:54; [cost=0.01696ms] result=@ArrayList[
    @Object[][
        @Integer[1544665400],
    ],
    @MathGame[
        random=@Random[java.util.Random@522b408a],
        illegalArgumentCount=@Integer[13038],
    ],
    null,
]
ts=2018-12-03 19:29:54; [cost=4.277392ms] result=@ArrayList[
    @Object[][
        @Integer[1544665400],
    ],
    @MathGame[
        random=@Random[java.util.Random@522b408a],
        illegalArgumentCount=@Integer[13038],
    ],
    @ArrayList[
        @Integer[2],
        @Integer[2],
        @Integer[2],
        @Integer[5],
        @Integer[5],
        @Integer[73],
        @Integer[241],
        @Integer[439],
    ],
]
参数里-n 2，表示只执行两次
这里输出结果中，第一次输出的是方法调用前的观察表达式的结果，第二次输出的是方法返回后的表达式的结果
结果的顺序和命令中 -s -b 的顺序没有关系，只与事件本身的先后顺序有关
调整-x的值，观察具体的方法参数值
$ watch demo.MathGame primeFactors "{params,target}" -x 3
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 58 ms.
ts=2018-12-03 19:34:19; [cost=0.587833ms] result=@ArrayList[
    @Object[][
        @Integer[47816758],
    ],
    @MathGame[
        random=@Random[
            serialVersionUID=@Long[3905348978240129619],
            seed=@AtomicLong[3133719055989],
            multiplier=@Long[25214903917],
            addend=@Long[11],
            mask=@Long[281474976710655],
            DOUBLE_UNIT=@Double[1.1102230246251565E-16],
            BadBound=@String[bound must be positive],
            BadRange=@String[bound must be greater than origin],
            BadSize=@String[size must be non-negative],
            seedUniquifier=@AtomicLong[-3282039941672302964],
            nextNextGaussian=@Double[0.0],
            haveNextNextGaussian=@Boolean[false],
            serialPersistentFields=@ObjectStreamField[][isEmpty=false;size=3],
            unsafe=@Unsafe[sun.misc.Unsafe@2eaa1027],
            seedOffset=@Long[24],
        ],
        illegalArgumentCount=@Integer[13159],
    ],
]
-x表示遍历深度，可以调整来打印具体的参数和结果内容，默认值是1。
条件表达式的例子
$ watch demo.MathGame primeFactors "{params[0],target}" "params[0]<0"
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 68 ms.
ts=2018-12-03 19:36:04; [cost=0.530255ms] result=@ArrayList[
    @Integer[-18178089],
    @MathGame[demo.MathGame@41cf53f9],
]
只有满足条件的调用，才会有响应。
观察异常信息的例子
$ watch demo.MathGame primeFactors "{params[0],throwExp}" -e -x 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 62 ms.
ts=2018-12-03 19:38:00; [cost=1.414993ms] result=@ArrayList[
    @Integer[-1120397038],
    java.lang.IllegalArgumentException: number is: -1120397038, need >= 2
    at demo.MathGame.primeFactors(MathGame.java:46)
    at demo.MathGame.run(MathGame.java:24)
    at demo.MathGame.main(MathGame.java:16)
,
]
-e表示抛出异常时才触发
express中，表示异常信息的变量是throwExp
按照耗时进行过滤
$ watch demo.MathGame primeFactors '{params, returnObj}' '#cost>200' -x 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 66 ms.
ts=2018-12-03 19:40:28; [cost=2112.168897ms] result=@ArrayList[
    @Object[][
        @Integer[2141897465],
    ],
    @ArrayList[
        @Integer[5],
        @Integer[428379493],
    ],
]
#cost>200(单位是ms)表示只有当耗时大于200ms时才会输出，过滤掉执行时间小于200ms的调用
观察当前对象中的属性
如果想查看方法运行前后，当前对象中的属性，可以使用target关键字，代表当前对象

$ watch demo.MathGame primeFactors 'target'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 52 ms.
ts=2018-12-03 19:41:52; [cost=0.477882ms] result=@MathGame[
    random=@Random[java.util.Random@522b408a],
    illegalArgumentCount=@Integer[13355],
]
然后使用target.field_name访问当前对象的某个属性

$ watch demo.MathGame primeFactors 'target.illegalArgumentCount'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 67 ms.
ts=2018-12-03 20:04:34; [cost=131.303498ms] result=@Integer[8]
ts=2018-12-03 20:04:35; [cost=0.961441ms] result=@Integer[8]



$ watch com.ls.controller.HelloController hello returnObj                                                                                                            
Press Ctrl+C to abort.                                                                                                                                               
Affect(class-cnt:1 , method-cnt:1) cost in 1696 ms.                                                                                                                  
ts=2018-12-30 21:30:59; [cost=2.028919ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:24; [cost=0.179334ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:24; [cost=0.167252ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:24; [cost=0.032468ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:24; [cost=0.033601ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:25; [cost=0.04455ms] result=@String[hello null]                                                                                                  
ts=2018-12-30 21:31:25; [cost=0.038131ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:25; [cost=0.094763ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:25; [cost=0.030581ms] result=@String[hello null]                                                                                                 
ts=2018-12-30 21:31:37; [cost=0.031336ms] result=@String[hello lll]                                                                                                  
ts=2018-12-30 21:31:45; [cost=0.038132ms] result=@String[hello ls]   