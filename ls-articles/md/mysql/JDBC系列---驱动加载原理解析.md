---
title: JDBC系列---驱动加载原理解析
categories: 
- mysql
tags:
---

#JDBC驱动加载原理解析

[参考](https://blog.csdn.net/luanlouis/article/details/29850811)

目录：
1、前言
2、概述
3、驱动类加载内存的过程
4、Driver的功能（手动加载驱动Driver并实例化进行数据库操作的例子）
5、DriverManager角色
5.1、使用DriverManager获取指定的Driver
5.2、使用DriverManager注册和注销驱动Driver
5.3、使用DriverManager创建connection链接对象
5.4、jdbcdrivers


