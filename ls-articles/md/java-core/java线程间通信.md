线程间通信

而在java中我们实现多线程间通信则主要采用"共享变量"和"管道流"这两种方法

1、在synchronized范围内使用wait()和notify（）方法（同一个对象上的）；
2、条件对象的等待/通知机制（condition）：所谓的条件对象也就是配合前面我们分析的Lock锁对象，通过锁对象的条件对象来实现等待/通知机制

sleep、join
CountDownLatch
CyclicBarrier
Callable接口的call方法返回值



- //创建条件对象
- Condition conditionObj=ticketLock.newCondition();
就这样我们创建了一个条件对象。注意这里返回的对象是与该锁（ticketLock）相关的条件对象。下面是条件对象的API：
方法函数方法对应的描述                                                                                                                                                                                    void await()将该线程放到条件等待池中（对应wait()方法）void signalAll()解除该条件等待池中所有线程的阻塞状态（对应notifyAll()方法）void signal()从该条件的等待池中随机地选择一个线程，解除其阻塞状态（对应notify()方法）
上述方法的过程分析：一个线程A调用了条件对象的await()方法进入等待状态，而另一个线程B调用了条件对象的signal()或者signalAll()方法，线程A收到通知后从条件对象的await()方法返回，进而执行后续操作。上述的两个线程通过条件对象来完成交互，而对象上的await()和signal()/signalAll()的关系就如同开关信号一样，用来完成等待方和通知方之间的交互工作。当然这样的操作都是必须基于对象锁的，当前线程只有获取了锁，才能调用该条件对象的await()方法，而调用后，当前线程将缩放锁。
这里有点要特别注意的是，上述两种等待/通知机制中，无论是调用了signal()/signalAll()方法还是调用了notify()/notifyAll()方法并不会立即激活一个等待线程。它们仅仅都只是解除等待线程的阻塞状态，以便这些线程可以在当前线程解锁或者退出同步方法后，通过争夺CPU执行权实现对对象的访问。到此，线程通信机制的概念分析完，我们下面通过生产者消费者模式来实现等待/通知机制。

- 如何让两个线程依次执行？
- 那如何让 两个线程按照指定方式有序交叉运行呢？
- 四个线程 A B C D，其中 D 要等到 A B C 全执行完毕后才执行，而且 A B C 是同步运行的
- 三个运动员各自准备，等到三个人都准备好后，再一起跑
- 子线程完成某件任务后，把得到的结果回传给主线程



(通过管道流）:
代码如下：

public class CommunicateWhitPiping {
        public static void main(String[] args) {
                /**
                * 创建管道输出流
                */
                PipedOutputStream pos = new PipedOutputStream();
                /**
                * 创建管道输入流
                */
                PipedInputStream pis = new PipedInputStream();
                try {
                        /**
                        * 将管道输入流与输出流连接 此过程也可通过重载的构造函数来实现
                        */
                        pos.connect(pis);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                /**
                * 创建生产者线程
                */
                Producer p = new Producer(pos);
                /**
                * 创建消费者线程
                */
                Consumer c = new Consumer(pis);
                /**
                * 启动线程
                */
                p.start();
                c.start();
        }
}

/**
* 生产者线程(与一个管道输入流相关联)
*
*/class Producer extends Thread {
        private PipedOutputStream pos;

        public Producer(PipedOutputStream pos) {
                this.pos = pos;
        }

        public void run() {
                int i = 8;
                try {
                        pos.write(i);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}

/**
* 消费者线程(与一个管道输入流相关联)
*
*/class Consumer extends Thread {
        private PipedInputStream pis;

        public Consumer(PipedInputStream pis) {
                this.pis = pis;
        }

        public void run() {
                try {
                        System.out.println(pis.read());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}




















线程间通信

模拟这样一种情况：有一个生产者，它将生产的产品放入Box中，有一个消费者，他将Box中的产品取出。如果Box不是空的，那么生产者就等待，等Box中的产品被取走后他再继续生产往里放；如果Box是空的，那么消费者就等待，等Box中有产品后他再取出来。
这就需要我们在两个线程之间互相发送通知，互相告诉对方自己的状态，以引导对方的行为。

程序实现:

public class LearnThreadCommunication {

    public static void main(String[] args) {
        Box box = new Box();
        Producer producer = new Producer(box);
        Consumer consumer = new Consumer(box);
        producer.start();
        consumer.start();
    }
}

class Box{
    public int boxValue = 0;
}

class Producer extends Thread {
    private Box box;

    public Producer(Box box) {
        super();
        this.box = box;
    }

    public void run(){
        for(int i=1;i<6;i++){
            synchronized (box) {
                while(box.boxValue!=0){
                    try {
                        System.out.println("Producer：Box非空，生产者等待");
                        //释放对象锁，交出控制权，使本线程进入休眠等待状态
                        box.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                box.boxValue=i;
                System.out.println("Producer:box中放入了"+i);
                //唤醒该对象等待队列上的等待线程，归还控制权
                box.notify();
            }
        }
    }
}

class Consumer extends Thread {

    private Box box;

    public Consumer(Box box) {
        this.box = box;
    }

    public void run(){
        for(int i=1;i<6;i++){
            synchronized (box) {
                while(box.boxValue==0){
                    try {
                        System.out.println("Consumer:Box是空的，消费者等待");
                        box.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consumer:box中取出了"+i);
                box.boxValue=0;
                box.notify();
            }
        }
    }
}

测试结果：

wait and notify

总结：

wait():

wait();
wait(timeout);
wait(timeout, nanos);

当一个对象调用wait()方法的时候，当前作用于他的线程(ThreadA)就会暂时释放对象锁(monitor)，让其他线程(ThreadB)可以访问该线程。而ThreadA进入等待状态，直到wait()中设置的时间到了，或者其他线程重新唤醒ThreadA。

notify():

box.notify();
box.notifyAll();

notify()方法能够唤醒一个正在等待该对象的monitor的线程，当有多个线程都在等待该对象的monitor的话，则只能唤醒其中一个线程。如果需要唤醒所有线程，可以用notifyAll()方法。

注意：
1.使用wait()和notify()方法时，当前线程必须拥有该对象的monitor
2.一个线程被唤醒并不代表它立刻就获得了该对象的monitor,只有在当前线程释放monitor以后，它才能够获得。任一时刻，一个兑现的monitor只能被一个线程拥有。——“朕一日不死，你永远是太子！”
3.用notify()方法唤醒正在等待的多个线程中的一个时，唤醒哪一个是不确定的。
4.用notifyAll()方法唤醒正在等待的所有线程后，哪一个线程未来将会得到monitor也是不确定的。


下面我从几个例子作为切入点来讲解下 Java 里有哪些方法来实现线程间通信。

- 如何让两个线程依次执行？（sleep）
- 那如何让 两个线程按照指定方式有序交叉运行呢？（wait和notify）
- 四个线程 A B C D，其中 D 要等到 A B C 全执行完毕后才执行，而且 A B C 是同步运行的
- 三个运动员各自准备，等到三个人都准备好后，再一起跑
- 子线程完成某件任务后，把得到的结果回传给主线程

如何让两个线程依次执行？
假设有两个线程，一个是线程 A，另一个是线程 B，两个线程分别依次打印 1-3 三个数字即可。我们来看下代码：



private static void demo1() {
Thread A = new Thread(new Runnable() {
@Override
public void run() {
printNumber("A");
}
});
Thread B = new Thread(new Runnable() {
@Override
public void run() {
printNumber("B");
}
});
A.start();
B.start();
}

其中的 printNumber(String) 实现如下，用来依次打印 1, 2, 3 三个数字：


private static void printNumber(String threadName) {
int i=0;
while (i++ < 3) {
try {
Thread.sleep(100);
} catch (InterruptedException e) {
e.printStackTrace();
}
System.out.println(threadName + "print:" + i);
}
}

这时我们得到的结果是：

B print: 1
A print: 1
B print: 2
A print: 2
B print: 3
A print: 3

可以看到 A 和 B 是同时打印的。

那么，如果我们希望 B 在 A 全部打印 完后再开始打印呢？我们可以利用 thread.join() 方法，代码如下:



private static void demo2() {
Thread A = new Thread(new Runnable() {
@Override
public void run() {
printNumber("A");
}
});
Thread B = new Thread(new Runnable() {
@Override
public void run() {
System.out.println("B 开始等待 A");
try {
A.join();
} catch (InterruptedException e) {
e.printStackTrace();
}
printNumber("B");
}
});
B.start();
A.start();
}

得到的结果如下：

B 开始等待 A
A print: 1
A print: 2
A print: 3

B print: 1
B print: 2
B print: 3

所以我们能看到 A.join() 方法会让 B 一直等待直到 A 运行完毕。

那如何让 两个线程按照指定方式有序交叉运行呢？
还是上面那个例子，我现在希望 A 在打印完 1 后，再让 B 打印 1, 2, 3，最后再回到 A 继续打印 2, 3。这种需求下，显然 Thread.join() 已经不能满足了。我们需要更细粒度的锁来控制执行顺序。

这里，我们可以利用 object.wait() 和 object.notify() 两个方法来实现。代码如下：


/**
* A 1, B 1, B 2, B 3, A 2, A 3
*/
private static void demo3() {
Object lock = new Object();
Thread A = new Thread(new Runnable() {
@Override
public void run() {
synchronized (lock) {
System.out.println("A 1");
try {
lock.wait();
} catch (InterruptedException e) {
e.printStackTrace();
}
System.out.println("A 2");
System.out.println("A 3");
}
}
});
Thread B = new Thread(new Runnable() {
@Override
public void run() {
synchronized (lock) {
System.out.println("B 1");
System.out.println("B 2");
System.out.println("B 3");
lock.notify();
}
}
});
A.start();
B.start();
}

打印结果如下：

A 1
A waiting…

B 1
B 2
B 3
A 2
A 3

正是我们要的结果。

那么，这个过程发生了什么呢？

- 首先创建一个 A 和 B 共享的对象锁 lock = new Object();
- 当 A 得到锁后，先打印 1，然后调用 lock.wait() 方法，交出锁的控制权，进入 wait 状态；
- 对 B 而言，由于 A 最开始得到了锁，导致 B 无法执行；直到 A 调用 lock.wait() 释放控制权后， B 才得到了锁；
- B 在得到锁后打印 1， 2， 3；然后调用 lock.notify() 方法，唤醒正在 wait 的 A;
- A 被唤醒后，继续打印剩下的 2，3。

为了更好理解，我在上面的代码里加上 log 方便读者查看。


private static void demo3() {
Object lock = new Object();
Thread A = new Thread(new Runnable() {
@Override
public void run() {
System.out.println("INFO: A 等待锁");
synchronized (lock) {
System.out.println("INFO: A 得到了锁 lock");
System.out.println("A 1");
try {
System.out.println("INFO: A 准备进入等待状态，放弃锁 lock 的控制权");
lock.wait();
} catch (InterruptedException e) {
e.printStackTrace();
}
System.out.println("INFO: 有人唤醒了 A, A 重新获得锁 lock");
System.out.println("A 2");
System.out.println("A 3");
}
}
});
Thread B = new Thread(new Runnable() {
@Override
public void run() {
System.out.println("INFO: B 等待锁");
synchronized (lock) {
System.out.println("INFO: B 得到了锁 lock");
System.out.println("B 1");
System.out.println("B 2");
System.out.println("B 3");
System.out.println("INFO: B 打印完毕，调用 notify 方法");
lock.notify();
}
}
});
A.start();
B.start();
}

打印结果如下:

INFO: A 等待锁
INFO: A 得到了锁 lock
A 1
INFO: A 准备进入等待状态，调用 lock.wait() 放弃锁 lock 的控制权
INFO: B 等待锁
INFO: B 得到了锁 lock
B 1
B 2
B 3
INFO: B 打印完毕，调用 lock.notify() 方法
INFO: 有人唤醒了 A, A 重新获得锁 lock
A 2
A 3

四个线程 A B C D，其中 D 要等到 A B C 全执行完毕后才执行，而且 A B C 是同步运行的
最开始我们介绍了 thread.join()，可以让一个线程等另一个线程运行完毕后再继续执行，那我们可以在 D 线程里依次 join A B C，不过这也就使得 A B C 必须依次执行，而我们要的是这三者能同步运行。

或者说，我们希望达到的目的是：A B C 三个线程同时运行，各自独立运行完后通知 D；对 D 而言，只要 A B C 都运行完了，D 再开始运行。针对这种情况，我们可以利用 CountdownLatch 来实现这类通信方式。它的基本用法是：

- 创建一个计数器，设置初始值，CountdownLatch countDownLatch = new CountDownLatch(2);
- 在 等待线程 里调用 countDownLatch.await() 方法，进入等待状态，直到计数值变成 0；
- 在 其他线程 里，调用 countDownLatch.countDown() 方法，该方法会将计数值减小 1；
- 当 其他线程 的 countDown() 方法把计数值变成 0 时，等待线程 里的 countDownLatch.await() 立即退出，继续执行下面的代码。

实现代码如下：

private static void runDAfterABC() {
int worker = 3;
CountDownLatch countDownLatch = new CountDownLatch(worker);
new Thread(new Runnable() {
@Override
public void run() {
System.out.println("D is waiting for other three threads");
try {
countDownLatch.await();
System.out.println("All done, D starts working");
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}).start();
for (char threadName='A'; threadName <= 'C'; threadName++) {
final String tN = String.valueOf(threadName);
new Thread(new Runnable() {
@Override
public void run() {
System.out.println(tN + "is working");
try {
Thread.sleep(100);
} catch (Exception e) {
e.printStackTrace();
}
System.out.println(tN + "finished");
countDownLatch.countDown();
}
}).start();
}
}

下面是运行结果：

D is waiting for other three threads
A is working
B is working
C is working

A finished
C finished
B finished
All done, D starts working

其实简单点来说，CountDownLatch 就是一个倒计数器，我们把初始计数值设置为3，当 D 运行时，先调用 countDownLatch.await() 检查计数器值是否为 0，若不为 0 则保持等待状态；当A B C 各自运行完后都会利用countDownLatch.countDown()，将倒计数器减 1，当三个都运行完后，计数器被减至 0；此时立即触发 D的 await() 运行结束，继续向下执行。

因此，CountDownLatch 适用于一个线程去等待多个线程的情况。

三个运动员各自准备，等到三个人都准备好后，再一起跑
上面是一个形象的比喻，针对 线程 A B C 各自开始准备，直到三者都准备完毕，然后再同时运行 。也就是要实现一种 线程之间互相等待 的效果，那应该怎么来实现呢？

上面的 CountDownLatch 可以用来倒计数，但当计数完毕，只有一个线程的 await() 会得到响应，无法让多个线程同时触发。

为了实现线程间互相等待这种需求，我们可以利用 CyclicBarrier 数据结构，它的基本用法是：

- 先创建一个公共 CyclicBarrier 对象，设置 同时等待 的线程数，CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
- 这些线程同时开始自己做准备，自身准备完毕后，需要等待别人准备完毕，这时调用 cyclicBarrier.await(); 即可开始等待别人；
- 当指定的 同时等待 的线程数都调用了 cyclicBarrier.await();时，意味着这些线程都准备完毕好，然后这些线程才 同时继续执行。

实现代码如下，设想有三个跑步运动员，各自准备好后等待其他人，全部准备好后才开始跑：


private static void runABCWhenAllReady() {
int runner = 3;
CyclicBarrier cyclicBarrier = new CyclicBarrier(runner);
final Random random = new Random();
for (char runnerName='A'; runnerName <= 'C'; runnerName++) {
final String rN = String.valueOf(runnerName);
new Thread(new Runnable() {
@Override
public void run() {
long prepareTime = random.nextInt(10000) + 100;
System.out.println(rN + "is preparing for time:" + prepareTime);
try {
Thread.sleep(prepareTime);
} catch (Exception e) {
e.printStackTrace();
}
try {
System.out.println(rN + "is prepared, waiting for others");
cyclicBarrier.await(); // 当前运动员准备完毕，等待别人准备好
} catch (InterruptedException e) {
e.printStackTrace();
} catch (BrokenBarrierException e) {
e.printStackTrace();
}
System.out.println(rN + "starts running"); // 所有运动员都准备好了，一起开始跑
}
}).start();
}
}

打印的结果如下：

A is preparing for time: 4131
B is preparing for time: 6349
C is preparing for time: 8206

A is prepared, waiting for others

B is prepared, waiting for others

C is prepared, waiting for others

C starts running
A starts running
B starts running

子线程完成某件任务后，把得到的结果回传给主线程
实际的开发中，我们经常要创建子线程来做一些耗时任务，然后把任务执行结果回传给主线程使用，这种情况在 Java 里要如何实现呢？

回顾线程的创建，我们一般会把 Runnable 对象传给 Thread 去执行。Runnable定义如下：

1
2
3

public interface Runnable {
public abstract void run();
}

可以看到 run() 在执行完后不会返回任何结果。那如果希望返回结果呢？这里可以利用另一个类似的接口类 Callable：

1
2
3
4
5
6
7
8
9
10

@FunctionalInterface
public interface Callable<V> {
/**
* Computes a result, or throws an exception if unable to do so.
*
* @return computed result
* @throws Exception if unable to compute a result
*/
V call() throws Exception;
}

可以看出 Callable 最大区别就是返回范型 V 结果。

那么下一个问题就是，如何把子线程的结果回传回来呢？在 Java 里，有一个类是配合 Callable 使用的：FutureTask，不过注意，它获取结果的 get 方法会阻塞主线程。

举例，我们想让子线程去计算从 1 加到 100，并把算出的结果返回到主线程。



private static void doTaskWithResultInWorker() {
Callable<Integer> callable = new Callable<Integer>() {
@Override
public Integer call() throws Exception {
System.out.println("Task starts");
Thread.sleep(1000);
int result = 0;
for (int i=0; i<=100; i++) {
result += i;
}
System.out.println("Task finished and return result");
return result;
}
};
FutureTask<Integer> futureTask = new FutureTask<>(callable);
new Thread(futureTask).start();
try {
System.out.println("Before futureTask.get()");
System.out.println("Result:" + futureTask.get());
System.out.println("After futureTask.get()");
} catch (InterruptedException e) {
e.printStackTrace();
} catch (ExecutionException e) {
e.printStackTrace();
}
}

打印结果如下：

Before futureTask.get()

Task starts
Task finished and return result

Result: 5050
After futureTask.get()

可以看到，主线程调用 futureTask.get() 方法时阻塞主线程；然后 Callable 内部开始执行，并返回运算结果；此时 futureTask.get() 得到结果，主线程恢复运行。

这里我们可以学到，通过 FutureTask 和 Callable 可以直接在主线程获得子线程的运算结果，只不过需要阻塞主线程。当然，如果不希望阻塞主线程，可以考虑利用 ExecutorService，把 FutureTask 放到线程池去管理执行。

小结
多线程是现代语言的共同特性，而线程间通信、线程同步、线程安全是很重要的话题。本文针对 Java 的线程间通信进行了大致的讲解，后续还会对线程同步、线程安全进行讲解。
