---
title: http请求和响应的格式
categories: 
- http
tags:
---


# HTTP请求报文格式
HTTP请求报文主要由请求行、请求头部、空行、请求正文四部分组成(可选部分，比如GET请求就没有请求正文)，其中空行的作用是通知服务器以下不再有请求头。
格式：
请求方法 	URL 	协议版本号
头部字段名称1：值1
头部字段名称2：值2
...
头部字段名称n：值n

请求正文

即：
＜request-line＞
＜headers＞
＜blank line＞
[＜request-body＞  可选


实例
## GET
最常见的一种请求方式，当客户端要从服务器中读取文档时，当点击网页上的链接或者通过在浏览器的地址栏输入网址来浏览网页的，使用的都是GET方式。GET方法要求服务器将URL定位的资源放在响应报文的数据部分，回送给客户端。使用GET方法时，请求参数和对应的值附加在URL后面，利用一个问号（“?”）代表URL的结尾与请求参数的开始，传递参数长度受限制。例如，/index.jsp?id=100&op=bind,这样通过GET方式传递的数据直接表示在地址中，所以我们可以把请求结果以链接的形式发送给好友。以用google搜索domety为例，Request格式如下：

GET /search?hl=zh-CN&source=hp&q=domety&aq=f&oq= HTTP/1.1  
Accept: image/gif, image/x-xbitmap, image/jpg, image/pjpg, application/vnd.ms-excel, application/vnd.ms-powerpoint, 
application/msword, application/x-silverlight, application/x-shockwave-flash, */*  
Referer: <a href="http://www.google.cn/">http://www.google.cn/</a>  
Accept-Language: zh-cn  
Accept-Encoding: gzip, deflate  
User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; TheWorld)  
Host: <a href="http://www.google.cn">www.google.cn</a>  
Connection: Keep-Alive  
Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g; 
NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-
FxlRugatx63JLv7CWMD6UB_O_r

可以看到，GET方式的请求一般不包含”请求内容”部分，请求数据以地址的形式表现在请求行。地址链接如下：
<a href="http://www.google.cn/search?hl=zh-CN&source=hp&q=domety&aq=f&oq=">http://www.google.cn/search?hl=zh-CN&source=hp
&q=domety&aq=f&oq=</a> 
地址中”?”之后的部分就是通过GET发送的请求数据，我们可以在地址栏中清楚的看到，各个数据之间用”&”符号隔开。显然，这种方式不适合传送私密数据。另外，由于不同的浏览器对地址的字符限制也有所不同，一般最多只能识别1024个字符，所以如果需要传送大量数据的时候，也不适合使用GET方式。

## POST
对于上面提到的不适合使用GET方式的情况，可以考虑使用POST方式，因为使用POST方法可以允许客户端给服务器提供信息较多。POST方法将请求参数封装在HTTP请求数据中，以名称/值的形式出现，可以传输大量数据，这样POST方式对传送的数据大小没有限制，而且也不会显示在URL中。还以上面的搜索domety为例，如果使用POST方式的话，格式如下：
POST /search HTTP/1.1  
Accept: image/gif, image/x-xbitmap, image/jpg, image/pjpg, application/vnd.ms-excel, application/vnd.ms-powerpoint, 
application/msword, application/x-silverlight, application/x-shockwave-flash, */*  
Referer: <a href="http://www.google.cn/">http://www.google.cn/</a>  
Accept-Language: zh-cn  
Accept-Encoding: gzip, deflate  
User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; TheWorld)  
Host: <a href="http://www.google.cn">www.google.cn</a>  
Connection: Keep-Alive  
Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g; 
NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-
FxlRugatx63JLv7CWMD6UB_O_r  

hl=zh-CN&source=hp&q=domety
可以看到，POST方式请求行中不包含数据字符串，这些数据保存在”请求内容”部分，各数据之间也是使用”&”符号隔开。POST方式大多用于页面的表单中。因为POST也能完成GET的功能，因此多数人在设计表单的时候一律都使用POST方式，其实这是一个误区。GET方式也有自己的特点和优势，我们应该根据不同的情况来选择是使用GET还是使用POST。





# HTTP响应报文格式
HTTP响应报文主要由状态行、响应头部、空行、响应正文四部分组成
格式：
协议版本号 	状态码	状态码描述
头部字段名称1：值1
头部字段名称2：值2
...
头部字段名称n：值n

响应正文

即：
＜status-line＞
＜headers＞
＜blank line＞
[＜response-body＞] 可选

例子：
HTTP/1.1 200 OK
Date: Sat, 31 Dec 2005 23:59:59 GMT
Content-Type: text/html;charset=ISO-8859-1
Content-Length: 122

＜html＞
＜head＞
＜title＞Wrox Homepage＜/title＞
＜/head＞
＜body＞
＜!-- body goes here --＞
＜/body＞
＜/html＞




# HTTP请求方法说明
方法		说明													支持的http协议版本
get		获取对象													http1.0  http1.1
post	传输实体主题（更新资源）									http1.0  http1.1
put		传输文件（更新资源，和post比有安全性问题）					http1.0  http1.1
head	获取报文的头部（同get，只是返回的内容只包含头部信息）		http1.0  http1.1
delete	删除文件（同put相反，不安全，没有验证机制）					http1.0  http1.1
options	询问uri支持的访问方法										http1.1
trace   追踪路径													http1.1
connect 要求用隧道协议链接代理									http1.1
link	建立与资源之间的链接（废弃）								http1.0
UNlink	断开链接关系 （废弃）										http1.0

# 请求头部
请求头部为请求报文添加了一些附加信息，由“名/值”对组成，每行一对，名和值之间使用冒号分隔。常见请求头如下：
请求头                             说明
Host					接受请求的服务器地址，可以是IP:端口号，也可以是域名
User-Agent				发送请求的应用程序名称
Connection				指定与连接相关的属性，如Connection:Keep-Alive
Accept-Charset			通知服务端可以发送的编码格式
Accept-Encoding			通知服务端可以发送的数据压缩格式
Accept-Language			通知服务端可以发送的语言
Cookie 					用户浏览器中保存的cookie信息


实例：
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9
Cache-Control: max-age=0
Connection: keep-alive
Cookie: shshshfpb=09338f5e76dfc40ef709250d9b4464e059541910a69b1b6285ac427cec; shshshfpa=cd7bc992-9603-0963-e5af-b0fac7fbe706-1532062950; 
Host: xxx.xxx.com
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36

# 响应头部
与请求头部类似，为响应报文添加了一些附加信息
响应头                             说明
Server					服务器应用程序软件的名称和版本
Content-Type			响应正文的类型（是图片还是二进制字符串）
Content-Length			响应正文长度
Content-Charset			响应正文使用的编码
Content-Encoding		响应正文使用的数据压缩格式
Content-Language		响应正文使用的语言


实例：
Cache-Control: max-age=0
Connection: close
Content-Encoding: gzip
Content-Language: zh-CN
Content-Type: text/html;charset=UTF-8
Date: Fri, 09 Nov 2018 01:42:29 GMT
Expires: Fri, 09 Nov 2018 01:42:29 GMT
Server: JDWS/1.0.0
Transfer-Encoding: chunked
Vary: Accept-Encoding



# 响应的状态码
状态行格式如下：
HTTP-Version Status-Code Reason-Phrase CRLF
其中，HTTP-Version表示服务器HTTP协议的版本；Status-Code表示服务器发回的响应状态代码；Reason-Phrase表示状态代码的文本描述。状态代码由三位数字组成，第一个数字定义了响应的类别，且有五种可能取值。

1xx：指示信息--表示请求已接收，继续处理。
2xx：成功--表示请求已被成功接收、理解、接受。
3xx：重定向--要完成请求必须进行更进一步的操作。
4xx：客户端错误--请求有语法错误或请求无法实现。
5xx：服务器端错误--服务器未能实现合法的请求。
常见状态代码、状态描述的说明如下。

200 OK：客户端请求成功。
400 Bad Request：客户端请求有语法错误，不能被服务器所理解。
401 Unauthorized：请求未经授权，这个状态代码必须和WWW-Authenticate报头域一起使用。
403 Forbidden：服务器收到请求，但是拒绝提供服务。
404 Not Found：请求资源不存在，举个例子：输入了错误的URL。
500 Internal Server Error：服务器发生不可预期的错误。
503 Server Unavailable：服务器当前不能处理客户端的请求，一段时间后可能恢复正常，举个例子：HTTP/1.1 200 OK（CRLF）。



# HTTP请求GET和POST的区别
1）、GET提交，请求的数据会附在URL之后（就是把数据放置在HTTP协议头＜request-line＞中），以?分割URL和传输数据，多个参数用&连接;例如：login.action?name=hyddd&password=idontknow&verify=%E4%BD%A0 %E5%A5%BD。如果数据是英文字母/数字，原样发送，如果是空格，转换为+，如果是中文/其他字符，则直接把字符串用BASE64加密，得出如： %E4%BD%A0%E5%A5%BD，其中％XX中的XX为该符号以16进制表示的ASCII。
POST提交：把提交的数据放置在是HTTP包的包体＜request-body＞中。因此，GET提交的数据会在地址栏中显示出来，而POST提交，地址栏不会改变

2）、传输数据的大小：
首先声明,HTTP协议没有对传输的数据大小进行限制，HTTP协议规范也没有对URL长度进行限制。 而在实际开发中存在的限制主要有：
GET:特定浏览器和服务器对URL长度有限制，例如IE对URL长度的限制是2083字节(2K+35)。对于其他浏览器，如Netscape、FireFox等，理论上没有长度限制，其限制取决于操作系统的支持。 因此对于GET提交时，传输数据就会受到URL长度的限制。
POST:由于不是通过URL传值，理论上数据不受限。但实际各个WEB服务器会规定对post提交数据大小进行限制，Apache、IIS6都有各自的配置。

3）、安全性：
 POST的安全性要比GET的安全性高。注意：这里所说的安全性和上面GET提到的“安全”不是同个概念。上面“安全”的含义仅仅是不作数据修改，而这里安全的含义是真正的Security的含义，比如：通过GET提交数据，用户名和密码将明文出现在URL上，因为(1)登录页面有可能被浏览器缓存， (2)其他人查看浏览器的历史纪录，那么别人就可以拿到你的账号和密码了，


# http短链接和长连接的区别
http短链接：每次tcp链接后进行一次http请求和响应后就断开，再次进行http请求的时候就会再次建立tcp链接；
http长连接：每次tcp链接后可以进行多次的http请求和响应，可以减少tcp链接建立的开销；


参考：
https://www.cnblogs.com/biyeymyhjob/archive/2012/07/28/2612910.html