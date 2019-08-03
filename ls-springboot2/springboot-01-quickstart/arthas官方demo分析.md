官方文档：
https://alibaba.github.io/arthas/quick-start.html
https://alibaba.github.io/arthas/advanced-use.html
[Arthas异步调用](https://alibaba.github.io/arthas/async.html)
[web-console] https://alibaba.github.io/arthas/web-console.html
[执行结果存日志](https://alibaba.github.io/arthas/save-log.html)
[批处理功能](https://alibaba.github.io/arthas/batch-support.html)
[ognl表达式的用法说明](https://github.com/alibaba/arthas/issues/11)


#基础命令
help——查看命令帮助信息
cls——清空当前屏幕区域
session——查看当前会话的信息
reset——重置增强类，将被 Arthas 增强过的类全部还原，Arthas 服务端关闭时会重置所有增强过的类
version——输出当前目标 Java 进程所加载的 Arthas 版本号
history——打印命令历史
quit——退出当前 Arthas 客户端，其他 Arthas 客户端不受影响
shutdown——关闭 Arthas 服务端，所有 Arthas 客户端全部退出
keymap——Arthas快捷键列表及自定义快捷键

#jvm相关
dashboard——当前系统的实时数据面板
thread——查看当前 JVM 的线程堆栈信息
jvm——查看当前 JVM 的信息
sysprop——查看和修改JVM的系统属性
sysenv——查看JVM的环境变量
getstatic——查看类的静态属性
New! ognl——执行ognl表达式

# class/classloader相关
sc——查看JVM已加载的类信息
sm——查看已加载类的方法信息
dump——dump 已加载类的 byte code 到特定目录
redefine——加载外部的.class文件，redefine到JVM里
jad——反编译指定已加载类的源码
classloader——查看classloader的继承树，urls，类加载信息，使用classloader去getResource

#monitor/watch/trace相关
请注意，这些命令，都通过字节码增强技术来实现的，会在指定类的方法中插入一些切面来实现数据统计和观测，因此在线上、预发使用时，请尽量明确需要观测的类、方法以及条件，诊断结束要执行 shutdown 或将增强过的类执行 reset 命令。
monitor——方法执行监控
watch——方法执行数据观测
trace——方法内部调用路径，并输出方法路径上的每个节点上耗时
stack——输出当前方法被调用的调用路径
tt——方法执行数据的时空隧道，记录下指定方法每次调用的入参和返回信息，并能对这些不同的时间下调用进行观测

#options
options——查看或设置Arthas全局开关

#管道
Arthas支持使用管道对上述命令的结果进行进一步的处理，如sm java.lang.String * | grep 'index'

grep——搜索满足条件的结果
plaintext——将命令的结果去除ANSI颜色
wc——按行统计输出结果

#后台异步任务
当线上出现偶发的问题，比如需要watch某个条件，而这个条件一天可能才会出现一次时，异步后台任务就派上用场了

使用 > 将结果重写向到日志文件，使用 & 指定命令是后台运行，session断开不影响任务执行（生命周期默认为1天）
jobs——列出所有job
kill——强制终止任务
fg——将暂停的任务拉到前台执行
bg——将暂停的任务放到后台执行

#Web Console
通过websocket连接Arthas。

Arthas目前支持Web Console，用户在attach成功之后，可以直接访问：http://127.0.0.1:8563/。
可以填入IP，远程连接其它机器上的arthas。

Web Console
其他特性
异步命令支持
执行结果存日志
批处理的支持
ognl表达式的用法说明



# help  帮助查看命令
$ help                                                                                                                                                               
 NAME         DESCRIPTION                                                                                                                                            
 help         Display Arthas Help                                                                                                                                    
 keymap       Display all the available keymap for the specified connection.        [快捷键]                                                                                 
 sc           Search all the classes loaded by JVM                                                                                                                   
 sm           Search the method of classes loaded by JVM                                                                                                             
 classloader  Show classloader info               [查看类加载器有哪些，以及各个类加载器加载的类的个数]                                                                                                                   
 jad          Decompile class              [查看编译后class文件类源码]                                                                                                                          
 getstatic    Show the static field of a class                                                                                                                       
 monitor      Monitor method execution statistics, e.g. total/success/failure count, average rt, fail rate, etc.                                                     
 stack        Display the stack trace for the specified class and method                                                                                             
 thread       Display thread info, thread stack                                                                                                                      
 trace        Trace the execution time of specified method invocation.                                                                                               
 watch        Display the input/output parameter, return object, and thrown exception of specified method invocation                                                 
 tt           Time Tunnel                                                                                                                                            
 jvm          Display the target JVM information          [jvm信息]                                                                                                           
 ognl         Execute ognl expression.                                                                                                                               
 dashboard    Overview of target jvm's thread, memory, gc, vm, tomcat info.    [jvm信息概览]                                                                                      
 dump         Dump class byte array from JVM                                                                                                                         
 options      View and change various Arthas options                                                                                                                 
 cls          Clear the screen                                                                                                                                       
 reset        Reset all the enhanced classes                                                                                                                         
 version      Display Arthas version         [版本信息]                                                                                                                        
 shutdown     Shutdown Arthas server and exit the console                                                                                                            
 session      Display current session information                                                                                                                    
 sysprop      Display, and change the system properties.   [系统属性信息]                                                                                                          
 sysenv       Display the system env.                                                                                                                                
 redefine     Redefine classes. @see Instrumentation#redefineClasses(ClassDefinition...)                                                                             
 history      Display command history                                                                                                                                

# keymap  快捷键
$ keymap                                                                                                                                                             
"\C-a": beginning-of-line      行头                                                                                                                                      
"\C-e": end-of-line                 行尾                                                                                                                                 
"\C-f": forward-word                                                                                                                                                 
"\C-b": backward-word                                                                                                                                                
"\e[D": backward-char                                                                                                                                                
"\e[C": forward-char                                                                                                                                                 
"\e[A": history-search-backward                                                                                                                                      
"\e[B": history-search-forward                                                                                                                                       
"\C-h": backward-delete-char                                                                                                                                         
"\C-?": backward-delete-char                                                                                                                                         
"\C-u": undo                                                                                                                                                         
"\C-d": delete-char                                                                                                                                                  
"\C-k": kill-line                                                                                                                                                    
"\C-i": complete                                                                                                                                                     
"\C-j": accept-line                                                                                                                                                  
"\C-m": accept-line                                                                                                                                                  
"\C-w": backward-delete-word                                                                                                                                         
"\C-x\e[3~": backward-kill-line                                                                                                                                      
"\e\C-?": backward-kill-word 




# classloader 类加载器信息
$ classloader                                                                                                                                                        
 name                                       numberOfInstances  loadedCountTotal                                                                                      
 com.taobao.arthas.agent.ArthasClassloader  1                  2529                                                                                                  
 BootstrapClassLoader                       1                  1962                                                                                                  
 sun.reflect.DelegatingClassLoader          9                  9                                                                                                     
 sun.misc.Launcher$AppClassLoader           1                  7                                                                                                     
 sun.misc.Launcher$ExtClassLoader           1                  4                                                                                                     
Affect(row-cnt:5) cost in 9 ms.   



# jvm

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





# demo测试的源码：
arthas-demo是一个简单的程序，每隔一秒生成一个随机数，再执行质因式分解，并打印出分解结果。

com.ls.MathGame
```java
public class MathGame {
    private static Random random = new Random();

    public int illegalArgumentCount = 0;

    public static void main(String[] args) throws InterruptedException {
        MathGame game = new MathGame();
        while (true) {
            game.run();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void run() throws InterruptedException {
        try {
            int number = random.nextInt();
            List<Integer> primeFactors = primeFactors(number);
            print(number, primeFactors);

        } catch (Exception e) {
            System.out.println(String.format("illegalArgumentCount:%3d, ", illegalArgumentCount) + e.getMessage());
        }
    }

    public static void print(int number, List<Integer> primeFactors) {
        StringBuffer sb = new StringBuffer(number + "=");
        for (int factor : primeFactors) {
            sb.append(factor).append('*');
        }
        if (sb.charAt(sb.length() - 1) == '*') {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
    }

    public List<Integer> primeFactors(int number) {
        if (number < 2) {
            illegalArgumentCount++;
            throw new IllegalArgumentException("number is: " + number + ", need >= 2");
        }

        List<Integer> result = new ArrayList<Integer>();
        int i = 2;
        while (i <= number) {
            if (number % i == 0) {
                result.add(i);
                number = number / i;
                i = 2;
            } else {
                i++;
            }
        }

        return result;
    }
}

```


# 1、查看dashboard
格式：dashboard
输入dashboard，按enter/回车，会展示当前进程的信息，按ctrl+c可以中断执行。
$ dashboard
ID            NAME                                     GROUP                       PRIORITY     STATE         %CPU          TIME          INTERRUPTED  DAEMON        
12            AsyncAppender-Worker-arthas-cache.result system                      5            WAITING       0             0:0           false        true          
5             Attach Listener                          system                      5            RUNNABLE      0             0:0           false        true          
3             Finalizer                                system                      8            WAITING       0             0:0           false        true          
6             Monitor Ctrl-Break                       main                        5            RUNNABLE      0             0:0           false        true          
2             Reference Handler                        system                      10           WAITING       0             0:0           false        true          
4             Signal Dispatcher                        system                      9            RUNNABLE      0             0:0           false        true          
25            Timer-for-arthas-dashboard-fabfa00d-c8c7 system                      10           RUNNABLE      0             0:0           false        true          
24            as-command-execute-daemon                system                      10           TIMED_WAITING 0             0:0           false        true          
14            job-timeout                              system                      5            TIMED_WAITING 0             0:0           false        true          
1             main                                     main                        5            TIMED_WAITING 0             0:6           false        false         
15            nioEventLoopGroup-2-1                    system                      10           RUNNABLE      0             0:0           false        false         
16            nioEventLoopGroup-3-1                    system                      10           RUNNABLE      0             0:0           false        false         
19            nioEventLoopGroup-3-2                    system                      10           RUNNABLE      0             0:0           false        false         
20            nioEventLoopGroup-3-3                    system                      10           RUNNABLE      0             0:0           false        false         
21            nioEventLoopGroup-3-4                    system                      10           RUNNABLE      0             0:0           false        false         
Memory                              used       total       max         usage       GC                                                                                
heap                                46M        147M        1717M       2.69%       gc.ps_scavenge.count                     4                                        
ps_eden_space                       32M        61M         634M        5.10%       gc.ps_scavenge.time(ms)                  54                                       
ps_survivor_space                   4M         5M          5M          99.85%      gc.ps_marksweep.count                    0                                        
ps_old_gen                          8M         81M         1288M       0.68%       gc.ps_marksweep.time(ms)                 0                                        
nonheap                             23M        23M         -1          96.40%                                                                                        
code_cache                          4M         4M          240M        1.72%                                                                                         
metaspace                           16M        17M         -1          97.11%                                                                                        
Runtime                                                                                                                                                              
os.name                                   Windows 7                                                                                                                  
os.version                                6.1                                                                                                                        
java.version                              1.8.0_181                                                                                                                  
java.home                                 D:\soft-install\jdk8\jre                                                                                                   
systemload.average                        -1.00                                                                                                                      
processors                                4                                                                                                                          
uptime                                    52s    

# 2、thread
通过thread命令来获取到arthas-demo进程的Main Class
$ thread -n -1 | grep 'main('                                                                                                                                        
    at com.ls.MathGame.main(MathGame.java:22) 


$ thread                                                                                                                                                             
Threads Total: 18, NEW: 0, RUNNABLE: 11, BLOCKED: 0, WAITING: 4, TIMED_WAITING: 3, TERMINATED: 0                                                                     
ID            NAME                                     GROUP                       PRIORITY     STATE         %CPU          TIME          INTERRUPTED  DAEMON        
12            AsyncAppender-Worker-arthas-cache.result system                      5            WAITING       0             0:0           false        true          
5             Attach Listener                          system                      5            RUNNABLE      0             0:0           false        true          
3             Finalizer                                system                      8            WAITING       0             0:0           false        true          
6             Monitor Ctrl-Break                       main                        5            RUNNABLE      0             0:0           false        true          
2             Reference Handler                        system                      10           WAITING       0             0:0           false        true          
4             Signal Dispatcher                        system                      9            RUNNABLE      0             0:0           false        true          
26            as-command-execute-daemon                system                      10           RUNNABLE      0             0:0           false        true          
14            job-timeout                              system                      5            TIMED_WAITING 0             0:0           false        true          
1             main                                     main                        5            TIMED_WAITING 0             0:27          false        false         
15            nioEventLoopGroup-2-1                    system                      10           RUNNABLE      0             0:0           false        false         
16            nioEventLoopGroup-3-1                    system                      10           RUNNABLE      0             0:0           false        false         
19            nioEventLoopGroup-3-2                    system                      10           RUNNABLE      0             0:0           false        false         
20            nioEventLoopGroup-3-3                    system                      10           RUNNABLE      0             0:0           false        false         
21            nioEventLoopGroup-3-4                    system                      10           RUNNABLE      0             0:0           false        false         
22            nioEventLoopGroup-3-5                    system                      10           RUNNABLE      0             0:0           false        false         
23            nioEventLoopGroup-3-6                    system                      10           RUNNABLE      0             0:0           false        false         
17            pool-1-thread-1                          system                      5            TIMED_WAITING 0             0:0           false        false         
18            pool-2-thread-1                          system                      5            WAITING       0             0:0           false        false         
Affect(row-cnt:0) cost in 120 ms. 


# 3、通过jad来反编绎Main Class
格式：jad [类的全路径]

jad com.ls.MathGame


$ jad com.ls.MathGame                                                                                                                                                
                                                                                                                                                                     
ClassLoader:                                                                                                                                                         
+-sun.misc.Launcher$AppClassLoader@18b4aac2                                                                                                                          
  +-sun.misc.Launcher$ExtClassLoader@65b54208                                                                                                                        
                                                                                                                                                                     
Location:                                                                                                                                                            
/D:/IdeaProjects/lishuai-notes/ls-springboot2/springboot-01-quickstart/target/classes/                                                                               
                                                                                                                                                                     
/*                                                                                                                                                                   
 * Decompiled with CFR 0_132.                                                                                                                                        
 */                                                                                                                                                                  
package com.ls;                                                                                                                                                      
                                                                                                                                                                     
import java.io.PrintStream;                                                                                                                                          
import java.util.ArrayList;                                                                                                                                          
import java.util.Iterator;                                                                                                                                           
import java.util.List;                                                                                                                                               
import java.util.Random;                                                                                                                                             
import java.util.concurrent.TimeUnit;                                                                                                                                
                                                                                                                                                                     
public class MathGame {                                                                                                                                              
    private static Random random = new Random();                                                                                                                     
    public int illegalArgumentCount = 0;                                                                                                                             
                                                                                                                                                                     
    public static void main(String[] args) throws InterruptedException {                                                                                             
        MathGame game = new MathGame();                                                                                                                              
        do {                                                                                                                                                         
            game.run();                                                                                                                                              
            TimeUnit.SECONDS.sleep(1L);                                                                                                                              
        } while (true);                                                                                                                                              
    }                                                                                                                                                                
                                                                                                                                                                     
    public void run() throws InterruptedException {                                                                                                                  
        try {                                                                                                                                                        
            int number = random.nextInt();                                                                                                                           
            List<Integer> primeFactors = this.primeFactors(number);                                                                                                  
            MathGame.print(number, primeFactors);                                                                                                                    
        }                                                                                                                                                            
        catch (Exception e) {                                                                                                                                        
            System.out.println(String.format("illegalArgumentCount:%3d, ", this.illegalArgumentCount) + e.getMessage());                                             
        }                                                                                                                                                            
    }                                                                                                                                                                
                                                                                                                                                                     
    public static void print(int number, List<Integer> primeFactors) {                                                                                               
        StringBuffer sb = new StringBuffer("" + number + "=");                                                                                                       
        Iterator<Integer> iterator = primeFactors.iterator();                                                                                                        
        while (iterator.hasNext()) {                                                                                                                                 
            int factor = iterator.next();                                                                                                                            
            sb.append(factor).append('*');                                                                                                                           
        }                                                                                                                                                            
        if (sb.charAt(sb.length() - 1) == '*') {                                                                                                                     
            sb.deleteCharAt(sb.length() - 1);                                                                                                                        
        }                                                                                                                                                            
        System.out.println(sb);                                                                                                                                      
    }                                
    public List<Integer> primeFactors(int number) {                                                                                                                  
            if (number < 2) {                                                                                                                                            
                ++this.illegalArgumentCount;                                                                                                                             
                throw new IllegalArgumentException("number is: " + number + ", need >= 2");                                                                              
            }                                                                                                                                                            
            ArrayList<Integer> result = new ArrayList<Integer>();                                                                                                        
            int i = 2;                                                                                                                                                   
            while (i <= number) {                                                                                                                                        
                if (number % i == 0) {                                                                                                                                   
                    result.add(i);                                                                                                                                       
                    number /= i;                                                                                                                                         
                    i = 2;                                                                                                                                               
                    continue;                                                                                                                                            
                }                                                                                                                                                        
                ++i;                                                                                                                                                     
            }                                                                                                                                                            
            return result;                                                                                                                                               
        }                                                                                                                                                                
    }                                                                                                                                                                    
                                                                                                                                                                         
    Affect(row-cnt:1) cost in 1007 ms. 

# 4、 watch
格式：watch [类的全路径] [方法的名称] returnObj
通过watch命令来查看demo.MathGame#primeFactors函数的返回值：

$ watch  com.ls.MathGame primeFactors returnObj                                                                                                                      
Press Ctrl+C to abort.                                                                                                                                               
Affect(class-cnt:1 , method-cnt:1) cost in 56 ms.                                                                                                                    
ts=2018-12-30 22:03:43; [cost=2.350586ms] result=@ArrayList[                                                                                                         
    @Integer[2],                                                                                                                                                     
    @Integer[2],                                                                                                                                                     
    @Integer[2],                                                                                                                                                     
    @Integer[17],                                                                                                                                                    
    @Integer[79],                                                                                                                                                    
    @Integer[83],                                                                                                                                                    
    @Integer[1571],                                                                                                                                                  
]                                                                                                                                                                    
ts=2018-12-30 22:03:44; [cost=0.261638ms] result=null                                                                                                                
ts=2018-12-30 22:03:45; [cost=2.63752ms] result=@ArrayList[                                                                                                          
    @Integer[3],                                                                                                                                                     
    @Integer[9733],                                                                                                                                                  
    @Integer[42467],                                                                                                                                                 
]                                                                                                                                                                    
ts=2018-12-30 22:03:46; [cost=0.259372ms] result=null                                                                                                                
ts=2018-12-30 22:03:47; [cost=1.45241ms] result=@ArrayList[                                                                                                          
    @Integer[2],                                                                                                                                                     
    @Integer[2],                                                                                                                                                     
    @Integer[5],                                                                                                                                                     
    @Integer[5],                                                                                                                                                     
    @Integer[5],                                                                                                                                                     
    @Integer[5],                                                                                                                                                     
    @Integer[29],                                                                                                                                                    
    @Integer[22573],                                                                                                                                                 
]                                                                                                                                                                    
ts=2018-12-30 22:03:48; [cost=0.130252ms] result=null   



# 5、退出arthas
如果只是退出当前的连接，可以用quit或者exit命令。Attach到目标进程上的arthas还会继续运行，端口会保持开放，下次连接时可以直接连接上。
如果想完全退出arthas，可以执行shutdown命令。