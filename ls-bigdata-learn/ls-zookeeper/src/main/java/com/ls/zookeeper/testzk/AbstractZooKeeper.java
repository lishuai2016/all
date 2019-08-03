package com.ls.zookeeper.testzk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AbstractZooKeeper implements Watcher {
    private static final int SESSION_TIME   = 2000;  
    protected ZooKeeper zooKeeper;
    protected CountDownLatch countDownLatch = new CountDownLatch(1);  

    public void connect(String hosts) throws IOException, InterruptedException{  
        zooKeeper = new ZooKeeper(hosts,SESSION_TIME,this);
        countDownLatch.await();  
        System.out.println("AbstractZooKeeper.connect()");
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