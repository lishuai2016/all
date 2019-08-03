package com.ls.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-04-27 22:29
 */
public class DistributedRedisLock {
    private static RedissonClient redisson = RedissonManager.getRedisson();
    private static final String LOCK_TITLE = "redisLock_";

    public static void acquire(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock mylock = redisson.getLock(key);
        mylock.lock(2, TimeUnit.MINUTES); //lock提供带timeout参数，timeout结束强制解锁，防止死锁
        System.err.println("======lock======"+Thread.currentThread().getName());
    }

    public static void release(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock mylock = redisson.getLock(key);
        mylock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }
}
