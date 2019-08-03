package com.ls.distributedlock.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/12 15:07
 */
public class DistributedRedisLock {
    private static Redisson redisson = RedissonManager.getRedisson();
    private static final String LOCK_TITLE = "redisLock_";

    public static boolean acquire(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock mylock = redisson.getLock(key);
        mylock.lock(2, TimeUnit.MINUTES); //lock提供带timeout参数，timeout结束强制解锁，防止死锁
        System.err.println("======lock======"+Thread.currentThread().getName());
        return  true;
    }

    public static void release(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock mylock = redisson.getLock(key);
        mylock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }
}
