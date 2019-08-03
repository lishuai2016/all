

# jetty和Tomcat运行maven插件
```xml
<!-- jetty插件 -->
<plugin>
<groupId>org.eclipse.jetty</groupId>
<artifactId>jetty-maven-plugin</artifactId>
<!--<version>9.0.0.v20130308</version>-->
<version>9.2.7.v20150116</version>
<configuration>
<scanIntervalSeconds>3</scanIntervalSeconds>
<webApp>
<contextPath>/</contextPath>
</webApp>
<httpConnector>
<port>6661</port>
</httpConnector>
<reload>automatic</reload>
</configuration>
</plugin>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<version>2.18.1</version>
<configuration>
<skipTests>true</skipTests>
<testFailureIgnore>true</testFailureIgnore>
</configuration>
</plugin>



<plugin>
<groupId>org.apache.tomcat.maven</groupId>
<artifactId>tomcat7-maven-plugin</artifactId>
<version>2.2</version>
<configuration>
<url>http://127.0.0.1:8080/manager/text</url>
<server>tomcat</server>
<path>/</path>
<port>6661</port>
</configuration>
</plugin>
```

#  idea引入本地的jar包
<dependency>
<groupId>org.wltea.ik-analyzer</groupId>
<artifactId>ik-analyzer</artifactId>
<version>4.10.2</version>
<scope>system</scope>
<systemPath>${basedir}/lib/ik-analyzer-4.10.2.jar</systemPath>
</dependency>
