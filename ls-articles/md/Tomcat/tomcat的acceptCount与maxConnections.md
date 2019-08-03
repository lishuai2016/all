---
title: tomcat的acceptCount与maxConnections
categories: 
- tomcat
tags:
---


[参考](https://segmentfault.com/a/1190000008064162)
[参考](https://blog.csdn.net/jackpk/article/details/32137483)


tomcat7连接器3个属性maxConnections、maxThreads、acceptCount官网对这3个属性的描述：
[acceptCount]（最大等待数）
The maximum queue length for incoming connection requests when all possible request processing threads are in use. 
Any requests received when the queue is full will be refused. The default value is 100.
[maxConnections]（最大连接数）
The maximum number of connections that the server will accept and process at any given time.
When this number has been reached, the server will accept, but not process, one further connection. 
This additional connection be blocked until the number of connections being processed falls below maxConnections 
at which point the server will start accepting and processing new connections again.
(Note that once the limit has been reached, the operating system may still accept connections based on the acceptCount setting.)
其中maxConnections描述红色部分说明当连接数达到最大值后，系统会继续接收连接但不会超过acceptCount的值。
The default value varies by connector type. 
For BIO the default is the value of maxThreads unless an Executor is used in which case the default will be the value of maxThreads from the executor. 
For NIO the default is 10000. For APR/native, the default is 8192.
Note that for APR/native on Windows, the configured value will be reduced to the highest multiple of 1024 that is less than or equal to maxConnections. 
This is done for performance reasons.If set to a value of -1, the maxConnections feature is disabled and connections are not counted.
[maxThreads]（最大线程数）
The maximum number of request processing threads to be created by this Connector,
which therefore determines the maximum number of simultaneous requests that can be handled. 
If not specified, this attribute is set to 200. If an executor is associated with this connector, 
this attribute is ignored as the connector will execute tasks using the executor rather than an internal thread pool. 
Note that if an executor is configured any value set for this attribute will be recorded correctly but it will be reported (e.g. via JMX) 
as -1 to make clear that it is not used.

# 从上面可以看出其中最后三个参数意义如下
maxThreads：tomcat起动的最大线程数，即同时处理的任务个数，默认值为200；
maxConnections：取决于IO的链接方式，BOI默认和maxThreads一致，NIO默认10000；
acceptCount：当tomcat起动的线程数达到最大时，接受排队的请求个数，默认值为100；
备注：在connector配置不同的协议protocol来决定使用哪种IO处理方式。
如果没有指定protocol，则使用默认值HTTP/1.1，其含义如下：
在Tomcat7中，自动选取使用BIO或APR（如果找到APR需要的本地库，则使用APR，否则使用BIO）；
在Tomcat8中，自动选取使用NIO或APR（如果找到APR需要的本地库，则使用APR，否则使用NIO）。

# 针对BOI情况，maxThreads和acceptCount两个值的工作情况
情况1：接受一个请求，此时tomcat起动的线程数没有到达maxThreads，tomcat会起动一个线程来处理此请求。
情况2：接受一个请求，此时tomcat起动的线程数已经到达maxThreads，tomcat会把此请求放入等待队列，等待空闲线程。
情况3：接受一个请求，此时tomcat起动的线程数已经到达maxThreads，等待队列中的请求个数也达到了acceptCount，
此时tomcat会直接拒绝此次请求，返回connection refused

# maxThreads如何配置
一般的服务器操作都包括量方面：1计算（主要消耗cpu），2等待（io、数据库等）
第一种极端情况，如果我们的操作是纯粹的计算，那么系统响应时间的主要限制就是cpu的运算能力，此时maxThreads应该尽量设的小，
降低同一时间内争抢cpu的线程个数，可以提高计算效率，提高系统的整体处理能力。
第二种极端情况，如果我们的操作纯粹是IO或者数据库，那么响应时间的主要限制就变为等待外部资源，
此时maxThreads应该尽量设的大，这样才能提高同时处理请求的个数，从而提高系统整体的处理能力。此情况下因为tomcat同时处理的请求量会比较大，
所以需要关注一下tomcat的虚拟机内存设置和linux的open file限制。

把maxThreads设置过大的话，会造成cpu在线程切换时消耗的时间随着线程数量的增加越来越大，
cpu把大多数时间都用来在多个线程直接切换上了，当然cpu就没有时间来处理我们的程序了。
简单的认为多线程=高效率。其实多线程本身并不能提高cpu效率，线程过多反而会降低cpu效率。
当cpu核心数<线程数时，cpu就需要在多个线程直接来回切换，以保证每个线程都会获得cpu时间，即通常我们说的并发执行。
所以maxThreads的配置绝对不是越大越好。
现实应用中，我们的操作都会包含以上两种类型（计算、等待），所以maxThreads的配置并没有一个最优值，一定要根据具体情况来配置。
最好的做法是：在不断测试的基础上，不断调整、优化，才能得到最合理的配置。


# acceptCount的配置
我一般是设置的跟maxThreads一样大，这个值应该是主要根据应用的访问峰值与平均值来权衡配置的。
如果设的较小，可以保证接受的请求较快相应，但是超出的请求可能就直接被拒绝
如果设的较大，可能就会出现大量的请求超时的情况，因为我们系统的处理能力是一定的。



关于tomcat的参数，有acceptCount、maxConnections、maxThreads、minSpareThreads这几个参数比较容易混淆，这里做一下澄清。

# 1、不同层次
1、[maxThreads]、[minSpareThreads]是tomcat[工作线程池]的配置参数，maxThreads就相当于jdk线程池的maxPoolSize，
而minSpareThreads就相当于jdk线程池的corePoolSize。
2、[acceptCount、maxConnections]是tcp层相关的参数。
<div align="center"> <img src="/images/tomcat连接数.jpg"/> </div><br>

tomcat有一个[acceptor线程]来accept socket连接，然后有[工作线程]来进行业务处理。对于client端的一个请求进来，流程是这样的：
tcp的三次握手建立连接，建立连接的过程中，OS维护了半连接队列(syn队列)以及完全连接队列(accept队列)，
在第三次握手之后，server收到了client的ack，则进入establish的状态，然后该连接由syn队列移动到accept队列。
tomcat的[acceptor线程]则负责从[accept队列]中取出该connection，接受该connection，
然后交给工作线程去处理(读取请求参数、处理逻辑、返回响应等等；如果该连接不是keep alived的话，则关闭该连接，然后该工作线程释放回线程池，
如果是keep alived的话，则等待下一个数据包的到来直到keepAliveTimeout，然后关闭该连接释放回线程池)，
然后自己接着去accept队列取connection(当前socket连接超过maxConnections的时候，acceptor线程自己会阻塞等待，等连接降下去之后，才去处理accept队列的下一个连接)。
[acceptCount指的就是这个accept队列的大小]

# 2、maxConnections(NioEndpoint$Acceptor):这个值表示最多可以有多少个socket连接到tomcat上。NIO模式下默认是10000.

    // --------------------------------------------------- Acceptor Inner Class
    /**
     * The background thread that listens for incoming TCP/IP connections and
     * hands them off to an appropriate processor.
     */
    protected class Acceptor extends AbstractEndpoint.Acceptor {

        @Override
        public void run() {

            int errorDelay = 0;

            // Loop until we receive a shutdown command
            while (running) {

                // Loop if endpoint is paused
                while (paused && running) {
                    state = AcceptorState.PAUSED;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }

                if (!running) {
                    break;
                }
                state = AcceptorState.RUNNING;

                try {
                    //if we have reached max connections, wait
                    countUpOrAwaitConnection();

                    SocketChannel socket = null;
                    try {
                        // Accept the next incoming connection from the server
                        // socket
                        socket = serverSock.accept();
                    } catch (IOException ioe) {
                        //we didn't get a socket
                        countDownConnection();
                        // Introduce delay if necessary
                        errorDelay = handleExceptionWithDelay(errorDelay);
                        // re-throw
                        throw ioe;
                    }
                    // Successful accept, reset the error delay
                    errorDelay = 0;

                    // setSocketOptions() will add channel to the poller
                    // if successful
                    if (running && !paused) {
                        if (!setSocketOptions(socket)) {
                            countDownConnection();
                            closeSocket(socket);
                        }
                    } else {
                        countDownConnection();
                        closeSocket(socket);
                    }
                } catch (SocketTimeoutException sx) {
                    // Ignore: Normal condition
                } catch (IOException x) {
                    if (running) {
                        log.error(sm.getString("endpoint.accept.fail"), x);
                    }
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    log.error(sm.getString("endpoint.accept.fail"), t);
                }
            }
            state = AcceptorState.ENDED;
        }
    }
这里countUpOrAwaitConnection()判断的就是当前的连接数是否超过maxConnections。

# 3、acceptCount(backlog):在源码里头是backlog参数，默认值为100。该参数是指当前连接数超过maxConnections的时候，还可接受的连接数，即tcp的完全连接队列(accept队列)的大小。

backlog参数提示内核监听队列的最大长度。监听队列的长度如果超过backlog，服务器将不受理新的客户连接，客户端也将收到ECONNREFUSED错误信息。
在内核版本2.2之前的Linux中，backlog参数是指所有处于半连接状态（SYN_RCVD）和完全连接状态（ESTABLISHED）的socket的上限。
但自内核版本2.2之后，它只表示处于完全连接状态的socket的上限，处于半连接状态的socket的上限则由/proc/sys/net/ipv4/tcp_max_syn_backlog内核参数定义。

# 4、TCP三次握手队列
## 4.1、client端的socket等待队列：
当第一次握手，建立半连接状态：client 通过 connect 向 server 发出 SYN 包时，client 会维护一个 socket 队列，如果 socket 等待队列满了，
而 client 也会由此返回 connection time out，只要是 client 没有收到 第二次握手SYN+ACK，3s 之后，client 会再次发送，如果依然没有收到，9s 之后会继续发送。

## 4.2、server端的半连接队列(syn队列)：
此时server 会维护一个 SYN 队列，半连接 syn 队列的长度为 max(64, /proc/sys/net/ipv4/tcp_max_syn_backlog)  ，
在机器的tcp_max_syn_backlog值在/proc/sys/net/ipv4/tcp_max_syn_backlog下配置，当 server 收到 client 的 SYN 包后，
会进行第二次握手发送SYN＋ACK 的包加以确认，client 的 TCP 协议栈会唤醒 socket 等待队列，发出 connect 调用。

## 4.3、server端的完全连接队列(accpet队列)：
当第三次握手时，当server接收到ACK 报之后， 会进入一个新的叫 accept 的队列，该队列的长度为 min(backlog, somaxconn)，
默认情况下，somaxconn 的值为 128，表示最多有 129 的 ESTAB 的连接等待 accept()，而 backlog 的值则应该是由 int listen(int sockfd, int backlog) 
中的第二个参数指定，listen 里面的 backlog 可以有我们的应用程序去定义的。

NioEndpoint的bind方法

    @Override
    public void bind() throws Exception {

        serverSock = ServerSocketChannel.open();
        socketProperties.setProperties(serverSock.socket());
        InetSocketAddress addr = (getAddress()!=null?new InetSocketAddress(getAddress(),getPort()):new InetSocketAddress(getPort()));
        serverSock.socket().bind(addr,getBacklog());
        serverSock.configureBlocking(true); //mimic APR behavior
        serverSock.socket().setSoTimeout(getSocketProperties().getSoTimeout());

        // Initialize thread count defaults for acceptor, poller
        if (acceptorThreadCount == 0) {
            // FIXME: Doesn't seem to work that well with multiple accept threads
            acceptorThreadCount = 1;
        }
        if (pollerThreadCount <= 0) {
            //minimum one poller thread
            pollerThreadCount = 1;
        }
        stopLatch = new CountDownLatch(pollerThreadCount);

        // Initialize SSL if needed
        initialiseSsl();

        selectorPool.open();
    }
这里的serverSock.socket().bind(addr,getBacklog());的backlog就是acceptCount参数值。

# 5、tcp的半连接与完全连接队列
当accept队列满了之后，即使client继续向server发送ACK的包，也会不被相应，此时，server通过/proc/sys/net/ipv4/tcp_abort_on_overflow来决定如何返回，
0表示直接丢丢弃该ACK，1表示发送RST通知client；相应的，client则会分别返回read timeout 或者 connection reset by peer。
<div align="center"> <img src="/images/tomcat连接数1.jpg"/> </div><br>

总的来说：可以看到，整个TCP连接中我们的Server端有如下的两个 queue:
一个是半连接队列：(syn queue) queue(max(tcp_max_syn_backlog, 64))，用来保存 SYN_SENT 以及 SYN_RECV 的信息。
一个是完全连接队列：accept queue(min(somaxconn, backlog))，保存 ESTAB 的状态，那么建立连接之后，我们的应用服务的线程就可以accept()处理业务需求了。

# 6、SYN攻击
在三次握手过程中，Server发送SYN-ACK之后，收到Client的ACK之前的TCP连接称为半连接（half-open connect），此时Server处于SYN_RCVD状态，
当收到ACK后，Server转入ESTABLISHED状态。SYN攻击就是 Client在短时间内伪造大量不存在的IP地址，并向Server不断地发送SYN包，Server回复确认包，
并等待Client的确认，由于源地址 是不存在的，因此，Server需要不断重发直至超时，这些伪造的SYN包将产时间占用未连接队列，
导致正常的SYN请求因为队列满而被丢弃，从而引起网络堵塞甚至系统瘫痪。SYN攻击时一种典型的DDOS攻击，检测SYN攻击的方式非常简单，
即当Server上有大量半连接状态且源IP地址是随机的，则可以断定遭到SYN攻击了，使用如下命令可以让之现行：

    netstat -nap | grep SYN_RECV

# 7、关于maxConnections与maxThreads
maxConnections表示有多少个socket连接到tomcat上。NIO模式下默认是10000。而maxThreads则是woker线程并发处理请求的最大数。
也就是虽然client的socket连接上了，但是可能都在tomcat的task queue里头，等待worker线程处理返回响应。

# 8、小结
tomcat server在tcp的accept队列的大小设置的基础上，对请求连接多做了一层保护，也就是maxConnections的大小限制。当client端的大量请求过来时，
首先是OS层的tcp的accept队列帮忙挡住，accept队列满了的话，后续的连接无法进入accept队列，无法交由工作线程处理，client将得到read timeout或者connection reset的错误。
第二层保护就是，在acceptor线程里头进行缓冲，当连接的socket超过maxConnections的时候，则进行阻塞等待，控制acceptor转给worker线程连接的速度，
稍微缓缓，等待worker线程处理响应client。




# 9、Tomcat的性能与最大并发数

##1、Tomcat并发数概述
操作系统对于进程中的线程数有一定的限制：
Windows 每个进程中的线程数不允许超过 2000
Linux 每个进程中的线程数不允许超过 1000
另外，在 Java 中每开启一个线程需要耗用 1MB 的 JVM 内存空间用于作为线程栈之用。

系统环境不同，Tomcat版本不同、JDK版本不同、以及修改的设定参数不同。并发量的差异还是满大的。
maxThreads="1000" 最大并发数 
minSpareThreads="100"///初始化时创建的线程数
maxSpareThreads="500"///一旦创建的线程超过这个值，Tomcat就会关闭不再需要的socket线程。
acceptCount="700"// 指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理


并发用户数和QPS两个概念没有直接关系，但是如果要说QPS时，一定需要指明是多少并发用户数下的QPS，
否则豪无意义，因为单用户数的40QPS和20并发用户数下的40QPS是两个不同的概念。
前者说明该应用可以在一秒内串行执行40个请求，而后者说明在并发20个请求的情况下，一秒内该应用能处理40个请求，
当QPS相同时，越大的并发用户数，代表了网站并发处理能力越好。对于当前的web服务器，其处理单个用户的请求肯定戳戳有余，
这个时候会存在资源浪费的情况（一方面该服务器可能有多个cpu，但是只处理单个进程，另一方面，在处理一个进程中，有些阶段可能是IO阶段，
这个时候会造成CPU等待，但是有没有其他请求进程可以被处理）。而当并发数设置的过大时，每秒钟都会有很多请求需要处理，会造成进程（线程）频繁切换，
反正真正用于处理请求的时间变少，每秒能够处理的请求数反而变少，同时用户的请求等待时间也会变大，甚至超过用户的心理底线。
所以在最小并发数和最大并发数之间，一定有一个最合适的并发数值，在并发数下，QPS能够达到最大。
但是，这个并发并非是一个最佳的并发，因为当QPS到达最大时的并发，可能已经造成用户的等待时间变得超过了其最优值，
所以对于一个系统，其最佳的并发数，一定需要结合QPS，用户的等待时间来综合确定。


## 2、Tomcat的优化分成两块

### 2.1、 Tomcat启动命令行中的优化参数即JVM优化
Tomcat 的启动参数位于tomcat的安装目录\bin目录下，如果你是Linux操作系统就是catalina.sh文件，
如果你是Windows操作系统那么 你需要改动的就是catalina.bat文件

### 2.2、Tomcat容器自身参数的优化（这块很像ApacheHttp Server）

各种Web容器，如Tomcat，Resion，Jetty等都有自己的线程池（可在配置文件中配置），所以在客户端进行请求调用的时候，
程序员不用针对Client的每一次请求，都新建一个线程。而容器会自动分配线程池中的线程，提高访问速度。

## 3、Tomcat的server.xml配置Tomcat线程池以使用高并发连接
1.打开共享的线程池：
<Service name="Catalina">
  <!--The connectors can use a shared executor, you can define one or more named thread pools-->
    <Executor name="tomcatThreadPool" namePrefix="catalina-exec-"
    maxThreads="1000" minSpareThreads="50" maxIdleTime="600000"/>
默认前后是注释<!-- -->掉的，去掉就可以了。
重要参数说明：
name：共享线程池的名字。这是Connector为了共享线程池要引用的名字，该名字必须唯一。默认值：None；
namePrefix:在JVM上，每个运行线程都可以有一个name 字符串。这一属性为线程池中每个线程的name字符串设置了一个前缀，Tomcat将把线程号追加到这一前缀的后面。默认值：tomcat-exec-；
maxThreads：该线程池可以容纳的最大线程数。默认值：200；
maxIdleTime：在Tomcat关闭一个空闲线程之前，允许空闲线程持续的时间(以毫秒为单位)。只有当前活跃的线程数大于minSpareThread的值，才会关闭空闲线程。默认值：60000(一分钟)。
minSpareThreads：Tomcat应该始终打开的最小不活跃线程数。默认值：25。
threadPriority：线程的等级。默认是Thread.NORM_PRIORITY

2. 在Connector中指定使用共享线程池：
<Connector executor="tomcatThreadPool"
           port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443"
           minProcessors="5"
           maxProcessors="75"
           acceptCount="1000"/>

重要参数说明：
executor：表示使用该参数值对应的线程池；
minProcessors：服务器启动时创建的处理请求的线程数；
maxProcessors：最大可以创建的处理请求的线程数；
acceptCount：指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理。


## 4、Tomcat线程池实现
1、使用APR的Pool技术，使用了JNI。
Tomcat从5.5.17开始，为了提高响应速度和效率，使用了Apache Portable Runtime(APR)作为最底层，
使用了APR中包含Socket、缓冲池等多种技术，性能也提高了。APR也是Apache HTTPD的最底层。
 
2、使用Java实现的Thread Pool。
ThreadPool默认创建了5个线程，保存在一个200维的线程数组中，创建时就启动了这些线程，当然在没有请求时，
它们都处理“等待”状态（其实就是一个while循环，不停的等待notify）。如果有请求时，空闲线程会被唤醒执行用户的请求。

具体的请求过程是： 服务启动时，创建一个一维线程数组（maxThread＝200个），并创建空闲线程(minSpareThreads＝5个)随时等待用户请求。 
当有用户请求时，调用 threadpool.runIt(ThreadPoolRunnable)方法，将一个需要执行的实例传给ThreadPool中。
其中用户需要执行的 实例必须实现ThreadPoolRunnable接口。 ThreadPool 首先查找空闲的线程，如果有则用它运行要执行的ThreadPoolRunnable；
如果没有空闲线程并且没有超过maxThreads，就一次性创建 minSpareThreads个空闲线程；如果已经超过了maxThreads了，就等待空闲线程了。
总之，要找到空闲的线程，以便用它执行实例。找到后，将该线程从线程数组中移走。 接着唤醒已经找到的空闲线程，
用它运行执行实例（ThreadPoolRunnable）。 运行完ThreadPoolRunnable后，就将该线程重新放到线程数组中，作为空闲线程供后续使用。
由此可以看出，Tomcat的线程池实现是比较简单的，ThreadPool.java也只有840行代码。用一个一维数组保存空闲的线程，
每次以一个较小步伐（5个）创建空闲线程并放到线程池中。使用时从数组中移走空闲的线程，用完后，再“归还”给线程池。

      
总结： 
tomcat5.5.10以上版本开始支持apr，支持通过apache runtime module进行JNI调用，使用本地代码来加速网络处理。
如果不使用apr之前，Tomcat的Servlet线程池使用的是阻塞IO的模式，使用apr之后，线程池变成了 NIO的非阻塞模式，
而且这种NIO还是使用了操作系统的本地代码，看tomcat文档上面的说法是，极大提升web处理能力，不再需要专门放一个web server处理静态页面了。 
我自己直观的感受是，不用apr之前，你配置多少个等待线程，tomcat就会启动多少个线程挂起等待，使用apr以后，不管你配置多少，
就只有几个NIO调度的线程，这一点你可以通过kill -3 PID，然后察看log得知。
假设不使用apr，可能端口的线程调度能力比较差，所以通过iptables进行端口转发，让两个端口去分担一个端口的线程调度，
就有可能减少线程调度的并发，从而提高处理能力，减少资源消耗。
 


## 问题
Q:现在有这样一个需求，在一秒中有3万的支付订单请求，有什么比较好的解决方案吗？
PS:我们数据库用的是oracle 程序是java spring mybatis dubbo mq等技术，现在有这样一个场景 高并发写 在一秒中有3万的支付订单请求有什么比较好的解决方案吗？
主要优化哪方面
 
 

1. 首先要解决掉数据库的压力，3万qps对应的磁盘 iops 很大，不过现在好的 SSD 能提供很好的 iops, 
比如这款： ARK | Intel® SSD DC P3700 Series (800GB, 2.5in PCIe 3.0, 20nm, MLC) 单盘 90000 IOPS，
应该能撑住你的数据库，考虑到主备，以及你的sharding需求，3-9 台数据库机器，高内存，高CPU，SSD磁盘应该能抗住

2. 业务逻辑这一层: Java 系，用线程来抗并发的，如果业务逻辑不太复杂，那么基本能做到 100ms 内响应，那么 30000qps,
 对应的是 3000并发线程，这部分设计的时候记得保持无状态，单台支撑 300-1000 并发没问题，加上一倍的冗余，那么 6~20 台业务型机器可以抗住。

3. 缓存层: 支付订单一般对缓存需求不高，但缓存层一般都会有，避免把查询压力压到数据库，简单两台缓存，
或者缓存平行部署在业务型机器上都可以解决，具体看你的情况了。

4. 接入层: nginx 做LVS就可以了，记得 backlog 配大点就可以了, 3万qps, 假设单个请求的数据在 10KB 左右，那么是 300MB/s，
如果是千兆机，每台4网卡，两内两外，加上冗余，我会部署4台入口机，如果是万兆机，两台做主备（心跳或者LVS)即可。

当然，魔鬼在细节，做好机器的监控，慢请求的监控，日志的汇聚与分析。然后逐步推进服务的 SOA 化来降低复杂度。
留一台业务机打小流量来做线上测试。优化JVM运行参数，等等，要做的事情还很多。



从交易角度来看，各种高并发系统可以粗略分为两大类：交易驱动的系统，内容驱动的系统。其中：
交易驱动的系统：包括支付系统、电信计费系统、银行核心交易系统等，此类系统强调数据库事务的ACID原则。
内容驱动的系统：包括SNS、微博、门户、视频、搜索引擎等系统，此类系统对数据库事务ACID的关注不是第一位的，
更强调CAP原则：Consistency（一致性）， Availability（可用性），Partition tolerance（分区容错性）。

与此对应，交易驱动的系统与内容驱动的系统在系统优化方法最大的差异在于：
交易驱动的系统：强调事务的ACID，按照CAP原则做选择，更强调CA（Consistency（一致性）和Availability（可用性）；
因此交易驱动的系统一般在核心交易上选择关系型数据库（包括采用内存数据库、缓存等涉及事务问题），当然这就导致交易系统最大的瓶颈一般都在关系数据库上。
内容驱动的系统：可以在CAP之间根据业务需要做选择三选二，因此一般选择NOSQL为主、RDBMS为辅的方案。

在优化策略上，内容驱动的系统采用的诸多优化手段交易驱动的系统也可以采用，这里重点说一下因事务导致的业务复杂性的处理。
3万笔/每秒这个级别的交易订单这个量级说实话挺大，但即便如此，也有诸多可优化的空间。由于题主未对具体场景说明，只能假定是典型的交易驱动系统，一些思考点：
1、3万笔/每秒是峰值最大交易量还是持续交易量？
2、3万笔/每秒是同一类型的订单还是诸多种类型的订单？
3、业务能否做拆分，例如从功能、从区域、从优先级等角度？
4、支付订单是实时交易还是非实时交易，能否延时入库？
5、支付订单能否延时批量处理？
6、支付订单是否涉及热点账户问题，也即对同一账户会有多个并发请求对其属性（例如账户余额）进行操作？





