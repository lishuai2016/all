# threadlocal
[ThreadLocal工作原理](https://blog.csdn.net/imzoer/article/details/8262101)


1、每个线程都有自己的局部变量

    每个线程都有一个独立于其他线程的上下文来保存这个变量，一个线程的本地变量对其他线程是不可见的（有前提，后面解释）

2、独立于变量的初始化副本

    ThreadLocal可以给一个初始值，而每个线程都会获得这个初始化值的一个副本，这样才能保证不同的线程都有一份拷贝。

3、状态与某一个线程相关联

    ThreadLocal 不是用于解决共享变量的问题的，不是为了协调线程同步而存在，而是为了方便每个线程处理自己的状态而引入的一个机制，理解这点对正确使用ThreadLocal至关重要。

什么是ThreadLocal类,怎么使用它？
     ThreadLocal类提供了线程局部 (thread-local) 变量。是一个线程级别的局部变量，并非“本地线程”。
     ThreadLocal为每个使用该变量的线程,提供了一个独立的变量副本，每个线程修改副本时不影响其它线程对象的副本

     下面是线程局部变量(ThreadLocal variables)的关键点：
          一个线程局部变量(ThreadLocal variables)为每个线程方便地提供了一个单独的变量。
          ThreadLocal 实例通常作为静态的私有的(private static)字段出现在一个类中，这个类用来关联一个线程。
          当多个线程访问 ThreadLocal 实例时，每个线程维护 ThreadLocal 提供的独立的变量副本。
          常用的使用可在 DAO 模式中见到，当 DAO 类作为一个单例类时，
          数据库链接(connection)被每一个线程独立的维护，互不影响。(基于线程的单例)


