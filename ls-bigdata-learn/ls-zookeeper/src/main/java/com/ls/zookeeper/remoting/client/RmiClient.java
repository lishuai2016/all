package com.ls.zookeeper.remoting.client;




import com.ls.zookeeper.remoting.common.HelloService;

import java.rmi.Naming;

public class RmiClient {

    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost:1099/demo.zookeeper.remoting.server.HelloServiceImpl";
        HelloService helloService = (HelloService) Naming.lookup(url);
        String result = helloService.sayHello("xidada");
        System.out.println(result);
    }
}