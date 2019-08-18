<!-- TOC -->

- [1、简介](#1简介)
- [02_hystrix与高可用系统架构：资源隔离+限流+熔断+降级+运维监控](#02_hystrix与高可用系统架构资源隔离限流熔断降级运维监控)
    - [1、hystrix是什么？](#1hystrix是什么)
    - [2、高可用系统架构](#2高可用系统架构)
    - [3、如何讲解这块内容？](#3如何讲解这块内容)
- [03_hystrix要解决的分布式系统可用性问题以及其设计原则](#03_hystrix要解决的分布式系统可用性问题以及其设计原则)
    - [1、Hystrix是什么？](#1hystrix是什么)
    - [2、Hystrix的历史](#2hystrix的历史)
    - [3、初步看一看Hystrix的设计原则是什么？](#3初步看一看hystrix的设计原则是什么)
    - [4、Hystrix要解决的问题是什么？](#4hystrix要解决的问题是什么)
    - [5、再看Hystrix的更加细节的设计原则是什么？](#5再看hystrix的更加细节的设计原则是什么)
    - [6、Hystrix是如何实现它的目标的？](#6hystrix是如何实现它的目标的)
- [04_电商网站的商品详情页缓存服务业务背景以及框架结构说明](#04_电商网站的商品详情页缓存服务业务背景以及框架结构说明)
- [05_基于spring boot快速构建缓存服务以及商品服务](#05_基于spring-boot快速构建缓存服务以及商品服务)
- [06_快速完成缓存服务接收数据变更消息以及调用商品服务接口的代码编写](#06_快速完成缓存服务接收数据变更消息以及调用商品服务接口的代码编写)
- [07_商品服务接口故障导致的高并发访问耗尽缓存服务资源的场景分析](#07_商品服务接口故障导致的高并发访问耗尽缓存服务资源的场景分析)
- [08_基于hystrix的线程池隔离技术进行商品服务接口的资源隔离](#08_基于hystrix的线程池隔离技术进行商品服务接口的资源隔离)
- [09_基于hystrix的信号量技术对地理位置获取逻辑进行资源隔离与限流](#09_基于hystrix的信号量技术对地理位置获取逻辑进行资源隔离与限流)
        - [10_hystrix的线程池+服务+接口划分以及资源池的容量大小控制](#10_hystrix的线程池服务接口划分以及资源池的容量大小控制)
        - [11_深入分析hystrix执行时的8大流程步骤以及内部原理](#11_深入分析hystrix执行时的8大流程步骤以及内部原理)
        - [12_基于request cache请求缓存技术优化批量商品数据查询接口](#12_基于request-cache请求缓存技术优化批量商品数据查询接口)
        - [13_开发品牌名称获取接口的基于本地缓存的fallback降级机制](#13_开发品牌名称获取接口的基于本地缓存的fallback降级机制)
        - [14_深入理解hystrix的短路器执行原理以及模拟接口异常时的短路实验](#14_深入理解hystrix的短路器执行原理以及模拟接口异常时的短路实验)
        - [15_深入理解线程池隔离技术的设计原则以及动手实战接口限流实验](#15_深入理解线程池隔离技术的设计原则以及动手实战接口限流实验)
        - [16_基于timeout机制来为商品服务接口的调用超时提供安全保护](#16_基于timeout机制来为商品服务接口的调用超时提供安全保护)
        - [17_基于hystrix的高可用分布式系统架构项目实战课程的总结](#17_基于hystrix的高可用分布式系统架构项目实战课程的总结)

<!-- /TOC -->


备注：

文章内容来自《亿级流量电商详情页系统实战（第二版）：缓存架构+高可用服务架构+微服务架构》84-99小节

Java工程师面试突击第1季（可能是史上最好的Java面试突击课程）-中华石杉老师\52_如何设计高可用系统架构？限流？熔断？降级？什么鬼！


# 1、简介

就是现在，一般来说，互联网的面试，一般都会考察你，什么是分布式系统，高并发，简单的高可用问题。限流、熔断、降级，在分布式的系统架构中，微服务架构中，其实都是最常见、基础和简单的保障系统高可用的手法。dubbo去开发了，spring cloud去开发了，在这个系统的接口调用中，我们是用hystrix去实现一整套的高可用保障机制，基于hystrix去做了限流、熔断和降级。

hystrix是国外的netflix开源的，netflix是国外很大的视频网站，系统非常复杂，微服务架构，多达几千个服务，为自己的场景，经过大量的工业验证，线上生产环境的实践，产出和开源了高可用相关的一个框架，熔断框架，hystrix。

如何用hystrix做限流、熔断和降级。以及这些都是什么鬼？如何使用hystrix来在你的系统中做开发，加入高可用的保障机制？我之前有一个课程都已经讲解过一整套了，hystrix从入门到精通，全程有动手代码实战开发的。

hystrix未来会成为国内的高可用的限流、熔断和降级这一块的事实上的标准，spring cloud微服务框架，就是集成了hystrix来做微服务架构中的限流、降级和熔断的。

- 3.1 如何设计一个高可用系统？

- 3.2 限流

（1）如何限流？在工作中是怎么做的？说一下具体的实现？

- 3.3 熔断

（1）如何进行熔断？熔断框架都有哪些？具体实现原理知道吗？

- 3.4 降级

（1）如何进行降级？


# 02_hystrix与高可用系统架构：资源隔离+限流+熔断+降级+运维监控

前半部分，专注在高并发这一块，缓存架构，承载高并发，在各种高并发导致的令人崩溃/异常的场景下，运行着

缓存架构，高可用性，在各种系统的各个地方有乱七八糟的异常和故障的情况下，整套缓存系统还能继续健康的run着

HA，HAProxy，主备服务间的切换，这就做到了高可用性，主备实例，多冗余实例，高可用最最基础的东西

什么样的情况下，可能会导致系统的崩溃，以及系统不可用，针对各种各样的一些情况，然后我们用什么技术，去保护整个系统处于高可用的一个情况下

## 1、hystrix是什么？

netflix（国外最大的类似于，爱奇艺，优酷）视频网站，五六年前，也是，感觉自己的系统，整个网站，经常出故障，可用性不太高

有时候一些vip会员不能支付，有时候看视频就卡顿，看不了视频。。。

影响公司的收入。。。

五六年前，netflix，api team，提升高可用性，开发了一个框架，类似于spring，mybatis，hibernate，等等这种框架

高可用性的框架，hystrix

hystrix，框架，提供了高可用相关的各种各样的功能，然后确保说在hystrix的保护下，整个系统可以长期处于高可用的状态，100%，99.99999%

最理想的状况下，软件的故障，就不应该说导致整个系统的崩溃，服务器硬件的一些故障，服务的冗余

唯一有可能导致系统彻底崩溃，就是类似于之前，支付宝的那个事故，工人施工，挖断了电缆，导致几个机房都停电

不可用，和产生一些故障或者bug的区别

## 2、高可用系统架构

资源隔离、限流、熔断、降级、运维监控

- 资源隔离：让你的系统里，某一块东西，在故障的情况下，不会耗尽系统所有的资源，比如线程资源

我实际的项目中的一个case，有一块东西，是要用多线程做一些事情，小伙伴做项目的时候，没有太留神，资源隔离，那块代码，在遇到一些故障的情况下，每个线程在跑的时候，因为那个bug，直接就死循环了，导致那块东西启动了大量的线程，每个线程都死循环

最终导致我的系统资源耗尽，崩溃，不工作，不可用，废掉了

资源隔离，那一块代码，最多最多就是用掉10个线程，不能再多了，就废掉了，限定好的一些资源

- 限流：高并发的流量涌入进来，比如说突然间一秒钟100万QPS，废掉了，10万QPS进入系统，其他90万QPS被拒绝了

- 熔断：系统后端的一些依赖，出了一些故障，比如说mysql挂掉了，每次请求都是报错的，熔断了，后续的请求过来直接不接收了，拒绝访问，10分钟之后再尝试去看看mysql恢复没有

- 降级：mysql挂了，系统发现了，自动降级，从内存里存的少量数据中，去提取一些数据出来

- 运维监控：监控+报警+优化，各种异常的情况，有问题就及时报警，优化一些系统的配置和参数，或者代码

## 3、如何讲解这块内容？

- （1）如何将eshop-cache，核心的缓存服务改造成高可用的架构

- （2）hystrix中的一部分内容，单拉出来，做成一个免费的小课程，作为福利发放出去

- （3）eshop-cache，写代码，eshop-cache-ha，业务场景，跟之前衔接起来，重新去写代码

- （4）hystrix做服务高可用这一块的内容，讲解成只有一个业务背景，重新写代码，独立


eshop-cache，在各级缓存数据都失效的情况下，会重新从源系统中调用接口，依赖源系统去查询mysql数据库去重新获取数据

如果你的各种依赖的服务有了故障，那么很可能会导致你的系统不可用

hystrix对系统进行各种高可用性的系统加固，来应对各种不可用的情况


缓存雪崩那一块去讲解，redis肯定挂，mysql有较大概率挂掉，在风雨飘摇中

我之前做的一个项目，我们多个项目都用了公司里公用的缓存的存储，缓存彻底挂了，雪崩了，导致各种业务系统全部崩溃，崩溃了好几个小时

导致公司损失了大量的资金的损失

其中导致公司损失最大的负责人，受到了很大的处分


# 03_hystrix要解决的分布式系统可用性问题以及其设计原则


![什么是分布式系统以及其中的故障和hystrix](../../pic/2019-06-12-21-06-47.png)

![依赖服务的故障导致服务被拖垮以及故障的蔓延](../../pic/2019-06-12-21-07-08.png)

如果使用三个线程池分别访问C\D\E，即使不适用hystrix做资源隔离可以吗？


![资源隔离如何保护依赖服务的故障不要拖垮整个系统](../../pic/2019-06-12-21-07-32.png)



高可用性这个topic，然后咱们会用几讲的时间来讲解一下如何用hystrix，来构建高可用的服务的架构

咱们会用一个真实的项目背景，作为业务场景，来带出来在这个特定的业务场景下，可能会产生哪些各种各样的可用性的一些问题

针对这些问题，我们用hystrix的解决方案和原理是什么

带着大家，纯手工将所有的服务的高可用架构的代码，全部纯手工自己敲出来

形成高可用服务架构的项目实战的一个课程

## 1、Hystrix是什么？

在分布式系统中，每个服务都可能会调用很多其他服务，被调用的那些服务就是依赖服务，有的时候某些依赖服务出现故障也是很正常的。

Hystrix可以让我们在分布式系统中对服务间的调用进行控制，加入一些调用延迟或者依赖故障的容错机制。

Hystrix通过将依赖服务进行资源隔离，进而组织某个依赖服务出现故障的时候，这种故障在整个系统所有的依赖服务调用中进行蔓延，同时Hystrix还提供故障时的fallback降级机制

总而言之，Hystrix通过这些方法帮助我们提升分布式系统的可用性和稳定性

## 2、Hystrix的历史

hystrix，就是一种高可用保障的一个框架，类似于spring（ioc，mvc），mybatis，activiti，lucene，框架，预先封装好的为了解决某个特定领域的特定问题的一套代码库

框架，用了框架之后，来解决这个领域的特定的问题，就可以大大减少我们的工作量，提升我们的工作质量和工作效率，框架

hystrix，高可用性保障的一个框架

Netflix（可以认为是国外的优酷或者爱奇艺之类的视频网站），API团队从2011年开始做一些提升系统可用性和稳定性的工作，Hystrix就是从那时候开始发展出来的。

在2012年的时候，Hystrix就变得比较成熟和稳定了，Netflix中，除了API团队以外，很多其他的团队都开始使用Hystrix。

时至今日，Netflix中每天都有数十亿次的服务间调用，通过Hystrix框架在进行，而Hystrix也帮助Netflix网站提升了整体的可用性和稳定性

## 3、初步看一看Hystrix的设计原则是什么？

hystrix为了实现高可用性的架构，设计hystrix的时候，一些设计原则是什么？？？

（1）对依赖服务调用时出现的调用延迟和调用失败进行控制和容错保护

（2）在复杂的分布式系统中，阻止某一个依赖服务的故障在整个系统中蔓延，服务A->服务B->服务C，服务C故障了，服务B也故障了，服务A故障了，整套分布式系统全部故障，整体宕机

（3）提供fail-fast（快速失败）和快速恢复的支持

（4）提供fallback优雅降级的支持

（5）支持近实时的监控、报警以及运维操作


- 调用延迟+失败，提供容错
- 阻止故障蔓延
- 快速失败+快速恢复
- 降级
- 监控+报警+运维

完全描述了hystrix的功能，提供整个分布式系统的高可用的架构

## 4、Hystrix要解决的问题是什么？

在复杂的分布式系统架构中，每个服务都有很多的依赖服务，而每个依赖服务都可能会故障

如果服务没有和自己的依赖服务进行隔离，那么可能某一个依赖服务的故障就会拖垮当前这个服务

举例来说，某个服务有30个依赖服务，每个依赖服务的可用性非常高，已经达到了99.99%的高可用性

那么该服务的可用性就是99.99%的30次方，也就是99.7%的可用性

99.7%的可用性就意味着3%的请求可能会失败，因为3%的时间内系统可能出现了故障不可用了

对于1亿次访问来说，3%的请求失败，也就意味着300万次请求会失败，也意味着每个月有2个小时的时间系统是不可用的

在真实生产环境中，可能更加糟糕

上面也就是说，即使你每个依赖服务都是99.99%高可用性，但是一旦你有几十个依赖服务，还是会导致你每个月都有几个小时是不可用的

画图分析说，当某一个依赖服务出现了调用延迟或者调用失败时，为什么会拖垮当前这个服务？以及在分布式系统中，故障是如何快速蔓延的？

## 5、再看Hystrix的更加细节的设计原则是什么？

（1）阻止任何一个依赖服务耗尽所有的资源，比如tomcat中的所有线程资源

（2）避免请求排队和积压，采用限流和fail fast来控制故障

（3）提供fallback降级机制来应对故障

（4）使用资源隔离技术，比如bulkhead（舱壁隔离技术），swimlane（泳道技术），circuit breaker（短路技术），来限制任何一个依赖服务的故障的影响

（5）通过近实时的统计/监控/报警功能，来提高故障发现的速度

（6）通过近实时的属性和配置热修改功能，来提高故障处理和恢复的速度

（7）保护依赖服务调用的所有故障情况，而不仅仅只是网络故障情况

调用这个依赖服务的时候，client调用包有bug，阻塞，等等，依赖服务的各种各样的调用的故障，都可以处理

## 6、Hystrix是如何实现它的目标的？

（1）通过HystrixCommand或者HystrixObservableCommand来封装对外部依赖的访问请求，这个访问请求一般会运行在独立的线程中，资源隔离

（2）对于超出我们设定阈值的服务调用，直接进行超时，不允许其耗费过长时间阻塞住。这个超时时间默认是99.5%的访问时间，但是一般我们可以自己设置一下

（3）为每一个依赖服务维护一个独立的线程池，或者是semaphore，当线程池已满时，直接拒绝对这个服务的调用

（4）对依赖服务的调用的成功次数，失败次数，拒绝次数，超时次数，进行统计

（5）如果对一个依赖服务的调用失败次数超过了一定的阈值，自动进行熔断，在一定时间内对该服务的调用直接降级，一段时间后再自动尝试恢复

（6）当一个服务调用出现失败，被拒绝，超时，短路等异常情况时，自动调用fallback降级机制

（7）对属性和配置的修改提供近实时的支持

画图分析，对依赖进行资源隔离后，如何避免依赖服务调用延迟或失败导致当前服务的故障



# 04_电商网站的商品详情页缓存服务业务背景以及框架结构说明


![小型电商网站的静态化方案](../../pic/2019-08-04-22-38-15.png)


本课程实现的架构为下面的架构

![大型电商网站的详情页系统的架构](../../pic/2019-08-04-22-37-38.png)

缓存服务模块订阅变更的消息，有变更了就去拉去数据。

我们这个课程，基于hystrix，如何来构建高可用的分布式系统的架构，项目实战

模拟真实业务的这么一个小型的项目，来全程贯穿，用这个项目中的业务场景去一个一个的讲解hystrix高可用的每个技术

纯讲hystrix，脱离实际的业务背景，听起来有点枯燥，大家学完了hystrix以后，可能没法完全感受到技术是如何融入我们的项目中的

大背景：电商网站，首页，商品详情页，搜索结果页，广告页，促销活动，购物车，订单系统，库存系统，物流系统

小背景：商品详情页，如何用最快的结果将商品数据填充到一个页面中，然后将页面显示出来

分布式系统：商品详情页，缓存服务，+底层源数据服务，商品信息服务，店铺信息服务，广告信息服务，推荐信息服务，综合起来组成一个分布式的系统

> 1、电商网站的商品详情页系统架构

（1）小型电商网站的商品详情页系统架构（不是我们要讲解的）

（2）大型电商网站的商品详情页系统架构

（3）页面模板

举个例子

将数据动态填充/渲染到一个html模板中，是什么意思呢？

```html
<html>
	<title>#{name}的页面</title>
	<body>
		商品的价格是：#{price}
		商品的介绍：#{description}
	</body>
</html>

```

上面这个就可以认为是一个页面模板，里面的很多内容是不确定的，#{name}，#{price}，#{description}，这都是一些模板脚本，不确定里面的值是什么？

将数据填充/渲染到html模板中，是什么意思呢？

```html
{
	"name": "iphone7 plus（玫瑰金+32G）",
	"price": 5599.50
	"description": "这个手机特别好用。。。。。。"
}

<html>
	<title>iphone7 plus（玫瑰金+32G）的页面</title>
	<body>
		商品的价格是：5599.50
		商品的介绍：这个手机特别好用。。。。。。
	</body>
</html>
```

上面这个就是一份填充好数据的一个html页面

> 2、缓存服务

缓存服务，订阅一个MQ的消息变更，如果有消息变更的话，那么就会发送一个网络请求，调用一个底层的对应的源数据服务的接口，去获取变更后的数据

将获取到的变更后的数据填充到分布式的redis缓存中去

高可用这一块儿，最可能出现说可用性不高的情况，是什么呢？就是说，在接收到消息之后，可能在调用各种底层依赖服务的接口时，会遇到各种不稳定的情况

比如底层服务的接口调用超时，200ms，2s都没有返回; 底层服务的接口调用失败，比如说卡了500ms之后，返回一个报错

在分布式系统中，对于这种大量的底层依赖服务的调用，就可能会出现各种可用性的问题，一旦没有处理好的话

可能就会导致缓存服务自己本身会挂掉，或者故障掉，就会导致什么呢？不可以对外提供服务，严重情况下，甚至会导致说整个商品详情页显示不出来

缓存服务接收到变更消息后，去调用各个底层依赖服务时的高可用架构的实现

我们刚才讲解的整套大型电商网站的商品详情页的缓存架构，完整的那个流程，《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

> 3、框架结构

围绕着缓存服务去拉取各种底层的源数据服务的数据，调用其接口时，可能出现的系统不可用的问题

从简

spring boot，微服务的非常快速，非常好用的技术框架，脱胎于spring，具体的东西就不讲解，直接带着大家上手搭建一个spring boot的框架

2个服务，缓存服务，商品服务，缓存服务依赖于商品服务

模拟各种商品服务可能接口调用时出现的各种问题，导致系统不可用的场景，然后用hystrix完整的各种技术点全部贯穿在里面

解决了一大堆设计业务背景下的系统不可用问题，hystrix整个技术体系，知识体系，也就讲解完了

消息队列，redis，咱们都不搞了

分布式系统，微服务，dubbo，不用dubbo，目前比较明显的一个趋势是，行业里，未来主要还是spring boot，spring cloud，主流的开源技术，去构建微服务的分布式系统

基于dubbo，官方很久之前就停止更新了，支持也不是太好

spring boot + http client + hystrix

> 自己总结

基于spring boot + http client + hystrix框架。场景：缓存服务去拉取各种底层的源数据服务的数据，调用其接口时，可能出现的系统不可用的问题，搭建的一个小项目来学习hystrix搭建高可用。


# 05_基于spring boot快速构建缓存服务以及商品服务


> 1、pom.xml

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.2.5.RELEASE</version>
</parent>

<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<java.version>1.8</java.version>
</properties>

<dependencies>
	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>1.2.2</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.2.8</version>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.1.43</version>
    </dependency>
</dependencies>
	
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>

<repositories>
    <repository>
        <id>spring-milestone</id>
        <url>https://repo.spring.io/libs-release</url>
    </repository>
</repositories>

<pluginRepositories>
    <pluginRepository>
        <id>spring-milestone</id>
        <url>https://repo.spring.io/libs-release</url>
    </pluginRepository>
</pluginRepositories>
```

你实际在你本地去搭建这个工程的时候，你首先就会发现说，你一修改这个pom.xml，发现下载各种spring boot依赖包，下载巨慢巨慢

北京，宽带，100M，联通，还是下载的巨慢

手工下载依赖，并安装到本地maven仓库

（1）在maven中央仓库搜索jar包，如果没有找到，就得手动在百度里面找，下载jar下来

（2）根据jar对应的group id，artifact id，找到自己本地的maven仓库，对应的目录，将jar包拷贝到那个目录里面去

jmxtool，groupId=com.sun.jdmk，artifactId=jmxtools，version=1.2.1
com\sun\jdmk\jmxtools\1.2.1

（3）手工执行mvn install:install-file的命令，在本地仓库中安装这个依赖

mvn install:install-file -Dfile=E:\apache-maven-3.0.5\mvn_repo\com\sun\jdmk\jmxtools\1.2.1\jmxtools-1.2.1.jar -DgroupId=com.sun.jdmk -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar

（4）强制kill掉你的eclipse

（5）重新再进入eclips，这个时候肯定是会报很多的错误的，重新加载maven依赖

（6）反复循环，手工下载了，十几个到二十个依赖，然后最终所有的依赖全部成功下载到了本地，工程部报错

> 2、配置文件（src/main/resources）

Application.properties

```
server.port=8081
spring.datasource.url=jdbc:mysql://192.168.31.85:3306/eshop
spring.datasource.username=eshop
spring.datasource.password=eshop
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

说明：我已经在一个虚拟机中，安装好了一个mysql数据库，大家需要自己在自己本地安装一个mysql，配置好对应的url连接串，还有对应的用户名和密码就可以了

怎么安装mysql，大家自己网上查一下吧，java工程师，大学的学生，自己在本地安装一个mysql还是可以搞定的吧

mybatis/UserMappper.xml

templates/hello.html

> 3、Application

```java
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@MapperScan("com.roncoo.eshop.cache.mapper")
public class Application {
 
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }
    
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
 
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}
```

> 4、HelloController

> 5、完成两个服务的构建


> 自己总结

本节就是搭建基础项目


# 06_快速完成缓存服务接收数据变更消息以及调用商品服务接口的代码编写

快速将核心的功能流程，用代码来实现

从下一讲开始，然后我们其实就针对这里面的一些东西，来给大家讲解哪些地方可能会有可用性的问题，如何用hystrix来解决这些可用性的问题

1、接收数据变更的消息，订阅一个MQ的topic，但是我们这里就简化一下，采取提供一个http接口

2、往http接口发送一条消息，就认为是通知缓存服务，有一个商品的数据变更了

```xml
<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpclient</artifactId>
	<version>4.4</version>
</dependency>
```

```java
/**
 * HttpClient工具类
 * @author lixuerui
 *
 */
@SuppressWarnings("deprecation")
public class HttpClientUtils {
	
	/**
	 * 发送GET请求
	 * @param url 请求URL
	 * @return 响应结果
	 */
	@SuppressWarnings("resource")
	public static String sendGetRequest(String url) {
		String httpResponse = null;
		
		HttpClient httpclient = null;
		InputStream is = null;
		BufferedReader br = null;
		
		try {
			// 发送GET请求
			httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);  
			HttpResponse response = httpclient.execute(httpget);
			
			// 处理响应
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				is = entity.getContent();
				br = new BufferedReader(new InputStreamReader(is));      
				
		        StringBuffer buffer = new StringBuffer("");       
		        String line = null;   
		        
		        while ((line = br.readLine()) != null) {  
		        		buffer.append(line + "\n");      
	            }  
	    
		        httpResponse = buffer.toString();      
			}
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {
			try {
				if(br != null) {
					br.close();
				}
				if(is != null) {
					is.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();  
			}
		}
		  
		return httpResponse;
	}
	
	/**
	 * 发送post请求
	 * @param url URL
	 * @param map 参数Map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static String sendPostRequest(String url, Map<String,String> map){  
		HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        
        try{  
            httpClient = new DefaultHttpClient();  
            httpPost = new HttpPost(url);  
            
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");    
                httpPost.setEntity(entity);  
            }  
            
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity, "utf-8");    
                }  
            }  
        } catch(Exception ex){  
            ex.printStackTrace();  
        } finally {
        	
        }
        
        return result;  
    }  
	
}
```

3、缓存服务接收到这条消息之后，就会去通过http调用商品服务的一个接口，获取到商品变更后的最新数据


> 自己总结

抽象简化业务逻辑。把缓存服务订阅一个MQ有变更消息后去拉去对应消息的数据更新自己。这里简化为通过调用一个http接口发送一条消息到缓存服务，然后缓存服务通过调用对应的一个http商品服务接口更新数据。






# 07_商品服务接口故障导致的高并发访问耗尽缓存服务资源的场景分析

![商品服务接口导致缓存服务资源耗尽的问题](../../pic/2019-08-04-23-04-04.png)

1、商品服务接口调用故障，导致缓存服务资源耗尽

2、hystrix针对一个一个的具体的业务场景，去开发高可用的架构



# 08_基于hystrix的线程池隔离技术进行商品服务接口的资源隔离

> 1、pom.xml

```xml
<dependency>
    <groupId>com.netflix.hystrix</groupId>
    <artifactId>hystrix-core</artifactId>
    <version>1.5.12</version>
</dependency>
```

> 2、将商品服务接口调用的逻辑进行封装

hystrix进行资源隔离，其实是提供了一个抽象，叫做command，就是说，你如果要把对某一个依赖服务的所有调用请求，全部隔离在同一份资源池内

对这个依赖服务的所有调用请求，全部走这个资源池内的资源，不会去用其他的资源了，这个就叫做资源隔离

hystrix最最基本的资源隔离的技术，线程池隔离技术

对某一个依赖服务，商品服务，所有的调用请求，全部隔离到一个线程池内，对商品服务的每次调用请求都封装在一个command里面

每个command（每次服务调用请求）都是使用线程池内的一个线程去执行的

所以哪怕是对这个依赖服务，商品服务，现在同时发起的调用量已经到了1000了，但是线程池内就10个线程，最多就只会用这10个线程去执行

不会说，对商品服务的请求，因为接口调用延迟，将tomcat内部所有的线程资源全部耗尽，不会出现了

```java
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        return "Hello " + name + "!";
    }

}
```

不让超出这个量的请求去执行了，保护说，不要因为某一个依赖服务的故障，导致耗尽了缓存服务中的所有的线程资源去执行

> 3、开发一个支持批量商品变更的接口

HystrixCommand：是用来获取一条数据的

HystrixObservableCommand：是设计用来获取多条数据的

```java
public class ObservableCommandHelloWorld extends HystrixObservableCommand<String> {

    private final String name;

    public ObservableCommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext("Hello " + name + "!");
                        observer.onNext("Hi " + name + "!");
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
         } ).subscribeOn(Schedulers.io());
    }
}
```

> 4、command的四种调用方式

- 同步：new CommandHelloWorld("World").execute()，new ObservableCommandHelloWorld("World").toBlocking().toFuture().get()

如果你认为observable command只会返回一条数据，那么可以调用上面的模式，去同步执行，返回一条数据

- 异步：new CommandHelloWorld("World").queue()，new ObservableCommandHelloWorld("World").toBlocking().toFuture()

对command调用queue()，仅仅将command放入线程池的一个等待队列，就立即返回，拿到一个Future对象，后面可以做一些其他的事情，然后过一段时间对future调用get()方法获取数据

- observe()：hot，已经执行过了

- toObservable(): cold，还没执行过


```java


Observable<String> fWorld = new CommandHelloWorld("World").observe();

assertEquals("Hello World!", fWorld.toBlocking().single());

fWorld.subscribe(new Observer<String>() {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(String v) {
        System.out.println("onNext: " + v);
    }

});

Observable<String> fWorld = new ObservableCommandHelloWorld("World").toObservable();

assertEquals("Hello World!", fWorld.toBlocking().single());

fWorld.subscribe(new Observer<String>() {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(String v) {
        System.out.println("onNext: " + v);
    }

});
```

> 5、如何解决刚才的问题

画图讲解资源隔离后的效果

![资源隔离生效的讲解](../../pic/2019-08-04-23-11-31.png)


# 09_基于hystrix的信号量技术对地理位置获取逻辑进行资源隔离与限流

> 1、线程池隔离技术与信号量隔离技术的区别

hystrix里面，核心的一项功能，其实就是所谓的资源隔离，要解决的最最核心的问题，就是将多个依赖服务的调用分别隔离到各自自己的资源池内

避免说对某一个依赖服务的调用，因为依赖服务的接口调用的延迟或者失败，导致服务所有的线程资源全部耗费在这个服务的接口调用上

一旦说某个服务的线程资源全部耗尽的话，可能就导致服务就会崩溃，甚至说这种故障会不断蔓延

hystrix，资源隔离，两种技术，线程池的资源隔离，信号量的资源隔离

信号量，semaphore

信号量跟线程池，两种资源隔离的技术，区别到底在哪儿呢？

2、线程池隔离技术和信号量隔离技术，分别在什么样的场景下去使用呢？？

线程池：适合绝大多数的场景，99%的，线程池，对依赖服务的网络请求的调用和访问，timeout这种问题

信号量：适合，你的访问不是对外部依赖的访问，而是对内部的一些比较复杂的业务逻辑的访问，但是像这种访问，系统内部的代码，其实不涉及任何的网络请求，那么只要做信号量的普通限流就可以了，因为不需要去捕获timeout类似的问题，算法+数据结构的效率不是太高，并发量突然太高，因为这里稍微耗时一些，导致很多线程卡在这里的话，不太好，所以进行一个基本的资源隔离和访问，避免内部复杂的低效率的代码，导致大量的线程被hang住

3、在代码中加入从本地内存获取地理位置数据的逻辑

业务背景里面， 比较适合信号量的是什么场景呢？

比如说，我们一般来说，缓存服务，可能会将部分量特别少，访问又特别频繁的一些数据，放在自己的纯内存中

一般我们在获取到商品数据之后，都要去获取商品是属于哪个地理位置，省，市，卖家的，可能在自己的纯内存中，比如就一个Map去获取

对于这种直接访问本地内存的逻辑，比较适合用信号量做一下简单的隔离

优点在于，不用自己管理线程池拉，不用care timeout超时了，信号量做隔离的话，性能会相对来说高一些

4、采用信号量技术对地理位置获取逻辑进行资源隔离与限流

super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
               .withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)));


《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案


### 10_hystrix的线程池+服务+接口划分以及资源池的容量大小控制


资源隔离，两种策略，线程池隔离，信号量隔离

对资源隔离这一块东西，做稍微更加深入一些的讲解，告诉你，除了可以选择隔离策略以外，对你选择的隔离策略，可以做一定的细粒度的一些控制

1、execution.isolation.strategy

指定了HystrixCommand.run()的资源隔离策略，THREAD或者SEMAPHORE，一种是基于线程池，一种是信号量

线程池机制，每个command运行在一个线程中，限流是通过线程池的大小来控制的

信号量机制，command是运行在调用线程中，但是通过信号量的容量来进行限流

如何在线程池和信号量之间做选择？

默认的策略就是线程池

线程池其实最大的好处就是对于网络访问请求，如果有超时的话，可以避免调用线程阻塞住

而使用信号量的场景，通常是针对超大并发量的场景下，每个服务实例每秒都几百的QPS，那么此时你用线程池的话，线程一般不会太多，可能撑不住那么高的并发，如果要撑住，可能要耗费大量的线程资源，那么就是用信号量，来进行限流保护

一般用信号量常见于那种基于纯内存的一些业务逻辑服务，而不涉及到任何网络访问请求

netflix有100+的command运行在40+的线程池中，只有少数command是不运行在线程池中的，就是从纯内存中获取一些元数据，或者是对多个command包装起来的facacde command，是用信号量限流的

// to use thread isolation
HystrixCommandProperties.Setter()
   .withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)
// to use semaphore isolation
HystrixCommandProperties.Setter()
   .withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)

2、command名称和command组

线程池隔离，依赖服务->接口->线程池，如何来划分

你的每个command，都可以设置一个自己的名称，同时可以设置一个自己的组

private static final Setter cachedSetter = 
    Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
        .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"));    

public CommandHelloWorld(String name) {
    super(cachedSetter);
    this.name = name;
}

command group，是一个非常重要的概念，默认情况下，因为就是通过command group来定义一个线程池的，而且还会通过command group来聚合一些监控和报警信息

同一个command group中的请求，都会进入同一个线程池中

3、command线程池

threadpool key代表了一个HystrixThreadPool，用来进行统一监控，统计，缓存

默认的threadpool key就是command group名称

每个command都会跟它的threadpool key对应的thread pool绑定在一起

如果不想直接用command group，也可以手动设置thread pool name

public CommandHelloWorld(String name) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool")));
    this.name = name;
}

command threadpool -> command group -> command key

command key，代表了一类command，一般来说，代表了底层的依赖服务的一个接口

command group，代表了某一个底层的依赖服务，合理，一个依赖服务可能会暴露出来多个接口，每个接口就是一个command key

command group，在逻辑上去组织起来一堆command key的调用，统计信息，成功次数，timeout超时次数，失败次数，可以看到某一个服务整体的一些访问情况

command group，一般来说，推荐是根据一个服务去划分出一个线程池，command key默认都是属于同一个线程池的

比如说你以一个服务为粒度，估算出来这个服务每秒的所有接口加起来的整体QPS在100左右

你调用那个服务的当前服务，部署了10个服务实例，每个服务实例上，其实用这个command group对应这个服务，给一个线程池，量大概在10个左右，就可以了，你对整个服务的整体的访问QPS大概在每秒100左右

一般来说，command group是用来在逻辑上组合一堆command的

举个例子，对于一个服务中的某个功能模块来说，希望将这个功能模块内的所有command放在一个group中，那么在监控和报警的时候可以放一起看

command group，对应了一个服务，但是这个服务暴露出来的几个接口，访问量很不一样，差异非常之大

你可能就希望在这个服务command group内部，包含的对应多个接口的command key，做一些细粒度的资源隔离

对同一个服务的不同接口，都使用不同的线程池

command key -> command group

command key -> 自己的threadpool key

逻辑上来说，多个command key属于一个command group，在做统计的时候，会放在一起统计

每个command key有自己的线程池，每个接口有自己的线程池，去做资源隔离和限流

但是对于thread pool资源隔离来说，可能是希望能够拆分的更加一致一些，比如在一个功能模块内，对不同的请求可以使用不同的thread pool

command group一般来说，可以是对应一个服务，多个command key对应这个服务的多个接口，多个接口的调用共享同一个线程池

如果说你的command key，要用自己的线程池，可以定义自己的threadpool key，就ok了

4、coreSize

设置线程池的大小，默认是10

HystrixThreadPoolProperties.Setter()
   .withCoreSize(int value)

一般来说，用这个默认的10个线程大小就够了

5、queueSizeRejectionThreshold

控制queue满后reject的threshold，因为maxQueueSize不允许热修改，因此提供这个参数可以热修改，控制队列的最大大小

HystrixCommand在提交到线程池之前，其实会先进入一个队列中，这个队列满了之后，才会reject

默认值是5

HystrixThreadPoolProperties.Setter()
   .withQueueSizeRejectionThreshold(int value)

6、execution.isolation.semaphore.maxConcurrentRequests

设置使用SEMAPHORE隔离策略的时候，允许访问的最大并发量，超过这个最大并发量，请求直接被reject

这个并发量的设置，跟线程池大小的设置，应该是类似的，但是基于信号量的话，性能会好很多，而且hystrix框架本身的开销会小很多

默认值是10，设置的小一些，否则因为信号量是基于调用线程去执行command的，而且不能从timeout中抽离，因此一旦设置的太大，而且有延时发生，可能瞬间导致tomcat本身的线程资源本占满

HystrixCommandProperties.Setter()
   .withExecutionIsolationSemaphoreMaxConcurrentRequests(int value)




《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案


### 11_深入分析hystrix执行时的8大流程步骤以及内部原理

之前几讲，我们用实际的业务背景给了一些可用性的问题

然后借着那些最最基础的可用性的问题，然后讲解了hystrix最基本的支持高可用的技术，资源隔离+限流

创建command，执行这个command，配置这个command对应的group和线程池，以及线程池/信号量的容量和大小

我们要去讲解一下，你开始执行这个command，调用了这个command的execute()方法以后，hystrix内部的底层的执行流程和步骤以及原理是什么呢？

在讲解这个流程的过程中，我们会带出来hystrix其他的一些核心以及重要的功能

画图分析整个8大步骤的流程，然后再对每个步骤进行细致的讲解

1、构建一个HystrixCommand或者HystrixObservableCommand

一个HystrixCommand或一个HystrixObservableCommand对象，代表了对某个依赖服务发起的一次请求或者调用

构造的时候，可以在构造函数中传入任何需要的参数

HystrixCommand主要用于仅仅会返回一个结果的调用
HystrixObservableCommand主要用于可能会返回多条结果的调用

HystrixCommand command = new HystrixCommand(arg1, arg2);
HystrixObservableCommand command = new HystrixObservableCommand(arg1, arg2);

2、调用command的执行方法

执行Command就可以发起一次对依赖服务的调用

要执行Command，需要在4个方法中选择其中的一个：execute()，queue()，observe()，toObservable()

其中execute()和queue()仅仅对HystrixCommand适用

execute()：调用后直接block住，属于同步调用，直到依赖服务返回单条结果，或者抛出异常
queue()：返回一个Future，属于异步调用，后面可以通过Future获取单条结果
observe()：订阅一个Observable对象，Observable代表的是依赖服务返回的结果，获取到一个那个代表结果的Observable对象的拷贝对象
toObservable()：返回一个Observable对象，如果我们订阅这个对象，就会执行command并且获取返回结果

K             value   = command.execute();
Future<K>     fValue  = command.queue();
Observable<K> ohValue = command.observe();         
Observable<K> ocValue = command.toObservable();    

execute()实际上会调用queue().get().queue()，接着会调用toObservable().toBlocking().toFuture()

也就是说，无论是哪种执行command的方式，最终都是依赖toObservable()去执行的

3、检查是否开启缓存

从这一步开始，进入我们的底层的运行原理啦，了解hysrix的一些更加高级的功能和特性

如果这个command开启了请求缓存，request cache，而且这个调用的结果在缓存中存在，那么直接从缓存中返回结果

4、检查是否开启了短路器

检查这个command对应的依赖服务是否开启了短路器

如果断路器被打开了，那么hystrix就不会执行这个command，而是直接去执行fallback降级机制

5、检查线程池/队列/semaphore是否已经满了

如果command对应的线程池/队列/semaphore已经满了，那么也不会执行command，而是直接去调用fallback降级机制

6、执行command

调用HystrixObservableCommand.construct()或HystrixCommand.run()来实际执行这个command

HystrixCommand.run()是返回一个单条结果，或者抛出一个异常
HystrixObservableCommand.construct()是返回一个Observable对象，可以获取多条结果

如果HystrixCommand.run()或HystrixObservableCommand.construct()的执行，超过了timeout时长的话，那么command所在的线程就会抛出一个TimeoutException

如果timeout了，也会去执行fallback降级机制，而且就不会管run()或construct()返回的值了

这里要注意的一点是，我们是不可能终止掉一个调用严重延迟的依赖服务的线程的，只能说给你抛出来一个TimeoutException，但是还是可能会因为严重延迟的调用线程占满整个线程池的

即使这个时候新来的流量都被限流了。。。

如果没有timeout的话，那么就会拿到一些调用依赖服务获取到的结果，然后hystrix会做一些logging记录和metric统计

7、短路健康检查

Hystrix会将每一个依赖服务的调用成功，失败，拒绝，超时，等事件，都会发送给circuit breaker断路器

短路器就会对调用成功/失败/拒绝/超时等事件的次数进行统计

短路器会根据这些统计次数来决定，是否要进行短路，如果打开了短路器，那么在一段时间内就会直接短路，然后如果在之后第一次检查发现调用成功了，就关闭断路器

8、调用fallback降级机制

在以下几种情况中，hystrix会调用fallback降级机制：run()或construct()抛出一个异常，短路器打开，线程池/队列/semaphore满了，command执行超时了

一般在降级机制中，都建议给出一些默认的返回值，比如静态的一些代码逻辑，或者从内存中的缓存中提取一些数据，尽量在这里不要再进行网络请求了

即使在降级中，一定要进行网络调用，也应该将那个调用放在一个HystrixCommand中，进行隔离

在HystrixCommand中，上线getFallback()方法，可以提供降级机制

在HystirxObservableCommand中，实现一个resumeWithFallback()方法，返回一个Observable对象，可以提供降级结果

如果fallback返回了结果，那么hystrix就会返回这个结果

对于HystrixCommand，会返回一个Observable对象，其中会发返回对应的结果
对于HystrixObservableCommand，会返回一个原始的Observable对象

如果没有实现fallback，或者是fallback抛出了异常，Hystrix会返回一个Observable，但是不会返回任何数据

不同的command执行方式，其fallback为空或者异常时的返回结果不同

对于execute()，直接抛出异常
对于queue()，返回一个Future，调用get()时抛出异常
对于observe()，返回一个Observable对象，但是调用subscribe()方法订阅它时，理解抛出调用者的onError方法
对于toObservable()，返回一个Observable对象，但是调用subscribe()方法订阅它时，理解抛出调用者的onError方法

9、不同的执行方式

execute()，获取一个Future.get()，然后拿到单个结果
queue()，返回一个Future
observer()，立即订阅Observable，然后启动8大执行步骤，返回一个拷贝的Observable，订阅时理解回调给你结果
toObservable()，返回一个原始的Observable，必须手动订阅才会去执行8大步骤




《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案






### 12_基于request cache请求缓存技术优化批量商品数据查询接口


我们上一讲讲解的那个图片，顺着那个图片的流程，来一个一个的讲解hystrix的核心技术

1、创建command，2种command类型
2、执行command，4种执行方式
3、查找是否开启了request cache，是否有请求缓存，如果有缓存，直接取用缓存，返回结果

首先，有一个概念，叫做reqeust context，请求上下文，一般来说，在一个web应用中，hystrix

我们会在一个filter里面，对每一个请求都施加一个请求上下文，就是说，tomcat容器内，每一次请求，就是一次请求上下文

然后在这次请求上下文中，我们会去执行N多代码，调用N多依赖服务，有的依赖服务可能还会调用好几次

在一次请求上下文中，如果有多个command，参数都是一样的，调用的接口也是一样的，其实结果可以认为也是一样的

那么这个时候，我们就可以让第一次command执行，返回的结果，被缓存在内存中，然后这个请求上下文中，后续的其他对这个依赖的调用全部从内存中取用缓存结果就可以了

不用在一次请求上下文中反复多次的执行一样的command，提升整个请求的性能




HystrixCommand和HystrixObservableCommand都可以指定一个缓存key，然后hystrix会自动进行缓存，接着在同一个request context内，再次访问的时候，就会直接取用缓存

用请求缓存，可以避免重复执行网络请求

多次调用一个command，那么只会执行一次，后面都是直接取缓存

对于请求缓存（request caching），请求合并（request collapsing），请求日志（request log），等等技术，都必须自己管理HystrixReuqestContext的声明周期

在一个请求执行之前，都必须先初始化一个request context

HystrixRequestContext context = HystrixRequestContext.initializeContext();

然后在请求结束之后，需要关闭request context

context.shutdown();

一般来说，在java web来的应用中，都是通过filter过滤器来实现的

public class HystrixRequestContextServletFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
     throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            chain.doFilter(request, response);
        } finally {
            context.shutdown();
        }
    }
}

@Bean
public FilterRegistrationBean indexFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean(new IndexFilter());
    registration.addUrlPatterns("/");
    return registration;
}

结合咱们的业务背景，我们做了一个批量查询商品数据的接口，在这个里面，我们其实通过HystrixObservableCommand一次性批量查询多个商品id的数据

但是这里有个问题，如果说nginx在本地缓存失效了，重新获取一批缓存，传递过来的productId都没有进行去重，1,1,2,2,5,6,7

那么可能说，商品id出现了重复，如果按照我们之前的业务逻辑，可能就会重复对productId=1的商品查询两次，productId=2的商品查询两次

我们对批量查询商品数据的接口，可以用request cache做一个优化，就是说一次请求，就是一次request context，对相同的商品查询只能执行一次，其余的都走request cache

public class CommandUsingRequestCache extends HystrixCommand<Boolean> {

    private final int value;

    protected CommandUsingRequestCache(int value) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.value = value;
    }

    @Override
    protected Boolean run() {
        return value == 0 || value % 2 == 0;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(value);
    }

}

@Test
public void testWithCacheHits() {
    HystrixRequestContext context = HystrixRequestContext.initializeContext();
    try {
        CommandUsingRequestCache command2a = new CommandUsingRequestCache(2);
        CommandUsingRequestCache command2b = new CommandUsingRequestCache(2);

        assertTrue(command2a.execute());
        // this is the first time we've executed this command with
        // the value of "2" so it should not be from cache
        assertFalse(command2a.isResponseFromCache());

        assertTrue(command2b.execute());
        // this is the second time we've executed this command with
        // the same value so it should return from cache
        assertTrue(command2b.isResponseFromCache());
    } finally {
        context.shutdown();
    }

    // start a new request context
    context = HystrixRequestContext.initializeContext();
    try {
        CommandUsingRequestCache command3b = new CommandUsingRequestCache(2);
        assertTrue(command3b.execute());
        // this is a new request context so this 
        // should not come from cache
        assertFalse(command3b.isResponseFromCache());
    } finally {
        context.shutdown();
    }
}

缓存的手动清理

public static class GetterCommand extends HystrixCommand<String> {

    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("GetterCommand");
    private final int id;

    public GetterCommand(int id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetSetGet"))
                .andCommandKey(GETTER_KEY));
        this.id = id;
    }

    @Override
    protected String run() {
        return prefixStoredOnRemoteDataStore + id;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    /**
     * Allow the cache to be flushed for this object.
     * 
     * @param id
     *            argument that would normally be passed to the command
     */
    public static void flushCache(int id) {
        HystrixRequestCache.getInstance(GETTER_KEY,
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }

}

public static class SetterCommand extends HystrixCommand<Void> {

    private final int id;
    private final String prefix;

    public SetterCommand(int id, String prefix) {
        super(HystrixCommandGroupKey.Factory.asKey("GetSetGet"));
        this.id = id;
        this.prefix = prefix;
    }

    @Override
    protected Void run() {
        // persist the value against the datastore
        prefixStoredOnRemoteDataStore = prefix;
        // flush the cache
        GetterCommand.flushCache(id);
        // no return value
        return null;
    }
}

《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案


### 13_开发品牌名称获取接口的基于本地缓存的fallback降级机制


1、创建command
2、执行command
3、request cache
4、短路器，如果打开了，fallback降级机制

1、fallback降级机制

hystrix调用各种接口，或者访问外部依赖，mysql，redis，zookeeper，kafka，等等，如果出现了任何异常的情况

比如说报错了，访问mysql报错，redis报错，zookeeper报错，kafka报错，error

对每个外部依赖，无论是服务接口，中间件，资源隔离，对外部依赖只能用一定量的资源去访问，线程池/信号量，如果资源池已满，reject

访问外部依赖的时候，访问时间过长，可能就会导致超时，报一个TimeoutException异常，timeout

上述三种情况，都是我们说的异常情况，对外部依赖的东西访问的时候出现了异常，发送异常事件到短路器中去进行统计

如果短路器发现异常事件的占比达到了一定的比例，直接开启短路，circuit breaker

上述四种情况，都会去调用fallback降级机制

fallback，降级机制，你之前都是必须去调用外部的依赖接口，或者从mysql中去查询数据的，但是为了避免说可能外部依赖会有故障

比如，你可以再内存中维护一个ehcache，作为一个纯内存的基于LRU自动清理的缓存，数据也可以放入缓存内

如果说外部依赖有异常，fallback这里，直接尝试从ehcache中获取数据

比如说，本来你是从mysql，redis，或者其他任何地方去获取数据的，获取调用其他服务的接口的，结果人家故障了，人家挂了，fallback，可以返回一个默认值

两种最经典的降级机制：纯内存数据，默认值

run()抛出异常，超时，线程池或信号量满了，或短路了，都会调用fallback机制

给大家举个例子，比如说我们现在有个商品数据，brandId，品牌，一般来说，假设，正常的逻辑，拿到了一个商品数据以后，用brandId再调用一次请求，到其他的服务去获取品牌的最新名称

假如说，那个品牌服务挂掉了，那么我们可以尝试本地内存中，会保留一份时间比较过期的一份品牌数据，有些品牌没有，有些品牌的名称过期了，Nike++，Nike

调用品牌服务失败了，fallback降级就从本地内存中获取一份过期的数据，先凑合着用着

public class CommandHelloFailure extends HystrixCommand<String> {

    private final String name;

    public CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }

}

@Test
public void testSynchronous() {
    assertEquals("Hello Failure World!", new CommandHelloFailure("World").execute());
}

HystrixObservableCommand，是实现resumeWithFallback方法

2、fallback.isolation.semaphore.maxConcurrentRequests

这个参数设置了HystrixCommand.getFallback()最大允许的并发请求数量，默认值是10，也是通过semaphore信号量的机制去限流

如果超出了这个最大值，那么直接被reject

HystrixCommandProperties.Setter()
   .withFallbackIsolationSemaphoreMaxConcurrentRequests(int value)

《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案


### 14_深入理解hystrix的短路器执行原理以及模拟接口异常时的短路实验

短路器深入的工作原理

1、如果经过短路器的流量超过了一定的阈值，HystrixCommandProperties.circuitBreakerRequestVolumeThreshold()

举个例子，可能看起来是这样子的，要求在10s内，经过短路器的流量必须达到20个；在10s内，经过短路器的流量才10个，那么根本不会去判断要不要短路

2、如果断路器统计到的异常调用的占比超过了一定的阈值，HystrixCommandProperties.circuitBreakerErrorThresholdPercentage()

如果达到了上面的要求，比如说在10s内，经过短路器的流量（你，只要执行一个command，这个请求就一定会经过短路器），达到了30个；同时其中异常的访问数量，占到了一定的比例，比如说60%的请求都是异常（报错，timeout，reject），会开启短路

3、然后断路器从close状态转换到open状态

4、断路器打开的时候，所有经过该断路器的请求全部被短路，不调用后端服务，直接走fallback降级

5、经过了一段时间之后，HystrixCommandProperties.circuitBreakerSleepWindowInMilliseconds()，会half-open，让一条请求经过短路器，看能不能正常调用。如果调用成功了，那么就自动恢复，转到close状态

短路器，会自动恢复的，half-open，半开状态

6、circuit breaker短路器的配置

（1）circuitBreaker.enabled

控制短路器是否允许工作，包括跟踪依赖服务调用的健康状况，以及对异常情况过多时是否允许触发短路，默认是true

HystrixCommandProperties.Setter()
   .withCircuitBreakerEnabled(boolean value)

（2）circuitBreaker.requestVolumeThreshold

设置一个rolling window，滑动窗口中，最少要有多少个请求时，才触发开启短路

举例来说，如果设置为20（默认值），那么在一个10秒的滑动窗口内，如果只有19个请求，即使这19个请求都是异常的，也是不会触发开启短路器的

HystrixCommandProperties.Setter()
   .withCircuitBreakerRequestVolumeThreshold(int value)

（3）circuitBreaker.sleepWindowInMilliseconds

设置在短路之后，需要在多长时间内直接reject请求，然后在这段时间之后，再重新导holf-open状态，尝试允许请求通过以及自动恢复，默认值是5000毫秒

HystrixCommandProperties.Setter()
   .withCircuitBreakerSleepWindowInMilliseconds(int value)

（4）circuitBreaker.errorThresholdPercentage

设置异常请求量的百分比，当异常请求达到这个百分比时，就触发打开短路器，默认是50，也就是50%

HystrixCommandProperties.Setter()
   .withCircuitBreakerErrorThresholdPercentage(int value)

（5）circuitBreaker.forceOpen

如果设置为true的话，直接强迫打开短路器，相当于是手动短路了，手动降级，默认false

HystrixCommandProperties.Setter()
   .withCircuitBreakerForceOpen(boolean value)

（6）circuitBreaker.forceClosed

如果设置为ture的话，直接强迫关闭短路器，相当于是手动停止短路了，手动升级，默认false

HystrixCommandProperties.Setter()
   .withCircuitBreakerForceClosed(boolean value)

7、实战演练

配置一个断路器，流量要求是20，异常比例是50%，短路时间是5s

在command内加入一个判断，如果是productId=-1，那么就直接报错，触发异常执行

写一个client测试程序，写入50个请求，前20个是正常的，但是后30个是productId=-1，然后继续请求，会发现

《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案


### 15_深入理解线程池隔离技术的设计原则以及动手实战接口限流实验


1、command的创建和执行：资源隔离
2、request cache：请求缓存
3、fallback：优雅降级
4、circuit breaker：短路器，快速熔断（一旦后端服务故障，立刻熔断，阻止对其的访问）

把一个分布式系统中的某一个服务，打造成一个高可用的服务

资源隔离，优雅降级，熔断

5、判断，线程池或者信号量的容量是否已满，reject，限流

限流，限制对后端的服务的访问量，比如说你对mysql，redis，zookeeper，各种后端的中间件的资源，访问，其实为了避免过大的流浪打死后端的服务，线程池，信号量，限流

限制服务对后端的资源的访问

1、线程池隔离技术的设计原则

Hystrix采取了bulkhead舱壁隔离技术，来将外部依赖进行资源隔离，进而避免任何外部依赖的故障导致本服务崩溃

线程池隔离，学术名称：bulkhead，舱壁隔离

外部依赖的调用在单独的线程中执行，这样就能跟调用线程隔离开来，避免外部依赖调用timeout耗时过长，导致调用线程被卡死

Hystrix对每个外部依赖用一个单独的线程池，这样的话，如果对那个外部依赖调用延迟很严重，最多就是耗尽那个依赖自己的线程池而已，不会影响其他的依赖调用

Hystrix选择用线程池机制来进行资源隔离，要面对的场景如下：

（1）每个服务都会调用几十个后端依赖服务，那些后端依赖服务通常是由很多不同的团队开发的
（2）每个后端依赖服务都会提供它自己的client调用库，比如说用thrift的话，就会提供对应的thrift依赖
（3）client调用库随时会变更
（4）client调用库随时可能会增加新的网络请求的逻辑
（5）client调用库可能会包含诸如自动重试，数据解析，内存中缓存等逻辑
（6）client调用库一般都对调用者来说是个黑盒，包括实现细节，网络访问，默认配置，等等
（7）在真实的生产环境中，经常会出现调用者，突然间惊讶的发现，client调用库发生了某些变化
（8）即使client调用库没有改变，依赖服务本身可能有会发生逻辑上的变化
（9）有些依赖的client调用库可能还会拉取其他的依赖库，而且可能那些依赖库配置的不正确
（10）大多数网络请求都是同步调用的
（11）调用失败和延迟，也有可能会发生在client调用库本身的代码中，不一定就是发生在网络请求中

简单来说，就是你必须默认client调用库就很不靠谱，而且随时可能各种变化，所以就要用强制隔离的方式来确保任何服务的故障不能影响当前服务

我不知道在学习这个课程的学员里，有多少人，真正参与过一些复杂的分布式系统的开发，不是说一个team，你们五六个人，七八个人，去做的

在一些大公司里，做一些复杂的项目的话，广告计费系统，特别复杂，可能涉及多个团队，总共三四十个人，五六十个人，一起去开发一个系统，每个团队负责一块儿

每个团队里的每个人，负责一个服务，或者几个服务，比较常见的大公司的复杂分布式系统项目的分工合作的一个流程

线程池机制的优点如下：

（1）任何一个依赖服务都可以被隔离在自己的线程池内，即使自己的线程池资源填满了，也不会影响任何其他的服务调用
（2）服务可以随时引入一个新的依赖服务，因为即使这个新的依赖服务有问题，也不会影响其他任何服务的调用
（3）当一个故障的依赖服务重新变好的时候，可以通过清理掉线程池，瞬间恢复该服务的调用，而如果是tomcat线程池被占满，再恢复就很麻烦
（4）如果一个client调用库配置有问题，线程池的健康状况随时会报告，比如成功/失败/拒绝/超时的次数统计，然后可以近实时热修改依赖服务的调用配置，而不用停机
（5）如果一个服务本身发生了修改，需要重新调整配置，此时线程池的健康状况也可以随时发现，比如成功/失败/拒绝/超时的次数统计，然后可以近实时热修改依赖服务的调用配置，而不用停机
（6）基于线程池的异步本质，可以在同步的调用之上，构建一层异步调用层

简单来说，最大的好处，就是资源隔离，确保说，任何一个依赖服务故障，不会拖垮当前的这个服务

线程池机制的缺点：

（1）线程池机制最大的缺点就是增加了cpu的开销

除了tomcat本身的调用线程之外，还有hystrix自己管理的线程池

（2）每个command的执行都依托一个独立的线程，会进行排队，调度，还有上下文切换
（3）Hystrix官方自己做了一个多线程异步带来的额外开销，通过对比多线程异步调用+同步调用得出，Netflix API每天通过hystrix执行10亿次调用，每个服务实例有40个以上的线程池，每个线程池有10个左右的线程
（4）最后发现说，用hystrix的额外开销，就是给请求带来了3ms左右的延时，最多延时在10ms以内，相比于可用性和稳定性的提升，这是可以接受的


我们可以用hystrix semaphore技术来实现对某个依赖服务的并发访问量的限制，而不是通过线程池/队列的大小来限制流量

sempahore技术可以用来限流和削峰，但是不能用来对调研延迟的服务进行timeout和隔离

execution.isolation.strategy，设置为SEMAPHORE，那么hystrix就会用semaphore机制来替代线程池机制，来对依赖服务的访问进行限流

如果通过semaphore调用的时候，底层的网络调用延迟很严重，那么是无法timeout的，只能一直block住

一旦请求数量超过了semephore限定的数量之后，就会立即开启限流

2、接口限流实验

假设，一个线程池，大小是15个，队列大小是10个，timeout时长设置的长一些，5s

模拟发送请求，然后写死代码，在command内部做一个sleep，比如每次sleep 1s，10个请求发送过去以后，直接被hang死，线程池占满

再发送请求，就会堵塞在缓冲队列，queue，10个，20个，10个，后10个应该就直接reject，fallback逻辑

15 + 10 = 25个请求，15在执行，10个缓冲在队列里了，剩下的流量全部被reject，限流，降级

withCoreSize：设置你的线程池的大小
withMaxQueueSize：设置的是你的等待队列，缓冲队列的大小
withQueueSizeRejectionThreshold：如果withMaxQueueSize<withQueueSizeRejectionThreshold，那么取的是withMaxQueueSize，反之，取得是withQueueSizeRejectionThreshold

线程池本身的大小，如果你不设置另外两个queue相关的参数，等待队列是关闭的

queue大小，等待队列的大小，timeout时长

先进去线程池的是10个请求，然后有8个请求进入等待队列，线程池里有空闲，等待队列中的请求如果还没有timeout，那么就进去线程池去执行

10 + 8 = 18个请求之外，7个请求，直接会被reject掉，限流，fallback

withExecutionTimeoutInMilliseconds(20000)：timeout也设置大一些，否则如果请求放等待队列中时间太长了，直接就会timeout，等不到去线程池里执行了
withFallbackIsolationSemaphoreMaxConcurrentRequests(30)：fallback，sempahore限流，30个，避免太多的请求同时调用fallback被拒绝访问

《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案


### 16_基于timeout机制来为商品服务接口的调用超时提供安全保护

一般来说，在调用依赖服务的接口的时候，比较常见的一个问题，就是超时

超时是在一个复杂的分布式系统中，导致不稳定，或者系统抖动，或者出现说大量超时，线程资源hang死，吞吐量大幅度下降，甚至服务崩溃

超时最大的一个问题

你去调用各种各样的依赖服务，特别是在大公司，你甚至都不认识开发一个服务的人，你都不知道那个人的水平怎么样，不了解

比尔盖茨说过一句话，在互联网的另外一头，你都不知道甚至坐着一条狗

分布式系统，大公司，多个团队，大型协作，服务是谁的，不了解，很可能说那个哥儿们，实习生都有可能

在一个复杂的系统里，可能你的依赖接口的性能很不稳定，有时候2ms，200ms，2s

如果你不对各种依赖接口的调用，做超时的控制，来给你的服务提供安全保护措施，那么很可能你的服务就被各种垃圾的依赖服务的性能给拖死了

大量的接口调用很慢，大量线程就卡死了，资源隔离，线程池的线程卡死了，超时的控制

（1）execution.isolation.thread.timeoutInMilliseconds

手动设置timeout时长，一个command运行超出这个时间，就被认为是timeout，然后将hystrix command标识为timeout，同时执行fallback降级逻辑

默认是1000，也就是1000毫秒

HystrixCommandProperties.Setter()
   .withExecutionTimeoutInMilliseconds(int value)

（2）execution.timeout.enabled

控制是否要打开timeout机制，默认是true

HystrixCommandProperties.Setter()
   .withExecutionTimeoutEnabled(boolean value)

让一个command执行timeout，然后看是否会调用fallback降级

《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、架构性能优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案



### 17_基于hystrix的高可用分布式系统架构项目实战课程的总结




已经学到的东西

hystrix的核心知识

1、hystrix内部工作原理：8大执行步骤和流程
2、资源隔离：你如果有很多个依赖服务，高可用性，先做资源隔离，任何一个依赖服务的故障不会导致你的服务的资源耗尽，不会崩溃
3、请求缓存：对于一个request context内的多个相同command，使用request cache，提升性能
4、熔断：基于短路器，采集各种异常事件，报错，超时，reject，短路，熔断，一定时间范围内就不允许访问了，直接降级，自动恢复的机制
5、降级：报错，超时，reject，熔断，降级，服务提供容错的机制
6、限流：在你的服务里面，通过线程池，或者信号量，限制对某个后端的服务或资源的访问量，避免从你的服务这里过去太多的流量，打死某个资源
7、超时：避免某个依赖服务性能过差，导致大量的线程hang住去调用那个服务，会导致你的服务本身性能也比较差

学会了这些东西以后，我们特意设置了大电商背景，商品详情页系统，缓存服务的业务场景，尽量的去结合一些仿真的业务，去学习hystrix的各项技术

这个东西做起来没那么容易，尽量做了，学习效果更好一些，兴趣也会更好一些

已经可以快速利用hystrix给自己开发的服务增加各种高可用的保障措施了，避免你的系统因为各种各样的异常情况导致崩溃，不可用

hystrix的高阶知识

1、request collapser，请求合并技术
2、fail-fast和fail-slient，高阶容错模式
3、static fallback和stubbed fallback，高阶降级模式
4、嵌套command实现的发送网络请求的降级模式
5、基于facade command的多级降级模式
6、request cache的手动清理
7、生产环境中的线程池大小以及timeout配置优化经验
8、线程池的自动化动态扩容与缩容技术
9、hystrix的metric高阶配置
10、基于hystrix dashboard的可视化分布式系统监控
11、生产环境中的hystrix工程运维经验

《亿级流量电商详情页系统的大型高并发与高可用缓存架构实战》

1、亿级流量的电商网站的商品详情页系统架构
2、大型的企业级缓存架构，支撑高并发与高可用
3、几十万QPS的高并发+99.99%高可用+1T以上的海量数据+绝对数据安全的redis集群架构
4、高并发场景下的数据库+缓存双写一致性保障方案
5、大缓存的维度化拆分方案
6、基于双层nginx部署架构的缓存命中率提升方案
7、基于kafka+spring boot+ehcache+redis+nginx+lua的多级缓存架构
8、基于zookeeper的缓存并发更新安全保障方案
9、基于storm+zookeeper的大规模缓存预热解决方案
10、基于storm+zookeeper+nginx+lua的热点缓存自动降级与恢复解决方案
11、基于hystrix的高可用缓存服务架构
12、hystrix的进阶高可用架构方案、配置优化以及监控运维
13、基于hystrix的大规模缓存雪崩解决方案
14、高并发场景下的缓存穿透解决方案
15、高并发场景下的缓存失效解决方案











































