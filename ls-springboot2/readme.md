#整合springboot的各个模块

## 前言
基于springboot2.0.4.RELEASE



[参考](http://blog.didispace.com/Spring-Boot%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)
https://github.com/dyc87112/SpringBoot-Learning

https://github.com/lishuai2016/spring-boot-leaning
https://github.com/lishuai2016/spring-boot-examples
https://github.com/lishuai2016/springboot-learning-example



## 1、springboot-01-quickstart模块
- server.port=80  指定端口
- 一个简单的restful风格的请求,访问：http://localhost/index,页面显示：index
- SpringBoot 使用MockMvc进行Controller的测试

ApplicationUserTests.getHello单元测试的结果：

    MockHttpServletRequest:
          HTTP Method = GET
          Request URI = /
           Parameters = {name=[lishuai]}
              Headers = {Accept=[text/html]}
                 Body = <no character encoding set>
        Session Attrs = {}
    
    Handler:
                 Type = com.ls.controller.HelloController
               Method = public java.lang.String com.ls.controller.HelloController.hello(java.lang.String)
    
    Async:
        Async started = false
         Async result = null
    
    Resolved Exception:
                 Type = null
    
    ModelAndView:
            View name = null
                 View = null
                Model = null
    
    FlashMap:
           Attributes = null
    
    MockHttpServletResponse:
               Status = 200
        Error message = null
              Headers = {Content-Type=[text/html;charset=UTF-8], Content-Length=[13]}
         Content type = text/html;charset=UTF-8
                 Body = hello lishuai
        Forwarded URL = null
       Redirected URL = null
              Cookies = []



## 2、springboot-02-perporties
自定义属性、随机数、多环境配置等
- 多环境配置文件激活属性 
- 自定义类读取perporties文件中的变量信息
- 中文乱码解决：
1、添加配置
banner.charset=UTF-8
server.tomcat.uri-encoding=UTF-8
spring.http.encoding.charset=UTF-8
spring.http.encoding.enabled=true
spring.http.encoding.force=true
spring.messages.encoding=UTF-8
2、设置文件类型
将application.properites的文件类型修改为UTF-8的编码类型。
通过以上方法测试获取出来的值还是乱码。
设置 File Encodings的Transparent native-to-ascii conversion为true，具体步骤如下：依次点击
File -> Settings -> Editor -> File Encodings
将Properties Files (*.properties)下的Default encoding for properties files设置为UTF-8，
将Transparent native-to-ascii conversion前的勾选上。

## 3、springboot-03-thymeleaf
- 静态资源访问
在我们开发Web应用的时候，需要引用大量的js、css、图片等静态资源。

- 默认配置
Spring Boot默认提供静态资源目录位置需置于classpath下，目录名需符合如下规则：
/static
/public
/resources
/META-INF/resources
举例：我们可以在src/main/resources/目录下创建static，在该位置放置一个图片文件。
启动程序后，尝试访问http://localhost:8080/D.jpg。如能显示图片，配置成功。

- 页面模板引擎的默认页面存放路径 src/main/resources/templates。当然也可以修改这个路径，具体如何修改，可在后续各模板引擎的配置属性中查询并修改。

注意：
在引入spring-boot-starter-thymeleaf之前需要引入一个额外的包，否则引入一直报错

    <!--避坑包  否则下面的包加载不了 -->
        <dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
            <version>1.9.22</version>
        </dependency>

## 4、springboot-04-swagger2
页面测试访问的路径：项目路径/swagger-ui.html 
如：http://localhost:8080/swagger-ui.html

备注：当启动后发现页面并没有要测试的controller，检查一下，swagger控制显示的包范围

## 5、springboot-05-global-exception
统一异常处理
- 使用thymeleaf页面引擎模板渲染页面
- 通过注解@ControllerAdvice来实现

## 6、springboot-06-JdbcTemplate

- 引入spring-boot-starter-jdbc在service层直接注入JdbcTemplate，然后再JdbcTemplate中直接灌入SQL即可
- springboot的默认数据库连接池为：spring.datasource.type=com.zaxxer.hikari.HikariDataSource
- druid数据库连接池的切换，spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
在pom文件中引入druid-spring-boot-starter，在不配数据库连接池类型spring.datasource.type的情况下，
springboot也会使用druid来作为数据库连接池，这是springboot引入包后自动配置的结果。

备注：在springboot中引入一个包后，会自动加载一些配置项


todo  研究下JdbcTemplate的源码

## 7、springboot-07-jpa
通过JpaRepository接口的继承封装可以简化数据库的访问


## 8、springboot-08-JdbcTemplate-multiDatasource
JdbcTemplate多数据源配置

问题：
1、报错jdbcUrl is required with driverClassName
说明URL设置有问题
springboot1和springboot2在URL的设置上有点不一样，
springboot1的格式是：spring.datasource.secondary.url
springboot2的格式是：spring.datasource.secondary.jdbc-url
并且还有一点需要注意，在一个数据源的配置时，两种方式都可以，但是在配置多个数据源的时候就必须使用jdbc-url格式

todo   研究一下两种方式为什么不一样？？？


## 9、springboot-09-jpa-multiDatasource
jpa多数据源设置

问题：
Table 'XXX.hibernate_sequence' doesn't exist报错
解决：在实体类的id生成策略改成@GeneratedValue(strategy = GenerationType.IDENTITY).

多数据源问题总结：
在单数据源是，不需要考虑DataSource和JdbcTemplate、jpa如何结合的，直接使用JdbcTemplate和JpaRepository来操作数据库，
而在多数据源的时候，需要自己编写DataSource解析类来解析在配置文件中设置的各个数据源，然后将
各个DataSource数据源关联到对应的JdbcTemplate和JpaRepository上。

## 10、springboot-10-mybatis
整合mybatis：SQL直接写在mapper接口上

- 需要注意的是数据库的URL是这样的格式spring.datasource.url，而不是jdbc-url？？？，否则报错
- 直接在mapper接口上使用@Mapper注解即可


## 11、springboot-11-mybatis-multiDatasources
整合mybatis多数据源,并且SQL写在xml文件中


## 12、springboot-12-redis
整合Redis

spring-boot-starter-redis在springboot 1.4.7版本后,改为了spring-boot-starter-data-redis
并且需要commons-pool2连接池。
1.5的版本默认采用的连接池技术是jedis，2.0以上版本默认连接池是lettuce, 因为此次是采用jedis，所以需要排除lettuce的jar

## 13、springboot-13-MongoDB
整合MongoDB




https://blog.csdn.net/CSUstudent007/article/details/82913294
https://blog.csdn.net/weixin_42033269/article/details/80048073
https://blog.csdn.net/u012935820/article/details/80900860

## 14、springboot-14-log4j
整合log4j
由于Spring Boot 2.0 默认情况下是使用logback作为日志系统的


### 1、默认的日志格式（格式化日志）
2018-12-16 19:22:06.377  INFO 13104 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 80 (http) with context path ''
2018-12-16 19:22:06.381  INFO 13104 --- [           main] com.ls.Application                       : Started Application in 5.603 seconds (JVM running for 7.928)

输出内容元素具体如下：
时间日期 — 精确到毫秒
日志级别 — ERROR, WARN, INFO, DEBUG or TRACE
进程ID
分隔符 — --- 标识实际日志的开始
线程名 — 方括号括起来（可能会截断控制台输出）
Logger名 — 通常使用源代码的类名
日志内容

### 2、控制台输出
在Spring Boot中默认配置了ERROR、WARN和INFO级别的日志输出到控制台。
我们可以通过两种方式切换至DEBUG级别：
在运行命令后加入--debug标志，如：$ java -jar myapp.jar --debug
在application.properties中配置debug=true，该属性置为true的时候，核心Logger（包含嵌入式容器、hibernate、spring）会输出更多内容，
但是你自己应用的日志并不会输出为DEBUG级别。

### 3、多彩输出
spring.output.ansi.enabled=DETECT
如果你的终端支持ANSI，设置彩色输出会让日志更具可读性。通过在application.properties中设置spring.output.ansi.enabled参数来支持。
NEVER：禁用ANSI-colored输出（默认项）
DETECT：会检查终端是否支持ANSI，是的话就采用彩色输出（推荐项）
ALWAYS：总是使用ANSI-colored格式输出，若终端不支持的时候，会有很多干扰信息，不推荐使用

### 4、文件输出
Spring Boot默认配置只会输出到控制台，并不会记录到文件中，但是我们通常生产环境使用时都需要以文件方式记录。

若要增加文件输出，需要在application.properties中配置logging.file或logging.path属性。

logging.file，设置文件，可以是绝对路径，也可以是相对路径。如：logging.file=my.log [这种方式可行]
logging.path，设置目录，会在该目录下创建spring.log文件，并写入日志内容，如：logging.path=/var/log[这种方式不可行]
日志文件会在10Mb大小的时候被截断，产生新的日志文件，默认级别为：ERROR、WARN、INFO


问题：
springboot2.0.4的spring-boot-starter-log4j2加载不了？   
        
        <!-- log4j2 默认的版本号加载不了 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>

###5、级别控制
在Spring Boot中只需要在application.properties中进行配置完成日志记录的级别控制。

配置格式：logging.level.*=LEVEL

logging.level：日志级别控制前缀，*为包名或Logger名
LEVEL：选项TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF
举例：
logging.level.com.ls=DEBUG：com.ls包下所有class以DEBUG级别输出
logging.level.root=WARN：root日志以WARN级别输出
### 6、自定义日志配置
由于日志服务一般都在ApplicationContext创建前就初始化了，它并不是必须通过Spring的配置文件控制。
因此通过系统属性和传统的Spring Boot外部配置文件依然可以很好的支持日志控制和管理。

根据不同的日志系统，你可以按如下规则组织配置文件名，就能被正确加载：
Logback：logback-spring.xml, logback-spring.groovy, logback.xml, logback.groovy
Log4j：log4j-spring.properties, log4j-spring.xml, log4j.properties, log4j.xml
Log4j2：log4j2-spring.xml, log4j2.xml
JDK (Java Util Logging)：logging.properties
Spring Boot官方推荐优先使用带有-spring的文件名作为你的日志配置（如使用logback-spring.xml，而不是logback.xml）

### 7、自定义输出格式
在Spring Boot中可以通过在application.properties配置如下参数控制输出格式：
logging.pattern.console：定义输出到控制台的样式（不支持JDK Logger）
logging.pattern.file：定义输出到文件的样式（不支持JDK Logger）


默认情况下，SpringBoot只会在控制台输出 INFO及以上级别（WARN、ERROR）的日志。
如果你想输出 DEBUG级别的日志，可以通过以下两种方法：
1、在运行SpringBoot应用 jar包时指定 --debug参数：
java -jar myApp.jar --debug
2、在你的 application.properties中添加 debug=true（好像不好使）


log4j2设置
1、使用xml格式配置日志相关信息
在根目录下创建一个log4j2-spring.xml
2、使用properties配置
需要放在主配置文件中否则不起作用


调整各系统日志级别
所有支持 logging的系统（框架）都可以在application.properties中设置一个不同的日志级别：
logging.level.root=WARN
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR


#OFF、FATAL、ERROR、WARN、INFO、DEBUG、TRACE、ALL
 %p 输出优先级，即 DEBUG ， INFO ， WARN ， ERROR ， FATAL
　　 %r 输出自应用启动到输出该 log 信息耗费的毫秒数
　　 %c 输出所属的类目，通常就是所在类的全名
　　 %t 输出产生该日志事件的线程名
　　 %n 输出一个回车换行符， Windows 平台为 “rn” ， Unix 平台为 “n”
　　 %d 输出日志时间点的日期或时间，默认格式为 ISO8601 ，也可以在其后指定格式，比如： %d{yyy MMM dd HH:mm:ss,SSS} ，输出类似： 2002 年 10 月 18 日 22 ： 10 ： 28 ， 921
　　 %l 输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)




### 在当前项目环境输出日志信息,如下所示，会输出com.ls.data.service包下类的logger信息
log4j.logger.com.ls.data.service=DEBUG   

## 15、springboot-15-kafka
消息队列kafka

## 16、springboot-16-Rabbit


## 17、springboot-17-RocketMQ


## 18、springboot-18-ActiveMQ

## 19、springboot-19-dubbo

## 20、springboot-20-admin
springboot-admin整合

模块：springboot-20-admin-client
模块：springboot-20-admin-server
一个作为服务端，另外一个作为客户端使用起来正常。
这种配置中，Spring Boot Admin 作为 Server，其他 Spring Boot 应用作为 Client，
Client 把自身的信息“注册”到 Server，我们就能在 Server 上看到“注册”的 Spring Boot 应用的状态信息了

原理：我们知道spring-boot-actuator暴露了大量统计和监控信息的端点，spring-boot-admin 就是为此提供的监控项目，
因此在client端需要配置spring-boot-starter-actuator

假如即作为服务端又作为客户端怎么没法使用？？？

问题1：


问题2：
Either health or status endpoint must be enabled!


https://www.jianshu.com/p/2b66433bd373



## 21、springboot-21-quartz

https://yq.aliyun.com/articles/29122

## 22、springboot-22-Security


## 23、springboot-23-Flyway
Flyway是一个简单开源数据库版本控制器（约定大于配置），对数据库表的信息进行控制(类似于git的版本控制功能)


问题：
Found non-empty schema(s) `test` without schema history table! Use baseline() or 
set baselineOnMigrate to true to initialize the schema history table.
解决方案：设置setBaselineOnMigrate为true。
但此时如果有脚本需要启动加载执行的话，脚本的版本号一定要比1大才能执行（注意红色标注），
举个例子V1.0__Base_version.sql不会被执行，但V1.1__Base_version.sql就会被执行。

## 24、springboot-24-cache
spring缓存设置

###1、准备工作
application.properties文件中新增spring.jpa.properties.hibernate.show_sql=true，开启hibernate对sql语句的打
修改单元测试ApplicationTests，初始化插入User表一条用户名为AAA，年龄为10的数据。并通过findByName函数完成两次查询。
```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void before() {
		userRepository.save(new User("AAA", 10));
	}

	@Test
	public void test() throws Exception {
		User u1 = userRepository.findByName("AAA");
		System.out.println("第一次查询：" + u1.getAge());

		User u2 = userRepository.findByName("AAA");
		System.out.println("第二次查询：" + u2.getAge());
	}

}
```

执行单元测试，我们可以在控制台中看到下面内容。
Hibernate: insert into user (age, name) values (?, ?)
Hibernate: select user0_.id as id1_0_, user0_.age as age2_0_, user0_.name as name3_0_ from user user0_ where user0_.name=?
第一次查询：10
Hibernate: select user0_.id as id1_0_, user0_.age as age2_0_, user0_.name as name3_0_ from user user0_ where user0_.name=?
第二次查询：10
在测试用例执行前，插入了一条User记录。然后每次findByName调用时，都执行了一句select语句来查询用户名为AAA的记录。

###2、引入缓存
在pom.xml中引入cache依赖，添加如下内容：
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
在Spring Boot主类中增加@EnableCaching注解开启缓存功能，如下：
```java
@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```
在数据访问接口中，增加缓存配置注解，如：
```java
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable
    User findByName(String name);

}
```
再来执行以下单元测试，可以在控制台中输出了下面的内容：
Hibernate: insert into user (age, name) values (?, ?)
Hibernate: select user0_.id as id1_0_, user0_.age as age2_0_, user0_.name as name3_0_ from user user0_ where user0_.name=?
第一次查询：10
第二次查询：10
到这里，我们可以看到，在调用第二次findByName函数时，没有再执行select语句，也就直接减少了一次数据库的读取操作。

为了可以更好的观察，缓存的存储，我们可以在单元测试中注入cacheManager。

@Autowired
private CacheManager cacheManager;
使用debug模式运行单元测试，观察cacheManager中的缓存集users以及其中的User对象的缓存加深理解。

###3、Cache注解详解
回过头来我们再来看，这里使用到的两个注解分别作了什么事情。

@CacheConfig：主要用于配置该类中会用到的一些共用的缓存配置。在这里@CacheConfig(cacheNames = "users")：配置了该数据访问对象中返回的内容将存储于名为users的缓存对象中，我们也可以不使用该注解，直接通过@Cacheable自己配置缓存集的名字来定义。

@Cacheable：配置了findByName函数的返回值将被加入缓存。同时在查询时，会先从缓存中获取，若不存在才再发起对数据库的访问。该注解主要有下面几个参数：

value、cacheNames：两个等同的参数（cacheNames为Spring 4新增，作为value的别名），用于指定缓存存储的集合名。由于Spring 4中新增了@CacheConfig，因此在Spring 3中原本必须有的value属性，也成为非必需项了
key：缓存对象存储在Map集合中的key值，非必需，缺省按照函数的所有参数组合作为key值，若自己配置需使用SpEL表达式，比如：@Cacheable(key = "#p0")：使用函数第一个参数作为缓存的key值，更多关于SpEL表达式的详细内容可参考官方文档
condition：缓存对象的条件，非必需，也需使用SpEL表达式，只有满足表达式条件的内容才会被缓存，比如：@Cacheable(key = "#p0", condition = "#p0.length() < 3")，表示只有当第一个参数的长度小于3的时候才会被缓存，若做此配置上面的AAA用户就不会被缓存，读者可自行实验尝试。
unless：另外一个缓存条件参数，非必需，需使用SpEL表达式。它不同于condition参数的地方在于它的判断时机，该条件是在函数被调用之后才做判断的，所以它可以通过对result进行判断。
keyGenerator：用于指定key生成器，非必需。若需要指定一个自定义的key生成器，我们需要去实现org.springframework.cache.interceptor.KeyGenerator接口，并使用该参数来指定。需要注意的是：该参数与key是互斥的
cacheManager：用于指定使用哪个缓存管理器，非必需。只有当有多个时才需要使用
cacheResolver：用于指定使用那个缓存解析器，非必需。需通过org.springframework.cache.interceptor.CacheResolver接口来实现自己的缓存解析器，并用该参数指定。
除了这里用到的两个注解之外，还有下面几个核心注解：

@CachePut：配置于函数上，能够根据参数定义条件来进行缓存，它与@Cacheable不同的是，它每次都会真是调用函数，所以主要用于数据新增和修改操作上。它的参数与@Cacheable类似，具体功能可参考上面对@Cacheable参数的解析
@CacheEvict：配置于函数上，通常用在删除方法上，用来从缓存中移除相应数据。除了同@Cacheable一样的参数之外，它还有下面两个参数：
allEntries：非必需，默认为false。当为true时，会移除所有数据
beforeInvocation：非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据。
缓存配置
完成了上面的缓存实验之后，可能大家会问，那我们在Spring Boot中到底使用了什么缓存呢？

###4、在Spring Boot中通过@EnableCaching注解自动化配置合适的缓存管理器（CacheManager），Spring Boot根据下面的顺序去侦测缓存提供者：

Generic
JCache (JSR-107)
EhCache 2.x
Hazelcast
Infinispan
Redis
Guava
Simple
除了按顺序侦测外，我们也可以通过配置属性spring.cache.type来强制指定。
我们可以通过debug调试查看cacheManager对象的实例来判断当前使用了什么缓存。


通过 CacheManager bean = ApplicationContextUtils.applicationContext.getBean(CacheManager.class);来获取具体的实现类是那个
提示：默认的是一个叫concurrentmapcacheManager的实现。如果在pom文件中引入ehcache，则为EhCacheCacheManager。

### 5、EhCache设置

在Spring Boot中开启EhCache非常简单，只需要在工程中加入ehcache.xml配置文件并在pom.xml中增加ehcache依赖，
框架只要发现该文件，就会创建EhCache的缓存管理器。

在src/main/resources目录下创建：ehcache.xml
```xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd">

    <cache name="users"
           maxEntriesLocalHeap="200"
           timeToLiveSeconds="600">
    </cache>

</ehcache>
```
在pom.xml中加入
```xml
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
</dependency>
```
完成上面的配置之后，再通过debug模式运行单元测试，观察此时CacheManager已经是EhCacheManager实例，说明EhCache开启成功了。
对于EhCache的配置文件也可以通过application.properties文件中使用spring.cache.ehcache.config属性来指定，比如：
spring.cache.ehcache.config=classpath:config/another-config.xml


### 5、问题
在单元测试直接引入报错：
//    @Autowired
//    private CacheManager cacheManager;

Unsatisfied dependency expressed through field 'cacheManager';
 nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: 
 No qualifying bean of type 'net.sf.ehcache.management.CacheManager' available: 
 expected at least 1 bean which qualifies as autowire candidate. 
 Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}






## 24-1、springboot-24-cache-Redis
基于Redis的分布式缓存

- 实体对象需要实现序列化接口Serializable，否则不走缓存
- 自己的实体对象，需要自定义序列化格式，然后设置到RedisCacheManager中



问题
org.springframework.data.redis.serializer.SerializationException:
 Cannot serialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException: 
 Failed to serialize object using DefaultSerializer; nested exception is java.lang.IllegalArgumentException: 
 DefaultSerializer requires a Serializable payload but received an object of type [com.ls.domain.User]
 
 DefaultSerializer requires a Serializable payload but received an object of type
解析不了缓存对象？？？



## 25、springboot-25-Scheduled
@Scheduled(fixedRate = 5000)  //每隔5秒执行一次  
注意：需要引入web模块




## 26、springboot-26-Async
-  @Async 异步注解执行任务，每个开启一个线程（默认的线程池SimpleAsyncTaskExecutor）
- @Async("taskExecutor") 通过引入自定义线程池来执行任务
- 引入lombok通过@Slf4j注解来实现日志功能，但是会通不过idea的检查，可以忽略



## 27、springboot-27-aop
### 1、请求参数问题
http://localhost:8080/hello?name=lishuai   注意下面定义的请求，因为使用@RequestParam String name，请求URL必须带参数，否则400错误

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    
### 2、实现AOP的切面主要有以下几个要素：
  使用@Aspect注解将一个java类定义为切面类
  使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
  根据需要在切入点不同位置的切入内容
  使用@Before在切入点开始处切入内容
  使用@After在切入点结尾处切入内容
  使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
  使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
  使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
  
### 3、AOP切面的优先级(多个切面各个切入点的执行顺序和拦截器各个方法的方式一样)
由于通过AOP实现，程序得到了很好的解耦，但是也会带来一些问题，比如：我们可能会对Web层做多个切面，校验用户，校验头信息等等，
这个时候经常会碰到切面的处理顺序问题。

所以，我们需要定义每个切面的优先级，我们需要@Order(i)注解来标识切面的优先级。
i的值越小，优先级越高。假设我们还有一个切面是CheckNameAspect用来校验name必须为ls，
我们为其设置@Order(10)，而上文中WebLogAspect设置为@Order(5)，所以WebLogAspect有更高的优先级，这个时候执行顺序是这样的：
在@Before中优先执行@Order(5)的内容，再执行@Order(10)的内容
在@After和@AfterReturning中优先执行@Order(10)的内容，再执行@Order(5)的内容
所以我们可以这样子总结：
在切入点前的操作，按order的值由小到大执行
在切入点后的操作，按order的值由大到小执行


## 28、springboot-28-email
模板邮件


## 29、springboot-29-elasticsearch
spring-boot-starter-data-elasticsearch  maven依赖不了？？？

https://www.elastic.co/cn/downloads/elasticsearch

https://www.cnblogs.com/xuwujing/p/8998168.html


https://github.com/lishuai2016/elasticsearch


## 30、springboot-30-websocket


## 31、springboot-31-webflux
SpringWebflux是SpringFramework5.0添加的新功能，WebFlux本身追随当下最火的Reactive Programming而诞生的框架
我们知道传统的Web框架，比如说：struts2，springmvc等都是基于Servlet API与Servlet容器基础之上运行的，
在Servlet3.1之后才有了异步非阻塞的支持。而WebFlux是一个典型非阻塞异步的框架，它的核心是基于Reactor的相关API实现的。
相对于传统的web框架来说，它可以运行在诸如Netty，Undertow及支持Servlet3.1的容器上，
因此它的运行环境的可选择行要比传统web框架多的多。

根据官方的说法，webflux主要在如下两方面体现出独有的优势：
1）非阻塞式
其实在servlet3.1提供了非阻塞的API，WebFlux提供了一种比其更完美的解决方案。
使用非阻塞的方式可以利用较小的线程或硬件资源来处理并发进而提高其可伸缩性

2) 函数式编程端点
老生常谈的编程方式了，Spring5必须让你使用java8，那么函数式编程就是java8重要的特点之一，
而WebFlux支持函数式编程来定义路由端点处理请求。


## 32、springboot-32-jvm
jvm性能测试调优

## 33、springboot-33-JMH
微基准测试


## 34、springboot-34-markdown
把Markdown页面预览

## 35、springboot-35-nutz
借助于nutz框架实现数据库表结构的文档化









