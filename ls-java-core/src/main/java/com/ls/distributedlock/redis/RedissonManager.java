package com.ls.distributedlock.redis;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/12 15:08
在初始化数据时候，最好不要使用static{} 即静态块。因为在多核机器的情况下读取配置文件，
会抛出java.lang.NoClassDefFoundError: Could not initialize class XXX。
所以最好还是使用init的方式，在启动程序的时候手动执行下。
 */
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.config.Config;

public class RedissonManager {

    private static final String RAtomicName = "genId_";

    private static Config config = new Config();
    private static Redisson redisson = null;

    public static void init(){
        try {
//            config.useClusterServers() //这是用的集群server
//                    .setScanInterval(2000) //设置集群状态扫描时间
//                    .setMasterConnectionPoolSize(10000) //设置连接数
//                    .setSlaveConnectionPoolSize(10000)
//                    .addNodeAddress("127.0.0.1:6379","127.0.0.1:6380");
            //单机模式
            config.useSingleServer().setAddress("redis://127.0.0.1:6379"); //单机模式不加redis://，会解析不了
            redisson = (Redisson)Redisson.create(config);
            //清空自增的ID数字
            RAtomicLong atomicLong = redisson.getAtomicLong(RAtomicName);
            atomicLong.set(1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Redisson getRedisson(){
        return redisson;
    }

    /** 获取redis中的原子ID */
    public static Long nextID(){
        RAtomicLong atomicLong = getRedisson().getAtomicLong(RAtomicName);
        atomicLong.incrementAndGet();
        return atomicLong.get();
    }
}
