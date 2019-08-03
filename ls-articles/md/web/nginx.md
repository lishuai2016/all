---
title: nginx
categories: 
- web
tags:
---


#NGINX


##1、Nginx的应用概述

Nginx作为一款高性能的http 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。主要有以下3方面的应用：
1、http服务器
Nginx是一个http服务可以独立提供http服务。可以做网页静态服务器。

2、虚拟主机
可以实现在一台服务器虚拟出多个网站。例如个人网站使用的虚拟主机。

3、反向代理，负载均衡
当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用nginx做反向代理。
并且多台服务器可以平均分担负载，不会因为某台服务器负载高宕机而某台服务器闲置的情况。 


## 2、nginx的反向代理和负载均衡功能

反向代理NGINX各个模块的处理流程
http--->server--->location--->proxy_pass   来设置反向代理（可以指向具体的服务器，要是指向upstream模块则请求转向服务器列表）

### 2.1、proxy模块

nginx通过proxy模块实现将客户端的请求代理至上游服务器，此时nginx与上游服务器的连接是通过http协议进行的。
nginx在实现反向代理功能时的最重要指令为 proxy_pass，它能够并能够根据URI、
客户端参数或其它的处理逻辑将用户请求调度至上游服务器上(upstream server)。

- proxy_pass URL
设置后端服务器的协议和地址；这条指令可以设置的协议是“http”或者“https”，
而地址既可以使用域名或者IP地址加端口（可选）的形式来定义：
proxy_pass http://localhost:8000/uri/;
如果解析一个域名得到多个地址，所有的地址都会以轮转的方式被使用。当然，也可以使用服务器组来定义多个地址。

- proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;    多代理设置，用来记录代理链的各个IP地址



### 2.2、全局变量
下面是可以用作if判断的全局变量

	* $args ： #这个变量等于请求行中的参数，同$query_string
	* $content_length ： 请求头中的Content-length字段。
	* $content_type ： 请求头中的Content-Type字段。
	* $document_root ： 当前请求在root指令中指定的值。
	* $host ： 请求主机头字段，否则为服务器名称。
	* $http_user_agent ： 客户端agent信息
	* $http_cookie ： 客户端cookie信息
	* $limit_rate ： 这个变量可以限制连接速率。
	* $request_method ： 客户端请求的动作，通常为GET或POST。
	* $remote_addr ： 客户端的IP地址。
	* $remote_port ： 客户端的端口。
	* $remote_user ： 已经经过Auth Basic Module验证的用户名。
	* $request_filename ： 当前请求的文件路径，由root或alias指令与URI请求生成。
	* $scheme ： HTTP方法（如http，https）。
	* $server_protocol ： 请求使用的协议，通常是HTTP/1.0或HTTP/1.1。
	* $server_addr ： 服务器地址，在完成一次系统调用后可以确定这个值。
	* $server_name ： 服务器名称。
	* $server_port ： 请求到达服务器的端口号。
	* $request_uri ： 包含请求参数的原始URI，不包含主机名，如：”/foo/bar.php?arg=baz”。
	* $uri ： 不带请求参数的当前URI，$uri不包含主机名，如”/foo/bar.html”。
	* $document_uri ： 与$uri相同。

例：http://localhost:88/test1/test2/test.php
$host：localhost
$server_port：88
$request_uri：/test1/test2/test.php
$document_uri：/test1/test2/test.php
$document_root：/var/www/html
$request_filename：/var/www/html/test1/test2/test.php





### 2.3、NGINX配置文件解析：

#运行用户
user www-data;

#指定nginx进程数，通常设置成和cpu的数量相等
worker_processes  1;

#全局错误日志定义类型，[ debug | info | notice | warn | error | crit ]
error_log /var/log/nginx/error.log info;

#进程文件
pid /var/run/nginx.pid;

#一个nginx进程打开的最多文件描述符数目，理论值应该是最多打开文件数（系统的值ulimit -n）与nginx进程数相除，但是nginx分配请求并不均匀，所以建议与ulimit -n的值保持一致。
worker_rlimit_nofile 65535;


#工作模式与连接数上限
events
{
#参考事件模型，use [ kqueue | rtsig | epoll | /dev/poll | select | poll ]; epoll模型是Linux 2.6以上版本内核中的高性能网络I/O模型，如果跑在FreeBSD上面，就用kqueue模型。
use epoll;
#单个进程最大连接数（最大连接数=连接数*进程数）
worker_connections 65535;
}


#设定http服务器，利用它的反向代理功能提供负载均衡支持
http {

    #设定mime类型,类型由mime.type文件定义
    include      mime.types;
    #默认文件类型
    default_type  application/octet-stream;
    #设定日志格式
    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #使用哪种格式的日志
    #access_log  logs/access.log  main;

	#charset utf-8; #默认编码
	server_names_hash_bucket_size 128; #服务器名字的hash表大小
	client_header_buffer_size 32k; #上传文件大小限制
	large_client_header_buffers 4 64k; #设定请求缓
	client_max_body_size 8m; #设定请求缓
	sendfile on; #开启高效文件传输模式，sendfile指令指定nginx是否调用sendfile函数来输出文件，对于普通应用设为 on，如果用来进行下载等应用磁盘IO重负载应用，可设置为off，以平衡磁盘与网络I/O处理速度，降低系统的负载。注意：如果图片显示不正常把这个改成off。
	autoindex on; #开启目录列表访问，合适下载服务器，默认关闭。
	tcp_nopush on; #防止网络阻塞
	tcp_nodelay on; #防止网络阻塞
	keepalive_timeout 120; #长连接超时时间，单位是秒


    #FastCGI相关参数是为了改善网站的性能：减少资源占用，提高访问速度。下面参数看字面意思都能理解。
	fastcgi_connect_timeout 300;
	fastcgi_send_timeout 300;
	fastcgi_read_timeout 300;
	fastcgi_buffer_size 64k;
	fastcgi_buffers 4 64k;
	fastcgi_busy_buffers_size 128k;
	fastcgi_temp_file_write_size 128k;

	#gzip模块设置
	gzip on; #开启gzip压缩输出
	gzip_min_length 1k; #最小压缩文件大小
	gzip_buffers 4 16k; #压缩缓冲区
	gzip_http_version 1.0; #压缩版本（默认1.1，前端如果是squid2.5请使用1.0）
	gzip_comp_level 2; #压缩等级
	gzip_types text/plain application/x-javascript text/css application/xml;
	#压缩类型，默认就已经包含text/html，所以下面就不用再写了，写上去也不会有问题，但是会有一个warn。
	gzip_vary on;
	#limit_zone crawler $binary_remote_addr 10m; #开启限制IP连接数的时候需要使用


    #设定负载均衡的服务器列表 支持多组的负载均衡,可以配置多个upstream  来服务于不同的Server.
    #nginx 的 upstream 支持 几 种方式的分配
    #1)、轮询（默认） 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。
    #2)、weight 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。 跟上面样，指定了权重。
    #3)、ip_hash 每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。
    #4)、fair     
    #5)、url_hash #Urlhash
    upstream mysvr {
      #weigth参数表示权值，权值越高被分配到的几率越大 
      #1.down 表示单前的server暂时不参与负载
      #2.weight 默认为1.weight越大，负载的权重就越大。   
      #3.backup： 其它所有的非backup机器down或者忙的时候，请求backup机器。所以这台机器压力会最轻。 
      #server 192.168.1.116  down;
      #server 192.168.1.116  backup;
      #server 192.168.1.121  weight=1;
      #server 192.168.1.122  weight=2;

     server 127.0.0.1:8080 weight=1;
     server 127.0.0.1:8082 weight=2;
    }

    #配置代理服务器的地址，即Nginx安装的服务器地址、监听端口、默认地址
    server {
        #1.侦听80端口
        listen      80;

        #对于server_name,如果需要将多个域名的请求进行反向代理，可以配置多个server_name来满足要求
        server_name  localhost;        //在浏览器访问的域名，域名可以有多个，用空格隔开
       
        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            # 默认主页目录在nginx安装目录的html子目录。
            root  html;
            index  index.html index.htm;         
            proxy_pass http://mysvr; #跟负载均衡服务器的upstream对应 mysvr自己去的名字，和上面对应即可
        }

        error_page  404              /404.html;

        #日志格式设定
		log_format access '$remote_addr - $remote_user [$time_local] "$request" '
		'$status $body_bytes_sent "$http_referer" '
		'"$http_user_agent" $http_x_forwarded_for';
		#定义本虚拟主机的访问日志
		access_log /var/log/nginx/ha97access.log access;


      

}




## 3、nginx几种负载均衡策略（轮询 、最少链接 、ip哈希、权重 ）
轮询：将请求依次轮询发给每个服务器。
最少链接：将请求发送给持有最少活动链接的服务器。
ip哈希：通过哈希函数决定请求发送给哪个服务器。
权重：服务器的权重越高，处理请求的概率越大。

###3.1、轮询负载均衡
在nginx.conf配置文件中添加如下配置，此配置有三台服务器提供支付服务。
http {
    upstream CashServers {
        server CashServers1.com;
        server CashServers2.com;
        server CashServers3.com;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://CashServers;        }
    }
}
 
需要注意以下几点
1.缺省配置就是轮询策略;
2.nginx负载均衡支持http和https协议，只需要修改 proxy_pass后协议即可;
3.nginx支持FastCGI, uwsgi, SCGI,memcached的负载均衡,只需将 proxy_pass改为fastcgi_pass, uwsgi_pass, scgi_pass,memcached_pass即可。
4.此策略适合服务器配置相当，无状态且短平快的服务使用。


###3.2、最少链接负载均衡
http {
    upstream CashServers {
      	least_conn;
        server CashServers1.com;
        server CashServers2.com;
        server CashServers3.com;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://CashServers;        }
    }
}
 
需要注意以下几点
1.最少链接负载均衡通过least_conn指令定义;
2.此负载均衡策略适合请求处理时间长短不一造成服务器过载的情况


###3.3、ip哈希负载均衡
http {
    upstream CashServers {
      	ip_hash;
        server CashServers1.com;
        server CashServers2.com;
        server CashServers3.com;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://CashServers;        }
    }
}
 
需要注意以下几点
1.ip哈希负载均衡使用ip_hash指令定义;
2.nginx使用请求客户端的ip地址进行哈希计算，确保使用同一个服务器响应请求;
3.此策略适合有状态服务，比如session;
###3.4、权重负载均衡
http {
    upstream CashServers {     
        server CashServers1.com weight=3;
        server CashServers2.com weight=2;
        server CashServers3.com weight=1;
    }

    server {
        listen 80;
        location / {
            proxy_pass http://CashServers;        }
    }
}
 
需要注意以下几点
1. 权重负载均衡需要使用weight指令定义;
2. 权重越高分配到需要处理的请求越多;
3.此策略可以与最少链接负载和ip哈希策略结合使用;
4.此策略比较适合服务器的硬件配置差别比较大的情况;

## 4、健康检测
nginx内置了针对服务器的健康检测机制，如果特定服务器请求失败，
则nginx即可进行标记待下次就不会请求分配给它。max_fails定义失败指定次数后进行标记服务器不可用。










http://nginx.org/en/docs/http/ngx_http_proxy_module.html?&_ga=1.161910972.1696054694.1422417685#proxy_pass

