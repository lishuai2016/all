---
title: DispatcherType
categories: 
- spring
tags:
---


DispatcherType请求转发的类型

HTTP请求的分发类型

```java
public enum DispatcherType {
    FORWARD,  
    INCLUDE,
    REQUEST,
    ASYNC,
    ERROR
}
```



FORWARD,  
INCLUDE,
REQUEST,
SYNC,
ERROR


有时一个请求需要多个Servlet协作才能完成，所以需要在一个Servlet跳到另一个Servlet！
一个请求跨多个Servlet，需要使用转发和包含。
1、请求转发：由下一个Servlet完成响应体！当前Servlet可以设置响应头！（留头不留体）
2、请求包含：由两个Servlet共同完成响应体！（留头又留体）
无论是请求转发还是请求包含，都在一个请求范围内！使用同一个request和response！



请求转发和重定向的区别：
1、请求转发是一个请求一次响应，而重定向是两次请求两次响应。
2、请求转发地址不变化，而重定向会显示后一个请求的地址
3、请求转发只能转发到本项目其它Servlet，而重定向不只能重定向到本项目的其它Servlet，还能定向到其它项目
4、请求转发是服务端行为，只需给出转发的Servlet路径，而重定向需要给出requestURI，既包含项目名！