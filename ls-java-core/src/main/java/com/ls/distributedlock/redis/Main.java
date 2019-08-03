package com.ls.distributedlock.redis;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/12 15:13
 *
 *
======lock======Thread-5
======获得锁后进行相应的操作======Thread-5
======unlock======Thread-5
=============================
======lock======Thread-3
======获得锁后进行相应的操作======Thread-3
======unlock======Thread-3
=============================
======lock======Thread-4
======获得锁后进行相应的操作======Thread-4
======unlock======Thread-4
=============================
======lock======Thread-9
======获得锁后进行相应的操作======Thread-9
======unlock======Thread-9
=============================
======lock======Thread-8
======获得锁后进行相应的操作======Thread-8
======unlock======Thread-8
=============================
======lock======Thread-7
======获得锁后进行相应的操作======Thread-7
======unlock======Thread-7
=============================
======lock======Thread-1
======获得锁后进行相应的操作======Thread-1
======unlock======Thread-1
=============================
======lock======Thread-2
======获得锁后进行相应的操作======Thread-2
======unlock======Thread-2
=============================
======lock======Thread-10
======获得锁后进行相应的操作======Thread-10
======unlock======Thread-10
=============================
======lock======Thread-6
======获得锁后进行相应的操作======Thread-6
======unlock======Thread-6
=============================
 */
public class Main {
    public static void main(String[] args) {
        redisLock();
    }

    private static void redisLock(){
        RedissonManager.init(); //初始化
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = "test123";
                        DistributedRedisLock.acquire(key);
                        Thread.sleep(1000); //获得锁之后可以进行相应的处理
                        System.err.println("======获得锁后进行相应的操作======"+Thread.currentThread().getName());
                        DistributedRedisLock.release(key);
                        System.err.println("=============================");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
}
