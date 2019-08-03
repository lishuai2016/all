---
title: cookie
categories: 
- http
tags:
---


存储在用户本地终端的数据,http请求自动发送，跨域除外
# 用途
客户端记录用户信息

# 特点
存储在硬盘上的cookie可以在不同的浏览器进程间共享，比如两个IE窗口。而对于保存在内存里的cookie，不同的浏览器有不同的处理方式

# 属性
name：cookie名称
value：cookie值
domain：可以访问cookie的域名，某一级域名可以访问上一级级域名的cookie
expires/Max-Age：过期时间
Size：cookie的大小
http：httponly属性，为true，不能用document.cookie获得
secure：为true只能在https获得
path：子路径访问父路径cookie


# cookie机制
正统的cookie分发是通过扩展HTTP协议来实现的，服务器通过在HTTP的响应头中加上一行特殊的指示以提示浏览器按照指示生成相应的cookie。
然而纯粹的客户端脚本如JavaScript或者VBScript也可以生成cookie。而cookie的使用是由浏览器按照一定的原则在后台自动发送给服务器的。
浏览器检查所有存储的cookie，如果某个cookie所声明的作用范围大于等于将要请求的资源所在的位置，则把该cookie附在请求资源的HTTP请求头上发送给服务器。

# cookie的内容主要包括：名字，值，过期时间，路径和域。
路径与域一起构成cookie的作用范围。若不设置过期时间，则表示这个cookie的生命期为浏览器会话期间，关闭浏览器窗口，cookie就消失。
这种生命期为浏览器会话期的cookie被称为会话cookie。会话cookie一般不存储在硬盘上而是保存在内存里，当然这种行为并不是规范规定的。
若设置了过期时间，浏览器就会把cookie保存到硬盘上，关闭后再次打开浏览器，这些cookie仍然有效直到超过设定的过期时间。
存储在硬盘上的cookie可以在不同的浏览器进程间共享，比如两个IE窗口。而对于保存在内存里的cookie，不同的浏览器有不同的处理方式。 


[参考](https://www.cnblogs.com/fnng/archive/2012/08/14/2637279.html)

