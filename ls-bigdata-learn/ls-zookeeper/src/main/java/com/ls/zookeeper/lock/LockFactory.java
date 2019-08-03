package com.ls.zookeeper.lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.util.Collections;

public class LockFactory {
     
    public static final ZooKeeper DEFAULT_ZOOKEEPER = getDefaultZookeeper();
    
    //data格式:  ip:stat  如: 192.168.1.107:lock   or    192.168.1.107:unlock
    
    public static synchronized Lock getLock(String path,String ip) throws Exception{
        if(DEFAULT_ZOOKEEPER != null){
            Stat stat = null;
            try{
                stat = DEFAULT_ZOOKEEPER.exists(path, true);
            }catch (Exception e) {
                // TODO: use log system and throw new exception
            }
            if(stat!=null){
                byte[] data = DEFAULT_ZOOKEEPER.getData(path, null, stat);
                String dataStr = new String(data);
                String[] ipv = dataStr.split(":");
                if(ip.equals(ipv[0])){
                    Lock lock = new Lock(path);
                    lock.setZooKeeper(DEFAULT_ZOOKEEPER);
                    return lock;
                }
                //is not your lock, return null
                else{
                    return null;
                }
            }
            //no lock created yet, you can get it
            else{
                createZnode(path);
                Lock lock = new Lock(path);
                lock.setZooKeeper(DEFAULT_ZOOKEEPER);
                return lock;
            }
        }
        return null;
    }
     
    private static ZooKeeper getDefaultZookeeper() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.80.201:2181", 3000, new Watcher(){
                public void process(WatchedEvent event) {
                  System.out.println("event: " + event.getType());
                }
            });
            // 如果是确保zookeeper连接正常再往下进行,如果刚刚启动zk可能需要等一段时间
            while (zooKeeper.getState() != ZooKeeper.States.CONNECTED) {
    			Thread.sleep(3000);
    		}
            return zooKeeper;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    private static void createZnode(String path) throws Exception{
         
        if(DEFAULT_ZOOKEEPER!=null){
            InetAddress address = InetAddress.getLocalHost();
            String data = address.getHostAddress()+":unlock";
            DEFAULT_ZOOKEEPER.create(path, data.getBytes(),Collections.singletonList(new ACL(Perms.ALL, Ids.ANYONE_ID_UNSAFE)) , CreateMode.EPHEMERAL);
        }
    }
}