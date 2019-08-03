---
title: Tomcat启动过程加载web.xml中的servlet和Spring容器
categories: 
- tomcat
tags:
---

#Tomcat启动过程(以及加载web.xml中的servlet和Spring容器)

在 Servlet API 中有一个 ServletContextListener 接口，它能够监听 ServletContext 对象的生命周期，实际上就是监听 Web 应用的生命周期。

当Servlet 容器启动或终止Web 应用时，会触发ServletContextEvent 事件，该事件由ServletContextListener 来处理。在 ServletContextListener 接口中定义了处理ServletContextEvent事件的两个方法。

1、  contextInitialized(ServletContextEvent sce) ：当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，并且对那些在Web 应用启动时就需要被初始化的Servlet进行初始化。

2、  contextDestroyed(ServletContextEvent sce) ：当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。

 Spring启动的监听器org.springframework.web.context.ContextLoaderListener实现了ServletContextListener

public class ContextLoaderListener extends ContextLoader implements ServletContextListener
这里可以看出ContextLoaderListener 继承contextloader实现servletcontextlistener，采用了类适配的设计模式

contextloader中初始化了一个默认的XmlWebApplicationContext容器，通过ConfigurableWebApplicationContext类的refresh()方法完成具体的初始化过程

ServletContext接口的简述：public interface ServletContext，定义了一系列方法用于与相应的servlet容器通信，比如：获得文件的MIME类型，分派请求，或者是向日志文件写日志等。 每一个web-app只能有一个ServletContext，web-app可以是一个放置有web application 文件的文件夹，也可以是一个.war的文件。ServletContext对象包含在ServletConfig对象之中，ServletConfig对象在servlet初始化时提供servlet对象。

我们创建好的applicationContext容器会放在servletContext中，在web容器中，通过ServletContext为Spring的IOC容器提供宿主环境，对应的建立起一个IOC容器的体系。其中，首先需要建立的是根上下文，这个上下文持有的对象可以有业务对象，数据存取对象，资源，事物管理器等各种中间层对象。在这个上下文的基础上，和web MVC相关还会有一个上下文来保存控制器之类的MVC对象，这样就构成了一个层次化的上下文结构。



Tomcat启动后，web加载顺序（[context-param -> listener -> filter -> servlet]）


 web.xml加载过程（步骤）：
1.启动WEB项目的时候,容器(如:Tomcat)会去读它的配置文件web.xml.读两个节点:

   <listener></listener> 和 <context-param></context-param>

2.紧接着,容器创建一个ServletContext(上下文),这个WEB项目所有部分都将共享这个上下文.

3.容器将<context-param></context-param>转化为键值对,并交给ServletContext.

4.容器创建<listener></listener>中的类实例,即创建监听.

5.在监听中会有contextInitialized(ServletContextEvent args)初始化方法,在这个方法中获得：

ServletContext = ServletContextEvent.getServletContext();
                   context-param的值 = ServletContext.getInitParameter("context-param的键");


web.xml节点加载顺序：

可以肯定的是，节点的加载顺序与它们在 web.xml 文件中的先后顺序无关。即不会因为 filter 写在 listener 的前面而会先加载 filter。最终得出的结论是：listener -> filter -> servlet

        同时还存在着这样一种配置节点：context-param，它用于向 ServletContext 提供键值对，即应用程序上下文信息。我们的 listener, filter 等在初始化时会用到这些上下文中的信息，那么 context-param 配置节是不是应该写在 listener 配置节前呢？实际上 context-param 配置节可写在任意位置，因此真正的加载顺序为：

context-param -> listener -> filter -> servlet

对于某类配置节而言，与它们出现的顺序是有关的。以 filter 为例，web.xml 中当然可以定义多个 filter，与 filter 相关的一个配置节是 filter-mapping，这里一定要注意，对于拥有相同 filter-name 的 filter 和 filter-mapping 配置节而言，filter-mapping 必须出现在 filter 之后，否则当解析到 filter-mapping 时，它所对应的 filter-name 还未定义。web 容器启动时初始化每个 filter 时，是按照 filter 配置节出现的顺序来初始化的，当请求资源匹配多个 filter-mapping 时，filter 拦截资源是按照 filter-mapping 配置节出现的顺序来依次调用 doFilter() 方法的。
servlet 同 filter 类似，此处不再赘述。



web.xml 的加载顺序是：[context-param -> listener -> filter -> servlet -> spring] ，而同类型节点之间的实际程序调用的时候的顺序是根据对应的 mapping 的顺序进行调用的。


1、tomcat启动Spring容器的过程




2、springmvc的核心分发器DispatcherServlet的启动
<servlet>
          <servlet-name>springmvc-front</servlet-name>
          <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
          <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:spring-mvc.xml</param-value>
          </init-param>
          <load-on-startup>1</load-on-startup>
     </servlet>
其本质是一个servlet，原理同tomcat启动servlet的原理一样，一般采用启动时实例化。
