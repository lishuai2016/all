---
title: nginx配置虚拟主机的三种方法
categories: 
- web
tags:
---

# nginx配置虚拟主机的三种方法

备注：基于域名的比较常用


nginx下，一个server标签就是一个虚拟主机。
1、基于域名的虚拟主机，通过域名来区分虚拟主机——应用：外部网站
2、基于端口的虚拟主机，通过端口来区分虚拟主机——应用：公司内部网站，外部网站的管理后台
3、基于ip的虚拟主机，几乎不用。

## 1、基于域名配置虚拟主机配置

需要建立/data/www /data/bbs目录，windows本地hosts添加虚拟机ip地址对应的域名解析；
对应域名网站目录下新增index.html文件； 

nginx.conf配置文件新增如下代码：

server {
   listen 80;
   server_name www.yong.com;
   index index.html;
   root /data/www;
}
server {
   listen 80;
   server_name bbs.yong.com;
   index index.html;
   root /data/bbs;
}
验证结果，使用curl测试，或者浏览器输入域名访问
# curl -xlocalhost:80 www.yong.com
this is yong linux
# curl -xlocalhost:80 bbs.yong.com
this is yong bbs

## 2、基于端口的虚拟主机配置:
使用端口来区分，浏览器使用域名或ip地址:端口号 访问

server
{
    listen 8000;
    server_name www.yong.com;
    root /data/www;
}
server
{
    listen 8001;
    server_name www.yong.com;
    root /data/bbs;
}
验证结果，使用curl测试，或者浏览器输入域名访问
# curl www.yong.com:8000
this is yong linux
# curl www.yong.com:8001
this is yong bbs

## 3、基于ip地址的虚拟主机配置:

通过ip来访问，需要配置多个ip
ifconfig eth0:1 192.168.22.21

server
{
    listen 192.168.20.20:80;
    server_name www.yong.com;
    root /data/www;
}
server
{
    listen 192.168.20.21:80;
    server_name www.yong.com;
    root /data/bbs;
}
验证结果，使用curl测试，或者浏览器输入域名访问
# curl 192.168.22.20
this is yong linux
# curl 192.168.22.21
this is yong bbs