---
title: RequestContextHolder
categories: 
- spring
tags:
---

开发时，有时候会遇到在一些方法中，函数参数并没有给出request,response或者session，那该怎么解决呢 
我们可以通过SpringBoot提供的RequestContextHolder获得

ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
HttpServletRequest request = requestAttributes.getRequest();
HttpServletResponse response = requestAttributes.getResponse();
//从session里面获取对应的值
String myValue = (String) requestAttributes.getAttribute("my_value",RequestAttributes.SCOPE_SESSION);

看到这一般都会想到两个问题: 
1. request和response怎么和当前请求挂钩? （借助threadlocal）
2. request和response等是什么时候设置进去的?
（借助于DispatcherServlet-->FrameworkServlet[在这里设置进去的]--->HttpServletBean [ApplicationContextAware]）



1、 request和response怎么和当前请求挂钩?
首先分析RequestContextHolder这个类,里面有两个ThreadLocal保存当前线程下的request,关于ThreadLocal可以参考我的另一篇博文Java学习记录–ThreadLocal使用案例

    //得到存储进去的request
    private static final ThreadLocal<RequestAttributes> requestAttributesHolder =
            new NamedThreadLocal<RequestAttributes>("Request attributes");
    //可被子线程继承的request
    private static final ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder =
            new NamedInheritableThreadLocal<RequestAttributes>("Request context");



再看getRequestAttributes()方法,相当于直接获取ThreadLocal里面的值,这样就保证了每一次获取到的Request是该请求的request.

    public static RequestAttributes getRequestAttributes() {
        RequestAttributes attributes = requestAttributesHolder.get();
        if (attributes == null) {
            attributes = inheritableRequestAttributesHolder.get();
        }
        return attributes;
    }

2.2request和response等是什么时候设置进去的?
找这个的话需要对springMVC结构的DispatcherServlet的结构有一定了解才能准确的定位该去哪里找相关代码.

那么剩下要分析的的就是三个类,简单看下源码 
1. HttpServletBean 进行初始化工作 
2. FrameworkServlet 初始化 WebApplicationContext,并提供service方法预处理请求 
3. DispatcherServlet 具体分发处理.

那么就可以在FrameworkServlet查看到该类重写了service(),doGet(),doPost()…等方法,这些实现里面都有一个预处理方法processRequest(request, response);,所以定位到了我们要找的位置

查看processRequest(request, response);的实现,具体可以分为三步: 
1. 获取上一个请求的参数 
2. 重新建立新的参数 
3. 设置到XXContextHolder 
4. 父类的service()处理请求 
5. 恢复request 
6. 发布事件


参考：
https://blog.csdn.net/u012706811/article/details/53432032