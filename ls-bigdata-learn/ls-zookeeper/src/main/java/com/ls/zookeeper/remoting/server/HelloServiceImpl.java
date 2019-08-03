package com.ls.zookeeper.remoting.server;
 



import com.ls.zookeeper.remoting.common.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
 
    protected HelloServiceImpl() throws RemoteException {
    }
 

    public String sayHello(String name) throws RemoteException {
        return String.format("Hello %s", name);
    }
}