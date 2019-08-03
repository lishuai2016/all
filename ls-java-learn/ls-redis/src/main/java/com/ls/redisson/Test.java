package com.ls.redisson;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-04-27 22:23
 */
public class Test {

    public static void main(String[] args) {
        System.out.println();
        redisLock();
    }

    private static void redisLock(){
        RedissonManager.init(); //初始化
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = "test123";
                        DistributedRedisLock.acquire(key);
                        Thread.sleep(1000); //获得锁之后可以进行相应的处理
                        System.err.println("======获得锁后进行相应的操作======");
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
