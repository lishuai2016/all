# cas单点登录总结
（Central Authentication Service）
![](../../pic/单点登录cas.jpg)


AuthenticationFilter读取session的用户的信息，有的话直接跳过，没有的话读取ticket，
有ticket的话交由TicketValidationFilter进行验证，否则跳转到登录

主要原理：
用户第一次访问一个CAS 服务的客户web 应用时（访问URL ：http://192.168.1.90:8081/web1 ），
部署在客户web 应用的cas AuthenticationFilter ，会截获此请求，生成service 参数，然后redirect 到CAS 服务的login 接口，
url为https://cas:8443/cas/login?service=http%3A%2F%2F192.168.1.90%3A8081%2Fweb1%2F ，
认证成功后，CAS 服务器会生成认证cookie ，写入浏览器，同时将cookie 缓存到服务器本地，
CAS 服务器还会根据service 参数生成ticket,ticket 会保存到服务器，也会加在url 后面，
然后将请求redirect 回客户web 应用，url 为http://192.168.1.90:8081/web1/?ticket=ST-5-Sx6eyvj7cPPCfn0pMZuMwnbMvxpCBcNAIi6-20 。
这时客户端的AuthenticationFilter 看到ticket 参数后，会跳过，由其后面的TicketValidationFilter 处理，
TicketValidationFilter 会利用httpclient 工具访问cas 服务的/serviceValidate 接口, 将ticket 、service 都传到此接口，
由此接口验证ticket 的有效性，TicketValidationFilter 如果得到验证成功的消息，就会把用户信息写入web 应用的session里。

至此为止，SSO 会话就建立起来了，以后用户在同一浏览器里访问此web 应用时，AuthenticationFilter 会在session 里读取到用户信息，
所以就不会去CAS 认证，如果在此浏览器里访问别的web 应用时，AuthenticationFilter 在session 里读取不到用户信息，
会去CAS 的login 接口认证，但这时CAS 会读取到浏览器传来的cookie ，所以CAS 不会要求用户去登录页面登录，
只是会根据service 参数生成一个ticket ，然后再和web 应用做一个验证ticket 的交互。
