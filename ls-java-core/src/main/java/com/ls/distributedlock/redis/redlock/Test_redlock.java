package com.ls.distributedlock.redis.redlock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-09 15:29
 */
public class Test_redlock {

    void init() {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("127.0.0.1:6369", "127.0.0.1:6379", "127.0.0.1:6389")
                .setMasterName("masterName")
                .setPassword("pwd")
                .setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        RLock redLock = redissonClient.getLock("REDLOCK_KEY");
        boolean isLock;

        try {
            // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
            isLock = redLock.tryLock(500,1000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redLock.unlock();
            // 无论如何, 最后都要解锁
        }


    }

}
