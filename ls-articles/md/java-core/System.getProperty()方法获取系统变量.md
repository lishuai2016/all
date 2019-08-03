---
title: System.getProperty
categories: 
- java
tags:
---



```java
public class Main {
    public static void main(String[] args) {
        System.out.println("java版本号：" + System.getProperty("java.version")); // java版本号
        System.out.println("Java提供商名称：" + System.getProperty("java.vendor")); // Java提供商名称
        System.out.println("Java提供商网站：" + System.getProperty("java.vendor.url")); // Java提供商网站
        System.out.println("jre目录：" + System.getProperty("java.home")); // Java，哦，应该是jre目录
        System.out.println("Java虚拟机规范版本号：" + System.getProperty("java.vm.specification.version")); // Java虚拟机规范版本号
        System.out.println("Java虚拟机规范提供商：" + System.getProperty("java.vm.specification.vendor")); // Java虚拟机规范提供商
        System.out.println("Java虚拟机规范名称：" + System.getProperty("java.vm.specification.name")); // Java虚拟机规范名称
        System.out.println("Java虚拟机版本号：" + System.getProperty("java.vm.version")); // Java虚拟机版本号
        System.out.println("Java虚拟机提供商：" + System.getProperty("java.vm.vendor")); // Java虚拟机提供商
        System.out.println("Java虚拟机名称：" + System.getProperty("java.vm.name")); // Java虚拟机名称
        System.out.println("Java规范版本号：" + System.getProperty("java.specification.version")); // Java规范版本号
        System.out.println("Java规范提供商：" + System.getProperty("java.specification.vendor")); // Java规范提供商
        System.out.println("Java规范名称：" + System.getProperty("java.specification.name")); // Java规范名称
        System.out.println("Java类版本号：" + System.getProperty("java.class.version")); // Java类版本号
        System.out.println("Java类路径：" + System.getProperty("java.class.path")); // Java类路径
        System.out.println("Java lib路径：" + System.getProperty("java.library.path")); // Java lib路径
        System.out.println("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir")); // Java输入输出临时路径
        System.out.println("Java编译器：" + System.getProperty("java.compiler")); // Java编译器
        System.out.println("Java执行路径：" + System.getProperty("java.ext.dirs")); // Java执行路径
        System.out.println("操作系统名称：" + System.getProperty("os.name")); // 操作系统名称
        System.out.println("操作系统的架构：" + System.getProperty("os.arch")); // 操作系统的架构
        System.out.println("操作系统版本号：" + System.getProperty("os.version")); // 操作系统版本号
        System.out.println("文件分隔符：" + System.getProperty("file.separator")); // 文件分隔符
        System.out.println("路径分隔符：" + System.getProperty("path.separator")); // 路径分隔符
        System.out.println("直线分隔符：" + System.getProperty("line.separator")); // 直线分隔符
        System.out.println("操作系统用户名：" + System.getProperty("user.name")); // 用户名
        System.out.println("操作系统用户的主目录：" + System.getProperty("user.home")); // 用户的主目录
        System.out.println("当前程序所在目录：" + System.getProperty("user.dir")); // 当前程序所在目录

    }
 }
```

ava版本号：1.8.0_181
Java提供商名称：Oracle Corporation
Java提供商网站：http://java.oracle.com/
jre目录：D:\soft-install\jdk8\jre
Java虚拟机规范版本号：1.8
Java虚拟机规范提供商：Oracle Corporation
Java虚拟机规范名称：Java Virtual Machine Specification
Java虚拟机版本号：25.181-b13
Java虚拟机提供商：Oracle Corporation
Java虚拟机名称：Java HotSpot(TM) 64-Bit Server VM
Java规范版本号：1.8
Java规范提供商：Oracle Corporation
Java规范名称：Java Platform API Specification
Java类版本号：52.0
Java类路径：D:\soft-install\jdk8\jre\lib\charsets.jar;D:\soft-install\jdk8\jre\lib\deploy.jar;D:\soft-install\jdk8\jre\lib\ext\access-bridge-64.jar;D:\soft-install\jdk8\jre\lib\ext\cldrdata.jar;D:\soft-install\jdk8\jre\lib\ext\dnsns.jar;D:\soft-install\jdk8\jre\lib\ext\jaccess.jar;D:\soft-install\jdk8\jre\lib\ext\jfxrt.jar;D:\soft-install\jdk8\jre\lib\ext\localedata.jar;D:\soft-install\jdk8\jre\lib\ext\nashorn.jar;D:\soft-install\jdk8\jre\lib\ext\sunec.jar;D:\soft-install\jdk8\jre\lib\ext\sunjce_provider.jar;D:\soft-install\jdk8\jre\lib\ext\sunmscapi.jar;D:\soft-install\jdk8\jre\lib\ext\sunpkcs11.jar;D:\soft-install\jdk8\jre\lib\ext\zipfs.jar;D:\soft-install\jdk8\jre\lib\javaws.jar;D:\soft-install\jdk8\jre\lib\jce.jar;D:\soft-install\jdk8\jre\lib\jfr.jar;D:\soft-install\jdk8\jre\lib\jfxswt.jar;D:\soft-install\jdk8\jre\lib\jsse.jar;D:\soft-install\jdk8\jre\lib\management-agent.jar;D:\soft-install\jdk8\jre\lib\plugin.jar;D:\soft-install\jdk8\jre\lib\resources.jar;D:\soft-install\jdk8\jre\lib\rt.jar;D:\IdeaProjects\lishuai-notes\ls-mybatis-plus\target\classes;D:\maven-respo\org\springframework\boot\spring-boot-starter\2.0.3.RELEASE\spring-boot-starter-2.0.3.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot\2.0.3.RELEASE\spring-boot-2.0.3.RELEASE.jar;D:\maven-respo\org\springframework\spring-context\5.0.7.RELEASE\spring-context-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-autoconfigure\2.0.3.RELEASE\spring-boot-autoconfigure-2.0.3.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-logging\2.0.3.RELEASE\spring-boot-starter-logging-2.0.3.RELEASE.jar;D:\maven-respo\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;D:\maven-respo\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;D:\maven-respo\org\apache\logging\log4j\log4j-to-slf4j\2.10.0\log4j-to-slf4j-2.10.0.jar;D:\maven-respo\org\apache\logging\log4j\log4j-api\2.10.0\log4j-api-2.10.0.jar;D:\maven-respo\org\slf4j\jul-to-slf4j\1.7.25\jul-to-slf4j-1.7.25.jar;D:\maven-respo\javax\annotation\javax.annotation-api\1.3.2\javax.annotation-api-1.3.2.jar;D:\maven-respo\org\springframework\spring-core\5.0.7.RELEASE\spring-core-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\spring-jcl\5.0.7.RELEASE\spring-jcl-5.0.7.RELEASE.jar;D:\maven-respo\org\yaml\snakeyaml\1.19\snakeyaml-1.19.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-web\2.0.3.RELEASE\spring-boot-starter-web-2.0.3.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-json\2.0.3.RELEASE\spring-boot-starter-json-2.0.3.RELEASE.jar;D:\maven-respo\com\fasterxml\jackson\core\jackson-databind\2.9.6\jackson-databind-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\core\jackson-annotations\2.9.0\jackson-annotations-2.9.0.jar;D:\maven-respo\com\fasterxml\jackson\core\jackson-core\2.9.6\jackson-core-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.9.6\jackson-datatype-jdk8-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.9.6\jackson-datatype-jsr310-2.9.6.jar;D:\maven-respo\com\fasterxml\jackson\module\jackson-module-parameter-names\2.9.6\jackson-module-parameter-names-2.9.6.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-tomcat\2.0.3.RELEASE\spring-boot-starter-tomcat-2.0.3.RELEASE.jar;D:\maven-respo\org\apache\tomcat\embed\tomcat-embed-core\8.5.31\tomcat-embed-core-8.5.31.jar;D:\maven-respo\org\apache\tomcat\embed\tomcat-embed-el\8.5.31\tomcat-embed-el-8.5.31.jar;D:\maven-respo\org\apache\tomcat\embed\tomcat-embed-websocket\8.5.31\tomcat-embed-websocket-8.5.31.jar;D:\maven-respo\org\hibernate\validator\hibernate-validator\6.0.10.Final\hibernate-validator-6.0.10.Final.jar;D:\maven-respo\javax\validation\validation-api\2.0.1.Final\validation-api-2.0.1.Final.jar;D:\maven-respo\org\jboss\logging\jboss-logging\3.3.2.Final\jboss-logging-3.3.2.Final.jar;D:\maven-respo\com\fasterxml\classmate\1.3.4\classmate-1.3.4.jar;D:\maven-respo\org\springframework\spring-web\5.0.7.RELEASE\spring-web-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\spring-beans\5.0.7.RELEASE\spring-beans-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\spring-webmvc\5.0.7.RELEASE\spring-webmvc-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\spring-aop\5.0.7.RELEASE\spring-aop-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\spring-expression\5.0.7.RELEASE\spring-expression-5.0.7.RELEASE.jar;D:\maven-respo\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;D:\maven-respo\org\projectlombok\lombok\1.16.22\lombok-1.16.22.jar;D:\maven-respo\com\baomidou\mybatis-plus-boot-starter\3.0.6\mybatis-plus-boot-starter-3.0.6.jar;D:\maven-respo\com\baomidou\mybatis-plus\3.0.6\mybatis-plus-3.0.6.jar;D:\maven-respo\com\baomidou\mybatis-plus-extension\3.0.6\mybatis-plus-extension-3.0.6.jar;D:\maven-respo\com\baomidou\mybatis-plus-core\3.0.6\mybatis-plus-core-3.0.6.jar;D:\maven-respo\com\baomidou\mybatis-plus-annotation\3.0.6\mybatis-plus-annotation-3.0.6.jar;D:\maven-respo\com\github\jsqlparser\jsqlparser\1.2\jsqlparser-1.2.jar;D:\maven-respo\org\mybatis\mybatis-spring\1.3.2\mybatis-spring-1.3.2.jar;D:\maven-respo\org\mybatis\mybatis\3.4.6\mybatis-3.4.6.jar;D:\maven-respo\com\baomidou\mybatis-plus-generator\3.0.6\mybatis-plus-generator-3.0.6.jar;D:\maven-respo\org\springframework\boot\spring-boot-starter-jdbc\2.0.3.RELEASE\spring-boot-starter-jdbc-2.0.3.RELEASE.jar;D:\maven-respo\com\zaxxer\HikariCP\2.7.9\HikariCP-2.7.9.jar;D:\maven-respo\org\springframework\spring-jdbc\5.0.7.RELEASE\spring-jdbc-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\spring-tx\5.0.7.RELEASE\spring-tx-5.0.7.RELEASE.jar;D:\maven-respo\org\springframework\boot\spring-boot-configuration-processor\2.0.3.RELEASE\spring-boot-configuration-processor-2.0.3.RELEASE.jar;D:\maven-respo\mysql\mysql-connector-java\5.1.38\mysql-connector-java-5.1.38.jar;D:\maven-respo\org\springframework\boot\spring-boot-devtools\2.0.3.RELEASE\spring-boot-devtools-2.0.3.RELEASE.jar;D:\idea-2017-new\IntelliJ IDEA 2017.2.2\lib\idea_rt.jar
Java lib路径：D:\soft-install\jdk8\bin;C:\Windows\Sun\Java\bin;C:\Windows\system32;C:\Windows;C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Program Files\Intel\WiFi\bin\;C:\Program Files\Common Files\Intel\WirelessCommon\;C:\Program Files\Lenovo\Touch Fingerprint Software\;C:\Program Files (x86)\Intel\UCRT\;C:\Program Files\Intel\UCRT\;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;D:\soft-install\Anaconda3;D:\soft-install\Anaconda3\Scripts;D:\soft-install\Anaconda3\Library\bin;D:\anzhuangbao\maven\apache-maven-2.2.1-bin\apache-maven-2.2.1\bin;D:\soft-install\jdk8\bin;D:\soft-install\jdk8\jre\bin;D:\soft-install\Lenovo Fingerprint Reader\;D:\soft-install\Lenovo Fingerprint Reader\x86\;C:\Program Files\MySQL\MySQL Server 5.5\bin;D:\soft-install\MySQL\MySQL Server 5.5\bin;D:\soft-install\git2.16\Git\cmd;D:\soft-install\TortoiseGit\bin;C:\Program Files\Intel\WiFi\bin\;C:\Program Files\Common Files\Intel\WirelessCommon\;.
Java输入输出临时路径：C:\Users\LISHUA~1\AppData\Local\Temp\
Java编译器：null
Java执行路径：D:\soft-install\jdk8\jre\lib\ext;C:\Windows\Sun\Java\lib\ext
操作系统名称：Windows 7
操作系统的架构：amd64
操作系统版本号：6.1
文件分隔符：\
路径分隔符：;
直线分隔符：

操作系统用户名：lishuai29
操作系统用户的主目录：C:\Users\lishuai29
当前程序所在目录：D:\IdeaProjects\lishuai-notes