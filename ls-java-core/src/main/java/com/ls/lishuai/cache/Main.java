package com.ls.lishuai.cache;

import com.google.common.cache.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/8 15:21
 *
 *
 * Guava Cache有两种创建方式：

　　1. cacheLoader
　　2. callable callback

　　通过这两种方法创建的cache，和通常用map来缓存的做法比，不同在于，这两种方法都实现了一种逻辑——从缓存中取key X的值，
如果该值已经缓存过了，则返回缓存中的值，如果没有缓存过，可以通过某个方法来获取这个值。
但不同的在于cacheloader的定义比较宽泛，是针对整个cache定义的，可以认为是统一的根据key值load value的方法。
而callable的方式较为灵活，允许你在get的时候指定。
 *
 *
 *
 *
 *
 *
 * 看看到在20此循环中命中次数是17次，未命中3次，这是因为我们设定缓存的过期时间是写入后的8秒，所以20秒内会失效两次，另外第一次获取时缓存中也是没有值的，所以才会未命中3次，其他则命中。

guava的内存缓存非常强大，可以设置各种选项，而且很轻量，使用方便。另外还提供了下面一些方法，来方便各种需要：

ImmutableMap<K, V> getAllPresent(Iterable<?> keys) 一次获得多个键的缓存值
put和putAll方法向缓存中添加一个或者多个缓存项
invalidate 和 invalidateAll方法从缓存中移除缓存项
asMap()方法获得缓存数据的ConcurrentMap<K, V>快照
cleanUp()清空缓存
refresh(Key) 刷新缓存，即重新取缓存数据，更新缓存
 *
 *
 *
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        //缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
        LoadingCache<Integer,Student> studentCache
                //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
                = CacheBuilder.newBuilder()
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                //设置写缓存后8秒钟过期
                .expireAfterWrite(8, TimeUnit.SECONDS)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                    }
                })
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(
                        new CacheLoader<Integer, Student>() {
                            @Override
                            public Student load(Integer key) throws Exception {
                                System.out.println("load student " + key);
                                Student student = new Student();
                                student.setId(key);
                                student.setName("name " + key);
                                return student;
                            }
                        }
                );

        for (int i=0;i<20;i++) {
            //从缓存中得到数据，由于我们没有设置过缓存，所以需要通过CacheLoader加载缓存数据
            Student student = studentCache.get(1);
            System.out.println(student);
            //休眠1秒
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("cache stats:");
        //最后打印缓存的命中率等 情况
        System.out.println(studentCache.stats().toString());


//callable callback的实现：
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
        String resultVal = cache.get("jerry", new Callable<String>() {
            public String call() {
                String strProValue="hello "+"jerry"+"!";
                return strProValue;
            }
        });
        System.out.println("jerry value : " + resultVal);

        resultVal = cache.get("peida", new Callable<String>() {
            public String call() {
                String strProValue="hello "+"peida"+"!";
                return strProValue;
            }
        });
        System.out.println("peida value : " + resultVal);


    }
}
