---
title: Spring设计思想---AOP设计基本原理
categories: 
- spring
tags:
---


Spring设计思想---AOP设计基本原理
AOP(Aspect Oriented Programming)

[参考](https://blog.csdn.net/luanlouis/article/details/51095702)



目录
1.Java程序运行在JVM中的特征
2.Java程序的执行流【了解AOP、连接点(Join Point)、切入点(point cut)   的概念 】
3.引入了代理模式的Java程序执行流(AOP实现的机制)
4.Spring AOP的工作原理


# 1. Java程序运行在JVM中的特征
当我们在某个类Foo中写好了一个main()方法，然后执行java Foo，你的Java程序之旅就开启了，如下：

    public class Foo {
        public static void main(String[] args) {
            // your codes begins here
        }
     
    }
那么在这个执行的过程中，JVM都为你干了什么呢？
当你执行java Foo 的时候，JVM会创建一个主线程main，这个主线程以上述的main()方法作为入口，开始执行你的代码。
每一个线程在内存中都会维护一个属于自己的栈(Stack),记录着整个程序执行的过程。栈里的每一个元素称为栈帧(Stack Frame)，
栈帧表示着某个方法调用，会记录方法调用的信息；实际上我们在代码中调用一个方法的时候，在内存中就对应着一个栈帧的入栈和出栈。

在某个特定的时间点，一个Main线程内的栈会呈现如下图所示的情况：
<div align="center"> <img src="/images/SpringAOP-1-1.png"/> </div><br>

从线程栈的角度来看，我们可以看到，[JVM处理Java程序的基本单位是方法调用]。实际上，JVM执行的最基本单位的指令(即原子操作)是汇编语言性质的机器字节码。
这里之所以讲方法调用时Java程序的基本执行单位，是从更宏观的角度看待的。

如何获取到虚拟机线程栈中的内容(即方法调用过程)？
试想一下，如何能够获取到JVM线程栈中的方法调用的内容？ 我相信所有的Java programmer都知道这个答案。
Java Programmer几乎每天都能看到它------当我们的代码抛出异常而未捕获或者运行时出现了Error错误时，我们会受到一个非常讨厌的Log信息，如下：
<div align="center"> <img src="/images/SpringAOP-1-2.png"/> </div><br>

当然，除了代码抛出异常外，我们还是可以其他方式察觉JVM线程栈内的内容。可以通过Thread.dumpStack()方法创建一个假的Exception实例，
然后将这个Exception实例记录的当前线程栈的内容输出到标准错误流中。例如我在某处代码里执行了Thread.dumpStack()方法，输出了如下的结果：
<div align="center"> <img src="/images/SpringAOP-1-3.png"/> </div><br>


# 2.Java程序执行流 【了解AOP、连接点(Join Point)、切入点(point cut)的概念 】
如果从虚拟机线程栈的角度考虑Java程序执行的话，那么，你会发现，真个程序运行的过程就是方法调用的过程。我们按照方法执行的顺序，
将方法调用排成一串，这样就构成了Java程序流。我们将上述的线程栈里的方法调用按照执行流排列，会有如下类似的图：
<div align="center"> <img src="/images/SpringAOP-1-4.png"/> </div><br>

基于时间序列，我们可以将方法调用排成一条线。而每个方法调用则可以看成Java执行流中的一个节点。这个节点在AOP的术语中，被称为[Join Point]，即连接点。 
一个Java程序的运行的过程，就是若干个连接点连接起来依次执行的过程。
在我们正常的面向对象的思维中，我们考虑的是如何按照时间序列通过方法调用来实现我们的业务逻辑。那么，什么是AOP（即面向切面的编程）呢？
通常面向对象的程序，代码都是按照时间序列纵向展开的，而他们都有一个共性：即都是以方法调用作为基本执行单位展开的。 将方法调用当做一个连接点，
那么由连接点串起来的程序执行流就是整个程序的执行过程。
AOP(Aspect Oriented Programming)则是从另外一个角度来考虑整个程序的，AOP将每一个方法调用，即连接点作为编程的入口，针对方法调用进行编程。
从执行的逻辑上来看，相当于在之前纵向的按照时间轴执行的程序横向切入。相当于将之前的程序横向切割成若干的面，即Aspect.每个面被称为切面。
所以，根据我的理解，[AOP本质上是针对方法调用的编程思路]
<div align="center"> <img src="/images/SpringAOP-1-5.png"/> </div><br>

既然AOP是针对切面进行的编程的，那么，你需要选择哪些切面(即 连接点Joint Point)作为你的编程对象呢？
因为切面本质上是每一个方法调用，选择切面的过程实际上就是选择方法的过程。那么，被选择的切面(Aspect)在AOP术语里被称为切入点([Point Cut]).  
切入点实际上也是从所有的连接点(Join point)挑选自己感兴趣的连接点的过程。
<div align="center"> <img src="/images/SpringAOP-1-6.png"/> </div><br>

Spring AOP框架中通过 方法匹配表达式来表示切入点(Point Cut)，至于详细的表达式语法是什么 不是本文的重点，请读者自行参考Spring相应的说明文档。
既然AOP是针对方法调用(连接点)的编程， 现在又选取了你感兴趣的自己感兴趣的链接点---切入点（Point Cut）了，那么，AOP能对它做什么类型的编程呢？AOP能做什么呢？ 
了解这个之前，我们先要知道一个非常重要的问题： 既然AOP是对方法调用进行的编程，那么，[AOP如何捕获方法调用的呢]？
 弄清楚这个问题，你不得不了解设计模式中的代理模式了。下面我们先来了解一下引入了代理模式的Java程序执行流是什么样子的。

# 3.引入了代理模式的Java程序执行流(AOP实现的机制)
我们假设在我们的Java代码里，都为实例对象通过代理模式创建了代理对象，访问这些实例对象必须要通过代理，
那么，加入了proxy对象的Java程序执行流会变得稍微复杂起来。我们来看下加入了proxy对象后，Java程序执行流的示意图：
<div align="center"> <img src="/images/SpringAOP-1-7.png"/> </div><br>

由上图可以看出，只要想调用某一个实例对象的方法时，都会经过这个实例对象相对应的代理对象， 即执行的控制权先交给代理对象。

关于代理模式
代理模式属于Java代码中经常用到的、也是比较重要的设计模式。代理模式可以为某些对象除了实现本身的功能外，提供一些额外的功能，大致作用如下图所示：
<div align="center"> <img src="/images/SpringAOP-1-8.png"/> </div><br>

关于代理模式的详细介绍和分析，参考：[Java动态代理机制详解（JDK 和CGLIB，Javassist，ASM）]

加入了代理模式的Java程序执行流，使得所有的方法调用都经过了代理对象。对于Spring AOP框架而言，它负责控制着真个容器内部的代理对象。
当我们调用了某一个实例对象的任何一个非final的public方法时，整个Spring框架都会知晓。
此时的SpringAOP框架在某种程度上扮演着一个上帝的角色：它知道你在这个框架内所做的任何操作，你对每一个实例对象的非final的public方法调用都可以被框架察觉到！
<div align="center"> <img src="/images/SpringAOP-1-9.png"/> </div><br>

既然Spring代理层可以察觉到你所做的每一次对实例对象的方法调用，那么，Spring就有机会在这个代理的过程中插入Spring的自己的业务代码。



# 4.Spring AOP的工作原理
前面已经介绍了AOP编程首先要选择它感兴趣的连接点----即切入点(Point cut)，那么，AOP能对切入点做什么样的编程呢？ 
我们先将代理模式下的某个连接点细化，你会看到如下这个示意图所表示的过程：
<div align="center"> <img src="/images/SpringAOP-1-10.png"/> </div><br>

为了降低我们对Spring的AOP的理解难度，我在这里将代理角色的职能进行了简化，方便大家理解。
（注意：真实的Spring AOP的proxy角色扮演的只能比这复杂的多，这里只是简化，方便大家理解，请不要先入为主）代理模式的代理角色最起码要考虑三个阶段：
1. 在调用真正对象的方法之前，应该需要做什么？
2. 在调用真正对象的方法过程中，如果抛出了异常，需要做什么？
3.在调用真正对象的方法后，返回了结果了，需要做什么？
AOP对这个方法调用的编程，就是针对这三个阶段插入自己的业务代码。

现在我们假设当前RealSubject这个角色的类是 org.luanlouis.springlearning.aop.FooService ，当前这个连接点对应的方法签名是：public void foo()。
那么上述的代理对象的三个阶段将会有以下的处理逻辑：            
1. 在调用真正对象的方法之前，
proxy会告诉Spring AOP:  "我将要调用类org.luanlouis.springlearning.aop.FooService  的public void foo() ，在调用之前，你有什么处理建议吗？";
Spring AOP这时根据proxy提供的类名和方法签名,然后拿这些信息尝试匹配是否在其感兴趣的切入点内，如果在感兴趣的切入点内，
Spring AOP会返回 MethodBeforeAdvice处理建议，告诉proxy应该执行的操作；

2. 在调用真正对象的方法过程中，如果抛出了异常，需要做什么？
proxy告诉Spring AOP: “我调用类org.luanlouis.springlearning.aop.FooService  的public void foo()过程中抛出了异常，你有什么处理建议？”
Spring AOP根据proxy提供的类型和方法签名，确定了在其感兴趣的切入点内，则返回相应的处理建议ThrowsAdvice，告诉proxy这个时期应该采取的操作。

3.在调用真正对象的方法后，返回了结果了，需要做什么？
proxy告诉Spring AOP:"我调用类org.luanlouis.springlearning.aop.FooService  的public void foo()结束了，并返回了结果你现在有什么处理建议？"；
Spring AOP 根据proxy提供的类型名和方法签名，确定了在其感兴趣的切入点内，则返回AfterReturingAdivce处理建议，proxy得到这个处理建议，然后执行建议；
<div align="center"> <img src="/images/SpringAOP-1-11.png"/> </div><br>

上述的示意图中已经明确表明了Spring AOP应该做什么样的工作：根据proxy提供的特定类的特定方法执行的特定时期阶段给出相应的处理建议。
要完成该工作，Spring AOP应该实现：
1.确定自己对什么类的什么方法感兴趣？ -----即确定 AOP的切入点（Point Cut），这个可以通过切入点(Point Cut)表达式来完成；
2. 对应的的类的方法的执行特定时期给出什么处理建议？------这个需要Spring AOP提供相应的建议 ，即我们常说的Advice。
<div align="center"> <img src="/images/SpringAOP-1-12.png"/> </div><br>

到此为止，AOP的基本工作机制已经介绍完毕了，我再次强调，上午我将Proxy方法的不同执行时期简单的拆成了三个，是为了方便大家理解AOP的工作机制，
实际的AOP proxy的实现比这复杂的多。
