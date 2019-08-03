参考：NettyRpc
http://www.cnblogs.com/luxiaoxun/p/5272384.html
备注：NettyRpc是参考这个 https://gitee.com/huangyong/rpc 开发的

# 问题
1、消费端如何发送消息的？
2、zookeeper的注册和发现机制？
3、负载均衡机制？


# RpcServer
1、借助接口ApplicationContextAware，在spring启动时，通过自定义注解RpcService获得各个接口的实现类；
2、借助接口InitializingBean，在实例化bean后启动start，开启服务提供者监听接口，等待消费端的请求；
3、在start函数的最后会注册服务提供者的IP和端口到zookeeper上，为什么只有IP和端口信息？？？如何区分不同的接口服务？？？



# RpcHandler
1、接收消费端的请求，通过线程池来处理请求，每个请求开启一个线程进行处理；
2、这里通过消费端传递过来的RpcRequest，通过解码获得请求的类接口、方法、参数类型、参数等信息；
3、然后通过请求接口查找注册的接口和实现类的hashmap，找到对应接口的实现类，然后通过反射调用具体的方法；
4、最后封装返回结果为RpcResponse；


# RpcClient
消费端通过它封装接口的调用通过动态代理调用远程的服务


# ClientProxy
具体的代理对象处理类，实现了InvocationHandler接口，动态代理
