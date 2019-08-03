备注：此基于netty的rpc模块是参考https://gitee.com/huangyong/rpc进行重构的

【主要关注】

# 注意的代码模块
ls-rpc-netty-sample ：框架的核心代码
ls-rpc-netty-sample-api ：demo使用的接口定义
ls-rpc-netty-sample-client ：demo消费端
ls-rpc-netty-sample-server ：demo服务提供者


# ls-rpc-netty-sample核心代码结构
com.xxx.rpc
    client
    common
        bean
        codec
        util
    registry
        zookeeper
    server


# 问题
1、消费端如何发送消息的？
2、zookeeper的注册和发现机制？
3、负载均衡机制？


# RpcServer
1、借助接口ApplicationContextAware，在spring启动时，通过自定义注解RpcService获得各个接口的实现类；(每个接口可以设置版本号)
2、借助接口InitializingBean，在实例化bean后启动start，开启服务提供者监听接口，等待消费端的请求；
3、在start函数的最后会注册服务提供者的IP和端口到zookeeper上，生成的永久节点为接口的全路径，临时节点服务提供者的IP:port



# RpcHandler
1、接收消费端的请求，通过线程池来处理请求，每个请求开启一个线程进行处理；
2、这里通过消费端传递过来的RpcRequest，通过解码获得请求的类接口、方法、参数类型、参数等信息；
3、然后通过请求接口查找注册的接口和实现类的hashmap，找到对应接口的实现类，然后通过反射调用具体的方法；
4、最后封装返回结果为RpcResponse；


# RpcProxy
1、消费端通过它封装接口的调用通过动态代理调用远程的服务；
2、通过zookeeper的服务发现，获得接口的服务提供者地址；
3、这里封装请求信息，根据获得的服务提供者的地址把请求信息通过RpcClient发送远程服务端；

# RpcClient
发送netty请求

