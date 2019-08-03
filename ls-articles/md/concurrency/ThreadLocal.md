---
title: ThreadLocal解析
categories:
- concurrent
tags:
---



http://www.cnblogs.com/dreamroute/p/5034726.html

ThreadLocal翻译成中文比较准确的叫法应该是：线程局部变量。

　　这个玩意有什么用处，或者说为什么要有这么一个东东？先解释一下，在并发编程的时候，成员变量如果不做任何处理其实是线程不安全的，各个线程都在操作同一个变量，显然是不行的，并且我们也知道volatile这个关键字也是不能保证线程安全的。那么在有一种情况之下，我们需要满足这样一个条件：变量是同一个，但是每个线程都使用同一个初始值，也就是使用同一个变量的一个新的副本。这种情况之下ThreadLocal就非常使用，比如说DAO的数据库连接，我们知道DAO是单例的，那么他的属性Connection就不是一个线程安全的变量。而我们每个线程都需要使用他，并且各自使用各自的。这种情况，ThreadLocal就比较好的解决了这个问题。

　　我们从源码的角度来分析这个问题。

　　首先定义一个ThreadLocal：

复制代码
public final class ConnectionUtil {

    private ConnectionUtil() {}

    private static final ThreadLocal<Connection> conn = new ThreadLocal<>();

    public static Connection getConn() {
        Connection con = conn.get();
        if (con == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("url", "userName", "password");
                conn.set(con);
            } catch (ClassNotFoundException | SQLException e) {
                // ...
            }
        }
        return con;
    }

}
复制代码
 

　　这样子，都是用同一个连接，但是每个连接都是新的，是同一个连接的副本。

　　那么实现机制是如何的呢？

　　1、每个Thread对象内部都维护了一个ThreadLocalMap这样一个ThreadLocal的Map，可以存放若干个ThreadLocal。
/* ThreadLocal values pertaining to this thread. This map is maintained
 * by the ThreadLocal class. */
ThreadLocal.ThreadLocalMap threadLocals = null;
　　2、当我们在调用get()方法的时候，先获取当前线程，然后获取到当前线程的ThreadLocalMap对象，如果非空，那么取出ThreadLocal的value，否则进行初始化，初始化就是将initialValue的值set到ThreadLocal中。

public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null)
            return (T)e.value;
    }
    return setInitialValue();
}
　　3、当我们调用set()方法的时候，很常规，就是将值设置进ThreadLocal中。

　　4、总结：当我们调用get方法的时候，其实每个当前线程中都有一个ThreadLocal。每次获取或者设置都是对该ThreadLocal进行的操作，是与其他线程分开的。

　　5、应用场景：当很多线程需要多次使用同一个对象，并且需要该对象具有相同初始化值的时候最适合使用ThreadLocal。

　　6、其实说再多也不如看一下源码来得清晰。如果要看源码，其中涉及到一个WeakReference和一个Map，这两个地方需要了解下，这两个东西分别是a.Java的弱引用，也就是GC的时候会销毁该引用所包裹(引用)的对象，这个threadLocal作为key可能被销毁，但是只要我们定义成他的类不卸载，tl这个强引用就始终引用着这个ThreadLocal的，永远不会被gc掉。b.和HashMap差不多。

　　事实上，从本质来讲，就是每个线程都维护了一个map，而这个map的key就是threadLocal，而值就是我们set的那个值，每次线程在get的时候，都从自己的变量中取值，既然从自己的变量中取值，那肯定就不存在线程安全问题，总体来讲，ThreadLocal这个变量的状态根本没有发生变化，他仅仅是充当一个key的角色，另外提供给每一个线程一个初始值。如果允许的话，我们自己就能实现一个这样的功能，只不过恰好JDK就已经帮我们做了这个事情。




参考：https://blog.csdn.net/u012706811/article/details/53231598

 private static Map<String,ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
这个map中的key对应的value只应该有一个。

测试类：com.ls.learn.threadlocal.DateUtil

日志分析
put new sdf of pattern MMddHHmmssSSS to map
thread: Thread[main,5,main] init pattern: MMddHHmmssSSS
thread: Thread[Thread-0,5,main] init pattern: MMddHHmmssSSS
thread: Thread[Thread-1,5,main] init pattern: MMddHHmmssSSS

分析
可以看出来sdfMap put进去了一次,而SimpleDateFormat被new了三次,因为代码中有三个线程.那么这是为什么呢?
对于每一个线程Thread,其内部有一个ThreadLocal.ThreadLocalMap threadLocals的全局变量引用,
ThreadLocal.ThreadLocalMap里面有一个保存该ThreadLocal和对应value,一图胜千言,结构图如下:



那么对于sdfMap的话,结构图就变更了下



那么日志为什么是这样的?分析下:

1.首先第一次执行DateUtil.formatDate(new Date(),MDHMSS);
//第一次执行DateUtil.formatDate(new Date(),MDHMSS)分析
    private static SimpleDateFormat getSdf(final String pattern){
        ThreadLocal<SimpleDateFormat> sdfThread = sdfMap.get(pattern);
        //得到的sdfThread为null,进入if语句
        if (sdfThread == null){
            synchronized (DateUtil.class){
                sdfThread = sdfMap.get(pattern);
                //sdfThread仍然为null,进入if语句
                if (sdfThread == null){
                    //打印日志
                    logger.debug("put new sdf of pattern " + pattern + " to map");
                    //创建ThreadLocal实例,并覆盖initialValue方法
                    sdfThread = new ThreadLocal<SimpleDateFormat>(){
                        @Override
                        protected SimpleDateFormat initialValue() {
                            logger.debug("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    //设置进如sdfMap
                    sdfMap.put(pattern,sdfThread);
                }
            }
        }
        return sdfThread.get();
    }

这个时候可能有人会问,这里并没有调用ThreadLocal的set方法,那么值是怎么设置进入的呢? 
这就需要看sdfThread.get()的实现:

    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }

也就是说当值不存在的时候会调用setInitialValue()方法,该方法会调用initialValue()方法,也就是我们覆盖的方法.

对应日志打印.

put new sdf of pattern MMddHHmmssSSS to map
thread: Thread[main,5,main] init pattern: MMddHHmmssSSS

2.第二次在子线程执行DateUtil.formatDate(new Date(),MDHMSS);
//第二次在子线程执行`DateUtil.formatDate(new Date(),MDHMSS);`
    private static SimpleDateFormat getSdf(final String pattern){
        ThreadLocal<SimpleDateFormat> sdfThread = sdfMap.get(pattern);
        //这里得到的sdfThread不为null,跳过if块（？？？？？？）
        if (sdfThread == null){
            synchronized (DateUtil.class){
                sdfThread = sdfMap.get(pattern);
                if (sdfThread == null){
                    logger.debug("put new sdf of pattern " + pattern + " to map");
                    sdfThread = new ThreadLocal<SimpleDateFormat>(){
                        @Override
                        protected SimpleDateFormat initialValue() {
                            logger.debug("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern,sdfThread);
                }
            }
        }
        //直接调用sdfThread.get()返回
        return sdfThread.get();
    }


分析sdfThread.get()

//第二次在子线程执行`DateUtil.formatDate(new Date(),MDHMSS);`
    public T get() {
        Thread t = Thread.currentThread();//得到当前子线程
        ThreadLocalMap map = getMap(t);
        //子线程中得到的map为null,跳过if块
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        //直接执行初始化,也就是调用我们覆盖的initialValue()方法
        return setInitialValue();
    }

对应日志:

Thread[Thread-0,5,main] init pattern: MMddHHmmssSSS
同理第三次执行和第二次类似.直接调用sdfThread.get(),然后调用initialValue()方法,对应日志

Thread[Thread-1,5,main] init pattern: MMddHHmmssSSS

总结
在什么场景下比较适合使用ThreadLocal？stackoverflow上有人给出了还不错的回答。 
When and how should I use a ThreadLocal variable? 
One possible (and common) use is when you have some object that is not thread-safe, but you want to avoid synchronizing access to that 
object (I’m looking at you, SimpleDateFormat). Instead, give each thread its own instance of the object.



