---
title: Arthas使用
categories: 
- 性能分析
tags:
---

Arthas使用

[使用文档](https://alibaba.github.io/arthas/install-detail.html)

# 1、Windows下使用

安装，下载解压
D:\anzhuangbao\arthas-packaging-3.0.5-bin
在该目录下有一个as.bat 
在安装目录运行：as PID


在Windows下查找jvm进程id
jcmd –l
jcmd pid VM.flags
jinfo –flags pid

还有就是使用java安装包的jvisualvm查看

# 2、Linux下使用


