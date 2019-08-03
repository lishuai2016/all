package com.ls.zookeeper.lockjk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AbstractZooKeeper implements Watcher {
    protected ZooKeeper zooKeeper;
    protected CountDownLatch countDownLatch = new CountDownLatch(1);  

    public ZooKeeper connect(String hosts, int SESSION_TIMEOUT) throws IOException, InterruptedException{
        zooKeeper = new ZooKeeper(hosts,SESSION_TIMEOUT,this);
        countDownLatch.await();  
        System.out.println("AbstractZooKeeper.connect()");
        return zooKeeper;
    }  
    
    public void process(WatchedEvent event) {
        if(event.getState() == KeeperState.SyncConnected){
            countDownLatch.countDown();  
        }  
    }  
    public void close() throws InterruptedException{  
        zooKeeper.close();  
    }  
}  