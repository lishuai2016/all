---
title: Filter
categories: 
- spring
tags:
---

# 1、filter过滤器

过滤器是处于客户端与服务器资源文件之间的一道过滤网，在访问资源文件之前，通过一系列的过滤器对请求进行修改、判断等，
把不符合规则的请求在中途拦截或修改。也可以对响应进行过滤，拦截或修改响应。

Servlet API中提供了一个Filter接口，开发web应用时，如果编写的Java类实现了这个接口，则把这个java类称之为过滤器Filter。
通过Filter技术，开发人员可以实现用户在访问某个目标资源之前，对访问的请求和响应进行拦截.

```java
public interface Filter {
    void init(FilterConfig var1) throws ServletException;

    void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;

    void destroy();
}
```


```java
public interface FilterChain {
    void doFilter(ServletRequest var1, ServletResponse var2) throws IOException, ServletException;
}
```
　　

# 2、Filter是如何实现拦截的
　　
Filter接口中有一个doFilter方法，当我们编写好Filter，并配置对哪个web资源进行拦截后，WEB服务器每次在调用web资源的service方法之前，
都会先调用一下filter的doFilter方法，因此，在该方法内编写代码可达到如下目的：

- 调用目标资源之前，让一段代码执行。
- 是否调用目标资源（即是否让用户访问web资源）。
- 调用目标资源之后，让一段代码执行。

web服务器在调用doFilter方法时，会传递一个filterChain对象进来，filterChain对象是filter接口中最重要的一个对象，
它也提供了一个doFilter方法，开发人员可以根据需求决定是否调用此方法，调用该方法，则web服务器就会调用web资源的service方法，
即web资源就会被访问，否则web资源不会被访问。

```java
public class FilterDemo01 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----过滤器初始化----");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        //对request和response进行一些预处理
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        System.out.println("FilterDemo01执行前！！！");
        chain.doFilter(request, response);  //让目标资源执行，放行
        System.out.println("FilterDemo01执行后！！！");
    }

    @Override
    public void destroy() {
        System.out.println("----过滤器销毁----");
    }
}
```


# 3、Filter链
在一个web应用中，可以开发编写多个Filter，这些Filter组合起来称之为一个Filter链。
web服务器根据Filter在web.xml文件中的注册顺序，决定先调用哪个Filter，当第一个Filter的doFilter方法被调用时，
web服务器会创建一个代表Filter链的FilterChain对象传递给该方法。在doFilter方法中，开发人员如果调用了FilterChain对象的doFilter方法，
则web服务器会检查FilterChain对象中是否还有filter，如果有，则调用第2个filter，如果没有，则调用目标资源。


# 4、Filter的生命周期
## 4.1、Filter的创建
Filter的创建和销毁由WEB服务器负责。 web 应用程序启动时，web 服务器将创建Filter 的实例对象，并调用其init方法，完成对象的初始化功能，
从而为后续的用户请求作好拦截的准备工作，filter对象只会创建一次，init方法也只会执行一次。
通过init方法的参数，可获得代表当前filter配置信息的FilterConfig对象。

## 4.2、Filter的销毁
Web容器调用destroy方法销毁Filter。destroy方法在Filter的生命周期中仅执行一次。在destroy方法中，可以释放过滤器使用的资源。

## 4.3、FilterConfig接口
用户在配置filter时，可以使用<init-param>为filter配置一些初始化参数，当web容器实例化Filter对象，调用其init方法时，
会把封装了filter初始化参数的filterConfig对象传递进来。因此开发人员在编写filter时，通过filterConfig对象的方法，就可获得：
　　String getFilterName()：得到filter的名称。
　　String getInitParameter(String name)： 返回在部署描述中指定名称的初始化参数的值。如果不存在返回null.
　　Enumeration getInitParameterNames()：返回过滤器的所有初始化参数的名字的枚举集合。
　　public ServletContext getServletContext()：返回Servlet上下文对象的引用。
范例：利用FilterConfig得到filter配置信息

```java
public class FilterDemo02 implements Filter {

    /* 过滤器初始化
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----过滤器初始化----");
        /**
         *  <filter>
                  <filter-name>FilterDemo02</filter-name>
                  <filter-class>me.gacl.web.filter.FilterDemo02</filter-class>
                  <!--配置FilterDemo02过滤器的初始化参数-->
                  <init-param>
                      <description>配置FilterDemo02过滤器的初始化参数</description>
                      <param-name>name</param-name>
                      <param-value>gacl</param-value>
                  </init-param>
                  <init-param>
                      <description>配置FilterDemo02过滤器的初始化参数</description>
                      <param-name>like</param-name>
                      <param-value>java</param-value>
                  </init-param>
            </filter>
            
             <filter-mapping>
                  <filter-name>FilterDemo02</filter-name>
                  <!--“/*”表示拦截所有的请求 -->
                  <url-pattern>/*</url-pattern>
             </filter-mapping>
         */
        //得到过滤器的名字
        String filterName = filterConfig.getFilterName();
        //得到在web.xml文件中配置的初始化参数
        String initParam1 = filterConfig.getInitParameter("name");
        String initParam2 = filterConfig.getInitParameter("like");
        //返回过滤器的所有初始化参数的名字的枚举集合。
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        
        System.out.println(filterName);
        System.out.println(initParam1);
        System.out.println(initParam2);
        while (initParameterNames.hasMoreElements()) {
            String paramName = (String) initParameterNames.nextElement();
            System.out.println(paramName);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        System.out.println("FilterDemo02执行前！！！");
        chain.doFilter(request, response);  //让目标资源执行，放行
        System.out.println("FilterDemo02执行后！！！");
    }

    @Override
    public void destroy() {
        System.out.println("----过滤器销毁----");
    }
}
```



# 5、Filter的部署
　　Filter的部署分为两个步骤：
　　1、注册Filter
　　2、映射Filter
## 5.1、注册Filter
　　开发好Filter之后，需要在web.xml文件中进行注册，这样才能够被web服务器调用
　　在web.xml文件中注册Filter范例：

```xml
<filter>
          <description>FilterDemo02过滤器</description>
          <filter-name>FilterDemo02</filter-name>
          <filter-class>me.gacl.web.filter.FilterDemo02</filter-class>
          <!--配置FilterDemo02过滤器的初始化参数-->
          <init-param>
              <description>配置FilterDemo02过滤器的初始化参数</description>
              <param-name>name</param-name>
              <param-value>gacl</param-value>
          </init-param>
          <init-param>
              <description>配置FilterDemo02过滤器的初始化参数</description>
              <param-name>like</param-name>
              <param-value>java</param-value>
          </init-param>
</filter>
```

　　<description>用于添加描述信息，该元素的内容可为空，<description>可以不配置。
　　<filter-name>用于为过滤器指定一个名字，该元素的内容不能为空。
　　<filter-class>元素用于指定过滤器的完整的限定类名。
　　<init-param>元素用于为过滤器指定初始化参数，它的子元素<param-name>指定参数的名字，<param-value>指定参数的值。在过滤器中，可以使用FilterConfig接口对象来访问初始化参数。如果过滤器不需要指定初始化参数，那么<init-param>元素可以不配置。

## 5.2、映射Filter

- 1、spring的一般部署方式
　　在web.xml文件中注册了Filter之后，还要在web.xml文件中映射Filter

<!--映射过滤器-->
  <filter-mapping>
      <filter-name>FilterDemo02</filter-name>
      <!--“/*”表示拦截所有的请求 -->
      <url-pattern>/*</url-pattern>
  </filter-mapping>

　　<filter-mapping>元素用于设置一个 Filter 所负责拦截的资源。一个Filter拦截的资源可通过两种方式来指定：Servlet 名称和资源访问的请求路径
　　<filter-name>子元素用于设置filter的注册名称。该值必须是在<filter>元素中声明过的过滤器的名字
　　<url-pattern>设置 filter 所拦截的请求路径(过滤器关联的URL样式)
　　<servlet-name>指定过滤器所拦截的Servlet名称。
　　<dispatcher>指定过滤器所拦截的资源被 Servlet 容器调用的方式，可以是REQUEST,INCLUDE,FORWARD和ERROR之一，默认REQUEST。用户可以设置多个<dispatcher> 子元素用来指定 Filter 对资源的多种调用方式进行拦截。如下：

<filter-mapping>
    <filter-name>testFilter</filter-name>
   <url-pattern>/index.jsp</url-pattern>
   <dispatcher>REQUEST</dispatcher>
   <dispatcher>FORWARD</dispatcher>
</filter-mapping>

<dispatcher> 子元素可以设置的值及其意义：
REQUEST：当用户直接访问页面时，Web容器将会调用过滤器。如果目标资源是通过RequestDispatcher的include()或forward()方法访问时，那么该过滤器就不会被调用。
INCLUDE：如果目标资源是通过RequestDispatcher的include()方法访问时，那么该过滤器将被调用。除此之外，该过滤器不会被调用。
FORWARD：如果目标资源是通过RequestDispatcher的forward()方法访问时，那么该过滤器将被调用，除此之外，该过滤器不会被调用。
ERROR：如果目标资源是通过声明式异常处理机制调用时，那么该过滤器将被调用。除此之外，过滤器不会被调用。



- 2、springboot方式部署
```java
@Configuration
public class FilterConfig
{
    @Value("${xss.enabled}")
    private String enabled;

    @Value("${xss.excludes}")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean xssFilterRegistration()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(StringUtils.split(urlPatterns, ","));
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", excludes);
        initParameters.put("enabled", enabled);
        registration.setInitParameters(initParameters);
        return registration;
    }
}
```

[参考博客](https://www.cnblogs.com/xdp-gacl/p/3948353.html)

```java
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new FilterDemo01());
        //过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("testFilter1");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }
}
```

FilterDemo01执行前！！！
IndexController的index方法被调用
FilterDemo01执行后！！！
FilterDemo01执行前！！！
FilterDemo01执行后！！！

一个springboot的请求
http://localhost/
同时也会发出一个这样的请求
http://localhost/favicon.ico
所以会造成请求两次