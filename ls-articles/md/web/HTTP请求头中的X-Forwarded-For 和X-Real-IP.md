---
title: HTTP请求头中的X-Forwarded-For 和X-Real-IP
categories: 
- web
tags:
---


#HTTP请求头中的X-Forwarded-For\X-Real-IP

HTTP请求头中的X-Forwarded-For\X-Real-IP


获取请求IP关注的几个http请求头
X-Forwarded-For
Proxy-Client-IP
WL-Proxy-Client-IP
HTTP_CLIENT_IP
HTTP_X_FORWARDED_FOR


X-Forwarded-For:简称XFF头，它代表客户端，也就是HTTP的请求端真实的IP，只有在通过了HTTP 代理或者负载均衡服务器时才会添加该项。
注意，引用X-Forwarded-For时要这样$http_x_forwarded_for
标准格式如下：
X-Forwarded-For: client1, proxy1, proxy2




location / {
            root   html;
            index  index.html index.htm index.php;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://192.168.247.132;
        }



总结：
X-Forwarded-For（会追加）：第一个IP为开启此设置代理服务器的前一个代理服务器IP或者用户的请求IP；
X-Real-IP（会覆盖）：IP为开启此设置的前一个代理服务器IP或者用户的请求IP；

测试实验
1、我们测试一下请求经过三层代理的情况，测试设备分配:
win10 一台
运行在win10上的虚拟机centos6-0，ip:192.168.247.131，一级代理
运行在win10上的虚拟机centos6-1, ip:192.168.247.132 ,二级代理
运行在win10上的虚拟机centos6-2, ip:192.168.247.133 ,三级代理
云服务器,应用服务器
 
2.测试环境配置:

win10 在/etc/hosts文件中添加192.168.247.131 http://test.proxy.com
centos6-0，ip:192.168.247.131，安装nginx，把所有请求转发到192.168.247.132
centos6-1, ip:192.168.247.132，安装nginx，把所有请求转发到192.168.247.133
centos6-2, ip:192.168.247.133，安装nginx，把所有请求转发到云服务器
在云服务器上的日志中打印http header中的X-Forwarded-For信息
防火墙可以关闭掉，防止win10请求无法进入代理链
 
3.nginx配置文件

#centos6-0，ip:192.168.247.131 ,nginx.conf
location / {
            root   html;
            index  index.html index.htm index.php;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://192.168.247.132;
        }

#centos6-1，ip:192.168.247.132 ,nginx.conf

    location / {
      root   html;
      index  index.html index.htm index.php;
      #proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_pass http://192.168.247.133;
    } 


#centos6-2，ip:192.168.247.133 ,nginx.conf
    location / {
        root   html;
        index  index.html index.htm index.php;　　
        #proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://123.206.96.111;
    }


#云服务器方便起见在日志中设置打印$http_x_forwarded_for，进行观察
 log_format  main  '$http_x_forwarded_for|$http_x_real_ip|$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';


4.基于上面的配置在win10浏览器输入:"http://test.proxy.com" 查看云服务器日志打印结果如下:

 

192.168.247.1, 192.168.247.131, 192.168.247.132|192.168.247.1|101.254.182.6 - - [22/May/2017:18:20:27 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131, 192.168.247.132"
 

192.168.247.1, 192.168.247.131, 192.168.247.132 为$http_x_forwarded_for内容，显然记录了代理过程，其中192.168.247.1是客户端ip
192.168.247.1 为基于上述设置的真实IP(不一定准确）
101.254.182.6 公网IP


通过上面可知：http_x_forwarded_for记录代理服务器的处理路径，并且第一个ip为请求第一个代理服务器的IP
http_x_forwarded_for：192.168.247.1, 192.168.247.131, 192.168.247.132
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6
拿到http_x_forwarded_for的第一个IP为一个局域网的IP
















我们要仔细测试一下在不同代理服务器设置X-FORWARDED-FOR在应用服务器拿到的$http_x_forwarded_for有何不同

1.只在proxy01设置X-FORWARDED-FOR, 在proxy02,proxy03配置文件中注释掉proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

192.168.247.1|192.168.247.1|101.254.182.6 - - [22/May/2017:18:52:49 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1"


总结：
http_x_forwarded_for：192.168.247.1
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6



2.只在proxy02设置X-FORWARDED-FOR, 在proxy01,proxy03配置文件中注释掉proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

192.168.247.131|192.168.247.1|101.254.182.6 - - [22/May/2017:18:59:59 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.131"
总结：
http_x_forwarded_for：192.168.247.131
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6


3.只在proxy03设置X-FORWARDED-FOR, 在proxy01,proxy02配置文件中注释掉proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

192.168.247.132|192.168.247.1|101.254.182.6 - - [22/May/2017:19:01:27 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.132"
总结：
http_x_forwarded_for：192.168.247.132
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6


4.只在proxy01，proxy03设置X-FORWARDED-FOR, 在proxy02配置文件中注释掉proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

192.168.247.1, 192.168.247.132|192.168.247.1|101.254.182.6 - - [22/May/2017:19:05:49 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.132"
总结：
http_x_forwarded_for：192.168.247.1, 192.168.247.132
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6


5.只在proxy02，proxy03设置X-FORWARDED-FOR, 在proxy01配置文件中注释掉proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

192.168.247.131, 192.168.247.132|192.168.247.1|101.254.182.6 - - [22/May/2017:19:08:39 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.131, 192.168.247.132"
总结：
http_x_forwarded_for：192.168.247.131, 192.168.247.132
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6


6.只在proxy01，proxy02设置X-FORWARDED-FOR, 在proxy03配置文件中注释掉proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

192.168.247.1, 192.168.247.131|192.168.247.1|101.254.182.6 - - [22/May/2017:19:10:40 +0800] "GET /admin/login/?next=%2Fadmin%2F HTTP/1.0" 200 623 "http://test.proxy.com/admin/login/?next=%2Fadmin%2F" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131"
 总结：
http_x_forwarded_for：192.168.247.1, 192.168.247.131
http_x_real_ip：192.168.247.1
remote_addr：101.254.182.6




 小结：

1.通过以上几种情况我们可以了解到设置X-Forwarded-For是一个可叠加的过程，后面的代理会把前面代理的IP加入X-Forwarded-For，类似于append的作用.

2.我们看到在三层代理情况下无论如何设置，应用服务器不可能从$http_x_forwarded_for拿到与它直连的这台服务器的ip（proxy03 ip）。
此时我们可以使用$remote_addr（远程ip,表示直连的那台代理）.
一句话，当前服务器无法通过$http_x_forwarded_for获得上级代理或者客户端的ip,应该使用$remote_addr.

3.在代理过程中至少有一个代理设置了proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;否则后面代理或者应用服务器无法获得相关信息.

4.注意，应用服务器可以通过$proxy_add_x_forwarded_for 客户端IP（只要至少proxy01代理设置了proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;我们取第一IP就好了），但是我们要考虑客户端伪造头部的情况,如下示例：

假设我们在所有代理都加上了proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
然后我们在proxy01机器上本机curl代替win10模拟一个客户端请求，

在proxy01上执行: curl localhost/admin -H 'X-Forwarded-For: 1.1.1.1' -H 'X-Real-IP: 2.2.2.2'

1.1.1.1, 127.0.0.1, 192.168.247.131, 192.168.247.132|127.0.0.1|101.254.182.6 - - [23/May/2017:11:02:09 +0800] "GET /admin HTTP/1.0" 301 263 "-" "curl/7.15.5 (i386-redhat-linux-gnu) libcurl/7.15.5 OpenSSL/0.9.8b zlib/1.2.3 libidn/0.6.5" "1.1.1.1, 127.0.0.1, 192.168.247.131, 192.168.247.132"
  总结：
http_x_forwarded_for：1.1.1.1, 127.0.0.1, 192.168.247.131, 192.168.247.132
http_x_real_ip：127.0.0.1
remote_addr：101.254.182.6
可以看到，1.1.1.1放到了最前面，所以我们不能够想当然的去取第一个ip作为客户端的这是IP.这里127.0.0.1是真实IP.

5.虽然X-Forwarded-For可以伪造，但是对我们依然有用，比如我们就从proxy01代理往后截取就行了，这样就能做到直接忽视伪造得IP.



 X-Real-IP

下面我们看一下有多级代理存在时如何获取客户端真实IP.

首先要明确在header里面的 X-Real-IP只是一个变量，后面的设置会覆盖前面的设置（跟X-Forwarded-For的追加特性区别明显）,所以我们一般只在第一个代理设置proxy_set_header X-Real-IP $remote_addr;就好了，然后再应用端直接引用$http_x_real_ip就行.

1.假如我们只在proxy01设置了 X-Real-IP
192.168.247.1, 192.168.247.131, 192.168.247.132|192.168.247.1|101.254.182.6 - - [23/May/2017:11:23:00 +0800] "GET /test/ HTTP/1.0" 200 9 "http://test.proxy.com/test/" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131, 192.168.247.132"
 
结果：
X-Real-IP：192.168.247.1
 
2.假如我们只在proxy02设置了X-Real-IP
192.168.247.1, 192.168.247.131, 192.168.247.132|192.168.247.131|101.254.182.6 - - [23/May/2017:11:26:22 +0800] "GET /test/ HTTP/1.0" 200 9 "http://test.proxy.com/test/" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131, 192.168.247.132"
 结果：为设置代理服务器被请求的IP
X-Real-IP：192.168.247.131

 

3.假如我们只在proxy03设置了X-Real-IP
192.168.247.1, 192.168.247.131, 192.168.247.132|192.168.247.132|101.254.182.6 - - [23/May/2017:11:27:21 +0800] "GET /test/ HTTP/1.0" 200 9 "http://test.proxy.com/test/" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131, 192.168.247.132"
结果：为设置代理服务器被请求的IP
X-Real-IP：192.168.247.132

4.所有代理都设置X-Real-IP
192.168.247.1, 192.168.247.131, 192.168.247.132|192.168.247.132|101.254.182.6 - - [23/May/2017:11:29:09 +0800] "GET /test/ HTTP/1.0" 200 9 "http://test.proxy.com/test/" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131, 192.168.247.132"
结果：为设置代理服务器被请求的IP
X-Real-IP：192.168.247.132


5.再试一个只设置proxy01,proxy02的看看

192.168.247.1, 192.168.247.131, 192.168.247.132|192.168.247.131|101.254.182.6 - - [23/May/2017:11:30:36 +0800] "GET /test/ HTTP/1.0" 200 9 "http://test.proxy.com/test/" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36" "192.168.247.1, 192.168.247.131, 192.168.247.132"
结果：为设置代理服务器被请求的IP
X-Real-IP：192.168.247.131

假如有人假冒X-Real-IP呢？
6. 在proxy01上执行: curl localhost/admin -H 'X-Forwarded-For: 1.1.1.1' -H 'X-Real-IP: xx.xx.xx.xx'

1.1.1.1, 127.0.0.1, 192.168.247.131, 192.168.247.132|192.168.247.131|101.254.182.6 - - [23/May/2017:11:36:02 +0800] "GET /admin HTTP/1.0" 301 263 "-" "curl/7.15.5 (i386-redhat-linux-gnu) libcurl/7.15.5 OpenSSL/0.9.8b zlib/1.2.3 libidn/0.6.5" "1.1.1.1, 127.0.0.1, 192.168.247.131, 192.168.247.132"
并没有影响.
结果：为设置代理服务器被请求的IP
X-Real-IP：192.168.247.131
 