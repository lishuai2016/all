[maven核心，pom.xml详解](https://blog.csdn.net/zhuxinhua/article/details/5788546)
[Maven：mirror和repository 区别](https://my.oschina.net/sunchp/blog/100634?nocache=1497320638051)
[Maven 集成Tomcat7插件](https://blog.csdn.net/binyao02123202/article/details/17793233)
[常用Maven插件介绍](http://www.cnblogs.com/crazy-fox/archive/2012/02/09/2343722.html)
[Maven之（六）setting.xml配置文件详解](https://blog.csdn.net/u012152619/article/details/51485152)


# 把自己本地jar包引入maven项目

```xml
<dependency>
              <groupId>bccs</groupId>
              <artifactId>bccs-api</artifactId>
              <version>3.0.1</version>
              <scope>system</scope>
               <systemPath>C:\Users\djn8\.m2\local-jars\bccs-api-3.0.1.jar</systemPath>
          </dependency>
          <dependency>
              <groupId>com.cloopen</groupId>
               <artifactId>cloonpen-httpclient</artifactId>
              <version>0.0.0</version>
              <type>jar</type>
              <scope>system</scope>
               <systemPath>C:\Users\djn8\.m2\local-jars\httpclient.jar</systemPath>
          </dependency>
```


# 国内的maven镜像仓库（可以使用的）

```xml
<mirror>
    <id>nexus-aliyun</id>
    <name>Nexus aliyun</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
</mirror>


     <!-- spring的libs-release镜像，存放spring项目及其子项目的jar包，以及相关的依赖jar -->
     <mirror>
         <id>libs-release</id>
         <mirrorOf>repo1</mirrorOf>
         <url>https://repo.spring.io/libs-release</url>
     </mirror>
     <!-- spring的milestone镜像，存放着spring项目及其子项目的里程碑版本jar包 -->
     <mirror>
         <id>milestone</id>
         <mirrorOf>repo2</mirrorOf>
         <url>https://repo.spring.io/milestone</url>
     </mirror>
     <!-- spring的snapshot镜像，存放着spring项目及其子项目的预览版本jar包 -->
     <mirror>
         <id>snapshot</id>
       <mirrorOf>repo3</mirrorOf>
        <url>https://repo.spring.io/snapshot</url>
    </mirror>
    <!-- mvnrepository镜像，常用的maven中央仓库jar查询站点，可直接当maven镜像使用 -->
     <mirror>
         <id>mvn</id>
         <mirrorOf>mvnrepository</mirrorOf>
         <url>http://mvnrepository.com/</url>
     </mirror>
```
