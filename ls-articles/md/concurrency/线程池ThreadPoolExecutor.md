---
title: 线程池ThreadPoolExecutor
categories: 
- concurrent
tags:
---

# 问题
1、提交到线程池的线程何时被执行？
2、线程池的状态？
3、线程的状态？
4、为什么线程池在线程运行都结束了而阻塞？即线程池不关闭


# 线程池的好处
线程是稀缺资源，如果被无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，合理的使用线程池对线程进行统一分配、调优和监控，有以下好处：
1、降低资源消耗；通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
2、提高响应速度；当任务到达时，任务可以不需要的等到线程创建就能立即执行。
3、提高线程的可管理性。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。


# 线程池常见的创建方式
Java1.5中引入的Executor框架把任务的提交和执行进行解耦，只需要定义好任务，
然后提交给线程池，而不用关心该任务是如何执行、被哪个线程执行，以及什么时候执行。
通过工厂类Executors的静态方法newFixedThreadPool、newCachedThreadPool、newSingleThreadExecutor可以构造线程池类ExecutorService其底层都是调用new ThreadPoolExecutor来实现的。

a、newFixedThreadPool 创建一个固定长度的线程池，当到达线程最大数量时，线程池的规模将不再变化。
b、newCachedThreadPool 创建一个可缓存的线程池，如果当前线程池的规模超出了处理需求，将回收空的线程；当需求增加时，会增加线程数量；线程池规模无限制。
c、newSingleThreadPoolExecutor 创建一个单线程的Executor，确保任务对了，串行执行
d、newScheduledThreadPool 创建一个固定长度的线程池，而且以延迟或者定时的方式来执行，类似Timer；
小结一下：在线程池中执行任务比为每个任务分配一个线程优势更多，通过重用现有的线程而不是创建新线程，可以在处理多个请求时分摊线程创建和销毁产生的巨大的开销。当请求到达时，通常工作线程已经存在，提高了响应性；通过配置线程池的大小，可以创建足够多的线程使CPU达到忙碌状态，还可以防止线程太多耗尽计算机的资源。


其实看这三种方式创建的源码就会发现，实际上还是利用 ThreadPoolExecutor 类实现的。

ThreadPoolExecutor(int corePoolSize, 
                    int maximumPoolSize, 
                    long keepAliveTime,
                     TimeUnit unit, 
                     BlockingQueue<Runnable> workQueue,
                     ThreadFactory threadFactory,
                      RejectedExecutionHandler handler)
这几个核心参数的作用：
corePoolSize 为线程池的基本大小。
maximumPoolSize 为线程池最大线程大小。
keepAliveTime 和 unit 则是线程空闲后的存活时间。线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
workQueue 用于存放任务的阻塞队列。
threadFactory 新建线程工厂（一般可以定制线程的名称用来区分不同的线程池）
handler 当队列和最大线程池都满了之后的饱和策略。（自定义线程池满了的时候抛出定义的异常便于排查问题）


## workQueue
  
用来保存等待被执行的任务的阻塞队列，且任务必须实现Runable接口，在JDK中提供了如下阻塞队列：
1、ArrayBlockingQueue：基于数组结构的有界阻塞队列，按FIFO排序任务；
2、LinkedBlockingQuene：基于链表结构的阻塞队列，按FIFO排序任务，吞吐量通常要高于ArrayBlockingQuene；
3、SynchronousQuene：一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQuene；
4、priorityBlockingQuene：具有优先级的无界阻塞队列；

当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略，通常有以下四种策略
ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。  
ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。  
ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）  
ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务  

## threadFactory
线程池中默认的线程工厂实现是很简单的，它做的事就是统一给线程池中的线程设置线程group、统一的线程前缀名。以及统一的优先级
备注：每个线程都有一个优先级，高优先级线程的执行优先于低优先级线程。定义了优先级的同时，定义了优先级的合法范围，默认值为5，最大值为10，最小值为1

## 线程池中常用的方法
1、submit()[有返回值]
将线程放入线程池中，除了使用execute，也可以使用submit，它们两个的区别是一个使用有返回值，一个没有返回值。
submit的方法很适应于生产者-消费者模式，通过和Future结合一起使用，可以起到如果线程没有返回结果，就阻塞当前线程等待线程 池结果返回。
2、execute()
表示往线程池添加线程，有可能会立即运行，也有可能不会。无法预知线程何时开始，何时线束。   
3、shutdown()
通常放在execute后面。如果调用 了这个方法，一方面，表明当前线程池已不再接收新添加的线程，新添加的线程会被拒绝执行。另一方面，表明当所有线程执行完毕时，回收线程池的资源。注意，它不会马上关闭线程池！
4、shutdownNow()
不管当前有没有线程在执行，马上关闭线程池！这个方法要小心使用，要不可能会引起系统数据异常！

# 线程池源码类图
![](/images/juc.png)
![](/images/ThreadPoolExecutor类图.png)
![](/images/ThreadPoolExecutor继承结构图.jpg)

## newFixedThreadPool
一个固定线程数量的线程池

	 public static ExecutorService newFixedThreadPool(int nThreads) {  
	 //corePoolSize跟maximumPoolSize值一样，同时传入一个无界阻塞队列
         //根据上面分析的woker回收逻辑，该线程池的线程会维持在指定线程数，不会进行回收
	     return new ThreadPoolExecutor(nThreads, nThreads,  
	                                   0L, TimeUnit.MILLISECONDS,  
	                                   new LinkedBlockingQueue<Runnable>());  
	 }  
定长线程池：
- 可控制线程最大并发数（同时执行的线程数）
- 超出的线程会在队列中等待

## newCachedThreadPool
不固定线程数量，且支持最大为Integer.MAX_VALUE的线程数量

	 public static ExecutorService newCachedThreadPool() {  
	  //这个线程池corePoolSize为0，maximumPoolSize为Integer.MAX_VALUE
              //意思也就是说来一个任务就创建一个woker，回收时间是60s
	     return new ThreadPoolExecutor(0, Integer.MAX_VALUE,  
	                                   60L, TimeUnit.SECONDS,  
	                                   new SynchronousQueue<Runnable>());  
	 }  

可缓存线程池：
- 线程数无限制
- 有空闲线程则复用空闲线程，若无空闲线程则新建线程
- 一定程序减少频繁创建/销毁线程，减少系统开销

## newSingleThreadExecutor
可以理解为线程数量为1的FixedThreadPool:

	 public static ExecutorService newSingleThreadExecutor() {  
	//线程池中只有一个线程进行任务执行，其他的都放入阻塞队列
        //外面包装的FinalizableDelegatedExecutorService类实现了finalize方法，在JVM垃圾回收的时候会关闭线程池
	     return new FinalizableDelegatedExecutorService  
	         (new ThreadPoolExecutor(1, 1,  
	                                 0L, TimeUnit.MILLISECONDS,  
	                                 new LinkedBlockingQueue<Runnable>()));  
	 }  

单线程化的线程池：
- 有且仅有一个工作线程执行任务
- 所有任务按照指定顺序执行，即遵循队列的入队出队规则
	 
## newScheduledThreadPool
支持定时以指定周期循环执行任务:

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }

其中前三种线程池是ThreadPoolExecutor不同配置的实例，最后一种是ScheduledThreadPoolExecutor的实例。



可以发现，其实它们调用的都是同一个接口ThreadPoolExecutor方法，只不过传入参数不一样而已。下面就来看看这个神秘的ThreadPoolExecutor。
首先来看看它的一些基本参数：

    public class ThreadPoolExecutor extends AbstractExecutorService {
 
    //运行状态标志位
    volatile int runState;
    static final int RUNNING    = 0;
    static final int SHUTDOWN   = 1;
    static final int STOP       = 2;
    static final int TERMINATED = 3;
 
    //线程缓冲队列，当线程池线程运行超过一定线程时并满足一定的条件，待运行的线程会放入到这个队列
    private final BlockingQueue<Runnable> workQueue;
    //重入锁，更新核心线程池大小、最大线程池大小时要加锁
    private final ReentrantLock mainLock = new ReentrantLock();
    //重入锁状态
    private final Condition termination = mainLock.newCondition();
    //工作都set集合
    private final HashSet<Worker> workers = new HashSet<Worker>();
    //线程执行完成后在线程池中的缓存时间
    private volatile long  keepAliveTime;
    //核心线程池大小 
    private volatile int   corePoolSize;
    //最大线程池大小 
    private volatile int   maximumPoolSize;
    //当前线程池在运行线程大小 
    private volatile int   poolSize;
    //当缓冲队列也放不下线程时的拒绝策略
    private volatile RejectedExecutionHandler handler;
    //线程工厂，用来创建线程
    private volatile ThreadFactory threadFactory;   
    //用来记录线程池中曾经出现过的最大线程数
    private int largestPoolSize;   
    //用来记录已经执行完毕的任务个数
    private long completedTaskCount;   
 
    ................
    }
    
其有多个构造函数，从源码中可以看到其实最终都是调用了以下的方法：

    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
这里很简单，就是设置一下各个参数，并校验参数是否正确，然后抛出对应的异常。


# 线程池的执行过程
通常我们都是使用:
threadPool.execute(new Job());
这样的方式来提交一个任务到线程池中，所以核心的逻辑就是 execute() 函数了。
在具体分析之前先了解下线程池中所定义的状态，这些状态都和线程的执行密切相关：
## ThreadPoolExecutor的线程池的状态变量
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));   
private static final int COUNT_BITS = Integer.SIZE - 3;
private static final int CAPACITY   = (1 << COUNT_BITS) - 1;   //最大的线程数

// runState is stored in the high-order bits
private static final int RUNNING    = -1 << COUNT_BITS;
private static final int SHUTDOWN   =  0 << COUNT_BITS;
private static final int STOP       =  1 << COUNT_BITS;
private static final int TIDYING    =  2 << COUNT_BITS;
private static final int TERMINATED =  3 << COUNT_BITS;

// Packing and unpacking ctl
private static int runStateOf(int c)     { return c & ~CAPACITY; }
private static int workerCountOf(int c)  { return c & CAPACITY; }
private static int ctlOf(int rs, int wc) { return rs | wc; }

其中ctl是ThreadPoolExecutor的同步状态变量。
workerCountOf()方法取得当前线程池的线程数量，算法是将ctl的值取低29位。[AtomicInteger]
runStateOf()方法取得线程池的状态，算法是将ctl的值取高3位:

RUNNING 111 表示正在运行，指可以接受任务执行队列里的任务
SHUTDOWN 000 表示拒绝接收新的任务,指调用了 shutdown() 方法，不再接受新任务了，但是队列里的任务得执行完毕。
STOP 001 指调用了 shutdownNow() 方法，不再接受新任务，同时抛弃阻塞队列里的所有任务并中断所有正在执行任务。
TIDYING 010 所有任务都执行完毕，在调用 shutdown()/shutdownNow() 中都会尝试更新为这个状态。
TERMINATED 011 表示已执行完terminated()方法。
当我们向线程池提交任务时，通常使用execute()方法，接下来就先从该方法开始分析。


各个状态之间的转化关系：
![线程池状态转化](/images/线程池状态转化.png)

## execute(Runnable command)
在分析execute代码之前，需要先说明下，我们都知道线程池是维护了一批线程来处理用户提交的任务，达到线程复用的目的，
线程池维护的这批线程被封装成了Worker。然后看看 execute() 方法是如何处理的：

    public void execute(Runnable command) {
            if (command == null)
                throw new NullPointerException();
            /*
             * Proceed in 3 steps:
             *
             * 1. If fewer than corePoolSize threads are running, try to
             * start a new thread with the given command as its first
             * task.  The call to addWorker atomically checks runState and
             * workerCount, and so prevents false alarms that would add
             * threads when it shouldn't, by returning false.
             *
             * 2. If a task can be successfully queued, then we still need
             * to double-check whether we should have added a thread
             * (because existing ones died since last checking) or that
             * the pool shut down since entry into this method. So we
             * recheck state and if necessary roll back the enqueuing if
             * stopped, or start a new thread if there are none.
             *
             * 3. If we cannot queue task, then we try to add a new
             * thread.  If it fails, we know we are shut down or saturated
             * and so reject the task.
             */
            int c = ctl.get();
            //判断1
            if (workerCountOf(c) < corePoolSize) {
                if (addWorker(command, true))
                    return;
                c = ctl.get();
            }
            //判断2
            if (isRunning(c) && workQueue.offer(command)) {
                int recheck = ctl.get();
                if (! isRunning(recheck) && remove(command))
                    reject(command);
                else if (workerCountOf(recheck) == 0)
                    addWorker(null, false); //空线程没有执行任务
            }
             //判断3   false为非核心线程池
            else if (!addWorker(command, false))
                reject(command);
        }
**第一个判断**
workerCountOf方法根据ctl的低29位，得到线程池的当前线程数，如果线程数小于corePoolSize，则执行addWorker方法创建新的线程执行任务；

**第二个判断**
判断线程池是否在运行，如果在，任务队列是否允许插入阻塞队列，插入成功再次验证线程池是否运行，如果不在运行，移除插入的任务，然后抛出拒绝策略。
如果在运行，没有线程了，就启用一个线程。

**第三个判断**
如果添加非核心线程失败，就直接拒绝了。 

![](/images/线程池中的线程创建流程1.png)
回顾FixedThreadPool，因为它配置的corePoolSize与maximumPoolSize相等，所以不会执行到情况3，
并且因为workQueue为默认的LinkedBlockingQueue，其长度为Integer.MAX_VALUE，几乎不可能出现任务无法被添加到workQueue的情况，
所以FixedThreadPool的所有任务执行在核心线程中。

而CachedThreadPool的corePoolSize为0，表示它不会执行到情况1，因为它的maximumPoolSize为Integer.MAX_VALUE，所以几乎没有线程数量上限，
因为它的workQueue为SynchronousQueue，所以当线程池里没有闲置的线程SynchronousQueue就会添加任务失败，因此会执行到情况3添加新的线程执行任务。


## 添加worker线程addWorker(Runnable firstTask, boolean core)
        
    private boolean addWorker(Runnable firstTask, boolean core) {
            retry:
            for (;;) {
                int c = ctl.get();
                int rs = runStateOf(c);
                //使用CAS机制轮询线程池的状态，如果线程池处于SHUTDOWN及大于它的状态则拒绝执行任务
                // Check if queue empty only if necessary.
                //第1个判断
                if (rs >= SHUTDOWN &&
                    ! (rs == SHUTDOWN &&
                       firstTask == null &&
                       ! workQueue.isEmpty()))
                    return false;
                  //使用CAS机制尝试将当前线程数+1
                  //如果是核心线程，当前线程数必须小于corePoolSize 
                  //如果是非核心线程，则当前线程数必须小于maximumPoolSize
                  //如果当前线程数大于线程池支持的最大线程数CAPACITY 也会返回失败
                  //如下所示：core为true表示是核心线程，支持的最大线程格式CAPACITY
                
                // 第2个判断
                for (;;) {
                    int wc = workerCountOf(c);
                    if (wc >= CAPACITY ||
                        wc >= (core ? corePoolSize : maximumPoolSize))
                        return false;
                    if (compareAndIncrementWorkerCount(c))
                        break retry;
                    c = ctl.get();  // Re-read ctl
                    if (runStateOf(c) != rs)
                        continue retry;
                    // else CAS failed due to workerCount change; retry inner loop
                }
            }
    //这里已经成功执行了CAS操作将线程池数量+1，下面创建线程
            boolean workerStarted = false;
            boolean workerAdded = false;
            Worker w = null;
            try {
            //第3个判断
                final ReentrantLock mainLock = this.mainLock;
                //Worker内部有一个Thread，并且执行Worker的run方法，因为Worker实现了Runnable
                w = new Worker(firstTask);
                final Thread t = w.thread;
                if (t != null) {
                    mainLock.lock();
                     //第4个判断
                    try {
                        // Recheck while holding lock.
                        // Back out on ThreadFactory failure or if
                        // shut down before lock acquired.
                        int c = ctl.get();
                        int rs = runStateOf(c);
    
                        if (rs < SHUTDOWN ||
                            (rs == SHUTDOWN && firstTask == null)) {
                            if (t.isAlive()) // precheck that t is startable
                                throw new IllegalThreadStateException();
                            workers.add(w);//把新建的woker线程放入集合保存，这里使用的是HashSet
                            int s = workers.size();
                            if (s > largestPoolSize)
                                largestPoolSize = s;
                            workerAdded = true;
                        }
                    } finally {
                        mainLock.unlock();
                    }
                     //第5个判断
                    if (workerAdded) {
                        t.start();//如果添加成功则运行线程
                        workerStarted = true;
                    }
                }
            } finally {
                if (! workerStarted)
                    addWorkerFailed(w);
            }
            return workerStarted;
        }
addWorker这个方法先尝试在线程池运行状态为RUNNING并且线程数量未达上限的情况下通过CAS操作将线程池数量+1，
接着在ReentrantLock同步锁的同步保证下判断线程池为运行状态，然后把Worker添加到HashSet workers中。如果添加成功则执行Worker的内部线程。
 
**第1个判断**
做是否能够添加工作线程条件过滤
判断线程池的状态，如果线程池的状态值大于或等SHUTDOWN，则不处理提交的任务，直接返回；

**第2个判断**
做自旋，更新创建线程数量
通过参数core判断当前需要创建的线程是否为核心线程，如果core为true，且当前线程数小于corePoolSize，则跳出循环，开始创建新的线程
备注：有人或许会疑问 retry 是什么？这个是java中的goto语法。只能运用在break和continue后面。
      
**第3个判断**
获取线程池主锁。
线程池的工作线程通过Woker类实现，通过ReentrantLock锁保证线程安全。

**第4个判断**
添加线程到workers中（线程池中）。

**第5个判断**
启动新建的线程。


## Worker是什么
Worker是ThreadPoolExecutor的内部类，源码如下

    private final class Worker
        extends AbstractQueuedSynchronizer
        implements Runnable
    {
    /**
     * This class will never be serialized, but we provide a
     * serialVersionUID to suppress a javac warning.
     */
    private static final long serialVersionUID = 6138294804551838833L;

    /** Thread this worker is running in.  Null if factory fails. */
    final Thread thread;
    /** Initial task to run.  Possibly null. */
    Runnable firstTask;
    /** Per-thread task counter */
    volatile long completedTasks;

    /**
     * Creates with given first task and thread from ThreadFactory.
     * @param firstTask the first task (null if none)
     */
    Worker(Runnable firstTask) {
        setState(-1); // inhibit interrupts until runWorker
        this.firstTask = firstTask;
        this.thread = getThreadFactory().newThread(this);  //这是关键，内部线程把它自己赋值给它
    }

    /** Delegates main run loop to outer runWorker. */
    public void run() {
        runWorker(this);   //调用
    }

    // Lock methods
    //
    // The value 0 represents the unlocked state.
    // The value 1 represents the locked state.

    protected boolean isHeldExclusively() {
        return getState() != 0;
    }

    protected boolean tryAcquire(int unused) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    protected boolean tryRelease(int unused) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    public void lock()        { acquire(1); }
    public boolean tryLock()  { return tryAcquire(1); }
    public void unlock()      { release(1); }
    public boolean isLocked() { return isHeldExclusively(); }

    void interruptIfStarted() {
        Thread t;
        if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
            try {
                t.interrupt();
            } catch (SecurityException ignore) {
            }
        }
    }
}

Worker构造方法指定了第一个要执行的任务firstTask，并通过线程池的线程工厂创建线程。可以发现这个线程的参数为this，即Worker对象，
因为Worker实现了Runnable因此可以被当成任务执行，执行的即Worker实现的run方法：

    public void run() {
        runWorker(this);
    }


## 工作队列workers

    /**
     * Set containing all worker threads in pool. Accessed only when
     * holding mainLock.
     */
    private final HashSet<Worker> workers = new HashSet<Worker>();
我们看看workers是什么。一个hashSet。所以，线程池底层的存储结构其实就是一个HashSet

## runWorker()方法
因为Worker为ThreadPoolExecutor的内部类，因此runWorker方法实际是ThreadPoolExecutor定义的

    final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        w.unlock(); // allow interrupts  // 因为Worker的构造函数中setState(-1)禁止了中断，这里的unclock用于恢复中断
        boolean completedAbruptly = true;
        try {
            //第1个判断
             //一般情况下，task都不会为空，因此会直接进入循环体中
            while (task != null || (task = getTask()) != null) {
                w.lock();
                // If pool is stopping, ensure thread is interrupted;
                // if not, ensure thread is not interrupted.  This
                // requires a recheck in second case to deal with
                // shutdownNow race while clearing interrupt
                if ((runStateAtLeast(ctl.get(), STOP) ||
                     (Thread.interrupted() &&
                      runStateAtLeast(ctl.get(), STOP))) &&
                    !wt.isInterrupted())
                    wt.interrupt();
                try {
                //第2个判断
                    beforeExecute(wt, task);//该方法是个空的实现，如果有需要用户可以自己继承该类进行实现
                    Throwable thrown = null;
                    try {
                    //第3个判断
                        task.run();//真正的任务执行逻辑
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                    //第4个判断
                        afterExecute(task, thrown);//该方法是个空的实现，如果有需要用户可以自己继承该类进行实现
                    }
                } finally {
                    task = null;//这里设为null，也就是循环体再执行的时候会调用getTask方法
                    w.completedTasks++;
                    w.unlock();
                }
            }
            completedAbruptly = false;
        } finally {
        //当指定任务执行完成，阻塞队列中也取不到可执行任务时，会进入这里，做一些善后工作
                //比如在corePoolSize跟maximumPoolSize之间的woker会进行回收
            processWorkerExit(w, completedAbruptly);
        }
    }
这个方法是线程池复用线程的核心代码，注意Worker继承了AbstractQueuedSynchronizer，在执行每个任务前通过lock方法加锁，执行完后通过unlock方法解锁，
这种机制用来防止运行中的任务被中断。在执行任务时先尝试获取firstTask，即构造方法传入的Runnable对象，然后尝试从getTask方法中获取任务队列中的任务。
在任务执行前还要再次判断线程池是否已经处于STOP状态或者线程被中断。

在runWorker中，每一个Worker在getTask()成功之后都要获取Worker的锁之后运行，也就是说运行中的Worker不会中断。
因为核心线程一般在空闲的时候会一直阻塞在获取Task上，也只有中断才可能导致其退出。这些阻塞着的Worker就是空闲的线程
（当然，非核心线程阻塞之后也是空闲线程）。如果设置了keepAliveTime>0，那非核心线程会在空闲状态下等待keepAliveTime之后销毁，
直到最终的线程数量等于corePoolSize

woker线程的执行流程就是首先执行初始化时分配给的任务，执行完成以后会尝试从阻塞队列中获取可执行的任务，如果指定时间内仍然没有任务可以执行，
则进入销毁逻辑调用processWorkerExit()方法。
注：这里只会回收corePoolSize与maximumPoolSize直接的那部分woker


**第1个判断**
是否是第一次执行任务，或者从队列中可以获取到任务。
**第2个判断**
获取到任务后，执行任务开始前操作钩子。
**第3个判断**
执行任务。
**第4个判断**
执行任务后钩子。

[这两个钩子（beforeExecute，afterExecute）允许我们自己继承线程池ThreadPoolExecutor，进行重写，做任务执行前后处理]


#  从阻塞队列中获取任务 getTask()
这里getTask()方法是要重点说明的，它的实现跟我们构造参数keepAliveTime存活时间有关。
我们都知道keepAliveTime代表了线程池中的线程（即woker线程）的存活时间，如果到期则回收woker线程，这个逻辑的实现就在getTask中。

getTask()方法就是去阻塞队列中取任务，用户设置的存活时间，就是从这个阻塞队列中取任务等待的最大时间，如果getTask返回null，
意思就是woker等待了指定时间仍然没有取到任务，此时就会跳过循环体，进入woker线程的销毁逻辑。



    private Runnable getTask() {
          boolean timedOut = false; // Did the last poll() time out?
  
          retry:
          for (;;) {
              int c = ctl.get();
              int rs = runStateOf(c);
  
              // Check if queue empty only if necessary.
              if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                  decrementWorkerCount();
                  return null;
              }
  
              boolean timed;      // Are workers subject to culling?
  
              for (;;) {
                  int wc = workerCountOf(c);
                  timed = allowCoreThreadTimeOut || wc > corePoolSize;
  
                  if (wc <= maximumPoolSize && ! (timedOut && timed))
                      break;
                  if (compareAndDecrementWorkerCount(c))
                      return null;
                  c = ctl.get();  // Re-read ctl
                  if (runStateOf(c) != rs)
                      continue retry;
                  // else CAS failed due to workerCount change; retry inner loop
              }
  
              try {
                  Runnable r = timed ?
                      workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                      workQueue.take();
                  if (r != null)
                      return r;
                  timedOut = true;
              } catch (InterruptedException retry) {
                  timedOut = false;
              }
          }
      }
这个getTask()方法通过一个循环不断轮询任务队列有没有任务到来，首先判断线程池是否处于正常运行状态，根据超时配置有两种方法取出任务：
- BlockingQueue.poll 阻塞指定的时间尝试获取任务，如果超过指定的时间还未获取到任务就返回null。
- BlockingQueue.take 这种方法会在取到任务前一直阻塞。

FixedThreadPool使用的是take方法，所以会线程会一直阻塞等待任务。
CachedThreadPool使用的是poll方法，也就是说CachedThreadPool中的线程如果在60秒内未获取到队列中的任务就会被终止。

到此ThreadPoolExecutor是怎么执行Runnable任务的分析结束。



![线程池execute方法执行过程](/images/线程池execute方法执行过程.png)

1、获取当前线程池的状态。
2、当前线程数量小于 coreSize 时创建一个新的线程运行。
3、如果当前线程处于运行状态，并且写入阻塞队列成功。
4、双重检查，再次获取线程状态；如果线程状态变了（非运行状态）就需要从阻塞队列移除任务，并尝试判断线程是否全部执行完毕。同时执行拒绝策略。
5、如果当前线程池为空就新创建一个线程并执行。
6、如果在第三步的判断为非运行状态，尝试新建线程，如果失败则执行拒绝策略。

总结：
Executor<---ExecutorService<---AbstractExecutorService<---ThreadPoolExecutor（前面两个是接口，后面两个是类）
ThreadPoolExecutor中，包含了一个任务缓存队列和若干个执行线程，任务缓存队列是一个大小固定的缓冲区队列，用来缓存待执行的任务，执行线程用来处理待执行的任务。每个待执行的任务，都必须实现Runnable接口，执行线程调用其run()方法，完成相应任务。
1、ThreadPoolExecutor对象初始化时，不创建任何执行线程，当有新任务进来时，才会创建执行线程。
2、构造ThreadPoolExecutor对象时，需要配置该对象的核心线程池大小和最大线程池大小
3、当目前执行线程的总数小于核心线程大小时，所有新加入的任务，都在新线程中处理
4、当目前执行线程的总数大于或等于核心线程时，所有新加入的任务，都放入任务缓存队列中
5、当目前执行线程的总数大于或等于核心线程，并且缓存队列已满，同时此时线程总数小于线程池的最大大小，那么创建新线程，加入线程池中，协助处理新的任务。
6、当所有线程都在执行，线程池大小已经达到上限，并且缓存队列已满时，就rejectHandler拒绝新的任务
7、如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；

![线程池execute方法执行过程1](/images/线程池execute方法执行过程1.png)
![线程池execute方法执行过程2](/images/线程池execute方法执行过程2.png)



# execute()和submit()方法对比：
execute()方法实际上是Executor中声明的方法，在ThreadPoolExecutor进行了具体的实现，这个方法是ThreadPoolExecutor的核心方法，
通过这个方法可以向线程池提交一个任务，交由线程池去执行。

submit()方法是在ExecutorService中声明的方法，在AbstractExecutorService就已经有了具体的实现，
在ThreadPoolExecutor中并没有对其进行重写，这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，
它能够返回任务执行的结果，去看submit()方法的实现，会发现它实际上还是调用的execute()方法，
只不过它利用了Future来获取任务执行结果。

    public Future<?> submit(Runnable task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<Void> ftask = newTaskFor(task, null);
        execute(ftask);
        return ftask;
    }


# 如何配置线程池的大小
有一点是肯定的，线程池肯定是不是越大越好，通常我们是需要根据这批任务执行的性质来确定的。
如果任务是IO密集型，一般线程数需要设置2倍CPU数以上，以此来尽量利用CPU资源。
如果任务是CPU密集型，一般线程数量只需要设置CPU数加1即可，更多的线程数也只能增加上下文切换，不能增加CPU利用率。
上述只是一个基本思想，如果真的需要精确的控制，还是需要上线以后观察线程池中线程数量跟队列的情况来定。


# 优雅的关闭线程池
有运行任务自然也有关闭任务，从上文提到的 5 个状态就能看出如何来关闭线程池。
其实无非就是两个方法 shutdown()/shutdownNow()。
但他们有着重要的区别：
shutdown() 执行后停止接受新任务，会把队列的任务执行完毕。
shutdownNow() 也是停止接受新任务，但会中断所有的任务，将线程池状态变为 stop。
两个方法都会中断线程，用户可自行判断是否需要响应中断。
shutdownNow() 要更简单粗暴，可以根据实际场景选择不同的方法。

    long start = System.currentTimeMillis();
    for (int i = 0; i <= 5; i++) {
       pool.execute(new Job());
    }
    
    pool.shutdown();
    
    while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
       LOGGER.info("线程还在执行。。。");
    }
    long end = System.currentTimeMillis();
    LOGGER.info("一共处理了【{}】", (end - start));
    
pool.awaitTermination(1, TimeUnit.SECONDS) 会每隔一秒钟检查一次是否执行完毕（状态为 TERMINATED），
当从 while 循环退出时就表明线程池已经完全终止了。




        
# 使用无界队列的线程池会导致内存飙升吗?
[workQueue积压任务消耗大量内存]
我们以最常用的fixed线程池举例，他的线程池数量是固定的，因为他用的是近乎于无界的LinkedBlockingQueue，几乎可以无限制的放入任务到队列里。
所以只要线程池里的线程数量达到了corePoolSize指定的数量之后，接下来就维持这个固定数量的线程了。
然后，所有任务都会入队到workQueue里去，线程从workQueue获取任务来处理。
这个队列几乎永远不会满，当然这是几乎，因为LinkedBlockingQueue默认的最大任务数量是Integer.MAX_VALUE，非常大，近乎于可以理解为无限吧。
只要队列不满，就跟maximumPoolSize、keepAliveTime这些没关系了，因为不会创建超过corePoolSize数量的线程的。
那么此时万一每个线程获取到一个任务之后，他处理的时间特别特别的长，长到了令人发指的地步。比如处理一个任务要几个小时，此时会如何？
当然会出现workQueue里不断的积压越来越多得任务，不停的增加。
这个过程中会导致机器的内存使用不停的飙升，最后也许极端情况下就导致JVM OOM了，系统就挂掉了。
所以这就是这个面试题背后你要知道的线程池的运行原理，以及可能遇到的一些问题，大家要做到心里有数。


参考下面的链接
https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247484480&idx=1&sn=1c7262d7f185ad6f99b840fb7779a575&chksm=fba6ec43ccd16555d826772a530c280548d8fd9785a9169b87bb4ed82d8b12ad75154c98b957&mpshare=1&scene=24&srcid=0129hE3KSl208Tk114nNDeBY#rd


# 线程池的应用范围

1、需要大量的线程来完成任务，且完成任务的时间比较短。 WEB服务器完成网页请求这样的任务，使用线程池技术是非常合适的。因为单个任务小，而任务数量巨大，你可以想象一个热门网站的点击次数。 但对于长时间的任务，比如一个Telnet连接请求，线程池的优点就不明显了。因为Telnet会话时间比线程的创建时间大多了。
2、对性能要求苛刻的应用，比如要求服务器迅速相应客户请求。
3、接受突发性的大量请求，但不至于使服务器因此产生大量线程的应用。突发性大量客户请求，在没有线程池情况下，将产生大量线程，虽然理论上大部分操作系统线程数目最大值不是问题，短时间内产生大量线程可能使内存到达极限，并出现"OutOfMemory"的错误。


# 总结
到此无论是主动提交任务给新建线程执行，还是已有的线程自己到阻塞队列取任务执行，都应该清楚了然了。
从数据结构的角度来看，线程池主要使用了阻塞队列（BlockingQueue）和HashSet集合构成。
从任务提交的流程角度来看，对于使用线程池的外部来说，线程池的机制是这样的：
1、如果正在运行的线程数 < coreSize，马上创建线程执行该task，不排队等待；
2、如果正在运行的线程数 >= coreSize，把该task放入阻塞队列；
3、如果队列已满 && 正在运行的线程数 < maximumPoolSize，创建新的线程执行该task；
4、如果队列已满 && 正在运行的线程数 >= maximumPoolSize，线程池调用handler的reject方法拒绝本次提交。

从worker线程自己的角度来看，当worker的task执行结束之后，循环从阻塞队列中取出任务执行。
![](/images/ThreadPoolExecutor执行流程图.png)




        
# 参考
https://blog.csdn.net/luoweifu/article/details/46595285
https://www.cnblogs.com/liuzhihu/p/8177371.html
https://www.jianshu.com/p/ade771d2c9c0
https://blog.csdn.net/evankaka/article/details/51489322
https://blog.csdn.net/u010983881/article/details/79322499
https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247484480&idx=1&sn=1c7262d7f185ad6f99b840fb7779a575&chksm=fba6ec43ccd16555d826772a530c280548d8fd9785a9169b87bb4ed82d8b12ad75154c98b957&mpshare=1&scene=24&srcid=0129hE3KSl208Tk114nNDeBY#rd