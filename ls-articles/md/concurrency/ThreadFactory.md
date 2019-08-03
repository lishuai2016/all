---
title: ThreadFactory
categories:
- concurrent
tags:
---


# 源码解读
ThreadFactory就是一个线程工厂。用来创建线程。这里为什么要使用线程工厂呢？其实就是为了统一在创建线程时设置一些参数，如是否守护线程。线程一些特性等，如优先级。通过这个TreadFactory创建出来的线程能保证有相同的特性。
下面是它的源码，它首先是一个接口类，而且方法只有一个。就是创建一个线程。

```java
public interface ThreadFactory {  
     Thread newThread(Runnable r);  
}  
```
我们都是继承它然后自己来写这个线程工厂的。下面的代码中在类Executors当中。默认的 我们创建线程池时使用的就是这个线程工厂
```java
static class DefaultThreadFactory implements ThreadFactory {  
	     private static final AtomicInteger poolNumber = new AtomicInteger(1);//原子类，线程池编号  
	     private final ThreadGroup group;//线程组  
	     private final AtomicInteger threadNumber = new AtomicInteger(1);//线程数目  
	     private final String namePrefix;//为每个创建的线程添加的前缀  
	   
	     DefaultThreadFactory() {  
	         SecurityManager s = System.getSecurityManager();  
	         group = (s != null) ? s.getThreadGroup() :  
	                               Thread.currentThread().getThreadGroup();//取得线程组  
	         namePrefix = "pool-" +  
	                       poolNumber.getAndIncrement() +  
	                      "-thread-";  
	     }  
	   
	     public Thread newThread(Runnable r) {  
	         Thread t = new Thread(group, r,  
	                               namePrefix + threadNumber.getAndIncrement(),  
	                               0);//真正创建线程的地方，设置了线程的线程组及线程名  
	         if (t.isDaemon())  
	             t.setDaemon(false);  
	         if (t.getPriority() != Thread.NORM_PRIORITY)//默认是正常优先级  
	             t.setPriority(Thread.NORM_PRIORITY);  
	         return t;  
	     }  
	 }  
```
在上面的代码中，可以看到线程池中默认的线程工厂实现是很简单的，它做的事就是统一给线程池中的线程设置线程group、统一的线程前缀名。以及统一的优先级。





