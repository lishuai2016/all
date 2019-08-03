package com.ls.zookeeper.lock;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Lock {
    private String path;
    private ZooKeeper zooKeeper;
    
    public Lock(String path){
        this.path = path;
    }
     
    /**
     * 方法描述: 上锁 lock it
     */
    public synchronized void lock() throws Exception{
        Stat stat = zooKeeper.exists(path, true);
        String data = InetAddress.getLocalHost().getHostAddress()+":lock";
        zooKeeper.setData(path, data.getBytes(), stat.getVersion());
    }
     
    /**
     * 方法描述：开锁 unlock it
     */
    public synchronized void unLock() throws Exception{
        Stat stat = zooKeeper.exists(path, true);
        String data = InetAddress.getLocalHost().getHostAddress()+":unlock";
        zooKeeper.setData(path, data.getBytes(), stat.getVersion());
    }
     
    /**
     * 方法描述：是否锁住了, isLocked?
     */
    public synchronized boolean isLock(){
        try {
            Stat stat = zooKeeper.exists(path, true);
            String data = InetAddress.getLocalHost().getHostAddress()+":lock";
            String nodeData = new String(zooKeeper.getData(path, true, stat));
            System.out.println(data);
            System.out.println(nodeData);
            if(data.equals(nodeData)){
                return true;
            }
        } catch (UnknownHostException e) {
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        }
        return false;
    }
 
    public String getPath() {
        return path;
    }
 
    public void setPath(String path) {
        this.path = path;
    }
 
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
     
     
}