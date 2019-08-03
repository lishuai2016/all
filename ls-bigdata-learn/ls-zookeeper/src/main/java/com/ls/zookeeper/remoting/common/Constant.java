package com.ls.zookeeper.remoting.common;
 
public interface Constant {
 
    String ZK_CONNECTION_STRING = "192.168.80.201:2181,192.168.80.202:2181,192.168.80.203:2181";
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";
}