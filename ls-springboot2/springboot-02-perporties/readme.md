# springboot maven 多环境配置

1.使用Intellij IDEA创建Spring Boot和Maven项目
2.Spring Boot项目下application.yaml(yaml支持中文)或者application.properties(properties不支持中文)
application.yaml

spring:
  profiles:
    active: @profileActive@
application.properties

spring.profiles.active=@profileActive@
3.创建不同环境下的配置文件
application-dev.yml、application-test.yml、application-prod.yml或者application-dev.properties、application-test.properties、application-prod.properties

4.pom.xml文件中配置profiles节点
<profiles>  
    <profile>  
        <id>dev</id>  
        <activation>  
            <activeByDefault>true</activeByDefault>  
        </activation>  
        <properties>  
            <profileActive>dev</profileActive>  
        </properties>  
    </profile>  
    <profile>  
        <id>test</id>  
        <properties>  
            <profileActive>test</profileActive>  
        </properties>  
    </profile>  
    <profile>  
        <id>prod</id>  
        <properties>  
            <profileActive>prod</profileActive>  
        </properties>  
    </profile>  
</profiles>  
5.使用maven命令打包成相应环境的程序包
生产环境
mvn clean package -Pprod -U  
或者
mvn clean package -DprofileActive=prod -U

测试环境
mvn clean package -Ptest -U  
或者
mvn clean package -DprofileActive=test -U

开发环境
mvn clean package -Pdev -U  
# 或者
mvn clean package -DprofileActive=dev -U