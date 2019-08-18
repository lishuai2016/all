请求网关发起服务调用：http://localhost:9000/order-service/order/create?productId=1&userId=1&count=1&totalPrice=1

灰度发布测试

http://localhost:9000/order-service/order/create?productId=1&userId=1&count=1&totalPrice=1&gray=true


这里是根据gray=true参数来切流量的，实际的场景应该是根据其他的规则来。


测试使用到的服务：

- service1 同一个服务的不同实例
- service2
- eureka-server-single  单台的注册中心
- zuul-gateway-gray-release 进行灰度测试的网关


1、首先使用它访问，http://localhost:9000/order-service/order/create?productId=1&userId=1&count=1&totalPrice=1

这个时候流量均匀的分发到两台机器上，

2、然后在数据库插入一条记录

1	order-service	/order	1  开启灰度发布功能

3、配置

```
service1

eureka: 
  instance: 
    metadata-map: 
      version: new

service2

eureka: 
  instance: 
    metadata-map: 
      version: current

```

4、请求http://localhost:9000/order-service/order/create?productId=1&userId=1&count=1&totalPrice=1&gray=true

发现请求全部打到service1上，实现了灰度发布功能。

