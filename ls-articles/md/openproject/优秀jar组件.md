---
title: 优秀jar组件
categories: 
- 开源组件
tags:
---

# 1、httpcomponents

    <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>${httpclient.version}</version>
        </dependency>

        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpcore</artifactId>
            <version>${httpcore.version}</version>
        </dependency>
        
        
        
# 2、guava
Google Guava软件包中的库或多或少是对核心库的对应部分有增强功能，并使编程更加高效和有效。
Guava 包括内存缓存、不可变集合、函数类型、图形库和可用于 I/O、散列、并发、原语、字符串处理、反射等等的API实用程序。
    
    <!--google tools谷歌工具箱-->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>19.0</version>
        </dependency>
        
        
# 3、fastjson
 <!--阿里巴巴json处理工具-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.28</version>
        </dependency>
        
      
# 4、druid
  <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
         <dependency>
             <groupId>com.alibaba</groupId>
             <artifactId>druid</artifactId>
             <version>1.0.15</version>
         </dependency>  
         
# 5、quartz
Quartz是一个完全由Java编写的开源作业调度框架，为在Java应用程序中进行作业调度提供了简单却强大的机制。
Quartz允许开发人员根据时间间隔来调度作业。
它实现了作业和触发器的多对多的关系，还能把多个作业与不同的触发器关联。
                 <!--spring定时任务-->
                 <dependency>
                     <groupId>org.quartz-scheduler</groupId>
                     <artifactId>quartz</artifactId>
                     <version>2.2.3</version>
                 </dependency>
                 
                 
01. JUnit
第一个要说的当然是JUnit了，JUnit毕竟是Java圈目前最知名及常用的测试框架。JUnit之所以能够成为Java圈中最热门的测试库，
是因为对于很多项目而言，单元测试是非常重要的。优点有很多
比如，给开发者提供了简洁的图形界面，可以轻松地写出可重复测试的代码，允许并发同时执行，
还允许开发者创建测试套件 (Test Suite) 来查看、检测整体的测试进度及测试期间发生的副作用等。

02. SLF4J

SLF4J或Simple Logging Facade for Java，它为不同的框架提供了一个抽象概念，允许开发人员在部署时插入任何框架。
它的功能在基于外观的简单日志API，并将客户端API与日志后端分开。
通过向classpath中添加所需的绑定，可以发现其后端。由于客户端API和后端完全解耦，因此它可以集成到任何框架或现有的代码片段。

03. Log4j
Log4j是Apache中的一个库，可用作日志工具。
Log4j恰好是其所在应用领域中最可靠的库，可以扩展到支持自定义组件配置。
配置语法非常简单，支持XML、YAML 和 JSON。并提供对多个API的支持，最重要的是，它的工作速度相当惊人。


05. XStream
当涉及将对象序列化到XML中时，这时常用XStream库, 开发人员通过XStream库可以轻松地将对象序列化为XML并返回。
XStream的功能也很多，比如，大多数对象可以被序列化，并提供特定的映射，提供高性能和低内存占用，信息不重复，
可自定义的转换策略，安全的框架，异常情况下的详细诊断等等。

06. iText
iText是用于在Java中创建和操作PDF件的Java开源库。
最近的iText版本改头换面，加入许多新功能。基本Java中创建和操作PDF件的各种操作都能完成

07. Apache PDF box
Apache PDFBox是另一个可用于操作PDF文件的开源库。
PDFBox的主要功能使其成为超级库，其中包括PDF创建、将单个PDF分割为多个PDF文件、合并并提取PDF文本的Unicode文本，
填写PDF表单，根据PDF/A标准验证PDF文件，将PDF保存为图像并对PDF进行数字签名。

08. jsoup
jsoup是一个很实用的Java库，用于处理和解析HTML。Jsoup提供了一个有用的用于提取数据的API。
jsoup中实现的标准是WHATWG HTML5。和最新的浏览器作法一样，jsoup将HTML解析为DOM。
它允许解析来自任何URL或文件的HTML，清理和操纵HTML元素和属性，以检索用户提交的数据并过滤掉XSS攻击属性，使用jsoup还可以完成更多功能。

09. Gson
Gson是Google的另一个库，它轻而易举的将Java Objects转换成等效的JSON表示形式。
它为Java泛型提供了极大的支持，并允许对象的自定义表示。

10. Joda Time
这就是我一直强调的简单但功能强大的库，它节省了大量的开发时间。 Joda-Time是一个Java库，作为Java中日期和时间类的一个很好的替代品。
Joda Time提供计算日期和时间的功能，并支持几乎所有需要的日期格式，而且肯定难以用简单的JDK方法进行复制。

11. Ok HTTP

用于通过HTTP协议有效地在现代应用程序之间交换数据。 Okhttp在断网时恢复连接，在多个基于IP的服务中切换IP地址。
okhttp的一个有用的功能是与现代TLS(SNI，ALPN)的自动连接，并且在发生故障时回到TLS 1.0。



