---
title: Tomcat结构解析
categories: 
- tomcat
tags:
---


#Tomcat的层次结构
Server--->Service--->[Connector1,ConnectorN,Engine[Host[Context1，ContextN]]]

<div align="center"> <img src="/images/Tomcat结构1.png"/> </div><br>
<div align="center"> <img src="/images/Tomcat结构2.png"/> </div><br>
<div align="center"> <img src="/images/Tomcat结构3.png"/> </div><br>
<div align="center"> <img src="/images/Tomcat结构4.png"/> </div><br>
<div align="center"> <img src="/images/Tomcat结构5.png"/> </div><br>
<div align="center"> <img src="/images/Tomcat结构6.png"/> </div><br>


##1、一个\conf\server.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Server port="8005" shutdown="SHUTDOWN">
  <Listener className="org.apache.catalina.startup.VersionLoggerListener"/>
  <Listener SSLEngine="on" className="org.apache.catalina.core.AprLifecycleListener"/>
  <Listener className="org.apache.catalina.core.JasperListener"/>

  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener"/>
  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener"/>
  <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener"/>
  
  <GlobalNamingResources>    
    <Resource auth="Container" description="User database that can be updated and saved" factory="org.apache.catalina.users.MemoryUserDatabaseFactory" name="UserDatabase" pathname="conf/tomcat-users.xml" type="org.apache.catalina.UserDatabase"/>
  </GlobalNamingResources>
  
  <Service name="Catalina">
    <!--The connectors can use a shared executor, you can define one or more named thread pools-->
    <!--
    <Executor name="tomcatThreadPool" namePrefix="catalina-exec-"
        maxThreads="150" minSpareThreads="4"/>
    -->
   
    <Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
    <Connector port="8009" protocol="AJP/1.3" redirectPort="8443"/>
    <Engine defaultHost="localhost" name="Catalina">
      
      <Realm className="org.apache.catalina.realm.LockOutRealm">
        
        <Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"/>
      </Realm>
      <Host appBase="webapps" autoDeploy="true" name="localhost" unpackWARs="true">
        
        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" pattern="%h %l %u %t &quot;%r&quot; %s %b" prefix="localhost_access_log." suffix=".txt"/>
      <Context docBase="didi" path="/didi" reloadable="true" source="org.eclipse.jst.jee.server:didi"/>
      </Host>
    </Engine>
  </Service>
</Server>

```

从上面的配置我们中，位于配置文件顶层的是Server和Service元素，其中Server元素是整个配置文件的根元素，而Service元素则是配置服务器的核心元素。
在Service元素内部，定义了一系列的连接器和内部容器类的组件。

### 1.1、Server
<Server>元素对应的是整个Servlet容器，是整个配置的顶层元素，由org.apache.catalina.Server接口来定义，
默认的实现类是org.apache.catalina.core.StandardServer。该元素可配置的属性主要是port和shutdown，
分别指的是监听shutdown命令的端口和命令。在该元素中可以定义一个或多个<Service>元素，除此以外还可以定义一些全局的资源或监听器。

### 1.2 Service
<Service>元素由org.apache.catalina.Service接口定义，默认的实现类为org.apache.catalina.core.StandardService。
在该元素中可以定义一个<Engine>元素、一个或多个<Connector>元素，这些<Connector>元素共享同一个<Engine>元素来进行请求的处理。

### 1.3、Connector
< Connector >元素由org.apache.catalina.connector.Connector类来定义。< Connector>是接受客户端浏览器请求并向用户最终返回响应结果的组件。
该元素位于< Service>元素中，可以定义多个，在我们的示例中配置了两个，分别接受AJP请求和HTTP请求，在配置中，需要为其制定服务的协议和端口号。

### 1.4 、Engine
<Engine>元素由org.apache.catalina.Engine元素来定义，默认的实现类是org.apache.catalina.core.StandardEngine。
<Engine>元素会用来处理<Service>中所有<Connector>接收到的请求，在<Engine>中可以定义多个<Host>元素作为虚拟主机。
<Engine>是Tomcat配置中第一个实现org.apache.catalina.Container的接口，因此可以在其中定义一系列的子元素如<Realm>、<Valve>。

### 1.5、Host
<Host>元素由org.apache.catalina.Host接口来定义，默认实现为org.apache.catalina.core.StandardHost，
该元素定义在<Engine>中，可以定义多个。每个<Host>元素定义了一个虚拟主机，它可以包含一个或多个Web应用（通过<Context>元素来进行定义）。
因为<Host>也是容器类元素，所以可以在其中定义子元素如<Realm>、<Valve>。

### 1.6、Context
 <Context>元素由org.apache.catalina.Context接口来定义，默认实现类为org.apache.catalina.core.StandardContext。
 该元素也许是大家用的最多的元素，在其中定义的是Web应用。一个<Host>中可以定义多个<Context>元素，分别对应不同的Web应用。
 该元素的属性，大家经常会用到如path、reloadable等，可以在<Context>中定义子元素如<Realm>、<Valve>。



## 2、Tomcat的核心组件就两个Connector和Container

从顶层开始：
- Server是Tomcat的最顶层元素，是service的集合，即可包含多个service，Server控制整个Tomcat的生命周期。
- Service由一个Container和多个Connector组成（或者说由Connector，Engine和线程池[可选]组成），形成一个独立完整的处理单元，对外提供服务。
一般情况下我们并不需要配置多个Service,conf/server.xml默认配置了一个“Catalina”的<Service>。
Tomcat将Engine，Host，Context，Wrapper统一抽象成Container。
Connector接受到请求后，会将请求交给Container，Container处理完了之后将结果返回给Connector
Connector 组件是 Tomcat 中两个核心组件之一，它的主要任务是负责接收浏览器的发过来的 tcp 连接请求，
创建一个 Request 和 Response 对象分别用于和请求端交换数据，然后会产生一个线程来处理这个请求并把产生的 Request 和 Response 
对象传给处理这个请求的线程，处理这个请求的线程就是 Container 组件要做的事了。
- Engine：没有父容器，一个 Engine代表一个完整的 Servlet 引擎，它接收来自Connector的请求，并决定传给哪个Host来处理，
Host处理完请求后，将结果返回给Engine，Engine再将结果返回给Connector。
- Host：Engine可以包含多个Host，每个Host代表一个虚拟主机，这个虚拟主机的作用就是运行多个应用，它负责安装和展开这些应用，
并且标识这个应用以便能够区分它们，每个虚拟主机对应的一个域名，不同Host容器接受处理对应不同域名的请求。
- Context：Host可以包含多个Context，Context是Servlet规范的实现，它提供了Servlet的基本环境，一个Context代表一个运行在Host上的Web应用
- Wrapper: Context可以包含多个Wrapper, Wrapper 代表一个 Servlet，它负责管理一个 Servlet，包括的 Servlet 的装载、初始化、执行以及资源回收。
Wrapper 是最底层的容器，它没有子容器了，所以调用它的 addChild 将会报错。

## 3、Tomcat Server处理一个http请求的过程
假设来自客户的请求为：
http://localhost:8080/wsota/wsota_index.jsp
1) 请求被发送到本机端口8080，被在那里侦听的CoyoteHTTP/1.1 Connector获得
2) Connector把该请求交给它所在的Service的Engine来处理，并等待来自Engine的回应
3) Engine获得请求localhost/wsota/wsota_index.jsp，匹配它所拥有的所有虚拟主机Host
4) Engine匹配到名为localhost的Host（即使匹配不到也把请求交给该Host处理，因为该Host被定义为该Engine的默认主机）
5) localhost Host获得请求/wsota/wsota_index.jsp，匹配它所拥有的所有Context
6) Host匹配到路径为/wsota的Context（如果匹配不到就把该请求交给路径名为""的Context去处理）
7) path="/wsota"的Context获得请求/wsota_index.jsp，在它的mapping table中寻找对应的servlet
8) Context匹配到URLPATTERN为*.jsp的servlet，对应于JspServlet类
9) 构造HttpServletRequest对象和HttpServletResponse对象，作为参数调用JspServlet的doGet或doPost方法
10)Context把执行完了之后的HttpServletResponse对象返回给Host
11)Host把HttpServletResponse对象返回给Engine
12)Engine把HttpServletResponse对象返回给Connector
13)Connector把HttpServletResponse对象返回给客户browser

Context的部署配置文件web.xml的说明
一个Context对应于一个Web App，每个Web App是由一个或者多个servlet组成的
当一个Web App被初始化的时候，它将用自己的ClassLoader对象载入“部署配置文件web.xml”中定义的每个servlet类
它首先载入在$CATALINA_HOME/conf/web.xml中部署的servlet类
然后载入在自己的Web App根目录下的WEB-INF/web.xml中部署的servlet类
web.xml文件有两部分：servlet类定义和servlet映射定义
每个被载入的servlet类都有一个名字，且被填入该Context的映射表(mappingtable)中，和某种URLPATTERN对应
当该Context获得请求时，将查询mappingtable，找到被请求的servlet，并执行以获得请求回应
分析一下所有的Context共享的web.xml文件，在其中定义的servlet被所有的Web App载入




## 4、Tomcat的类加载机制
在Tomcat的代码实现中，为了优化内存空间以及不同应用间的类隔离，Tomcat通过内置的一些类加载器来完成了这些功能。
在Java语言中，ClassLoader是以父子关系存在的，Java本身也有一定的类加载规范。在Tomcat中基本的ClassLoader层级关系如下图所示：
<div align="center"> <img src="/images/Tomcat结构7类加载.png"/> </div><br>

Tomcat启动的时候，会初始化图示所示的类加载器。而上面的三个类加载器：
CommonClassLoader、CatalinaClassLoader和SharedClassLoader是与具体部署的Web应用无关的，
而WebappClassLoader则对应Web应用，每个Web应用都会有独立的类加载器，从而实现类的隔离。

在每个Web应用初始化的时候，StandardContext对象代表每个Web应用，它会使用WebappLoader类来加载Web应用，
而WebappLoader中会初始化org.apache.catalina.loader.WebappClassLoader来为每个Web应用创建单独的类加载器，
当处理请求时，容器会根据请求的地址解析出由哪个Web应用来进行对应的处理，进而将当前线程的类加载器设置为请求Web应用的类加载器。


standEngine, StandHost, StandContext及StandWrapper是容器，他们之间有互相的包含关系。
例如，StandEngine是StandHost的父容器，StandHost是StandEngine的子容器。在StandService内还包含一个Executor及Connector。
1） Executor是线程池，它的具体实现是java的concurrent包实现的executor，这个不是必须的，如果没有配置，则使用自写的worker thread线程池
2） Connector是网络socket相关接口模块，它包含两个对象，ProtocolHandler及Adapter
- ProtocolHandler是接收socket请求，并将其解析成HTTP请求对象，可以配置成nio模式或者传统io模式
- Adapter是处理HTTP请求对象，它就是从StandEngine的valve一直调用到StandWrapper的valve

详细情况如图所示：
 <div align="center"> <img src="/images/Tomcat结构8URL.jpg"/> </div><br>

-  Wrapper封装了具体的访问资源，例如 index.html
- Context 封装了各个wrapper资源的集合，例如 app
- Host 封装了各个context资源的集合，例如 www.mydomain.com


按照领域模型，这个典型的URL访问，可以解析出三层领域对象，他们之间互有隶属关系。这是最基本的建模。
从上面的分析可以看出，从wrapper到host是层层递进，层层组合。那么host 资源的集合是什么呢，就是上面所说的engine。 
如果说以上的三个容器可以看成是物理模型的封装，那么engine可以看成是一种逻辑的封装。 
好了，有了这一整套engine的支持，我们已经可以完成从engine到host到context再到某个特定wrapper的定位，
然后进行业务逻辑的处理了(关于怎么处理业务逻辑，会在之后的blog中讲述)。

就好比，一个酒店已经完成了各个客房等硬件设施的建设与装修，接下来就是前台接待工作了。 
先说线程池，这是典型的线程池的应用。首先从线程池中取出一个可用线程（如果有的话），来处理请求，这个组件就是connector。
它就像酒店的前台服务员登记客人信息办理入住一样，主要完成了HTTP消息的解析，根据tomcat内部的mapping规则，
完成从engine到host到context再到某个特定wrapper的定位，进行业务处理，然后将返回结果返回。
之后，此次处理结束，线程重新回到线程池中，为下一次请求提供服务。 
如果线程池中没有空闲线程可用，则请求被阻塞，一直等待有空闲线程进行处理，直至阻塞超时。
线程池的实现有executor及worker thread两种。缺省的是worker thread 模式。 
至此，可以说一个酒店有了前台接待，有了房间等硬件设施，就可以开始正式运营了。
那么把engine，处理线程池，connector封装在一起，形成了一个完整独立的处理单元，这就是service，就好比某个独立的酒店。 
通常，我们经常看见某某集团旗下酒店。也就是说，每个品牌有多个酒店同时运营。就好比tomcat中有多个service在独自运行。
那么这多个service的集合就是server，就好比是酒店所属的集团。 


作用域 
那为什么要按层次分别封装一个对象呢？这主要是为了方便统一管理。类似命名空间的概念，在不同层次的配置，其作用域不一样。
以tomcat自带的打印request与response消息的RequestDumperValve为例。这个valve的类路径是： 
org.apache.catalina.valves.RequestDumperValve

valve机制是tomcat非常重要的处理逻辑的机制，会在相关文档里专门描述。 如果这个valve配置在server.xml的节点下，
则其只打印出访问这个app(my)的request与response消息。 
```xml
<Host name="localhost" appBase="webapps"  
          unpackWARs="true" autoDeploy="true"  
          xmlValidation="false" xmlNamespaceAware="false">  
             <Context path="/my" docBase=" /usr/local/tomcat/backup/my" >  
                    <Valve className="org.apache.catalina.valves.RequestDumperValve"/>  
              </Context>  
              <Context path="/my2" docBase=" /usr/local/tomcat/backup/my" >  
              </Context>  
  </Host>  
```


如果这个valve配置在server.xml的节点下，则其可以打印出访问这个host下两个app的request与response消息。 
```xml
<Host name="localhost" appBase="webapps"  
               unpackWARs="true" autoDeploy="true"  
                xmlValidation="false" xmlNamespaceAware="false">  
                    <Valve className="org.apache.catalina.valves.RequestDumperValve"/>  
                    <Context path="/my" docBase=" /usr/local/tomcat/backup/my" >  
                    </Context>  
                    <Context path="/my2" docBase=" /usr/local/tomcat/backup/my" >   
                    </Context>  
 </Host>  
```

在这里贴一个缺省的server.xml的配置，通过这些配置可以加深对tomcat核心架构分层模块的理解， 
```xml
 <Server port="8005" shutdown="SHUTDOWN">  
          <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />  
          <Listener className="org.apache.catalina.core.JasperListener" />   
          <Listener className="org.apache.catalina.mbeans.ServerLifecycleListener" />  
          <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />  
          <GlobalNamingResources>  
               <Resource name="UserDatabase" auth="Container"  
                       type="org.apache.catalina.UserDatabase"  
                      description="User database that can be updated and saved"  
                      factory="org.apache.catalina.users.MemoryUserDatabaseFactory"  
                      pathname="conf/tomcat-users.xml" />   
           </GlobalNamingResources>  
           <Service name="Catalina">  
                <Executor name="tomcatThreadPool" namePrefix="catalina-exec-"   
                      maxThreads="150" minSpareThreads="4"/>  
                <Connector port="80" protocol="HTTP/1.1"   
                      connectionTimeout="20000"   
                      redirectPort="7443" />  
                <Connector port="7009" protocol="AJP/1.3" redirectPort="7443" />  
                <Engine name="Catalina" defaultHost="localhost">  
                     <Realm className="org.apache.catalina.realm.UserDatabaseRealm"  
                            resourceName="UserDatabase"/>  
                     <Host name="localhost" appBase="webapps"  
                            unpackWARs="true" autoDeploy="true"  
                            xmlValidation="false" xmlNamespaceAware="false">  
                            <Context path="/my" docBase="/usr/local/tomcat/backup/my" >  
                            </Context>   
                     </Host>   
                 </Engine>  
             </Service>  
</Server>  
```


## 5、Tomcat总体架构

-  面向组件架构
-  基于JMX
-  事件侦听

1）面向组件架构
tomcat代码看似很庞大，但从结构上看却很清晰和简单，它主要由一堆组件组成，如Server、Service、Connector等，并基于JMX管理这些组件，
另外实现以上接口的组件也实现了代表生存期的接口Lifecycle，使其组件履行固定的生存期，在其整个生存期的过程中通过事件侦听LifecycleEvent实现扩展。
Tomcat的核心类图如下所示：
<div align="center"> <img src="/images/Tomcat结构9.jpg"/> </div><br>

Catalina：与开始/关闭shell脚本交互的主类，因此如果要研究启动和关闭的过程，就从这个类开始看起。
Server：是整个Tomcat组件的容器，包含一个或多个Service。
Service：Service是包含Connector和Container的集合，Service用适当的Connector接收用户的请求，再发给相应的Container来处理。
Connector：实现某一协议的连接器，如默认的有实现HTTP、HTTPS、AJP协议的。
Container：可以理解为处理某类型请求的容器，处理的方式一般为把处理请求的处理器包装为Valve对象，并按一定顺序放入类型为Pipeline的管道里。Container有多种子类型：Engine、Host、Context和Wrapper，这几种子类型Container依次包含，处理不同粒度的请求。另外Container里包含一些基础服务，如Loader、Manager和Realm。
Engine：Engine包含Host和Context，接到请求后仍给相应的Host在相应的Context里处理。
Host：就是我们所理解的虚拟主机。
Context：就是我们所部属的具体Web应用的上下文，每个请求都在是相应的上下文里处理的。
Wrapper：Wrapper是针对每个Servlet的Container，每个Servlet都有相应的Wrapper来管理。
可以看出Server、Service、Connector、Container、Engine、Host、Context和Wrapper这些核心组件的作用范围是逐层递减，并逐层包含。
下面就是些被Container所用的基础组件：
Loader：是被Container用来载入各种所需的Class。
Manager：是被Container用来管理Session池。
Realm：是用来处理安全里授权与认证。
分析完核心类后，再看看Tomcat启动的过程，Tomcat启动的时序图如下所示：
<div align="center"> <img src="/images/Tomcat结构10.jpg"/> </div><br>

从上图可以看出，Tomcat启动分为init和start两个过程，核心组件都实现了Lifecycle接口，都需实现start方法，
因此在start过程中就是从Server开始逐层调用子组件的start过程。[模板方法？？？]

2）基于JMX
Tomcat会为每个组件进行注册过程，通过Registry管理起来，而Registry是基于JMX来实现的，
因此在看组件的init和start过程实际上就是初始化MBean和触发MBean的start方法，会大量看到形如：
Registry.getRegistry(null, null).invoke(mbeans, "init", false);
Registry.getRegistry(null, null).invoke(mbeans, "start", false);
这样的代码，这实际上就是通过JMX管理各种组件的行为和生命期。

备注：
JMX（Java Management Extensions，即Java管理扩展）是一个为应用程序、设备、系统等植入管理功能的框架。JMX可以跨越一系列异构操作系统平台、
系统体系结构和网络传输协议，灵活的开发无缝集成的系统、网络和服务管理应用。

3）事件侦听
各个组件在其生命期中会有各种各样行为，而这些行为都有触发相应的事件，Tomcat就是通过侦听这些时间达到对这些行为进行扩展的目的。
在看组件的init和start过程中会看到大量如：
lifecycle.fireLifecycleEvent(AFTER_START_EVENT, null);这样的代码，这就是对某一类型事件的触发，如果你想在其中加入自己的行为，
就只用注册相应类型的事件即可。


protocol
我们发现这个类里面有很多与protocol有关的属性和方法，tomcat中支持两种协议的连接器：HTTP/1.1与AJP/1.3，
查看tomcat的配置文件server.xml可以看到如下配置：

- <Connector port="8080" protocol="HTTP/1.1"
-                connectionTimeout="20000"
-                redirectPort="8443" URIEncoding="utf-8"/>
-
- <Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />

HTTP/1.1协议负责建立HTTP连接，web应用通过浏览器访问tomcat服务器用的就是这个连接器，默认监听的是8080端口；
AJP/1.3协议负责和其他HTTP服务器建立连接，监听的是8009端口，比如tomcat和apache或者iis集成时需要用到这个连接器。
协议上有三种不同的实现方式：JIO、APR、NIO。
JIO(Java.io)：用java.io纯JAVA编写的TCP模块，这是tomcat默认连接器实现方法；
APR(Apache Portable Runtime)：有C语言和JAVA两种语言实现，连接Apache httpd Web服务器的类库是在C中实现的，同时用APR进行网络通信；
NIO(java.nio)：这是用纯Java编写的连接器(Conector)的一种可选方法。该实现用java.nio核心Java网络类以提供非阻塞的TCP包特性。
