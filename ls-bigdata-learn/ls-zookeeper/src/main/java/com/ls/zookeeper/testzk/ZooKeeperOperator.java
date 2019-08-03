package com.ls.zookeeper.testzk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.Arrays;
import java.util.List;

public class ZooKeeperOperator extends AbstractZooKeeper {
    /**
     * 创建持久态的znode,比支持多层创建.比如在创建/parent/child的情况下,无/parent.无法通过. 
     * @param path eg:  /parent/child1 
     * @param data
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void create(String path,byte[] data) throws KeeperException, InterruptedException{
        this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT/*此处创建的为持久态的节点,可为瞬态*/);
    }


    /**
     * 获取节点的孩子信息 
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void getChild(String path) throws KeeperException, InterruptedException{
        try {
            List<String> children = this.zooKeeper.getChildren(path, false);
            if (children.isEmpty()) {
                System.out.printf("没有节点在%s中.", path);
                return;
            }else{
                System.out.printf("节点%s中存在的节点:\n", path);
                for(String child: children){
                    System.out.println(child);
                }
            }
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("%s节点不存在.", path);
            throw e;
        }
    }

    public byte[] getData(String path) throws KeeperException, InterruptedException {
        return  this.zooKeeper.getData(path, false,null);
    }


    public static void main(String[] args) {
        try {
            ZooKeeperOperator zkoperator             = new ZooKeeperOperator();
            zkoperator.connect("192.168.1.201");
            byte[] data = new byte[]{'d','a','t','a'};

            zkoperator.create("/root",null);
            System.out.println(Arrays.toString(zkoperator.getData("/root")));

            zkoperator.create("/root/child1",data);
            System.out.println(Arrays.toString(zkoperator.getData("/root/child1")));

            zkoperator.create("/root/child2",data);
            System.out.println(Arrays.toString(zkoperator.getData("/root/child2")));

            System.out.println("节点孩子信息:");
            zkoperator.getChild("/root");

            zkoperator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  