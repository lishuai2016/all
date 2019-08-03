[HTTPS 原理解析](http://www.cnblogs.com/zery/p/5164795.html)

---
title: HTTPS
categories: 
- web
tags:
---

https加解密
[参考](https://segmentfault.com/a/1190000017544854)


https://www.cnblogs.com/taomylife/p/4778009.html
http://www.4hou.com/info/news/4067.html




什么叫做SSL,TLS.

SSL其实就是个加密套件，负责对HTTP的数据进行加密。TLS是SSL的升级版。现在的HTTPS，加密套件基本上是TLS。


一、协议

1、HTTP 协议（HyperText Transfer Protocol，超文本传输协议）：是客户端浏览器或其他程序与Web服务器之间的应用层通信协议 。

2、HTTPS 协议（HyperText Transfer Protocol over Secure Socket Layer）：可以理解为HTTP+SSL/TLS， 即 HTTP 下加入 SSL 层，HTTPS 的安全基础是 SSL，因此加密的详细内容就需要 SSL，用于安全的 HTTP 数据传输。

HTTPS 加密算法原理详解
    如上图所示  HTTPS 相比 HTTP 多了一层 SSL/TLS

SSL（Secure Socket Layer，安全套接字层）：1994年为 Netscape 所研发，SSL 协议位于 TCP/IP 协议与各种应用层协议之间，为数据通讯提供安全支持。

TLS（Transport Layer Security，传输层安全）：其前身是 SSL，它最初的几个版本（SSL 1.0、SSL 2.0、SSL 3.0）由网景公司开发，1999年从 3.1 开始被 IETF 标准化并改名，发展至今已经有 TLS 1.0、TLS 1.1、TLS 1.2 三个版本。SSL3.0和TLS1.0由于存在安全漏洞，已经很少被使用到。TLS 1.3 改动会比较大，目前还在草案阶段，目前使用最广泛的是TLS 1.1、TLS 1.2。





二、加密算法

1、对称加密

有流式、分组两种，加密和解密都是使用的同一个密钥。

例如：DES、AES-GCM、ChaCha20-Poly1305等

2、非对称加密

加密使用的密钥和解密使用的密钥是不相同的，分别称为：公钥、私钥，公钥和算法都是公开的，私钥是保密的。非对称加密算法性能较低，但是安全性超强，由于其加密特性，非对称加密算法能加密的数据长度也是有限的。

例如：RSA、DSA、ECDSA、 DH、ECDHE

3、哈希算法

将任意长度的信息转换为较短的固定长度的值，通常其长度要比信息小得多，且算法不可逆。

例如：MD5、SHA-1、SHA-2、SHA-256 等

4、数字签名

签名就是在信息的后面再加上一段内容（信息经过hash后的值），可以证明信息没有被修改过。hash值一般都会加密后（也就是签名）再和信息一起发送，以保证这个hash值不被修改。







三、握手过程的具体描述如下：


1.浏览器将自己支持的一套加密规则发送给网站。 
2.网站从中选出一组加密算法与HASH算法，并将自己的身份信息以证书的形式发回给浏览器。证书里面包含了网站地址，加密公钥，以及证书的颁发机构等信息。 
3.浏览器获得网站证书之后浏览器要做以下工作： 
a) 验证证书的合法性（颁发证书的机构是否合法，证书中包含的网站地址是否与正在访问的地址一致等），如果证书受信任，则浏览器栏里面会显示一个小锁头，否则会给出证书不受信的提示。 
b) 如果证书受信任，或者是用户接受了不受信的证书，浏览器会生成一串随机数的密码，并用证书中提供的公钥加密。 
c) 使用约定好的HASH算法计算握手消息，并使用生成的随机数对消息进行加密，最后将之前生成的所有信息发送给网站。 
4.网站接收浏览器发来的数据之后要做以下的操作： 
a) 使用自己的私钥将信息解密取出密码，使用密码解密浏览器发来的握手消息，并验证HASH是否与浏览器发来的一致。 
b) 使用密码加密一段握手消息，发送给浏览器。 
5.浏览器解密并计算握手消息的HASH，如果与服务端发来的HASH一致，此时握手过程结束，之后所有的通信数据将由之前浏览器生成的随机密码并利用对称加密算法进行加密。


















密钥

什么叫做密钥，其实就是在交互过程中，“高大上的”密码算法中需要输入的一段参数。同一个明文，同一套密码算法，但是就是因为密钥的不同，就会产生不同的“密文”。 
很多知名的密码算法都是公开的，比如说MD5,RSA,DES。但是密钥才是决定密文是否是安全的重要参数，不言而喻，密钥越长，破解的难度就越大。根据密钥的使用方法，密码大概分了两类：对称加密，非对称加密。





对称加密，又叫做共享钥匙加密。 
它有一个特点：加密和解密使用相同的密钥！上面说的著名的DES加密，就是一种对称加密！ 
对称加密的优点在于：加密、解密效率比较高。
缺点在于：数据发送方、数据接收方需要协商、共享同一把密钥，并确保密钥不泄露给其他人。此外，对于有很多数据交换需求的个体，两两之间需要分配并维护一把密钥，这个带来的成本基本是不可接受的。换句话说，接收的人，需要发送的人告诉他密钥才能解密，所以密钥怎么才能安全的发送给接受的人，成了一个很难的问题。








https://itbilu.com/other/relate/VkDiP1ivz.html






升级HTTPS，我们可以分为购买证书、安装证书、设置跳转这三个步骤，下面我们展开来讲讲这三步详细的实施流程。
https://www.cnblogs.com/powertoolsteam/p/http2https.html


Nginx
1、首先在Nginx的安装目录下创建cert目录，将下载的全部文件拷贝到cert目录中。
2、打开 Nginx 安装目录下 conf 目录中的 nginx.conf 文件，找到“HTTPS server”部分。
3、指定证书路径，为如下示意并保存：

3.1、NGINX配置证书（安装）
server {
    listen 443;
    server_name 你网站的域名;
    ssl on;
    root html;
    index index.html index.htm;
    ssl_certificate   cert/你的证书文件名.pem;
    ssl_certificate_key  cert/你的证书文件名.key;
    ssl_session_timeout 5m;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers on;
    location / {
        root html;
        index index.html index.htm;
    }
}
3.2、设置http请求到https（设置跳转）
server {
 
        listen 80;
 
        server_name 您的域名;
 
        return 301 https://$server_name$request_uri;
 
}

页面永久性移走（301重定向）是一种非常重要的“自动转向”技术。网址重定向最为可行的一种办法。当用户或搜索引擎向网站服务器发出浏览请求时，服务器返回的HTTP数据流中头信息(header)中的状态码的一种，表示本网页永久性转移到另一个地址。

4、 重启Nginx，这时候你的站点应该就已经可以通过https方式访问了




Tomcat中Connector
tomcat中有三种 Connector 实现：block、nio 和 APR。前两者使用Java SSL（这需要 keystore 的配置 ），APR使用OpenSSL（不需要用keystore，直接指定证书），配置略有不同。

**Nginx+Tomcat+SSL
**
实际上，大规模的网站都有很多台Web服务器和应用服务器组成，用户的请求可能是经由 Varnish、HAProxy、Nginx之后才到应用服务器，中间有好几层。而中小规模的典型部署常见的是 Nginx+Tomcat 这种两层配置，而Tomcat 会多于一台，Nginx 作为静态文件处理和负载均衡。

如果Nginx作为前端代理的话，则Tomcat根本不需要自己处理 https，全是Nginx处理的。用户首先和Nginx建立连接，完成SSL握手，而后Nginx 作为代理以 http 协议将请求转给 tomcat 处理，Nginx再把 tomcat 的输出通过SSL 加密发回给用户，这中间是透明的，Tomcat只是在处理 http 请求而已。因此，这种情况下不需要配置 Tomcat 的SSL，只需要配置 Nginx 的SSL 和 Proxy。

在代理模式下，Tomcat 如何识别用户的直接请求（URL、IP、https还是http )？
在透明代理下，如果不做任何配置Tomcat 认为所有的请求都是 Nginx 发出来的，这样会导致如下的错误结果：
request.getScheme() //总是 http，而不是实际的http或https
request.isSecure() //总是false（因为总是http）
request.getRemoteAddr() //总是 nginx 请求的 IP，而不是用户的IP
request.getRequestURL() //总是 nginx 请求的URL 而不是用户实际请求的 URL
response.sendRedirect( 相对url ) //总是重定向到 http 上 （因为认为当前是 http 请求）

如果程序中把这些当实际用户请求做处理就有问题了。解决方法很简单，只需要分别配置一下 Nginx 和 Tomcat 就好了，而不用改程序。

配置 Nginx 的转发选项：

proxy_set_header Host $host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
proxy_set_header X-Forwarded-Proto $scheme;

配置Tomcat server.xml 的 Engine 模块下配置一个 Value：
<Valve className="org.apache.catalina.valves.RemoteIpValve" remoteIpHeader="X-Forwarded-For" protocolHeader="X-Forwarded-Proto" protocolHeaderHttpsValue="https"/>

配置双方的 X-Forwarded-Proto 就是为了正确地识别实际用户发出的协议是 http 还是 https。X-Forwarded-For 是为了获得实际用户的 IP。
这样以上5项测试就都变为正确的结果了，就像用户在直接访问 Tomcat 一样。




